package com.ic.mathulator;

import java.io.File;
import com.ic.libICMath.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ViewFlipper;
import android.widget.ZoomControls;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;


public class main extends Activity
{
	DecimalFormat places;
	LinearLayout holder_main_layout_unfinal;
	main me;
	GraphView graphholder;
	GoogleAnalyticsTracker ana;
	SharedPreferences seting;
	LinearLayout selectionLayout;
	Button pointSelectionButton;
	WindowOptionsDialog winOps;
	MultiAutoCompleteTextView input_text;
	Vector<CalculationView> ans_res;
	int MATHUlATOR_CALCULATOR = 1,
			MATHULATOR_2D_GRAPH = 2;
	int currentView = MATHUlATOR_CALCULATOR;
	MathulatorSettings settings;
	ICCalculatorBase calc;

	ImageButton graph_adder;
	EditText name_adder;
	EditText expression_adder;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
		{
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		settings = new MathulatorSettings(this);
		calc = new ICCalculatorBase(settings);
		boolean hasMathKeyboard = false;
		try
		{
			this.getPackageManager().getPackageInfo("com.andymc.mathkeyboard", 0);
			hasMathKeyboard = true;
		}
		catch (NameNotFoundException e)
		{
		}
		try
		{
			this.getPackageManager().getPackageInfo("net.schwiz.wolfram.full", 0);
			hasMathKeyboard = true;
		}
		catch (NameNotFoundException e)
		{
		}
		try
		{
			this.getPackageManager().getPackageInfo("will.droid.mathkeyboard", 0);
			hasMathKeyboard = true;
		}
		catch (NameNotFoundException e)
		{
		}
		try
		{
			this.getPackageManager().getPackageInfo("com.appwizzle.android", 0);
			hasMathKeyboard = true;
		}
		catch (NameNotFoundException e)
		{
		}

		if (!hasMathKeyboard)
		{
			final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Math Keyboard");
			alertDialog.setMessage("You do not seem to have a math keyboard installed. This app is alot easier to use with a Math Keyboard. We suggest Schwiz's Math Keyboard but any is good");
			alertDialog.setButton("Ok", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					alertDialog.dismiss();
				}

			});
			alertDialog.setButton2("Get The Suggested", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("market://details?id=net.schwiz.wolfram.full"));
					startActivity(intent);
				}

			});
			alertDialog.setButton3("Find Me one", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("market://search?q=Math Keyboard"));
					startActivity(intent);
				}

			});
			alertDialog.show();
		}

		seting = PreferenceManager.getDefaultSharedPreferences(this);
		if (seting.getBoolean("use_analytics", true))
		{
			ana = GoogleAnalyticsTracker.getInstance();
			ana.startNewSession("UA-15921819-2", 30, this);

			ana.trackPageView("/Mathulator");
			ana.setAnonymizeIp(seting.getBoolean("ip", true));
		}
		me = this;

		places = new DecimalFormat();
		places.setMaximumFractionDigits(12);
		places.setMinimumFractionDigits(0);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		selectionLayout = new LinearLayout(this);
		ans_res = new Vector<CalculationView>();

		final LinearLayout holder_main_layout = (LinearLayout) findViewById(R.id.linearLayout2);
		holder_main_layout_unfinal = holder_main_layout;

		final ViewFlipper flip = (ViewFlipper) findViewById(R.id.viewFlipper1);

		final Button button = (Button) findViewById(R.id.button1);

		final Button calc_button = (Button) findViewById(R.id.Calc_button);
		final Button graph_button = (Button) findViewById(R.id.Graph_button);

		final ScrollView scroller = (ScrollView) findViewById(R.id.scrollView1);
		final main self = this;
		final ZoomControls graph_zoom = (ZoomControls) findViewById(R.id.graphZoomer);

		final SlidingDrawer main_slide = (SlidingDrawer) findViewById(R.id.SlidingDrawer);
		final InputMethodManager hideshow = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		RelativeLayout calc_rel = (RelativeLayout) findViewById(R.id.rellay_graph);

		flip.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right));
		flip.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left));

		final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View graphControls = inflater.inflate(R.layout.graphcontrols, null);
		LinearLayout graph_control_layout = (LinearLayout) findViewById(R.id.calc_contentLayout);
		graph_control_layout.addView(graphControls);
		final LinearLayout graph_functions_go_here = (LinearLayout) findViewById(R.id.functionholder);
		expression_adder = (EditText) findViewById(R.id.Expression);
		name_adder = (EditText) findViewById(R.id.FunctionName);
		input_text = (MultiAutoCompleteTextView) findViewById(R.id.editText1);
		graph_adder = (ImageButton) findViewById(R.id.addBut);
		graph_adder.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				boolean taken = false;
				int search;
					if (calc.functionExists(name_adder.getText().toString()))
					{
						taken = true;
					}
				if (!taken)
				{
					boolean error = calc.createFunction(name_adder.getText().toString(), expression_adder.getText().toString(), "x");

					if (!error)
					{

						for (search = 0; calc.functions.size() > search; search++)
						{
							if (calc.functions.get(search).name.equals(name_adder.getText().toString()))
							{
								calc.graphingFunctions.add(calc.functions.get(search));
								GraphExpressionEntry expres = new GraphExpressionEntry(self, calc, calc.functions.get(search), graph_functions_go_here,graphholder);
								expres.name_view.setText(name_adder.getText().toString() + "(x)=");
								expres.expression_view.setText(expression_adder.getText().toString());
								graph_functions_go_here.addView(expres);
								if (seting.getBoolean("use_analytics", true))
								{
									ana.trackEvent("Graph View", "Add Function", "Sucess", 0);
								}
								name_adder.setText("");
								expression_adder.setText("");
								graphholder.invalidateBuffer(true);
							}
						}
					}

					else
					{

						final AlertDialog.Builder function_error = new AlertDialog.Builder(me);
						if (seting.getBoolean("use_analytics", true))
						{
							function_error.setMessage("There was an error with that function:" + calc.getErrorString());
						}
						ana.trackEvent("Graph View", "Add Function", "Failed", calc.getError());
						function_error.setIcon(R.drawable.x);
						function_error.setTitle("ERROR!");
						function_error.setNeutralButton("Ok", new DialogInterface.OnClickListener()
						{

							@Override
							public void onClick(DialogInterface dialog, int which)
							{

							}
						});
						function_error.show();
					}
				}
				else
				{

					final AlertDialog.Builder function_error = new AlertDialog.Builder(me);
					function_error.setMessage("That function already exists");
					function_error.setIcon(R.drawable.x);
					function_error.setTitle("ERROR!");
					function_error.setNeutralButton("Ok", new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which)
						{

						}
					});
					function_error.show();
				}
			}

		});
		input_text.setText("");

		Handler zoom_controls = new Handler()
		{
			public void handleMessage(Message msg)
			{
				switch (msg.what)
				{
				case 0:
					this.removeMessages(1);
					Message hide = Message.obtain();
					hide.what = 1;
					hide.setTarget(this);
					this.sendMessageDelayed(hide, 2500);
					graph_zoom.setVisibility(0);
					break;

				case 1:
					Animation fadeout = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.fade_out);
					fadeout.setAnimationListener(new AnimationListener()
					{
						@Override
						public void onAnimationEnd(Animation animation)
						{
							graph_zoom.setVisibility(4);
						}

						@Override
						public void onAnimationRepeat(Animation animation)
						{
						}

						@Override
						public void onAnimationStart(Animation animation)
						{
						}
					});

					graph_zoom.startAnimation(fadeout);
					break;

				}
			}
		};
		final PointSelector graph_point_select = new PointSelector(this);

		final GraphView graphview = new GraphView(this, calc);
		winOps = new WindowOptionsDialog(this, graphview);
		final TextView valueExplain = new TextView(this);
		valueExplain.setText("X=");
		selectionLayout.addView(valueExplain);
		LinearLayout.LayoutParams confirmParams = new LinearLayout.LayoutParams(-1, -2);
		confirmParams.weight = 3;
		graph_point_select.graph = graphview;
		graphholder = graphview;
		calc_rel.addView(graphview, 0);
		selectionLayout.addView(graph_point_select);
		selectionLayout.setBackgroundColor(Color.BLACK);
		LinearLayout.LayoutParams pointParams = new LinearLayout.LayoutParams(-1, -2);
		pointParams.weight = 1;
		graph_point_select.setLayoutParams(pointParams);
		selectionLayout.setPadding(0, 0, 25, 15);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-1, -2);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		selectionLayout.setLayoutParams(params);
		calc_rel.addView(selectionLayout);

		pointSelectionButton = new Button(this);
		pointSelectionButton.setText("Select");
		pointSelectionButton.setVisibility(View.GONE);
		selectionLayout.addView(pointSelectionButton);

		name_adder.setImeOptions(EditorInfo.IME_ACTION_NEXT);
		name_adder.setOnEditorActionListener(new OnEditorActionListener()
		{
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_ACTION_NEXT)
				{
					expression_adder.requestFocus();
					return true;
				}
				return false;
			}
		});

		expression_adder.setImeOptions(EditorInfo.IME_ACTION_GO);
		expression_adder.setOnEditorActionListener(new OnEditorActionListener()
		{
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_ACTION_GO)
				{
					graph_adder.performClick();
					name_adder.requestFocus();
					return true;
				}
				return false;
			}
		});

		pointSelectionButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
			//	if (graphview.touchmode == graphview.TRACE)
				//{
				//	graphholder.touchmode = 0;
				//	graphholder.invalidate();
				//	pointSelectionButton.setText("Select");
				//	selectionLayout.setVisibility(4);
			//	}
			//	if (graphview.touchmode == graphview.DERIVE)
				{
					double X = Double.valueOf(graph_point_select.getText().toString());
			//		double Y = calc.graphingFunctions.get(graphview.to_trace).eval(X);
			//		double slope = ICMath.derive(calc.graphingFunctions.get(graphview.to_trace), X);

				//	String name = "d" + calc.graphingFunctions.get(graphview.to_trace).name;
				//	while (calc.functionExists(name))
					{
				//		name = "d" + name;
					}

					String plusMinusX = "-";
					if (X < 0)
					{
						plusMinusX = "+";
					}
					String plusMinusY = "+";
				//	if (Y < 0)
				//	{
						plusMinusY = "-";
				//	}

				//	name_adder.setText(name);
				//	expression_adder.setText(String.valueOf(slope) + "(x" + plusMinusX + Double.valueOf(Math.abs(X)) + ")" + plusMinusY + Double.valueOf(Math.abs(Y)));
					graph_adder.performClick();
				//	calc.setVariable("last_derivative", slope);

					AlertDialog inform = new AlertDialog.Builder(me).create();
					inform.setTitle("Derivative Info");
				//	inform.setMessage(calc.graphingFunctions.get(graphview.to_trace).name + "\'(" + String.valueOf(X) + ")=" + String.valueOf(slope) + "\n\n It has been saved in the variable \"last_derivative\" and The tangent line has been added to the graph");
					inform.setButton("Ok", new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
						}
					});
					inform.show();

			//		graphholder.touchmode = 0;
					graphholder.invalidate();
					selectionLayout.setVisibility(4);
					pointSelectionButton.setVisibility(4);
			//	}
			//	if (graphview.touchmode == graphview.INTEGRATE)
				//{
				//	if (graphview.integral_mode == graphholder.INTEGRAL_GETPOINT_1)
				//	{
				//		graphview.integral_point_1 = Double.valueOf(graph_point_select.getText().toString());
				//		graphview.integral_mode = graphholder.INTEGRAL_GETPOINT_2;
					//}
					//else if (graphview.integral_mode == graphholder.INTEGRAL_GETPOINT_2)
				//	{
				//		graphview.integral_point_2 = Double.valueOf(graph_point_select.getText().toString());
				//		graphview.integral_mode = graphholder.INTEGRAL_DRAW;
						//double area = ICMath.integrate(calc.graphingFunctions.get(graphview.to_trace), graphview.integral_point_1, graphview.integral_point_2);
						//AlertDialog inform = new AlertDialog.Builder(me).create();
						inform.setTitle("Integral Info");
						//inform.setMessage("The integral of " + calc.graphingFunctions.get(graphview.to_trace).name + "(x) from " + String.valueOf(graphholder.integral_point_1) + " to " + String.valueOf(graphholder.integral_point_2) + " was evaluated to be: " + String.valueOf(area) + "\n\n and has been store in the variable last_integral");
						inform.setButton("Ok", new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int which)
							{
								dialog.dismiss();
							}
						});
						inform.show();
						inform.setOnDismissListener(new OnDismissListener()
						{

							@Override
							public void onDismiss(DialogInterface paramDialogInterface)
							{
								calc.invalidate();
							}

						});

						//calc.setVariable("last_integral", area);
						pointSelectionButton.setText("Exit");
						graph_point_select.setVisibility(View.GONE);

				//	}
				//	else if (graphview.integral_mode == graphholder.INTEGRAL_DRAW)
				//	{
						pointSelectionButton.setText("Select");
						graph_point_select.setVisibility(View.VISIBLE);
						selectionLayout.setVisibility(View.INVISIBLE);
				//		graphview.touchmode = graphview.NORMAL;

				//	}
				}
			}
		});

		selectionLayout.setVisibility(4);
		graph_zoom.setOnZoomInClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
	//			graphview.show_zoom();
	//			graphview.scale_base++;
	//			if (graphview.scale_base == 0)
	//			{
	//				graphview.scale_base++;
	//			}
	//			graphview.gridspace--;
	//			if (graphview.gridspace < graphview.min_gridspace || graphview.gridspace > graphview.max_gridspace)
	//			{
	//				graphview.gridspace = 35;
	//			}

	//			if (graphview.scale_base == 0)
	//			{
	//				graphview.scale = 1;
	//			}
		//		if (graphview.scale_base < 0)
	//			{
		//			graphview.scale = -1 / ((double) graphview.scale_base);
	//		}
	//			graphview.invalidate();
			}
		});
		graph_zoom.setOnZoomOutClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
		//		graphview.show_zoom();
//
		//		graphview.scale_base--;
		//		if (graphview.scale_base == 0)
		//		{
		//			graphview.scale_base--;
		//		}
		//		graphview.gridspace++;
		//		if (graphview.gridspace < graphview.min_gridspace || graphview.gridspace > graphview.max_gridspace)
		//		{
		//			graphview.gridspace = 35;
		//		}
//
		//		if (graphview.scale_base == 0)
		//		{
		//			graphview.scale = 1;
		//		}
		//		if (graphview.scale_base < 0)
		//		{
		//			graphview.scale = -1 / ((double) graphview.scale_base);
		//		}
		//		graphview.invalidate();
			}
		});

		calc_button.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				if (seting.getBoolean("use_analytics", true))
				{
					ana.trackPageView("/Graph");
				}
				main_slide.close();
				flip.setDisplayedChild(0);
				hideshow.showSoftInput(input_text, 0);
				currentView = MATHUlATOR_CALCULATOR;

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				{
					invalidateOptionsMenu();
				}
			}
		});
		graph_button.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				main_slide.close();
				flip.setDisplayedChild(1);
				if (seting.getBoolean("use_analytics", true))
				{
					ana.trackPageView("/Mathulator");
				}
				hideshow.hideSoftInputFromWindow(input_text.getWindowToken(), 0);
				currentView = MATHULATOR_2D_GRAPH;

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				{
					invalidateOptionsMenu();
				}
			}
		});

		button.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				final CalculationView calculationView = new CalculationView(self, seting, holder_main_layout);
				me.registerForContextMenu(calculationView);
				calculationView.setInput(input_text.getText().toString());
				ans_res.add(calculationView);
				holder_main_layout.addView(calculationView);

				String out = calc.stringEvalExpression(input_text.getText().toString());

				if (calc.calcError())
				{
					calculationView.setOutput("Could not Mathulate: " + calc.getErrorString());
					if (seting.getBoolean("use_analytics", true))
					{
						ana.trackEvent("Calculator", "Eval", "Failed", calc.getError());
					}
				}
				else
				{
					calculationView.setOutput(out);
					if (seting.getBoolean("use_analytics", true))
					{
						ana.trackEvent("Calculator", "Eval", "Success", calc.getEvalTime());
					}
				}
				calculationView.setRPN(calc.toString());
				calculationView.setTime(String.valueOf(calc.getEvalTime()));

				scroller.post(new Runnable()
				{
					@Override
					public void run()
					{
						scroller.fullScroll(View.FOCUS_DOWN);
					}
				});

				input_text.setText("");
			}
		});

		input_text.setTokenizer(new InputTokenizer(this));
		input_text.setImeOptions(EditorInfo.IME_ACTION_GO);
		input_text.setOnEditorActionListener(new OnEditorActionListener()
		{
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if (actionId == EditorInfo.IME_ACTION_GO)
				{
					button.performClick();
					return true;
				}
				return false;
			}
		});
		input_text.setImeActionLabel("Evaluate", EditorInfo.IME_ACTION_GO);
		input_text.setOnKeyListener(new OnKeyListener()
		{

			@Override
			public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
			{
				if (paramKeyEvent.getAction() == KeyEvent.ACTION_DOWN && paramKeyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)
				{
					button.performClick();
					return true;

				}
				return false;
			}
		});
		input_text.setThreshold(1);

		input_text.addTextChangedListener(new TextWatcher()
		{
			String text;
			int i;
			char temp_char;
			Editable edit;

			@Override
			public void afterTextChanged(Editable s)
			{

				text = input_text.getText().toString();
				if (text.length() > 0)
				{
					for (i = 0; i < text.length(); i++)
					{
						temp_char = text.charAt(i);
						if (!Character.isWhitespace(temp_char))
						{
							if (temp_char == '+' || temp_char == '*' || temp_char == '/' || temp_char == '−' || temp_char == '-' || temp_char == '%' || temp_char == '^')
							{
								text = "Ans" + text;
								input_text.setText(text);
								edit = input_text.getText();
								Selection.setSelection(edit, text.length());
							}
							else
							{
								break;
							}
						}
					}
					if (text.charAt(text.length() - 1) == '(')
					{
						input_text.append(")");
						if (!PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean("smart_backspace", false))
						{
							input_text.setText(input_text.getText().toString() + "");
						}
						edit = input_text.getText();
						Selection.setSelection(edit, text.length() - 1);
					}
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}
		});

		NewInThisVersionDialog newVersion = new NewInThisVersionDialog(this);
		if (!seting.getString("newInThisVersionShown", "").equals(getResources().getString(R.string.version)))
		{
			Editor edit = seting.edit();
			edit.putString("newInThisVersionShown", getResources().getString(R.string.version));
			edit.commit();
			newVersion.show();
		}
		calc.setListener(new ICCalculatorBase.VariableFunctionChangeListener()
		{
			@Override
			public void onChange(HashMap<String, Double> variables, ArrayList<ICFunction> functions)
			{
				ArrayList<String> toAdapt = new ArrayList<String>();
				int index;
				
					Iterator<String> it=variables.keySet().iterator();
					while(it.hasNext())
					{
						toAdapt.add(it.next());
					}
				for (index = 0; index < functions.size(); index++)
				{
					toAdapt.add(functions.get(index).name + "(");
				}
				for (index = 0; index < ICMath.builtInFunctions.length; index++)
				{
					toAdapt.add(ICMath.builtInFunctions[index] + "(");
				}
				input_text.setAdapter(new ArrayAdapter<String>(me, android.R.layout.simple_dropdown_item_1line, toAdapt));
				
			}

			@Override
			public void onFunctionChange(ArrayList<ICFunction> arg0)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onVariablesChange(HashMap<String, Double> arg0)
			{
				// TODO Auto-generated method stub
				
			}
		});

		loadData();
		calc.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();

		if (currentView == MATHUlATOR_CALCULATOR)
		{
			inflater.inflate(R.menu.calculator_menu, menu);
		}
		else if (currentView == MATHULATOR_2D_GRAPH)
		{
			inflater.inflate(R.menu.twodgraph_menu, menu);
		}
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.clear();
		MenuInflater inflater = getMenuInflater();

		if (currentView == MATHUlATOR_CALCULATOR)
		{
			inflater.inflate(R.menu.calculator_menu, menu);
		}
		else if (currentView == MATHULATOR_2D_GRAPH)
		{
			inflater.inflate(R.menu.twodgraph_menu, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.menu_clear:
			holder_main_layout_unfinal.removeAllViews();
			break;
		case R.id.menu_function_menu:
			Intent function_menu = new Intent(getBaseContext(), FunctionCategoryListActivity.class);
			startActivityForResult(function_menu, 5);
			break;
		case R.id.settings:
			Intent settingsActivity = new Intent(getBaseContext(), Preferences.class);
			if (seting.getBoolean("use_analytics", true))
			{
				ana.trackPageView("/Settings");
			}
			startActivity(settingsActivity);
			break;
		case R.id.trace:
	//		if (graphholder.touchmode != 1)
	//		{
		//		graphholder.touchmode = 1;
		//		selectionLayout.setVisibility(0);
		//		pointSelectionButton.setText("Exit");
		//		pointSelectionButton.setVisibility(View.VISIBLE);
		//		new GraphFunctionSelectDialog(this, graphholder, true).show();
		//	}
			break;
		case R.id.derive:
			//graphholder.touchmode = graphholder.DERIVE;
			selectionLayout.setVisibility(0);
			pointSelectionButton.setVisibility(0);
			new GraphFunctionSelectDialog(this, graphholder, false).show();
			break;
		case R.id.integrate:
			//graphholder.touchmode = graphholder.INTEGRATE;
			//graphholder.integral_mode = graphholder.INTEGRAL_GETPOINT_1;
			selectionLayout.setVisibility(0);
			pointSelectionButton.setVisibility(0);
			new GraphFunctionSelectDialog(this, graphholder, false).show();
			break;
		case R.id.survey:
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse("http://www.surveymonkey.com/s/W9G3HLN"));
			startActivity(i);
			break;
		case R.id.windowoptions:
			winOps.show();
		}
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null)
		{
			if (requestCode == 5)
			{
				input_text.getEditableText().insert(input_text.getSelectionStart(), data.getStringExtra("return") + "()");
				input_text.setSelection(input_text.getSelectionStart() - 1);
			}
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item)
	{
		for (int index = 0; index < ans_res.size(); index++)
		{
			if (ans_res.get(index).processContextSelection(item))
			{
				return true;
			}
		}
		return false;

	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		saveData();
	}

	private void saveData()
	{
		Editor dump = seting.edit();
		int functionIndex;
		for (functionIndex = 0; functionIndex < calc.functions.size(); functionIndex++)
		{
			dump.putString("function_save_" + String.valueOf(functionIndex), calc.functions.get(functionIndex).name + "(" + calc.functions.get(functionIndex).basis + "):=" + calc.functions.get(functionIndex).name);
		}
		dump.putInt("function count", functionIndex + 1);

		int graphedIndex;
		String graphed = "";
		for (graphedIndex = 0; graphedIndex < calc.graphingFunctions.size(); graphedIndex++)
		{
			graphed += calc.graphingFunctions.get(graphedIndex).name;
			if (graphedIndex != calc.graphingFunctions.size() - 1)
			{
				graphed += ",";
			}
		}
		dump.putString("graphed", graphed);

		int totalCalcs = holder_main_layout_unfinal.getChildCount();
		int totalRealCalcs = 0;
		for (int currentCalc = 0; currentCalc < totalCalcs; currentCalc++)
		{
			View temp;
			temp = holder_main_layout_unfinal.getChildAt(currentCalc);
			try
			{
				CalculationView tempCalc = (CalculationView) temp;
				dump.putString("calculation_" + String.valueOf(totalRealCalcs), tempCalc.mInput + "|" + tempCalc.mOutput + "," + tempCalc.mRPN + "," + tempCalc.mTime + ",");
				totalRealCalcs++;
			}
			catch (Exception e)
			{
			}
		}
		dump.putInt("calculations", totalRealCalcs);

		String variableReps = "";
		String variableValues = "";
		String rep;
		Iterator<String> it=calc.variables.keySet().iterator();
		while(it.hasNext())
		{
			rep=it.next();
			variableReps += rep;
			variableValues += calc.variables.get(rep);
				variableReps += ",";
				variableValues += ",";
		}
		
		dump.putString("variable_values", variableValues);
		dump.putString("variable_reps", variableReps);
		dump.commit();
		saveSharedPreferences();
	}

	private void loadData()
	{
		Editor grab = seting.edit();

		try
		{
			String variable_reps[] = seting.getString("variable_reps", "").split(",");
			String variable_values_temp[] = seting.getString("variable_values", "").split(",");

			if (variable_reps[0] != "")
			{
				for (int index = 0; index < variable_reps.length; index++)
				{
					calc.setVariable(variable_reps[index], Double.valueOf(variable_values_temp[index]));
				}
				grab.remove("variable_reps");
				grab.remove("variable_values");
			}

			int numberOfFunctions = seting.getInt("function count", 0);
			String graphedFunctions[] = seting.getString("graphed", "").split(",");

			for (int index = 0; index < numberOfFunctions; index++)
			{
				boolean handled = false;
				String function = seting.getString("function_save_" + String.valueOf(index), "");
				String name = function.split("\\(")[0];

				if (name.equals(""))
				{
					grab.remove("function_save_" + String.valueOf(index));
					grab.remove("function count");
					break;

				}
				for (int index2 = 0; index2 < graphedFunctions.length; index2++)
				{
					if (graphedFunctions[index2].equals(name))
					{
						String functionExpr = function.split("\\:=")[1];
						name_adder.setText(name);
						expression_adder.setText(functionExpr);

						graph_adder.performClick();

						handled = true;
					}
				}

				if (!handled)
				{
					calc.evalExpression(function);
				}
				grab.remove("function_save_" + String.valueOf(index));

			}

			int calcCount = seting.getInt("calculations", 0);

			for (int index = 0; index < calcCount; index++)
			{
				CalculationView temp = new CalculationView(this, seting, holder_main_layout_unfinal);
				String calcInfo = seting.getString("calculation_" + String.valueOf(index), "");
				if (calcInfo.equals(""))
				{
					grab.remove("calculations");
					grab.remove("calculation_" + String.valueOf(index));
					break;
				}
				String input = calcInfo.split("\\|")[0];
				String moreInfo[] = calcInfo.split("\\|")[1].split(",");

				temp.setInput(input);
				temp.setOutput(moreInfo[0]);
				temp.setTime(moreInfo[2]);
				temp.setRPN(moreInfo[1]);

				holder_main_layout_unfinal.addView(temp);
				grab.remove("calculation_" + String.valueOf(index));
			}

		}
		catch (Exception e)
		{
			Log.e("Mathulator-loadDataError", e.getMessage());
		}
		;

		grab.commit();

	}

	private void saveSharedPreferences()
	{
		// create some junk data to populate the shared preferences
		SharedPreferences prefs = seting;

		// BEGIN EXAMPLE
		File myPath = new File(Environment.getExternalStorageDirectory().toString());
		File myFile = new File(myPath, "MySharedPreferences");

		try
		{
			FileWriter fw = new FileWriter(myFile);
			PrintWriter pw = new PrintWriter(fw);

			Map<String, ?> prefsMap = prefs.getAll();

			for (Map.Entry<String, ?> entry : prefsMap.entrySet())
			{
				pw.println(entry.getKey() + ": " + entry.getValue().toString());
			}

			pw.close();
			fw.close();
		}
		catch (Exception e)
		{
			// what a terrible failure...
			Log.e(getClass().getName(), e.toString());
		}
	}

}