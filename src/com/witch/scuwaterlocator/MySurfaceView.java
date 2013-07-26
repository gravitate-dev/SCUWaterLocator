package com.witch.scuwaterlocator;

import com.example.scuwaterlocator.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

	    private int tmp;
	    private Bitmap arrow;
	    float orientation_degree = 0;
	    float drawing_degree = 0;
	    float bearing_degree = 0;
	    double distance_to_water = 0;
	    int pixel_size;

	    public MySurfaceView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        getHolder().addCallback(this);
	        setFocusable(true);
	        arrow = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
	        int MY_DIP_VALUE = 5; //5dp

	        pixel_size= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
	                                      MY_DIP_VALUE, getResources().getDisplayMetrics());
	    }
	    
	    @Override
	    public void onDraw(Canvas c) {
	    	
	    	int width = arrow.getWidth();
	        int height = arrow.getHeight();
	        
	        //if device is taller than wider then the limiting dimention is width
	        int limiting_dimension;
	        if (c.getHeight() > c.getWidth())
	        	limiting_dimension = (int)((float)c.getWidth()*0.6f);
	        else 
	        	limiting_dimension = (int)((float)c.getHeight()*0.6f);
	        
	        int newWidth = limiting_dimension;
	        int newHeight = limiting_dimension;

	        // calculate the scale - in this case = 0.4f
	        float scaleWidth = ((float) newWidth) / width;
	        float scaleHeight = ((float) newHeight) / height;

	        // createa matrix for the manipulation
	        Matrix matrix = new Matrix();
	        // resize the bit map
	        matrix.postScale(scaleWidth, scaleHeight);
	        // rotate the Bitmap
	        matrix.postTranslate((float)(-newWidth/2.0f),(float)(-newHeight/2.0f));
	        drawing_degree = bearing_degree + orientation_degree;
	        matrix.postRotate(drawing_degree);
	        matrix.postTranslate((float)(c.getWidth()/2.0f), (float)(c.getHeight()/2.0f));
	        //i chose to push it to the right because it looked wierd in the center
	        matrix.postTranslate((float)(c.getWidth()/4.0f), 0);
	        Paint p = new Paint();
	        
	        c.drawBitmap(arrow, matrix , p);
	        p.setColor(Color.BLUE);
	        p.setTextSize(pixel_size*3);
	        double distance_to_water_rounded = (double)(Math.round(distance_to_water*100))/100;
	        c.drawText("Distance To Water", 60, 60, p);
	        p.setTextSize(pixel_size*6);
	        c.drawText(""+distance_to_water_rounded, 90, 200, p);
	        tmp++;

	        //System.out.println(tmp);
	    }

	    @Override
	    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	        // TODO Auto-generated method stub

	    }

	    @Override
	    public void surfaceCreated(SurfaceHolder holder) {
	    }

	    @Override
	    public void surfaceDestroyed(SurfaceHolder holder) {
	    }
}
