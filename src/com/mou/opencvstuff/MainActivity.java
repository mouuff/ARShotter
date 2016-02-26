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
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

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
		
		Updater = new UpdateView(this);
		mView = (View) findViewById(R.id.lock_on);
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camview);
		mOpenCvCameraView.setCvCameraViewListener(this);
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
    	
    	mRgba = inputFrame.rgba();
    	mGray = inputFrame.gray();
    	
    	Mat circles = new Mat();
    	Imgproc.GaussianBlur(mGray, mGray, new Size(11,11), 2, 2);
    	//floutage pour une meilleure detection
    	Imgproc.HoughCircles(mGray, circles, Imgproc.CV_HOUGH_GRADIENT, 2.0, mGray.rows() / 4, 200, 100, 0, 0);
    	for (int i = 0; i < circles.cols(); i++)
    	{
    		double mCircle[] = circles.get(0, i);
    		Point a = new Point(mCircle[0], mCircle[1]);
    		Updater.updateData(mCircle[0], mCircle[1], mCircle[2]);
    		Core.circle(mRgba, a, (int)mCircle[2], new Scalar(0, 255, 0));
    	}
    	runOnUiThread(Updater);
    	//move View from last sequence
    	return (mRgba);
	}
}
