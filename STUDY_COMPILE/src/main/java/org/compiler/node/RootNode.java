package org.compiler.node;

import java.util.ArrayList;
import java.util.List;

public class RootNode extends NodeContainer {

    public RootNode() {
        this(new ArrayList<>());
    }

    public RootNode(List<Node> body) {
        super(NodeType.Root, body);
    }
}
