package maze;

import maze.solver.MazeSolution;
import maze.squares.Coordinate;
import maze.squares.Mark;
import maze.squares.SquareFactory;
import maze.squares.interfaces.Square;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

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
                int col = position % size;
                int row = Math.floorDiv(position, size);
                squares[col][row] = SquareFactory.getSquare(input, new Coordinate(row, col));
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
                exit = result.get().getCoordinate();
                break;
            }
        }
        if(exit == null) {
            throw new RuntimeException("Invalid grid file");
        }
    }


    @Override
    public String toString() {
        return toString(null);
    }

    @SuppressWarnings("ConstantConditions")
    public String toString(MazeSolution solution) {
        StringBuilder build = new StringBuilder("Generator: \n");
        for(int y = 0; y < squares[0].length;y++) {
            for(int x = 0; x < squares.length; x++) {
                //build.append(squares[x][y].isWall() ? " \u2588 " : (solution != null && solution.isCoordinateInPath(new Coordinate(y, x)) ? " X " : "   "));
                String c;
                if(getSquare(y, x).isMarked(Mark.DEAD)) {
                    c = " D ";
                } else if (getSquare(y, x).isMarked(Mark.GOLD)) {
                    c = " G ";
                } else if(getSquare(y, x).isMarked(Mark.LIVE)) {
                    c = " L ";
                } else {
                    c = "   ";
                }
                if(solution != null && solution.isCoordinateInPath(new Coordinate(y, x))) {
                    c = " \u2022 ";
                }
                build.append(squares[x][y].isWall() ? " \u2588 " : c);
            }
            build.append('\n');
        }
        build.append("Entrance: ").append(entrance);
        build.append("Exit: ").append(exit);
        return build.toString();
    }



    private Square getSquare(int row, int col) {
        if(!validCoordinate(row, col)) {
            return null;
        }
        return squares[col][row];
    }

    public Square getSquare(Coordinate c) {
        if(c == null) {
            return null;
        }
        return getSquare(c.getRow(), c.getColumn());
    }

    public Square getEntrance() {
        return getSquare(entrance);
    }

    public boolean isExit(Square square) {
        return(square == getSquare(exit));
    }

    public Square getExit() {
        return getSquare(exit);
    }

    public Stream<Square> getNeighbours(Coordinate position) {
        Square[] neighbours = new Square[] {
                getSquare(position.east()),
                getSquare(position.south()),
                getSquare(position.north()),
                getSquare(position.west())
        };
        return Arrays.stream(neighbours).filter(s -> s != null && s.isSpace());
    }

    public boolean validCoordinate(Coordinate c) {
        return validCoordinate(c.getRow(), c.getColumn());
    }

    public boolean validCoordinate(int row, int col) {
        return !(row >= squares.length || col >= squares[0].length || row < 0 || col < 0);
    }
}
