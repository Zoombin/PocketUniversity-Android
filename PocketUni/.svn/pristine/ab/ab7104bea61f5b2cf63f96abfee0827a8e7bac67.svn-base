package com.xyhui.activity.weibo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
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
import com.xyhui.types.Follower;
import com.xyhui.types.User;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;

public class UserList extends CListView {
	public static final int USER_FOLLOWS = 1;
	public static final int USER_FOLLOWED = 2;
	public static final int USER_SEARCH = 3;
	public static final int USER_RECOMMEND = 4;

	private int mUserType = USER_FOLLOWS;
	private String mKeyword;

	private String mSchoolId;
	private String mDpartId;
	private String mUid;

	public UserList(PullToRefreshListView lv, Activity activity, int userType, String keyword,
			String uid) {
		// 在父类将执行 initListItemResource();ensureUi();
		super(lv, activity);
		mUserType = userType;
		mKeyword = keyword;

		if (uid == null) {
			mUid = new PrefUtil().getPreference(Params.LOCAL.UID);
		} else {
			mUid = uid;
		}

		if (USER_SEARCH != userType && USER_RECOMMEND != userType) {
			initListViewStart();
		}
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_user);
	}

	public void search(String sid, String dpartId, String keyword) {
		mSchoolId = sid;
		mDpartId = dpartId;
		mKeyword = keyword;
		mUserType = USER_SEARCH;

		refreshListViewStart();
	}

	public void recommend(String sid, String dpartId) {
		mSchoolId = sid;
		mDpartId = dpartId;
		mUserType = USER_RECOMMEND;

		refreshListViewStart();
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;

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
				// 打开个人主页
				int i = (Integer) v.getTag();
				User item = (User) mListItems.get(i);

				Intent intent = new Intent();
				intent.setClass(mActivity, UserHomePageActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.USER_ID, item.uid);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		User user = (User) obj;

		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar, R.drawable.avatar00, true);
		if (!TextUtils.isEmpty(user.face)) {
			avatarLVP.setImgAsync(true);
			avatarLVP.setItemTag(user.face);
		}
		LVP.add(avatarLVP);

		LVP.add(new CListViewParam(R.id.text_nickname, user.uname, true));
		LVP.add(new CListViewParam(R.id.text_school, user.school, true));
		LVP.add(new CListViewParam(R.id.text_params, user.getInfo(), true));

		return LVP;
	}

	ListCallBack<ArrayList<User>> userSearchCB = new ListCallBack<ArrayList<User>>(UserList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<User>>() {
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

	ListCallBack<ArrayList<Follower>> userFollowCB = new ListCallBack<ArrayList<Follower>>(
			UserList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<Follower>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i).user);
				}
			} else {
				NotificationsUtil.ToastTopMsg(mActivity, "没有搜索到相关信息");
				return;
			}
		}
	};

	@Override
	public void asyncData() {
		super.asyncData();

		if (mUserType == USER_RECOMMEND) {
			mAdapter.setGetMoreResource(0);
		} else {
			super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
					"查看更多结果");
			mAdapter.setGetMoreResource(R.layout.list_item_getmore);
		}

		if (mUserType == USER_FOLLOWED) {
			new Api(userFollowCB, mActivity).following(PuApp.get().getToken(), mUid, mPerpage,
					page);
		} else if (mUserType == USER_FOLLOWS) {
			new Api(userFollowCB, mActivity).followers(PuApp.get().getToken(), mUid, mPerpage,
					page);
		} else if (mUserType == USER_SEARCH) {
			new Api(userSearchCB, mActivity).searchuser(PuApp.get().getToken(), mSchoolId,
					mDpartId, mKeyword, mPerpage, page);
		} else if (mUserType == USER_RECOMMEND) {
			new Api(userSearchCB, mActivity).recommendUser(PuApp.get().getToken(), mSchoolId,
					mDpartId);
		}
	}
}
