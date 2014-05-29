package com.xyhui.activity.app;

import java.util.ArrayList;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.NotificationsUtil;
import com.xyhui.R;
import com.xyhui.types.CourseCat;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

public class CourseSearchActivity extends FLActivity {

	private Button btn_back;
	private Spinner spinner_sort;
	private ArrayAdapter<CourseCat> sortAdapter;

	private PullToRefreshListView course_listview;
	private CourseList mCourseListView;
	private EditText edit_keyword;
	private Button btn_search;

	private ArrayList<CourseCat> sorts;

	private String schoolId;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_course_search);

		btn_back = (Button) findViewById(R.id.btn_back);
		spinner_sort = (Spinner) findViewById(R.id.spinner_sort);
		course_listview = (PullToRefreshListView) findViewById(R.id.course_listview);
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
				// 查询课程
				String key = edit_keyword.getText().toString();

				if (TextUtils.isEmpty(key.trim())) {
					NotificationsUtil.ToastBottomMsg(getBaseContext(), "输入字段不能为空");
					return;
				}

				hideSoftInput(edit_keyword);

				String sortid = "0";

				int j = spinner_sort.getSelectedItemPosition();
				if (sorts != null && j < sorts.size()) {
					sortid = sorts.get(j).id;
				}

				if (mCourseListView == null) {
					mCourseListView = new CourseList(course_listview, mActivity,
							CourseList.COURSE_SEARCH, schoolId, sortid, key);
				} else {
					mCourseListView.search(schoolId, sortid, key);
				}
			}
		});
	}

	@Override
	public void ensureUi() {
		schoolId = new PrefUtil().getPreference(Params.LOCAL.SCHOOLID);

		setCatSpinner();
	}

	private void setCatSpinner() {
		// sorts = PuApp.get().getLocalDataMgr().getCourseCats();

		if (null != sorts) {
			// 将可选内容与ArrayAdapter连接起来
			sortAdapter = new ArrayAdapter<CourseCat>(CourseSearchActivity.this,
					R.layout.spinner_item_layout, sorts);
			sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 将adapter 添加到spinner中
			spinner_sort.setAdapter(sortAdapter);

			spinner_sort.setSelection(0);
		}
	}
}
