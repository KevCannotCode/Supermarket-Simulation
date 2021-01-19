package edu.uwm.cs351;

import java.awt.Graphics;

/**
 * Show solution (or lack thereof)
 * on a graphical device.
 */
public interface SolutionDisplay {
	/**
	 * Render the solution on top of the maze drawing.
	 * @param g graphics device, must not be null
	 * @param size size to draw squares in maze
	 */
	public void draw(Graphics g, int size);
}
