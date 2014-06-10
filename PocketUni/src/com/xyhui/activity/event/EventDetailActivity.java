package com.xyhui.activity.event;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.NotificationsUtil;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.activity.event.EventCommentsList.CommentsClickCallBack;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.FLActivity;

public class EventDetailActivity extends FLActivity {
	private Button btn_back, btn_comments;

	private Button btn_event_news;
	private PullToRefreshListView event_news_listview;

	private Button btn_event_photo;
	private GridView event_photo_gridview;
	private EventPhotoGridView mEventPhotoGridView;
	private EventList eventList;
	private String eventid;

	private Button btn_event_comments;
	private PullToRefreshListView event_comment_listview;
	private EventCommentsList mEventCommentsList;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.EVENTID)) {
			eventid = getIntent().getStringExtra(Params.INTENT_EXTRA.EVENTID);
		} else {
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_event_detail);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_comments = (Button) findViewById(R.id.btn_comments);
		btn_event_news = (Button) findViewById(R.id.btn_event_news);
		event_news_listview = (PullToRefreshListView) findViewById(R.id.event_news_listview);
		btn_event_photo = (Button) findViewById(R.id.btn_event_photo);
		event_photo_gridview = (GridView) findViewById(R.id.event_photo_gridview);
		btn_event_comments = (Button) findViewById(R.id.btn_event_comments);
		event_comment_listview = (PullToRefreshListView) findViewById(R.id.event_comment_listview);
	}

	private void showPanel(final EventComment comment) {
		Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("选择操作");

		final String userid = new PrefUtil().getPreference(Params.LOCAL.UID);
		builder.setItems(
				userid.equals(comment.uid) ? R.array.event_comment_withdelete
						: R.array.event_comment_withoutdelete,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0: {
							// 回复评论
							Intent intent = new Intent(mActivity,
									EventCommentsEditActivity.class);
							intent.putExtra(
									Params.INTENT_EXTRA.EVENT_COMMENT_EDIT,
									Params.INTENT_VALUE.WEIBO_REPLY);
							intent.putExtra(Params.INTENT_EXTRA.EVENT_ID,
									eventid);
							intent.putExtra(Params.INTENT_EXTRA.USERNAME,
									comment.uname);
							intent.putExtra(
									Params.INTENT_EXTRA.EVENT_COMMENT_TOID,
									comment.id);
							startActivity(intent);
							break;
						}
						case 1:
							if(userid.equals(comment.uid)){
								deleteComment(comment.id);
								break;
							}
						case 2:
							dialog.cancel();
							break;
						}
					}
				}).show();
	}

	public void deleteComment(String id) {
		showProgress("正在提交请求", "请稍候...");
		new Api(new CallBack(){
			@Override
			public void onSuccess(String response) {
				super.onSuccess(response);
				dismissProgress();
				int status = 0;
				String msg = "";
				try {
					JSONObject jsonObject = new JSONObject(response);
					status = jsonObject.optInt("status");
					msg = jsonObject.optString("msg");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (status > 0) {
					NotificationsUtil.ToastBottomMsg(mActivity, "操作成功");
					Intent intent = new Intent()
							.setAction(Params.INTENT_ACTION.EVENTLIST);
					sendBroadcast(intent);
				} else if (0 == status) {
					NotificationsUtil.ToastBottomMsg(mActivity, msg);
				}
			}
			
			@Override
			public void onFailure(String message) {
				super.onFailure(message);
				dismissProgress();
				NotificationsUtil.ToastBottomMsg(mActivity, "操作失败");
			
			}
		}, mActivity).getDeleteComment(PuApp.get()
				.getToken(), id);
	}

	@Override
	public void bindListener() {
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭返回
				finish();
			}
		});
		btn_comments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 发表评论
				Intent intent = new Intent(mActivity,
						EventCommentsEditActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.EVENT_COMMENT_EDIT,
						Params.INTENT_VALUE.WEIBO_NEW);
				intent.putExtra(Params.INTENT_EXTRA.EVENT_ID, eventid);
				startActivity(intent);
			}
		});

		btn_event_news.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_comments.setVisibility(View.INVISIBLE);
				// 查看活动新闻
				btn_event_news.setSelected(true);
				event_news_listview.setVisibility(View.VISIBLE);
				btn_event_photo.setSelected(false);
				event_photo_gridview.setVisibility(View.GONE);
				btn_event_comments.setSelected(false);
				event_comment_listview.setVisibility(View.GONE);
			}
		});
		btn_event_photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_comments.setVisibility(View.INVISIBLE);
				// 查看现场花絮
				btn_event_news.setSelected(false);
				event_news_listview.setVisibility(View.GONE);
				btn_event_photo.setSelected(true);
				event_photo_gridview.setVisibility(View.VISIBLE);
				btn_event_comments.setSelected(false);
				event_comment_listview.setVisibility(View.GONE);
				if (mEventPhotoGridView == null) {
					mEventPhotoGridView = new EventPhotoGridView(
							event_photo_gridview, mActivity, eventid);
				}
			}
		});
		btn_event_comments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_comments.setVisibility(View.VISIBLE);
				// 查看现场花絮
				btn_event_news.setSelected(false);
				event_news_listview.setVisibility(View.GONE);
				btn_event_photo.setSelected(false);
				event_photo_gridview.setVisibility(View.GONE);
				btn_event_comments.setSelected(true);
				event_comment_listview.setVisibility(View.VISIBLE);
				if (mEventCommentsList == null) {
					mEventCommentsList = new EventCommentsList(
							event_comment_listview, mActivity, eventid);
					mEventCommentsList
							.setmClickListener(new CommentsClickCallBack() {

								@Override
								public void onClick(View v, EventComment comment) {
									showPanel(comment);
								}
							});
				}
			}
		});
	}

	@Override
	public void ensureUi() {
		btn_event_news.setSelected(true);
		btn_comments.setVisibility(View.INVISIBLE);
		event_news_listview.setVisibility(View.VISIBLE);
		event_photo_gridview.setVisibility(View.GONE);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Params.INTENT_ACTION.EVENTLIST);
		registerReceiver(mEvtReceiver, filter);

		new EventNewsList(event_news_listview, mActivity, eventid);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mEvtReceiver);
	}

	public BroadcastReceiver mEvtReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Params.INTENT_ACTION.EVENTLIST)) {
				if (mEventCommentsList != null) {
					mEventCommentsList.refresh();
				}
			}
		}
	};
}
