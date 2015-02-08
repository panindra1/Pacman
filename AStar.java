/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author panindra
 */
public class AStar {
    LinkedList<Node> frontier = new LinkedList<>();
    Map expansionMap = new HashMap(); 
    int nodeId = 0;
    
    void  createStartNode(int[][] matrix) {
        Node startNode = new Node();
        startNode.setCostToGoal(0);
        startNode.setCostFromStart(0);
        startNode.setTotalCost(0);
        startNode.setId(nodeId);

        for(int x = 0; x < matrix.length; x ++ ){
          for(int y = 0; y < matrix[0].length; y ++ ){
              if(matrix[x][y] == -2) {
                startNode.setICoordinate(x);
                startNode.setJCoordinate(y);
              }
          }
        }

        frontier.add(startNode);
        expansionMap.put( startNode.getId(), startNode);
  }

    void calculatepath(int[][] matrix) {
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
    
            if(expansionMap.containsKey(frontier.get(k).getId()) && frontier.get(k).getId() != 0) {
                visitedFlag = true;
            }
            if(visitedFlag) {
                continue;
            }   
            else {
                 int i = frontier.get(k).getICoordinate();
                 int j = frontier.get(k).getJCoordinate();
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

                  int val = frontier.get(k).getCostFromStart() + min;
                  if( val < frontierMin) {
                    frontierMin = val;
                    selectedFrontierNode = k;
                    selectedChild_i = i_value;
                    selectedChild_j = j_value;
                  }

                  if(frontier.get(k).getId() != 0) {
                     expansionMap.put(frontier.get(k).getId(), frontier.get(k));
                  }
                // frontier.remove(frontier.get(k));
            }
        }
  }

   Boolean createPath(Node parent, int h, int i, int j) {
      nodeId++;
      Node newNode = new Node();
      newNode.setParent(parent);
      newNode.setCostToGoal(h);
      newNode.setICoordinate(i);
      newNode.setJCoordinate(j);
      newNode.setId(nodeId);
      newNode.setCostFromStart(parent.getCostFromStart() + 1);
      frontier.add(newNode);  
      
      if(h == 0) {
        System.out.println("End node found"); 
        printTree(newNode);
        return true;
      }
      return false;
  }

   void printTree(Node child) {
    while(child != null) {
      System.out.println(child.getICoordinate() + " : "+ child.getICoordinate());
      child = child.getParent();
    }
  }
}
