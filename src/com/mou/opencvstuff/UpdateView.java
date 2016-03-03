/**
 * 
 */
package com.mou.opencvstuff;

import org.opencv.core.Point;


/**
 * @author alies_a
 * modification de l'ui lors d'une detection
 */

public class UpdateView implements Runnable{
	private MainActivity context;
	private Point old;
	private Point curr;
	private double rad;
	public int CamHeight;
	public int CamWidth;

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
		/*
		Animation rotate = AnimationUtils.loadAnimation(context, R.animator.rotate);
		context.mView.setPivotX((int)curr.x);
		context.mView.setPivotY((int)curr.y);
		context.mView.setAnimation(rotate);
		*/
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
	}

}
