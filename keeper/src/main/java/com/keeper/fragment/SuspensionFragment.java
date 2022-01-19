

package com.keeper.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.keeper.R;
import com.keeper.dialog.ScheduleDialog;
import com.keeper.dialog.TimeDialog;
import com.keeper.dialog.TimerDialog;

public class SuspensionFragment extends Fragment implements OnClickListener {

	public static SuspensionFragment newInstance() {
		SuspensionFragment fragment = new SuspensionFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_suspension, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		view.findViewById(R.id.buttonPause).setOnClickListener(this);
		view.findViewById(R.id.buttonTimer).setOnClickListener(this);
		view.findViewById(R.id.buttonTime).setOnClickListener(this);
		view.findViewById(R.id.buttonSchedule).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonPause:
			Toast.makeText(getActivity(), "buttonPause", Toast.LENGTH_SHORT).show();
			break;
		case R.id.buttonTimer:
			TimerDialog timerDialog = new TimerDialog();
			timerDialog.show(getFragmentManager(), null);
			break;
		case R.id.buttonTime:
			TimeDialog timeDialog = new TimeDialog();
			timeDialog.show(getFragmentManager(), null);
			break;
		case R.id.buttonSchedule:
			ScheduleDialog scheduleDialog = new ScheduleDialog();
			scheduleDialog.show(getFragmentManager(), null);
			break;
		}
	}
}
