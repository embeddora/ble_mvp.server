
package com.keeper.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keeper.R;

public abstract class BaseDialog extends DialogFragment {

	private TextView title;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.Dialog);
		dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window);
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_base, null, false);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		view.setLayoutParams(layoutParams);
		title = (TextView) view.findViewById(R.id.section_label);
		return view;
	}

	public void setBaseView(int layoutResId) {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,
				1);
		((LinearLayout) getView()).addView(View.inflate(getActivity(), layoutResId, null), layoutParams);
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}

	public void setTitle(int resId) {
		title.setText(resId);
	}
}
