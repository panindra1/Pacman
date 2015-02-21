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
    static Map<ArrayList<Integer>, Integer> inputMap = new HashMap<ArrayList<Integer>, Integer>();
    static LinkedList<PuzzleNode> frontier = new LinkedList<>();
    static PriorityQueue<int[][]> pq1;
    static int count = 0;
    static int total = 0;
    static int[][] goalState = {{0, 1 ,2},
                                {3, 4 ,5},
                                {6, 7, 8}};
    
    static int[][] initialState = {{1, 4, 2},
                                  {3, 0 ,5},
                                  {6, 7, 8}};
         
     
    public static void main(String[] args) throws Exception{  
        saveState(false, goalState);
        saveState(true, goalState);
        for(int i = 0; i < 5; i++) {
            initialState =  generateInputMatix();
            printMatrix(initialState);
            
            saveState(true, initialState);
            createStartNode(initialState);

            stateSpaceMap.clear();
            inputMap.clear();
        }
     }
     
     static void  createStartNode(int[][] matrix) {
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
        frontier.add(startNode);
        calculatepath(matrix, HUERISTIC_TYPE.MISPLACED);
  }
     
     static void calculatepath(int[][] matrix, HUERISTIC_TYPE type) {
        int expandedNodeCount = 1; 
        int count = 0;
        if(type == HUERISTIC_TYPE.GASCHNIG) {
            while(pq1.size() > 0) {
                int[][] top = pq1.poll();

                if(!checkGoalState(top)) {
                    if(top[0][0] == 0) {
                        count++;
                    } 
                    if(giveMisplacedTiles(top) == 2 && top[0][0] != 0) {
                        ArrayList<Integer> coordinates = giveCoordinates(0, top);
                        int i = coordinates.get(0);
                        int j = coordinates.get(1);
                        
                        pq1.add(swap(top, i, j, 0, 0));
                        count++;
                        break;
                    }
                     
                     matrix = gaschnig(matrix); 
                    //saveState(matrix);
                    
                    pq1.add(matrix);
                    count++;
                }
                else {
                    break;
                }
            }
            System.out.println("Path cost if we get output:" + count);
        }
        else {                         
            int rows = matrix.length;
            int cols = matrix[0].length;

            Boolean visitedFlag = false;

            while(pq1.size() > 0) {
                int[][] top = pq1.poll();
                
                matrix = top;
                visitedFlag = checkInStateSpace(true, top);
                if(count == 0) {
                    visitedFlag = false;          
                }
                
                if(checkGoalState(top)) {
                    System.out.println("Path cost :" + count);
                    System.out.println("Expanded Node count = " + expandedNodeCount);
                    break;
                }
                
                if(visitedFlag){
                    continue;
                }
                
                count++;
                
                saveState(true, matrix);
                //printMatrix(matrix);
                pq1.clear();
                ArrayList<Integer> coordinates = giveCoordinates(0, matrix);
                ArrayList<Integer> singleList =  convertToList(matrix);
                //searc for the node in the frontier with I and J as given below

                int i = coordinates.get(0);
                int j = coordinates.get(1);

                PuzzleNode pNode = null;
                boolean foundParent = false;
               
                /*for(int x=0; x<frontier.size(); x++){
                    pNode = frontier.get(x);
                    if(pNode.i == i && pNode.j == j){
                        foundParent = true;
                        break;
                    }
                }*/

                if(!foundParent){
                    pNode = new PuzzleNode(i, j);
                }
                int var = 0;
                if((i - 1) >= 0) {
                    expandedNodeCount++;
                   var = (i - 1) * 3 + j;
                   if( var != singleList.get(var)) {
                        int matrix1[][] = swap(matrix, i, j, i - 1, j);

                        pq1.add(matrix1);
                        
                        if(createPath(pNode, giveHeuristics(matrix1, type), i - 1, j))
                          return;
                   }
               }

                if(i+1 < rows){
                    expandedNodeCount++;
                    var = (i + 1) * 3 + j;
                    if( var != singleList.get(var)) {
                        int matrix1[][] = swap(matrix, i, j, i + 1, j);

                        pq1.add(matrix1);
                        if(createPath(pNode, giveHeuristics(matrix1, type), i + 1, j))
                           return;
                    }
                }

                if(j-1 >= 0){
                    expandedNodeCount++;
                        
                     var = i * 3 + (j - 1);
                    if( var != singleList.get(var)) {
                        int matrix1[][] = swap(matrix, i, j, i, j - 1);

                        pq1.add(matrix1);
                        if(createPath(pNode, giveHeuristics(matrix1, type), i, j - 1))
                           return; 
                    }
                 }

                if(j+1 < cols){
                    expandedNodeCount++;
                     var = i * 3 + (j + 1);
                    if( var != singleList.get(var)) {
                        int matrix1[][] = swap(matrix, i, j, i, j + 1);

                        pq1.add(matrix1);
                        if(createPath(pNode, giveHeuristics(matrix1, type), i , j + 1))
                           return;
                    }
                }
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
      return false;
  }

   static void printTree(PuzzleNode child) {
    int pathCost = 0;
    while(child != null) {
      pathCost++;
      System.out.println(child.i + " : "+ child.j);
      child = child.parent;
    }
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
            	//heuristicValue = giveGaschnigHeuristic(mat);
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
   
   static public int giveTotalManhattanDistance(int[][] matrix) {
        return giveDistance(matrix, goalState) + (2 * giveDistance(matrix, initialState));  
   }
   
    static public int giveDistance(int[][] matrix, int[][] toCompare) {
         int rows = matrix.length;
        int cols = matrix[0].length; 
        int totalManhattanDistance = 0;
        
         for(int i= 0; i< rows; i++) {
            for(int j = 0; j<cols; j++) {
                if(matrix[i][j] != 0) {
                    ArrayList<Integer> coordinates = giveCoordinates(matrix[i][j], toCompare);
                    totalManhattanDistance += Math.abs(coordinates.get(0) - i) + Math.abs(coordinates.get(1) - j);
                }
            }
        }
         return totalManhattanDistance;
    }
   
   static public int[][] giveGaschnigHeuristicMatrix(int[][] initialMatrix){
	   int rows = initialMatrix.length;
	   int cols = initialMatrix[0].length;
	   ArrayList<int[][]> posOutcomes = new ArrayList<int[][]>();
	   int iValue = giveCoordinates(0, initialMatrix).get(0);
	   int jValue = giveCoordinates(0, initialMatrix).get(1);
	   
	   //try out all possible swap combinations
	   int[][] matrix = new int[3][3];
	   int minIndex = -1;
	   int index = -1;
	   //int[] misplacedTilesCountArr = new int[10];
	   ArrayList<Integer> misplacedTilesArr = new ArrayList<Integer>();
	   int misplacedTiles = 100;
	   int min = 100;
	   for(int x=0; x<rows; x++){
                for(int y=0; y<cols; y++){
                    if( (x==iValue && y==jValue) || (x == 0 && y == 0)){
                        continue;
                    }
                    else {
                        matrix = swap(initialMatrix, iValue, jValue, x, y);
                        posOutcomes.add(matrix);
                        index++;
                        misplacedTiles = giveMisplacedTiles(matrix);
                        
                        misplacedTilesArr.add(misplacedTiles);
                        if(misplacedTiles < min){
                            min = misplacedTiles;
                            minIndex = index;
                        }
                    }
                }
	   }
           
	   //get the optimal matrix in posOutcomes with index minIndex
	   return posOutcomes.get(minIndex);	   
	   
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
   
   static public void saveState(boolean isStateSpace, int[][] stateSpace) {
       int rows = stateSpace.length;
       int cols = stateSpace[0].length; 
        ArrayList<Integer> list = new ArrayList<Integer>();
        
        for(int i= 0; i< rows; i++) {
            for(int j = 0; j<cols; j++) {
                list.add(stateSpace[i][j]);
            }
        }
        if(isStateSpace) {
            stateSpaceMap.put(list, 1);
        }
        else {
            inputMap.put(list, 1);
        }
   } 

   static public Boolean checkInStateSpace(boolean isStateSpace, int[][] stateSpace) { 
       boolean retVal = false;
       if(isStateSpace) {
            if(stateSpaceMap.containsKey(convertToList(stateSpace)))
                retVal = true;
       }
       else {
            if(inputMap.containsKey(convertToList(stateSpace)))
                retVal = true;           
       }
       return retVal;
   }

  static public int[][] gaschnig(int[][] matrix) {
      ArrayList<Integer> list = convertToList(matrix);
      int blankPos = -1;
      int toSwapPos = -1;
      
      for (int i = 0; i < list.size(); i++) {
          
          if(list.get(i) == 0) {
              blankPos = i;
              if(blankPos == 0) {
              for(int l = 1; l < list.size(); l++) {
                  if(list.get(l) != l) {
                       list.set(0, list.get(l));
                       list.set(l, 0);
                       blankPos = l;
                       break;
                    }
                  }
              }
              break;
          }
          
          
      }
      
      for (int j = 0; j < list.size(); j++) {
          if(list.get(j) == blankPos) {
              toSwapPos = j;
              break;
          }
      }
      
      int temp = list.get(toSwapPos);
      list.set(blankPos, temp);
      list.set(toSwapPos, 0);
      
      return convertListToMatrix(list);
  }
  
  static public ArrayList<Integer> convertToList(int[][] matrix) {
      int rows = matrix.length;
      int cols = matrix[0].length; 
       
      ArrayList<Integer> list = new ArrayList<>();
        
      for(int i= 0; i< rows; i++) {
        for(int j = 0; j<cols; j++) {
            list.add(matrix[i][j]);
        }
      }
      return list;
  }
  
  static public int[][] convertListToMatrix(ArrayList<Integer> list) {
      int[][] matrix = new int[3][3];
      int count = 0;
      for(int i= 0; i< list.size() / 3; i++) {
         for(int row = 0; row < list.size() / 3; row++) {
             matrix[i][row] = list.get(count);
             count++;
         }
      }   
      return matrix;
  }
 
    static public Boolean checkGoalState(int[][] curMatrix) {
        int rows = curMatrix.length;
        int cols = curMatrix[0].length; 
        
        ArrayList<Integer> coordinates = new ArrayList<Integer>();
        for(int i= 0; i< rows; i++) {
            for(int j = 0; j<cols; j++) {
                if(curMatrix[i][j] != goalState[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    static public void printMatrix(int[][] curMatrix) {
         int rows = curMatrix.length;
        int cols = curMatrix[0].length; 
       
        for(int i= 0; i< rows; i++) {
            for(int j = 0; j<cols; j++) {
                System.out.print(curMatrix[i][j] );
            }
            System.out.println("");
        }
        System.out.println("\n");
    }
    
    static public int[][] generateInputMatix() {
        int[][] mat = goalState;
        int rows = mat.length;
        int cols = mat[0].length, i, j;
        
        int numSteps = 14 + (int) (Math.random() * (10 - 8));
        ArrayList<Integer> coordinates = new ArrayList<Integer>();
        
        for(int step = 0; step < numSteps;) {
            coordinates = giveCoordinates(0, mat);
            i = coordinates.get(0);
            j = coordinates.get(1);
            
            int direction = (int) (Math.random() * 4);
            switch(direction) {
                case 0:
                    if((i - 1) >= 0) {
                        mat = swap(mat, i, j, (i - 1), j);
                        step++;
                    }
                break;
                  
                case 1:
                    if(i+1 < rows){
                        mat = swap(mat, i, j, (i + 1), j);
                        step++;
                    }
                break;
                    
                case 2:
                    if(j-1 >= 0){
                        mat = swap(mat, i, j, i , (j - 1) );
                        step++;
                    }
                break;
                    
                case 3:
                    if(j+1 < cols){
                        mat = swap(mat, i, j, i, (j + 1));
                        step++;
                    }
                break;
            }
        }
        
        if(checkInStateSpace(false, mat)) {
            mat = generateInputMatix();
        }
        
        saveState(false, mat);
        return mat;
    }
}

