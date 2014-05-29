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
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CActivity;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.Response;
import com.xyhui.utils.Params;

public class DonateList extends CListView {
	public static final int DONATE_ALL = 0;
	public static final int DONATE_MINE = 1;
	public static final int DONATE_SHOPPING = 2;

	private int mDonateType = DONATE_ALL;
	private String mProvinceId;
	private String mCityId;
	private String mSchoolId;
	private String mDepartId;
	private String mSortId;
	private String mPrice;

	public DonateList(PullToRefreshListView lv, Activity activity, int type) {
		super(lv, activity);
		mDonateType = type;
		initListViewStart();
	}

	public DonateList(PullToRefreshListView lv, Activity activity, int type, String pid,
			String cityid, String schoolid, String departid, String sortid, String price) {
		// 在父类将执行 initListItemResource();ensureUi();
		super(lv, activity);

		mDonateType = type;
		mProvinceId = pid;
		mCityId = cityid;
		mSchoolId = schoolid;
		mDepartId = departid;
		mSortId = sortid;
		mPrice = price;

		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.list_item_donate);
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
				Donate item = (Donate) mListItems.get(i);
				// 打开捐赠物品详情页
				Intent intent = new Intent(mActivity, DonateViewActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.DONATE_ID, item.id);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		final Donate item = (Donate) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar, R.drawable.img_default,
				true);
		avatarLVP.setImgAsync(true);
		avatarLVP.setItemTag(item.pic);
		LVP.add(avatarLVP);

		LVP.add(new CListViewParam(R.id.text_title, item.title, true));
		LVP.add(new CListViewParam(R.id.text_cost, String.format("价格:%s", item.price), true));

		CListViewParam collectLVP;
		if (mDonateType == DONATE_ALL) {
			collectLVP = new CListViewParam(R.id.btn_collect, "购买", true);
		} else {
			collectLVP = new CListViewParam(R.id.btn_collect, "购买", false);
		}
		collectLVP.setOnclickLinstener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Button btn = (Button) v;
				btn.setEnabled(false);

				CallBack collectCB = new CallBack() {
					@Override
					public void onSuccess(String response) {
						((CActivity) mActivity).dismissProgress();
						Response r = JSONUtils.fromJson(response, Response.class);
						if (1 == r.status) {
							NotificationsUtil.ToastBottomMsg(mActivity, "购买成功");
						} else if (0 == r.status && !TextUtils.isEmpty(r.info)) {
							NotificationsUtil.ToastBottomMsg(mActivity, r.info);
						}
					}

					@Override
					public void onFailure(String message) {
						super.onFailure(message);
						((CActivity) mActivity).dismissProgress();
						NotificationsUtil.ToastBottomMsg(mActivity, "购买失败");
						btn.setEnabled(true);
					}
				};

				((CActivity) mActivity).showProgress();
				new Api(collectCB, mActivity).payment(PuApp.get().getToken(), item.id);
			}
		});
		LVP.add(collectLVP);

		return LVP;
	}

	@Override
	public void asyncData() {
		super.asyncData();
		if (DONATE_SHOPPING == mDonateType) {
			new Api(callback, mActivity).myDonateBuyer(PuApp.get().getToken());
		} else if (DONATE_MINE == mDonateType) {
			new Api(callback, mActivity).myDonate(PuApp.get().getToken());
		} else {
			new Api(callback, mActivity).donateList(PuApp.get().getToken(), mProvinceId, mCityId,
					mSchoolId, mDepartId, mSortId, mPrice, page, mPerpage);
		}
	}

	ListCallBack<ArrayList<Donate>> callback = new ListCallBack<ArrayList<Donate>>(DonateList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<Donate>>() {
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

	public void search(String pid, String cityid, String schoolid, String departid, String sortid,
			String price) {
		mProvinceId = pid;
		mCityId = cityid;
		mSchoolId = schoolid;
		mDepartId = departid;
		mSortId = sortid;
		mPrice = price;
		initListViewStart();
	}

	private static class Donate {
		String id;
		String title;
		String pic;
		String price;
	}
}
