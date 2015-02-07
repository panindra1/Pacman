public class Node {
  
  int costToGoal;
  int costFromStart;
  int totalCost;
  Node parent;
  
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

