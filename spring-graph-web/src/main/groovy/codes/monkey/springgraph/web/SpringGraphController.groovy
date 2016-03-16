package codes.monkey.springgraph.web

import codes.monkey.springgraph.GraphBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import static codes.monkey.springgraph.GraphBuilder.FILTER_CONNECTED_ONLY

/**
 * Created by jzietsman on 3/10/16.
 */
@Controller
@RequestMapping(['/spring-graph', '/spring-graph-ui'])
class SpringGraphController {

    GraphBuilder builder

    @Autowired
    SpringGraphController(GraphBuilder builder) {
        this.builder = builder
    }

    @RequestMapping(path = '/graph', produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    byte[] graph() {
        builder.graphVizGraph('png', [FILTER_CONNECTED_ONLY])
    }

    @RequestMapping(path = '/api/test')
    @ResponseBody
    Map api() {
        [hello: 'world']
    }

    @RequestMapping(path = '/api/vis')
    @ResponseBody
    Map vis() {
        builder.toVisMap([FILTER_CONNECTED_ONLY])
    }
}
