package com.ic.mathulator;

import android.app.Activity;

import com.ic.libICMath.ICCalculatorBase;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MathulatorSettings extends ICCalculatorBase.Settings
{
	SharedPreferences set;
	Activity p;

	public MathulatorSettings(Activity parent)
	{
		set = PreferenceManager.getDefaultSharedPreferences(parent);
		p = parent;
	}

	@Override
	public boolean getShowRPN()
	{
		boolean ret = set.getBoolean("show_rpn", false);
		return ret;
	}

	@Override
	public boolean geShowTime()
	{
		boolean ret = set.getBoolean("show_time", false);
		return ret;
	}

	@Override
	public int getAngleUnit()
	{
		return Integer.parseInt(set.getString("angle", "0"));
	}

	@Override
	public boolean getGraphingSwithcColors()
	{
		boolean ret = set.getBoolean("graphing_colors_switched", false);
		return ret;
	}

	@Override
	public boolean getGraphingShowGrid()
	{
		boolean ret = set.getBoolean("graphing_show_grid", false);
		return ret;
	}

	@Override
	public boolean getGraphingDotMode()
	{
		boolean ret = set.getBoolean("graphing_dot_mode", false);
		return ret;
	}
}
