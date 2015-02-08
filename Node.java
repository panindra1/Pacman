/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author panindra
 */
public class Node {
  private int costToGoal;
  private int costFromStart;
  private int totalCost;
  private int i;
  private int j;
  private int id;
  private Node parent;
  
  public Node() {
    this.parent = null;
  }

  public void setCostToGoal(int value) {
    costToGoal = value;
  }

  public void setCostFromStart(int value) {
    costFromStart = value;
  }

  public void setTotalCost(int value) {
    totalCost = value;
  }
  
  public void setICoordinate(int value) {
    i = value;
  }
  
  public void setJCoordinate(int value) {
    j = value;
  }
  
  public void setId(int value) {
      id = value;
  }
  
  public void setParent(Node parentNode) {
      parent = parentNode;
  }
  
    public int getTotalCost() {
     return totalCost;
  }
  
  public int getICoordinate() {
       return i;
  }
  
  public int getJCoordinate() {
    return j;
  }
  
  public int getId() {
     return id;
  }
  
  public Node getParent() {
    return parent;
  }
  
  public int getCostToGoal() {
    return costToGoal;
  }

  public int getCostFromStart() {
    return costFromStart;
  }

};


