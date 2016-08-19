package maze.squares;

import org.jetbrains.annotations.NotNull;

public class SquareFactory {
    public static @NotNull Square getSquare(byte inputCharacter) {
        switch (inputCharacter) {
            case 'X':
                return new MazeSquare(MazeSquare.Type.WALL);
            case ' ':
                return new MazeSquare(MazeSquare.Type.SPACE);
            default:
                throw new IllegalArgumentException();
        }
    }
}
