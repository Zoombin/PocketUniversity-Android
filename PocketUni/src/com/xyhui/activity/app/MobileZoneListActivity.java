package com.xyhui.activity.app;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.activity.TabAppActivity;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

public class MobileZoneListActivity extends FLActivity {
	private Button btn_back;
	private TextView navbar_TitleText;

	private PullToRefreshListView mobile_zone_listview;
	private MobileZoneList mobileZoneListView;

	private String mCityId;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_mobileapp_list);

		btn_back = (Button) findViewById(R.id.btn_back);
		mobile_zone_listview = (PullToRefreshListView) findViewById(R.id.mobileapp_listview);
		navbar_TitleText = (TextView) findViewById(R.id.navbar_TitleText);
	}

	@Override
	public void bindListener() {
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭返回
				finish();
			}
		});
	}

	@Override
	public void ensureUi() {
		mCityId = new PrefUtil().getPreference(Params.LOCAL.CITYID);

		if (mCityId.equals(TabAppActivity.SUZHOU_CITY_ID)) {
			navbar_TitleText.setText("移动专区");
		} else {
			navbar_TitleText.setText("运营商专区");
		}

		if (null == mobileZoneListView) {
			mobileZoneListView = new MobileZoneList(mobile_zone_listview, mActivity);
		}
	}
}
