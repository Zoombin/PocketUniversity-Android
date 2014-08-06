package com.xyhui.activity.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.xyhui.R;
import com.xyhui.fragment.PopFragment;
import com.xyhui.widget.FLActivity;

public class TupopActivity extends FLActivity {

	private Button btn_back;
	private Button btn_setting;
	private RelativeLayout rl_nav;
	private RadioGroup rg_nav_content;
	private ImageView iv_nav_indicator;
	private ViewPager mViewPager;
	private int indicatorWidth;
	public static String[] tabTitle = new String[2];
	private LayoutInflater mInflater;
	private TabFragmentPagerAdapter mAdapter;
	private int currentIndicatorLeft = 0;
	private static final int new_index = 0;
	private static final int hot_index = 1;
	
	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_tupop);
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_setting = (Button) findViewById(R.id.btn_setting);
		rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);
		rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);
		iv_nav_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		indicatorWidth = dm.widthPixels / 2;

		LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		iv_nav_indicator.setLayoutParams(cursor_Params);

		// 获取布局填充器
		mInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);

		rg_nav_content.removeAllViews();
		tabTitle[0] = "最新";
		tabTitle[1] = "最热";
		
		for (int i = 0; i < tabTitle.length; i++) {

			RadioButton rb = (RadioButton) mInflater.inflate(
					R.layout.nav_radiogroup_item, null);
			rb.setId(i);
			rb.setText(tabTitle[i]);
			rb.setLayoutParams(new LayoutParams(indicatorWidth,
					LayoutParams.MATCH_PARENT));

			rg_nav_content.addView(rb);
		}

		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setAdapter(mAdapter);
		
		((RadioButton) rg_nav_content.getChildAt(0))
		.performClick();
	}
	
	@Override
	public void bindListener() {
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_setting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TupopActivity.this, TupopSettingActivity.class);
				startActivity(intent);
			}
		});
		setListener();
	}
	
	private void setListener() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (rg_nav_content != null
						&& rg_nav_content.getChildCount() > position) {
					((RadioButton) rg_nav_content.getChildAt(position))
							.performClick();
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		rg_nav_content
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {

						if (rg_nav_content.getChildAt(checkedId) != null) {

							TranslateAnimation animation = new TranslateAnimation(
									currentIndicatorLeft,
									((RadioButton) rg_nav_content
											.getChildAt(checkedId)).getLeft(),
									0f, 0f);
							animation.setInterpolator(new LinearInterpolator());
							animation.setDuration(100);
							animation.setFillAfter(true);

							// 执行位移动画
							iv_nav_indicator.startAnimation(animation);

							mViewPager.setCurrentItem(checkedId); // ViewPager
																	// 跟随一起 切换

							// 记录当前 下标的距最左侧的 距离
							currentIndicatorLeft = ((RadioButton) rg_nav_content
									.getChildAt(checkedId)).getLeft();

						}
					}
				});
	}
	
	public static class TabFragmentPagerAdapter extends FragmentPagerAdapter {

		Fragment newft = null;
		Fragment hotft = null;
		
		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int index) {
			Bundle bundle;
			
			switch (index) {
			case new_index:
				if (newft == null) {
					bundle = new Bundle();
					bundle.putInt("type", index);
					newft = new PopFragment();
					newft.setArguments(bundle);
				}
				return newft;
			case hot_index:
				if (hotft == null) {
					bundle = new Bundle();
					bundle.putInt("type", index);
					hotft = new PopFragment();
					hotft.setArguments(bundle);
				}
				return hotft;
			default:
				
				break;
			}
			return null;
		}

		@Override
		public int getCount() {
			return tabTitle.length;
		}
	}
}
