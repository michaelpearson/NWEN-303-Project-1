package maze.squares;

import org.jetbrains.annotations.NotNull;

public class SquareFactory {
    public static @NotNull Square getSquare(byte inputCharacter) {
        switch (inputCharacter) {
            case 'X':
                return new Wall();
            case ' ':
                return new Space();
            default:
                throw new IllegalArgumentException();
        }
    }
}
