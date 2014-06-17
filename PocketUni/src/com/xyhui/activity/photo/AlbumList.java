package com.xyhui.activity.photo;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
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
import com.xyhui.types.Album;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;

public class AlbumList extends CListView {

	private String mUserID;

	public AlbumList(PullToRefreshListView lv, Activity activity, String user_id) {
		super(lv, activity);
		mUserID = user_id;

		// 在父类将执行 initListItemResource();ensureUi();
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		setListItemResource(R.layout.list_item_photo_album);
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;
		super.setGetMoreResource(R.layout.list_item_getmore,
				R.id.list_item_getmore_title, "查看更多相册");
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
		final Album album = (Album) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		
		CListViewParam avatarLVP = new CListViewParam(R.id.img_cover, R.drawable.avatar00, true);
		avatarLVP.setImgAsync(true);
		avatarLVP.setItemTag(album.cover);
		LVP.add(avatarLVP);
		CListViewParam layout = new CListViewParam(R.id.layout, null, true);
		layout.setOnclickLinstener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//当前用户
				// 个人首页
				Intent intent = new Intent(mActivity, PhotoListActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.USER_ID, album.userId);
				intent.putExtra(Params.INTENT_EXTRA.PHOTO_PRIVACY, album.privacy);
				intent.putExtra(Params.INTENT_EXTRA.ALBUM_ID, album.id);
				intent.putExtra(Params.INTENT_EXTRA.ALBUM_NAME, album.name);
				mActivity.startActivity(intent);
			}
		});
		LVP.add(layout);
		
		LVP.add(new CListViewParam(R.id.text_albumname, "相册名称:"+album.name, true));
		LVP.add(new CListViewParam(R.id.text_photonum, "图片:"+album.photoCount, true));
		LVP.add(new CListViewParam(R.id.text_updatetime, "更新时间:"+album.mTime, true));
		return LVP;
	}

	ListCallBack<ArrayList<Album>> callback = new ListCallBack<ArrayList<Album>>(
			AlbumList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<ArrayList<Album>>() {
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

		if (mUserID != null) {
			new Api(callback, mActivity).getAlbumList(PuApp.get().getToken(),
					mUserID, page, 10);
		}
	}
}
