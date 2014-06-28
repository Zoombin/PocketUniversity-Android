package com.xyhui.activity.weibo;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.ListCallBack;
import com.xyhui.types.SystemMsg;
import com.xyhui.types.SystemMsgs;
import com.xyhui.utils.SpanUtils;

public class SystemMsgList extends CListView {
	public SystemMsgList(PullToRefreshListView lv, Activity activity) {
		super(lv, activity);
		initListViewStart();
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_message);
	}

	@Override
	public void ensureUi() {
		mPerpage = 10;
		super.setGetMoreResource(R.layout.list_item_getmore,
				R.id.list_item_getmore_title, "查看更多通知");
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

		final SystemMsg item = (SystemMsg) obj;

		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		CListViewParam avatarLVP = new CListViewParam(R.id.img_avatar,
				R.drawable.avatar00, true);
		if (!TextUtils.isEmpty(item.face)) {
			avatarLVP.setImgAsync(true);
			avatarLVP.setItemTag(item.face);
		}
		LVP.add(avatarLVP);

		LVP.add(new CListViewParam(R.id.img_new_message,
				R.drawable.icon_new_message, false));

		LVP.add(new CListViewParam(R.id.text_nickname, item.title, true));
		LVP.add(new CListViewParam(R.id.text_date, getTime(item.ctime), true));
		SpannableString spanStr = SpanUtils.txtToImg(mActivity, item.body);
		LVP.add(new CListViewParam(R.id.text_message, spanStr, true));

		return LVP;
	}

	private String getTime(String time) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = new Date(Long.parseLong(time +"000"));
			return sdf.format(date);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	ListCallBack<SystemMsgs> callback = new ListCallBack<SystemMsgs>(
			SystemMsgList.this) {
		@Override
		public void setType() {
			myType = new TypeToken<SystemMsgs>() {
			}.getType();
		}

		@Override
		public void addItems() {
			if (mT != null && mT.data != null && !mT.data.isEmpty()) {
				for (int i = 0; i < mT.data.size(); i++) {
					mListItems.add(mT.data.get(i));
				}
			}
		}
	};

	@Override
	public void asyncData() {
		super.asyncData();
		new Api(callback, mActivity).systemMSG(PuApp.get().getToken(),
				mPerpage, page);
	}
}
