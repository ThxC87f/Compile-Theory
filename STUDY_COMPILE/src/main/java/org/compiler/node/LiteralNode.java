package org.compiler.node;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Getter
public class LiteralNode<T> extends Node {
    final T value;

    public LiteralNode(T value) {
        super(NodeType.Literal);
        this.value = value;
    }

}
