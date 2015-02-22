/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *
 * @author panindra
 */
public class EightPuzzle {
    //member variables required for the program.
    private Map<ArrayList<Integer>, Integer> stateSpaceMap = new HashMap<ArrayList<Integer>, Integer>();
    private Map<int[][], PuzzleNode> nodeMap = new HashMap<int[][], PuzzleNode>();
    private Map<ArrayList<Integer>, Integer> inputMap = new HashMap<ArrayList<Integer>, Integer>();

    private PriorityQueue<int[][]> pq1;
    private HUERISTIC_TYPE mHueristicType;
    private Boolean mIsGoalReached;
    private int[][] initialState;    
    private int[][] goalState;
    
    //geetters and setters for meber variables..
    public void setGoalState(int[][] goalState) {
        this.goalState = goalState;
    }
    //Initial program to be called. get the input matrix from the matrix and continue the program
    public void startEightPuzzle(HUERISTIC_TYPE type) {
        mHueristicType = type;
        
        for(int i = 0; i < 4; i++) {
            initialState =  generateInputMatix();
            printMatrix(initialState);

            saveState(MAP_TYPE.SAVESTATEMAP, initialState);
            createStartNode(initialState);

            stateSpaceMap.clear();
            inputMap.clear();
        }
    }
    
    //start node of the path
    private void createStartNode(int[][] matrix) {
        
        /*saveState(MAP_TYPE.SAVESTATEMAP, initialState);
        createStartNode(initialState);*/
        
        int startnodeI = 0;
        int startnodeJ = 0;
        mIsGoalReached = false;

        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {
                if (matrix[x][y] == 0) {
                    startnodeI = x;
                    startnodeJ = y;
                }
            }
        }

        PuzzleNode startNode = new PuzzleNode(startnodeI, startnodeJ);
        startNode.startNode = true;
        startNode.parent = null;

        pq1 = new PriorityQueue<int[][]>(10, new Comparator<int[][]>() {
            public int compare(int[][] o1, int[][] o2) {
                if (giveHeuristics(o1) > giveHeuristics(o2)) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        pq1.add(matrix);

        nodeMap.put(matrix, startNode);
        calculatepath(matrix);
    }

    //driver program for the 8puzzle. check the surrounding nodes and if possible create path.
    private void calculatepath(int[][] matrix) {
        int expandedNodeCount = 1;
        int count = 0;

        int rows = matrix.length;
        int cols = matrix[0].length;

        Boolean visitedFlag = false;

        while (pq1.size() > 0) {
            int[][] top = pq1.poll();

            matrix = top;
            visitedFlag = checkInStateSpace(true, top);
            if (count == 0) {
                visitedFlag = false;
            }
            if (visitedFlag) {
                continue;
            }
       
            if (checkInState(top, MATRIX_TYPE.GOAL)) {           
                System.out.println("Expanded Node count = " + expandedNodeCount);
                mIsGoalReached = true;
                if(mHueristicType == HUERISTIC_TYPE.GASCHNIG) {
                    printTree(nodeMap.get(top));
                    break;
                }
            }

            count++;

            saveState(MAP_TYPE.SAVESTATEMAP, matrix);
            ArrayList<Integer> coordinates = giveCoordinates(0, matrix);

            int i = coordinates.get(0);
            int j = coordinates.get(1);

            PuzzleNode pNode = nodeMap.get(top);

            //check all four cases.
            if ((i - 1) >= 0) {
                expandedNodeCount++;
                
                int[][] newMatrix = swap(matrix, i, j, i - 1, j);
                if (!checkInStateSpace(true, newMatrix)) {
                    pq1.add(newMatrix);

                    if (createPath(pNode, newMatrix, i - 1, j)) {
                        return;
                    }
                }                
            }

            if (i + 1 < rows) {
                expandedNodeCount++;
                int[][] newMatrix = swap(matrix, i, j, i + 1, j);
                if (!checkInStateSpace(true, newMatrix)) {
                    pq1.add(newMatrix);

                    if (createPath(pNode, newMatrix, i + 1, j)) {
                        return;
                    }
                }
            }

            if (j - 1 >= 0) {
                expandedNodeCount++;

                int[][] newMatrix = swap(matrix, i, j, i, j - 1);
                if (!checkInStateSpace(true, newMatrix)) {
                    pq1.add(newMatrix);

                    if (createPath(pNode, newMatrix, i, j - 1)) {
                        return;
                    }
                }
            }

            if (j + 1 < cols) {
                expandedNodeCount++;
                
                int[][] newMatrix = swap(matrix, i, j, i, j + 1);
                if (!checkInStateSpace(true, newMatrix)) {
                    pq1.add(newMatrix);

                    if (createPath(pNode, newMatrix, i, j + 1)) {
                        return;
                    }
                }
            }
        }
    }
   //create path to all four nodes and create link to the parent
     private Boolean createPath(PuzzleNode parent, int[][] mat, int i, int j) {
        PuzzleNode newNode = new PuzzleNode(i, j);
        newNode.parent = parent;

        if (!nodeMap.containsKey(mat)) {
            nodeMap.put(mat, newNode);
        }
        
        //if goal is reached print the tree.
        if (mIsGoalReached) {
            printTree(newNode);
            return true;
        }

        return false;
    }
    
    // function to print the tree.
    private void printTree(PuzzleNode child) {
        int pathCost = 0;
        while (child != null) {
            pathCost++;
            //System.out.println(child.i + " : " + child.j);
            child = child.parent;
        }
        System.out.println("Path cost :" + (pathCost - 2));
    }
    
    //utility function to swap the lements of the matrix
    private int[][] swap(int[][] curMat, int cur_i, int cur_j, int new_i, int new_j) {
        int[][] newMat = new int[curMat.length][];
        for (int i = 0; i < curMat.length; i++) {
            newMat[i] = curMat[i].clone();
        }

        int temp = newMat[cur_i][cur_j];
        newMat[cur_i][cur_j] = newMat[new_i][new_j];
        newMat[new_i][new_j] = temp;
        return newMat;
    }
    
    //Main program to get the hueristics and number to be returned based on heuristic type.
    private int giveHeuristics(int[][] mat) {
        int heuristicValue = 0;
        switch (mHueristicType) {
            case MISPLACED:
                heuristicValue = giveMisplacedTiles(mat, MATRIX_TYPE.GOAL) + giveMisplacedTiles(mat, MATRIX_TYPE.INITIAL);
                break;
            case MANHATTAN:
                heuristicValue = giveTotalManhattanDistance(mat, MATRIX_TYPE.GOAL) + giveTotalManhattanDistance(mat, MATRIX_TYPE.INITIAL);
                break;
            case GASCHNIG:                
                heuristicValue = giveGaschnigHeuristic(mat, MATRIX_TYPE.GOAL) + giveGaschnigForInitial(mat);
                break;
        }
        return heuristicValue;
    }
    
    //function to calculate misplaced tiles at any point for the matrix.
    private int giveMisplacedTiles(int[][] mat, MATRIX_TYPE type) {
        int[][] matrixTOCompare = goalState;
        if (type == MATRIX_TYPE.INITIAL) {
            matrixTOCompare = initialState;
        }

        int rows = mat.length;
        int cols = mat[0].length;
        int numberOfMisplaced = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mat[i][j] != matrixTOCompare[i][j]) {
                    numberOfMisplaced++;
                }
            }
        }
        return numberOfMisplaced;
    }
    
    //function to give total manhatten distance heuristic for the matrix.
    private int giveTotalManhattanDistance(int[][] matrix, MATRIX_TYPE type) {
        int[][] toCompare = goalState;
        if (type == MATRIX_TYPE.INITIAL) {
            toCompare = initialState;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;
        int totalManhattanDistance = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] != 0) {
                    ArrayList<Integer> coordinates = giveCoordinates(matrix[i][j], toCompare);
                    totalManhattanDistance += Math.abs(coordinates.get(0) - i) + Math.abs(coordinates.get(1) - j);
                }
            }
        }
        return totalManhattanDistance;
    }
    
    //function to give gashnig heuristic for a matrix.
    private int giveGaschnigHeuristic(int[][] matrix, MATRIX_TYPE type) {
        PriorityQueue<int[][]> GPriorityQ = new PriorityQueue<int[][]>();
        GPriorityQ.add(matrix);
        int CostToGoal = 0;

        while (GPriorityQ.size() > 0) {
            int[][] top = GPriorityQ.poll();

            if (!checkInState(top, type)) {
                if (top[0][0] == 0) {
                    CostToGoal++;
                }
                if (giveMisplacedTiles(top, type) == 2) {
                    ArrayList<Integer> coordinates = giveCoordinates(0, top);
                    int i = coordinates.get(0);
                    int j = coordinates.get(1);

                    GPriorityQ.add(swap(top, i, j, 0, 0));
                    CostToGoal++;
                    break;
                }

                matrix = gaschnig(top, type);
                CostToGoal++;

                GPriorityQ.add(matrix);
            } else {
                break;
            }
        }
        return (CostToGoal);
    }
    
    //utility function for gashnig to give best path among the alternatives.
    private int[][] gaschnig(int[][] matrix, MATRIX_TYPE type) {
        int[][] matrixToCompare = goalState;
        if (type == MATRIX_TYPE.INITIAL) {
            matrixToCompare = initialState;
        }

        ArrayList<Integer> list = convertToList(matrix);
        ArrayList<Integer> listToCompare = convertToList(matrixToCompare);

        int blankPos = -1;
        int toSwapPos = -1;
        int blankPosInListToCompare = -1;

        for (int i = 0; i < listToCompare.size(); i++) {
            if (listToCompare.get(i) == 0) {
                blankPosInListToCompare = i;
                break;
            }
        }

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == 0) {
                blankPos = i;
                if (blankPos == blankPosInListToCompare) {
                    for (int l = 0; l < list.size(); l++) {
                        if (list.get(l) != listToCompare.get(l)) {
                            list.set(blankPos, list.get(l));
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
            if (list.get(j) == blankPos) {
                toSwapPos = j;
                break;
            }
        }

        int temp = list.get(toSwapPos);
        list.set(blankPos, temp);
        list.set(toSwapPos, 0);

        return convertListToMatrix(list);
    }

    private int giveGaschnigForInitial(int[][] matrix) {
        ArrayList<Integer> list = convertToList(matrix);
        ArrayList<Integer> initialMatrixList = convertToList(initialState);
        int blankPos = -1;
        int initialBlankPos = -1;
        int toSwapPos = -1;
        int toSwapWithItem = -1;
        int swapCount = 0;

        while (!checkInState(matrix, MATRIX_TYPE.INITIAL)) {
            for (int j = 0; j < initialMatrixList.size(); j++) {
                if (initialMatrixList.get(j) == 0) {
                    initialBlankPos = j;
                    break;
                }
            }

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == 0) {
                    blankPos = i;
                    if (blankPos == initialBlankPos) {
                        for (int l = 0; l < list.size(); l++) {
                            if (list.get(l) != initialMatrixList.get(l) && l != blankPos) {
                                list.set(blankPos, list.get(l));
                                list.set(l, 0);
                                blankPos = l;
                                swapCount++;
                                break;
                            }
                        }
                    }
                    break;
                }
            }

            //what should be at blankPos in the initialMatrix
            toSwapWithItem = initialMatrixList.get(blankPos);

        //where is this item in your current list ???
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == toSwapWithItem) {
                    toSwapPos = i;
                    break;
                }
            }

        //now swap blankPOs and toSwapPos
            int temp = list.get(toSwapPos);
            list.set(blankPos, temp);
            list.set(toSwapPos, 0);
            swapCount++;
            matrix = convertListToMatrix(list);
        }

        return swapCount;
    }
    
    //get the start position at any point.
    private ArrayList<Integer> giveCoordinates(int num, int[][] curMatrix) {
        int rows = curMatrix.length;
        int cols = curMatrix[0].length;

        ArrayList<Integer> coordinates = new ArrayList<Integer>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (num == curMatrix[i][j]) {
                    coordinates.add(i);
                    coordinates.add(j);
                    return coordinates;
                }
            }
        }
        return coordinates;
    }
    //utility function to save the current state space in spaceMap
    private void saveState(MAP_TYPE type, int[][] stateSpace) {
        int rows = stateSpace.length;
        int cols = stateSpace[0].length;
        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                list.add(stateSpace[i][j]);
            }
        }
        if (type == MAP_TYPE.SAVESTATEMAP) {
            stateSpaceMap.put(list, 1);
        } else {
            inputMap.put(list, 1);
        }
    }
    
    //fucntion to check the current state i nstatespacemap.
    private Boolean checkInStateSpace(boolean isStateSpace, int[][] stateSpace) {
        boolean retVal = false;
        if (isStateSpace) {
            if (stateSpaceMap.containsKey(convertToList(stateSpace))) {
                retVal = true;
            }
        } else {
            if (inputMap.containsKey(convertToList(stateSpace))) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    //utility function to convert matrix to list.
    private ArrayList<Integer> convertToList(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        ArrayList<Integer> list = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                list.add(matrix[i][j]);
            }
        }
        return list;
    }

    //utility function to convert list to matrix
    private int[][] convertListToMatrix(ArrayList<Integer> list) {
        int[][] matrix = new int[3][3];
        int count = 0;
        for (int i = 0; i < list.size() / 3; i++) {
            for (int row = 0; row < list.size() / 3; row++) {
                matrix[i][row] = list.get(count);
                count++;
            }
        }
        return matrix;
    }
    
    //utikity function to check a state in initial or  goal state
    private Boolean checkInState(int[][] curMatrix, MATRIX_TYPE type) {
        int[][] checkInState = goalState;
        if (type == MATRIX_TYPE.INITIAL) {
            checkInState = initialState;
        }
        int rows = curMatrix.length;
        int cols = curMatrix[0].length;

        ArrayList<Integer> coordinates = new ArrayList<Integer>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (curMatrix[i][j] != checkInState[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    //utility function to print matrix
    private void printMatrix(int[][] curMatrix) {
        int rows = curMatrix.length;
        int cols = curMatrix[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(curMatrix[i][j]);
            }
            System.out.println("");
        }
        System.out.println("\n");
    }
    
    //function to generate the input matrix
    private int[][] generateInputMatix() {
        int[][] mat = goalState;
        int rows = mat.length;
        int cols = mat[0].length, i, j;

        int numSteps = 5 + (int) (Math.random() * (10 - 8));
        ArrayList<Integer> coordinates = new ArrayList<Integer>();

        for (int step = 0; step < numSteps;) {
            coordinates = giveCoordinates(0, mat);
            i = coordinates.get(0);
            j = coordinates.get(1);

            int direction = (int) (Math.random() * 4);
            switch (direction) {
                case 0:
                    if ((i - 1) >= 0 && !checkInStateSpace(false, swap(mat, i, j, (i - 1), j))) {
                        mat = swap(mat, i, j, (i - 1), j);
                        saveState(MAP_TYPE.INPUTMAP, mat);
                        step++;
                    }
                    break;

                case 1:
                    if (i + 1 < rows && !checkInStateSpace(false, swap(mat, i, j, (i + 1), j))) {
                        mat = swap(mat, i, j, (i + 1), j);
                        saveState(MAP_TYPE.INPUTMAP, mat);
                        step++;
                    }
                    break;

                case 2:
                    if (j - 1 >= 0 && !checkInStateSpace(false, swap(mat, i, j, i, (j - 1)))) {
                        mat = swap(mat, i, j, i, (j - 1));
                        saveState(MAP_TYPE.INPUTMAP, mat);
                        step++;
                    }
                    break;

                case 3:
                    if (j + 1 < cols && !checkInStateSpace(false, swap(mat, i, j, i, (j + 1)))) {
                        mat = swap(mat, i, j, i, (j + 1));
                        saveState(MAP_TYPE.INPUTMAP, mat);
                        step++;
                    }
                    break;
            }
        }
        return mat;
    }
}
