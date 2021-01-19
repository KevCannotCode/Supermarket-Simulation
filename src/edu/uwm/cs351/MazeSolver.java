package edu.uwm.cs351;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
		// TODO: implement this method
		pending.push(maze.makeCell(maze.rows()-1, 0) );
		LinkedList <Cell> path = new LinkedList<>();
		int i  = maze.rows()-1;
		int j = 0; 
		Cell goal = maze.makeCell(0, maze.columns()-1);
		//visited[0][maze.columns()-1] = pending.peek();

		while(!pending.isEmpty()){
			Cell currentCell = pending.peek();
			path.add(currentCell);

			//			while(visited[currentCell.row][currentCell.column] != null && !pending.isEmpty()){
			//				currentCell = pending.pop();
			//				path.add(currentCell);
			//			}

			i = currentCell.row;
			j = currentCell.column; 
			visited[i][j]= currentCell;

			//goal
			if(currentCell.equals(goal) ){
				LinkedList <Cell> solution = new LinkedList<>();
				while(!pending.isEmpty())
					solution.addFirst( pending.pop());
				PathSolutionDisplay solutionPath = new PathSolutionDisplay(maze, solution);
				return solutionPath;
			}
			move(i,j,currentCell, goal);
		}
		VisitedSolutionDisplay visitedSolution = new VisitedSolutionDisplay(maze, tried(visited)); // ??
		return visitedSolution;

	}
	// Our solution uses a helper method to avoid repeating code.  This is optional.
	void move(int i, int j, Cell currentCell, Cell goal){
		//where do I go?
		Boolean stuck = true;
		while(stuck&& !pending.isEmpty()){
			if(maze.rows() > 1) {
				//Up
				if(maze.isOpenUp(i , j)) {
					if(i ==0 && visited[0] [j] == null || i!= 0 && visited[i-1] [j] == null ){
						if(i != 0) {
							pending.push( maze.makeCell(i-1, j)); 
						}
						stuck =false;
					}
				}

				//down
				if(maze.isOpenDown(i , j) && visited[i+1] [j] == null){
					pending.push( maze.makeCell(i+1, j) );
					stuck =false;
				}
			}

			if(maze.columns()>1) { 
				//Left
				if(j != 0 && maze.isOpenLeft(i , j) && visited[i] [j-1] == null){
					pending.push( maze.makeCell(i, j-1) ); 
					stuck =false;
				}

				//Right
				if(maze.isOpenRight(i , j) && visited[i] [j+1] == null ){
					pending.push( maze.makeCell(i, j+1) );
					stuck =false;
				}
			}
			//no move 
			if(stuck){
				currentCell = pending.pop();
				i = currentCell.row;
				j = currentCell.column;
				//visited[i][j]= currentCell;
			}
		}
	}

	private boolean [] [] tried (Cell [] [] visited){
		boolean [] [] triedCells = new boolean [maze.rows()][maze.columns()];
		for(int i = 0; i < maze.rows(); i++) 
			for(int j = 0; j < maze.columns(); j++)
				triedCells [i][j] = visited[i][j] == null? false: true;
		return triedCells;
	}


}
