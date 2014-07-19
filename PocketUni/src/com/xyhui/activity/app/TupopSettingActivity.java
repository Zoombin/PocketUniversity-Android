package com.xyhui.activity.app;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xyhui.R;
import com.xyhui.widget.FLActivity;

public class TupopSettingActivity extends FLActivity implements OnClickListener {

	private Button btn_back;
	private LinearLayout layout_school;
	private LinearLayout layout_msg;
	private LinearLayout layout_forum;
	
	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_tupop_setting);
		
		btn_back = (Button) findViewById(R.id.btn_back);
		layout_school = (LinearLayout) findViewById(R.id.layout_school);
		layout_msg = (LinearLayout) findViewById(R.id.layout_msg);
		layout_forum = (LinearLayout) findViewById(R.id.layout_forum);
	}
	
	@Override
	public void bindListener() {
		btn_back.setOnClickListener(this);
		layout_school.setOnClickListener(this);
		layout_msg.setOnClickListener(this);
		layout_forum.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.layout_school:
			intent.setClass(this, SchoolListActivity.class);
			startActivity(intent);
			break;
		case R.id.layout_msg:
			
			break;
		case R.id.layout_forum:
			
			break;
		default:
			break;
		}
	}
}
