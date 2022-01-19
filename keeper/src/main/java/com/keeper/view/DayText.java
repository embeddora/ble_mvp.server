

package com.keeper.view;

import com.keeper.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class DayText extends TextView
{
	public boolean isSelected;

	public DayText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		update();
	}

	public void toggle()
	{
		isSelected = !isSelected;
		update();
	}

	public void update()
	{
		setBackgroundResource(isSelected ? R.color.pressed_color : android.R.color.transparent);
	}
}
