

package com.keeper.fragment.device;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.keeper.R;

public class DeviceAddSensitivityFragment extends Fragment {

	public static DeviceAddSensitivityFragment newInstance() {
		DeviceAddSensitivityFragment fragment = new DeviceAddSensitivityFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_device_add_sensitivity, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
	}
	
	
}
