package org.compiler.api;

import org.compiler.node.Node;

import java.util.function.Consumer;

public interface Traverser {

    void traverse(Node root, Consumer<Node> visitor);
}
