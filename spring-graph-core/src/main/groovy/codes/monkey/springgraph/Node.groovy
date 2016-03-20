package codes.monkey.springgraph

import org.springframework.beans.factory.config.BeanDefinition

/**
 * Created by jzietsman on 3/18/16.
 */
class Node {
    int id, ctxPosition
    String name, clazz, ctxName
    List<Node> dependsOn, dependantNodes = []
    BeanDefinition beanDefinition


    String getSimpleName() {
        "ctx${ctxPosition}_${sanitize(name)}"
    }

    private String sanitize(String s) {
        s.replaceAll('.*\\.', '').replaceAll('[^\\w]+', '_')
    }

    @Override
    public String toString() {
        name
    }
}
