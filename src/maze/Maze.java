package maze;

import maze.squares.Coordinate;
import maze.squares.Square;
import maze.squares.SquareFactory;

import java.io.*;
import java.util.Scanner;

public class Maze {

    private Square[][] squares;
    private Coordinate entrance;

    public Maze(File mazeFile) throws FileNotFoundException {
        initMaze(mazeFile);
    }

    private void initMaze(File mazeFile) throws FileNotFoundException {
        Scanner mazeInput = new Scanner(mazeFile);
        int size = mazeInput.nextInt();
        squares = new Square[size][size];
        entrance = new Coordinate(mazeInput.nextInt(), mazeInput.nextInt());
        int position = 0;
        while(mazeInput.hasNextLine()) {
            byte[] line = mazeInput.nextLine().getBytes();
            for(byte input : line) {
                if (input != ' ' && input != 'X') {
                    continue;
                }
                squares[position % size][Math.floorDiv(position, size)] = SquareFactory.getSquare(input);
                position++;
            }
        }
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    @Override
    public String toString() {
        StringBuilder build = new StringBuilder("maze.Maze: \n");
        for(int y = 0; y < squares[0].length;y++) {
            for(int x = 0; x < squares.length; x++) {
                build.append(squares[x][y].toString());
            }
            build.append('\n');
        }
        return build.toString();
    }
}
