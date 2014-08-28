package com.ic.mathulator;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WindowOptionsDialog extends Dialog
{
	EditText graphCenterX;
	EditText graphCenterY;
	EditText graphGridspace;
	EditText graphScale;
	Button graphOkButton;
	Button graphCancelButton;
	GraphView g;

	public WindowOptionsDialog(Context context, GraphView graph)
	{
		super(context);
		this.setContentView(R.layout.graphwindowdialog);
		this.setTitle("Windows Settings");
		graphCenterX = (EditText) findViewById(R.id.graphCenterX);
		graphCenterY = (EditText) findViewById(R.id.graphCenterY);
		graphGridspace = (EditText) findViewById(R.id.graphGridspace);
		graphScale = (EditText) findViewById(R.id.graphScale);
		graphOkButton = (Button) findViewById(R.id.graphOkButton);
		graphCancelButton = (Button) findViewById(R.id.graphCancelButton);
		g = graph;
		graphOkButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				try
				{
					g.scale = Double.valueOf(graphScale.getText().toString());
					//g.gridspace = Integer.valueOf(graphGridspace.getText().toString());
					//g.xoffset = (int) (-Double.valueOf(graphCenterX.getText().toString()) * g.scale);
				//g.yoffset = (int) (Double.valueOf(graphCenterY.getText().toString()) * g.scale);
					g.invalidate();
					dismiss();
				}
				catch (Exception e)
				{
					Toast toast = Toast.makeText(getContext(), "Error: Window settings input, Not A valid Number", Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
		graphCancelButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				dismiss();
			}
		});
	}

	@Override
	public void onStart()
	{
		//graphCenterX.setText(String.valueOf(-g.xoffset / g.scale));
		//graphCenterY.setText(String.valueOf(g.yoffset / g.scale));
		//graphGridspace.setText(String.valueOf(g.gridspace));
		//graphScale.setText(String.valueOf(g.scale));
	}

}
