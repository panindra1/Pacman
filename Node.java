class Node {
  
  int costToGoal;
  int costFromStart;
  int totalCost;
  Node parent;
  
  public Node() {
    this.parent = NULL;
  }

  public int getTotalCost() {
   
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

