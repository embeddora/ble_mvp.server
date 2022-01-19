

package com.keeper.fragment.device;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.keeper.R;

public class DeviceAddFragment extends Fragment implements OnClickListener {

	public static DeviceAddFragment newInstance() {
		DeviceAddFragment fragment = new DeviceAddFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_device_add, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		view.findViewById(R.id.buttonName).setOnClickListener(this);
		view.findViewById(R.id.buttonSensitivity).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.buttonBluetooth:
				break;
			case R.id.buttonName:
				FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
				Fragment fragment = DeviceAddNameFragment.newInstance();
				fragmentTransaction.replace(android.R.id.content, fragment, DeviceAddNameFragment.class.getSimpleName());
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				break;
			case R.id.buttonSensitivity:
				fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
				fragment = DeviceAddSensitivityFragment.newInstance();
				fragmentTransaction.replace(android.R.id.content, fragment, DeviceAddSensitivityFragment.class.getSimpleName());
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				break;
			case R.id.buttonActivation:
				break;
			default:
				break;
		}
	};
}
