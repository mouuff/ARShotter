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
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;
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
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
		 OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, getApplicationContext(), mLoaderCallback);
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
		// TODO Auto-generated method stub
		
	}
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		Mat mRgba;
    	Mat mGray;
    	Mat mCanny = new Mat();
    	Mat lines = new Mat();
    	double thresh = 60;
    	
    	mGray = inputFrame.gray();
    	mRgba = inputFrame.rgba();
    	Imgproc.Canny(mGray, mCanny, thresh * 0.4, thresh);
    	Imgproc.HoughLinesP(mCanny, lines, 1, Math.PI/180, 30, 70, 10);
    	for (int i = 0; i < lines.cols(); i++)
    	{
    		double mvec[] = lines.get(0, i);
    		Point a = new Point(mvec[0], mvec[1]);
        	Point b = new Point(mvec[2], mvec[3]);
        	Core.line(mRgba, a, b, new Scalar(0, 255, 0, 255), 3);
    	}
    	return (mRgba);
	}
}
