package com.xyhui.activity.event;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class EventDetailActivity extends FLActivity {
	private Button btn_back,btn_comments;

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
				//发表评论
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
					mEventPhotoGridView = new EventPhotoGridView(event_photo_gridview, mActivity,
							eventid);
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
					mEventCommentsList = new EventCommentsList(event_comment_listview, mActivity,
							eventid);
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

		new EventNewsList(event_news_listview, mActivity, eventid);
	}
}
