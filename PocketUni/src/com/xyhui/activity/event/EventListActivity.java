package com.xyhui.activity.event;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.types.Banner;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.AdBannerLayout;
import com.xyhui.widget.FLActivity;

public class EventListActivity extends FLActivity {
	private Button btn_back;
	private Button btn_operate;
	private Button btn_all_event;
	private Button btn_recommend_event;
	private Button btn_my_event;
	private Button btn_wofaqide;
	private Button btn_weikaishide;
	private Button btn_yikaishide;
	private Button btn_woshoucangde;
	private Button btn_selected;

	private PullToRefreshListView all_event_listview;
	private EventList all_EventListView;

	private PullToRefreshListView recommend_event_listview;
	private EventList recommend_EventListView;

	private PullToRefreshListView my_event_listview;
	private EventList my_EventListView;
	private LinearLayout my_event_toolbar;

	private AdBannerLayout ad_banner;

	private String cityId;
	private String schoolId;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_event_list);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_operate = (Button) findViewById(R.id.btn_operate);

		btn_all_event = (Button) findViewById(R.id.btn_all_event);
		btn_recommend_event = (Button) findViewById(R.id.btn_recommend_event);
		btn_my_event = (Button) findViewById(R.id.btn_my_event);

		all_event_listview = (PullToRefreshListView) findViewById(R.id.all_event_listview);

		recommend_event_listview = (PullToRefreshListView) findViewById(R.id.recommend_event_listview);

		ad_banner = (AdBannerLayout) findViewById(R.id.ad_banner);

		my_event_toolbar = (LinearLayout) findViewById(R.id.my_event_toolbar);
		btn_wofaqide = (Button) findViewById(R.id.btn_wofaqide);
		btn_wofaqide.setTag(EventList.EVENT_WOFAQIDE);
		btn_weikaishide = (Button) findViewById(R.id.btn_weikaishide);
		btn_weikaishide.setTag(EventList.EVENT_WEIKAISHI);
		btn_yikaishide = (Button) findViewById(R.id.btn_yikaishide);
		btn_yikaishide.setTag(EventList.EVENT_YIKAISHI);
		btn_woshoucangde = (Button) findViewById(R.id.btn_woshoucangde);
		btn_woshoucangde.setTag(EventList.EVENT_WOSHOUCANG);
		my_event_listview = (PullToRefreshListView) findViewById(R.id.my_event_listview);

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
		btn_operate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPanel();
			}
		});
		btn_all_event.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all_event.setSelected(true);
				btn_recommend_event.setSelected(false);
				btn_my_event.setSelected(false);
				my_event_toolbar.setVisibility(View.GONE);

				all_event_listview.setVisibility(View.VISIBLE);
				recommend_event_listview.setVisibility(View.GONE);
				my_event_listview.setVisibility(View.GONE);
			}
		});

		btn_recommend_event.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all_event.setSelected(false);
				btn_recommend_event.setSelected(true);
				btn_my_event.setSelected(false);
				my_event_toolbar.setVisibility(View.GONE);

				all_event_listview.setVisibility(View.GONE);
				recommend_event_listview.setVisibility(View.VISIBLE);
				my_event_listview.setVisibility(View.GONE);

				if (recommend_EventListView == null) {
					recommend_EventListView = new EventList(
							recommend_event_listview, mActivity,
							EventList.EVENT_RECOMMEND);
				}
			}
		});
		btn_my_event.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all_event.setSelected(false);
				btn_recommend_event.setSelected(false);
				btn_my_event.setSelected(true);
				my_event_toolbar.setVisibility(View.VISIBLE);

				all_event_listview.setVisibility(View.GONE);
				recommend_event_listview.setVisibility(View.GONE);
				my_event_listview.setVisibility(View.VISIBLE);

				if (my_EventListView == null) {
					my_EventListView = new EventList(my_event_listview,
							mActivity, EventList.EVENT_WOFAQIDE);
				}
			}
		});

		OnClickListener myEventOCL = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btn_selected != null) {
					btn_selected.setSelected(false);
				}
				btn_selected = (Button) v;
				btn_selected.setSelected(true);

				if (my_EventListView == null) {
					my_EventListView = new EventList(my_event_listview,
							mActivity, (Integer) btn_selected.getTag());
				} else {
					my_EventListView.refresh((Integer) btn_selected.getTag(),
							"", "");
				}
			}
		};
		btn_wofaqide.setOnClickListener(myEventOCL);
		btn_weikaishide.setOnClickListener(myEventOCL);
		btn_yikaishide.setOnClickListener(myEventOCL);
		btn_woshoucangde.setOnClickListener(myEventOCL);

	}

	@Override
	protected void onResume() {
		super.onResume();
		ad_banner.reload();
	}

	@Override
	protected void onPause() {
		super.onPause();
		ad_banner.pause();
	}

	@Override
	public void ensureUi() {
		ad_banner.init(Banner.TYPE_EVENT);

		btn_all_event.setSelected(true);
		btn_recommend_event.setSelected(false);
		btn_my_event.setSelected(false);
		my_event_toolbar.setVisibility(View.GONE);
		all_event_listview.setVisibility(View.VISIBLE);
		recommend_event_listview.setVisibility(View.GONE);
		my_event_listview.setVisibility(View.GONE);

		// 默认为用户所在学校
		schoolId = new PrefUtil().getPreference(Params.LOCAL.SCHOOLID);
		// 默认为用户所在城市
		cityId = new PrefUtil().getPreference(Params.LOCAL.CITYID);
		
		btn_selected = btn_wofaqide;
		btn_selected.setSelected(true);
		
		search();
	}

	private void showPanel() {
		Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("选择操作");

		builder.setItems(R.array.event_operate,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent();

						switch (which) {
						case 0:
							// 打开搜索活动
							intent.setClass(mActivity,
									EventSearchActivity.class);
							startActivity(intent);
							break;
						case 1:
							// 发起活动
							intent.setClass(mActivity,
									EventLaunchActivity.class);
							startActivity(intent);
							break;
						case 2:
							dialog.cancel();
							break;
						}
					}
				}).show();

	}

	void search() {
		String sortid = "0";
		if (all_EventListView != null) {
			all_EventListView.search(schoolId, sortid, null);
		} else {
			all_EventListView = new EventList(all_event_listview, mActivity,
					EventList.EVENT_ALL, schoolId, sortid, "");
		}
	}
}
