package sample;

import java.util.ArrayList;

public class TransferFunction {
    ArrayList<Relation> contents;
    ArrayList<String> ForwardPaths;
    ArrayList<String> loops;

    public TransferFunction(ArrayList<Relation> weights) {
        this.contents = weights;
    }

    public ArrayList<String> getForwardPaths() {
        ForwardPaths = new ArrayList<String>();

        //logic to get forward paths

        return ForwardPaths;
    }

    public ArrayList<String> getLoops() {
        loops = new ArrayList<String>();

        //logic to get all loops

        return loops;
    }

}
