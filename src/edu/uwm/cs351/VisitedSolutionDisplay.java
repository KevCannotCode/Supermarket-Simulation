package edu.uwm.cs351;

import java.awt.Graphics;

/**
 * A solution display where we just mark the places
 * we were able to reach.
 */
public class VisitedSolutionDisplay extends AbstractSolutionDisplay {
	private boolean[][] marked;

	/**
	 * Create a display for the given maze and array indicating what spots
	 * we were able to visit.
	 * @param m maze being solved
	 * @param tried the points of the maze that we <i>did</i> reach.
	 */
	public VisitedSolutionDisplay(Maze m, boolean[][] tried) {
		super(m);
		marked = tried;
	}

	@Override
	public void draw(Graphics g, int size) {
		if (marked[maze.rows()-1][0]) {
			g.fillRect(0, (2*maze.rows()-1)*size, size, 2*size);
		}
		if (marked[0][maze.columns()-1]) {
			g.fillRect((2*maze.columns()-1)*size, 0,2*size, size);
		}
		for (int i=0; i < maze.rows(); ++i) {
			for (int j=0; j < maze.columns(); ++j) {
				if (marked[i][j]) {
					g.fillRect((2*j + 1)*size, (2*i+1)*size, size, size);
					if (i+1 < maze.rows() && maze.isOpenDown(i,j) && marked[i+1][j])
						g.fillRect((2*j+1)*size, 2*size*(i+1), size, size);
					if (j+1 < maze.columns() && maze.isOpenRight(i,j) && marked[i][j+1])
						g.fillRect(2*size*(j+1), (2*i+1)*size, size, size);
				}
			}
		}
	}

	/**
	 * Was this spot visited?
	 * @param i row (must be in range)
	 * @param j column (must be in range)
	 * @return whether this spot of the maze was visited.
	 */
	public boolean isVisited(int i, int j) {
		return marked[i][j];
	}
}
