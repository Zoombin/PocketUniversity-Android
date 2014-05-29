package com.xyhui.activity.group;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.Follower;
import com.xyhui.types.Response;
import com.xyhui.types.User;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;

public class GroupInviteList extends CListView {
	private String mGid;
	private String mUid;

	public GroupInviteList(PullToRefreshListView lv, Activity activity, String group_id) {

		super(lv, activity);

		mGid = group_id;
		mUid = new PrefUtil().getPreference(Params.LOCAL.UID);

		// 在父类将执行 initListItemResource();ensureUi();
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_invite);
	}

	public void refresh() {
		refreshListViewStart();
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;

		super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
				"查看更多好友");
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
				// 暂无事件
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		Follower item = (Follower) obj;

		User user = item.user;

		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar, R.drawable.avatar00, true);
		if (!TextUtils.isEmpty(user.face)) {
			avatarLVP.setImgAsync(true);
			avatarLVP.setItemTag(user.face);
		}
		LVP.add(avatarLVP);

		LVP.add(new CListViewParam(R.id.text_nickname, user.uname, true));
		LVP.add(new CListViewParam(R.id.text_params, "微博:" + user.weibo_count + "   粉丝:"
				+ user.followers_count + "   关注:" + user.followed_count, true));

		CListViewParam inviteLVP = new CListViewParam(R.id.btn_invite, "邀请", true);
		inviteLVP.setItemTag(user.uid);
		inviteLVP.setOnclickLinstener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 发送邀请
				String user_id = v.getTag().toString();

				final Button btn = (Button) v;

				CallBack inviteCB = new CallBack() {

					@Override
					public void onSuccess(String response) {
						Response r = JSONUtils.fromJson(response, Response.class);
						NotificationsUtil.ToastBottomMsg(mActivity, r.msg);
						if (null != r) {
							if (0 == r.status) {
								btn.setText("邀请");
								btn.setEnabled(true);
							} else if (1 == r.status) {
								btn.setText("已邀请");
								btn.setEnabled(false);
							}
						}
					}
				};

				new Api(inviteCB, mActivity).groupinvite(PuApp.get().getToken(), mGid, user_id);

			}

		});
		LVP.add(inviteLVP);
		return LVP;
	}

	ListCallBack<ArrayList<Follower>> callback = new ListCallBack<ArrayList<Follower>>(
			GroupInviteList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<Follower>>() {
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

	@Override
	public void asyncData() {
		super.asyncData();

		new Api(callback, mActivity).followers(PuApp.get().getToken(), mUid, mPerpage, page);
	}
}
