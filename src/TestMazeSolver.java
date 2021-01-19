import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Maze;
import edu.uwm.cs351.MazeSolver;
import edu.uwm.cs351.PathSolutionDisplay;
import edu.uwm.cs351.SolutionDisplay;
import edu.uwm.cs351.VisitedSolutionDisplay;


public class TestMazeSolver extends LockedTestCase {
	
	protected static void assertContents(Collection<?> col, Object... as) {
		assertEquals(Arrays.toString(as) + ".size != " + col + ".size()",as.length,col.size());
		Iterator<?> it = col.iterator();
		for (Object a : as) {
			assertEquals(a,it.next());
		}
	}

	private Maze maze;
	private MazeSolver solver;
	private SolutionDisplay solution;
	
	protected Maze.Cell c(int i, int j) { return maze.makeCell(i, j); }
	
	protected void ask(String question, String answer) {}
	
	public void test() {
		// Answer the following questions about the design of MazeSolver
		ask("What is the name of the method that looks for a path?",
				Ts(86258809));
		// In the next two questions give the name of the class
		ask("What sort of solution display is returned if a solution is found?",
				Ts(390735482));
		ask("What if no path can be found?",
				Ts(859214686));
	}
	
	public void test0() {
		maze = new Maze(1,1);
		solver = new MazeSolver(maze);
		solution = solver.findPath();
		PathSolutionDisplay psd = (PathSolutionDisplay)solution;
		assertContents(psd.getPath(),c(0,0));
	}

	public void test1() {
		maze = new Maze(1,2);
		solver = new MazeSolver(maze);
		solution = solver.findPath();
		VisitedSolutionDisplay vsd = (VisitedSolutionDisplay)solution;
		assertFalse(vsd.isVisited(0, 1));
		assertTrue(vsd.isVisited(0, 0));
	}

	public void test2() {
		maze = new Maze(2,1);
		solver = new MazeSolver(maze);
		solution = solver.findPath();
		VisitedSolutionDisplay vsd = (VisitedSolutionDisplay)solution;
		assertFalse(vsd.isVisited(0, 0));
		assertTrue(vsd.isVisited(1, 0));
	}
	
	public void test3() {
		maze = new Maze(1,2);
		solver = new MazeSolver(maze);
		maze.setOpenRight(0, 0, true);
		solution = solver.findPath();
		PathSolutionDisplay psd = (PathSolutionDisplay)solution;
		assertContents(psd.getPath(),c(0,0),c(0,1));
	}
	
	public void test4() {
		maze = new Maze(2,1);
		solver = new MazeSolver(maze);
		maze.setOpenDown(0, 0, true);
		solution = solver.findPath();
		PathSolutionDisplay psd = (PathSolutionDisplay)solution;
		assertContents(psd.getPath(),c(1,0),c(0,0));
	}
	
	public void test5() {
		maze = new Maze(2,2);
		solver = new MazeSolver(maze);
		maze.setOpenDown(0, 0, true);
		maze.setOpenRight(1, 0, true);
		solver = new MazeSolver(maze);
		solution = solver.findPath();
		VisitedSolutionDisplay vsd = (VisitedSolutionDisplay)solution;
		assertTrue(vsd.isVisited(0, 0));
		assertTrue(vsd.isVisited(1, 0));
		assertTrue(vsd.isVisited(1, 1));
		assertFalse(vsd.isVisited(0, 1));
	}
	
	public void test6() {
		maze = new Maze(2,2);
		solver = new MazeSolver(maze);
		maze.setOpenDown(0, 0, true);
		maze.setOpenRight(0, 0, true);
		solver = new MazeSolver(maze);
		solution = solver.findPath();
		PathSolutionDisplay psd = (PathSolutionDisplay)solution;
		assertContents(psd.getPath(),c(1,0),c(0,0),c(0,1));
	}
	
	public void test7() {
		// see test62 in TestMaze
		maze = new Maze(3,5);
		solver = new MazeSolver(maze);
		maze.setOpenRight(0, 0, true);
		maze.setOpenRight(0, 1, true);
		maze.setOpenRight(0, 2, true);
		maze.setOpenRight(1, 0, true);
		maze.setOpenRight(1, 3, true);
		maze.setOpenRight(2, 1, true);
		maze.setOpenRight(2, 2, true);
		maze.setOpenRight(2, 3, true);
		maze.setOpenDown(0, 1, true);
		maze.setOpenDown(0, 3, true);
		maze.setOpenDown(0, 4, true);
		maze.setOpenDown(1, 0, true);
		maze.setOpenDown(1, 2, true);
		maze.setOpenDown(1, 4, true);
		solution = solver.findPath();
		PathSolutionDisplay psd = (PathSolutionDisplay)solution;
		assertContents(psd.getPath(),
				c(2,0),c(1,0),c(1,1),
				c(0,1),c(0,2),c(0,3),
				c(1,3),c(1,4),c(0,4));
	}
	
	public void test8() throws IOException {
		maze = Maze.fromFile("lib"+File.separator+"noway.txt");
		solver = new MazeSolver(maze);
		solution = solver.findPath();
		VisitedSolutionDisplay vsd = (VisitedSolutionDisplay)solution;
		assertTrue(vsd.isVisited(0, 0));
		assertTrue(vsd.isVisited(0, 1));
		assertTrue(vsd.isVisited(1, 0));
		assertTrue(vsd.isVisited(1, 1));
		assertTrue(vsd.isVisited(2, 0));
		assertFalse(vsd.isVisited(2, 1));
		assertFalse(vsd.isVisited(2, 2));
		assertFalse(vsd.isVisited(2, 3));
		assertFalse(vsd.isVisited(1, 2));
		assertFalse(vsd.isVisited(1, 3));
		assertFalse(vsd.isVisited(0, 2));
		assertFalse(vsd.isVisited(0, 3));
	}
	
	public void test9() throws IOException {
		maze = Maze.fromFile("lib"+File.separator+"medium.txt");
		solver = new MazeSolver(maze);
		solution = solver.findPath();
		PathSolutionDisplay psd = (PathSolutionDisplay)solution;
		assertContents(psd.getPath(),
				c(9,0), c(9,1), c(9,2), c(8,2), c(7,2), 
				c(7,3), c(7,4), c(8,4), c(9,4), c(9,5), 
				c(9,6), c(9,7), c(9,8), c(9,9), c(8,9), 
				c(7,9), c(6,9), c(6,8), c(7,8), c(7,7), 
				c(6,7), c(6,6), c(5,6), c(4,6), c(4,5), 
				c(3,5), c(3,4), c(4,4), c(5,4), c(5,3), 
				c(5,2), c(5,1), c(4,1), c(3,1), c(2,1), 
				c(2,0), c(1,0), c(0,0), c(0,1), c(0,2), 
				c(1,2), c(2,2), c(3,2), c(3,3), c(2,3), 
				c(2,4), c(2,5), c(2,6), c(1,6), c(1,7), 
				c(2,7), c(2,8), c(1,8), c(0,8), c(0,9), 
				c(0,10), c(0,11), c(1,11), c(2,11), c(3,11), 
				c(4,11), c(4,12), c(4,13), c(3,13), c(3,12), 
				c(2,12), c(1,12), c(0,12), c(0,13), c(0,14));
	}
}
