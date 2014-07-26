package com.xyhui.activity.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.types.School;
import com.xyhui.utils.Utils;
import com.xyhui.widget.SchoolAdapter;
import com.xyhui.widget.Sidebar;

/**
 * 联系人列表页
 *
 */
public class SchoolListActivity extends Activity {
	private SchoolAdapter adapter;
	private ArrayList<School> schools;
	private ListView listView;
	private Sidebar sidebar;
	private Activity mActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school_list);
		mActivity = this;
		
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
				String schoolid = adapter.getItem(position).id;
				
			}
		});
	}
	
	private void getSchoolList(){
		schools.clear();
		schools = PuApp.get().getLocalDataMgr().getSchools();
		//排序
		Collections.sort(schools, new Comparator<School>() {

			@Override
			public int compare(School lhs, School rhs) {
				return Utils.getHeader(lhs.display_order).compareTo(Utils.getHeader(rhs.display_order));
			}
		});
	}
}
