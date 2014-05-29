package com.xyhui.activity.weibo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.VolleyLog;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.Weibo;
import com.xyhui.types.WeiboComment;
import com.xyhui.utils.Params;

public class CommentList extends CListView {
	public CommentList(PullToRefreshListView lv, Activity activity) {
		super(lv, activity);

		// 在父类将执行 initListItemResource();ensureUi();
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_comments);
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;
		super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
				"查看更多评论");
		super.ensureUi();

		super.setGetMoreClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getmoreListViewStart();
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		final WeiboComment weibo = (WeiboComment) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar, R.drawable.avatar00, true);
		avatarLVP.setImgAsync(true);
		avatarLVP.setItemTag(weibo.user.face);
		avatarLVP.setOnclickLinstener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开个人主页
				Intent intent = new Intent();
				intent.setClass(mActivity, UserHomePageActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.USER_ID, weibo.uid);
				mActivity.startActivity(intent);
			}
		});
		LVP.add(avatarLVP);

		LVP.add(new CListViewParam(R.id.text_nickname, weibo.uname, true));
		VolleyLog.d("content:%s", weibo.content);
		LVP.add(new CListViewParam(R.id.text_content, weibo.content, true));

		if (weibo.status instanceof JsonObject) {
			Weibo transpond_data = JSONUtils.fromJson(weibo.status, Weibo.class);
			VolleyLog.d("jsonobject,transpond_data=%s", null == transpond_data ? "null"
					: transpond_data.content);
			LVP.add(new CListViewParam(R.id.text_forward, transpond_data, true));
		} else {
			LVP.add(new CListViewParam(R.id.text_forward, null, false));
		}

		LVP.add(new CListViewParam(R.id.text_datefrom, weibo.ctime, true));
		return LVP;
	}

	ListCallBack<ArrayList<WeiboComment>> callback = new ListCallBack<ArrayList<WeiboComment>>(
			CommentList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<WeiboComment>>() {
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
	};

	@Override
	public void asyncData() {
		super.asyncData();
		new Api(callback, mActivity).comments_receive_me(PuApp.get().getToken(), mPerpage, page);
	}

}
