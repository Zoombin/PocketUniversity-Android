package com.xyhui.activity.group;

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
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.types.Banner;
import com.xyhui.types.GroupCats;
import com.xyhui.types.KeyValuePair;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.AdBannerLayout;
import com.xyhui.widget.FLActivity;

public class GroupListActivity extends FLActivity {
	private final int CAT_NUM = 4;

	private final String CUPID = "13";
	private final String cityId = new PrefUtil().getPreference(Params.LOCAL.CITYID);

	private Button btn_back;
	private Button btn_search;

	private Button btn_hot_group;
	private Button btn_star_group;
	private Button btn_my_group;

	private PullToRefreshListView hot_group_listview;
	private PullToRefreshListView my_star_listview;
	private PullToRefreshListView my_group_listview;
	private GroupList mGroupListView_hot;
	private GroupList mGroupListView_star;
	private GroupList mGroupListView_my;

	private LinearLayout schoolbar;

	private GroupCats groupCats;

	private AdBannerLayout ad_banner;

	@SuppressWarnings("unchecked")
	private ArrayAdapter<KeyValuePair>[] adapters = (ArrayAdapter<KeyValuePair>[]) new ArrayAdapter<?>[CAT_NUM];

	private ArrayList<KeyValuePair>[] data;

	private Spinner[] spinners = new Spinner[CAT_NUM];

	private String[] ids = new String[CAT_NUM];

	private final int CUP_LAY = -1;
	private final int INIT_LAY = 0;
	private final int DPART_ONE_LAY = 1;
	private final int DPART_TWO_LAY = 2;
	private final int DPART_THREE_LAY = 3;

	private int layId = 0;

	private SpinnerListener spinnerListener = new SpinnerListener();

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_group_list);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_search = (Button) findViewById(R.id.btn_search);

		btn_hot_group = (Button) findViewById(R.id.btn_hot_group);
		btn_star_group = (Button) findViewById(R.id.btn_star_group);
		btn_my_group = (Button) findViewById(R.id.btn_my_group);

		hot_group_listview = (PullToRefreshListView) findViewById(R.id.hot_group_listview);
		my_star_listview = (PullToRefreshListView) findViewById(R.id.my_star_listview);
		my_group_listview = (PullToRefreshListView) findViewById(R.id.my_group_listview);

		ad_banner = (AdBannerLayout) findViewById(R.id.ad_banner);

		schoolbar = (LinearLayout) findViewById(R.id.spinnerbar);

		spinners[0] = (Spinner) findViewById(R.id.spinner_group_dpart);
		spinners[1] = (Spinner) findViewById(R.id.spinner_group_school);
		spinners[2] = (Spinner) findViewById(R.id.spinner_group_year);
		spinners[3] = (Spinner) findViewById(R.id.spinner_group_category);
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

		btn_hot_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btn_hot_group.isSelected() == false) {
					btn_hot_group.setSelected(true);
					btn_star_group.setSelected(false);
					btn_my_group.setSelected(false);

					hot_group_listview.setVisibility(View.VISIBLE);
					my_star_listview.setVisibility(View.GONE);
					my_group_listview.setVisibility(View.GONE);

					schoolbar.setVisibility(View.VISIBLE);
				}
			}
		});

		btn_star_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btn_star_group.isSelected() == false) {
					btn_hot_group.setSelected(false);
					btn_star_group.setSelected(true);
					btn_my_group.setSelected(false);

					hot_group_listview.setVisibility(View.GONE);
					my_star_listview.setVisibility(View.VISIBLE);
					my_group_listview.setVisibility(View.GONE);

					schoolbar.setVisibility(View.VISIBLE);
					
					if (mGroupListView_star == null) {
						mGroupListView_star = new GroupList(my_star_listview, mActivity,
								GroupList.GROUP_STAR);
					}
				}
			}
		});
		
		btn_my_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btn_my_group.isSelected() == false) {
					btn_hot_group.setSelected(false);
					btn_star_group.setSelected(false);
					btn_my_group.setSelected(true);

					hot_group_listview.setVisibility(View.GONE);
					my_star_listview.setVisibility(View.GONE);
					my_group_listview.setVisibility(View.VISIBLE);

					schoolbar.setVisibility(View.GONE);

					if (mGroupListView_my == null) {
						mGroupListView_my = new GroupList(my_group_listview, mActivity,
								GroupList.GROUP_MY);
					}
				}
			}
		});

		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开搜索部落
				Intent intent = new Intent(mActivity, GroupSearchActivity.class);
				startActivity(intent);
			}
		});
	}

	private void spinnerLayout() {
		switch (layId) {
		case CUP_LAY:
			spinners[0].setVisibility(View.VISIBLE);
			spinners[1].setVisibility(View.VISIBLE);
			spinners[2].setVisibility(View.INVISIBLE);
			spinners[3].setVisibility(View.GONE);
			break;
		case INIT_LAY:
		case DPART_ONE_LAY:
			spinners[2].setVisibility(View.INVISIBLE);
			spinners[3].setVisibility(View.GONE);
			break;
		case DPART_TWO_LAY:
			spinners[2].setVisibility(View.VISIBLE);
			spinners[3].setVisibility(View.GONE);
			break;
		case DPART_THREE_LAY:
			spinners[2].setVisibility(View.GONE);
			spinners[3].setVisibility(View.VISIBLE);
			break;
		}
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
		ad_banner.init(Banner.TYPE_GROUP);

		/*btn_hot_group.setSelected(true);
		hot_group_listview.setVisibility(View.VISIBLE);
		my_star_listview.setVisibility(View.GONE);
		my_group_listview.setVisibility(View.GONE);*/
		
		setTitle();
		setSpinner();
		
		//先显示我的部落
		btn_my_group.setSelected(true);

		hot_group_listview.setVisibility(View.GONE);
		my_star_listview.setVisibility(View.GONE);
		my_group_listview.setVisibility(View.VISIBLE);

		schoolbar.setVisibility(View.GONE);
		if (mGroupListView_my == null)
			mGroupListView_my = new GroupList(my_group_listview, mActivity,
					GroupList.GROUP_MY);
		
		
	}

	private void setTitle() {
		if (cityId.equals(CUPID)) {
			btn_hot_group.setText("活跃项目");
			btn_star_group.setText("星级项目");
			btn_my_group.setText("我的项目");
		} else {
			btn_hot_group.setText("活跃部落");
			btn_star_group.setText("星级部落");
			btn_my_group.setText("我的部落");
		}
	}

	private void setSpinner() {
		setData();

		if (null != data) {
			for (int i = 0; i < CAT_NUM; i++) {
				adapters[i] = new ArrayAdapter<KeyValuePair>(mActivity,
						R.layout.spinner_item_layout, data[i]);

				adapters[i].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// 将adapter 添加到spinner中
				spinners[i].setAdapter(adapters[i]);

				if (i == 0) {
					spinners[0].setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position,
								long id) {
							ArrayList<KeyValuePair> dpartList = groupCats.dpart;
							if (dpartList != null && position < dpartList.size()) {
								if (cityId.equals(CUPID)) {
									layId = CUP_LAY;
								} else {
									layId = Integer.parseInt(dpartList.get(position).id);
								}
								spinnerLayout();
							}
							search();
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {

						}
					});
				} else {
					spinners[i].setOnItemSelectedListener(spinnerListener);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void setData() {
		groupCats = PuApp.get().getLocalDataMgr().getGroupCats();

		ArrayList<KeyValuePair> schoolList = groupCats.school;
		ArrayList<KeyValuePair> dpartList = groupCats.dpart;

		if (cityId.equals(CUPID)) {
			schoolList.remove(0);
			schoolList.add(0, new KeyValuePair("0", "选择分类"));

			dpartList.remove(3);
			dpartList.remove(2);
			dpartList.remove(1);
			dpartList.add(1, new KeyValuePair("1", "哲学社科"));
			dpartList.add(2, new KeyValuePair("2", "科学发明制作"));
			dpartList.add(3, new KeyValuePair("3", "自然科学"));
		}

		ArrayList<String> years = groupCats.year;
		ArrayList<KeyValuePair> yearList = new ArrayList<KeyValuePair>();
		yearList.add(0, new KeyValuePair("0", years.get(0)));
		for (int i = 1; i < years.size(); i++) {
			String year = years.get(i);
			KeyValuePair kv = new KeyValuePair(year, year + "级");
			yearList.add(i, kv);
		}

		data = (ArrayList<KeyValuePair>[]) new ArrayList<?>[] { dpartList, schoolList, yearList,
				groupCats.category };
	}

	void search() {
		for (int i = 0; i < CAT_NUM; i++) {
			int pos = spinners[i].getSelectedItemPosition();
			if (data[i] != null && pos < data[i].size()) {
				ids[i] = data[i].get(pos).id;
			}
		}

		if (mGroupListView_hot != null) {
			mGroupListView_hot.getHotGroup(ids);
		} else {
			mGroupListView_hot = new GroupList(hot_group_listview, mActivity, GroupList.GROUP_HOT);
		}
	}

	private class SpinnerListener implements OnItemSelectedListener {
		// 防止多个spinner的监听器同时执行
		int count = 0;

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			if (++count > 2) {
				VolleyLog.d("SpinnerTest %s", parent.toString());
				search();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}
}
