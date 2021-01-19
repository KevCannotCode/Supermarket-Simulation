package edu.uwm.cs351;

/**
 * An abstract class for solution display that records which maze we are solving.
 */
public abstract class AbstractSolutionDisplay implements SolutionDisplay {
	protected final Maze maze;
	
	public AbstractSolutionDisplay(Maze m) {
		maze = m;
	}
}
