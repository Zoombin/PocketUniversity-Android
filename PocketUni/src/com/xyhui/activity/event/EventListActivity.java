package com.xyhui.activity.event;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.JSONUtils;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Banner;
import com.xyhui.types.City;
import com.xyhui.types.EventCat;
import com.xyhui.types.EventOrg;
import com.xyhui.types.School;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.AdBannerLayout;
import com.xyhui.widget.FLActivity;

public class EventListActivity extends FLActivity {
	private Button btn_back;
	private Button btn_operate;
	private Button btn_all_event;
	private Button btn_my_event;
	private Button btn_create;
	private Button btn_join;
	private Button btn_favor;
	private Button btn_selected;

	private PullToRefreshListView all_event_listview;
	private EventList all_EventListView;
	private LinearLayout all_event_toolbar;

	private PullToRefreshListView my_event_listview;
	private EventList my_EventListView;
	private LinearLayout my_event_toolbar;

	private AdBannerLayout ad_banner;

	private Spinner spinner_city;
	private ArrayAdapter<City> cityAdapter;
	private ArrayList<City> citys;
	private String cityId;
	private int citySelectionId;

	private Spinner spinner_school;
	private ArrayAdapter<School> schoolAdapter;
	private ArrayList<School> schools;
	private String schoolId;
	private int schoolSelectionId;

	private Spinner spinner_org;
	private ArrayAdapter<EventCat> orgAdapter;
	private ArrayList<EventCat> mOrgs;

	private boolean isInitSchoolSpinner = true;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_event_list);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_operate = (Button) findViewById(R.id.btn_operate);

		btn_all_event = (Button) findViewById(R.id.btn_all_event);
		btn_my_event = (Button) findViewById(R.id.btn_my_event);

		all_event_toolbar = (LinearLayout) findViewById(R.id.all_event_toolbar);
		spinner_city = (Spinner) findViewById(R.id.spinner_city);
		spinner_school = (Spinner) findViewById(R.id.spinner_school);
		spinner_org = (Spinner) findViewById(R.id.spinner_sort);
		all_event_listview = (PullToRefreshListView) findViewById(R.id.all_event_listview);

		ad_banner = (AdBannerLayout) findViewById(R.id.ad_banner);

		my_event_toolbar = (LinearLayout) findViewById(R.id.my_event_toolbar);
		btn_create = (Button) findViewById(R.id.btn_create);
		btn_create.setTag(EventList.EVENT_MY_CREATE);
		btn_join = (Button) findViewById(R.id.btn_join);
		btn_join.setTag(EventList.EVENT_MY_JOIN);
		btn_favor = (Button) findViewById(R.id.btn_favor);
		btn_favor.setTag(EventList.EVENT_MY_FAVOR);
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
				btn_my_event.setSelected(false);
				all_event_toolbar.setVisibility(View.VISIBLE);
				my_event_toolbar.setVisibility(View.GONE);
				all_event_listview.setVisibility(View.VISIBLE);
				my_event_listview.setVisibility(View.GONE);
			}
		});
		btn_my_event.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all_event.setSelected(false);
				btn_my_event.setSelected(true);
				all_event_toolbar.setVisibility(View.GONE);
				my_event_toolbar.setVisibility(View.VISIBLE);
				all_event_listview.setVisibility(View.GONE);
				my_event_listview.setVisibility(View.VISIBLE);

				if (my_EventListView == null) {
					my_EventListView = new EventList(my_event_listview, mActivity,
							EventList.EVENT_MY_CREATE);
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
					my_EventListView = new EventList(my_event_listview, mActivity,
							(Integer) btn_selected.getTag());
				} else {
					my_EventListView.refresh((Integer) btn_selected.getTag(), "", "");
				}
			}
		};
		btn_create.setOnClickListener(myEventOCL);
		btn_join.setOnClickListener(myEventOCL);
		btn_favor.setOnClickListener(myEventOCL);

		spinner_city.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				setSchoolSpinner();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		spinner_school.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				int i = spinner_school.getSelectedItemPosition();
				if (schools != null && i < schools.size()) {
					schoolId = schools.get(i).school;
				}

				new Api(eventOrgListCB, mActivity).getOrgList(PuApp.get().getToken(), schoolId);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		spinner_org.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				search();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

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
		btn_my_event.setSelected(false);
		all_event_toolbar.setVisibility(View.VISIBLE);
		my_event_toolbar.setVisibility(View.GONE);
		all_event_listview.setVisibility(View.VISIBLE);
		my_event_listview.setVisibility(View.GONE);

		setCitySpinner();

		btn_selected = btn_create;
		btn_selected.setSelected(true);
	}

	private void showPanel() {
		Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("选择操作");

		builder.setItems(R.array.event_operate, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				Intent intent = new Intent();

				switch (which) {
				case 0:
					// 打开搜索活动
					intent.setClass(mActivity, EventSearchActivity.class);
					startActivity(intent);
					break;
				case 1:
					// 发起活动
					intent.setClass(mActivity, EventLaunchActivity.class);
					startActivity(intent);
					break;
				case 2:
					dialog.cancel();
					break;
				}
			}
		}).show();

	}

	private void setCitySpinner() {
		citys = PuApp.get().getLocalDataMgr().getCitys();

		if (null != citys) {
			// 将可选内容与ArrayAdapter连接起来
			cityAdapter = new ArrayAdapter<City>(this, R.layout.spinner_item_layout, citys);
			cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// 将adapter 添加到spinner中
			spinner_city.setAdapter(cityAdapter);

			// 默认为用户所在城市
			cityId = new PrefUtil().getPreference(Params.LOCAL.CITYID);

			int i = 1;
			for (; i < citys.size(); i++) {
				if (citys.get(i).id.equals(cityId)) {
					citySelectionId = i;
					break;
				}
			}
			if (i >= citys.size()) {
				citySelectionId = 0;
			}

			spinner_city.setSelection(citySelectionId);
		}
	}

	private void initialSchoolSpinner() {
		// 默认为用户所在学校
		schoolId = new PrefUtil().getPreference(Params.LOCAL.SCHOOLID);

		if (null != schools) {
			int selection = 1;
			for (; selection < schools.size(); selection++) {
				if (schools.get(selection).school.equals(schoolId)) {
					schoolSelectionId = selection;
					break;
				}
			}
			if (selection >= schools.size()) {
				schoolSelectionId = 0;
			}

			spinner_school.setSelection(schoolSelectionId);
		}
	}

	private void setSchoolSpinner() {

		schools = PuApp.get().getLocalDataMgr().getSchools();

		if (null != schools) {
			int i = spinner_city.getSelectedItemPosition();

			if (i > 0 && i < citys.size()) {
				cityId = citys.get(i).id;

				for (int j = schools.size() - 1; j >= 0; j--) {
					School school = schools.get(j);

					if (!cityId.equals(school.cityId)) {
						schools.remove(school);
					}
				}
			} else if (i != 0) {
				return;
			}

			// 将可选内容与ArrayAdapter连接起来
			schoolAdapter = new ArrayAdapter<School>(this, R.layout.spinner_item_layout, schools);
			schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// 将adapter添加到spinner中
			spinner_school.setAdapter(schoolAdapter);

			if (isInitSchoolSpinner) {
				initialSchoolSpinner();
				isInitSchoolSpinner = false;
			} else {
				spinner_school.setSelection(0);
			}
		}
	}

	private void setOrgSpinner() {
		if (null != mOrgs) {
			// 将可选内容与ArrayAdapter连接起来
			orgAdapter = new ArrayAdapter<EventCat>(EventListActivity.this,
					R.layout.spinner_item_layout, mOrgs);
			orgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// 将adapter 添加到spinner中
			spinner_org.setAdapter(orgAdapter);
			// 默认选择
			spinner_org.setSelection(0);
		}
	}

	void search() {
		String sortid = "0";

		int j = spinner_org.getSelectedItemPosition();
		if (mOrgs != null && j < mOrgs.size()) {
			sortid = mOrgs.get(j).id;
		}

		if (all_EventListView != null) {
			all_EventListView.search(schoolId, sortid, null);
		} else {
			all_EventListView = new EventList(all_event_listview, mActivity, EventList.EVENT_ALL,
					schoolId, sortid, "");
		}
	}

	CallBack eventOrgListCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			EventOrg eventOrg = JSONUtils.fromJson(response, EventOrg.class);

			if (null != eventOrg) {
				mOrgs = eventOrg.getList();

				EventCat sort = new EventCat();
				sort.title = "选择院系";
				sort.id = "0";
				mOrgs.add(0, sort);

				setOrgSpinner();
			}
		}
	};
}