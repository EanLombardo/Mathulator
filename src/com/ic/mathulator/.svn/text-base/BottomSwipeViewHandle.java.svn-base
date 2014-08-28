package com.ic.mathulator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

public class BottomSwipeViewHandle extends android.view.View
{
	Context cont;
	Bitmap arrow;
	Bitmap rest_of_bar;
	Paint p;

	public BottomSwipeViewHandle(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		cont = context;
		arrow = ((BitmapDrawable) cont.getResources().getDrawable(R.drawable.bottom_swipe_arrow)).getBitmap();
		rest_of_bar = ((BitmapDrawable) cont.getResources().getDrawable(R.drawable.bottom_swipe)).getBitmap();

		p = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		int draw_index;
		for (draw_index = 0; draw_index < (canvas.getWidth() / 30) + 1; draw_index++)
		{
			canvas.drawBitmap(rest_of_bar, draw_index * 30, 0, p);
		}
		canvas.drawBitmap(arrow, (canvas.getWidth() / 2) - 15, 0, p);

	}

}
