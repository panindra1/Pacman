/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author panindra
 */
enum HUERISTIC_TYPE{MISPLACED, MANHATTAN, GASCHNIG};
    
public class Puzzle {
     static Map<ArrayList<Integer>, Integer> stateSpaceMap = new HashMap<ArrayList<Integer>, Integer>();
     static LinkedList<Node> expansionList = new LinkedList<>();
     static PriorityQueue<Node> pq1;
     
     public static void main(String[] args) throws Exception{
        
         
         int[][] initialState = {{7, 2 ,4},
                                {5, 0 ,6 },
                                {8, 3, 1}};
         
         int[][] goalState = {{0, 1 ,2},
                              {3, 4 ,5},
                              {6, 7, 8}};
         
         saveState(initialState);
         saveState(goalState);
         
         System.out.println(checkInStateSpace(goalState));
         
         //int numberOFMisplaced = giveMisplacedTiles(mat);
         //int totalManhattanDistance = giveTotalManhattanDistance(initialState, goalState);
         //System.out.println(totalManhattanDistance);
        /*
        for(int i =0; i < 3; i++) {
            for(int j = 0; j <3; j++ ) {
                mat[i][j] = (int) Math.floor(Math.random()*10);
            }
                
        }*/
     
     }
     static void  createStartNode(int[][] matrix, final Algorithm algo) {
        int[][] inputMatrix = matrix;
        Node startNode = new Node();
        startNode.setCostToGoal(0);
        startNode.setCostFromStart(0);
        startNode.setTotalCost(0);
        startNode.setNodeType(NodeType.StartNode);
        //startNode.id = nodeId;

        for(int x = 0; x < matrix.length; x ++ ){
          for(int y = 0; y < matrix[0].length; y ++ ){
              if(matrix[x][y] == 0) {
                startNode.setI(x);
                startNode.setJ(y);
              }
          }
        }

        //frontier.add(startNode);
        pq1 = new PriorityQueue<Node>(10, new Comparator<Node>(){
                public int compare(Node o1, Node o2) {
                if(algo == Algorithm.AStar) {
                    if (o1.getTotalCost()> o2.getTotalCost())
                    return 1;
                else if (o1.getTotalCost() < o2.getTotalCost())
                    return -1;
                }
                else if(algo == Algorithm.GreedyBestFirst) {
                    if (o1.getCostToGoal()> o2.getCostToGoal())
                    return 1;
                else if (o1.getCostToGoal() < o2.getCostToGoal())
                    return -1;
                }

                return 0;
                }
            });
        
        pq1.add(startNode);
        expansionList.add(startNode);
        
        //calculatepath(matrix);
  }
     
     void calculatepath(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length; 
        
        Boolean visitedFlag = false;
        //PriorityQueue<Integer> pq2;
        
        while(pq1 != null) {
            Node top = pq1.poll();
            
            visitedFlag = false;
            
            /*for (Node visitedList : expansionList) {
                if ((top.getI() == visitedList.getI() && top.getJ() == visitedList.getJ()) && top.getNodeType() != NodeType.StartNode) {
                    visitedFlag = true;
                }
            }
            if(visitedFlag) {
                continue;
            }*/
            
            ArrayList<Integer> coordinates = giveCoordinates(0, matrix);
            
            int i = coordinates.get(0);
            int j = coordinates.get(1);
            
             // i-1,j // i+1, j // i,j-1  // i,j+1
             if((i - 1) >= 0) {
                 //swap(matrix[i][j], matrix[i - 1][j]); 
                 if(createPath(top, matrix[i -1][j], i - 1, j)) 
                    return;
             }

             if(i+1 < rows){
                 if(createPath(top, matrix[i + 1][j], i + 1, j))
                    return;
             }
             
             if(j-1 >= 0){
                 if(createPath(top, matrix[i][j - 1], i, j - 1))
                    return;  
              }

             if(j+1 < cols){
                 if(createPath(top, matrix[i][j + 1], i , j + 1))
                    return;
             }
             //expansionList.add(top);
        }
  }
    
             
    static Boolean createPath(Node parent, int h, int i, int j) {
      Node newNode = new Node();
      newNode.setParent(parent);
      newNode.setCostToGoal(h);
      newNode.setI(i);
      newNode.setJ(j);
      //newNode.id = nodeId;
      newNode.setCostFromStart(parent.getCostFromStart() + 1);
      newNode.setTotalCost(newNode.getCostFromStart() + newNode.getCostToGoal());
      //frontier.add(newNode);
      //pq1.add(newNode);
      
      if(h == 0) {
        System.out.println("End node found"); 
        printTree(newNode);
      
        return true;
      }
      return false;
  }

   static void printTree(Node child) {
    int pathCost = 0;
    while(child != null) {
      pathCost++;
      System.out.println(child.getI() + " : "+ child.getJ());
      child = child.getParent();
    }
     //System.out.println("PathCost size :"+pathCost+" expansionList size : " + expansionList.size());
  }
   
    static public void giveHeuristics(int[][] mat, HUERISTIC_TYPE type) {
        switch(type) {  
            case MISPLACED: 
                int numberOFMisplaced = giveMisplacedTiles(mat);
            break;
            
            case MANHATTAN: 
                
            break;
            
            case GASCHNIG: 
                
            break;
            
        }
    }
     
   static public int giveMisplacedTiles(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length; 
        int numberOfMisplaced = 0;
        for(int i= 0; i< rows; i++) {
            for(int j = 0; j<cols; j++) {
                if(mat[i][j] != (i +j + 1) ) {
                    numberOfMisplaced++;
                }
            }
        }
        return numberOfMisplaced;
    }
   
   static public int giveTotalManhattanDistance(int[][] initialState, int[][] goalState) {
        int rows = initialState.length;
        int cols = initialState[0].length; 
        int totalManhattanDistance = 0;
        
        for(int i= 0; i< rows; i++) {
            for(int j = 0; j<cols; j++) {
                if(initialState[i][j] != 0) {
                    ArrayList<Integer> coordinates = giveCoordinates(initialState[i][j], goalState);
                    totalManhattanDistance += Math.abs(coordinates.get(0) - i) + Math.abs(coordinates.get(1) - j);
                }
            }
        }
        return totalManhattanDistance;  
   }
   
   static public ArrayList<Integer> giveCoordinates(int num, int[][] goalState) {
        int rows = goalState.length;
        int cols = goalState[0].length; 
        
        ArrayList<Integer> coordinates = new ArrayList<Integer>();
        for(int i= 0; i< rows; i++) {
            for(int j = 0; j<cols; j++) {
                if(num == goalState[i][j]) {
                    coordinates.add(i);
                    coordinates.add(j);
                    return coordinates;
                }
            }
        }
       
       return coordinates;
   }
   
   
   
   static public void saveState(int[][] stateSpace) {
       int rows = stateSpace.length;
       int cols = stateSpace[0].length; 
        ArrayList<Integer> list = new ArrayList<Integer>();
        
        for(int i= 0; i< rows; i++) {
            for(int j = 0; j<cols; j++) {
                list.add(stateSpace[i][j]);
            }
        }
        
        stateSpaceMap.put(list, 1);
   } 
   
   
   static public Boolean checkInStateSpace(int[][] stateSpace) {
       int rows = stateSpace.length;
       int cols = stateSpace[0].length; 
        ArrayList<Integer> list = new ArrayList<Integer>();
        
        for(int i= 0; i< rows; i++) {
            for(int j = 0; j<cols; j++) {
                list.add(stateSpace[i][j]);
            }
        }
        
        if(stateSpaceMap.containsKey(list))
            return true;
        else 
            return false;
   }
  
}
