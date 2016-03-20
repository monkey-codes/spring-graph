package codes.monkey.springgraph.web

import codes.monkey.springgraph.Formats
import codes.monkey.springgraph.GraphBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

import static codes.monkey.springgraph.Filters.FILTER_CONNECTED_ONLY
import static codes.monkey.springgraph.Formats.PNG
import static codes.monkey.springgraph.Formats.SVG

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

    @RequestMapping(path = '/image/png', produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    byte[] png() {
        builder.format(PNG, [FILTER_CONNECTED_ONLY])
    }

    @RequestMapping(path = '/image/svg', produces = 'image/svg+xml')
    @ResponseBody
    byte[] svg() {
        builder.format(SVG, [FILTER_CONNECTED_ONLY])
    }


    @RequestMapping(path = '/api/{format}')
    @ResponseBody
    <T> T api(@PathVariable String format) {
        def fmt = Formats[format.toUpperCase()]
        builder.format(fmt, [FILTER_CONNECTED_ONLY])
    }

}
