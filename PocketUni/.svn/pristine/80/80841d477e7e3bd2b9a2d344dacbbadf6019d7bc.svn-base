package com.xyhui.activity.group;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.NotificationsUtil;
import com.xyhui.R;
import com.xyhui.widget.FLActivity;

public class GroupSearchActivity extends FLActivity {
	private Button btn_back;
	private PullToRefreshListView group_listview;
	private GroupList mGroupListView;
	private EditText edit_keyword;
	private Button btn_search;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_group_search);

		btn_back = (Button) findViewById(R.id.btn_back);
		group_listview = (PullToRefreshListView) findViewById(R.id.group_listview);
		edit_keyword = (EditText) findViewById(R.id.edit_keyword);
		btn_search = (Button) findViewById(R.id.btn_search);
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
		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 查询部落
				String key = edit_keyword.getText().toString();

				if (TextUtils.isEmpty(key.trim())) {
					NotificationsUtil.ToastTopMsg(getBaseContext(), "输入字段不能为空");
					return;
				}

				hideSoftInput(edit_keyword);

				if (mGroupListView != null) {
					mGroupListView.search(key);
				} else {
					mGroupListView = new GroupList(group_listview, mActivity,
							GroupList.GROUP_SEARCH);
					mGroupListView.search(key);
				}
			}
		});
	}

	@Override
	public void ensureUi() {

	}
}
