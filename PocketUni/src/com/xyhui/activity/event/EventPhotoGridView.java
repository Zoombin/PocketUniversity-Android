package com.xyhui.activity.event;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;
import com.mslibs.utils.JSONUtils;
import com.mslibs.widget.CActivity;
import com.mslibs.widget.CGridView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.ImageZoomActivity;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.EventNews;
import com.xyhui.utils.Params;

public class EventPhotoGridView extends CGridView {
	private int page = 1;
	private String eventID;

	public EventPhotoGridView(GridView gv, Activity activity, String eid) {
		super(gv, activity);

		eventID = eid;
		initListViewStart();
	}

	public void ensureUi() {

		setPerPage(100);

		super.ensureUi();

		setItemOnclickLinstener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// item点击事件
				int i = (Integer) v.getTag();
				EventNews item = (EventNews) getListItems().get(i);

				// Intent intent = new Intent();
				// intent.setClass(getContext(), WebViewActivity.class);
				// intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_TITLE, "图片浏览");
				// intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_URL, item.path);
				// intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_TYPE,
				// WebViewActivity.TYPE_IMAGE);
				// ((Activity) getContext()).startActivity(intent);

				Intent intent = new Intent(getContext(),
						ImageZoomActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_URL, item.path);
				getContext().startActivity(intent);
			}
		});
	}

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.grid_item_event_photo);
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		EventNews item = (EventNews) obj;

		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		CListViewParam v = new CListViewParam(R.id.img_photo,
				R.drawable.img_default, true);
		v.setItemTag(item.path);
		v.setImgAsync(true);
		LVP.add(v);

		return LVP;
	}

	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			ArrayList<EventNews> items = JSONUtils.fromJson(response,
					new TypeToken<ArrayList<EventNews>>() {
					});

			switch (actionType) {
			case INIT: {
				getListItems().clear();
				getmDateList().clear();

				if (items != null) {
					for (int i = 0; i < items.size(); i++) {
						getListItems().add(items.get(i));
					}
				}

				initListViewFinish();
				break;
			}

			case REFRESH: {
				getListItems().clear();
				getmDateList().clear();

				if (items != null) {
					for (int i = 0; i < items.size(); i++) {
						getListItems().add(items.get(i));
					}
				}

				refreshListViewFinish();
				break;
			}

			case GETMORE: {
				if (items != null) {
					for (int i = 0; i < items.size(); i++) {
						getListItems().add(items.get(i));
					}
				}

				getmoreListViewFinish();
				break;
			}
			}

			actionType = IDLE;
			((CActivity) getContext()).dismissProgress();
		}

		@Override
		public void onFailure(String message) {
			actionType = IDLE;
			((CActivity) getContext()).dismissProgress();
		}
	};

	@Override
	public void asyncData() {
		((CActivity) getContext()).showProgress();

		switch (actionType) {
		case INIT: {
			page = 1;
		}
			break;
		case REFRESH: {
			page = 1;
		}
			break;
		case GETMORE: {
			page++;
		}
			break;
		}

		new Api(callback, getContext()).getEventPhotoList(PuApp.get()
				.getToken(), eventID, getPerPage(), page);
	}
}
