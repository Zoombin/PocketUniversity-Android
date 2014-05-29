package com.xyhui.activity.group;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class GroupInviteListActivity extends FLActivity {
	private Button btn_back;
	private PullToRefreshListView invite_listview;
	private String group_id;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.GROUP_ID)) {
			group_id = getIntent().getStringExtra(Params.INTENT_EXTRA.GROUP_ID);
			VolleyLog.d("got groupid:%s", group_id);
		}

		if (TextUtils.isEmpty(group_id)) {
			VolleyLog.d("no groupid");
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_group_invite_list);

		btn_back = (Button) findViewById(R.id.btn_back);
		invite_listview = (PullToRefreshListView) findViewById(R.id.invite_listview);
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
	}

	@Override
	public void ensureUi() {
		new GroupInviteList(invite_listview, mActivity, group_id);
	}

}
