package pacman;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
/**
 *
 * @author panindra
 */
public class GraphSearch {
    LinkedList<Node> frontier = new LinkedList<>();
    LinkedList<Node> expansionList = new LinkedList<>();
    PriorityQueue<Node> pq1;
     
    void  createStartNode(int[][] matrix, final Algorithm algo) {
        Node startNode = new Node();
        startNode.setCostToGoal(0);
        startNode.setCostFromStart(0);
        startNode.setTotalCost(0);
        startNode.setNodeType(NodeType.StartNode);
        //startNode.id = nodeId;

        for(int x = 0; x < matrix.length; x ++ ){
          for(int y = 0; y < matrix[0].length; y ++ ){
              if(matrix[x][y] == -2) {
                startNode.setI(x);
                startNode.setJ(y);
              }
          }
        }

        frontier.add(startNode);
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
        
        calculatepath(matrix);
  } 
     
    void calculatepath(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length; 
        
        Boolean visitedFlag = false;
        //PriorityQueue<Integer> pq2;
        
        while(pq1 != null) {
            Node top = pq1.poll();
            
            visitedFlag = false;
            
            for (Node visitedList : expansionList) {
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
             
             expansionList.add(top);
        }
       
  }

   Boolean createPath(Node parent, int h, int i, int j) {
      Node newNode = new Node();
      newNode.setParent(parent);
      newNode.setCostToGoal(h);
      newNode.setI(i);
      newNode.setJ(j);
      //newNode.id = nodeId;
      newNode.setCostFromStart(parent.getCostFromStart() + 1);
      newNode.setTotalCost(newNode.getCostFromStart() + newNode.getCostToGoal());
      frontier.add(newNode);
      pq1.add(newNode);
      
      if(h == 0) {
        System.out.println("End node found"); 
        printTree(newNode);
        return true;
      }
      return false;
  }

   void printTree(Node child) {
    int pathCost = 0;
    while(child != null) {
      pathCost++;
      System.out.println(child.getI() + " : "+ child.getJ());
      child = child.getParent();
    }
     System.out.println("PathCost size : "+(pathCost - 2)+" expansionList size : " + expansionList.size());
  }
   
}

  
