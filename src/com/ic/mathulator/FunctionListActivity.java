package com.ic.mathulator;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class FunctionListActivity extends ListActivity
{
	ListAdapter adapter;
	String Name;

	public void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		Name = getIntent().getStringExtra("Name");
		createAdapter();
		setListAdapter(adapter);
		setTitle(Name);
	}

	protected void createAdapter()
	{
		if (Name.equals("Number Manipulation"))
		{
			String testValues[] =
			{ "ciel", "floor", "abs", "isPrime", "round", "ipart", "fpart", "int", "sign" };
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testValues);
		}
		else if (Name.equals("Statistics"))
		{
			String testValues[] =
			{ "rnd", "rndn", "rndi", "rndb", "npr", "ncr", "seed" };
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testValues);
		}
		else if (Name.equals("Calculus"))
		{
			String testValues[] =
			{ "derive", "integrate" };
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testValues);
		}
		else if (Name.equals("Series"))
		{
			String testValues[] =
			{ "sum", "product" };
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testValues);
		}
		else if (Name.equals("Mathulator"))
		{
			String testValues[] =
			{ "clearAll", "delete" };
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testValues);
		}
		else if (Name.equals("Trigonometry"))
		{
			String testValues[] =
			{ "cos", "sin", "tan", "acos", "asin", "atan", "csc", "sec", "cot", "acsc", "asec", "acot", "cosh", "sinh", "tanh" };
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testValues);
		}
		else if (Name.equals("General Functions"))
		{
			String testValues[] =
			{ "ln", "log" };
			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testValues);
		}
	}

	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Intent resultIntent = new Intent();
		resultIntent.putExtra("return", (String) adapter.getItem(position));
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}
