import {Component, View} from 'angular2/core';

@Component({
  selector: 'spring-graph-ui'
})

@View({
  templateUrl: 'spring-graph-ui.html'
})

export class SpringGraphUi {

  constructor() {
    console.info('SpringGraphUi Component Mounted Successfully');
  }

}
