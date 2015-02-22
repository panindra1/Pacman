package pacman;

import java.util.*;
import java.util.concurrent.*;

class DFS
{
    Maze maze;
    MazeParser parser = null;
    public DFS(Maze maze)
    {
        this.maze = maze;
    }

    public void parseMaze() throws Exception
    {
        Set<Coordinate> visited = new HashSet<Coordinate>();
        Coordinate start = new Coordinate(maze.startx, maze.starty, null);
        visited.add(start);
        parseNode(start, maze, visited);
    }

    private void parseNode(Coordinate coordinate, Maze maze, Set<Coordinate> visited)
    {
        List<Coordinate> neighbors = getNeighbors(maze.mazeMatrix, coordinate);
        for(Coordinate neighbor : neighbors)
        {
            if(neighbor.x == maze.goalx && neighbor.y == maze.goaly)
            {
                writeMazeOutput(maze.mazeMatrix, neighbor);
                return;
            }
            if(!visited.contains(neighbor))
            {
                System.out.println("Neighbor added : " + neighbor);
                visited.add(neighbor);
                parseNode(neighbor, maze, visited);
            }
        }
    }

    private void writeMazeOutput(int[][] maze, Coordinate current)
    {
        //parser.printMaze(maze, current);
        System.exit(0);
    }

    private List<Coordinate> getNeighbors(int[][] maze, Coordinate coordinate)
    {
        List<Coordinate> neighbors = new LinkedList<Coordinate>();
        if(coordinate.x > 0 && maze[coordinate.y][coordinate.x - 1] != -1)
        {
            neighbors.add(new Coordinate(coordinate.x - 1, coordinate.y, coordinate));
        }
        if(coordinate.y > 0 && maze[coordinate.y - 1][coordinate.x] != -1)
        {
            neighbors.add(new Coordinate(coordinate.x, coordinate.y - 1, coordinate));
        }
        if(coordinate.x <= maze[0].length && maze[coordinate.y][coordinate.x + 1] != -1)
        {
            neighbors.add(new Coordinate(coordinate.x + 1, coordinate.y, coordinate));
        }
        if(coordinate.y <= maze.length && maze[coordinate.y + 1][coordinate.x] != -1)
        {
            neighbors.add(new Coordinate(coordinate.x, coordinate.y + 1, coordinate));
        }
        return neighbors;
    }

  /*  public static void main(String[] args) throws Exception
    {
        MazeParser parser = new MazeParser(args[0]);
        DFS dfs = new DFS(parser.getMaze());
        dfs.parser = parser;
        dfs.parseMaze();
    }*/
}

/*class Coordinate
{
    int x, y;
    Coordinate parent;
    Coordinate(int x, int y, Coordinate parent)
    {
        this.x = x;
        this.y = y;
        this.parent = parent;
    }

    @Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + x;
	    result = prime * result + y;
	    return result;
    }
    @Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    Coordinate other = (Coordinate) obj;
	    if (x != other.x)
		    return false;
	    if (y != other.y)
		    return false;
	    return true;
    }
    
    public String toString()
    {
        return("[" + x + ", " + y + "]");
    }
}*/
