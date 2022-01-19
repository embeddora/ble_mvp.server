

package com.keeper.fragment.appsettings;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.keeper.R;

public class SoundFragment extends Fragment implements OnClickListener {

	public static SoundFragment newInstance() {
		SoundFragment fragment = new SoundFragment();
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_settings_sound, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		view.findViewById(R.id.buttonSound).setOnClickListener(this);
		view.findViewById(R.id.buttonVibration).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.buttonSound:
		case R.id.buttonVibration:
			((CheckedTextView) v).toggle();
			break;

		default:
			break;
		}
	};
}
