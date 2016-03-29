package codes.monkey.springgraph

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Created by jzietsman on 3/21/16.
 */
@Configuration
@ComponentScan('codes.monkey.springgraph.web')
class SpringGraphAutoConfiguration {

    @Bean
    public static GraphBuilder graphBuilder(){
        new GraphBuilder()
    }



}
