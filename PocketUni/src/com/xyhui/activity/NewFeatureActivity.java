package com.xyhui.activity;

import android.content.Intent;

import com.xyhui.R;
import com.xyhui.widget.FLActivity;

public class NewFeatureActivity extends FLActivity {
	// private ImageView img_new_feature;

	// private AnimationDrawable mAnim;

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_new_feature);

		// img_new_feature = (ImageView) findViewById(R.id.img_new_feature);
		// mAnim = (AnimationDrawable) img_new_feature.getBackground();
	}

	@Override
	public void bindListener() {

	}

	@Override
	public void ensureUi() {
		// mAnim.start();
		// img_new_feature.postDelayed(new Runnable() {
		// @Override
		// public void run() {
		gotoMain();
		// overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		// }
		// }, 3500);
	}

	private void gotoMain() {
		Intent intent = new Intent(NewFeatureActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
