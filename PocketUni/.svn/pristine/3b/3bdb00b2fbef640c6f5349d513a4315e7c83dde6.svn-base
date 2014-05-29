package com.xyhui.activity.weibo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.VolleyLog;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.ImageZoomActivity;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.Comment;
import com.xyhui.types.Weibo;
import com.xyhui.types.WeiboTypeData;
import com.xyhui.utils.Params;

public class WeiboViewList extends CListView {
	private Weibo mWeibo;
	private Weibo mTranspondData;

	public WeiboViewList(PullToRefreshListView lv, Activity activity, Weibo weibo,
			Weibo transpond_data) {
		super(lv, activity);

		mWeibo = weibo;
		mTranspondData = transpond_data;
		// 在父类将执行 initListItemResource();ensureUi();
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_weibo_view_reply);
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
		super.setHeaderResource(R.layout.list_item_weibo_view_header);
		super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
				"查看更多评论");
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
				// 暂无
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		if (index == 0) {
			CListViewParam iLVP = new CListViewParam(R.id.layout_user, "", true);
			iLVP.setOnclickLinstener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 打开个人主页
					Intent intent = new Intent();
					intent.setClass(mActivity, UserHomePageActivity.class);
					intent.putExtra(Params.INTENT_EXTRA.USER_ID, mWeibo.uid);
					mActivity.startActivity(intent);

				}
			});
			LVP.add(iLVP);

			CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar, R.drawable.avatar00,
					true);
			avatarLVP.setImgAsync(true);
			avatarLVP.setItemTag(mWeibo.face);
			LVP.add(avatarLVP);
			LVP.add(new CListViewParam(R.id.text_nickname, mWeibo.uname, true));
			LVP.add(new CListViewParam(R.id.text_content, mWeibo.content, true));

			final WeiboTypeData img = mWeibo.type_data;
			if (img == null) {
				LVP.add(new CListViewParam(R.id.img_weibo, null, false));
			} else {
				img.fix();
				LVP.add(new CListViewParam(R.id.img_weibo, R.drawable.img_default, true));

				CListViewParam imgweiboLVP = new CListViewParam(R.id.img_weibo,
						R.drawable.img_default, true);
				imgweiboLVP.setImgAsync(true);
				imgweiboLVP.setItemTag(img.thumburl);
				imgweiboLVP.setOnclickLinstener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 放大图片浏览
						// Intent intent = new Intent();
						// intent.setClass(mActivity, WebViewActivity.class);
						// intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_TITLE, "图片浏览");
						// intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_URL, img.thumbmiddleurl);
						// intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_TYPE,
						// WebViewActivity.TYPE_IMAGE);
						// mActivity.startActivity(intent);

						Intent intent = new Intent(mActivity, ImageZoomActivity.class);
						intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_URL, img.thumbmiddleurl);
						mActivity.startActivity(intent);
					}
				});
				LVP.add(imgweiboLVP);
			}

			if (mTranspondData != null) {
				VolleyLog.d("got WEIBO_FORWARD: %s", mTranspondData.content);
				LVP.add(new CListViewParam(R.id.text_forward, mTranspondData, true));
			} else {
				VolleyLog.d("no WEIBO_FORWARD");
				LVP.add(new CListViewParam(R.id.text_forward, null, false));
			}

			LVP.add(new CListViewParam(R.id.text_datefrom, mWeibo.ctime, true));
			String zan = mWeibo.is_hearted == 1 ? "已赞" : "赞";
			LVP.add(new CListViewParam(R.id.text_params, String.format("转发(%s) |评论(%s) | %s(%s) ",
					mWeibo.transpond, mWeibo.comment, zan, mWeibo.heart), true));
		} else {
			CListViewParam iLVP = new CListViewParam(R.id.item_reply, "", true);

			if (obj == null) {
				LVP.add(new CListViewParam(R.id.text_nickname, null, false));
				LVP.add(new CListViewParam(R.id.text_content, null, false));
				LVP.add(new CListViewParam(R.id.text_date, null, false));
				LVP.add(new CListViewParam(R.id.text_empty_tips, null, true));

				iLVP.setOnclickLinstener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 打开评论
						Intent intent = new Intent();
						intent.setClass(mActivity, WeiboEditActivity.class);
						intent.putExtra(Params.INTENT_EXTRA.WEIBO_EDIT,
								Params.INTENT_VALUE.WEIBO_REPLY);
						intent.putExtra(Params.INTENT_EXTRA.WEIBO_ID, mWeibo.weibo_id);
						mActivity.startActivity(intent);
					}
				});
			} else {
				final Comment comment = (Comment) obj;
				LVP.add(new CListViewParam(R.id.text_nickname, comment.uname, true));
				LVP.add(new CListViewParam(R.id.text_content, comment.content, true));
				LVP.add(new CListViewParam(R.id.text_date, comment.ctime, true));
				LVP.add(new CListViewParam(R.id.text_empty_tips, null, false));

				iLVP.setOnclickLinstener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 打开评论（回复）
						Intent intent = new Intent();
						intent.setClass(mActivity, WeiboEditActivity.class);
						intent.putExtra(Params.INTENT_EXTRA.WEIBO_EDIT,
								Params.INTENT_VALUE.WEIBO_REPLY);
						intent.putExtra(Params.INTENT_EXTRA.WEIBO_ID, mWeibo.weibo_id);
						intent.putExtra(Params.INTENT_EXTRA.USERNAME, comment.uname);
						mActivity.startActivity(intent);
					}
				});
			}

			LVP.add(iLVP);
		}
		return LVP;
	}

	ListCallBack<ArrayList<Comment>> callback = new ListCallBack<ArrayList<Comment>>(
			WeiboViewList.this) {

		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<Comment>>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (GETMORE != actionType) {
				mListItems.add("weiboheader");
			}

			if (mT != null && !mT.isEmpty()) {
				for (int i = 0; i < mT.size(); i++) {
					mListItems.add(mT.get(i));
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

		VolleyLog.d("weibo.weibo_id: %s", mWeibo.weibo_id);
		new Api(callback, mActivity).comments(PuApp.get().getToken(), mWeibo.weibo_id, mPerpage,
				page);
	}
}
