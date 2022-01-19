


package com.keeper.activity;

import android.app.ActionBar;
import android.app.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.keeper.R;

public class BaseActivity extends Activity implements OnClickListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();

		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);

		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.abc_custom);
		actionBar.getCustomView().findViewById(R.id.imageHome).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageHome:
			onBackPressed();
			break;
		}
	}
}
