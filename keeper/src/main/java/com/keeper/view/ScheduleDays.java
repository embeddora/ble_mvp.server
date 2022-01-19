

package com.keeper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.keeper.R;

public class ScheduleDays extends LinearLayout implements OnClickListener
{

	public ScheduleDays(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.dialog_schedule_day, this, true);
		initChild();
	}

	private void initChild()
	{
		for (int index = 0, size = getChildCount(); index < size; index++)
		{
			getChildAt(index).setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v)
	{
		for (int index = 0, size = getChildCount(); index < size; index++)
		{
			if (v.equals(getChildAt(index)) && getChildAt(index) instanceof DayText)
			{
				DayText textView = (DayText) getChildAt(index);
				textView.toggle();
			}
		}
	}
}
