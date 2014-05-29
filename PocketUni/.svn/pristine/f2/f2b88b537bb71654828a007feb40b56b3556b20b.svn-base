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
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.widget.FLActivity;

public class TrainListActivity extends FLActivity {
	private Button btn_back;

	private Button btn_all_course;
	private Button btn_my_course;

	private LinearLayout course_toolbar;

	private Spinner spinner_province;
	private Spinner spinner_city;
	private Spinner spinner_sort;

	private ArrayAdapter<TrainArea> mProvinceAdapter;
	private ArrayAdapter<TrainArea> mCityAdapter;
	private ArrayAdapter<TrainSort> mSortAdapter;

	private ArrayList<TrainArea> mProvinceList;
	private ArrayList<TrainArea> mCityList;
	private ArrayList<TrainSort> mSortList;

	private SpinnerListener spinnerListener = new SpinnerListener();

	private String mProvinceId;
	private String mCity;
	private String mSortId;

	private PullToRefreshListView all_course_listview;
	private TrainList all_CourseListView;

	private PullToRefreshListView my_course_listview;
	private TrainList my_CourseListView;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_train_course_list);

		btn_back = (Button) findViewById(R.id.btn_back);

		btn_all_course = (Button) findViewById(R.id.btn_all_course);
		btn_my_course = (Button) findViewById(R.id.btn_my_course);

		course_toolbar = (LinearLayout) findViewById(R.id.toolbar);

		spinner_province = (Spinner) findViewById(R.id.spinner_province);
		spinner_city = (Spinner) findViewById(R.id.spinner_city);
		spinner_sort = (Spinner) findViewById(R.id.spinner_sort);
		all_course_listview = (PullToRefreshListView) findViewById(R.id.all_course_listview);
		my_course_listview = (PullToRefreshListView) findViewById(R.id.my_course_listview);
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

		btn_all_course.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all_course.setSelected(true);
				btn_my_course.setSelected(false);
				course_toolbar.setVisibility(View.VISIBLE);
				all_course_listview.setVisibility(View.VISIBLE);
				my_course_listview.setVisibility(View.GONE);

				all_CourseListView.refreshListViewStart();
			}
		});

		btn_my_course.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_all_course.setSelected(false);
				btn_my_course.setSelected(true);
				course_toolbar.setVisibility(View.GONE);
				all_course_listview.setVisibility(View.GONE);
				my_course_listview.setVisibility(View.VISIBLE);

				if (my_CourseListView == null) {
					my_CourseListView = new TrainList(my_course_listview, mActivity,
							TrainList.COURSE_MY_JOIN);
				} else {
					my_CourseListView.refreshListViewStart();
				}
			}
		});
	}

	@Override
	public void ensureUi() {
		showProgress();
		showProgress();
		new Api(provinceCB, mActivity).trainArea(PuApp.get().getToken(), null);
		new Api(sortCB, mActivity).trainSort(PuApp.get().getToken());

		btn_all_course.setSelected(true);
		btn_my_course.setSelected(false);
		course_toolbar.setVisibility(View.VISIBLE);
		all_course_listview.setVisibility(View.VISIBLE);
		my_course_listview.setVisibility(View.GONE);
	}

	private void setProvinceSpinner() {
		if (null != mProvinceList && mProvinceList.size() > 0) {
			TrainArea trainArea = new TrainArea();
			trainArea.id = "0";
			trainArea.title = "选择省份";
			mProvinceList.add(0, trainArea);

			// 将可选内容与ArrayAdapter连接起来
			mProvinceAdapter = new ArrayAdapter<TrainArea>(TrainListActivity.this,
					R.layout.spinner_item_layout, mProvinceList);
			mProvinceAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter 添加到spinner中
			spinner_province.setAdapter(mProvinceAdapter);
			spinner_province.setSelection(0);
			spinner_province.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					showProgress();
					new Api(cityCB, mActivity).trainArea(PuApp.get().getToken(),
							mProvinceList.get(position).id);
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
			TrainArea trainArea = new TrainArea();
			trainArea.id = "0";
			trainArea.title = "选择城市";
			mCityList.add(0, trainArea);

			// 将可选内容与ArrayAdapter连接起来
			mCityAdapter = new ArrayAdapter<TrainArea>(TrainListActivity.this,
					R.layout.spinner_item_layout, mCityList);
			mCityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter 添加到spinner中
			spinner_city.setAdapter(mCityAdapter);
			spinner_city.setSelection(0);
			spinner_city.setOnItemSelectedListener(spinnerListener);
		} else {
			NotificationsUtil.ToastBottomMsg(mActivity, "获取城市信息失败");
			finish();
		}
	}

	private void setSortSpinner() {
		if (null != mSortList && mSortList.size() > 0) {
			TrainSort trainSort = new TrainSort();
			trainSort.id = "0";
			trainSort.name = "选择分类";
			mSortList.add(0, trainSort);

			// 将可选内容与ArrayAdapter连接起来
			mSortAdapter = new ArrayAdapter<TrainSort>(TrainListActivity.this,
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
			mCity = mCityList.get(j).title;
		}

		j = spinner_sort.getSelectedItemPosition();
		if (mSortList != null && j < mSortList.size()) {
			mSortId = mSortList.get(j).id;
		}

		if (all_CourseListView != null) {
			all_CourseListView.search(mProvinceId, mCity, mSortId);
		} else {
			all_CourseListView = new TrainList(all_course_listview, mActivity,
					TrainList.COURSE_ALL, mProvinceId, mCity, mSortId);
		}
	}

	private class SpinnerListener implements OnItemSelectedListener {
		private int mCount;

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			mCount++;
			VolleyLog.d(mCount + "");
			if (mCount >= 2) {
				search();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}

	CallBack provinceCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			mProvinceList = JSONUtils.fromJson(response, new TypeToken<ArrayList<TrainArea>>() {
			});
			setProvinceSpinner();
		}

		public void onFailure(String message) {
			NotificationsUtil.ToastBottomMsg(mActivity, "获取省份信息失败");
			finish();
		}
	};

	CallBack cityCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			mCityList = JSONUtils.fromJson(response, new TypeToken<ArrayList<TrainArea>>() {
			});
			setCitySpinner();
		}

		public void onFailure(String message) {
			NotificationsUtil.ToastBottomMsg(mActivity, "获取城市信息失败");
			finish();
		}
	};

	CallBack sortCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			mSortList = JSONUtils.fromJson(response, new TypeToken<ArrayList<TrainSort>>() {
			});
			setSortSpinner();
		}

		public void onFailure(String message) {
			NotificationsUtil.ToastBottomMsg(mActivity, "获取分类信息失败");
			finish();
		}
	};

	private static class TrainSort {
		String id;
		String name;

		@Override
		public String toString() {
			return name;
		}
	}

	private static class TrainArea {
		String id;
		String title;

		@Override
		public String toString() {
			return title;
		}
	}
}
