package maze.squares;

import maze.squares.interfaces.Square;
import org.jetbrains.annotations.NotNull;

public class SquareFactory {
    public static @NotNull Square getSquare(byte inputCharacter, Coordinate coordinate) {
        switch (inputCharacter) {
            case 'X': return new MazeSquare(MazeSquare.Type.WALL, coordinate);
            case ' ': return new MazeSquare(MazeSquare.Type.SPACE, coordinate);
            default: throw new IllegalArgumentException();
        }
    }
}
