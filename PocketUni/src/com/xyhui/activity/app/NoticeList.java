package com.xyhui.activity.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.VolleyLog;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.Announce;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;

public class NoticeList extends CListView {

	/**
	 * 校内通知
	 */
	public static final int NOTICE_SCHOOL = 0;

	/**
	 * 官方公告
	 */
	public static final int NOTICE_OFFICIAL = 1;

	private int mCatId;
	private String mSchoolId;
	private String timeStr;
	private int mNoticeType;
	private PrefUtil mPrefUtil;

	public NoticeList(PullToRefreshListView lv, Activity activity, int cid, long stime,
			int noticeType) {
		// 在父类将执行 initListItemResource();ensureUi();
		super(lv, activity);

		mSchoolId = new PrefUtil().getPreference(Params.LOCAL.SCHOOLID);
		// 默认为苏州大学
		if (TextUtils.isEmpty(mSchoolId)) {
			mSchoolId = "1";
		}

		refresh(noticeType, cid, stime);
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_notice);
	}

	public void refresh(int noticeType, int cid, long stime) {
		try {
			timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(stime * 1000));
			VolleyLog.d("timeStr=%s", timeStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		mNoticeType = noticeType;
		mCatId = cid;
		initListViewStart();
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;

		super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
				"查看更多通知");
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
				// 暂无操作

				int i = (Integer) v.getTag();

				if (i >= mListItems.size())
					return;

				Announce item = (Announce) mListItems.get(i);

				Intent intent = new Intent();
				intent.setClass(mActivity, NoticeViewActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.ANNOUNCE_ID, item.id);
				intent.putExtra(Params.INTENT_EXTRA.ANNOUNCE_TYPE, mNoticeType);
				mActivity.startActivity(intent);
			}
		});

		mPrefUtil = new PrefUtil();
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		Announce item = (Announce) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		boolean isNew = timeStr.compareTo(item.time) <= 0;
		// 检测是否是新的校内通知
		if (isNew) {
			LVP.add(new CListViewParam(R.id.text_title, "", false));
			LVP.add(new CListViewParam(R.id.text_title_red, item.title, true));
		} else {
			LVP.add(new CListViewParam(R.id.text_title, item.title, true));
			LVP.add(new CListViewParam(R.id.text_title_red, "", false));
		}
		LVP.add(new CListViewParam(R.id.text_date, item.time, true));
		return LVP;
	}

	ListCallBack<ArrayList<Announce>> callback = new ListCallBack<ArrayList<Announce>>(
			NoticeList.this) {
		@Override
		public String preProcess(String response) {
			if (!TextUtils.isEmpty(response) && page == 1) {
				if (NOTICE_SCHOOL == mNoticeType) {
					mPrefUtil.setPreference(Params.LOCAL.NOTICE_SCHOOL_LIST + mCatId, response);
				} else {
					mPrefUtil.setPreference(Params.LOCAL.NOTICE_OFFICIAL_LIST + mCatId, response);
				}
			}

			return response;
		}

		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<Announce>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}
			}
		}

		@Override
		public void onFailure(String message) {

			if (page == 1) {
				if (NOTICE_SCHOOL == mNoticeType) {
					onSuccess(mPrefUtil.getPreference(Params.LOCAL.NOTICE_SCHOOL_LIST + mCatId));
					VolleyLog.d(mPrefUtil.getPreference(Params.LOCAL.NOTICE_SCHOOL_LIST + mCatId));
				} else {
					onSuccess(mPrefUtil.getPreference(Params.LOCAL.NOTICE_OFFICIAL_LIST + mCatId));
					VolleyLog.d(mPrefUtil
							.getPreference(Params.LOCAL.NOTICE_OFFICIAL_LIST + mCatId));
				}
			} else {
				super.onFailure(message);
			}
		}
	};

	@Override
	public void asyncData() {
		super.asyncData();

		if (NOTICE_SCHOOL == mNoticeType) {
			int offset = (page - 1) * mPerpage;
			if (offset < 0)
				offset = 0;
			new Api(callback, mActivity).getAnnounceList(PuApp.get().getToken(), mSchoolId,
					mCatId, mPerpage, offset);
		} else if (NOTICE_OFFICIAL == mNoticeType) {
			new Api(callback, mActivity).getNoticeList(PuApp.get().getToken(), mCatId, mPerpage,
					page);
		}
	}
}
