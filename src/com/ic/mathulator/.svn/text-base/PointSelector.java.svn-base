package com.ic.mathulator;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PointSelector extends EditText
{
	boolean ignore_change = false;
	PointSelector me;
	GraphView graph;

	public PointSelector(Context context)
	{
		super(context);
		me = this;
		this.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
			{
			}

			@Override
			public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
			{
			}

			@Override
			public void afterTextChanged(Editable paramEditable)
			{
				try
				{
					if (!ignore_change)
					{
					//	graph.setPos(Double.valueOf(getText().toString()));
					//	graph.textChanged(me.getGraphValue());
					}
				}
				catch (Exception e)
				{
				}
			}

		});
	}

	double getGraphValue()
	{
		//return (Double.valueOf(me.getText().toString()) * graph.scale) + graph.xoffset + graph.xcent;
		return 0;
	}

}
