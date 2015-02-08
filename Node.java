/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author panindra
 */
 enum NodeType {
    StartNode, EndNode;
}

public class Node {
  
  int costToGoal;
  int costFromStart;
  int totalCost;
  int i;
  int j;
  int id;
  Node parent;
 NodeType nodeType;
  
  public Node() {
    this.parent = null;
  }

  public void setCostToGoal(int value) {
    costToGoal = value;
  }

  public void setCostFromStart(int value) {
    costFromStart = value;
  }

  public int getCostToGoal() {
    return costToGoal;
  }

  public int getCostFromStart() {
    return costFromStart;
  }

};


