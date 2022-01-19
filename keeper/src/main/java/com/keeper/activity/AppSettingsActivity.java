

package com.keeper.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.keeper.fragment.appsettings.AppSettingsFragment;

public class AppSettingsActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null)
		{
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			
			Fragment fragment = AppSettingsFragment.newInstance();
			
			fragmentTransaction.add(android.R.id.content, fragment, AppSettingsFragment.class.getSimpleName()).commit();
		}
	}
}
