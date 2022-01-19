

package com.keeper.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.keeper.fragment.HistoryFragment;

public class HistoryActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			Fragment fragment = HistoryFragment.newInstance();
			fragmentTransaction.add(android.R.id.content, fragment, HistoryFragment.class.getSimpleName()).commit();
		}
	}
}
