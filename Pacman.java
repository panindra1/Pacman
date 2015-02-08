/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman;

/**
 *
 * @author panindra
 */
import java.util.*; 

class Pacman {
  static LinkedList<Node> frontier = new LinkedList<Node>();
  static LinkedList<Node> expansionList = new LinkedList<Node>();

  public static void main(String[] args) {
    int[][] mat = new int[][]{
                  {-2, 3, 2},
                  {3, -1, 1},
                  {2, -1, 0}};
    
    createStartNode(mat);      
    calculatepath(mat);
  }

  static void  createStartNode(int[][] matrix) {
     Node startNode = new Node();
    startNode.costToGoal = 0;
    startNode.costFromStart = 0;
    startNode.totalCost = 0;
    startNode.visited = false;
    startNode.nodeType = NodeType.StartNode;
    
    for(int x = 0; x < matrix.length; x ++ ){
      for(int y = 0; y < matrix[0].length; y ++ ){
          if(matrix[x][y] == -2) {
            startNode.i = x;
            startNode.j = y;
          }
      }
    }

    frontier.add(startNode);
    expansionList.add(startNode);

  }

  static void calculatepath(int[][] matrix) {
    int rows = matrix.length;
    int cols = matrix[0].length; 
    int min = 10000;
    int i_value = -1;
    int j_value = -1;
    int frontierMin = 100000;
    int selectedNode = -1;
    int selectedFrontierNode = -1;
    int selectedChild_i = -1;
    int selectedChild_j = -1;
    Boolean visitedFlag = false;
    
   for(int k = 0; k < frontier.size(); k++ ){
      visitedFlag = false;
        for(int l = 0; l < expansionList.size(); l++ ){
           if( (frontier.get(k).i== expansionList.get(l).i &&frontier.get(k).j== expansionList.get(l).j) && frontier.get(k).nodeType != NodeType.StartNode) {
               
               visitedFlag = true;
           }
        }
        if(visitedFlag) {
            continue;
        }   
           else {
                //System.out.println(" Iteration and frontier "+ k + " : "+frontier.get(k).costToGoal);   
                int i = frontier.get(k).i;
                int j = frontier.get(k).j;
                 // i-1,j // i+1, j // i,j-1  // i,j+1
                 if((i - 1) >= 0) {
                     if(matrix[i-1][j] < min && matrix[i-1][j] >= 0){
                         min = matrix[i-1][j];
                         i_value = i - 1; 
                         j_value = j;
                   }

                   if(matrix[i-1][j] >= 0) {
                     if(createPath(frontier.get(k), matrix[i -1][j], i - 1, j)) 
                         return;
                     
                   }

                 }

                 if(i+1 < rows){
                     if (matrix[i+1][j] < min && matrix[i+1][j] >= 0) {
                         min = matrix[i+1][j];
                         i_value = i + 1;
                         j_value = j;                    
                     }
                     if(matrix[i + 1][j] >= 0) {
                       if(createPath(frontier.get(k), matrix[i + 1][j], i + 1, j))
                           return;
                     }
                 }

                 if(j-1 >= 0){
                     if(matrix[i][j-1] < min && matrix[i][j - 1] >= 0){
                         min = matrix[i][j-1];
                         i_value = i;
                         j_value = j - 1;
                     }
                     if(matrix[i][j - 1] >= 0) {
                       if(createPath(frontier.get(k), matrix[i][j - 1], i, j - 1))
                           return;
                     }
                 }

                 if(j+1 < cols){
                     if(matrix[i][j+1] < min && matrix[i][j + 1] >= 0) {
                         min = matrix[i][j+1];
                         i_value = i;
                         j_value = j + 1;
                     }

                     if(matrix[i][j + 1] >= 0) {
                       if(createPath(frontier.get(k), matrix[i][j + 1], i , j + 1))
                           return;
                     }
                 }

                 int val = frontier.get(k).costFromStart + min;
                 if( val < frontierMin) {
                   frontierMin = val;
                   selectedFrontierNode = k;
                   selectedChild_i = i_value;
                   selectedChild_j = j_value;
                 }
                   
                 if(frontier.get(k).nodeType != NodeType.StartNode) {
                    expansionList.add(frontier.get(k));
                 }
                 frontier.get(k).visited = true;
                 //frontier.remove(frontier.get(k));
                 System.out.println("Front size : "+ frontier.size());   
               }
           }
   
     
     
    
  }

  static Boolean createPath(Node parent, int h, int i, int j) {
      //System.out.println("End node will never be found" + h);   

      Node newNode = new Node();
      newNode.parent = parent;
      newNode.costToGoal = h;
      newNode.i = i;
      newNode.j = j;
      newNode.costFromStart = parent.costFromStart + 1;
      newNode.visited = false;
       frontier.add(newNode);
      
  
      
      if(h == 0) {
        System.out.println("End node found"); 
        printTree(newNode);
        return true;
      }
      return false;
  }

  static void printTree(Node child) {
    while(child.parent != null) {
      System.out.println(child.i + " : "+ child.j);
      child = child.parent;
    }
  }

}
