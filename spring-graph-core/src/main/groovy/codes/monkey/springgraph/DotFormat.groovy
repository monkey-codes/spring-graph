package codes.monkey.springgraph

/**
 * Created by jzietsman on 3/18/16.
 */
class DotFormat implements Format<String> {

    @Override
    String apply(List<Node> targets) {
        def gnodes = targets.findAll { it }.collect { node ->
            "${node.simpleName} -> {${node.dependsOn.collect { it.simpleName }.join(' ')}};"
        }
        """|digraph {
           |rankdir=LR;
           |${gnodes.join('\n')}
           |}""".stripMargin()
    }

}
