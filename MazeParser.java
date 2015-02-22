package pacman;

import java.util.*;
import java.io.*;

public class MazeParser {

    File file;

    public MazeParser() {
    }

    public MazeParser(File file) {
        this.file = file;
    }
    
    public int[][] getMazeMatrix() throws Exception {
        int[][] output;
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<int[]> lineList = new ArrayList<int[]>();
        String line;
        int goalX = 0, goalY = 0, l = 0;
        while ((line = br.readLine()) != null) {
            int[] nodeArray = new int[line.length()];
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '.') {
                    goalX = i;
                    goalY = l;
                    System.out.println("goalX = " + goalX + ", goalY = " + goalY);
                    nodeArray[i] = 0;
                } else if (line.charAt(i) == '%') {
                    nodeArray[i] = -1;
                } else if (line.charAt(i) == 'P') {
                    nodeArray[i] = -2;
                }
            }
            lineList.add(nodeArray);
            l++;
        }
        output = new int[lineList.size()][];
        for (int i = 0; i < lineList.size(); i++) {
            output[i] = lineList.get(i);
        }
        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < output[i].length; j++) {
                if (output[i][j] == 0) {
                    output[i][j] = Math.abs(j - goalX) + Math.abs(i - goalY);
                }
            }
        }
        return output;
    }

    public void printMaze(int[][] maze, Coordinate goal) {
        Coordinate temp = null;
        while (goal != null) {
            temp = goal;
            System.out.println("Path back : " + goal);
            maze[goal.y][goal.x] = -5;
            goal = goal.parent;
        }
        maze[temp.y][temp.x] = -3;
        printChars(maze);
    }
    
    public void printMaze(int[][] maze, Node goal) {
        Node temp = null;
        while (goal != null) {
            temp = goal;
            System.out.println("Path back : " + goal.getJ() + " "+ goal.getI() + " : "+maze[goal.getI()][goal.getJ()]);
            maze[goal.getI()][goal.getJ()] = -5;
            goal = goal.getParent();
        }
        maze[temp.getI()][temp.getJ()] = -3;
        printChars(maze);
    }

    private void printChars(int[][] maze) {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == -1) {
                    System.out.print("%");
                } else if (maze[i][j] == -2 || maze[i][j] == -5) {
                    System.out.print(".");
                } else if (maze[i][j] == -3) {
                    System.out.print("P");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

}
