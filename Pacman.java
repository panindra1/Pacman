/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author panindra
 */
class Pacman {
 
  public static void main(String[] args) {
    File inputFile = new File("smallMaze.txt");
    MazeParser maze;
      maze = new MazeParser(inputFile);
    int[][] mat = null;
      try {
          mat = maze.getMazeMatrix();
      } catch (Exception ex) {
          Logger.getLogger(Pacman.class.getName()).log(Level.SEVERE, null, ex);
      }
    /*int[][] mat = new int[][]{
                  {4, 3, 2, 3},
                  {3, -1, -1, 2},
                  {-2, -1, 0, 1}};*/
    AStar astar = new AStar();
    astar.createStartNode(mat);      
    astar.calculatepath(mat);
  }
}

 
