package codes.monkey.springgraph

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.ContextHierarchy
import spock.lang.Specification

/**
 * Created by jzietsman on 3/10/16.
 */
@ContextHierarchy([
        @ContextConfiguration(classes = GraphBuilderSpec.TestParentConfig.class),
        @ContextConfiguration(classes = GraphBuilderSpec.TestChildConfig.class)
])

class GraphBuilderSpec extends Specification {

    @Autowired
    GraphBuilder graphBuilder

    def "it should provide a byte[] of the graph in given format"() {
        expect:
        graphBuilder.graphVizGraph('png').size() > 0
    }

    def "it should build the graph"() {
        expect:
        !graphBuilder.registry.isEmpty()
    }

    def "it should spit out the graph in DOT format"() {
        expect:
        graphBuilder.toDOTString() != null

    }

    def "it should provide only connected filter"() {
        when:
        def result = graphBuilder.toDOTString([GraphBuilder.FILTER_CONNECTED_ONLY])

        then:
        result.contains('beanThree')
        result.contains('ctx1_beanOne -> {ctx1_beanTwo ctx0_parentBeanTwo}')
        !result.contains('beanFour')

    }

    def "it should create vis json map"() {
        when:
        def result = graphBuilder.toVisMap([GraphBuilder.FILTER_CONNECTED_ONLY])

        then:
        result.nodes.size > 0
        result.edges.size > 0

    }

    @Configuration
    public static class TestParentConfig {


        @Bean
        public ParentBeanOne parentBeanOne() {
            new ParentBeanOne()
        }

        @Bean
        public ParentBeanTwo parentBeanTwo(ParentBeanOne parentBeanOne) {
            new ParentBeanTwo(parentBeanOne: parentBeanOne)
        }

    }


    @Configuration
    public static class TestChildConfig {

        /**
         * A static @Bean method can be called by the container without requiring
         * the instantiation of its declaring @Configuration class. This is
         * particularly useful when dealing with BeanFactoryPostProcessor beans,
         * as they can interfere with the standard post-processing lifecycle
         * necessary to handle @Autowired, @Inject, @Value, @PostConstruct and
         * other annotations.
         */
        @Bean
        public static GraphBuilder graphBuilder() {
            new GraphBuilder()
        }

        @Bean
        public BeanThree beanThree() {
            new BeanThree()
        }

        @Bean
        public BeanTwo beanTwo(BeanThree beanThree) {
            new BeanTwo(beanThree: beanThree)
        }

        @Bean
        public BeanOne beanOne(BeanTwo beanTwo, ParentBeanTwo parentBeanTwo) {
            new BeanOne(beanTwo: beanTwo,
            parentBeanTwo: parentBeanTwo)
        }

        @Bean
        public  BeanFour beanFour() {
            new BeanFour()
        }
    }

    static class BeanOne {
        BeanTwo beanTwo
        ParentBeanTwo parentBeanTwo
    }

    static class BeanTwo {
        BeanThree beanThree
    }

    static class BeanThree {
    }

    static class BeanFour {

    }

    static class ParentBeanOne {

    }

    static class ParentBeanTwo {
        ParentBeanOne parentBeanOne
    }


}
