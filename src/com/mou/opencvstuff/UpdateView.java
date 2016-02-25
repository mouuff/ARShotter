/**
 * 
 */
package com.mou.opencvstuff;

/**
 * @author alies_a
 * modification du l'ui lors d'une detection
 */

public class UpdateView implements Runnable{
	private MainActivity context;
	private double x;
	private double y;
	private double rad;

	public UpdateView(MainActivity _context, double _x, double _y, double _rad)
	{
		context = _context;
		x = _x;
		y = _y;
		rad = _rad;
	}
	@Override
	public void run() {
		context.mView.setX((int) x);
		context.mView.setY((int) y);
	}

}
