import maze.Maze;
import maze.solver.MazeSolution;
import maze.solver.SolverFactory;
import maze.solver.interfaces.MazeSolver;

import java.io.File;
import java.io.FileNotFoundException;

public class MazeSolverProject {

    public static void main(String[] argv) throws FileNotFoundException, InterruptedException {
        Maze maze = new Maze(new File(argv[0]));
        System.out.print(maze);
        MazeSolver solver = SolverFactory.getSolver(maze, Integer.parseInt(argv[1]));
        MazeSolution solution = solver.solveMaze();
        System.out.print(maze.toString(solution));
    }

}
