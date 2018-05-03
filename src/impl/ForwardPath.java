package impl;

import sample.Controller;

import java.util.ArrayList;

public class ForwardPath {
    ArrayList<Node> forwardPath;
    public ForwardPath() {
        forwardPath = new ArrayList<Node>();
    }

    public int getWeight() {
        int weight = 1;
        ArrayList<Relation> rel;
        for(int i = 0; i < forwardPath.size()-1; i++) {
            rel = forwardPath.get(i).relations;
            for(int j = 0; j < rel.size(); j++) {
                if(rel.get(j).nextNode.name.equals(forwardPath.get(i+1).name)) {
                    weight *= rel.get(j).weight;
                    break;
                }
            }
        }
        return weight;
    }
    public String getName() {
        String name = "";
        for(int i = 0; i < forwardPath.size(); i++) {
            name+= forwardPath.get(i).name;
        }
        //name = sortAlphabetically(name);
        int numOfNodes = Controller.numbOfNodes;
        char[] nameArray = new char[numOfNodes];
        nameArray = putDashes(nameArray);
        for(int i = 0; i < name.length(); i++) {
            nameArray[name.charAt(i) - 97] = name.charAt(i);
        }
        name = "";
        for(int i = 0; i < nameArray.length; i++) {
            name += nameArray[i];
        }        return name;
    }

    private char[] putDashes(char[] nameArray) {
        for(int i = 0; i < nameArray.length; i++) {
            nameArray[i] = '-';
        }
        return nameArray;
    }

    private String sortAlphabetically(String name) {
        String sortedName = "";
        return sortedName;
    }
}
