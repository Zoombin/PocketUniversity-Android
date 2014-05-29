package com.xyhui.activity.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CActivity;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.BaseApi;
import com.xyhui.api.CallBack;
import com.xyhui.api.ListCallBack;
import com.xyhui.utils.Params;

public class TrainList extends CListView {
	public static final int COURSE_ALL = 0;
	public static final int COURSE_MY_JOIN = 2;

	private int course_type = COURSE_ALL;
	private String mProvinceId;
	private String mCity;
	private String mSortId;

	public TrainList(PullToRefreshListView lv, Activity activity, int type) {
		super(lv, activity);
		course_type = type;
		initListViewStart();
	}

	public TrainList(PullToRefreshListView lv, Activity activity, int type, String pid,
			String city, String sortid) {
		// 在父类将执行 initListItemResource();ensureUi();
		super(lv, activity);

		course_type = type;
		mProvinceId = pid;
		mCity = city;
		mSortId = sortid;

		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.list_item_train_course);
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;
		super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
				"查看更多课程");
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
				Course item = (Course) mListItems.get(i);

				// 打开课程详情页
				Intent intent = new Intent();
				intent.setClass(mActivity, TrainViewActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.COURSEID, item.id);

				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		final Course item = (Course) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar, R.drawable.img_default,
				true);
		avatarLVP.setImgAsync(true);
		avatarLVP.setItemTag(item.orgLogo);
		LVP.add(avatarLVP);

		LVP.add(new CListViewParam(R.id.text_title, item.title, true));
		LVP.add(new CListViewParam(R.id.text_hours, String.format("课时:%s", item.dauer), true));
		LVP.add(new CListViewParam(R.id.text_school, String.format("授课学校:%s", item.org), true));
		LVP.add(new CListViewParam(R.id.text_cost, String.format("价格:%s", item.cost), false));

		String strCollect = "收藏";
		if (item.collect != 0) {
			strCollect = "已收藏";
		}
		CListViewParam collectLVP = new CListViewParam(R.id.btn_collect, strCollect, true);
		collectLVP.setOnclickLinstener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final Button btn = (Button) v;

				btn.setEnabled(false);

				CallBack collectCB = new CallBack() {

					@Override
					public void onSuccess(String response) {
						((CActivity) mActivity).dismissProgress();
						btn.setEnabled(true);
						String res = BaseApi.decodeUnicode(response);
						NotificationsUtil.ToastBottomMsg(mActivity,
								res.substring(1, res.length() - 1));
						if (!TextUtils.isEmpty(response)) {
							if (1 == item.collect) {
								item.collect = 0;
								btn.setText("收藏");
							} else {
								item.collect = 1;
								btn.setText("已收藏");
							}
						}
					}

					@Override
					public void onFailure(String message) {
						super.onFailure(message);
						((CActivity) mActivity).dismissProgress();
						btn.setEnabled(true);
					}
				};

				((CActivity) mActivity).showProgress();
				if (1 == item.collect) {
					new Api(collectCB, mActivity).cancelCollect(PuApp.get().getToken(), item.id);
				} else {
					new Api(collectCB, mActivity).collect(PuApp.get().getToken(), item.id);
				}
			}
		});

		LVP.add(collectLVP);

		return LVP;
	}

	@Override
	public void asyncData() {
		super.asyncData();
		if (COURSE_MY_JOIN == course_type) {
			new Api(callback, mActivity).collectList(PuApp.get().getToken(), mPerpage, page);
		} else {
			new Api(callback, mActivity).courseList(PuApp.get().getToken(), mProvinceId, mCity,
					mSortId, mPerpage, page);
		}
	}

	ListCallBack<ArrayList<Course>> callback = new ListCallBack<ArrayList<Course>>(TrainList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<Course>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
				}
			} else {
				NotificationsUtil.ToastTopMsg(mActivity, "没有搜索到相关信息");
				return;
			}
		}
	};

	public void search(String pid, String city, String sortid) {
		mProvinceId = pid;
		mCity = city;
		mSortId = sortid;
		initListViewStart();
	}

	private static class Course {
		String id;
		String title;
		String cost;
		String dauer;
		String org;
		String orgLogo;
		int collect;
	}
}
