package com.ic.mathulator;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.text.ClipboardManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CalculationView extends LinearLayout
{
	String mInput;
	String mOutput;
	String mTime;
	String mRPN;
	SharedPreferences mPrefs;

	TextView InView;
	TextView OutView;
	TextView TimeView;
	TextView RPNView;
	View Bar;
	LinearLayout mLayout;
	MenuItem mCopyIn;
	MenuItem mCopyOut;
	MenuItem mViewInfo;
	MenuItem mDelete;

	public CalculationView(Context context, SharedPreferences prefs, LinearLayout layout)
	{
		super(context);
		mPrefs = prefs;
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.calculation_view, this);
		InView = (TextView) findViewById(R.id.in);
		OutView = (TextView) findViewById(R.id.output);
		TimeView = (TextView) findViewById(R.id.time);
		RPNView = (TextView) findViewById(R.id.rpn);
		Bar = (View) findViewById(R.id.bar1);
		mLayout = layout;

		if (mPrefs.getBoolean("separate_input", false))
		{
			Bar.setVisibility(VISIBLE);
		}
		else
		{
			Bar.setVisibility(GONE);
		}

		if (mPrefs.getBoolean("show_rpn", false))
		{
			RPNView.setVisibility(VISIBLE);
		}
		else
		{
			RPNView.setVisibility(GONE);
		}

		if (mPrefs.getBoolean("show_time", false))
		{
			TimeView.setVisibility(VISIBLE);
		}
		else
		{
			TimeView.setVisibility(GONE);
		}
	}

	public void setInput(String in)
	{
		mInput = in;
		InView.setText(mInput);
		if (mPrefs.getBoolean("show_rpn", false))
		{
			RPNView.setVisibility(VISIBLE);
		}
		else
		{
			RPNView.setVisibility(GONE);
		}

		if (mPrefs.getBoolean("show_time", false))
		{
			TimeView.setVisibility(VISIBLE);
		}
		else
		{
			TimeView.setVisibility(GONE);
		}
	}

	public void setOutput(String out)
	{
		mOutput = out;
		OutView.setText(mOutput);
		if (mPrefs.getBoolean("show_rpn", false))
		{
			RPNView.setVisibility(VISIBLE);
		}
		else
		{
			RPNView.setVisibility(GONE);
		}

		if (mPrefs.getBoolean("show_time", false))
		{
			TimeView.setVisibility(VISIBLE);
		}
		else
		{
			TimeView.setVisibility(GONE);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu)
	{
		menu.add("Copy Input");
		menu.add("Copy Output");
		menu.add("View Info");
		menu.add("Delete");

		mCopyIn = menu.getItem(0);
		mCopyOut = menu.getItem(1);
		mViewInfo = menu.getItem(2);
		mDelete = menu.getItem(3);

		super.onCreateContextMenu(menu);
	}

	public void setTime(String time)
	{
		mTime = time;
		TimeView.setText(mTime + " nanosecond(s)");
		if (mPrefs.getBoolean("show_rpn", false))
		{
			RPNView.setVisibility(VISIBLE);
		}
		else
		{
			RPNView.setVisibility(GONE);
		}

		if (mPrefs.getBoolean("show_time", false))
		{
			TimeView.setVisibility(VISIBLE);
		}
		else
		{
			TimeView.setVisibility(GONE);
		}
	}

	public void setRPN(String RPN)
	{
		mRPN = RPN;
		RPNView.setText("RPN: " + mRPN);
		if (mPrefs.getBoolean("show_rpn", false))
		{
			RPNView.setVisibility(VISIBLE);
		}
		else
		{
			RPNView.setVisibility(GONE);
		}

		if (mPrefs.getBoolean("show_time", false))
		{
			TimeView.setVisibility(VISIBLE);
		}
		else
		{
			TimeView.setVisibility(GONE);
		}
	}

	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (mPrefs.getBoolean("show_rpn", false))
		{
			RPNView.setVisibility(VISIBLE);
		}
		else
		{
			RPNView.setVisibility(GONE);
		}

		if (mPrefs.getBoolean("show_time", false))
		{
			TimeView.setVisibility(VISIBLE);
		}
		else
		{
			TimeView.setVisibility(GONE);
		}
	}

	public boolean processContextSelection(MenuItem id)
	{
		final ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
		if (id == mCopyIn)
		{
			Toast toast = Toast.makeText(getContext(), "Input Copied", 2000);
			toast.show();

			clipboard.setText(mInput);

			return true;
		}
		else if (id == mCopyOut)
		{
			Toast toast = Toast.makeText(getContext(), "Output Copied", 2000);
			toast.show();

			clipboard.setText(mOutput);
			return true;
		}
		else if (id == mViewInfo)
		{
			AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
			alertDialog.setTitle("Info");
			alertDialog.setMessage("Input: " + mInput + "\nOutput: " + mOutput + "\nRPN: " + mRPN + "\nTime: " + mTime);
			alertDialog.show();
			return true;
		}
		else if (id == mDelete)
		{
			mLayout.removeView(this);
			return true;
		}
		else
		{
			return false;
		}
	}
}
