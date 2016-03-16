package codes.monkey.springgraph

import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

/**
 * Created by jzietsman on 3/10/16.
 */
class GraphBuilder implements ApplicationContextAware, BeanFactoryPostProcessor {

    @Lazy
    Map registry = { buildGraph() }()

    ApplicationContext applicationContext
    ConfigurableListableBeanFactory listableBeanFactory

    @Override
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext
    }

    @Override
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.listableBeanFactory = beanFactory
    }

    static FILTER_CONNECTED_ONLY = {String key, Node value ->
        value.dependsOn.size() > 0 || value.dependantNodes.size() > 0
    }

    String toDOTString() {
        toDOTString([])
    }

    String toDOTString(List<Closure> filters) {
        def targets = filters.inject(registry){p, current -> p.findAll(current)}
        def gnodes = targets.values().findAll { it }.collect { node ->
            "${node.dotName} -> {${node.dependsOn.collect { it?.dotName }.join(' ')}};"

        }
        def str = """|digraph {
           |rankdir=LR;
           |${gnodes.join('\n')}
           |}""".stripMargin()
        str
    }

    Map toVisMap(List<Closure> filters) {

        def targets = filters.inject(registry){p, current -> p.findAll(current)}.values()
        [
                nodes: targets.collect{[id: it.id, label: it.dotName]},
                edges: targets.collect{Node node -> node.dependsOn.collect{[from:node.id, to: it.id, arrows:'to']}}.flatten()
        ]
    }

    def buildGraph() {
        def nodeRegistry = [:]
        def register
        def id = 0
        register = { name ->
            if (nodeRegistry.containsKey(name)) return nodeRegistry[name]
            if (!listableBeanFactory.containsBeanDefinition(name)) {
                nodeRegistry[name] = new Node(name: name)
                return nodeRegistry[name]
            }
            BeanDefinition beanDefinition = listableBeanFactory.getBeanDefinition(name)
            Node node = new Node(
                    id: id++,
                    name: name,
                    clazz: beanDefinition.beanClassName,
                    beanDefinition: beanDefinition
            )
            nodeRegistry[name] = node
            node
        }
        applicationContext.beanDefinitionNames.collect(register).each { Node node ->
            node.dependsOn = [] + listableBeanFactory.getDependenciesForBean(node.name).collect { nodeRegistry[it] }.findAll{it}
            node.dependantNodes = [] + listableBeanFactory.getDependentBeans(node.name).collect { nodeRegistry[it] }.findAll{it}
        }
        nodeRegistry
    }

    byte[] graphVizGraph(String format) {
        graphVizGraph(format, [])
    }
    byte[] graphVizGraph(String format, List<Closure> filters) {

        def graphBytes = null
        File.createTempFile('graphviz', '.dot').with {
            write toDOTString(filters)
            def output = "$parent/${name[0..-5]}-graph.$format"
            println absolutePath
            println output
            def command = "dot -T$format $absolutePath -o $output"
            command.execute().waitForOrKill(1000)
            new File(output).with {
                graphBytes = bytes
                delete()
            }
            delete()
        }
        graphBytes
    }

    static class Node {
        int id
        String name, clazz, loadedFrom
        List<Node> dependsOn, dependantNodes = []
        BeanDefinition beanDefinition


        String getDotName() {
            name.replaceAll('.*\\.', '').replaceAll('[^\\w]+', '_' )
        }

        @Override
        public String toString() {
            name
        }
    }

}
