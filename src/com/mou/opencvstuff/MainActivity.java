package com.mou.opencvstuff;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity implements CvCameraViewListener2 {
	public View mView;
	public CameraBridgeViewBase mOpenCvCameraView;
	private String TAG = "opencvstuff";
	private UpdateView Updater;
	
	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
        public void onManagerConnected(int status) {
			switch (status) {
				case LoaderCallbackInterface.SUCCESS:
				{
					Log.i(TAG, "OpenCV loaded successfully");
					mOpenCvCameraView.enableView();
				} break;
				default:
				{
					super.onManagerConnected(status);
				} break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		mView = (View) findViewById(R.id.lock_on);
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camview);
		mOpenCvCameraView.setCvCameraViewListener(this);
		Updater = new UpdateView(this);
	}
	private Mat Zoom(Mat from, double factor, double xoff, double yoff)
	{
		Mat res = new Mat(from.rows(), from.cols(), from.type());
		Mat resized;
		resized = from.submat(
				(int)((from.rows() - from.rows() * factor) - xoff),
				(int)((from.rows() * factor) - xoff),
				(int)((from.cols() - from.cols() * factor) - yoff),
				(int)((from.cols() * factor) - yoff));
		//Imgproc.resize(from, resized, new Size(from.rows() * factor, from.cols() * factor), 0, 0, Imgproc.INTER_CUBIC);
		Imgproc.resize(resized, res, res.size(), 0, 0, Imgproc.INTER_NEAREST);
		return (res);
	}
	private double getDist(Point a, Point b)
	{
		return (Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2)));
	}
	private double[] getClosestCircle(Mat circles, Point mid)
	{
		double closest[] = new double[3];
		Point buff = new Point();
		
		for (int i = 0; i < circles.cols(); i++)
    	{
    		double mCircle[] = circles.get(0, i);
    		buff.x = mCircle[0];
    		buff.y = mCircle[1];
    		if (getDist(buff, mid) < getDist(new Point(closest[0], closest[1]), mid) || i == 0)
    		{
    			closest[0] = mCircle[0];
    			closest[1] = mCircle[1];
    			closest[2] = mCircle[2];
    		}
    	}
		return (closest);
	}
	@Override
	public void onPause()
	{
		super.onPause();
		if (mOpenCvCameraView != null){
			mOpenCvCameraView.disableView();
		}
	}
	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, getApplicationContext(), mLoaderCallback);
	}
	public void onDestroy() {
		super.onDestroy();
		mOpenCvCameraView.disableView();
	}
	@Override
	public void onCameraViewStarted(int width, int height) {
		Updater.setCameraSize(width, height);
	}
	@Override
	public void onCameraViewStopped() {
	}
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Mat mRgba;
    	Mat mGray;
    	Mat rRgba;
    	Mat rGray;
    	
    	mRgba = inputFrame.rgba();
    	mGray = inputFrame.gray();
    	rRgba = Zoom(mRgba, 0.655, 10, -25);
    	rGray = Zoom(mGray, 0.655, 10, -25);
    	
    	Mat circles = new Mat();
    	Imgproc.GaussianBlur(rGray, rGray, new Size(7, 7), 2, 2);
    	Imgproc.HoughCircles(rGray, circles, Imgproc.CV_HOUGH_GRADIENT, 1.0, rGray.rows() / 4, 100, 50, 0, 0);
    	
    	if (circles.cols() > 0)
    	{
    		double[] circle = getClosestCircle(circles, new Point(rGray.cols() / 2, rGray.rows() / 2));
    		Updater.updateData(circle);
    		Core.circle(rRgba, new Point(circle[0], circle[1]), (int)circle[2], new Scalar(0, 255, 0));
    	}
    	runOnUiThread(Updater);
    	
    	//double[] pt = rRgba.get(rRgba.rows() / 2, rRgba.cols() / 2);
    	//Core.rectangle(rRgba, new Point(0, 0), new Point(10, 10), new Scalar(pt[0], pt[1], pt[2], pt[3]));
    	//move View from last sequence
    	//System.gc();
    	return (rRgba);
	}
}
