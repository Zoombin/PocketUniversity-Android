package com.xyhui.activity.app;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.widget.FLActivity;

public class DonateListActivity extends FLActivity {
	private static final String DEFAULT_ID = "0";
	private static final String DEFAULT_TYPE = "";
	private static final String PROVINCE = "province";
	private static final String CITY = "city";
	private static final String SID = "sid";
	private static final String SID1 = "sid1";

	private Button btn_back;
	private Button btn_all_donate;
	private Button btn_my_donate;
	private Button btn_my_shopping;
	private Button btn_price_one;
	private Button btn_price_three;
	private Button btn_price_five;
	private Button btn_select;
	private LinearLayout donate_toolbar;
	private Spinner spinner_province;
	private Spinner spinner_city;
	private Spinner spinner_school;
	private Spinner spinner_depart;
	private Spinner spinner_sort;
	private PullToRefreshListView all_donate_listview;
	private DonateList all_DonateListView;
	private PullToRefreshListView my_donate_listview;
	private DonateList my_DonateListView;
	private PullToRefreshListView my_shopping_listview;
	private DonateList my_ShoppingListView;

	private ArrayAdapter<DonateArea> mProvinceAdapter;
	private ArrayAdapter<DonateArea> mCityAdapter;
	private ArrayAdapter<DonateArea> mSchoolAdapter;
	private ArrayAdapter<DonateArea> mDepartAdapter;
	private ArrayAdapter<DonateSort> mSortAdapter;

	private ArrayList<DonateArea> mProvinceList;
	private ArrayList<DonateArea> mCityList;
	private ArrayList<DonateArea> mSchoolList;
	private ArrayList<DonateArea> mDepartList;
	private ArrayList<DonateSort> mSortList;

	private SpinnerListener spinnerListener = new SpinnerListener();

	private String mProvinceId;
	private String mCityId;
	private String mSchoolId;
	private String mDepartId;
	private String mSortId;
	private String mPrice;

	private String mCurType;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_donate_list);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_all_donate = (Button) findViewById(R.id.btn_all_course);
		btn_my_donate = (Button) findViewById(R.id.btn_my_donate);
		btn_my_shopping = (Button) findViewById(R.id.btn_my_course);
		btn_price_one = (Button) findViewById(R.id.btn_price_one);
		btn_price_three = (Button) findViewById(R.id.btn_price_three);
		btn_price_five = (Button) findViewById(R.id.btn_price_five);
		donate_toolbar = (LinearLayout) findViewById(R.id.toolbar);
		spinner_province = (Spinner) findViewById(R.id.spinner_province);
		spinner_city = (Spinner) findViewById(R.id.spinner_city);
		spinner_school = (Spinner) findViewById(R.id.spinner_school);
		spinner_depart = (Spinner) findViewById(R.id.spinner_depart);
		spinner_sort = (Spinner) findViewById(R.id.spinner_sort);
		all_donate_listview = (PullToRefreshListView) findViewById(R.id.all_course_listview);
		my_donate_listview = (PullToRefreshListView) findViewById(R.id.my_donate_listview);
		my_shopping_listview = (PullToRefreshListView) findViewById(R.id.my_course_listview);
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

		btn_all_donate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all_donate.setSelected(true);
				btn_my_donate.setSelected(false);
				btn_my_shopping.setSelected(false);
				donate_toolbar.setVisibility(View.VISIBLE);
				all_donate_listview.setVisibility(View.VISIBLE);
				my_donate_listview.setVisibility(View.GONE);
				my_shopping_listview.setVisibility(View.GONE);

				all_DonateListView.refreshListViewStart();
			}
		});

		btn_my_donate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all_donate.setSelected(false);
				btn_my_donate.setSelected(true);
				btn_my_shopping.setSelected(false);
				donate_toolbar.setVisibility(View.GONE);
				all_donate_listview.setVisibility(View.GONE);
				my_donate_listview.setVisibility(View.VISIBLE);
				my_shopping_listview.setVisibility(View.GONE);

				if (my_DonateListView == null) {
					my_DonateListView = new DonateList(my_donate_listview, mActivity,
							DonateList.DONATE_MINE);
				} else {
					my_DonateListView.refreshListViewStart();
				}
			}
		});

		btn_my_shopping.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all_donate.setSelected(false);
				btn_my_donate.setSelected(false);
				btn_my_shopping.setSelected(true);
				donate_toolbar.setVisibility(View.GONE);
				all_donate_listview.setVisibility(View.GONE);
				my_donate_listview.setVisibility(View.GONE);
				my_shopping_listview.setVisibility(View.VISIBLE);

				if (my_ShoppingListView == null) {
					my_ShoppingListView = new DonateList(my_shopping_listview, mActivity,
							DonateList.DONATE_SHOPPING);
				} else {
					my_ShoppingListView.refreshListViewStart();
				}
			}
		});

		OnClickListener priceOCL = new OnClickListener() {
			@Override
			public void onClick(View v) {
				setPrice((Button) v);
				search();
			}
		};

		btn_price_one.setOnClickListener(priceOCL);
		btn_price_three.setOnClickListener(priceOCL);
		btn_price_five.setOnClickListener(priceOCL);
	}

	@Override
	public void ensureUi() {
		showProgress();
		new Api(sortCB, mActivity).catList(PuApp.get().getToken());

		setPrice(btn_price_one);
		setCurrentArea(DEFAULT_TYPE, DEFAULT_ID);

		btn_all_donate.setSelected(true);
		btn_my_donate.setSelected(false);
		btn_my_shopping.setSelected(false);
		donate_toolbar.setVisibility(View.VISIBLE);
		all_donate_listview.setVisibility(View.VISIBLE);
		my_donate_listview.setVisibility(View.GONE);
		my_shopping_listview.setVisibility(View.GONE);
	}

	private void setPrice(Button btn) {
		btn.setSelected(true);

		if (btn_select != null && btn_select != btn) {
			btn_select.setSelected(false);
		}
		btn_select = btn;

		switch (btn.getId()) {
		case R.id.btn_price_one:
			mPrice = "1";
			break;
		case R.id.btn_price_three:
			mPrice = "3";
			break;
		case R.id.btn_price_five:
			mPrice = "5";
			break;
		}
	}

	private void setCurrentArea(String type, String id) {
		mCurType = type;
		if (DEFAULT_TYPE.equals(type)) {
			mProvinceId = DEFAULT_ID;
			mCityId = DEFAULT_ID;
			mSchoolId = DEFAULT_ID;
			mDepartId = DEFAULT_ID;

			if (null == mProvinceList || mProvinceList.size() <= 0) {
				showProgress();
				new Api(areaCB, mActivity)
						.citySchool(PuApp.get().getToken(), PROVINCE, DEFAULT_ID);
			}
		} else if (PROVINCE.equals(type)) {
			mProvinceId = id;
			mCityId = DEFAULT_ID;
			mSchoolId = DEFAULT_ID;
			mDepartId = DEFAULT_ID;
			if (mProvinceId != DEFAULT_ID) {
				showProgress();
				new Api(areaCB, mActivity).citySchool(PuApp.get().getToken(), CITY, mProvinceId);
			}
		} else if (CITY.equals(type)) {
			mCityId = id;
			mSchoolId = DEFAULT_ID;
			mDepartId = DEFAULT_ID;
			if (mCityId != DEFAULT_ID) {
				showProgress();
				new Api(areaCB, mActivity).citySchool(PuApp.get().getToken(), SID, mCityId);
			}
		} else if (SID.equals(type)) {
			mSchoolId = id;
			mDepartId = DEFAULT_ID;
			if (mSchoolId != DEFAULT_ID) {
				showProgress();
				new Api(areaCB, mActivity).citySchool(PuApp.get().getToken(), SID1, mSchoolId);
			}
		} else if (SID1.equals(type)) {
			mDepartId = id;
		}

		showSpinner();
		if (!DEFAULT_TYPE.equals(type)) {
			search();
		}
	}

	private void showSpinner() {
		if (DEFAULT_TYPE.equals(mCurType)) {
			spinner_province.setVisibility(View.VISIBLE);
			spinner_city.setVisibility(View.INVISIBLE);
			spinner_school.setVisibility(View.INVISIBLE);
			spinner_depart.setVisibility(View.INVISIBLE);
		} else if (PROVINCE.equals(mCurType)) {
			if (DEFAULT_ID.equals(mProvinceId)) {
				spinner_province.setVisibility(View.VISIBLE);
				spinner_city.setVisibility(View.INVISIBLE);
				spinner_school.setVisibility(View.INVISIBLE);
				spinner_depart.setVisibility(View.INVISIBLE);
			} else {
				spinner_province.setVisibility(View.VISIBLE);
				spinner_city.setVisibility(View.VISIBLE);
				spinner_school.setVisibility(View.INVISIBLE);
				spinner_depart.setVisibility(View.INVISIBLE);
			}
		} else if (CITY.equals(mCurType)) {
			if (DEFAULT_ID.equals(mCityId)) {
				spinner_province.setVisibility(View.VISIBLE);
				spinner_city.setVisibility(View.VISIBLE);
				spinner_school.setVisibility(View.INVISIBLE);
				spinner_depart.setVisibility(View.INVISIBLE);
			} else {
				spinner_province.setVisibility(View.VISIBLE);
				spinner_city.setVisibility(View.VISIBLE);
				spinner_school.setVisibility(View.VISIBLE);
				spinner_depart.setVisibility(View.INVISIBLE);
			}
		} else if (SID.equals(mCurType)) {
			if (DEFAULT_ID.equals(mSchoolId)) {
				spinner_province.setVisibility(View.VISIBLE);
				spinner_city.setVisibility(View.VISIBLE);
				spinner_school.setVisibility(View.VISIBLE);
				spinner_depart.setVisibility(View.INVISIBLE);
			} else {
				spinner_province.setVisibility(View.VISIBLE);
				spinner_city.setVisibility(View.VISIBLE);
				spinner_school.setVisibility(View.VISIBLE);
				spinner_depart.setVisibility(View.VISIBLE);
			}
		}
	}

	private void setProvinceSpinner() {
		if (null != mProvinceList && mProvinceList.size() > 0) {
			DonateArea trainArea = new DonateArea();
			trainArea.id = DEFAULT_ID;
			trainArea.title = "选择省份";
			mProvinceList.add(0, trainArea);

			// 将可选内容与ArrayAdapter连接起来
			mProvinceAdapter = new ArrayAdapter<DonateArea>(DonateListActivity.this,
					R.layout.spinner_item_layout, mProvinceList);
			mProvinceAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// 将adapter 添加到spinner中
			spinner_province.setAdapter(mProvinceAdapter);
			spinner_province.setSelection(0);
			spinner_province.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					setCurrentArea(PROVINCE, mProvinceList.get(position).id);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});
		} else {
			NotificationsUtil.ToastBottomMsg(mActivity, "获取省份信息失败");
			finish();
		}
	}

	private void setCitySpinner() {
		if (null != mCityList && mCityList.size() > 0) {
			DonateArea trainArea = new DonateArea();
			trainArea.id = DEFAULT_ID;
			trainArea.title = "选择城市";
			mCityList.add(0, trainArea);

			// 将可选内容与ArrayAdapter连接起来
			mCityAdapter = new ArrayAdapter<DonateArea>(DonateListActivity.this,
					R.layout.spinner_item_layout, mCityList);
			mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter添加到spinner中
			spinner_city.setAdapter(mCityAdapter);
			spinner_city.setSelection(0);
			spinner_city.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					setCurrentArea(CITY, mCityList.get(position).id);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});
		} else {
			NotificationsUtil.ToastBottomMsg(mActivity, "获取城市信息失败");
			finish();
		}
	}

	private void setSchoolSpinner() {
		if (null != mSchoolList && mSchoolList.size() > 0) {
			DonateArea trainArea = new DonateArea();
			trainArea.id = DEFAULT_ID;
			trainArea.title = "选择学校";
			mSchoolList.add(0, trainArea);

			// 将可选内容与ArrayAdapter连接起来
			mSchoolAdapter = new ArrayAdapter<DonateArea>(DonateListActivity.this,
					R.layout.spinner_item_layout, mSchoolList);
			mSchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// 将adapter 添加到spinner中
			spinner_school.setAdapter(mSchoolAdapter);
			spinner_school.setSelection(0);
			spinner_school.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					setCurrentArea(SID, mSchoolList.get(position).id);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});
		} else {
			NotificationsUtil.ToastBottomMsg(mActivity, "获取学校信息失败");
			finish();
		}
	}

	private void setDepartSpinner() {
		if (null != mDepartList && mDepartList.size() > 0) {
			DonateArea trainArea = new DonateArea();
			trainArea.id = DEFAULT_ID;
			trainArea.title = "选择院系";
			mDepartList.add(0, trainArea);

			// 将可选内容与ArrayAdapter连接起来
			mDepartAdapter = new ArrayAdapter<DonateArea>(DonateListActivity.this,
					R.layout.spinner_item_layout, mDepartList);
			mDepartAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter 添加到spinner中
			spinner_depart.setAdapter(mDepartAdapter);
			spinner_depart.setSelection(0);
			spinner_depart.setOnItemSelectedListener(spinnerListener);
		} else {
			NotificationsUtil.ToastBottomMsg(mActivity, "获取院系信息失败");
			finish();
		}
	}

	private void setSortSpinner() {
		if (null != mSortList && mSortList.size() > 0) {
			DonateSort trainSort = new DonateSort();
			trainSort.id = DEFAULT_ID;
			trainSort.name = "选择分类";
			mSortList.add(0, trainSort);

			// 将可选内容与ArrayAdapter连接起来
			mSortAdapter = new ArrayAdapter<DonateSort>(DonateListActivity.this,
					R.layout.spinner_item_layout, mSortList);
			mSortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter 添加到spinner中
			spinner_sort.setAdapter(mSortAdapter);
			spinner_sort.setSelection(0);
			spinner_sort.setOnItemSelectedListener(spinnerListener);
		} else {
			NotificationsUtil.ToastBottomMsg(mActivity, "获取分类信息失败");
			finish();
		}
	}

	private void search() {
		int j = spinner_province.getSelectedItemPosition();
		if (mProvinceList != null && j < mProvinceList.size()) {
			mProvinceId = mProvinceList.get(j).id;
		}

		j = spinner_city.getSelectedItemPosition();
		if (mCityList != null && j < mCityList.size()) {
			mCityId = mCityList.get(j).id;
		}

		j = spinner_school.getSelectedItemPosition();
		if (mSchoolList != null && j < mSchoolList.size()) {
			mSchoolId = mSchoolList.get(j).id;
		}

		j = spinner_depart.getSelectedItemPosition();
		if (mDepartList != null && j < mDepartList.size()) {
			mDepartId = mDepartList.get(j).id;
		}

		j = spinner_sort.getSelectedItemPosition();
		if (mSortList != null && j < mSortList.size()) {
			mSortId = mSortList.get(j).id;
		}

		if (all_DonateListView != null) {
			all_DonateListView.search(mProvinceId, mCityId, mSchoolId, mDepartId, mSortId, mPrice);
		} else {
			all_DonateListView = new DonateList(all_donate_listview, mActivity,
					DonateList.DONATE_ALL, mProvinceId, mCityId, mSchoolId, mDepartId, mSortId,
					mPrice);
		}
	}

	private class SpinnerListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			search();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	CallBack areaCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			if (DEFAULT_TYPE.equals(mCurType)) {
				mProvinceList = JSONUtils.fromJson(response,
						new TypeToken<ArrayList<DonateArea>>() {
						});
				setProvinceSpinner();
			} else if (PROVINCE.equals(mCurType)) {
				mCityList = JSONUtils.fromJson(response, new TypeToken<ArrayList<DonateArea>>() {
				});
				setCitySpinner();
			} else if (CITY.equals(mCurType)) {
				mSchoolList = JSONUtils.fromJson(response, new TypeToken<ArrayList<DonateArea>>() {
				});
				setSchoolSpinner();
			} else if (SID.equals(mCurType)) {
				mDepartList = JSONUtils.fromJson(response, new TypeToken<ArrayList<DonateArea>>() {
				});
				setDepartSpinner();
			}
		}

		public void onFailure(String message) {
			NotificationsUtil.ToastBottomMsg(mActivity, "获取信息失败");
			finish();
		}
	};

	CallBack sortCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			mSortList = JSONUtils.fromJson(response, new TypeToken<ArrayList<DonateSort>>() {
			});
			setSortSpinner();
		}

		public void onFailure(String message) {
			NotificationsUtil.ToastBottomMsg(mActivity, "获取分类信息失败");
			finish();
		}
	};

	private static class DonateSort {
		String id;
		String name;

		@Override
		public String toString() {
			return name;
		}
	}

	private static class DonateArea {
		String id;
		String title;

		@Override
		public String toString() {
			return title;
		}
	}
}
