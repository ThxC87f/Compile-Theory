package org.compiler.node;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CallExpressionNode extends NodeContainer {
    final String name;

    public CallExpressionNode(String name) {
        this(name, new ArrayList<>());
    }

    public CallExpressionNode(String name, List<Node> params) {
        super(NodeType.CallExpression, params);
        this.name = name;
    }

}
