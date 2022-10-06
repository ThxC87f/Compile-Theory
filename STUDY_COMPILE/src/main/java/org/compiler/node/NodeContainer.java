package org.compiler.node;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class NodeContainer extends Node {

    final List<Node> body;

    public NodeContainer(NodeType type, List<Node> body) {
        super(type);
        this.body = body;
    }

    public void addBodyNode(Node node) {
        body.add(node);
    }

}
