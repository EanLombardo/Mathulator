package com.ic.mathulator;

import com.ic.libICMath.ICCalculatorBase;
import com.ic.libICMath.ICFunction;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GraphExpressionEntry extends LinearLayout
{
	Context context;
	TextView name_view;
	EditText expression_view;
	ImageButton delete;
	ImageButton save;
	ICFunction function;
	ICCalculatorBase calc;
	LinearLayout Parent;
	GraphExpressionEntry me;
	GraphView graph;

	public GraphExpressionEntry(Context cont, ICCalculatorBase calc2, ICFunction icFunction, LinearLayout p,final GraphView graph)
	{
		super(cont);
		context = cont;
		Parent = p;
		calc = calc2;
		function = icFunction;
		me = this;
		name_view = new TextView(context);
		this.addView(name_view);
		expression_view = new EditText(context);
		expression_view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1f));
		this.addView(expression_view);
		delete = new ImageButton(context);
		delete.setImageResource(android.R.drawable.ic_delete);
		this.graph=graph;

		delete.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				calc.graphingFunctions.remove(function);
				calc.functions.remove(function);
				graph.invalidateBuffer(true);
				Parent.removeView(me);
			}
		});
		this.addView(delete);

		save = new ImageButton(context);
		save.setImageResource(R.drawable.check);
		save.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				String temp = String.valueOf(function.name);
				function.parse(expression_view.getText().toString());
				function.name = temp;
				graph.invalidateBuffer(true);
			}
		});
		this.addView(save);
	}

}
