package com.ic.mathulator;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FunctionCategoryListActivity extends ListActivity
{

	private ItemsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		FunctionCategoryItem toAdapt[] =
		{ new FunctionCategoryItem("Number Manipulation", getResources().getDrawable(R.drawable.number_manipulation)), new FunctionCategoryItem("Statistics", getResources().getDrawable(R.drawable.statistics)), new FunctionCategoryItem("Calculus", getResources().getDrawable(R.drawable.calculus)), new FunctionCategoryItem("Series", getResources().getDrawable(R.drawable.series)), new FunctionCategoryItem("Mathulator", getResources().getDrawable(R.drawable.mathulator)), new FunctionCategoryItem("Trigonometry", getResources().getDrawable(R.drawable.trig)), new FunctionCategoryItem("General Functions", getResources().getDrawable(R.drawable.gen)) };
		this.adapter = new ItemsAdapter(this, R.layout.category_list_item_layout, toAdapt);
		setListAdapter(this.adapter);
	}

	private class ItemsAdapter extends ArrayAdapter<FunctionCategoryItem>
	{

		private FunctionCategoryItem[] items;

		public ItemsAdapter(Context context, int textViewResourceId, FunctionCategoryItem[] items)
		{
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v = convertView;
			if (v == null)
			{
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.category_list_item_layout, null);
			}

			FunctionCategoryItem item = items[position];
			if (item != null)
			{
				ImageView image = (ImageView) v.findViewById(R.id.image);
				if (image != null)
				{
					image.setImageDrawable(item.image);
				}
				TextView text = (TextView) v.findViewById(R.id.text);
				if (text != null)
				{
					text.setText(item.name);
				}
			}

			return v;
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id)
	{
		Intent function_menu = new Intent(getBaseContext(), FunctionListActivity.class);
		function_menu.putExtra("Name", adapter.getItem(position).name);
		startActivityForResult(function_menu, 7);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null)
		{
			if (requestCode == 7)
			{
				setResult(Activity.RESULT_OK, data);
				finish();
			}
		}

	}
}