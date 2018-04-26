package impl;

public class Relation {
    public  int weight;
    public  Node nextNode;

    public Relation (int weight, Node t) {
        this.weight = weight;
        this.nextNode = t;
    }

}
