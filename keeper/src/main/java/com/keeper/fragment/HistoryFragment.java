

package com.keeper.fragment;

import java.io.Serializable;  
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.keeper.R;
import com.keeper.model.Device;
import com.keeper.model.History;

public class HistoryFragment extends ListFragment
{
	
	private ArrayList<History> histories;

 
	public static HistoryFragment newInstance()
	{
		HistoryFragment fragment = new HistoryFragment();		
		
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return inflater.inflate(R.layout.fragment_history, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		Intent IdxIntent = getActivity().getIntent();		
		
		Bundle bundle=IdxIntent.getExtras();
		
		histories = (ArrayList<History>)bundle.getSerializable("com.keeper.device");
		
		//+++  2021-12-01 exception  historiessetAdaptergetListView().historiessetAdapter(new HistoryAdapter(  histories ));
		
		return ;
	}

	private class HistoryAdapter extends ArrayAdapter<History>
	{

		public HistoryAdapter(List<History> objects)
		{
			super(getActivity(), R.layout.item_history, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = View.inflate(getContext(), R.layout.item_history, null);
			}

			TextView deviceName = (TextView) convertView.findViewById(R.id.textDeviceName);
			TextView deviceState = (TextView) convertView.findViewById(R.id.textDeviceState);
			TextView deviceTime = (TextView) convertView.findViewById(R.id.textDeviceTime);
			ImageView deviceIcon = (ImageView) convertView.findViewById(R.id.imageView1);

			History history = getItem(position);

			deviceName.setText(history.name);
			switch (history.state)
			{
			case Device.STATE_ON:
				deviceState.setText("Включение устройства");
				deviceIcon.setImageResource(R.drawable.ico_on);
				break;
			case Device.STATE_PAUSE:
				deviceState.setText("Выключение устройства");
				deviceIcon.setImageResource(R.drawable.ico_off);
				break;
			case Device.STATE_DISCONNECTED:
				deviceState.setText("Потеря связи с устройством");
				deviceIcon.setImageResource(R.drawable.ico_disconnect);
				break;
				
			case Device.STATE_ACTIVTED:
				deviceState.setText("Активирован");
				deviceIcon.setImageResource(R.drawable.ico_on);
				break;				
			case Device.STATE_DEACTIVTED:
				deviceState.setText("Деактивирован");
				deviceIcon.setImageResource(R.drawable.ico_off);
				break;				
			case Device.STATE_SEARCHED:
				deviceState.setText("RSSI активирован");
				deviceIcon.setImageResource(R.drawable.ico_on);
				break;				
			case Device.STATE_DISAPPEARED:
				deviceState.setText("Покинул пределы досягаемости (0-50м)");
				deviceIcon.setImageResource(R.drawable.ico_off);				
				break;
			case Device.STATE_APPEARED:
				deviceState.setText("В пределах досягаемости (0-50м)");
				deviceIcon.setImageResource(R.drawable.ico_on);				
				break;
			
				
			}
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			deviceTime.setText(dateFormat.format(new Date(history.time)));

			return convertView;
		}
	}
}


