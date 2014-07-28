package com.xyhui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.types.School;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.utils.Utils;
import com.xyhui.widget.SchoolAdapter;
import com.xyhui.widget.Sidebar;

/**
 * 学校选择
 *
 */
public class SchoolListActivity extends Activity {
	private SchoolAdapter adapter;
	private ArrayList<School> schools = new ArrayList<School>();
	private ListView listView;
	private Sidebar sidebar;
	private Activity mActivity;
	private PrefUtil mPrefUtil;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_school_list);
		mActivity = this;
		
		mPrefUtil = new PrefUtil();
		
		listView = (ListView) findViewById(R.id.list);
		sidebar = (Sidebar) findViewById(R.id.sidebar);
		sidebar.setListView(listView);
		schools = new ArrayList<School>();

		getSchoolList();
		//设置adapter
		adapter = new SchoolAdapter(this, R.layout.row_school, schools, sidebar);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				School school = adapter.getItem(position);

				VolleyLog.d("onclick: %s", school.name);

				mPrefUtil.setPreference(Params.LOCAL.SCHOOLID, school.school);
				mPrefUtil.setPreference(Params.LOCAL.SCHOOLNAME, school.name);
				mPrefUtil.setPreference(Params.LOCAL.SCHOOLEMAIL, school.email);

				Intent intent = new Intent().setAction("android.user.SCHOOL");
				intent.putExtra(Params.INTENT_EXTRA.SCHOOL, school);
				mActivity.sendBroadcast(intent);

				((Activity) mActivity).finish();
			}
		});
	}
	
	private void getSchoolList() {
		schools.clear();
		schools = PuApp.get().getLocalDataMgr().getSchools();
		
		if (schools != null && !schools.isEmpty()) {
			schools.remove(0);

//			String cityId = new PrefUtil().getPreference(Params.LOCAL.CITYID);

//			for (int i = 0; i < tempschools.size(); i++) {
//				School school = tempschools.get(i);
//
//				if (cityId.equals(school.cityId)) {
//					schools.add(school);
//				}
//			}
			
			//排序
			Collections.sort(schools, new Comparator<School>() {

				@Override
				public int compare(School lhs, School rhs) {
					return Utils.getHeader(lhs.display_order).compareTo(Utils.getHeader(rhs.display_order));
				}
			});
		}
	}
}
