package com.xyhui.activity.app;

import java.util.ArrayList;

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
import com.xyhui.R;
import com.xyhui.types.CourseCat;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

public class CourseListActivity extends FLActivity {
	private Button btn_back;
	private Button btn_search;

	private Button btn_all_course;
	private Button btn_my_course;

	private LinearLayout course_toolbar;

	private Spinner spinner_sort;
	private ArrayAdapter<CourseCat> sortAdapter;

	private PullToRefreshListView all_course_listview;
	private CourseList all_CourseListView;

	private PullToRefreshListView my_course_listview;
	private CourseList my_CourseListView;

	private ArrayList<CourseCat> sorts;

	private String schoolId;

	private SpinnerListener spinnerListener = new SpinnerListener();

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_course_list);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_search = (Button) findViewById(R.id.btn_search);

		btn_all_course = (Button) findViewById(R.id.btn_all_course);
		btn_my_course = (Button) findViewById(R.id.btn_my_course);

		course_toolbar = (LinearLayout) findViewById(R.id.toolbar);

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

		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开查找课程
				Intent intent = new Intent();
				intent.setClass(mActivity, CourseSearchActivity.class);
				mActivity.startActivity(intent);
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
					my_CourseListView = new CourseList(my_course_listview, mActivity,
							CourseList.COURSE_MY_JOIN);
				}
			}
		});
	}

	@Override
	public void ensureUi() {
		btn_all_course.setSelected(true);
		btn_my_course.setSelected(false);
		course_toolbar.setVisibility(View.VISIBLE);
		all_course_listview.setVisibility(View.VISIBLE);
		my_course_listview.setVisibility(View.GONE);

		schoolId = new PrefUtil().getPreference(Params.LOCAL.SCHOOLID);

		setCatSpinner();
	}

	private void setCatSpinner() {
		// sorts = PuApp.get().getLocalDataMgr().getCourseCats();

		if (null != sorts) {
			// 将可选内容与ArrayAdapter连接起来
			sortAdapter = new ArrayAdapter<CourseCat>(CourseListActivity.this,
					R.layout.spinner_item_layout, sorts);
			sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter 添加到spinner中
			spinner_sort.setAdapter(sortAdapter);

			spinner_sort.setSelection(0);

			spinner_sort.setOnItemSelectedListener(spinnerListener);
		}
	}

	void search() {
		String sortid = "0";

		int j = spinner_sort.getSelectedItemPosition();
		if (sorts != null && j < sorts.size()) {
			sortid = sorts.get(j).id;
		}

		if (all_CourseListView != null) {
			all_CourseListView.search(schoolId, sortid, null);
		} else {
			all_CourseListView = new CourseList(all_course_listview, mActivity,
					CourseList.COURSE_ALL, schoolId, sortid, null);
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
}
