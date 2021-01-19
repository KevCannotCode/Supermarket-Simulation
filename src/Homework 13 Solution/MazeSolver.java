package edu.uwm.cs351;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import edu.uwm.cs351.Maze.Cell;

/**
 * Try to solve a maze.
 */
public class MazeSolver {
	private final Maze maze;
	private Stack<Maze.Cell> pending = new Stack<Cell>();
	private Cell[][] visited;
	
	/**
	 * Create a maze solver for this maze.
	 * @param m maze to solve, must not be null
	 */
	public MazeSolver(Maze m) {
		maze = m;
		visited = new Cell[maze.rows()][maze.columns()];
	}

	/**
	 * Try to find a path, and return a solution display:
	 * either a path solution display, if a path was found,
	 * or a visited solution display if no path was found.
	 * @return solution display (must not be null)
	 */
	public SolutionDisplay findPath() {
		// #(	
		int rows = maze.rows();
		int columns = maze.columns();
		
		pending.push(maze.new Cell(rows-1,0));
		visited[rows-1][0] = maze.new Cell(0,columns-1); // force not to look again
		while (!pending.isEmpty()) {
			Cell c = pending.pop();
			// System.out.println("Looking at " + c);
			if (c.row == 0 && c.column == columns -1) {
				List<Cell> path = new ArrayList<>();
				visited[rows-1][0] = null;
				while (c != null) {
					path.add(c);
					c = visited[c.row][c.column];
				}
				Collections.reverse(path);
				return  new PathSolutionDisplay(maze,path);
			}
			int i = c.row;
			int j = c.column;
			if (maze.isOpenLeft(i,j)) helpFindPath(c,i,j-1);
			if (maze.isOpenUp(i,j)) helpFindPath(c,i-1,j);
			if (maze.isOpenRight(i,j)) helpFindPath(c,i,j+1);
			if (maze.isOpenDown(i,j)) helpFindPath(c,i+1,j);
		}
		boolean[][] marked = new boolean[rows][columns];
		for (int i=0; i < rows; ++i) 
			for (int j=0; j < columns; ++j)
				if (visited[i][j] != null) marked[i][j] = true;
		return new VisitedSolutionDisplay(maze,marked);
		/* #)
		return null; // TODO: implement this method
		## */
	}
	// #(
	
	private void helpFindPath(Cell from, int i, int j) {
		if (i < 0 || maze.rows() <= i ||
		    j < 0 || maze.columns() <= j) return;
		if (visited[i][j] != null) return;
		visited[i][j] = from;
		pending.push(maze.new Cell(i,j));
	}
	// #)
	// Our solution uses a helper method to avoid repeating code.  This is optional.
}
