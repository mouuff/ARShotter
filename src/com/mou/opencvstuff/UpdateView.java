/**
 * 
 */
package com.mou.opencvstuff;

import android.util.Log;
import android.widget.RelativeLayout;

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
	public int CamHeight;
	public int CamWidth;

	public UpdateView(MainActivity _context)
	{
		context = _context;
		oldx = 0;
		oldy = 0;
		x = 0;
		y = 0;
		rad = 0;
		CamWidth = 0;
		CamHeight = 0;
	}
	public void setCameraSize(int width, int height)
	{
		CamWidth = width;
		CamHeight = height;
	}
	public void updateData(double _x, double _y, double _rad)
	{
		x = _x;
		y = _y;
		rad = _rad;
	}
	private int XtoAbsolute(double x)
	{
		int ViewRes;
		ViewRes = context.mOpenCvCameraView.getWidth();
		return ((int)(x / (double)CamWidth * (double)ViewRes));
	}
	private int YtoAbsolute(double y)
	{
		int ViewRes;
		ViewRes = context.mOpenCvCameraView.getHeight();
		return ((int)(y / (double)CamHeight * (double)ViewRes));
	}
	@Override
	public void run() {
		double newx;
		double newy;
		
		if (CamWidth == 0 || CamHeight == 0)
			return ;
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
		context.mView.setX(XtoAbsolute(newx) - context.mView.getWidth() / 2);
		context.mView.setY(YtoAbsolute(newy) - context.mView.getHeight() / 2);
		context.mView.bringToFront();
		oldx = newx;
		oldy = newy;
	}

}
