import maze.Maze;
import maze.solver.MazeSolution;
import maze.solver.SolverFactory;

import java.io.File;
import java.io.FileNotFoundException;

public class MazeSolverProject {

    public static void main(String[] argv) throws FileNotFoundException {
        Maze maze = new Maze(new File(argv[0]));
        System.out.print(maze);
        MazeSolution solution = SolverFactory.getSolver(maze).solveMaze();
        System.out.print(solution);
    }

}
