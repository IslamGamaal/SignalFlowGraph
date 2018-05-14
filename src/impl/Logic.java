package impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Logic {
    ArrayList<Node> nodes;
    boolean[] visited;
    public static ArrayList<Loop> loops;
    Node TempStartNode;
    public static ArrayList<ArrayList<Loop>>[] nonTouchedLoops;
    boolean startCall = new Boolean(false);
    Stack stack = new Stack() ;
    public static ArrayList<ForwardPath> forwardPaths;
    Node startNode;
    Node endNode;
    public static ArrayList<Float> deltas = new ArrayList<Float>();
    public static float transferFunction;
    public Logic(ArrayList<Node> nodes) {
        this.nodes = nodes;
        visited = new boolean[nodes.size()];
        startNode = nodes.get(0);
        endNode = nodes.get(nodes.size()-1);
        forwardPaths = new ArrayList<ForwardPath>();
        loops = new ArrayList<Loop>();
        stack.add(startNode);
        dfsForward(startNode);
        visited = new boolean[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            stack = new Stack();
            stack.add(nodes.get(i));
            startCall = true;
            dfsLoops(nodes.get(i),nodes.get(i));
        }
        deltas = new ArrayList<>();
       /*nonTouchedLoops = new ArrayList[loops.size()+1];*/
//        getUnTouchedLoops();
        getTransferFunction();
        int x = 0;
    }

    public boolean dfsForward(Node checkNode){
        boolean temp = false;
        if (checkNode == endNode){
            ForwardPath forwardPath = new ForwardPath();
            Stack tempStack = new Stack();
            while (stack.size()!= 0){
                tempStack.push(stack.pop());
            }
            while (tempStack.size()!=0){
                forwardPath.forwardPath.add((Node) tempStack.pop());
            }
            for (int i = 0; i < forwardPath.forwardPath.size() ; i++) {
                stack.push(forwardPath.forwardPath.get(i));
            }
            forwardPaths.add(forwardPath);
            return true;
        }
        if (visited[nodes.indexOf(checkNode)] == true){
            return false;
        }
        visited[nodes.indexOf(checkNode)] = true;
        for (int i = 0; i <checkNode.relations.size() ; i++) {
            stack.push(checkNode.relations.get(i).nextNode);
            temp = dfsForward(checkNode.relations.get(i).nextNode);
            while (stack.size() >0){
                if (stack.peek()!= checkNode)
                    stack.pop();
                else if(stack.size()>=2){
                    Node tempNode = (Node) stack.pop();
                    if (stack.peek() ==checkNode)
                        break;
                    else{
                        stack.push(tempNode);
                        break;
                    }
                }
                else
                    break;
            }
        }
        visited[nodes.indexOf(checkNode)] = false;

        return temp;

    }

    public boolean dfsLoops(Node sNode , Node checkNode) {
        boolean temp =false;
        if (sNode == checkNode && !startCall) {
            Loop loop = new Loop();
            Stack tempStack = new Stack();
            while (stack.size()!= 0){
                tempStack.push(stack.pop());
            }
            while (tempStack.size()!=0){
                loop.loop.add((Node) tempStack.pop());
            }
            for (int i = 0; i < loop.loop.size() ; i++) {
                stack.push(loop.loop.get(i));
            }
            loops.add(loop);
            return true;
        }
        if (visited[nodes.indexOf(checkNode)] == true) {
            return false;
        }
        if (checkNode.name.compareTo(sNode.name) < 0) {
            return false;
        }
        startCall = false;
        visited[nodes.indexOf(checkNode)] = true;
        for (int i = 0; i <checkNode.relations.size() ; i++) {
            stack.push(checkNode.relations.get(i).nextNode);
            dfsLoops(sNode , checkNode.relations.get(i).nextNode);
            while (stack.size() >0){
                if (stack.peek()!= checkNode)
                    stack.pop();
                else if(stack.size()>=2){
                    Node tempNode = (Node) stack.pop();
                    if (stack.peek() ==checkNode)
                        break;
                    else{
                        stack.push(tempNode);
                        break;
                    }
                }
                else
                    break;
            }
        }
        visited[nodes.indexOf(checkNode)] = false;
        return temp;
    }

    public ArrayList<ArrayList<Loop>>[] getUnTouchedLoops(ArrayList<Loop> loops){
        ArrayList<ArrayList<Loop>>[] nonTouched =new ArrayList[loops.size()+1];
        for (int i = 0; i < Math.pow(2,loops.size()); i++) {
            String indexBinary = Integer.toBinaryString(i);
            int numberOfOnes = getNumberOfOnes(indexBinary);
            if (numberOfOnes != 1 && numberOfOnes!= 0){
                ArrayList indexes = getOnesIndecies(indexBinary);
                ArrayList<Loop> testedLoops = new ArrayList<Loop>();
                for (int j = 0; j < indexes.size(); j++) {
                    testedLoops.add(loops.get((Integer) indexes.get(j)));
                }
                if(areUntouched(testedLoops)){
                    if (nonTouched[numberOfOnes] ==null) {
                        nonTouched[numberOfOnes] = new ArrayList<ArrayList<Loop>>();
                    }
                    ArrayList loopsList = new ArrayList<Loop>();
                    for (int j = 0; j <testedLoops.size() ; j++) {
                        loopsList.add(testedLoops.get(j));
                    }
                    nonTouched[numberOfOnes].add(loopsList);
                }
            }
        }
        return nonTouched;
    }


    private boolean areUntouched(ArrayList<Loop> loops) {
        Set allNodes = new HashSet();
        String allNodesCascaded = "";
        int lengthOfAllLoops = 0;
        for(int i = 0; i < loops.size(); i++) {
            allNodesCascaded+= loops.get(i).getNameWithoutDashes();
            lengthOfAllLoops+= loops.get(i).getNameWithoutDashes().length();
        }
        for(int i = 0; i < allNodesCascaded.length(); i++) {
            allNodes.add(allNodesCascaded.charAt(i));
        }
        if(allNodes.size() == lengthOfAllLoops) {
            return true;
        } else {
            return false;
        }
    }
    private ArrayList<Integer> getOnesIndecies(String binaryRepresentation) {
        ArrayList<Integer> indicies = new ArrayList<Integer>();
        for(int i = binaryRepresentation.length()-1; i >= 0; i--) {
            if(binaryRepresentation.charAt(i) == '1') {
                indicies.add(binaryRepresentation.length() - 1 - i);
            }
        }
        return indicies;
    }

    private int getNumberOfOnes(String indexBinary) {
        int counter =0;
        for (int i = 0; i <indexBinary.length() ; i++) {
            if (indexBinary.charAt(i) == '1'){
                counter ++;
            }
        }
        return counter;
    }
    private ArrayList<Loop> getMatchingLoops(ForwardPath f) {
        ArrayList<Loop> matchingLoops = new ArrayList<Loop>();
        for(int i = 0; i < loops.size(); i++) {
            Set allNodes = new HashSet();
            String allNodesCascaded = "";
            int lengthOfAllLoops = 0;
            lengthOfAllLoops = loops.get(i).getNameWithoutDashes().length() + f.getNameWithoutDashes().length();
            allNodesCascaded = loops.get(i).getNameWithoutDashes() + f.getNameWithoutDashes();
            for(int j = 0; j < allNodesCascaded.length(); j++) {
                allNodes.add(allNodesCascaded.charAt(j));
            }
            if(lengthOfAllLoops == allNodes.size()) {
                matchingLoops.add(loops.get(i));
            }
        }
        return matchingLoops;
    }

    float sumOfProductOfNonTouchingLoops(ArrayList<ArrayList<Loop>> nonTouched){
        float sum = 0;
        for (int i = 0; i <nonTouched.size() ; i++) {
            float gain =1;
            for (int j = 0; j <nonTouched.get(i).size() ; j++) {
                gain = gain * nonTouched.get(i).get(j).getWeight();
            }
            sum += gain;
        }
        return sum;
    }
    float sumOfLoops(ArrayList<Loop> loops){
        float sum = 0;
        for (int i = 0; i < loops.size(); i++) {
            sum += loops.get(i).getWeight();
        }
        return sum;
    }

    void getTransferFunction(){
        ArrayList<ArrayList<Loop>>[] nonTouched;
        nonTouched = getUnTouchedLoops(loops);
        nonTouchedLoops = nonTouched;
        float generalDelta =getDelta(this.loops, nonTouched);
        float segmaDelta = 0;
        for (int i = 0; i <forwardPaths.size() ; i++) {
            ArrayList<Loop> matchingLoops = getMatchingLoops(forwardPaths.get(i));
            ArrayList<ArrayList<Loop>>[] nonTouchedDelta;
            nonTouchedDelta = getUnTouchedLoops(matchingLoops);
            float delta = getDelta(matchingLoops, nonTouchedDelta);
            delta = delta * forwardPaths.get(i).getWeight();
            deltas.add(delta);
            segmaDelta +=delta;
        }
        this.transferFunction = segmaDelta / generalDelta;

    }

    private float getDelta(ArrayList<Loop> loops, ArrayList<ArrayList<Loop>>[] nonTouched) {
        float delta = 1 - this.sumOfLoops(loops);
        for (int i = 2; i <nonTouched.length && nonTouched[i] != null && nonTouched[i].size()!=0; i++) {
            float sum = 0;
            for (int j = 0; j <nonTouched[i].size(); j++) {
                 sum = sumOfProductOfNonTouchingLoops(nonTouched[i]);
            }
            if (i % 2 != 0){
                sum = sum * -1;
            }
            delta +=sum;
        }
        return delta;
    }
}
