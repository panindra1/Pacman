/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman;
/**
 *
 * @author panindra
 */

enum HUERISTIC_TYPE {
    MISPLACED, MANHATTAN, GASCHNIG
};

enum MATRIX_TYPE {
    GOAL, INITIAL
};

enum MAP_TYPE {
    SAVESTATEMAP, INPUTMAP
}

public class Puzzle {
    //define goal state and set the goal state and pass the heuristic
    public static void main(String[] args) throws Exception {
        
        int[][] goalState = {{0, 1, 2},
                            {3, 4, 5},
                            {6, 7, 8}};
        EightPuzzle eightPuzzle = new EightPuzzle();
        eightPuzzle.setGoalState(goalState);
        //pass the right heuristic
        eightPuzzle.startEightPuzzle(HUERISTIC_TYPE.MANHATTAN);
    }
    
}
