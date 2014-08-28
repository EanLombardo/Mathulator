package com.ic.mathulator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

public class LeftSwipeViewHandle extends View
{
	Context cont;
	Bitmap arrow;
	Bitmap rest_of_bar;
	Paint p;

	public LeftSwipeViewHandle(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		cont = context;
		arrow = ((BitmapDrawable) cont.getResources().getDrawable(R.drawable.left_swipe_arrow)).getBitmap();
		rest_of_bar = ((BitmapDrawable) cont.getResources().getDrawable(R.drawable.left_swipe)).getBitmap();
		p = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		int draw_index;
		for (draw_index = 0; draw_index < (canvas.getHeight() / 30) + 1; draw_index++)
		{
			canvas.drawBitmap(rest_of_bar, 0, draw_index * 30, p);
		}
		canvas.drawBitmap(arrow, 0, (canvas.getHeight() / 2) - 15, p);

	}

}
