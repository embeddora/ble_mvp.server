

package com.keeper.activity;

import java.util.ArrayList;

import com.keeper.R;
import com.keeper.activity.HomeActivity.SectionsPagerAdapter;
//import com.keeper.activity.HomeActivity.mServsReceiverX;
import com.keeper.model.Device;
import com.keeper.view.CirclePageIndicator;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(19)

public class RadarView extends View
{
	public static final String RSSI_CONNECTED = "com.keeper.radarview.rssi_connected";
	public static final String RSSI_DISCONNECTED = "com.keeper.radarview.rssi_disconnected";
	public static final String RSSI_DATA = "com.keeper.radarview.rssi_data";
	public static final String RSSI_AUX = "com.keeper.radarview.rssi_aux";

	private Paint mGridPaint, mGridPaint1, mErasePaint;

	private float mDistanceRatio;

	private Paint mSweepPaintBlack;

	private Paint mSweepPaintNrBlue1, mSweepPaintNrBlue2, mSweepPaintNrBlue3;
	//private Paint mSweepPaintNrRed1, mSweepPaintNrRed2, mSweepPaintNrRed3, mSweepPaintNrRed4;
	private Paint mSweepPaintPassiveRed1, mSweepPaintPassiveRed2, mSweepPaintPassiveRed3, mSweepPaintPassiveRed4;
	private Paint mSweepPaintActiveRed1, mSweepPaintActiveRed2, mSweepPaintActiveRed3, mSweepPaintActiveRed4;
	private Paint mSweepPaintYellow;
	private Paint mGridDarkGrey;

	public SoundPool soundPoolRadar;
	public int soundID_beep;

	private int iCurrentRSSI, iPreviousRSSI;

	private int mConnectionState;

	String LongString = "RSSI debug";

	mRssiReceiverX mOwnRssiReceiver;

	private String mAddr = "nix!(no addr)";
	private String mName = "nix!(no name)";

	private long mSweepTime;

	private boolean mSweepBefore;

	private long mBlipTime;

	public RadarView(Context context)
	{
		this(context, null);
	}


	public void onDestroy()
	{
		// TODO: don't seem to enter here, but why??
	}


	public RadarView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public RadarView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		// Paint used for the rings and ring text
		mGridPaint = new Paint();
		mGridPaint.setColor(0xFFBBBBBB);
		mGridPaint.setAntiAlias(true);
		mGridPaint.setStyle(Style.STROKE);
		mGridPaint.setStrokeWidth(1.0f);
		mGridPaint.setTextSize(20.0f);// this is for 0.5m 1m 3m 5m  // TODO: 10.0f - seems to be bnetter choice!
		mGridPaint.setTextAlign(Align.CENTER);

		mGridPaint1 = new Paint();
		mGridPaint1.setColor(0xFF777777);
		mGridPaint1.setAntiAlias(true);
		mGridPaint1.setStyle(Style.STROKE);
		mGridPaint1.setStrokeWidth(10.0f);
		mGridPaint1.setTextSize(10.0f);
		mGridPaint1.setTextAlign(Align.CENTER);

		mGridDarkGrey = new Paint();
		mGridDarkGrey.setColor(0xFF222222);
		mGridDarkGrey.setAntiAlias(true);
		mGridDarkGrey.setStyle(Style.STROKE);
		mGridDarkGrey.setStrokeWidth(10.0f);
		mGridDarkGrey.setTextSize(10.0f);
		mGridDarkGrey.setTextAlign(Align.CENTER);

		// Paint used to erase the rectangle behind the ring text
		mErasePaint = new Paint();
		mErasePaint.setColor(0xFF191919);
		mErasePaint.setStyle(Style.FILL);



		// New Radar :
		mSweepPaintNrBlue1 = new Paint(/*Paint.ANTI_ALIAS_FLAG*/);
		mSweepPaintNrBlue1.setColor(0x773333FF);//+++
		mSweepPaintNrBlue1.setAntiAlias(true);
		mSweepPaintNrBlue1.setStyle(Style.STROKE);
		mSweepPaintNrBlue1.setStrokeWidth(50.0f);

		// New Radar :
		mSweepPaintNrBlue2 = new Paint(/*Paint.ANTI_ALIAS_FLAG*/);
		mSweepPaintNrBlue2.setColor(0xFF7777FF);//+++
		mSweepPaintNrBlue2.setAntiAlias(true);

		// New Radar :
		mSweepPaintNrBlue3 = new Paint(/*Paint.ANTI_ALIAS_FLAG*/);
		mSweepPaintNrBlue3.setColor(0x223333FF);//+++
		mSweepPaintNrBlue3.setAntiAlias(true);


		mSweepPaintNrBlue3.setStyle(Style.STROKE);
		mSweepPaintNrBlue3.setStrokeWidth(120.0f);


		//New Radar : Red 'One'
		mSweepPaintYellow = new Paint(/*Paint.ANTI_ALIAS_FLAG*/);
		mSweepPaintYellow.setColor(0xFFFFA500);//+++
		mSweepPaintYellow.setAntiAlias(true);
		mSweepPaintYellow.setStyle(Style.STROKE);
		mSweepPaintYellow.setStrokeWidth(120.0f);

		mSweepPaintBlack = new Paint();
		mSweepPaintBlack.setColor(0xFF000000);
		mSweepPaintBlack.setTextSize(33);






		//New Radar : Red 'One'
		mSweepPaintPassiveRed1 = new Paint();
		mSweepPaintPassiveRed1.setColor(0x11FF3300);//+++
		// GREEN mSweepPaintNrRed1.setColor(0x55337733);//+++
		mSweepPaintPassiveRed1.setAntiAlias(true);
		mSweepPaintPassiveRed1.setStyle(Style.STROKE);

		//New Radar : Red 'two'
		mSweepPaintPassiveRed2 = new Paint();
		mSweepPaintPassiveRed2.setColor(0x11DD3300);//+++
		// GREEN mSweepPaintNrRed2.setColor(0x44337733);//+++
		mSweepPaintPassiveRed2.setAntiAlias(true);
		mSweepPaintPassiveRed2.setStyle(Style.STROKE);

		//New Radar : Red 'three'
		mSweepPaintPassiveRed3 = new Paint();
		mSweepPaintPassiveRed3.setColor(0x11AA3300);//+++
		// GREEN mSweepPaintNrRed3.setColor(0x33449944);//+++
		mSweepPaintPassiveRed3.setAntiAlias(true);
		mSweepPaintPassiveRed3.setStyle(Style.STROKE);

		//New Radar : Red 'four'
		mSweepPaintPassiveRed4 = new Paint();
		mSweepPaintPassiveRed4.setColor(0x11883300);//+++
		// GREEN mSweepPaintNrRed4.setColor(0x2277FF77);//+++
		mSweepPaintPassiveRed4.setAntiAlias(true);
		mSweepPaintPassiveRed4.setStyle(Style.STROKE);





		//New Radar : Red 'One'
		mSweepPaintActiveRed1 = new Paint();
		mSweepPaintActiveRed1.setColor(0x22FF3300);//+++
		mSweepPaintActiveRed1.setAntiAlias(true);
		mSweepPaintActiveRed1.setStyle(Style.STROKE);

		//New Radar : Red 'two'
		mSweepPaintActiveRed2 = new Paint();
		mSweepPaintActiveRed2.setColor(0x22DD3300);//+++
		mSweepPaintActiveRed2.setAntiAlias(true);
		mSweepPaintActiveRed2.setStyle(Style.STROKE);

		//New Radar : Red 'three'
		mSweepPaintActiveRed3 = new Paint();
		mSweepPaintActiveRed3.setColor(0x22AA3300);//+++
		mSweepPaintActiveRed3.setAntiAlias(true);
		mSweepPaintActiveRed3.setStyle(Style.STROKE);

		//New Radar : Red 'four'
		mSweepPaintActiveRed4 = new Paint();
		mSweepPaintActiveRed4.setColor(0x22883300);//+++
		// GREEN mSweepPaintNrRed4.setColor(0x2277FF77);//+++
		mSweepPaintActiveRed4.setAntiAlias(true);
		mSweepPaintActiveRed4.setStyle(Style.STROKE);



	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		int center = getWidth() / 2;
		int radius = center - 8;

//////////////////// more or less good <<BLUE>>
//		Paint mSweepPaintNrGreen;
//		mSweepPaintNrGreen = new Paint();
//		mSweepPaintNrGreen.setColor(0xAA3333FF);//+++
//		mSweepPaintNrGreen.setAntiAlias(true);
//
//		RadialGradient gradientGreen = new android.graphics.RadialGradient(
//				center, center,
//				radius, 0xAA3333FF, 0x77000011,
//				android.graphics.Shader.TileMode.CLAMP);
//		mSweepPaintNrGreen.setShader(gradientGreen);
//
//		canvas.drawCircle(center, center, radius, mSweepPaintNrGreen);
////////////////////more or less good.

////////////////////////// Very good <<GREEN>>
		Paint mSweepPaintNrGreen;
		mSweepPaintNrGreen = new Paint();
		mSweepPaintNrGreen.setColor(0xAA33FF33);//+++
		mSweepPaintNrGreen.setAntiAlias(true);

		RadialGradient gradientGreen = new android.graphics.RadialGradient(
				center, center,
				radius, 0xAA33FF33, 0x77000000,
		android.graphics.Shader.TileMode.CLAMP);
		mSweepPaintNrGreen.setShader(gradientGreen);

		canvas.drawCircle(center, center, radius, mSweepPaintNrGreen);
/////////////////////////////////////////////////////////////


		// Draw the rings
		final Paint gridPaint = mGridPaint;

		canvas.drawCircle(center, center, radius, gridPaint);
		canvas.drawCircle(center, center, radius - (radius >> 5), gridPaint);
		canvas.drawCircle(center, center, radius + (radius >> 5), gridPaint);

		canvas.drawCircle(center, center, radius/5 * 1, mGridPaint1);
		canvas.drawCircle(center, center, radius/5 * 2, mGridPaint1);
		canvas.drawCircle(center, center, radius/5 * 3, mGridPaint1);
		canvas.drawCircle(center, center, radius/5 * 4, mGridPaint1);

		mSweepPaintYellow.setStrokeWidth( ( radius>>5 ) / 2 );// margin 2
		canvas.drawCircle(center, center, radius + (radius >> 5) /2 , mSweepPaintYellow);

		// Draw small vertical strips in center of Area
		canvas.drawLine(center,	center - radius/5 + 12,	center,	center - 12, gridPaint);
		canvas.drawLine(center,	center + radius/5 - 12,	center,	center + 12, gridPaint);

		// Draw small horizontal strips in center of Area
		canvas.drawLine(center - radius/5 + 12,	center,	center - 12, center, gridPaint);
		canvas.drawLine(center + radius/5 - 12,	center,	center + 12, center, gridPaint);

		// iHalfFontHeight - font height divided by 2
		int iHalfFontHeight = 7;

		// Draw 0.5m 1m 3m 5m  EAST
		canvas.drawText("0.5m", center +   radius/5 + radius/10, center+iHalfFontHeight, gridPaint);
		canvas.drawText("1m",   center + 2*radius/5 + radius/10, center+iHalfFontHeight, gridPaint);
		canvas.drawText("3m",   center + 3*radius/5 + radius/10, center+iHalfFontHeight, gridPaint);
		canvas.drawText("5m",   center + 4*radius/5 + radius/10, center+iHalfFontHeight, gridPaint);


		// Draw 0.5m 1m 3m 5m WEST
		canvas.drawText("0.5m", center -   radius/5 - radius/10, center+iHalfFontHeight, gridPaint);
		canvas.drawText("1m",   center - 2*radius/5 - radius/10, center+iHalfFontHeight, gridPaint);
		canvas.drawText("3m",   center - 3*radius/5 - radius/10, center+iHalfFontHeight, gridPaint);
		canvas.drawText("5m",   center - 4*radius/5 - radius/10, center+iHalfFontHeight, gridPaint);

		// Draw 0.5m 1m 3m 5m NORD
		canvas.drawText("0.5m", center, center -   radius/5 - radius/10, gridPaint);
		canvas.drawText("1m",   center, center - 2*radius/5 - radius/10, gridPaint);
		canvas.drawText("3m",   center, center - 3*radius/5 - radius/10, gridPaint);
		canvas.drawText("5m",   center, center - 4*radius/5 - radius/10, gridPaint);

		// Draw 0.5m 1m 3m 5m SOUTH
		canvas.drawText("0.5m", center, center +   radius/5 + radius/10, gridPaint);
		canvas.drawText("1m",   center, center + 2*radius/5 + radius/10, gridPaint);
		canvas.drawText("3m",   center, center + 3*radius/5 + radius/10, gridPaint);
		canvas.drawText("5m",   center, center + 4*radius/5 + radius/10, gridPaint);

////////////////////////////////////////
//		// GOOD ///
//		Paint mSweepPaintNrGreen;
//		mSweepPaintNrGreen = new Paint();
//		mSweepPaintNrGreen.setColor(0x7733FF33);//+++
//		mSweepPaintNrGreen.setAntiAlias(true);
//
//		RadialGradient gradientGreen = new android.graphics.RadialGradient(
//				center, center,
//				radius, 0xAA33FF33, 0x3333FF33,
//		android.graphics.Shader.TileMode.CLAMP);
//		mSweepPaintNrGreen.setShader(gradientGreen);
//
//		canvas.drawCircle(center, center, radius, mSweepPaintNrGreen);
//		// GOOD ///

//		// BAD ///
//		Paint mSweepPaintNrGreen;
//		mSweepPaintNrGreen = new Paint();
//		mSweepPaintNrGreen.setAntiAlias(true);
//
//		RadialGradient gradientGreen = new android.graphics.RadialGradient(
//				center, center,
//				radius, 0xAA3333FF, 0x333333FF,
//		android.graphics.Shader.TileMode.CLAMP);
//		mSweepPaintNrGreen.setShader(gradientGreen);
//
//		canvas.drawCircle(center, center, radius, mSweepPaintNrGreen);
//		// BAD ///

//		// BAD //
//		Paint mSweepPaintNrGreen;
//		mSweepPaintNrGreen = new Paint();
//		mSweepPaintNrGreen.setAntiAlias(true);
//
//		RadialGradient gradientGreen = new android.graphics.RadialGradient(
//				center, center,
//				radius, 0xCC333333, 0x33CCCCCC,
//		android.graphics.Shader.TileMode.CLAMP);
//		mSweepPaintNrGreen.setShader(gradientGreen);
//
//		canvas.drawCircle(center, center, radius, mSweepPaintNrGreen);
//		// BAD //


		// BAD
//		Paint mSweepPaintNrGreen;
//		mSweepPaintNrGreen = new Paint();
//		mSweepPaintNrGreen.setAntiAlias(true);
//
//		RadialGradient gradientGreen = new android.graphics.RadialGradient(
//				center, center,
//				radius, 0x77000080, 0x11000080,
//		android.graphics.Shader.TileMode.CLAMP);
//		mSweepPaintNrGreen.setShader(gradientGreen);
//
//		canvas.drawCircle(center, center, radius, mSweepPaintNrGreen);
		// BAD


		// H/Z/ ?
//		Paint mSweepPaintNrGreen;
//		mSweepPaintNrGreen = new Paint();
//		mSweepPaintNrGreen.setColor(0xFF33FF33);//+++
//		mSweepPaintNrGreen.setAntiAlias(true);
//
//		RadialGradient gradientGreen = new android.graphics.RadialGradient(
//				center, center,
//				radius, 0xFF33FF33, 0x77000000,
//		android.graphics.Shader.TileMode.CLAMP);
//		mSweepPaintNrGreen.setShader(gradientGreen);
//
//		canvas.drawCircle(center, center, radius, mSweepPaintNrGreen);



////////////////////////////////////////


		// original : int blipRadius = (int) (mDistanceRatio * radius  );
		int blipRadius = (int) (mDistanceRatio * (radius - (radius >> 4) ) );


		final long now = SystemClock.uptimeMillis() ;

		mSweepPaintPassiveRed1.setStrokeWidth(4*radius/20);
		mSweepPaintPassiveRed2.setStrokeWidth(3*radius/20);
		mSweepPaintPassiveRed3.setStrokeWidth(2*radius/20);
		mSweepPaintPassiveRed4.setStrokeWidth(1*radius/20);


		mSweepPaintActiveRed1.setStrokeWidth(4*radius/20);
		mSweepPaintActiveRed2.setStrokeWidth(3*radius/20);
		mSweepPaintActiveRed3.setStrokeWidth(2*radius/20);
		mSweepPaintActiveRed4.setStrokeWidth(1*radius/20);





		//if (false)
		if (mSweepTime > 0)
		{
			// Draw the sweep. Radius is determined by how long ago it started [long sweepDifference = (now - mSweepTime);]
			//long sweepDifference = (now - mSweepTime) / 8;
			long sweepDifference = (now - mSweepTime);


			if (sweepDifference < 1000 ) // on backdraft ADDED
			//	if (sweepDifference < 512L )  // on backdraft COMMENTED OUT
			{
				int sweepRadius = (int) (((radius - (radius >> 4) + 6) * sweepDifference) >> 9);

				if (sweepRadius > radius)  sweepRadius = 2*radius - sweepRadius; 					// on backdraft ADDED

				int sweepRadius_ = (int) (((radius - (radius >> 4) + 6) * sweepDifference) >> 9); 					// on backdraft ADDED

				if ( BluetoothLeService.STATE_CONNECTED == mConnectionState )
				{
					// 1. First: 'pulsing' blue cloud, bzw. repeating red circle.
					if (  (radius/10) < sweepRadius   )
					{
						if (false)
						// OBSOLETE
						{
							// 	let's draw a blue cloud 'pulsing' in center everywhere except  sweepRadius-area, or lets have some spare time otherwise.
							int MiniRad = 10;
							mSweepPaintNrBlue2.setAntiAlias(true);
							RadialGradient gradientBlue2 = new android.graphics.RadialGradient(
									center, center,
//+++ more or less good <<BLUE>>					Math.max((float)MiniRad, (float)sweepRadius), 0xDD7777FF, 0x55333377 ,   /* BLUE */
//	GOOD <<GREEN>>									Math.max((float)MiniRad, (float)sweepRadius), 0xDD77FF77, 0x55337733 , /* GREEN */
													Math.max((float)MiniRad, (float)sweepRadius), 0xDDFF7722, 0x55775511 , /* YELLOW */
									android.graphics.Shader.TileMode.CLAMP);
							mSweepPaintNrBlue2.setShader(gradientBlue2);
							canvas.drawCircle(center, center, sweepRadius, mSweepPaintNrBlue2);
						}
						else
						{

							if ( iPreviousRSSI < -37)
							{
								// nix!
							}
							else if ( iPreviousRSSI < -27 )
							{
								if (2*radius - 4*radius/5 - ((radius/5)/2) > sweepRadius_ ) // on backdraft ADDED
								{
									// 	let's draw a blue cloud 'pulsing' in center everywhere except  sweepRadius-area, or lets have some spare time otherwise.
									int MiniRad = 10;
									mSweepPaintNrBlue2.setAntiAlias(true);
									RadialGradient gradientBlue2 = new android.graphics.RadialGradient(
											center, center,
															Math.max((float)MiniRad, (float)sweepRadius), 0xDDFFFF22, 0x55777711 ,   /* YELLOW */
//+++ good <<BLUE>>											Math.max((float)MiniRad, (float)sweepRadius), 0xDD7777FF, 0x55333377 ,   /* BLUE */
// very good <<GREEN>>												Math.max((float)MiniRad, (float)sweepRadius), 0xDD77FF77, 0x55337733 , /* GREEN */
											android.graphics.Shader.TileMode.CLAMP);
									mSweepPaintNrBlue2.setShader(gradientBlue2);
									canvas.drawCircle(center, center, sweepRadius, mSweepPaintNrBlue2);
								}
								else
								{
									canvas.drawCircle(center, center, 4*radius/5 + ((radius/5)/2), mSweepPaintActiveRed1);
									canvas.drawCircle(center, center, 4*radius/5 + ((radius/5)/2), mSweepPaintActiveRed2);
									canvas.drawCircle(center, center, 4*radius/5 + ((radius/5)/2), mSweepPaintActiveRed3);
									canvas.drawCircle(center, center, 4*radius/5 + ((radius/5)/2), mSweepPaintActiveRed4);
								}
							}
							else if ( iPreviousRSSI < -22 )
							{
								if (2*radius - 3*radius/5 - ((radius/5)/2) > sweepRadius_ ) // on backdraft ADDED
								{
									// 	let's draw a blue cloud 'pulsing' in center everywhere except  sweepRadius-area, or lets have some spare time otherwise.
									int MiniRad = 10;
									mSweepPaintNrBlue2.setAntiAlias(true);
									RadialGradient gradientBlue2 = new android.graphics.RadialGradient(
											center, center,
											Math.max((float)MiniRad, (float)sweepRadius), 0xDDFFFF22, 0x55777711 ,   /* YELLOW */
//+++ good <<BLUE>>											Math.max((float)MiniRad, (float)sweepRadius), 0xDD7777FF, 0x55333377 ,   /* BLUE */
// very good <<GREEN>>												Math.max((float)MiniRad, (float)sweepRadius), 0xDD77FF77, 0x55337733 , /* GREEN */
											android.graphics.Shader.TileMode.CLAMP);
									mSweepPaintNrBlue2.setShader(gradientBlue2);
									canvas.drawCircle(center, center, sweepRadius, mSweepPaintNrBlue2);
								}
								else
								{
									canvas.drawCircle(center, center, 3*radius/5 + ((radius/5)/2), mSweepPaintActiveRed1);
									canvas.drawCircle(center, center, 3*radius/5 + ((radius/5)/2), mSweepPaintActiveRed2);
									canvas.drawCircle(center, center, 3*radius/5 + ((radius/5)/2), mSweepPaintActiveRed3);
									canvas.drawCircle(center, center, 3*radius/5 + ((radius/5)/2), mSweepPaintActiveRed4);
								}
							}
							else if ( iPreviousRSSI < -16 )
							{
								if (2*radius - 2*radius/5 - ((radius/5)/2) > sweepRadius_ ) // on backdraft ADDED
								{
									// 	let's draw a blue cloud 'pulsing' in center everywhere except  sweepRadius-area, or lets have some spare time otherwise.
									int MiniRad = 10;
									mSweepPaintNrBlue2.setAntiAlias(true);
									RadialGradient gradientBlue2 = new android.graphics.RadialGradient(
											center, center,
											Math.max((float)MiniRad, (float)sweepRadius), 0xDDFFFF22, 0x55777711 ,   /* YELLOW */
//+++ good <<BLUE>>											Math.max((float)MiniRad, (float)sweepRadius), 0xDD7777FF, 0x55333377 ,   /* BLUE */
// very good <<GREEN>>												Math.max((float)MiniRad, (float)sweepRadius), 0xDD77FF77, 0x55337733 , /* GREEN */
											android.graphics.Shader.TileMode.CLAMP);
									mSweepPaintNrBlue2.setShader(gradientBlue2);
									canvas.drawCircle(center, center, sweepRadius, mSweepPaintNrBlue2);
								}
								else
								{
									canvas.drawCircle(center, center, 2*radius/5 + ((radius/5)/2), mSweepPaintActiveRed1);
									canvas.drawCircle(center, center, 2*radius/5 + ((radius/5)/2), mSweepPaintActiveRed2);
									canvas.drawCircle(center, center, 2*radius/5 + ((radius/5)/2), mSweepPaintActiveRed3);
									canvas.drawCircle(center, center, 2*radius/5 + ((radius/5)/2), mSweepPaintActiveRed4);
								}
							}
							else if ( iPreviousRSSI < -10 )
							{
								if (2*radius - radius/5 - ((radius/5)/2) > sweepRadius_ ) // on backdraft ADDED
								{
									// 	let's draw a blue cloud 'pulsing' in center everywhere except  sweepRadius-area, or lets have some spare time otherwise.
									int MiniRad = 10;
									mSweepPaintNrBlue2.setAntiAlias(true);
									RadialGradient gradientBlue2 = new android.graphics.RadialGradient(
											center, center,
											Math.max((float)MiniRad, (float)sweepRadius), 0xDDFFFF22, 0x55777711 ,   /* YELLOW */
//+++ good <<BLUE>>											Math.max((float)MiniRad, (float)sweepRadius), 0xDD7777FF, 0x55333377 ,   /* BLUE */
// very good <<GREEN>>												Math.max((float)MiniRad, (float)sweepRadius), 0xDD77FF77, 0x55337733 , /* GREEN */
											android.graphics.Shader.TileMode.CLAMP);
									mSweepPaintNrBlue2.setShader(gradientBlue2);
									canvas.drawCircle(center, center, sweepRadius, mSweepPaintNrBlue2);
								}
								else
								{
									canvas.drawCircle(center, center, radius/5 + ((radius/5)/2), mSweepPaintActiveRed1);
									canvas.drawCircle(center, center, radius/5 + ((radius/5)/2), mSweepPaintActiveRed2);
									canvas.drawCircle(center, center, radius/5 + ((radius/5)/2), mSweepPaintActiveRed3);
									canvas.drawCircle(center, center, radius/5 + ((radius/5)/2), mSweepPaintActiveRed4);
								}
							}
							else if ( iPreviousRSSI <= 1 )
							{
								if (2*radius - ((radius/5)/2) > sweepRadius_ ) // on backdraft ADDED
								{
									// 	let's draw a blue cloud 'pulsing' in center everywhere except  sweepRadius-area, or lets have some spare time otherwise.
									int MiniRad = 10;
									mSweepPaintNrBlue2.setAntiAlias(true);
									RadialGradient gradientBlue2 = new android.graphics.RadialGradient(
											center, center,
											Math.max((float)MiniRad, (float)sweepRadius), 0xDDFFFF22, 0x55777711 ,   /* YELLOW */
//+++ good <<BLUE>>											Math.max((float)MiniRad, (float)sweepRadius), 0xDD7777FF, 0x55333377 ,   /* BLUE */
// very good <<GREEN>>												Math.max((float)MiniRad, (float)sweepRadius), 0xDD77FF77, 0x55337733 , /* GREEN */
											android.graphics.Shader.TileMode.CLAMP);
									mSweepPaintNrBlue2.setShader(gradientBlue2);
									canvas.drawCircle(center, center, sweepRadius, mSweepPaintNrBlue2);
								}
								else
								{
									canvas.drawCircle(center, center,  ((radius/5)/2), mSweepPaintActiveRed1);
									canvas.drawCircle(center, center,  ((radius/5)/2), mSweepPaintActiveRed2);
									canvas.drawCircle(center, center,  ((radius/5)/2), mSweepPaintActiveRed3);
									canvas.drawCircle(center, center,  ((radius/5)/2), mSweepPaintActiveRed4);
								}
							}
							else
							{
								// nothing!
							}

						}
					}

					else
					{

					}


					// 2. Second. Red <Halo> to mark the distance between smartphone and currently selected  device
					if ( iCurrentRSSI < -37)
					{
						// TODO: study RSSI/dBm at http://www.tamos.ru/htmlhelp/commwifi/signal.htm
						canvas.drawText("TOP mark error: RSSI dBm belongs to [-37..-0], you have:  " + Integer.toString(iCurrentRSSI) , 100, center*2+100+40, mSweepPaintBlack);
					}
					else if ( iCurrentRSSI < -27 )
					{
						//if (sweepRadius >= 4*radius/5 + ((radius/5)/2))
						{
							canvas.drawCircle(center, center, 4*radius/5 + ((radius/5)/2), mSweepPaintPassiveRed1);
							canvas.drawCircle(center, center, 4*radius/5 + ((radius/5)/2), mSweepPaintPassiveRed2);
							canvas.drawCircle(center, center, 4*radius/5 + ((radius/5)/2), mSweepPaintPassiveRed3);
							canvas.drawCircle(center, center, 4*radius/5 + ((radius/5)/2), mSweepPaintPassiveRed4);
							iPreviousRSSI = iCurrentRSSI;
						}
					}
					else if ( iCurrentRSSI < -22 )
					{
						//if (sweepRadius >= 3*radius/5 + ((radius/5)/2))
						{
							canvas.drawCircle(center, center, 3*radius/5 + ((radius/5)/2), mSweepPaintPassiveRed1);
							canvas.drawCircle(center, center, 3*radius/5 + ((radius/5)/2), mSweepPaintPassiveRed2);
							canvas.drawCircle(center, center, 3*radius/5 + ((radius/5)/2), mSweepPaintPassiveRed3);
							canvas.drawCircle(center, center, 3*radius/5 + ((radius/5)/2), mSweepPaintPassiveRed4);
							iPreviousRSSI = iCurrentRSSI;
						}
					}
					else if ( iCurrentRSSI < -16 )
					{
						//if (sweepRadius >= 2*radius/5 + ((radius/5)/2))
						{
							canvas.drawCircle(center, center, 2*radius/5 + ((radius/5)/2), mSweepPaintPassiveRed1);
							canvas.drawCircle(center, center, 2*radius/5 + ((radius/5)/2), mSweepPaintPassiveRed2);
							canvas.drawCircle(center, center, 2*radius/5 + ((radius/5)/2), mSweepPaintPassiveRed3);
							canvas.drawCircle(center, center, 2*radius/5 + ((radius/5)/2), mSweepPaintPassiveRed4);
							iPreviousRSSI = iCurrentRSSI;
						}
					}
					else if ( iCurrentRSSI < -10 )
					{
						//if (sweepRadius >= 1*radius/5 + ((radius/5)/2))
						{
							canvas.drawCircle(center, center, radius/5 + ((radius/5)/2), mSweepPaintPassiveRed1);
							canvas.drawCircle(center, center, radius/5 + ((radius/5)/2), mSweepPaintPassiveRed2);
							canvas.drawCircle(center, center, radius/5 + ((radius/5)/2), mSweepPaintPassiveRed3);
							canvas.drawCircle(center, center, radius/5 + ((radius/5)/2), mSweepPaintPassiveRed4);
							iPreviousRSSI = iCurrentRSSI;
						}
					}
					else if ( iCurrentRSSI <= 1 )
					{
						//if (sweepRadius >= (radius/5)/2)
						{
							// Draw small red circle in center of Draw. Almost never seen.
							canvas.drawCircle(center, center, (radius/5)/2, mSweepPaintPassiveRed1);
							canvas.drawCircle(center, center, (radius/5)/2, mSweepPaintPassiveRed2);
							canvas.drawCircle(center, center, (radius/5)/2, mSweepPaintPassiveRed3);
							canvas.drawCircle(center, center, (radius/5)/2, mSweepPaintPassiveRed4);
							iPreviousRSSI = iCurrentRSSI;
						}
					}
					else
					{
						// TODO: study RSSI/dBm at http://www.tamos.ru/htmlhelp/commwifi/signal.htm
						canvas.drawText("BOTTOM mark error: RSSI dBm belongs to [-33..-0], you have:  " + Integer.toString(iCurrentRSSI) , 100, center*2+100+40, mSweepPaintBlack);
					}
				}
				else
				{
					// Rest cases, e.g. BluetoothLeService.STATE_DISCONNECTED,
					// BluetoothLeService.STATE_DISCONNECTING, BluetoothLeService.STATE_ONNECTING

					mGridDarkGrey.setStrokeWidth( ( radius>>5 ) / 2 );// margin 2
					canvas.drawCircle(center, center, radius + (radius >> 5) /2 , mGridDarkGrey);

					canvas.drawText("Urregular/Not_connected RSSI== " + Integer.toString(iCurrentRSSI), 100, center*2+100+40, mSweepPaintBlack);
				}

				// Note when the sweep has passed the blip we out to do some extra handling
				boolean before = sweepRadius < blipRadius;

				if (!before && mSweepBefore)
				{
					// Don't clean draw area, draw into 'as is'
					mSweepBefore = false;

					// Equalize both timers
					mBlipTime = now;
				}



			}
			else
			{




				//mSweepTime = now +4*999; // 8 seconds, TODO: keep it for debug
				//mSweepTime = now +2*999; // 4 seconds, TODO: keep it for debug
				//mSweepTime = now + 1000; // 2 seconds, TODO: keep it for debug
				mSweepTime = now + 500; // 1 second, TODO: keep it for debug

				// <300> means that three times a second we renew the Draw
				//mSweepTime = now + 300;

				// Draw area should be cleaned before any further drawing
				mSweepBefore = true;

				// Play the <beep> sound
				soundPoolRadar.play(soundID_beep, 1.0f, 1.0f, 1, 0, 1f);

			}

			// The helluva Draw-area must be heavenly bundled and re-drawn. That's it!
			postInvalidate();
		}

		// Draw vertical radial strips
		canvas.drawLine(center, center - radius + (radius >> 5) + 3, center, center - radius - 3, gridPaint);
		canvas.drawLine(center,	center + radius - (radius >> 5) - 3, center, center + radius + 3, gridPaint);

		// Draw horizontal radial strips
		canvas.drawLine(center - radius + (radius >> 5) + 3, center, center - radius - 3, center, gridPaint);
		canvas.drawLine(center + radius - (radius >> 5) - 3, center, center + radius + 3, center, gridPaint);

		double PiBy4 = Math.toRadians(45);

		// Draw <Pi/4>-oriented radial strips
		canvas.drawLine(
				(float)(center + (radius - (radius >> 5) - 3)*Math.cos(PiBy4)),
				(float)(center - (radius - (radius >> 5) - 3)*Math.sin(PiBy4)),
				(float)(center + (radius + 2)*Math.cos(PiBy4)),
				(float)(center - (radius + 2)*Math.sin(PiBy4)),
				gridPaint
		);

		canvas.drawLine(
				(float)(center - (radius - (radius >> 5) - 3)*Math.cos(PiBy4)),
				(float)(center - (radius - (radius >> 5) - 3)*Math.sin(PiBy4)),
				(float)(center - (radius + 2)*Math.cos(PiBy4)),
				(float)(center - (radius + 2)*Math.sin(PiBy4)),
				gridPaint
		);

		canvas.drawLine(
				(float)(center + (radius - (radius >> 5) - 3)*Math.cos(PiBy4)),
				(float)(center + (radius - (radius >> 5) - 3)*Math.sin(PiBy4)),
				(float)(center + (radius + 2)*Math.cos(PiBy4)),
				(float)(center + (radius + 2)*Math.sin(PiBy4)),
				gridPaint
		);

		canvas.drawLine(
				(float)(center - (radius - (radius >> 5) - 3)*Math.cos(PiBy4)),
				(float)(center + (radius - (radius >> 5) - 3)*Math.sin(PiBy4)),
				(float)(center - (radius + 2)*Math.cos(PiBy4)),
				(float)(center + (radius + 2)*Math.sin(PiBy4)),
				gridPaint
		);

		{


			// Draw the blip. Alpha is based on how long ago the sweep crossed the blip
			long blipDifference = now - mBlipTime;

			gridPaint.setAlpha(255 - (int) ((128 * blipDifference) >> 10));

			// TODO: this is purely debug-purposed shit. TODO: Remove despite it looks informative!
			canvas.drawText(LongString, 100, center*2+100, mSweepPaintBlack);

			// TODO:  ... canvas.drawText(mName, center-100, 10, mSweepPaintBlack);

			gridPaint.setAlpha(255);
		}
	}

	// called by <onResume> in upper <View>-entity, such as <SearchFragment>
	public void startOwnRadar( int iIndex /* a.k.a. (currentPosition+1) */)
	{
		// Initialize time values for drawing
		mSweepTime = SystemClock.uptimeMillis();
		mSweepBefore = true;

		// Initially we know nothing about connection state
		mConnectionState = BluetoothLeService.STATE_DISCONNECTED;

		// Get sound data into Snd.-player
		soundPoolRadar = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundID_beep = soundPoolRadar.load("/sdcard/Music/countdown.wav", 1);

		// Start data-catching
		mOwnRssiReceiver = new mRssiReceiverX(iIndex - 1);
		getContext().registerReceiver(mOwnRssiReceiver, RssiFilter());
	}

	// called by <onResume> in upper <View>-entity, such as <SearchFragment>
	public void startOwnNamedRadar( int iIndex , String sName, String sAddr)
	{
		// Initialize time values for drawing
		mSweepTime = SystemClock.uptimeMillis();
		mSweepBefore = true;

		// Initially we know nothing about connection state
		mConnectionState = BluetoothLeService.STATE_DISCONNECTED;

		// Get sound data into Snd.-player
		soundPoolRadar = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundID_beep = soundPoolRadar.load("/sdcard/Music/countdown.wav", 1);// FIXME: beware: this piece implies us to have it placed over there (e.g. into /storage/sdcard0/) before any activity.

		// Start data-catching
		mOwnRssiReceiver = new mRssiReceiverX(iIndex - 1);
		getContext().registerReceiver(mOwnRssiReceiver, RssiFilter());

		// Give it a name
		mName = sName;
		mAddr = sAddr;

	}

	// called by <onDestroyView> in upper <View>-entity, such as <SearchFragment>
	public void stopOwnRadar( int iIndex )
	{
		// Turn the animation off
		mSweepTime = 0L;

		// Stop catching data;
		getContext().unregisterReceiver(mOwnRssiReceiver);// stop this instance
	}

	// Do some useful pre-draw, pre-catch things
	public void initialize()
	{
		;
	}

	private static IntentFilter RssiFilter()
	{
		final IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction(RSSI_CONNECTED);
		intentFilter.addAction(RSSI_DISCONNECTED);
		intentFilter.addAction(RSSI_DATA);
		intentFilter.addAction(RSSI_AUX);

		return intentFilter;
	}

	public class mRssiReceiverX extends BroadcastReceiver
	{
		private int interX;

		public mRssiReceiverX(int i)
		{
			// 1.
			interX = i;

			// 2. etc

			// 3. etc
		}

		@Override
		public void onReceive(Context context, Intent intent)
		{
			final String action = intent.getAction();

			if (action.equals(RSSI_CONNECTED))
			{
				mConnectionState = intent.getIntExtra("RSSI_CONNECTED", BluetoothLeService.STATE_CONNECTED);
				iPreviousRSSI = -36;// low bottom + 1
			}
			else
			if (action.equals(RSSI_DISCONNECTED))
			{
				mConnectionState = intent.getIntExtra("RSSI_DISCONNECTED", BluetoothLeService.STATE_DISCONNECTED);
			}
			else
			if (action.equals(RSSI_DATA))
			{
				iCurrentRSSI = intent.getIntExtra("RSSI_DATA", 0);
				mConnectionState = BluetoothLeService.STATE_CONNECTED;
			}
			else
			if (action.equals(RSSI_AUX))
			{
				LongString = intent.getExtras().getString("aux");
			}
			else
			{
				//Toast.makeText( this, "intent NOT CAUGHT ", Toast.LENGTH_LONG).show();
			}

		} // public void onReceive

	} // public class mServsReceiverX extends BroadcastReceiver

}
