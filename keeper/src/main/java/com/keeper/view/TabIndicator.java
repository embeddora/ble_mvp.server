
package com.keeper.view;

import com.keeper.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TabIndicator extends RelativeLayout
{
	private TextView title;
	private ImageView icon;

	public TabIndicator(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		View.inflate(getContext(), R.layout.abc_tab, this);
		setGravity(Gravity.CENTER);
		setClickable(true);

		title = (TextView) findViewById(R.id.textTab);
		icon = (ImageView) findViewById(R.id.imageTab);

		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TabIndicator);
		title.setText(typedArray.getString(R.styleable.TabIndicator_titleText));
		title.setTextColor(typedArray.getColor(R.styleable.TabIndicator_titleTextColor, 0));
		title.setTextSize(typedArray.getDimensionPixelSize(R.styleable.TabIndicator_titleTextSize, 14));
		icon.setImageResource(typedArray.getResourceId(R.styleable.TabIndicator_titleIcon, 0));
		typedArray.recycle();
	}
}
