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

class PuzzleNode {
    int i;
    int j;
    PuzzleNode parent;
    Boolean startNode;
    
    PuzzleNode(int i, int j) {
        this.i = i;
        this.j = j;
        this.parent = null;
        this.startNode = false;
    }
}

public class Puzzle {
     static Map<ArrayList<Integer>, Integer> stateSpaceMap = new HashMap<ArrayList<Integer>, Integer>();
     static LinkedList<Node> expansionList = new LinkedList<>();
     static LinkedList<PuzzleNode> frontier = new LinkedList<>();
     static PriorityQueue<int[][]> pq1;
     static int count = 0;
     static int[][] goalState = {{0, 1 ,2},
                                {3, 4 ,5},
                                {6, 7, 8}};
         
     public static void main(String[] args) throws Exception{
        
         /*int[][] initialState = {{0, 3 ,2},
                                {1, 4 ,5 },
                                {6, 7, 8}};*/
        
         
         int[][] initialState = {{7, 2 ,4},
                                {5, 0 ,6 },
                                {8, 3, 1}};
         
         
        /* int[][] initialState = {{5, 6, 2},
                                 {4, 3 ,8},
                                {1, 0, 7}};*/
         if(checkIfSolvable(initialState)) {
            saveState(initialState);
            saveState(goalState);
            createStartNode(initialState);
         }
         else {
             System.err.println("Problem cannot be solved");
         }
         
     }
     static void  createStartNode(int[][] matrix) {
        int[][] inputMatrix = matrix;
        
        int startnodeI = 0;
        int startnodeJ = 0;
        
        for(int x = 0; x < matrix.length; x ++ ){
          for(int y = 0; y < matrix[0].length; y ++ ){
              if(matrix[x][y] == 0) {
                startnodeI = x;
                startnodeJ = y;
              }
          }
        }
        
        PuzzleNode startNode = new PuzzleNode(startnodeI, startnodeJ);
        startNode.startNode = true;
        
        //frontier.add(startNode);
        pq1 = new PriorityQueue<int[][]>(10, new Comparator<int[][]>(){
                public int compare(int[][] o1, int[][] o2) {
                    
                    if(giveHeuristics(o1, HUERISTIC_TYPE.MANHATTAN) > giveHeuristics(o2, HUERISTIC_TYPE.MANHATTAN))
                        return 1;
                    else 
                        return -1;
                  
                }  
            });
        
        pq1.add(matrix);
       // expansionList.add(startNode);
        frontier.add(startNode);
        calculatepath(matrix);
  }
     
     static void calculatepath(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length; 
        
        Boolean visitedFlag = false;
        //PriorityQueue<Integer> pq2;
        
        while(pq1.size() > 0) {
            int[][] top = pq1.poll();
            matrix = top;
            visitedFlag = checkInStateSpace(top);
            if(count == 0) {
                visitedFlag = false;           
            }
            count++;
            
            if(visitedFlag) {
                continue;
            }
            saveState(matrix);
            ArrayList<Integer> coordinates = giveCoordinates(0, matrix);
            
            int i = coordinates.get(0);
            int j = coordinates.get(1);
            PuzzleNode pNode = new PuzzleNode(i , j);
            
             if((i - 1) >= 0) {
                  int matrix1[][] = swap(matrix, i, j, i - 1, j); 
                
                  pq1.add(matrix1);
                 if(createPath(pNode, giveHeuristics(matrix1, HUERISTIC_TYPE.MANHATTAN), i - 1, j)) 
                    return;
             }

             if(i+1 < rows){
                 int matrix1[][] = swap(matrix, i, j, i + 1, j);
                
                 pq1.add(matrix1);
                 if(createPath(pNode, giveHeuristics(matrix1, HUERISTIC_TYPE.MANHATTAN), i + 1, j))
                    return;
             }
             
             if(j-1 >= 0){
                 int matrix1[][] = swap(matrix, i, j, i, j - 1); 
                
                 pq1.add(matrix1);
                 if(createPath(pNode, giveHeuristics(matrix1, HUERISTIC_TYPE.MANHATTAN), i, j - 1))
                    return;  
              }

             if(j+1 < cols){
                 int matrix1[][] = swap(matrix, i, j, i, j + 1); 
                
                 pq1.add(matrix1);
                 if(createPath(pNode, giveHeuristics(matrix1, HUERISTIC_TYPE.MANHATTAN), i , j + 1))
                    return;
             }
        }
  }
    
    static public int[][] swap(int[][] curMat, int cur_i, int cur_j, int new_i, int new_j) {
        int [][] newMat = new int[curMat.length][];
        for(int i = 0; i < curMat.length; i++)
            newMat[i] = curMat[i].clone();

        int temp = newMat[cur_i][cur_j];
        newMat[cur_i][cur_j] = newMat[new_i][new_j];
        newMat[new_i][new_j] = temp;
        return newMat;
    }
    
    static Boolean createPath(PuzzleNode parent, int h, int i, int j) {
      PuzzleNode newNode = new PuzzleNode(i , j);
      newNode.parent = parent;
      
      frontier.add(newNode);
       System.out.println("Misplaced tiles" + h);
      if(h == 0) {
        System.out.println("End node found : " + count);
        //printTree(newNode);      
        return true;
      }
      return false;
  }

   static void printTree(PuzzleNode child) {
    int pathCost = 0;
    while(child != null) {
      pathCost++;
      System.out.println(child.i + " : "+ child.j);
      child = child.parent;
    }
     //System.out.println("PathCost size :"+pathCost+" expansionList size : " + expansionList.size());
  }
   
    static public int giveHeuristics(int[][] mat, HUERISTIC_TYPE type) {
        int heuristicValue = 0;
        switch(type) {  
            case MISPLACED: 
                heuristicValue = giveMisplacedTiles(mat);
            break;
            
            case MANHATTAN: 
                heuristicValue = giveTotalManhattanDistance(mat);
            break;
            
            case GASCHNIG: 
                
            break;
            
        }
       return heuristicValue;
    }
     
   static public int giveMisplacedTiles(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length; 
        int numberOfMisplaced = 0;
        for(int i= 0; i< rows; i++) {
            for(int j = 0; j<cols; j++) {
                if(mat[i][j] != goalState[i][j] ) {
                    numberOfMisplaced++;
                }
            }
        }
        return numberOfMisplaced;
    }
   
   static public int giveTotalManhattanDistance(int[][] initialState) {
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
   
   static public ArrayList<Integer> giveCoordinates(int num, int[][] curMatrix) {
        int rows = curMatrix.length;
        int cols = curMatrix[0].length; 
        
        ArrayList<Integer> coordinates = new ArrayList<Integer>();
        for(int i= 0; i< rows; i++) {
            for(int j = 0; j<cols; j++) {
                if(num == curMatrix[i][j]) {
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
   
    static public Boolean checkIfSolvable(int[][] matrix) {
        int rows = matrix.length;
       int cols = matrix[0].length; 
        ArrayList<Integer> list = new ArrayList<Integer>();
        
        for(int i= 0; i< rows; i++) {
            for(int j = 0; j<cols; j++) {
                list.add(matrix[i][j]);
            }
        }
        
       if(mergesort(list, 0, list.size()) %2 == 0)
           return true;
       else
           return false;
   }
    
   static private int mergesort(ArrayList<Integer> list, int low, int high) {
    // check if low is smaller then high, if not then the array is sorted
    if (low < high) {
      // Get the index of the element which is in the middle
      int middle = low + (high - low) / 2;
      
      return mergesort(list, low, middle) + mergesort(list, middle + 1, high) + merge(list, low, middle, high);
    }
    return 0;
  }

  static private int merge(ArrayList<Integer> numbers,int low, int middle, int high) {
      int inv_count = 0;
      int[] helper = new int[numbers.size()];
    // Copy both parts into the helper array
    for (int i = low; i <= high; i++) {
      helper[i] = numbers.get(i);
    }

    int i = low;
    int j = middle + 1;
    int k = low;
    // Copy the smallest values from either the left or the right side back
    // to the original array
    while (i <= middle && j <= high) {
      if (helper[i] <= helper[j]) {
        numbers.add(helper[i]);
        i++;
      } else {
        numbers.add(helper[j]);
        j++;
        inv_count += (middle - i);
      }
      k++;
    }
    // Copy the rest of the left side of the array into the target array
    while (i <= middle) {
      numbers.add(helper[i]);
      k++;
      i++;
    }
  return inv_count;
  }
  
}
