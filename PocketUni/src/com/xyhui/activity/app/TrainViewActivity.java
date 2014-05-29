package com.xyhui.activity.app;

import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CActivity;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.BaseApi;
import com.xyhui.api.CallBack;
import com.xyhui.utils.Params;
import com.xyhui.utils.ShareSDKUtil;
import com.xyhui.widget.FLActivity;

public class TrainViewActivity extends FLActivity {
	private Button btn_back;
	private Button btn_collect;
	private Button btn_share;
	private Button btn_desc_course;
	private Button btn_desc_org;

	private CourseInfo course;

	private TextView text_cost;
	private TextView text_contact;
	private TextView text_title;
	private TextView text_address;
	private TextView text_startDate;
	private TextView text_duration;
	private TextView text_school;
	private TextView text_hot;
	private TextView text_desc;

	private String courseid;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.COURSEID)) {
			courseid = getIntent().getStringExtra(Params.INTENT_EXTRA.COURSEID);
		} else {
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_train_course_view);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_collect = (Button) findViewById(R.id.btn_collect);
		btn_share = (Button) findViewById(R.id.btn_share);
		btn_desc_course = (Button) findViewById(R.id.btn_desc_course);
		btn_desc_org = (Button) findViewById(R.id.btn_desc_org);
		text_cost = (TextView) findViewById(R.id.text_cost);
		text_contact = (TextView) findViewById(R.id.text_contact);
		text_title = (TextView) findViewById(R.id.text_title);
		text_address = (TextView) findViewById(R.id.text_address);
		text_startDate = (TextView) findViewById(R.id.text_startDate);
		text_duration = (TextView) findViewById(R.id.text_duration);
		text_school = (TextView) findViewById(R.id.text_school);
		text_hot = (TextView) findViewById(R.id.text_hot);
		text_desc = (TextView) findViewById(R.id.text_desc);
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

		btn_collect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showProgress();
				btn_collect.setEnabled(false);
				if (null == course) {
					return;
				} else if (1 == course.collect) {
					new Api(collectCB, mActivity).cancelCollect(PuApp.get().getToken(), course.id);
				} else {
					new Api(collectCB, mActivity).collect(PuApp.get().getToken(), course.id);
				}
			}
		});

		btn_share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String content = String
						.format("分享培训课程:【%s】：http://www.pocketuni.net/index.php?app=train&mod=Index&act=detail&id=%s",
								course.title, course.id);
				ShareSDKUtil.showShare(mActivity, false, content, null, null);
			}
		});

		btn_desc_course.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != course) {
					text_desc.setText(course.description);
					btn_desc_course.setBackgroundColor(getResources().getColor(R.color.white));
					btn_desc_org.setBackgroundColor(getResources().getColor(R.color.grayccc));
					btn_desc_course.setTextColor(getResources().getColor(R.color.black));
					btn_desc_org.setTextColor(getResources().getColor(R.color.blue_highlight));
				}
			}
		});

		btn_desc_org.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != course) {
					text_desc.setText(course.orgDesc);
					btn_desc_org.setBackgroundColor(getResources().getColor(R.color.white));
					btn_desc_course.setBackgroundColor(getResources().getColor(R.color.grayccc));
					btn_desc_org.setTextColor(getResources().getColor(R.color.black));
					btn_desc_course.setTextColor(getResources().getColor(R.color.blue_highlight));
				}
			}
		});
	}

	CallBack collectCB = new CallBack() {

		@Override
		public void onSuccess(String response) {
			((CActivity) mActivity).dismissProgress();
			btn_collect.setEnabled(true);
			String res = BaseApi.decodeUnicode(response);
			NotificationsUtil.ToastBottomMsg(mActivity, res.substring(1, res.length() - 1));
			if (!TextUtils.isEmpty(response)) {
				if (1 == course.collect) {
					course.collect = 0;
					btn_collect.setText("收藏");
				} else {
					course.collect = 1;
					btn_collect.setText("已收藏");
				}
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			((CActivity) mActivity).dismissProgress();
			btn_collect.setEnabled(true);
		}
	};

	@Override
	public void ensureUi() {
		showProgress();
		text_desc.setMovementMethod(ScrollingMovementMethod.getInstance());
		new Api(callback, mActivity).course(PuApp.get().getToken(), courseid);
	}

	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			course = JSONUtils.fromJson(response, CourseInfo.class);

			if (course != null) {
				if (0 == course.collect) {
					btn_collect.setText("收藏");
				} else {
					btn_collect.setText("已收藏");
				}

				text_cost.setText(course.cost);
				text_contact.setText(course.contact);
				text_title.setText(course.title);
				text_address.setText(course.address);
				text_startDate.setText(course.kDate);
				text_duration.setText(course.dauer);
				text_school.setText(course.org);
				text_hot.setText(String.format("已有 %s 人浏览", course.click));
				text_desc.setText(course.description);
			}
		}
	};

	private static class CourseInfo {
		String id;
		String title;
		String description;
		String address;
		String cost;
		String contact;
		String kDate;
		String dauer;
		String click;
		@SuppressWarnings("unused")
		String orgId;
		String org;
		String orgDesc;
		int collect;
	}
}
