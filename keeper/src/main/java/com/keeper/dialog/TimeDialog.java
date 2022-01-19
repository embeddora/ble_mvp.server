

package com.keeper.dialog;

import net.simonvt.numberpicker.NumberPicker;

import com.keeper.R;

import android.os.Bundle;
import android.view.View;

public class TimeDialog extends BaseDialog {

	private NumberPicker hours;
	private NumberPicker minutes;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTitle("Задать время включения");
		setBaseView(R.layout.dialog_timer);

		hours = (NumberPicker) view.findViewById(R.id.numberPicker1);
		minutes = (NumberPicker) view.findViewById(R.id.numberPicker2);

		hours.setMinValue(0);
		hours.setMaxValue(23);

		minutes.setMinValue(0);
		minutes.setMaxValue(59);
	}
}
