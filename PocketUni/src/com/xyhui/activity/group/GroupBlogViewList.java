package com.xyhui.activity.group;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.activity.weibo.UserHomePageActivity;
import com.xyhui.api.Api;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.Topic;
import com.xyhui.utils.Params;
import com.xyhui.utils.SpanUtils;

public class GroupBlogViewList extends CListView {

	/**
	 * 由于每次都会从服务器获取主题，所以要获取11个item，才会包含10个回复
	 */
	private final int ITEM_PER_PAGE = 11;

	private String mThreadId;

	public GroupBlogViewList(PullToRefreshListView lv, Activity activity, String thread_id) {
		super(lv, activity);
		mThreadId = thread_id;
		// 在父类将执行 initListItemResource();ensureUi();
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_group_blog_view_reply);
	}

	public void refresh() {
		refreshListViewStart();
	}

	public void search(String key) {
		refreshListViewStart();
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;
		super.setHeaderResource(R.layout.list_item_group_blog_view_header);
		super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
				"查看更多回复");
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

		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		if (index == 0) {
			final Topic.Thread item = (Topic.Thread) obj;

			CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar, R.drawable.avatar00,
					true);
			if (!TextUtils.isEmpty(item.face)) {
				avatarLVP.setImgAsync(true);
				avatarLVP.setItemTag(item.face);
			}
			avatarLVP.setOnclickLinstener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 打开个人主页
					Intent intent = new Intent();
					intent.setClass(mActivity, UserHomePageActivity.class);
					intent.putExtra(Params.INTENT_EXTRA.USER_ID, item.uid);
					mActivity.startActivity(intent);
				}
			});
			LVP.add(avatarLVP);

			LVP.add(new CListViewParam(R.id.text_title, item.title, true));
			LVP.add(new CListViewParam(R.id.text_nickname, item.name, true));
			LVP.add(new CListViewParam(R.id.text_info, SpanUtils.getTimeString(item.addtime), true));
			LVP.add(new CListViewParam(R.id.text_content, item.content, true));
		} else {
			if (obj == null) {
				LVP.add(new CListViewParam(R.id.text_nickname, null, false));
				LVP.add(new CListViewParam(R.id.text_content, null, false));
				LVP.add(new CListViewParam(R.id.text_date, null, false));
				LVP.add(new CListViewParam(R.id.text_empty_tips, null, true));
			} else {
				Topic.Reply item = (Topic.Reply) obj;
				LVP.add(new CListViewParam(R.id.text_nickname, item.name, true));
				LVP.add(new CListViewParam(R.id.text_content, item.content, true));
				LVP.add(new CListViewParam(R.id.text_date, SpanUtils.getTimeString(item.ctime),
						true));
				LVP.add(new CListViewParam(R.id.text_empty_tips, null, false));
			}
		}
		return LVP;
	}

	ListCallBack<Topic> callback = new ListCallBack<Topic>(GroupBlogViewList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<Topic>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (GETMORE != actionType) {
				mListItems.add(mT.topic);
			}

			if (mT != null && mT.reply != null && mT.reply.size() > 1) {
				for (int i = 1; i < mT.reply.size(); i++) {
					mListItems.add(mT.reply.get(i));
				}
			} else if (GETMORE != actionType) {
				// reply empty
				mListItems.add(null);
			}

		}
	};

	@Override
	public void asyncData() {
		super.asyncData();
		new Api(callback, mActivity).viewtopic(PuApp.get().getToken(), mThreadId, ITEM_PER_PAGE,
				page);
	}
}
