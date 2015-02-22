package pacman;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
/**
 *
 * @author panindra
 */
public class GraphSearch {
    //member variables to hold data.
    private LinkedList<Node> mExpansionList = new LinkedList<>(); 
    private PriorityQueue<Node> mPriorityQueue;
    private File mFile;
    
    //Create start node.. This will be called initally. create start node and make initializations
    void  createStartNode(int[][] matrix, final Algorithm algo, File file) {
        mFile = file;
        Node startNode = new Node();
        startNode.setCostToGoal(0);
        startNode.setCostFromStart(0);
        startNode.setTotalCost(0);
        startNode.setNodeType(NodeType.StartNode);
  
        for(int x = 0; x < matrix.length; x++ ){
          for(int y = 0; y < matrix[0].length; y ++ ){
              if(matrix[x][y] == -2) {
                startNode.setI(x);
                startNode.setJ(y);
                break;
              }
          }
        }
        //comparator to modify priority queue behavior for A star and Greedy best first search.
        mPriorityQueue = new PriorityQueue<Node>(10, new Comparator<Node>(){
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
        
        mPriorityQueue.add(startNode);
        mExpansionList.add(startNode);
        
        calculatepath(matrix);
  } 
     //The driver program for the algortihm. It tries to create path for all four nodes surrounding the moving node.
    void calculatepath(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length; 
        
        Boolean visitedFlag = false;
        
        while(mPriorityQueue != null) {
            Node top = mPriorityQueue.poll();
            
            visitedFlag = false;
            
            for (Node visitedList : mExpansionList) {
                if ((top.getI() == visitedList.getI() && top.getJ() == visitedList.getJ()) && top.getNodeType() != NodeType.StartNode) {
                    visitedFlag = true;
                }
            }
            if(visitedFlag) {
                continue;
            }
            
            int i = top.getI();
            int j = top.getJ();
             // i-1,j // i+1, j // i,j-1  // i,j+1
            //Check all four directions, if possble create path in that direction and check.
             if((i - 1) >= 0 && matrix[i-1][j] >= 0) {
                 if(createPath(top, matrix[i -1][j], i - 1, j)) 
                    return;
             }

             if(i+1 < rows && matrix[i+1][j] >= 0){
                 if(createPath(top, matrix[i + 1][j], i + 1, j))
                    return;
             }
             
             if(j-1 >= 0 && matrix[i][j - 1] >= 0){
                 if(createPath(top, matrix[i][j - 1], i, j - 1))
                    return;  
              }

             if(j+1 < cols && matrix[i][j + 1] >= 0){
                 if(createPath(top, matrix[i][j + 1], i , j + 1))
                    return;
             }
             
             //finally add only the expanded node to the list
             mExpansionList.add(top);
        }
  }
    
   //create path for each of the nodes and establish link with the parent and print when the heuristic becomes zero.
   Boolean createPath(Node parent, int h, int i, int j) {
      Node newNode = new Node();
      newNode.setParent(parent);
      newNode.setCostToGoal(h);
      newNode.setI(i);
      newNode.setJ(j);
      
      // set path from the parent also for A star.
      newNode.setCostFromStart(parent.getCostFromStart() + 1);
      newNode.setTotalCost(newNode.getCostFromStart() + newNode.getCostToGoal());
     
      mPriorityQueue.add(newNode);
      
      //check for the end node. i.e when the heuristic reaches zero.
      if(h == 0) {
        System.out.println("End node found"); 
        printTree(newNode);
        return true;
      }
      return false;
  }

   //function to print the tree.
   void printTree(Node child) {
    int pathCost = 0;
    
    MazeParser maze = new MazeParser(mFile);
        try { // use maze parser to print the path traversed
            maze.printMaze(maze.getMazeMatrix(), child);
        } catch (Exception ex) {
            
        }
        
    while(child!= null) {
      pathCost++;
      System.out.println(child.getI() + " : "+ child.getJ());
      child = child.getParent();
    }
    System.out.println("PathCost size : "+(pathCost - 2)+" expansionList size : " + mExpansionList.size());
  }
   
   // function to print the matrix
   private void printMatrix(int[][] curMatrix) {
        int rows = curMatrix.length;
        int cols = curMatrix[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(curMatrix[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("\n");
    }
}

  
