package codes.monkey.springgraph

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

/**
 * Created by jzietsman on 3/10/16.
 */
@ContextConfiguration(classes = GraphBuilderSpec.TestConfig.class)
class GraphBuilderSpec extends Specification {

    @Autowired
    GraphBuilder graphBuilder

    def "it should provide a byte[] of the graph in given format"(){
        expect:
        graphBuilder.graphVizGraph('png').size() > 0
    }

    def "it should build the graph"() {
        expect:
        !graphBuilder.registry.isEmpty()
    }

    def "it should spit out the graph in DOT format"(){
        expect:
        graphBuilder.toDOTString() != null

    }

    def "it should provide only connected filter"(){
        when:
        def result = graphBuilder.toDOTString([GraphBuilder.FILTER_CONNECTED_ONLY])

        then:
        result.contains('beanThree')
        !result.contains('beanFour')

    }

    @Configuration
    public static class TestConfig {

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
        BeanThree beanThree() {
            new BeanThree()
        }

        @Bean
        BeanTwo beanTwo(BeanThree beanThree) {
            new BeanTwo(beanThree: beanThree)
        }

        @Bean
        BeanOne beanOne(BeanTwo beanTwo) {
            new BeanOne(beanTwo: beanTwo)
        }

        @Bean
        BeanFour beanFour() {
            new BeanFour()
        }
    }

    static class BeanOne {
        BeanTwo beanTwo

    }

    static class BeanTwo {
        BeanThree beanThree

    }

    static class BeanThree {

    }

    static class BeanFour {

    }


}
