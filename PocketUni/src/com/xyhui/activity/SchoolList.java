package com.xyhui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.mslibs.utils.VolleyLog;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.types.School;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;

public class SchoolList extends CListView {
	private PrefUtil mPrefUtil;

	public SchoolList(ListView lv, Activity activity) {
		// 在父类将执行 initListItemResource();ensureUi();
		super(lv, activity);

		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.list_item_school);
	}

	@Override
	public void ensureUi() {
		mPrefUtil = new PrefUtil();

		mPerpage = 10000;
		super.setFooterResource(R.layout.list_item_school_bottom);
		super.ensureUi();

		super.setGetMoreClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getmoreListViewStart();
			}
		});

		super.setItemOnclickLinstener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				int i = (Integer) v.getTag();

				School school = (School) mListItems.get(i);

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

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		School school = (School) obj;
		String title = school.name;
		LVP.add(new CListViewParam(R.id.school_title, title, true));

		return LVP;
	}

	private void customSchoolList() {

		ArrayList<School> schools = PuApp.get().getLocalDataMgr().getSchools();

		if (schools != null && !schools.isEmpty()) {
			schools.remove(0);

			mListItems.clear();
			mDateList.clear();

			String cityId = new PrefUtil().getPreference(Params.LOCAL.CITYID);

			for (int i = 0; i < schools.size(); i++) {
				School school = schools.get(i);

				if (cityId.equals(school.cityId)) {
					mListItems.add(school);
				}
			}

			initListViewFinish();
			actionType = IDLE;
		}
	}

	@Override
	public void asyncData() {
		customSchoolList();
	}
}
