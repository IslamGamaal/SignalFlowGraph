package impl;

import java.util.ArrayList;
import java.util.Stack;

public class Logic {
    ArrayList<Node> nodes;
    boolean[] visited;
    ArrayList<Loop> loops;
    Node TempStartNode;
    boolean startCall = new Boolean(false);
    Stack stack = new Stack() ;
    ArrayList<ForwardPath> forwardPaths;
    ArrayList<ArrayList> nonTouchedLoops;
    Node startNode;
    Node endNode;
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
        for(int i = 0; i < loops.size(); i++) {
            System.out.println(loops.get(i).getName());
            System.out.println(loops.get(i).getWeight());
            System.out.println("----------------");
        }
    }

    public boolean dfsForward(Node checkNode){
        boolean temp =false;
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
            while (stack.size() >0&& stack.peek() != checkNode){
                stack.pop();
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
            for (int i = loop.loop.size()-1; i >= 0 ; i--) {
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
            if (stack.size() !=0) {
                stack.pop();
            }
        }
        visited[nodes.indexOf(checkNode)] = false;
        return temp;
    }

    public void transferFunction(){

    }



}
