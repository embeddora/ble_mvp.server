
package com.keeper.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.keeper.fragment.SearchFragment;

public class SearchActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			Fragment fragment = SearchFragment.newInstance();
			fragmentTransaction.add(android.R.id.content, fragment, SearchFragment.class.getSimpleName()).commit();
		}
	}
}
