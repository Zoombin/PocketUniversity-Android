package com.xyhui.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TabHost;

import com.xyhui.R;
import com.xyhui.utils.TableOperate;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements TabHost.OnTabChangeListener {
	private TabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initTabHost();
	}

	private void initTabHost() {
		if (mTabHost != null) {
			throw new IllegalStateException("Trying to intialize already initializd TabHost");
		}

		mTabHost = getTabHost();

		TableOperate.addTab(mTabHost, "我", R.drawable.tab_selector_index, "index", new Intent(
				this, TabMeActivity.class));
		TableOperate.addTab(mTabHost, "学校", R.drawable.tab_selector_weibo, "weibo", new Intent(
				this, TabSchoolActivity.class));
		TableOperate.addTab(mTabHost, "", R.drawable.tab_selector_app, "app", new Intent(this,
				TabFriendActivity.class));
		TableOperate.addTab(mTabHost, "PU", R.drawable.tab_selector_friend, "friend", new Intent(
				this, TabAppActivity.class));
		TableOperate.addTab(mTabHost, "更多", R.drawable.tab_selector_more, "more", new Intent(this,
				TabMoreActivity.class));

		// Fix layout for 1.5.
		if (Build.VERSION.SDK_INT < 4) {
			FrameLayout flTabContent = (FrameLayout) findViewById(android.R.id.tabcontent);
			flTabContent.setPadding(0, 0, 0, 0);
		}
	}

	@Override
	public void onTabChanged(String tabId) {

	}
}
