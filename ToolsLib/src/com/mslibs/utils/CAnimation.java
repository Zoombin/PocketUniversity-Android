package com.mslibs.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout.LayoutParams;

public class CAnimation {

	public static void PanTO(final View moveView, View toView, int duration) {

		LayoutParams moveLayPms = (LayoutParams) moveView.getLayoutParams();
		LayoutParams toLayPms = (LayoutParams) toView.getLayoutParams();
		int xoffset = (toLayPms.leftMargin + toLayPms.width / 2)
				- (moveLayPms.leftMargin + moveLayPms.width / 2);
		int yoffset = (toLayPms.topMargin + toLayPms.height / 2)
				- (moveLayPms.topMargin + moveLayPms.height / 2);

		final LayoutParams newLayPms = new LayoutParams(moveLayPms.width, moveLayPms.height);
		newLayPms.leftMargin = moveLayPms.leftMargin + xoffset;
		newLayPms.topMargin = moveLayPms.topMargin + yoffset;

		moveView.clearAnimation();

		Animation anim = new TranslateAnimation(0, xoffset, 0, yoffset);
		anim.setDuration(duration);// 设置动画持续时间
		anim.setRepeatCount(0);// 设置重复次数

		anim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation animation) {
				moveView.clearAnimation();
				moveView.setLayoutParams(newLayPms);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationStart(Animation animation) {

			}

		});

		moveView.startAnimation(anim);
	}
}
