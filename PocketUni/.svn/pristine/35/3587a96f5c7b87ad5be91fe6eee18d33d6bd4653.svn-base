package com.xyhui.activity;

import android.widget.ListView;
import android.widget.TextView;

import com.xyhui.R;
import com.xyhui.widget.FLActivity;

public class SchoolListActivity extends FLActivity {

	private ListView school_listview;
	private TextView title;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_schoollist);

		school_listview = (ListView) findViewById(R.id.school_listview);
		title = (TextView) findViewById(R.id.textView1);
	}

	@Override
	public void bindListener() {

	}

	@Override
	public void ensureUi() {
		new SchoolList(school_listview, mActivity);
		title.setText("请选择所在学校");
	}

}
