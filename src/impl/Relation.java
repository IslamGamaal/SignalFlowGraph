package impl;

public class Relation {
    public  float weight;
    public  Node nextNode;

    public Relation (float weight, Node t) {
        this.weight = weight;
        this.nextNode = t;
    }

}
