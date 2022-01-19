

package com.keeper.dialog;

import net.simonvt.numberpicker.NumberPicker;

import com.keeper.R;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ScheduleDialog extends BaseDialog {

	private NumberPicker hoursStart;
	private NumberPicker minutesStart;

	private NumberPicker hoursEnd;
	private NumberPicker minutesEnd;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTitle("Задать период таймера");
		setBaseView(R.layout.dialog_schedule);

		hoursStart = (NumberPicker) view.findViewById(R.id.numberPickerHourStart);
		minutesStart = (NumberPicker) view.findViewById(R.id.numberPickerMinutesStart);

		hoursEnd = (NumberPicker) view.findViewById(R.id.numberPickerHourEnd);
		minutesEnd = (NumberPicker) view.findViewById(R.id.numberPickerMinutesEnd);

		hoursStart.setMinValue(0);
		hoursStart.setMaxValue(23);
		minutesStart.setMinValue(0);
		minutesStart.setMaxValue(59);

		hoursEnd.setMinValue(0);
		hoursEnd.setMaxValue(23);
		minutesEnd.setMinValue(0);
		minutesEnd.setMaxValue(59);

		view.findViewById(R.id.textStart).setOnClickListener(onClickListener);
		view.findViewById(R.id.textEnd).setOnClickListener(onClickListener);
	}

	private View.OnClickListener onClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			View view = null;
			boolean isShown = false;
			TextView textView = (TextView) v;
			switch (v.getId()) {
			case R.id.textStart:
				view = getView().findViewById(R.id.containerStart);
				isShown = view.isShown();
				break;
			case R.id.textEnd:
				view = getView().findViewById(R.id.containerEnd);
				isShown = view.isShown();
				break;
			}
			view.setVisibility(isShown ? View.GONE : View.VISIBLE);
			textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, isShown ? R.drawable.arrow_open: R.drawable.arrow_close, 0);
		}
	};
}
