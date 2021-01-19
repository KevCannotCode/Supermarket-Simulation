package edu.uwm.cs351;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * A 2 dimensional maze.
 * It is entered on the last row at the left
 * and is exited at the last column at the top.
 */
public class Maze {

	private final int rows, columns;
	private boolean[][] ropen, copen;
	
	/* Explanation:
	 * ropen[i,j] is true if one can get from cell [i,j] to cell [i+1,j]
	 * copen[i,j] is true is one can get from cell [i,j] to cell[i,j+1];
	 */
	
	/**
	 * Create a maze of the given size, in which everything is blocked.
	 * @param rows number of rows, must be positive
	 * @param columns number of columns, must be positive
	 */
	public Maze(int rows, int columns) {
		if (rows < 1 || columns < 1) throw new IllegalArgumentException("Maze must not be empty");
		this.rows = rows;
		this.columns = columns;
		ropen = new boolean[rows-1][columns];
		copen = new boolean[rows][columns-1];
	}
	
	public int rows() { return rows; }
	public int columns() { return columns; }
	
	private void checkCell(int i, int j) {
		if (i < 0 || rows <= i) throw new IllegalArgumentException("row " + i + " out of range [0," + rows + ")");
		if (j < 0 || columns <= j) throw new IllegalArgumentException("column " + j + " out of range [0," + columns + ")");
	}
	
	/**
	 * Is this cell open to the left?
	 * As a special case, the bottom left cell is open to the left
	 * (this is the maze entry).
	 * @param i row
	 * @param j column
	 * @return whether there is an opening to the left.
	 * @exception IllegalArgumentException if coordinates out of range.
	 */
	public boolean isOpenLeft(int i, int j) {
		// #(
		checkCell(i,j);
		if (j == 0) return i == rows-1; // only open at bottom left
		return copen[i][j-1];
		/* #)
		return false; // TODO: implement this method.
		## */
	}
	
	/**
	 * Is this cell open to the right?
	 * @param i row
	 * @param j column
	 * @return whether there is an opening to the right.
	 * @exception IllegalArgumentException if coordinates out of range.
	 */
	public boolean isOpenRight(int i, int j) {
		// #(
		checkCell(i,j);
		if (j == columns()-1) return false; // end of maze
		return copen[i][j];
		/* #)
		return false; // TODO: implement this method.
		## */
	}
	
	/**
	 * Is this cell open above?
	 * As special case, the top right cell is open above
	 * (this is the maze exit).
	 * @param i row
	 * @param j column
	 * @return whether there is an opening going up
	 * @exception IllegalArgumentException if coordinates out of range.
	 */
	public boolean isOpenUp(int i, int j) {
		// #(
		checkCell(i,j);
		if (i == 0) return j == columns-1; // only exit at top right
		return ropen[i-1][j];
		/* #)
		return false; // TODO: implement this method.
		## */
	}
	
	/**
	 * Is this cell open below?
	 * @param i row
	 * @param j column
	 * @return whether there is an opening down down
	 * @exception IllegalArgumentException if coordinates out of range.
	 */
	public boolean isOpenDown(int i, int j) {
		// #(
		checkCell(i,j);
		if (i == rows()-1) return false; // end of maze
		return ropen[i][j];
		/* #)
		return false; // TODO: implement this method.
		## */
	}
	
	
	/// Mutators
	
	public void setOpenRight(int i, int j, boolean open) {
		checkCell(i,j);
		checkCell(i,j+1);
		copen[i][j] = open;
	}
	
	public void setOpenDown(int i, int j, boolean open) {
		checkCell(i,j);
		checkCell(i+1,j);
		ropen[i][j] = open;
	}
	
	
	/**
	 * Read in contents of a maze printed with ASCII graphics from the file.
	 * The rows and columns are already set (do not read these).
	 * Example (assuming rows = 3, columns = 4)
	 * <pre>
	 * +-+-+-+ +
	 * |   |   |
	 * + + + +-+
	 * | |     |
	 * + +---+ +
	 *       | |
	 * +-----+-+      
	 * </pre>
	 * @param r buffered reader to read lines from
	 * @throws IOException if a problem happens with reading
	 * @throws ParseException if the maze is badly formatted.
	 * (The implementation is also permitted to simply overlook 
	 * format errors)
	 */
	public void read(BufferedReader r) throws IOException {
		// #(
		clear(false);
		for (int i=0; i <= rows; ++i) {
			String rwalls = r.readLine();
			for (int j=0; j <= columns; ++j) {
				switch (rwalls.charAt(j*2)) {
				case '+': break;
				default: throw new ParseException("Expected '+', got '" + rwalls.charAt(j*2) + "'");
				}
				if (j == columns) break;
				switch (rwalls.charAt(j*2+1)) {
				case '-': break;
				case ' ': if (i > 0 && i < rows) ropen[i-1][j] = true; break;
				default: throw new ParseException("Expected '-', got '" + rwalls.charAt(j*2+1) + "'");
				}
			}
			if (i == rows) break;
			String cwalls = r.readLine();
			for (int j=0; j <= columns; ++j) {
				switch (cwalls.charAt(j*2)) {
				case '|': break;
				case ' ': if (j > 0 && j < columns) copen[i][j-1] = true; break;
				default: throw new ParseException("Expected '|', got '" + cwalls.charAt(j*2+1) + "'");
				}
				if (j== columns) break;
				switch (cwalls.charAt(j*2+1)) {
				case ' ': break;
				default: throw new ParseException("Expected ' ', got '" + cwalls.charAt(j*2+1) + "'");
				}
			}
		}
		// #)
		// TODO: Implement this method
	}
	
	/**
	 * Write out the maze in a specific textual and human readable form.
	 * For example:
	 * <pre>
	 * +-+-+-+ +
	 * |   |   |
	 * + + + +-+
	 * | |     |
	 * + +---+ +
	 *       | |
	 * +-----+-+      
	 * </pre>
	 * 
	 * @param pw print writer to write to, must not be null
	 */
	public void write(PrintWriter pw) {
		// #(
		char[][] result = new char[2*rows+3][2*columns+3];
		for (int i=0; i <= rows; ++i) {
			for (int j=0; j <= columns; ++j) {
				result[i*2][j*2] = '+'; // corners
				result[i*2+1][j*2] = '|';
				result[i*2][j*2+1] = '-';
				result[i*2+1][j*2+1] = ' ';
			}
		}
		for (int i = 0; i < rows; ++i) {
			for (int j=0; j < columns; ++j) {
				if (isOpenDown (i,j)) result[i*2+2][j*2+1] = ' ';
				if (isOpenRight(i,j)) result[i*2+1][j*2+2] = ' ';
			}
		}
		result[2*rows-1][0] = ' ';
		result[0][2*columns-1] = ' ';
		// pw.println(rows + " " + columns);
		for (int i=0; i < rows*2+1; ++i) {
			pw.println(new String(result[i],0,2*columns+1));
		}
		// #)
		// TODO: Print this maze using the same format read expects
	}

	
	/// NB: The rest of this class is done for you
	
	/**
	 * Clear maze (everything is the same open/closed).
	 * @param open whether everything should be open (or closed)
	 */
	public void clear(boolean open) {
		for (int i = 0; i < rows; ++i) {
			for (int j=0; j < columns; ++j) {
				if (i+1 != rows) ropen[i][j] = open;
				if (j+1 != columns) copen[i][j] = open;
			}
		}
	}
	
	/**
	 * Exception thrown by {@link #read} if it notices a problem.
	 *
	 */
	public static class ParseException extends IOException {
		/**
		 * Keep Eclipse happy
		 */
		private static final long serialVersionUID = 1L;

		public ParseException(String s) { super(s); }
	}
	
	/**
	 * A representation of an [row,column] coordinate.
	 * You may this useful when implementing methods, but there's
	 * no requirement that you use it.
	 */
	public class Cell {
		public final int row, column;
		public Cell(int i, int j) {
			checkCell(i,j);
			this.row = i;
			this.column = j;
		}
		@Override
		public String toString() {
			return "[" + row + "," + column + "]"; // useful for debugging
		}
		@Override
		public boolean equals(Object x) {
			if (!(x instanceof Cell)) return false;
			Cell c = (Cell)x;
			return row == c.row && column == c.column;
		}
		@Override
		public int hashCode() {
			return (row << 8) ^ column;
		}
	}
	
	/** Create a cell for row i and column j.
	 * This is a convenience method for creating new cells,
	 * since the Java syntax for doing so in a different class is tricky.
	 * You don't need to use this method.
	 * @param i row
	 * @param j column
	 * @return new Cell(i,j)
	 */
	public Cell makeCell(int i, int j) {
		return new Cell(i,j);
	}
	
	private static final int SQUARESIZE = 10;

	public static void usage()
	{
		System.out.println("Usage: maze arg... where the following arguments are supported:");
		System.out.println("  --read <filename>");
		System.out.println("  --generate R C"); // ##
		System.out.println("  --solve");
		System.out.println("  --print");
		System.out.println("There must be a maze loaded before it can be solved/printed.");
		System.exit(1);
	}
	
	public static void main(String[] args) {
		Maze maze = null;
		SolutionDisplay solution = null;
		for (int i=0; i < args.length; ++i) {
			if (args[i].equals("--read")) {
				try {
					if (++i == args.length) usage();
					maze = fromFile(args[i]);
				} catch (NumberFormatException e) {
					System.out.println("Badly formatted number of rows (or columns): " + e);
					return;
				} catch (FileNotFoundException e) {
					System.out.println("File not found: " + args[i]);
					return;
				} catch (IOException e) {
					System.out.println("Problem reading file: " + e);
					return;
				}
				System.out.println("Loaded maze from " + args[i]);
			} else if (args[i].equals("--print")) {
				if (maze != null) {
					PrintWriter pw = new PrintWriter(System.out);
					pw.println(maze.rows() + " " + maze.columns());
					maze.write(pw);
					pw.close();
				}
			} else if (args[i].equals("--generate")) { // #( 
				if ((i += 2) >= args.length) usage();
				try {
					int rows = Integer.parseInt(args[i-1]);
					int columns = Integer.parseInt(args[i]);
					maze = new Maze(rows,columns);
					maze.generateRandom();
				} catch (NumberFormatException e) {
					System.out.println("Badly formatted number of rows (or columns): " + e);
					return;
				} // #)
			} else if (args[i].equals("--solve")) {
				if (maze == null) usage();
				if ((solution = new MazeSolver(maze).findPath()) instanceof PathSolutionDisplay) {
					System.out.println("A path was found.");
				} else {
					System.out.println("No path was found.");
				}
			} else if (args[i].equals("--help")) {
				usage();
			} else {
				System.out.println("Option not understood: " + args[i]);
			}
		}
		if (maze == null) usage();
		final Maze m = maze;
		final SolutionDisplay sd = solution;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame f = new JFrame("Maze");
				MazeDisplay md = new MazeDisplay(m,SQUARESIZE);
				md.setSolution(sd);
				f.setContentPane(md);
				f.setSize((2*m.columns()+1)*SQUARESIZE,(2*m.rows()+3)*SQUARESIZE);
				f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				f.setVisible(true);
			}
		});
	}

	public static Maze fromFile(String fileName) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String firstLine = br.readLine();
			String[] nums = firstLine.split(" ");
			if (nums.length != 2) throw new ParseException("wrong format for first line of maze: " + firstLine);
			int rows = Integer.parseInt(nums[0]);
			int columns = Integer.parseInt(nums[1]);
			Maze m = new Maze(rows,columns);
			m.read(br);
			return m;
		}
	}
	
	// #(
	// The following functions are for instructor use:
	// They are not needed/defined/used by the students.
	
	public boolean connect(Cell c1, Cell c2) {
		if (c1.row == c2.row) {
			if (c1.column+1 == c2.column) {
				copen[c1.row][c1.column] = true;
			} else if (c1.column-1 == c2.column) {
				copen[c2.row][c2.column] = true;
			} else return false;
		} else if (c2.column == c1.column) {
			if (c1.row+1 == c2.row) {
				ropen[c1.row][c1.column] = true;
			} else if (c1.row-1 == c2.row) {
				ropen[c2.row][c2.column] = true;
			} else return false;
		} else return false;
		return true;
	}

	public void generateRandom() {
		clear(false);
		int remaining = rows * columns;;
		boolean[][] visited = new boolean[rows][columns];
		Stack<Cell> back = new Stack<Cell>();
		java.util.Random r = new java.util.Random();
		Cell start = new Cell(r.nextInt(rows),r.nextInt(columns));
		back.push(start);
		visited[start.row][start.column]= true;
		--remaining;
		java.util.List<Cell> next = new java.util.ArrayList<Cell>();
		while (remaining > 0) {
			Cell c = back.peek();
			next.clear();
			// check all four directions:
			if (c.row > 0 && !visited[c.row-1][c.column]) next.add(new Cell(c.row-1,c.column));
			if (c.row+1 < rows && !visited[c.row+1][c.column]) next.add(new Cell(c.row+1,c.column));
			if (c.column > 0 && !visited[c.row][c.column-1]) next.add(new Cell(c.row,c.column-1));
			if (c.column+1 < columns && !visited[c.row][c.column+1]) next.add(new Cell(c.row,c.column+1));
			if (next.isEmpty()) {
				back.pop();
			} else {
				Cell n = next.get(r.nextInt(next.size()));
				if (!connect(c,n)) throw new IllegalStateException("max generation broken");
				visited[n.row][n.column]= true;
				--remaining;
				back.push(n);
			}
		}
	}
	
	// #)
}
