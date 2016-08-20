package maze.solver.threadedsolver;

import maze.Maze;
import maze.solver.MazeSolution;
import maze.squares.Coordinate;
import maze.squares.Mark;
import maze.squares.interfaces.Square;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Agent implements Runnable {
    private final AtomicInteger threadsAvailable;
    private final Maze maze;
    private final LinkedList<Square> path = new LinkedList<>();
    private Coordinate currentPosition;

    private static final List<Thread> threads = new LinkedList<>();

    public Agent(Maze maze, int threadsAvailable, List<Square> previousPath, Coordinate startingPosition) {
        this.maze = maze;
        this.threadsAvailable = new AtomicInteger(threadsAvailable);
        this.currentPosition = startingPosition;
        if(previousPath != null) {
            path.addAll(previousPath);
        }
    }

    @Override
    public void run() {
        threads.add(Thread.currentThread());
        if(!moveAndMark(currentPosition)) {
            return;
        }
        while(true) {
            Square nextSquare = getNextSquare();
            if(!moveAndMark(nextSquare.getCoordinate())) {
                return;
            }
            if(maze.isExit(nextSquare)) {
                //Go back and mark squares as gold
                for(int a = 0;a < path.size(); a++) {
                    path.get(a).setNextGold(a == path.size() - 1 ? maze.getSquare(currentPosition) : path.get(a + 1));
                    path.get(a).mark(Mark.GOLD);
                }
                return;
            }
        }
    }

    public static List<Thread> getAllThreads() {
        return threads;
    }

    private Square getNextSquare() {

        Square currentSquare = maze.getSquare(currentPosition);
        if(currentSquare.isMarked(Mark.GOLD)) {
            return currentSquare.getNextGold();
        }

        //Filter dead nodes.
        List<Square> viableNeighbours = maze
                .getNeighbours(currentPosition)
                .filter(s -> !s.isMarked(Mark.DEAD)) //Ensure that none of the "neighbours" are marked as dead
                .filter(s -> !path.contains(s)) //Ensure that we didn't come from there.
                .collect(Collectors.toList());

        //Only way we can go is to follow our previous path.
        if(viableNeighbours.size() == 0) {
            currentSquare.mark(Mark.DEAD);
            return path.getLast();
        }

        //If we only have one choice take it
        if(viableNeighbours.size() == 1) {
            return viableNeighbours.iterator().next();
        }

        //Otherwise, if there is a path which is gold, take it
        for(Square s : viableNeighbours) {
            if(s.isMarked(Mark.GOLD)) {
                return s;
            }
        }

        //No gold paths, more than one neighbour.
        List<Square> unexploredNeighbours = viableNeighbours.stream()
                .filter(s -> !s.isMarked(Mark.LIVE)) //Only take paths that are not marked as live
                .collect(Collectors.toList());

        //There is only one unexplored neighbour. Take it!
        if(unexploredNeighbours.size() == 1) {
            //There is exactly one explored neighbour. Take it.
            return unexploredNeighbours.get(0);
        } else {
            //We are going to explore unexplored routes if there are any (At this point there must either be 0 or more than 1). Otherwise we will explore not dead paths.
            List<Square> toExplore;
            if(unexploredNeighbours.size() > 1) {
                toExplore = unexploredNeighbours;
            } else {
                toExplore = viableNeighbours;
            }

            //We only have one in the group! just take the first path :(
            if(threadsAvailable.get() == 1) {
                //We only have one thread. We cannot split.
                return toExplore.get(0);
            }

            //Split this agent into |toExplore| new agents. Then continue of the toExplore[0] path
            int totalAvailable = threadsAvailable.get();
            int splitOffSize = totalAvailable / toExplore.size();
            if(toExplore.size() > totalAvailable) {
                splitOffSize = 1;
            }

            if(totalAvailable > 1) {
                for (int a = 1; a < toExplore.size(); a++) {
                    int remaining = threadsAvailable.addAndGet(-splitOffSize);
                    Agent newAgent = new Agent(maze, splitOffSize, path, toExplore.get(a).getCoordinate());

                    new Thread(newAgent).start();
                    if (remaining == 1) {
                        break;
                    }
                }
            }
            return toExplore.get(0);
        }
    }

    private boolean moveAndMark(Coordinate coordinate) {
        //Remove our self from the current square so no one can attach to us.
        Square currentSquare = maze.getSquare(currentPosition);
        currentSquare.removeVisitor();
        Square newSquare = maze.getSquare(coordinate);
        Agent newVisitor = (Agent)newSquare.setVisitor(this);

        //Check if we have bumped into another thread.
        if(newVisitor != this) {
            //Record the value we think our new group has.
            int expectedValue = newVisitor.threadsAvailable.get();
            int ourGroupSize = threadsAvailable.get();

            //Update the other group with the new amount only if they haven't changed their numbers. Also change our group size to 0 to make sure other threads joining to us fail.
            if(!newVisitor.threadsAvailable.compareAndSet(expectedValue, expectedValue + threadsAvailable.getAndSet(0))) {
                //The update failed. That means the other threads group size changed, rollback
                threadsAvailable.set(ourGroupSize);
                //Try this whole process again.
                return moveAndMark(coordinate);
            }
            //We successfully joined another group. We can die now!
            return false;
        }
        //No problem moving to the coordinate. Keep running.
        //If our path contains the square we are moving to we must be backtracking.
        if(path.contains(newSquare)) {
            //Remove dead paths from our followed path
            int index = path.indexOf(newSquare);
            for(int a = index;a < path.size();a++) {
                path.remove(a);
            }
        } else {
            //We have never been here so we simply add the new position to our path
            path.add(maze.getSquare(currentPosition));
            //We need to mark any new square as live (doesn't matter if the path is actually gold)
            newSquare.mark(Mark.LIVE);
        }
        //Update our current position to reflect our actual new position.
        currentPosition = newSquare.getCoordinate();
        return true;
    }

    public MazeSolution getSolution() {
        return new MazeSolution(path);
    }

    public int getGroupSize() {
        return threadsAvailable.get();
    }
}
