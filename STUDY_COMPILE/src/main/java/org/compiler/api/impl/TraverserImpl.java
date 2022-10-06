package org.compiler.api.impl;

import org.compiler.api.Traverser;
import org.compiler.node.Node;
import org.compiler.node.NodeContainer;

import java.util.function.Consumer;

public class TraverserImpl implements Traverser {

    @Override
    public void traverse(Node node, Consumer<Node> visitor) {
        if (node instanceof NodeContainer) {
            traverseContainer((NodeContainer) node, visitor);
        } else {
            traverseNode(node, visitor);
        }
    }


    public void traverseNode(Node root, Consumer<Node> visitor) {
        visitor.accept(root);
    }

    public void traverseContainer(NodeContainer nodeContainer, Consumer<Node> visitor) {
        visitor.accept(nodeContainer);
        for (Node node : nodeContainer.getBody()) {
            switch (node.getType()) {
                case Root, CallExpression -> traverseContainer((NodeContainer) node, visitor);
                case Literal -> traverseNode(node, visitor);
            }
        }
    }

}
