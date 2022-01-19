

package com.keeper.fragment.device;

import java.util.ArrayList;
import java.util.List;

import com.keeper.R;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class DeviceAddNameFragment extends Fragment {

	private GridView gridView;

	public static DeviceAddNameFragment newInstance() {
		DeviceAddNameFragment fragment = new DeviceAddNameFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_device_add_name, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		gridView = (GridView) view.findViewById(R.id.gridView1);
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		arrayList.add(R.drawable.device_icon);
		arrayList.add(R.drawable.device_icon);
		arrayList.add(R.drawable.device_icon);
		arrayList.add(R.drawable.device_icon);
		arrayList.add(R.drawable.device_icon);
		arrayList.add(R.drawable.device_icon);
		gridView.setAdapter(new ImageGridAdapter(getActivity(), arrayList));
	}

	public static class ImageGridAdapter extends ArrayAdapter<Integer> {

		public ImageGridAdapter(Context context, List<Integer> objects) {
			super(context, R.layout.item_device_icon, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getContext(), R.layout.item_device_icon, null);
			}
			ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
			imageView.setImageResource(getItem(position));
			return convertView;
		}
	}
}
