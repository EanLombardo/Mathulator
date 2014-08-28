package com.ic.mathulator;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GraphFunctionSelectDialog extends Dialog
{
	ListView list;
	GraphView g;
	GraphFunctionSelectDialog me = this;

	public GraphFunctionSelectDialog(Context context, final GraphView graph, final boolean allowAll)
	{
		super(context);
		this.setContentView(R.layout.just_a_list_view);
		this.setTitle("Select a function:");
		list = (ListView) findViewById(R.id.listView1);
		ArrayList<String> functions = new ArrayList<String>();
		if (allowAll)
		{
			functions.add("All Functions");
		}
		int index;
		//for (index = 0; index < graph.calcbase.graphingFunctions.size(); index++)
		//{
		//	functions.add(graph.calcbase.graphingFunctions.get(index).name);
		//}
		list.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, functions));
		list.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
		//		graph.to_trace = position - (allowAll ? 1 : 0);
				dismiss();
			}
		});

	}
}
