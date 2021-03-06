package com.xyhui.activity.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.mslibs.widget.CListView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.types.Maplist;
import com.xyhui.utils.Params;

public class NavAddressList extends CListView {
	private ArrayList<Maplist> mMaps;

	private String mKey;

	public NavAddressList(ListView lv, Activity activity, ArrayList<Maplist> maps) {
		super(lv, activity);

		mMaps = maps;

		// 在父类将执行 initListItemResource();ensureUi();
		initListViewStart();
	}

	public void setKey(String key) {
		mKey = key;
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.list_item_nav_address);
	}

	@Override
	public void ensureUi() {
		mPerpage = 1000;

		super.setGetMoreResource(R.layout.list_item_getmore, R.id.list_item_getmore_title,
				"查看更多地点");
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

				Maplist item = (Maplist) mListItems.get(i);

				// 打开地点查看
				Intent intent = new Intent();
				intent.setClass(mActivity, NavAddressViewActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.MAP, item);
				mActivity.startActivity(intent);
			}
		});
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {
		Maplist item = (Maplist) obj;
		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();
		LVP.add(new CListViewParam(R.id.text_title, item.title, true));
		return LVP;
	}

	@Override
	public void asyncData() {
		mListItems.clear();
		mDateList.clear();

		if (mMaps != null) {
			int size = mMaps.size();
			for (int i = 0; i < size; i++) {
				Maplist map = mMaps.get(i);
				if (!TextUtils.isEmpty(mKey)) {
					if (!map.title.contains(mKey)) {
						continue;
					}
				}
				mListItems.add(map);
			}
		}

		initListViewFinish();
		actionType = IDLE;
	}
}
