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
    File inputFile = new File("Map2.txt");
    MazeParser maze;
      maze = new MazeParser(inputFile);
    int[][] mat = null;
      try {
          mat = maze.getMazeMatrix();
      } catch (Exception ex) {
          Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
      }
    /*int[][] mat1 = new int[][]{
                  {4, 3, 2, 3},
                  {3, -1, -1, 2},
                  {-2, -1, 0, 1}};*/
     int[][] mat1 = new int[][]{
                  {4, 3, 2, 3},
                  {3, 2, -1, 2},
                  {2, -1, 0, 1},
                  {-2, 2, -1, 2}};
     
    GraphSearch astar = new GraphSearch();
    astar.createStartNode(mat, Algorithm.AStar, inputFile);      
     
     /*BFS bfs = new BFS(maze.getMaze());
     bfs.parseMaze(maze);*/
     
    /* DFS dfs = new DFS(maze.getMaze());
    dfs.parser = maze;
    dfs.parseMaze();*/
     
     
    //astar = new GraphSearch();
    //astar.createStartNode(mat, Algorithm.GreedyBestFirst);      
  }
 
}

 
