/**
 * 
 */
package com.mou.opencvstuff;

import org.opencv.core.Point;
import org.opencv.core.Scalar;


/**
 * @author alies_a
 * modification de l'ui lors d'une detection
 */

public class UpdateView implements Runnable{
	private MainActivity context;
	private Point old;
	public Point curr;
	public double rad;
	public int timeout;
	public Scalar color;
	public int CamHeight;
	public int CamWidth;

	public boolean isRed()
	{
		double[] vals;
		
		if (this.color == null)
			return (false);
		vals = this.color.val;
		
		if (vals[0] > vals[2])
			return (true);
		return (false);
	}
	public UpdateView(MainActivity _context)
	{
		context = _context;
		old = new Point();
		if (context.mView != null)
			curr = new Point(context.mView.getWidth() / 2, context.mView.getHeight() / 2);
		else
			curr = new Point();
		rad = 0;
		CamWidth = 0;
		CamHeight = 0;
		timeout = 0;
	}
	public void setCameraSize(int width, int height)
	{
		CamWidth = width;
		CamHeight = height;
	}
	public void updateData(double[] circle)
	{
		curr = new Point(circle[0], circle[1]);
		rad = circle[2];
		timeout = 50;
	}
	private Point getMiddle(Point from)
	{
		Point res = new Point();
		res.x = from.x / (double)CamWidth * (double)context.mOpenCvCameraView.getWidth();
		res.y = from.y / (double)CamHeight * (double)context.mOpenCvCameraView.getHeight();
		return (res);
	}
	@Override
	public void run() {
		Point new_pos = new Point();
		Point middle;
		
		if (CamWidth == 0 || CamHeight == 0)
			return ;
		if (old.x == 0 || old.y == 0)
		{
			new_pos.x = curr.x;
			new_pos.y = curr.y;
		}
		else
		{
			new_pos.x = old.x - (old.x - curr.x) / 10;
			new_pos.y = old.y - (old.y - curr.y) / 10;
		}
		middle = getMiddle(new_pos);
		context.mView.setX((float) (middle.x - context.mView.getWidth() / 2));
		context.mView.setY((float) (middle.y - context.mView.getHeight() / 2));
		context.mView.bringToFront();
		old = new_pos;
		timeout -= 1;
		if (timeout <= 0)
		{
			curr = new Point(CamWidth / 2, CamHeight / 2);
			timeout = 0;
		}
	}

}
