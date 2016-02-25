package com.mou.opencvstuff;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements CvCameraViewListener2 {
	private CameraBridgeViewBase mOpenCvCameraView;
	private String TAG = "opencvstuff";
	
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
		//Intent intent = new Intent("org.opencv.engine.BIND");
		//intent.setPackage("org.opencv.engine");
		
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camview);
		mOpenCvCameraView.setCvCameraViewListener(this);
	}
	
	private void setViewPos(int x, int y)
	{
		TextView view = (TextView) findViewById(R.id.text);
		view.setX(x);
		view.setY(y);
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
	}
	@Override
	public void onCameraViewStopped() {
	}
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Mat mRgba;
    	Mat mGray;
    	//Mat mCanny = new Mat();
    	//double thresh = 70;
    	
    	mRgba = inputFrame.rgba();
    	mGray = inputFrame.gray();
    	//Imgproc.Canny(mGray, mCanny, thresh * 0.4, thresh);
    	
    	Mat circles = new Mat();
    	Imgproc.GaussianBlur(mGray, mGray, new Size(9,9), 2, 2);
    	Imgproc.HoughCircles(mGray, circles, Imgproc.CV_HOUGH_GRADIENT, 2.0, mGray.rows() / 4);
    	for (int i = 0; i < circles.cols(); i++)
    	{
    		double mCircle[] = circles.get(0, i);
    		Point a = new Point(mCircle[0], mCircle[1]);
    		Core.circle(mRgba, a, (int)mCircle[2], new Scalar(0, 255, 0));
    		final int x = (int) mCircle[0];
    		final int y = (int) mCircle[1];
    		
    		runOnUiThread(new Runnable() {
    		     @Override
    		     public void run() {
    		    	 setViewPos(x, y);

    		    }
    		});
    		
    	}
    	/*
    	Mat lines = new Mat();
    	Imgproc.HoughLinesP(mCanny, lines, 1, Math.PI/180, 30, 70, 10);
    	for (int i = 0; i < lines.cols(); i++)
    	{
    		double mvec[] = lines.get(0, i);
    		Point a = new Point(mvec[0], mvec[1]);
        	Point b = new Point(mvec[2], mvec[3]);
        	Core.line(mRgba, a, b, new Scalar(0, 255, 0, 255), 3);
    	}
    	*/
    	return (mRgba);
	}
}
