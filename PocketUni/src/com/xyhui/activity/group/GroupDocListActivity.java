package com.xyhui.activity.group;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class GroupDocListActivity extends FLActivity {
	private Button btn_back;
	private Button btn_newdoc;
	private PullToRefreshListView doc_listview;

	private String GID;
	private int isMemeber;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.GROUP_ID)) {
			GID = getIntent().getStringExtra(Params.INTENT_EXTRA.GROUP_ID);
		} else {
			finish();
			return;
		}

		if (getIntent().hasExtra(Params.INTENT_EXTRA.GROUPMEMBER)) {
			isMemeber = getIntent().getIntExtra(Params.INTENT_EXTRA.GROUPMEMBER, 0);
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_doc_list);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_newdoc = (Button) findViewById(R.id.btn_newdoc);
		doc_listview = (PullToRefreshListView) findViewById(R.id.doc_listview);
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

		btn_newdoc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 新建文档
				Intent intent = new Intent();
				intent.setClass(mActivity, GroupDocUploadActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.GROUP_ID, GID);
				mActivity.startActivity(intent);
			}
		});

	}

	@Override
	public void ensureUi() {
		if (isMemeber != 1) {
			btn_newdoc.setVisibility(View.GONE);
		}

		new GroupDocList(doc_listview, mActivity, GID);
	}

}
