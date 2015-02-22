# Pacman
Pacman algorithm for Aritifcial Intelligence class

1. pacman
2. Eight puzzle

Files in pacman.
1. Pacman.java (Main program to execute and pass the right algorithm)
2. GraphSearch.java (Logic for the pacman problem resides in this file)
3. Node.java (Utility class to keep track of the path traversed by the pacman till end node)
4. Maze.java (Utility file to print output)
5. MazeParser.java (Utility file for IO operations)
6. BFS.java (BFS algortihm) (Can be run individually also)
7. DFS.java (DFS algortihm)


Files in Eight puzzle.
1. Puzzle.java (Main program to execute and pass the right algorithm)
2. EightPuzzle.java (Logic for the Eight puzzle using A star resides in this file)
3. PuzzleNode.java (Utility class to keep track of the path traversed by the pacman till end node)
4. Maze.java (Utility file to print output)
5. MazeParser.java (Utility file for IO operations)

Instructions to run pacman.
1. Run the Pacman.java
2. Pass the correct algortihm (A star or GBFS)
3. Uncomment code for DFS and BFS.

Instructions to run eight puzzle.
1. Run the Puzzle.java file.
2. pass the right Hueristic to the function. (Misplaced, Manhattan, Gashnig).

Sample input for pacman:
%%%%%%%%%%%%%%%%%%%%%%
% %%        % %      %
%    %%%%%% % %%%%%% %
%%%%%%     P  %      %
%    % %%%%%% %% %%%%%
% %%%% %         %   %
%        %%% %%%   % %
%%%%%%%%%%    %%%%%% %
%.         %%        %
%%%%%%%%%%%%%%%%%%%%%%

Sample output for pacman:
%%%%%%%%%%%%%%%%%%%%%%
% %%        % %      %
%    %%%%%% % %%%%%% %
%%%%%%.....P  %      %
%    %.%%%%%% %% %%%%%
% %%%%.%.........%...%
%     ...%%% %%%...%.%
%%%%%%%%%%....%%%%%%.%
%..........%%........%
%%%%%%%%%%%%%%%%%%%%%%

(Path from pacman to end node ('.') in this case.)

