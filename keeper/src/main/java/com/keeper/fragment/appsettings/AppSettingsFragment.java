
package com.keeper.fragment.appsettings;

import com.keeper.R;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class AppSettingsFragment extends Fragment implements OnClickListener {

	public static AppSettingsFragment newInstance() {
		AppSettingsFragment fragment = new AppSettingsFragment();
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_settings, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		view.findViewById(R.id.buttonSettingsAbout).setOnClickListener(this);
		view.findViewById(R.id.buttonSettingsDeviceSearch).setOnClickListener(this);
		view.findViewById(R.id.buttonSettingsFeedback).setOnClickListener(this);
		view.findViewById(R.id.buttonSettingsPin).setOnClickListener(this);
		view.findViewById(R.id.buttonSettingsRestore).setOnClickListener(this);
		view.findViewById(R.id.buttonSettingsSound).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonSettingsSound:
			FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
			fragmentTransaction.replace(android.R.id.content, SoundFragment.newInstance(), SoundFragment.class.getSimpleName());
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
			break;
		case R.id.buttonSettingsDeviceSearch:
			fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
			fragmentTransaction.replace(android.R.id.content, SearchSoundFragment.newInstance(), SearchSoundFragment.class.getSimpleName());
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
			break;
		case R.id.buttonSettingsPin:
			break;
		case R.id.buttonSettingsFeedback:
			break;
		case R.id.buttonSettingsAbout:
			break;
		case R.id.buttonSettingsRestore:
			break;
		default:
			break;
		}
	};
}
