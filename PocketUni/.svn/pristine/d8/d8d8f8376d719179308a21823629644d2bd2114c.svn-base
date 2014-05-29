package com.xyhui.activity.app;

import java.util.ArrayList;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.types.Banner;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.widget.AdBannerLayout;
import com.xyhui.widget.FLActivity;

public class NoticeListActivity extends FLActivity {

	private PrefUtil mPrefUtil;

	private final String CUPID = "13";

	private Button btn_back;

	private Button btn_sort01;
	private Button btn_sort02;
	private Button btn_sort03;
	private Button btn_sort04;
	private Button btn_sort05;
	private Button btn_sort06;
	private Button btn_sort07;

	private Button btn_official_notice;
	private Button btn_school_notice;

	private AdBannerLayout ad_banner;

	private ArrayList<Button> btnsortList;
	private Button btn_select;

	private PullToRefreshListView notice_listview;
	private NoticeList mNoticeListView;

	private int mShowType = NoticeList.NOTICE_OFFICIAL;

	@Override
	public void init() {
		mPrefUtil = new PrefUtil();
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_notice_list);

		btn_back = (Button) findViewById(R.id.btn_back);

		btn_official_notice = (Button) findViewById(R.id.btn_official_notice);
		btn_school_notice = (Button) findViewById(R.id.btn_school_notice);

		btnsortList = new ArrayList<Button>();
		btn_sort01 = (Button) findViewById(R.id.btn_sort01);
		btn_sort01.setTag(0);// 通知分类cid 设置在btn tag里
		btnsortList.add(btn_sort01);

		btn_sort02 = (Button) findViewById(R.id.btn_sort02);
		btn_sort02.setTag(1);// 通知分类cid 设置在btn tag里
		btnsortList.add(btn_sort02);

		btn_sort03 = (Button) findViewById(R.id.btn_sort03);
		btn_sort03.setTag(2);// 通知分类cid 设置在btn tag里
		btnsortList.add(btn_sort03);

		btn_sort04 = (Button) findViewById(R.id.btn_sort04);
		btn_sort04.setTag(3);// 通知分类cid 设置在btn tag里
		btnsortList.add(btn_sort04);

		btn_sort05 = (Button) findViewById(R.id.btn_sort05);
		btn_sort05.setTag(4);// 通知分类cid 设置在btn tag里
		btnsortList.add(btn_sort05);

		btn_sort06 = (Button) findViewById(R.id.btn_sort06);
		btn_sort06.setTag(1);// 通知分类cid 设置在btn tag里
		btnsortList.add(btn_sort06);

		btn_sort07 = (Button) findViewById(R.id.btn_sort07);
		btn_sort07.setTag(2);// 通知分类cid 设置在btn tag里
		btnsortList.add(btn_sort07);

		ad_banner = (AdBannerLayout) findViewById(R.id.ad_banner);

		notice_listview = (PullToRefreshListView) findViewById(R.id.notice_listview);
	}

	private void customNoticeTitle() {
		String cityId = mPrefUtil.getPreference(Params.LOCAL.CITYID);
		if (cityId.equals(CUPID)) {
			btn_sort04.setText("会务信息");
			btn_sort02.setText("通知公告");
			btn_sort03.setText("新闻简报");
		} else {
			btn_sort04.setText("院系通知");
			btn_sort02.setText("学校发文");
			btn_sort03.setText("学校通知");
		}
	}

	@Override
	public void bindListener() {
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭返回
				long currentTime = System.currentTimeMillis();
				long announcetime = (long) (currentTime / 1000);
				mPrefUtil.setPreference(Params.LOCAL.ANNOUNCETIME, announcetime);
				finish();
			}
		});

		btn_official_notice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showOnType(NoticeList.NOTICE_OFFICIAL);
			}
		});

		btn_school_notice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showOnType(NoticeList.NOTICE_SCHOOL);
			}
		});

		OnClickListener sortOCL = new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectSortBtnByCID((Button) v);
			}
		};

		for (int i = 0; i < btnsortList.size(); i++) {
			Button btn = btnsortList.get(i);
			btn.setOnClickListener(sortOCL);
		}
	}

	private void showOnType(int showType) {
		mShowType = showType;

		if (NoticeList.NOTICE_OFFICIAL == mShowType) {
			btn_official_notice.setSelected(true);
			btn_school_notice.setSelected(false);
			for (int i = 0; i < 5; i++) {
				btnsortList.get(i).setVisibility(View.GONE);
			}
			btnsortList.get(5).setVisibility(View.VISIBLE);
			btnsortList.get(6).setVisibility(View.VISIBLE);

			selectSortBtnByCID(btn_sort06);
		} else if (NoticeList.NOTICE_SCHOOL == mShowType) {
			btn_official_notice.setSelected(false);
			btn_school_notice.setSelected(true);
			for (int i = 0; i < 5; i++) {
				btnsortList.get(i).setVisibility(View.VISIBLE);
			}
			btnsortList.get(5).setVisibility(View.GONE);
			btnsortList.get(6).setVisibility(View.GONE);

			selectSortBtnByCID(btn_sort01);
		}
	}

	@Override
	public void ensureUi() {
		ad_banner.init(Banner.TYPE_NOTICE);

		customNoticeTitle();

		showOnType(NoticeList.NOTICE_OFFICIAL);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ad_banner.reload();
	}

	@Override
	protected void onPause() {
		super.onPause();
		ad_banner.pause();
	}

	private void selectSortBtnByCID(Button btn) {
		btn.setSelected(true);

		if (btn_select != null && btn_select != btn) {
			btn_select.setSelected(false);
		}
		btn_select = btn;
		// 刷新数据
		int cid = (Integer) btn.getTag();
		VolleyLog.d("选择分类cid = %d", cid);

		long stime = mPrefUtil.getLongPreference(Params.LOCAL.ANNOUNCETIME);

		if (mNoticeListView == null) {
			mNoticeListView = new NoticeList(notice_listview, mActivity, cid, stime,
					NoticeList.NOTICE_OFFICIAL);
		} else {
			mNoticeListView.refresh(mShowType, cid, stime);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		long currentTime = System.currentTimeMillis();
		long announcetime = (long) (currentTime / 1000);
		mPrefUtil.setPreference(Params.LOCAL.ANNOUNCETIME, announcetime);
	}
}
