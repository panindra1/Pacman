package pacman;

import java.util.*;
import java.util.concurrent.*;

class BFS
{
    Maze maze;
    public BFS(Maze maze)
    {
        this.maze = maze;
    }

    public void parseMaze(MazeParser parser) throws Exception
    {
        LinkedBlockingQueue<Coordinate> queue = new LinkedBlockingQueue<Coordinate>();
        Set<Coordinate> visited = new HashSet<Coordinate>();
        Coordinate start = new Coordinate(maze.startx, maze.starty, null);
        visited.add(start);
        queue.put(start);
        outer:
        do
        {
            Coordinate current = queue.poll();
            List<Coordinate> neighbors = getNeighbors(maze.mazeMatrix, current);
            for(Coordinate coordinate : neighbors) 
            {
                if(coordinate.x == maze.goalx && coordinate.y == maze.goaly)
                {
                    writeMazeOutput(maze, coordinate, parser);
                    break outer;
                }
                if(!visited.contains(coordinate))
                {
                    System.out.println("Visited : " + coordinate);
                    queue.put(coordinate);
                }
                visited.add(coordinate);
            }
        } while(queue.size() != 0);   
    }

    private void writeMazeOutput(Maze maze, Coordinate current, MazeParser parser)
    {
        //parser.printMaze(maze.mazeMatrix, current);
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

    /*public static void main(String[] args) throws Exception
    {
        MazeParser parser = new MazeParser(args[0]);
        BFS bfs = new BFS(parser.getMaze());
        bfs.parseMaze(parser);
    }*/

}

class Coordinate
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
}
