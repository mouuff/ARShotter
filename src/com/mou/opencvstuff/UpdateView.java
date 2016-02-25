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
	private double oldx;
	private double oldy;
	private double x;
	private double y;
	private double rad;

	public UpdateView(MainActivity _context)
	{
		context = _context;
		oldx = 0;
		oldy = 0;
		x = 0;
		y = 0;
		rad = 0;
	}
	public void updateData(double _x, double _y, double _rad)
	{
		x = _x;
		y = _y;
		rad = _rad;
	}
	@Override
	public void run() {
		double newx;
		double newy;
		
		if (oldx == 0 || oldy == 0)
		{
			newx = x;
			newy = y;
		}
		else
		{
			newx = oldx - (oldx - x) / 10;
			newy = oldy - (oldy - y) / 10;
		}
		context.mView.setX((int) newx);
		context.mView.setY((int) newy);
		oldx = newx;
		oldy = newy;
	}

}
