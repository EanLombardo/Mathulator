package com.ic.mathulator;

import android.content.Context;
import android.widget.MultiAutoCompleteTextView.Tokenizer;

public class InputTokenizer implements Tokenizer
{
	Context context;

	InputTokenizer(Context cont)
	{
		context = cont;
	}

	@Override
	public int findTokenStart(CharSequence text, int cursor)
	{
		int i = cursor;
		while (i > 0 && is_letter(text.charAt(i - 1)))
		{
			i--;
			if (i < 0)
			{
				return 0;
			}
		}
		return i;
	}

	@Override
	public int findTokenEnd(CharSequence text, int cursor)
	{
		int i = 0;
		while (i < text.length())
		{
			if (!is_letter(text.charAt(i)))
			{
				return i;
			}
			else
			{
				i++;
			}
		}
		return i;
	}

	@Override
	public CharSequence terminateToken(CharSequence text)
	{
		return text;
	}

	private boolean is_letter(char test)
	{
		String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_ΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩαβγδεζηθικλμνξοπρςστυφχψω";
		boolean ret = false;
		int i;
		for (i = 0; i < letters.length(); i++)
		{
			if (test == letters.charAt(i))
			{
				ret = true;
			}
		}
		return ret;
	}

}
