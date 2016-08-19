package maze;

import maze.squares.Coordinate;
import maze.squares.Square;
import maze.squares.SquareFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class Maze {

    private Square[][] squares;
    private Coordinate entrance;
    private Coordinate exit;

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

        for(int a = 1; a < squares.length - 1; a++) {
            Square[] edgeSquares = new Square[] {
                    getSquare(0, a),
                    getSquare(a, 0),
                    getSquare(squares.length - 1, a),
                    getSquare(a, squares.length - 1)
            };
            Optional<Square> result = Arrays.stream(edgeSquares).filter(s -> s.isSpace() && s != getEntrance()).findFirst();
            if(result.isPresent()) {
                exit = getCoordinate(result.get());
                break;
            }
        }
        if(exit == null) {
            throw new RuntimeException("Invalid grid file");
        }
    }

    private Coordinate getCoordinate(Square square) {
        for(int col = 0; col < squares.length;col++) {
            for(int row = 0; row < squares[col].length;row++) {
                if(getSquare(row, col) == square) {
                    return new Coordinate(row, col);
                }
            }
        }
        throw new RuntimeException("Square not found");
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    @Override
    public String toString() {
        StringBuilder build = new StringBuilder("Maze: \n");
        for(int y = 0; y < squares[0].length;y++) {
            for(int x = 0; x < squares.length; x++) {
                build.append(squares[x][y].isWall() ? " \u2588 " : "   ");
            }
            build.append('\n');
        }
        build.append("Entrance: ").append(entrance);
        build.append("Exit: ").append(exit);
        return build.toString();
    }

    public Square getSquare(int row, int col) {
        if(row >= squares.length || col >= squares[0].length || row < 0 || col < 0) {
            throw new IllegalArgumentException();
        }
        return squares[col][row];
    }

    public Square getSquare(Coordinate c) {
        if(c == null) {
            throw new IllegalArgumentException();
        }
        return getSquare(c.getRow(), c.getColumn());
    }

    public Square getEntrance() {
        return getSquare(entrance);
    }

    public boolean isExit(Square square) {
        return(square == getSquare(exit));
    }
}
