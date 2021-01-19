import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Maze;



public class TestMaze extends LockedTestCase {
    protected static void assertException(Class<? extends Throwable> c, Runnable r) {
    	try {
    		r.run();
    		assertFalse("Exception should have been thrown",true);
        } catch (RuntimeException ex) {
        	assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
        }	
    }	

    private StringWriter output;
	private PrintWriter outputCollector;

	public String[] getOutput() {
		outputCollector.close();
		List<String> results = new ArrayList<>();
		BufferedReader br = new BufferedReader(new StringReader(output.toString()));
		String s;
		try {
			while ((s = br.readLine()) != null) {
				results.add(s);
			}
		} catch (IOException e) {
			assertFalse("Internal Error",true);
		}
		return results.toArray(new String[results.size()]);
	}
	
	protected static String toString(Object... as) {
		StringBuilder sb = new StringBuilder();
		for (Object a : as) {
			sb.append(a);
			sb.append(System.lineSeparator());
		}
		String string = sb.toString();
		return string;
	}

	protected static BufferedReader toBR(Object... as) {
		String string = toString(as);
		return new BufferedReader(new StringReader(string));
	}
	
	protected static void assertContents(Collection<?> col, Object... as) {
		assertEquals(Arrays.toString(as) + ".size != " + col + ".size()",as.length,col.size());
		Iterator<?> it = col.iterator();
		for (Object a : as) {
			assertEquals(a,it.next());
		}
	}


	private Maze maze;
	
	@Override
	protected void setUp() {
		maze = new Maze(3,5);
		output = new StringWriter();
		outputCollector = new PrintWriter(output);
	}

	
	/// Locked Tests
	
	private String getMazeOpen(boolean down, int i) {
		StringBuilder sb = new StringBuilder();
		int max = down ? maze.columns() : maze.columns() - 1;
		for (int k = 0; k < max; ++k) {
			boolean b = down ? maze.isOpenDown(i, k) : maze.isOpenRight(i, k);
			sb.append(b ? '1' : '0');
		}
		return sb.toString();
	}
	
	public void test() throws IOException {
		// Look at the assignment handout where it gives ropen and copen
		// as matrices of zeros and ones.  We will write them without spaces, e.g.
		// ropen  1010      copen  101
		//        1111             100
		//                         011
		// Remember that 1 = open, 0 = closed.
		// But here we use a different example, which is wider by one column
		// (and so ropen and copen will be longer too)
		maze.read(toBR(
				"+-+-+-+-+ +",
				"| |       |",
				"+ + +-+-+ +",
				"|   |     |",
				"+-+-+ +-+ +",
				"      |   |",
				"+-+-+-+-+-+"));
		assertEquals(Ts(1424575435),getMazeOpen(true,0)); // what is ropen's first row?
		assertEquals(Ts(620812796),getMazeOpen(true,1)); // what is ropen's second row?
		assertEquals(Ts(1247968559),getMazeOpen(false,0)); // what is copen's first row?
		assertEquals(Ts(652299538),getMazeOpen(false,1)); // what is copen's second row?
		assertEquals(Ts(356826078),getMazeOpen(false,2)); // what is copen's third row?
	}
	
	/// test0x: left
	
	public void test00() {
		assertEquals(3,maze.rows());
		assertEquals(5,maze.columns());
	}
	
	public void test01() {
		assertFalse(maze.isOpenLeft(0, 0));
		assertFalse(maze.isOpenLeft(1, 0));
	}
	
	public void test02() {
		assertTrue(maze.isOpenLeft(2,0));
	}
	
	public void test03() {
		assertFalse(maze.isOpenLeft(0, 1));
		assertFalse(maze.isOpenLeft(1, 1));
		assertFalse(maze.isOpenLeft(2, 1));
	}
	
	public void test04() {
		assertFalse(maze.isOpenLeft(0, 4));
		assertFalse(maze.isOpenLeft(1, 4));
		assertFalse(maze.isOpenLeft(2, 4));
	}
	
	public void test05() {
		maze.setOpenRight(1, 3, true);
		maze.setOpenDown(0, 2, true);
		assertTrue(maze.isOpenLeft(1, 4));
	}
	
	public void test06() {
		maze.setOpenRight(1, 3, true);
		maze.setOpenDown(0, 2, true);
		assertFalse(maze.isOpenLeft(1, 3));
	}
	
	public void test07() {
		maze.setOpenRight(1, 3, true);
		maze.setOpenDown(0, 2, true);
		assertFalse(maze.isOpenLeft(0, 2));
		assertFalse(maze.isOpenLeft(0, 3));
		assertFalse(maze.isOpenLeft(1, 2));
	}
	
	public void test08() {
		assertException(IllegalArgumentException.class, () -> maze.isOpenLeft(3,0));
		assertException(IllegalArgumentException.class, () -> maze.isOpenLeft(0,5));
	}
	
	public void test09() {
		assertException(IllegalArgumentException.class, () -> maze.isOpenLeft(-1,1));
		assertException(IllegalArgumentException.class, () -> maze.isOpenLeft(1,-1));
	}
	
	
	/// test1x: right tests
	
	public void test11() {
		assertFalse(maze.isOpenRight(0, 0));
		assertFalse(maze.isOpenRight(1, 0));
		assertFalse(maze.isOpenRight(2, 0));
	}
	
	public void test12() {
		assertFalse(maze.isOpenRight(0, 1));
	}
	
	public void test13() {
		assertFalse(maze.isOpenRight(1, 4));
		assertFalse(maze.isOpenRight(2, 4));
	}
	
	public void test15() {
		maze.setOpenRight(1, 3, true);
		maze.setOpenDown(0, 2, true);
		assertFalse(maze.isOpenRight(1, 4));
	}
	
	public void test16() {
		maze.setOpenRight(1, 3, true);
		maze.setOpenDown(0, 2, true);
		assertTrue(maze.isOpenRight(1, 3));
	}
	
	public void test17() {
		maze.setOpenRight(1, 3, true);
		maze.setOpenDown(0, 2, true);
		assertFalse(maze.isOpenRight(0, 2));
		assertFalse(maze.isOpenRight(0, 3));
		assertFalse(maze.isOpenRight(1, 2));
	}
	
	public void test18() {
		assertException(IllegalArgumentException.class, () -> maze.isOpenRight(3,0));
		assertException(IllegalArgumentException.class, () -> maze.isOpenRight(0,5));
	}

	public void test19() {
		assertException(IllegalArgumentException.class, () -> maze.isOpenRight(-1,1));
		assertException(IllegalArgumentException.class, () -> maze.isOpenRight(1,-1));
	}

	
	/// test2x: up
	
	public void test20() {
		assertFalse(maze.isOpenUp(0, 0));
	}
	
	public void test21() {
		assertFalse(maze.isOpenUp(0, 1));
		assertFalse(maze.isOpenUp(0, 2));
		assertFalse(maze.isOpenUp(0, 3));
	}
	
	public void test22() {
		assertTrue(maze.isOpenUp(0, 4));
	}
	
	public void test23() {
		assertFalse(maze.isOpenUp(1, 0));
		assertFalse(maze.isOpenUp(1, 1));
		assertFalse(maze.isOpenUp(1, 2));
		assertFalse(maze.isOpenUp(1, 3));
		assertFalse(maze.isOpenUp(1, 4));
	}
	
	public void test24() {
		assertFalse(maze.isOpenUp(2, 0));
		assertFalse(maze.isOpenUp(2, 1));
		assertFalse(maze.isOpenUp(2, 2));
		assertFalse(maze.isOpenUp(2, 3));
		assertFalse(maze.isOpenUp(2, 4));
	}
	
	public void test25() {
		maze.setOpenRight(1, 3, true);
		maze.setOpenDown(0, 2, true);
		assertTrue(maze.isOpenUp(1, 2));
	}
	
	public void test26() {
		maze.setOpenRight(1, 3, true);
		maze.setOpenDown(0, 2, true);
		assertFalse(maze.isOpenUp(0, 2));
	}
	
	public void test27() {
		maze.setOpenRight(1, 3, true);
		maze.setOpenDown(0, 2, true);
		assertFalse(maze.isOpenUp(1, 3));
		assertFalse(maze.isOpenUp(0, 3));
		assertFalse(maze.isOpenUp(2, 3));
	}
	
	public void test28() {
		assertException(IllegalArgumentException.class, () -> maze.isOpenUp(3,0));
		assertException(IllegalArgumentException.class, () -> maze.isOpenUp(0,5));
	}
	
	public void test29() {
		assertException(IllegalArgumentException.class, () -> maze.isOpenUp(-1,1));
		assertException(IllegalArgumentException.class, () -> maze.isOpenUp(1,-1));
	}
	
	
	/// test3x: down tests
	
	public void test31() {
		assertFalse(maze.isOpenDown(0, 0));
		assertFalse(maze.isOpenDown(0, 1));
		assertFalse(maze.isOpenDown(0, 2));
		assertFalse(maze.isOpenDown(0, 3));
		assertFalse(maze.isOpenDown(0, 4));
	}
	
	public void test32() {
		assertFalse(maze.isOpenDown(0, 0));
		assertFalse(maze.isOpenDown(0, 1));
		assertFalse(maze.isOpenDown(0, 2));
		assertFalse(maze.isOpenDown(0, 3));
		assertFalse(maze.isOpenDown(0, 4));
	}
	
	public void test33() {
		assertFalse(maze.isOpenDown(0, 0));
		assertFalse(maze.isOpenDown(0, 1));
		assertFalse(maze.isOpenDown(0, 2));
		assertFalse(maze.isOpenDown(0, 3));
		assertFalse(maze.isOpenDown(0, 4));
	}
	
	public void test35() {
		maze.setOpenRight(1, 3, true);
		maze.setOpenDown(0, 2, true);
		assertFalse(maze.isOpenDown(1, 2));
	}
	
	public void test36() {
		maze.setOpenRight(1, 3, true);
		maze.setOpenDown(0, 2, true);
		assertTrue(maze.isOpenDown(0, 2));
	}
	
	public void test37() {
		maze.setOpenRight(1, 3, true);
		maze.setOpenDown(0, 2, true);
		assertFalse(maze.isOpenDown(0, 3));
		assertFalse(maze.isOpenDown(1, 3));
		assertFalse(maze.isOpenDown(2, 3));
	}
	
	public void test38() {
		assertException(IllegalArgumentException.class, () -> maze.isOpenDown(3,0));
		assertException(IllegalArgumentException.class, () -> maze.isOpenDown(0,5));
	}

	public void test39() {
		assertException(IllegalArgumentException.class, () -> maze.isOpenDown(-1,1));
		assertException(IllegalArgumentException.class, () -> maze.isOpenDown(1,-1));
	}
	
	
	/// test5x: small output tests
	
	public void test50() {
		maze = new Maze(1,1); // minimum size
		maze.write(outputCollector);
		String[] ss = getOutput();
		assertEquals(3,ss.length);
		assertEquals("+ +",ss[0]);
		assertEquals("  |",ss[1]);
		assertEquals("+-+",ss[2]);
	}
	
	public void test51() {
		maze = new Maze(1,2);
		maze.write(outputCollector);
		String[] ss = getOutput();
		assertEquals(3,ss.length);
		assertEquals("+-+ +",ss[0]);
		assertEquals("  | |",ss[1]);
		assertEquals("+-+-+",ss[2]);
	}
	
	public void test52() {
		maze = new Maze(1,2);
		maze.setOpenRight(0, 0, true);
		maze.write(outputCollector);
		String[] ss = getOutput();
		assertEquals(3,ss.length);
		assertEquals("+-+ +",ss[0]);
		assertEquals("    |",ss[1]);
		assertEquals("+-+-+",ss[2]);
	}

	public void test53() {
		maze = new Maze(2,1);
		maze.write(outputCollector);
		String[] ss = getOutput();
		assertEquals(5,ss.length);
		assertEquals("+ +",ss[0]);
		assertEquals("| |",ss[1]);
		assertEquals("+-+",ss[2]);
		assertEquals("  |",ss[3]);
		assertEquals("+-+",ss[4]);
	}
	
	public void test54() {
		maze = new Maze(2,1);
		maze.setOpenDown(0, 0, true);
		maze.write(outputCollector);
		String[] ss = getOutput();
		assertEquals(5,ss.length);
		assertEquals("+ +",ss[0]);
		assertEquals("| |",ss[1]);
		assertEquals("+ +",ss[2]);
		assertEquals("  |",ss[3]);
		assertEquals("+-+",ss[4]);
	}
	
	public void test55() {
		maze = new Maze(2,2);
		maze.write(outputCollector);
		String[] ss = getOutput();
		assertEquals(5,ss.length);
		assertEquals("+-+ +",ss[0]);
		assertEquals("| | |",ss[1]);
		assertEquals("+-+-+",ss[2]);
		assertEquals("  | |",ss[3]);
		assertEquals("+-+-+",ss[4]);
	}
	
	public void test56() {
		maze = new Maze(2,2);
		maze.setOpenDown(0, 0, true);
		maze.setOpenRight(0, 0, true);
		maze.write(outputCollector);
		String[] ss = getOutput();
		assertEquals(5,ss.length);
		assertEquals("+-+ +",ss[0]);
		assertEquals("|   |",ss[1]);
		assertEquals("+ +-+",ss[2]);
		assertEquals("  | |",ss[3]);
		assertEquals("+-+-+",ss[4]);
	}
	
	public void test57() {
		maze = new Maze(2,2);
		maze.setOpenDown(0, 1, true);
		maze.setOpenRight(0, 0, true);
		maze.write(outputCollector);
		String[] ss = getOutput();
		assertEquals(5,ss.length);
		assertEquals("+-+ +",ss[0]);
		assertEquals("|   |",ss[1]);
		assertEquals("+-+ +",ss[2]);
		assertEquals("  | |",ss[3]);
		assertEquals("+-+-+",ss[4]);
	}
	
	public void test58() {
		maze = new Maze(2,2);
		maze.setOpenDown(0, 1, true);
		maze.setOpenRight(1, 0, true);
		maze.write(outputCollector);
		String[] ss = getOutput();
		assertEquals(5,ss.length);
		assertEquals("+-+ +",ss[0]);
		assertEquals("| | |",ss[1]);
		assertEquals("+-+ +",ss[2]);
		assertEquals("    |",ss[3]);
		assertEquals("+-+-+",ss[4]);
	}
	
	public void test59() {
		maze = new Maze(2,2);
		maze.setOpenDown(0, 0, true);
		maze.setOpenRight(1, 0, true);
		maze.write(outputCollector);
		String[] ss = getOutput();
		assertEquals(5,ss.length);
		assertEquals("+-+ +",ss[0]);
		assertEquals("| | |",ss[1]);
		assertEquals("+ +-+",ss[2]);
		assertEquals("    |",ss[3]);
		assertEquals("+-+-+",ss[4]);
	}
	
	
	/// test6x: larger output tests
	
	public void test62() {
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
		maze.write(outputCollector);
		String[] ss = getOutput();
		assertEquals(7,ss.length);
		assertEquals("+-+-+-+-+ +",ss[0]);
		assertEquals("|       | |",ss[1]);
		assertEquals("+-+ +-+ + +",ss[2]);
		assertEquals("|   | |   |",ss[3]);
		assertEquals("+ +-+ +-+ +",ss[4]);
		assertEquals("  |       |",ss[5]);
		assertEquals("+-+-+-+-+-+",ss[6]);
	}
	
	
	/// test7x: small input tests
	
	public void test70() throws IOException {
		maze = new Maze(1,1);
		maze.read(toBR("+ +","  |","+-+"));
	}
	
	public void test71() throws IOException {
		maze = new Maze(1,2);
		maze.read(toBR(
				"+-+ +",
				"  | |",
				"+-+-+"
				));
		assertFalse(maze.isOpenRight(0, 0));
	}
	
	public void test72() throws IOException {
		maze = new Maze(1,2);
		maze.read(toBR(
				"+-+ +",
				"    |",
				"+-+-+"
				));
		assertTrue(maze.isOpenRight(0, 0));
	}

	public void test73() throws IOException {
		maze = new Maze(2,1);
		maze.read(toBR(
			"+ +",
			"| |",
			"+-+",
			"  |",
			"+-+"));
		assertFalse(maze.isOpenDown(0, 0));
	}
	
	public void test74() throws IOException {
		maze = new Maze(2,1);
		maze.read(toBR(
			"+ +",
			"| |",
			"+ +",
			"  |",
			"+-+"));
		assertTrue(maze.isOpenDown(0, 0));
	}
	
	public void test75() throws IOException {
		maze = new Maze(2,2);
		maze.read(toBR(
			"+-+ +",
			"| | |",
			"+-+-+",
			"  | |",
			"+-+-+"));
		assertFalse(maze.isOpenRight(0, 0));
		assertFalse(maze.isOpenRight(1, 0));
		assertFalse(maze.isOpenDown(0, 0));
		assertFalse(maze.isOpenDown(0, 1));
	}
	
	public void test76() throws IOException {
		maze = new Maze(2,2);
		maze.read(toBR(
			"+-+ +",
			"|   |",
			"+ +-+",
			"  | |",
			"+-+-+"));
		assertTrue(maze.isOpenDown(0, 0));
		assertTrue(maze.isOpenRight(0, 0));
		assertFalse(maze.isOpenRight(1, 0));
		assertFalse(maze.isOpenDown(0, 1));
	}
	
	public void test77() throws IOException {
		maze = new Maze(2,2);
		maze.read(toBR(
			"+-+ +",
			"|   |",
			"+-+ +",
			"  | |",
			"+-+-+"));
		assertTrue(maze.isOpenDown(0, 1));
		assertTrue(maze.isOpenRight(0, 0));
		assertFalse(maze.isOpenRight(1, 0));
		assertFalse(maze.isOpenDown(0, 0));
	}
	
	public void test78() throws IOException {
		maze = new Maze(2,2);
		maze.read(toBR(
			"+-+ +",
			"| | |",
			"+-+ +",
			"    |",
			"+-+-+"));
		assertTrue(maze.isOpenDown(0, 1));
		assertTrue(maze.isOpenRight(1, 0));
		assertFalse(maze.isOpenRight(0, 0));
		assertFalse(maze.isOpenDown(0, 0));
	}
	
	public void test79() throws IOException {
		maze = new Maze(2,2);
		maze.read(toBR(
			"+-+ +",
			"| | |",
			"+ +-+",
			"    |",
			"+-+-+"));
		assertTrue(maze.isOpenDown(0, 0));
		assertTrue(maze.isOpenRight(1, 0));
		assertFalse(maze.isOpenRight(0, 0));
		assertFalse(maze.isOpenDown(0, 1));
	}
	
	
	/// test8x: larger read test
	
	public void test82() throws IOException {
		maze.read(toBR(
			"+-+-+-+-+ +",
			"|       | |",
			"+-+ +-+ + +",
			"|   | |   |",
			"+ +-+ +-+ +",
			"  |       |",
			"+-+-+-+-+-+"));
		assertTrue(maze.isOpenRight(0, 0));
		assertTrue(maze.isOpenRight(0, 1));
		assertTrue(maze.isOpenRight(0, 2));
		assertFalse(maze.isOpenRight(0, 3));
		assertTrue(maze.isOpenRight(1, 0));
		assertFalse(maze.isOpenRight(1, 1));
		assertFalse(maze.isOpenRight(1, 2));
		assertTrue(maze.isOpenRight(1, 3));
		assertFalse(maze.isOpenRight(2, 0));
		assertTrue(maze.isOpenRight(2, 1));
		assertTrue(maze.isOpenRight(2, 2));
		assertTrue(maze.isOpenRight(2, 3));
		assertFalse(maze.isOpenDown(0, 0));
		assertTrue(maze.isOpenDown(0, 1));
		assertFalse(maze.isOpenDown(0, 2));
		assertTrue(maze.isOpenDown(0, 3));
		assertTrue(maze.isOpenDown(0, 4));
		assertTrue(maze.isOpenDown(1, 0));
		assertFalse(maze.isOpenDown(1, 1));
		assertTrue(maze.isOpenDown(1, 2));
		assertFalse(maze.isOpenDown(1, 3));
		assertTrue(maze.isOpenDown(1, 4));
	}
}
