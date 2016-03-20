package codes.monkey.springgraph

/**
 * Created by jzietsman on 3/18/16.
 */
class Filters {

    static FILTER_CONNECTED_ONLY = { String key, Node value ->
        value.dependsOn.size() > 0 || value.dependantNodes.size() > 0
    }

    static NOOP = {true}
}
