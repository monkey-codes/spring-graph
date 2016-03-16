'use strict';


angular.module('springGraphUiApp')
.directive('graph', function GraphDirective($http, GraphOptionFactory) {
  return {
    link:function(scope,element){
      $http.get('api/vis.json').then(function(response){
        var container = element.find('#graph').get(0);
        function shapeTypes(node){
          node.shape = 'box';
          return node;
        }
        var data = {
          nodes: new vis.DataSet(response.data.nodes.map(shapeTypes)),
          edges: new vis.DataSet(response.data.edges)
        };
        var options = GraphOptionFactory.get();
        var network = new vis.Network(container, data,options );
      });


    },
    templateUrl: 'views/templates/graph.html'
  };

})
.factory('GraphOptionFactory', function GraphOptionFactory(){
  var options = {
    "configure": {
      "enabled": false,
      "filter": [
        "nodes",
        "edges",
        "layout",
        "interaction",
        "manipulation",
        "physics",
        "global"
      ]
    },
    "edges": {
      "shadow": true,
      "smooth": {
        "forceDirection": "none"
      }
    },
    "nodes": {
      "shadow": true
    },
    "physics": {
      "barnesHut": {
        "springLength": 610,
        "springConstant": 0.085,
        "avoidOverlap": 0.53
      },
      "minVelocity": 0.75,
      "timestep": 0.8
    }
  };
  return {
    get: function get(){return options;}
  }
});


