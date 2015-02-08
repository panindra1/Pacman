import java.util.*;
import java.io.*;

public class MazeParser
{
    File file;
    public MazeParser()
    {}

    public MazeParser(File file)
    {
        this.file = file;
    }

    public int[][] getMazeMatrix() throws Exception
    {
	int[][] output;
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<int[]> lineList = new ArrayList<int[]>();
        String line;
        int goalX = 0, goalY = 0, l = 0;
        while((line = br.readLine()) != null)
        {
            int[] nodeArray = new int[line.length()];
            for(int i = 0 ; i < line.length() ; i ++)
            {
                if(line.charAt(i) == '.')
                {
                    goalX = i;
                    goalY = l;
                    System.out.println("goalX = " + goalX + ", goalY = " + goalY);
                    nodeArray[i] = 0;
                }
                else if(line.charAt(i) == '%')
                {
                    nodeArray[i] = -1;
                }
                else if(line.charAt(i) == 'P')
                {
                    nodeArray[i] = -2;
                }  
            }
            lineList.add(nodeArray);
            l ++;
        }
        output = new int[lineList.size()][];
        for(int i = 0 ; i < lineList.size() ; i ++) output[i] = lineList.get(i);
        for(int i = 0 ; i < output.length ; i ++)
        {
            for(int j = 0 ; j < output[i].length ; j ++)
            {
                if(output[i][j] == 0) output[i][j] = Math.abs(j - goalX) + Math.abs(i - goalY);
            }
        }
        return output;
    }

    public static void main(String[] args) throws Exception
    {
        File file = new File(args[0]);
        MazeParser parser = new MazeParser(file);
        int[][] output = parser.getMazeMatrix();
        for(int i = 0 ; i < output.length ; i ++)
        {
            for(int j = 0 ; j < output[i].length ; j ++)
            {
                System.out.print(output[i][j] + " ");
            }
            System.out.println();
        }
    }
}

        
