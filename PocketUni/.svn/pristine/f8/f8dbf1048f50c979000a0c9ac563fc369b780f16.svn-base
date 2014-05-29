package com.xyhui.activity;

import android.widget.ListView;
import android.widget.TextView;

import com.xyhui.R;
import com.xyhui.widget.FLActivity;

public class CityListActivity extends FLActivity {

	private ListView city_listview;
	private TextView title;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_schoollist);

		city_listview = (ListView) findViewById(R.id.school_listview);
		title = (TextView) findViewById(R.id.textView1);
	}

	@Override
	public void ensureUi() {
		new CityList(city_listview, mActivity);
		title.setText("请选择所在城市");
	}

}
