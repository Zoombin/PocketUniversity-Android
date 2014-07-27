package com.xyhui.activity.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.Course;
import com.xyhui.types.Gift;
import com.xyhui.utils.Params;

/**
 * 中奖纪录
 * @author 烨
 *
 */
public class GiftList extends CListView {

	public GiftList(PullToRefreshListView lv, Activity activity) {
		super(lv, activity);
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.list_item_gift);
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;
		super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
				"查看更多");
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
				Gift item = (Gift) mListItems.get(i);

				// 打开课程详情页
				Intent intent = new Intent();
				intent.setClass(mActivity, GiftDetailActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.GIFTID, item.id);

				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		Gift item = (Gift) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		LVP.add(new CListViewParam(R.id.text_title, item.getNotice(), true));
//		LVP.add(new CListViewParam(R.id.text_tips, item.getTips(), true));
		return LVP;
	}

	@Override
	public void asyncData() {
		super.asyncData();
		new Api(callback, mActivity).getGiftList(PuApp.get().getToken(), "", mPerpage, page);
	}

	ListCallBack<ArrayList<Gift>> callback = new ListCallBack<ArrayList<Gift>>(GiftList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<Gift>>() {
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

//	public void search(String schoolId, String sortId, String key) {
//		course_type = COURSE_SEARCH;
//		schoolID = schoolId;
//		sortID = sortId;
//		keyword = key;
//		initListViewStart();
//	}
}
