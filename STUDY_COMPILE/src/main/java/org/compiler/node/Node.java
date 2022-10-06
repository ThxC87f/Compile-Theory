package org.compiler.node;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public abstract class Node {
    private static final Gson G = new GsonBuilder()
            // .setPrettyPrinting()
            .create();


    NodeType type;

    @Override
    public String toString() {
        return G.toJson(this);
    }
}
