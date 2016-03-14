package codes.monkey

import codes.monkey.springgraph.GraphBuilder
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class SpringGraphTestApplication {

	@Bean
	public static GraphBuilder graphBuilder(){
		new GraphBuilder()
	}

	static void main(String[] args) {
		SpringApplication.run SpringGraphTestApplication, args
	}
}
