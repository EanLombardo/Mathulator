package com.ic.mathulator;

import java.text.DecimalFormat;

import com.ic.libICMath.ICCalculatorBase;
import com.ic.libICMath.ICFunction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
class GraphView extends View
{
	public int centerX=0;
	public int centerY=0;
	
	public int offsetX=0;
	public int offsetY=0;
	
	public int translateX=0;
	public int translateY=0;
	
	public int downX=0;
	public int downY=0;
	
	public double scale=1;
	public int scaleBasis=1;
	
	private volatile int lastY=0;
	
	public volatile  ICCalculatorBase calculator;
	
	volatile int width;
	volatile int height;
	
	volatile int bufferWidth;
	volatile int bufferHeight;
	
	public int drawMode=DrawModes.DRAWMODE_NORMAL;
	public static interface DrawModes
	{
		int DRAWMODE_NORMAL=0;
		int DRAWMODE_TRANSLATE=1;
	}
	
	public static interface Paints
	{
		public static Paint BLUE=new Paint();
		public static Paint BLACK=new Paint();
		public static Paint WHITE=new Paint();
	}
	
	volatile Bitmap bufferBitmap=null;
	volatile Canvas bufferCanvas=null;
	volatile Path graphPath=new Path();
	
	Double lastValue;

	public GraphView(Context context, ICCalculatorBase calc)
	{
		super(context);
		calculator = calc;
		
		Paints.BLUE.setColor(Color.BLUE);
		Paints.BLUE.setAntiAlias(true);
		Paints.BLUE.setStrokeWidth(2);
		Paints.BLUE.setStyle(Style.STROKE);
		Paints.BLACK.setColor(Color.BLACK);
		Paints.WHITE.setColor(Color.WHITE);
		Paints.WHITE.setAlpha(128);
	}
	
	public void invalidateBuffer(boolean reDraw)
	{

		bufferCanvas.drawARGB(255,0,0,0);
		
		lastY=-1;
		for(int functionIndex=0;functionIndex<calculator.graphingFunctions.size();functionIndex++)
		{
			ICFunction fun=calculator.graphingFunctions.get(functionIndex);
			graphPath.rewind();
		for(int pixelIndex=0;pixelIndex<bufferWidth;pixelIndex++)
		{
			int currentPos=pixelIndex-centerX-offsetX;
			int value=(int)fun.eval((double)currentPos);
			int curY=(centerY+offsetY)-value;
			if(lastY<0)
			{
				graphPath.moveTo(pixelIndex, curY);
			}
			else
			{
				graphPath.lineTo(pixelIndex,curY);	
			}
			
			lastY=(centerY+offsetY)-value;
		}
		bufferCanvas.drawPath(graphPath, Paints.BLUE);
		}
		bufferCanvas.drawLine(centerX+offsetX+translateX, 0, centerX+offsetX+translateX, centerY*2, Paints.WHITE);
		bufferCanvas.drawLine(0, centerY+offsetY+translateY, centerX*2, centerY+offsetY+translateY, Paints.WHITE);
		
		if(reDraw)
		{
			invalidate();
		}

	}
	@Override
	public void onDraw(Canvas canvas)
	{
		width=canvas.getWidth();
		height=canvas.getHeight();
		
		bufferWidth=3*width;
		bufferHeight=3*height;
		
		if(bufferBitmap==null)
		{
			bufferBitmap=null;
			bufferBitmap=Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);
			
			bufferCanvas=null;
			bufferCanvas = new Canvas(bufferBitmap);
			invalidateBuffer(false);
		}
		if(centerX==0)
		{
			centerX=(3*this.getWidth())/2;
			centerY=(3*this.getHeight())/2;
		}
		
		if(drawMode==DrawModes.DRAWMODE_NORMAL)
		{
			canvas.drawBitmap(bufferBitmap, -width, -height, Paints.BLACK);
		}
		else
		{
			canvas.translate(translateX, translateY);
			canvas.drawBitmap(bufferBitmap, -width, -height, Paints.BLACK);
			canvas.translate(-translateX, -translateY);
			
			int from=0;
			int to=0;
			if(translateX>0)
			{
				from=0;
				to=translateX;
			}
			else if(translateX<0)
			{
				from=canvas.getWidth()+translateX;
				to=canvas.getWidth();
			}
			
			
			
		}
		canvas.drawText("offsetX= "+String.valueOf(offsetX), 0, 100, Paints.BLUE);
		canvas.drawText("offsetY= "+String.valueOf(offsetY), 0, 120, Paints.BLUE);
		
		canvas.drawText("translateX= "+String.valueOf(translateX), 0, 140, Paints.BLUE);
		canvas.drawText("translateY= "+String.valueOf(translateY), 0, 160, Paints.BLUE);
		
		canvas.drawText("centerX= "+String.valueOf(centerX), 0, 180, Paints.BLUE);
		canvas.drawText("centerY= "+String.valueOf(centerY), 0, 200, Paints.BLUE);
	}

	@Override
	public boolean onTouchEvent(MotionEvent me)
	{
			int action = me.getAction();
			if (me.getPointerCount() == 1 && action == MotionEvent.ACTION_UP)
			{
				drawMode=DrawModes.DRAWMODE_NORMAL;
				offsetX+=translateX;
				offsetY+=translateY;
				translateX=0;
				translateY=0;
				invalidateBuffer(false);
			}
			else if((me.getPointerCount() == 1 && action == MotionEvent.ACTION_DOWN))
			{
				drawMode=DrawModes.DRAWMODE_TRANSLATE;
				
				downX=(int)me.getX();
				downY=(int)me.getY();
			}
			
			if(drawMode==DrawModes.DRAWMODE_TRANSLATE && action == MotionEvent.ACTION_MOVE)
			{
				translateX=(int)me.getX()-downX;
				translateY=(int)me.getY()-downY;
				
			}
			invalidate();
			
			
		return true;
	}
}