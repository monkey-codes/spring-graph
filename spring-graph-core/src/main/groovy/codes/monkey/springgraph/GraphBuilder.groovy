package codes.monkey.springgraph

import org.springframework.beans.BeansException
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

/**
 * Created by jzietsman on 3/10/16.
 */
class GraphBuilder implements ApplicationContextAware {

    @Lazy
    Map registry = { buildGraph().asImmutable() }()

    List<ApplicationContext> applicationContexts = []

    @Override
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        def runUp
        runUp = { ctx ->
            applicationContexts << ctx
            if (ctx.parent) runUp(ctx.parent)
        }
        runUp(applicationContext)
        this.applicationContexts = this.applicationContexts.reverse()
    }


    public <T> T format(Format<T> format, List<Closure> filters){
        def targets = filters.inject(registry) { p, current -> p.findAll(current) }.values()
        format.apply(targets as List<Node>)
    }


    def buildGraph() {
        def nodeRegistry = [:]
        def register
        def id = 0
        register = { ApplicationContext ctx,  name ->
            def listableBeanFactory = (ConfigurableListableBeanFactory) ctx.autowireCapableBeanFactory
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
        applicationContexts.eachWithIndex { ApplicationContext applicationContext, ctxIndex ->

            applicationContext.beanDefinitionNames.collect(register.curry(applicationContext)).each { Node node ->
                def listableBeanFactory = (ConfigurableListableBeanFactory) applicationContext.autowireCapableBeanFactory
                node.ctxPosition = ctxIndex // 0 root
                node.ctxName = applicationContext.displayName
                node.dependsOn = [] + listableBeanFactory.getDependenciesForBean(node.name).collect {
                    nodeRegistry[it]
                }.findAll {
                    it
                }
                node.dependantNodes = [] + listableBeanFactory.getDependentBeans(node.name).collect {
                    nodeRegistry[it]
                }.findAll {
                    it
                }
            }
        }
        nodeRegistry
    }

}
