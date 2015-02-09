class Maze
{
    int[][] mazeMatrix;
    int startx,starty,goalx,goaly;

    public Maze() {}

    public Maze(int[][] mazeMatrix, int startx, int starty, int goalx, int goaly)
    {
        this.mazeMatrix = mazeMatrix;
        this.startx = startx;
        this.starty = starty;
        this.goalx = goalx;
        this.goaly = goaly;
    }
}
