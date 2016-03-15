'use strict';

/**
 * @ngdoc function
 * @name springGraphUiApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the springGraphUiApp
 */
angular.module('springGraphUiApp')
  .controller('MainCtrl', function ($http) {

    $http.get('api/vis.json').then(function(response){
      var container = document.getElementById('graph');
      var data = {
        nodes: new vis.DataSet(response.data.nodes),
        edges: new vis.DataSet(response.data.edges)
      };
      var network = new vis.Network(container, data, {});
    });

    this.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
