import {Component, View} from 'angular2/core';
import {bootstrap} from 'angular2/platform/browser';
import {SpringGraphUi} from 'spring-graph-ui';

@Component({
  selector: 'main'
})

@View({
  directives: [SpringGraphUi],
  template: `
    <spring-graph-ui></spring-graph-ui>
  `
})

class Main {

}

bootstrap(Main);
