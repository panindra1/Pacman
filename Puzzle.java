/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    /* static int[][] goalState = {{0, 1 ,2},
                                {3, 4 ,5},
                                {6, 7, 8}};*/
     
     static int[][] goalState = {{1 ,2, 3},
                                {4 ,5, 6},
                                {7, 8, 0}};
         
     public static void main(String[] args) throws Exception{
        
        /* int[][] initialState = {{0, 3 ,2},
                                {1, 4 ,5 },
                                {6, 7, 8}};*/
        
         
        /*int[][] initialState = {{1, 2 , 4},
                                {3, 0 ,5 },
                                {6, 7, 8}};*/
         
         
         int[][] initialState = {{1, 3, 7},
                                 {6, 0 ,5},
                                {2, 4, 8}};
         
         if(true/*checkIfSolvable(initialState)*/) {
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
        calculatepath(matrix, HUERISTIC_TYPE.GASCHNIG);
  }
     
     static void calculatepath(int[][] matrix, HUERISTIC_TYPE type) {
        if(type == HUERISTIC_TYPE.GASCHNIG) {
            //matrix = gasching(matrix);
        	matrix = giveGaschnigHeuristicMatrix(matrix);
        }
        else {
            int rows = matrix.length;
            int cols = matrix[0].length; 

            Boolean visitedFlag = false;

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
                   if(createPath(pNode, giveHeuristics(matrix1, type), i - 1, j)) 
                     return;
               }

                if(i+1 < rows){
                    int matrix1[][] = swap(matrix, i, j, i + 1, j);

                    pq1.add(matrix1);
                    if(createPath(pNode, giveHeuristics(matrix1, type), i + 1, j))
                       return;
                }

                if(j-1 >= 0){
                    int matrix1[][] = swap(matrix, i, j, i, j - 1); 

                    pq1.add(matrix1);
                    if(createPath(pNode, giveHeuristics(matrix1, type), i, j - 1))
                       return;  
                 }

                if(j+1 < cols){
                    int matrix1[][] = swap(matrix, i, j, i, j + 1); 

                    pq1.add(matrix1);
                    if(createPath(pNode, giveHeuristics(matrix1, type), i , j + 1))
                       return;
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
   
   static public int[][] giveGaschnigHeuristicMatrix(int[][] initialState){
	   int rows = initialState.length;
	   int cols = initialState[0].length;
	   ArrayList<int[][]> posOutcomes = new ArrayList<int[][]>();
	   int iValue = -1;
	   int jValue = -1;
	   
	   
	   //find where 0 is
	   for(int i=0; i<rows; i++){
		  for(int j=0; j<cols; j++){
			  if(initialState[i][j] == 0){
				  iValue = i;
				  jValue = j;
			  }
		  }
	   }
	   
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
			   if(x==iValue && y==jValue){
				   continue;
			   }
			   else {
				   matrix = swap(initialState, iValue, jValue, x, y);
				   posOutcomes.add(matrix);
				   index++;
				   misplacedTiles = giveMisplacedTiles(matrix);
				   misplacedTilesArr.add(misplacedTiles);
				   if(min < misplacedTiles){
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
        if(stateSpaceMap.containsKey(convertToList(stateSpace)))
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
  
  static public int[][] gasching(int[][] matrix) {
      ArrayList<Integer> list = convertToList(matrix);
      ArrayList<Integer> bList = new ArrayList<>();
      
      int n = 0;
      while(n <= 8) {
        for (int i = 0; i < list.size(); i++) {
          for (int j = 0; j < list.size(); j++) {
              if(list.get(j) == i) {
                  bList.add(j);
              }
          }
        }
        int temp = list.get(bList.get(n));
        int swapWith = list.get(bList.get(bList.get(n)));
        list.set(bList.get(n), swapWith);
        list.set(bList.get(bList.get(n)), temp); 
        bList.clear();
        
        Boolean sorted = true;
        ArrayList<Integer> goalList = convertToList(goalState);
        for (int i = 0; i < list.size(); i++) {
          if(list.get(i) != goalList.get(i)) {
              sorted = false;
              break;
          }
        }
        n++;
        if(sorted) {
            System.out.println(" Pathcost:" + (n + 1) +" Expanded nodes: " + (n + 1));
            break;
        }
      }
      System.out.println(" : " + list);
      /*int blankPos = -1;
      int toSwapPos = -1;
      
      for (int i = 0; i < list.size(); i++) {
          if(list.get(i) == 0) {
              blankPos = i;
              break;
          }
      }
      
      for (int j = 0; j < list.size(); j++) {
          if(list.get(j) == blankPos) {
              toSwapPos = j;
              if(toSwapPos == blankPos) {
                  
                  continue;
              }else {
                  break;
              }
          }
      }
      
      int temp = list.get(toSwapPos);
      list.set(blankPos, temp);
      list.set(toSwapPos, 0);
      */
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
  
  
}
