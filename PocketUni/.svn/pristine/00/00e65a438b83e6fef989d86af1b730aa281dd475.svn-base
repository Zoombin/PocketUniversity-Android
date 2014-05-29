package com.xyhui.activity.event;

import java.util.ArrayList;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.City;
import com.xyhui.types.EventCat;
import com.xyhui.types.EventOrg;
import com.xyhui.types.School;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

public class EventSearchActivity extends FLActivity {
	private Button btn_back;
	private PullToRefreshListView event_listview;
	private EventList mEventListView;
	private EditText edit_keyword;
	private Button btn_search;

	private Spinner spinner_city;
	private String cityId;
	private int citySelectionId;
	private ArrayList<City> citys;
	private ArrayAdapter<City> cityAdapter;

	private Spinner spinner_school;
	private String schoolId;
	private int schoolSelectionId;
	private ArrayList<School> schools;
	private ArrayAdapter<School> schoolAdapter;

	private Spinner spinner_org;
	private ArrayList<EventCat> mOrgs;
	private ArrayAdapter<EventCat> orgAdapter;

	private boolean isInitSchoolSpinner = true;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_event_search);

		btn_back = (Button) findViewById(R.id.btn_back);
		spinner_school = (Spinner) findViewById(R.id.spinner_school);
		spinner_city = (Spinner) findViewById(R.id.spinner_city);
		spinner_org = (Spinner) findViewById(R.id.spinner_sort);
		event_listview = (PullToRefreshListView) findViewById(R.id.event_listview);
		edit_keyword = (EditText) findViewById(R.id.edit_keyword);
		btn_search = (Button) findViewById(R.id.btn_search);
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
		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 查询部落
				String key = edit_keyword.getText().toString();

				if (TextUtils.isEmpty(key.trim())) {
					NotificationsUtil.ToastTopMsg(getBaseContext(), "输入字段不能为空");
					return;
				}

				hideSoftInput(edit_keyword);

				String sid = "0";
				String sortid = "0";

				int i = spinner_school.getSelectedItemPosition();
				if (schools != null && i < schools.size()) {
					sid = schools.get(i).school;
				}
				int j = spinner_org.getSelectedItemPosition();
				if (mOrgs != null && j < mOrgs.size()) {
					sortid = mOrgs.get(j).id;
				}

				if (null == mEventListView) {
					mEventListView = new EventList(event_listview, mActivity,
							EventList.EVENT_SEARCH, sid, sortid, key);
				} else {
					mEventListView.search(sid, sortid, key);
				}
			}
		});

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
	}

	@Override
	public void ensureUi() {
		setCitySpinner();
	}

	private void setCitySpinner() {
		citys = PuApp.get().getLocalDataMgr().getCitys();

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

	private void initialSchoolSpinner() {
		// 默认为用户所在学校
		schoolId = new PrefUtil().getPreference(Params.LOCAL.SCHOOLID);

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

	private void setSchoolSpinner() {

		schools = PuApp.get().getLocalDataMgr().getSchools();

		int i = spinner_city.getSelectedItemPosition();

		if (i > 0 && i < citys.size()) {
			cityId = citys.get(i).id;

			for (int j = schools.size() - 1; j >= 0; j--) {
				School school = schools.get(j);
				String schoolCityId = school.cityId;

				if (!cityId.equals(schoolCityId)) {
					schools.remove(school);
				}
			}
		} else if (i != 0) {
			return;
		}

		// 将可选内容与ArrayAdapter连接起来
		schoolAdapter = new ArrayAdapter<School>(this, R.layout.spinner_item_layout, schools);
		schoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_school.setAdapter(schoolAdapter);

		if (isInitSchoolSpinner) {
			initialSchoolSpinner();
			isInitSchoolSpinner = false;
		} else {
			spinner_school.setSelection(0);
		}
	}

	private void setOrgSpinner() {
		if (null != mOrgs) {
			// 将可选内容与ArrayAdapter连接起来
			orgAdapter = new ArrayAdapter<EventCat>(EventSearchActivity.this,
					R.layout.spinner_item_layout, mOrgs);
			orgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// 将adapter 添加到spinner中
			spinner_org.setAdapter(orgAdapter);
			// 默认选择
			spinner_org.setSelection(0);
		}
	}

	CallBack eventOrgListCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			EventOrg eventOrg = JSONUtils.fromJson(response, EventOrg.class);

			mOrgs = eventOrg.getList();

			EventCat sort = new EventCat();
			sort.title = "选择院系";
			sort.id = "0";
			mOrgs.add(0, sort);

			setOrgSpinner();
		}
	};
}
