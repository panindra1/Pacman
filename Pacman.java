/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author panindra
 */

enum Algorithm { AStar, GreedyBestFirst};

class Pacman {
 
  public static void main(String[] args) throws Exception {
    //give the file name here.
      
    File inputFile = new File("smallMaze.txt");
    MazeParser maze;
      maze = new MazeParser(inputFile);
    int[][] mat = null;
      try {
          mat = maze.getMazeMatrix();
      } catch (Exception ex) {
          Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
      }
     
    //A star algortihm
    GraphSearch astar = new GraphSearch();
    astar.createStartNode(mat, Algorithm.AStar, inputFile);      
    
    //uncomment this to run GBFS star algortihm
    //GraphSearch astar = new GraphSearch();
    //astar.createStartNode(mat, Algorithm.GreedyBestFirst);      
    
    //Uncomment this to run BFS.
     /*BFS bfs = new BFS(maze.getMaze());
     bfs.parseMaze(maze);*/
    
    //Uncomment this to run DFS.
    /* DFS dfs = new DFS(maze.getMaze());
    dfs.parser = maze;
    dfs.parseMaze();*/    
  }
 
}

 
