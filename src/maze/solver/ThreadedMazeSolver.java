package maze.solver;

import maze.Maze;
import maze.solver.interfaces.MazeSolver;
import maze.solver.threadedsolver.Agent;

class ThreadedMazeSolver implements MazeSolver {

    private Maze maze;
    private int maxNumberOfThreads;

    ThreadedMazeSolver(Maze maze, int maxNumberOfThreads) {
        this.maze = maze;
        this.maxNumberOfThreads = maxNumberOfThreads;
    }

    @Override
    public MazeSolution solveMaze() throws InterruptedException {
        Agent initialAgent = new Agent(maze, maxNumberOfThreads, null, maze.getEntrance().getCoordinate());
        Thread initialThread = new Thread(initialAgent);
        initialThread.start();
        initialThread.join();

        for(int a = 0;a < Agent.getAllThreads().size();a++) {

            Agent.getAllThreads().get(a).join();
        }
        Agent exitAgent = (Agent)maze.getExit().setVisitor(null);
        if(exitAgent.getGroupSize() != maxNumberOfThreads) {
            System.out.println(maxNumberOfThreads);
            System.out.println(exitAgent.getGroupSize());
            throw new RuntimeException("Not everyone made it");
        }

        return exitAgent.getSolution();
    }
}
