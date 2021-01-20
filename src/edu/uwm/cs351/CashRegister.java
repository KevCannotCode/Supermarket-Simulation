/*Kevin Umba CS351 Homework 7
 * Tyler Trinh helped me understand and complete some aspects of run()
 */
package edu.uwm.cs351;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.DelayQueue;

import edu.uwm.cs351.util.Utilities;
import edu.uwm.cs351.util.Utilities.Computation;
import edu.uwm.cs351.util.Queue;

/**
 * A simulation of a cash register.
 * It has a queue and a thread for operating on the queue.
 * The queue itself is "owned" by the Swing thread:
 * it should only be accessed inside the UI thread.
 * If code needs to access it (to run a method or to copy it),
 * the code should do that inside of a {@link edu.uwm.cs351.util.Computation}
 * or {@link Runnable} passed to {@link Utilities#computeUI(Computation)
 * or {@link Utilities#invokeUI(Runnable)} respectively.
 * @author boyland
 *
 */
public class CashRegister implements Runnable {
	private final int maximumItems;	
	Queue<Shopper> queue = new Queue<Shopper>();

	public CashRegister(int max) { maximumItems = max; }

	public void enqueue(final Shopper shopper) {
		// TODO
		// Add a new shopper to the queue. Remember that th   e
		// queue can only be accessed inside the UI thread.
		Utilities.invokeUI( () -> queue.enqueue(shopper));
	}

	public int getMaximum() { return maximumItems; }

	public int getLength() {
		// TODO
		// Return the number of shoppers in the queue.  Remember that the
		// queue can only be accessed inside the UI thread.
		return Utilities.computeUI(() -> queue.size());
	}

	public int getTotalItems() {
		// TODO
		// Make a copy of the queue and then count up the total number of
		// items of all the shoppers in the queue.  Remember that the
		// queue can only be accessed inside the UI thread.
		
		int totalItems =  Utilities.computeUI( () ->{
			Queue<Shopper> queueCopy = new Queue<Shopper>();
			queueCopy = queue.clone();
			int count= 0;
			while(!queueCopy.isEmpty()) {
				count = queueCopy.dequeue().getNumItems();
			}
			return count;
		});
		return totalItems;
		
	}

	private static int secondsPerItem = 10;
	private static int secondsPerRest = 10;

	public static boolean setParameter(String name, int value) {
		if ("CashRegister.secondsPerItem".equals(name)) {
			secondsPerItem = value;
			return true;
		} else if ("CashRegister.secondsPerRest".equals(name)) {
			secondsPerRest = value;
			return true;
		}
		return false;
	}

	private int workingTime;
	private int restingTime;

	public void run() {
		while (!Thread.interrupted()) {
			// TODO
			// Figure out if there is a shopper in this queue.
			// If not, rest for {@link #secondsPerRest} seconds,
			// using {@link Clock#pause} and adding to the total
			// {@link #restingTime}.
			// If there is a shopper, figure out how long it
			// takes to empty their baskets (using the number of items they
			// have and the parameter {@link #secondsPerItem}), 
			// pause for that time,
			// and add that time to the total {@link #workingTime}.
			// Finally remove them from the queue, and 
			// record that the shopper is done, using
			// {@link Simulation#recordComplete}.
			// NB: The queue can only be accessed inside of 
			// computations/runnables passed to CS351Utilities static functions.
			
			boolean noShoppers = Utilities.computeUI( () -> queue.isEmpty());
			if(!noShoppers) {
				int timeToEmptyBasket = Utilities.computeUI(() -> queue.front().getNumItems())*secondsPerItem;
				Clock.pause(timeToEmptyBasket);
				workingTime += timeToEmptyBasket;
				Shopper x = Utilities.computeUI(() -> queue.dequeue());
				Simulation.recordComplete(x);
			}
			else {
				restingTime+= secondsPerItem;
				Clock.pause(secondsPerRest);
			}
		}
	}

	public String getProductivity() {
		if (workingTime == 0) return "0%";
		return (int)(100.0*workingTime/((double)workingTime+restingTime)) + "%";
	}

	public static final int XSIZE = 50;
	public static final int YSIZE = 20;
	private static final int SEP = 10;

	public void draw(Graphics g, int x, int y) {
		g.setColor(maximumItems < 100 ? Color.RED : Color.BLACK);
		g.drawRect(x, y, XSIZE,YSIZE);
		g.drawString(getProductivity(), x+1, y+YSIZE-1);
		Queue<Shopper> q = queue.clone(); // doesn't need to be computeUI because in Event thread already
		x += SEP + XSIZE;
		while (!q.isEmpty()) {
			x += SEP + q.front().draw(g, x, y);
			q.dequeue();
		}
	}
}
