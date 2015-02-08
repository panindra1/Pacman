import.java.util.*

class Pacman {
  ArrayList<Node> frontier = new ArrayList<Node>();

  public static void main() {
    int[][] mat = new int[][]{
                  {-2, 3, 2},
                  {3, -1, 1},
                  {2, 1, 0}};

        
  }

  void calculatepath(int[][] matrix) {
    Node startNode = new Node();
    startNode.costToGoal = 0;
    startNode.costFromStart = 0;
    startNode.totalCost = 0;

    int rows = matrix.length;
    int cols = matrix[0].length; 
    for(int i = 0; i < rows; i++) {
      for(int j = 0; j < cols; j++) {
        if(matrix[i][j] == -2) {
          if((i - 1) >= 0) {
            Node newNode = new Node();
            newNode.parent = startNode;
            newNode.costToGoal = matrix[i-1][j];
            newNode.costFromStart = newNode.parent.costFromStart + 1;
          }
          if((j - 1) >= 0 ) {
          
          }
        }
      }
    }

  }

  void createPath(Node parent, Node child) {
    child.parent = parent;
  }

  void addToFrontier(Node node) {
    frontier.add(node);
  }
  
};
