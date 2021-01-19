package edu.uwm.cs351;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import edu.uwm.cs351.Maze.Cell;

/**
 * We found a path in the maze.
 */
public class PathSolutionDisplay extends AbstractSolutionDisplay {
	private List<Maze.Cell> path;
	/**
	 * A solution display with a path to mark
	 * @param m maze being solved, must not be null
	 * @param p path to show, must not be null
	 */
	public PathSolutionDisplay(Maze m, List<Maze.Cell> p) {
		super(m);
		path = p;
	}

	@Override
	public void draw(Graphics g, int size) {
		int i = path.get(0).row;
		int j = path.get(0).column;
		g.fillRect((2*maze.columns()-1)*size,0,2*size,size);
		g.fillRect(0,(2*maze.rows()-1)*size,size,2*size);
		for (Cell c : path) {
			g.fillRect((j+c.column+1)*size,(i+c.row+1)*size,size,size);
			i = c.row;
			j = c.column;
			g.fillRect((j+c.column+1)*size,(i+c.row+1)*size,size,size);	
		}
	}

	public List<Cell> getPath() {
		return new ArrayList<>(path); // don't give clients MY copy.
	}
}
