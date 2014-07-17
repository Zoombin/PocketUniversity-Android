package com.xyhui.activity.app;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.xyhui.R;
import com.xyhui.widget.FLActivity;

public class TupopSettingActivity extends FLActivity {

	private Button btn_back;
	private Button btn_setting;
	
	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_tupop);
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_setting = (Button) findViewById(R.id.btn_setting);
	}
}
