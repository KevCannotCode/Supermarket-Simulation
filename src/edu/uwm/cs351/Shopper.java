package edu.uwm.cs351;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;
import java.util.Random;

public class Shopper {

	private static int averageItems = 10;
	private static int deviation = 10;
	
	//??
	public static boolean setParameter(String name, int value) {
		boolean result = false;
		if (name.equals("Shopper.averageItems")) {
			averageItems = value;
			result = true;
		}
		else if (name.equals("Shopper.deviation")) {
			deviation = value;
			result = true;
		}
		return result;
	}
	
	private final int numItems;
	private final long creationTime = Clock.elapsedTime();
	
	public Shopper(Random r) {
		double t = r.nextGaussian()*deviation + averageItems;
		if (t < 0) t = -t;
		numItems = 1+(int)t;
	}
	
	public int getNumItems() { return numItems; }
	public long getCreationTime() { return creationTime; }
	protected Color getColor() { return Color.GRAY; }
	
	/**
	 * Adds this shopper to a cash register
	 * @param 
	 * 		registers - a collection of CashRegister objects
	 * @precondition 
	 * 		register must not be null
	 * @postcondition
	 * 		this shopper was added to a cashRegister with the smallest "maximum number of item" possible in the register collection
	 * @throws
	 * 		NullPointerException - if register was null
	 */
	public void checkout(Collection<CashRegister> registers) {
		CashRegister choice = null;
		for (CashRegister cr : registers) {
			if (choice == null || better(cr,choice)) {
				choice = cr;
			}
		}
		choice.enqueue(this);
	}
	
	/**
	 * compares two cash registers and selects the register with the closest "maximum number of items" to this shopper's number of items
	 * @param 
	 * 		previousCashRegister - the cash register the comes before in the register collection
	 * 		currentCashRegister - the current cash register in the register collection
	 * @precondition 
	 * 		registers must not be null
	 * @return
	 * 		a boolean that indicates if the currentCashRegister is better than the previous one
	 * @throws
	 * 		NullPointerException - if register was null
	 */
	protected boolean better(CashRegister previousCashRegister, CashRegister currentCashRegister) {
		if (currentCashRegister.getMaximum() < numItems) return true;
		if (previousCashRegister.getMaximum() < numItems) return false;
		int count1 = evaluate(previousCashRegister);
		int count2 = evaluate(currentCashRegister);
		return (count1 < count2);
	}
	
	/**
	 * returns the length of the register's queue
	 * @param 
	 * 		r - a cash register 
	 * @precondition 
	 * 		r must not be null
	 * @return
	 * 		an int that indicates if the length of r's queue
	 * @throws
	 * 		NullPointerException - if r was null
	 */
	protected int evaluate(CashRegister r) {
		return r.getLength();
	}

	public static final int ITEM_WIDTH = 1;
	/**
	 * draws a rectangle on the graphical interface representing this shopper and their items
	 * @param 
	 * 		g - the graphical interface that contains the rectangle that will represent the shopper
	 * 		x - the x coordinate on the GUI ?
	 * 		y - the y coordinate on the GUI ?
	 * @precondition 
	 * 		g must not be null
	 * @return
	 * 		an int that indicates if the width of the rectangle that represents the shopper
	 * @throws
	 * 		NullPointerException - if g was null
	 */
	public int draw(Graphics g, int x, int y) {
		g.setColor(getColor());
		int width = numItems * ITEM_WIDTH;
		int height = CashRegister.YSIZE;
		g.fillRect(x, y+(CashRegister.YSIZE-height), width, height);
		return width;
	}
}
