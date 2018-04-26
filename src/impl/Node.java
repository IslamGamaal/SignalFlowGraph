package impl;

import java.util.ArrayList;

public class Node {
    String name;
    public ArrayList<Relation> relations;

    public Node(String name) {
        this.name = name;
        relations = new ArrayList<Relation>();
    }
}
