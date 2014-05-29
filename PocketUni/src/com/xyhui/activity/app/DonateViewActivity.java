package com.xyhui.activity.app;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CActivity;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.DonateDetail;
import com.xyhui.types.Response;
import com.xyhui.utils.Params;
import com.xyhui.widget.DonateBannerLayout;
import com.xyhui.widget.FLActivity;
import com.xyhui.widget.SpannedTextViewNotice;

public class DonateViewActivity extends FLActivity {
	private final String NO_BUYER = "0";
	private final String LOVE_DESC = "爱心校园是一个慈善商店，将“电子商务”和“爱心商店”进行结合，打造了一个创新的公益模式。提倡“每一个都有价值”，基于环保的3R理念：reuse（再使用）、reduce（垃圾减量）、recycle（循环使用），再使用理念不耗费新的资源，也不会向社会增加垃圾。搭建这样一个爱心捐赠认购平台，可以让个人或单位捐出其多余的物品在该平台出售，所得货款全部纳入爱心基金池中。爱心基金将用于资助在校贫困生和高校公益项目。达到物尽其用，货畅其流之目的。如此可让义卖物品处在不断流通的管道，真正实现“再利用”。义卖物品可以涉及二手物品也可以是新的物品，对于在校的学生来说书籍，学习用具，衣物，生活用具等物品都可以“再使用”。";

	private Button btn_buy;
	private Button btn_desc_donate;
	private Button btn_desc_love;
	private TextView text_title;
	private TextView text_price;
	private TextView text_cat;
	private TextView text_city;
	private TextView text_school;
	private TextView text_donate_group;
	private TextView text_contact;
	private TextView text_mobile;
	private SpannedTextViewNotice text_desc;
	private LinearLayout layout_donate_group;
	private DonateBannerLayout donate_banner;

	private DonateDetail mDonate;
	private String mDonateId;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.DONATE_ID)) {
			mDonateId = getIntent().getStringExtra(Params.INTENT_EXTRA.DONATE_ID);
		} else {
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_donate_view);

		btn_buy = (Button) findViewById(R.id.btn_buy);
		btn_desc_donate = (Button) findViewById(R.id.btn_desc_donate);
		btn_desc_love = (Button) findViewById(R.id.btn_desc_love);
		text_title = (TextView) findViewById(R.id.text_title);
		text_price = (TextView) findViewById(R.id.text_price);
		text_cat = (TextView) findViewById(R.id.text_cat);
		text_city = (TextView) findViewById(R.id.text_city);
		text_school = (TextView) findViewById(R.id.text_school);
		text_donate_group = (TextView) findViewById(R.id.text_donate_group);
		text_contact = (TextView) findViewById(R.id.text_contact);
		text_mobile = (TextView) findViewById(R.id.text_mobile);
		text_desc = (SpannedTextViewNotice) findViewById(R.id.text_desc);
		layout_donate_group = (LinearLayout) findViewById(R.id.layout_donate_group);
		donate_banner = (DonateBannerLayout) findViewById(R.id.index_banner);
	}

	@Override
	public void bindListener() {
		btn_buy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_buy.setEnabled(false);
				((CActivity) mActivity).showProgress();
				new Api(mBuyCB, mActivity).payment(PuApp.get().getToken(), mDonateId);
			}
		});

		btn_desc_donate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mDonate) {
					text_desc.setData(mDonate.content);
					btn_desc_donate.setBackgroundColor(getResources().getColor(R.color.white));
					btn_desc_love.setBackgroundColor(getResources().getColor(R.color.grayccc));
					btn_desc_donate
							.setTextColor(getResources().getColor(R.color.orange_highlight));
					btn_desc_love.setTextColor(getResources().getColor(R.color.black));
				}
			}
		});

		btn_desc_love.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mDonate) {
					text_desc.setData(LOVE_DESC);
					btn_desc_love.setBackgroundColor(getResources().getColor(R.color.white));
					btn_desc_donate.setBackgroundColor(getResources().getColor(R.color.grayccc));
					btn_desc_love.setTextColor(getResources().getColor(R.color.orange_highlight));
					btn_desc_donate.setTextColor(getResources().getColor(R.color.black));
				}
			}
		});
	}

	CallBack mBuyCB = new CallBack() {
		@Override
		public void onSuccess(String response) {
			((CActivity) mActivity).dismissProgress();
			Response r = JSONUtils.fromJson(response, Response.class);
			if (1 == r.status) {
				NotificationsUtil.ToastBottomMsg(mActivity, "购买成功");
			} else if (0 == r.status && !TextUtils.isEmpty(r.info)) {
				NotificationsUtil.ToastBottomMsg(mActivity, r.info);
			}
		}

		@Override
		public void onFailure(String message) {
			super.onFailure(message);
			((CActivity) mActivity).dismissProgress();
			NotificationsUtil.ToastBottomMsg(mActivity, "购买失败");
			btn_buy.setEnabled(true);
		}
	};

	@Override
	public void ensureUi() {
		showProgress();
		new Api(callback, mActivity).donate(PuApp.get().getToken(), mDonateId);
	}

	@Override
	protected void onResume() {
		super.onResume();
		donate_banner.reload();
	}

	@Override
	protected void onPause() {
		super.onPause();
		donate_banner.pause();
	}

	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			mDonate = JSONUtils.fromJson(response, DonateDetail.class);

			if (mDonate != null) {
				donate_banner.init(mDonate.imgs);

				text_title.setText(mDonate.title);
				text_price.setText(mDonate.price);
				text_cat.setText(mDonate.cat);
				text_city.setText(mDonate.city);
				text_school.setText(mDonate.school);
				text_donate_group.setText(mDonate.groupName);
				text_desc.setData(mDonate.content);

				if (NO_BUYER.equals(mDonate.buyer)) {
					btn_buy.setEnabled(true);
					btn_buy.setText("购买");
					layout_donate_group.setVisibility(View.GONE);
					text_contact.setText("[购买后可查看联系人及联系方式]");
					text_mobile.setText("[购买后可查看联系人及联系方式]");
				} else {
					btn_buy.setEnabled(false);
					btn_buy.setText("已售出");
					layout_donate_group.setVisibility(View.VISIBLE);
					text_contact.setText(mDonate.contact);
					text_mobile.setText(mDonate.mobile);
				}
			}
		}
	};
}
