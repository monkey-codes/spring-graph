package codes.monkey.springgraph
/**
 * Created by jzietsman on 3/18/16.
 */
class VisFormat implements Format<Map> {

    @Override
    Map apply(List<Node> targets) {
        [
                nodes: targets.collect { [id: it.id, label: it.simpleName] },
                edges: targets.collect { Node node ->
                    node.dependsOn.collect {
                        [from: node.id, to: it.id, arrows: 'to']
                    }
                }.flatten()
        ]
    }
}
