package com.xyhui.activity.photo;

import java.util.ArrayList;

import ru.truba.touchgallery.GalleryWidget.BasePagerAdapter.OnItemChangeListener;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.xyhui.R;
import com.xyhui.widget.FLActivity;

/**
 * 照片浏览模块
 * 
 * @author 烨
 * 
 */
public class PhotosActivity extends FLActivity {

	UrlPagerAdapter pagerAdapter;

	private int index;
	private String photo;
	private ArrayList<String> photos;
	private GalleryViewPager mViewPager;
	private Button btn_return;
	private TextView tv_count;

	@Override
	public void linkUiVar() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_photos);
		
		// String[] urls = {
		// "http://cs407831.userapi.com/v407831207/18f6/jBaVZFDhXRA.jpg",
		// "http://cs407831.userapi.com/v4078f31207/18fe/4Tz8av5Hlvo.jpg",
		// "http://cs407831.userapi.com/v407831207/1906/oxoP6URjFtA.jpg",
		// "http://cs407831.userapi.com/v407831207/190e/2Sz9A774hUc.jpg",
		// "http://cs407831.userapi.com/v407831207/1916/Ua52RjnKqjk.jpg",
		// "http://cs407831.userapi.com/v407831207/191e/QEQE83Ok0lQ.jpg" };
		// List<String> items = new ArrayList<String>();
		// Collections.addAll(items, urls);

		photos = getIntent().getStringArrayListExtra("PHOTOS");
		photo = getIntent().getStringExtra("PHOTO");
		for (int i=0; i<photos.size(); i++) {
			if (photo.equals(photos.get(i))) {
				index = i;
				break;
			}
		}
		
		btn_return = (Button) findViewById(R.id.btn_return);
		btn_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		tv_count = (TextView) findViewById(R.id.tv_count);

		pagerAdapter = new UrlPagerAdapter(this, photos);
		pagerAdapter.setOnItemChangeListener(new OnItemChangeListener() {
			@Override
			public void onItemChange(int currentPosition) {
				tv_count.setText((currentPosition + 1) + "/"
						+ photos.size());
			}
		});

		mViewPager = (GalleryViewPager) findViewById(R.id.viewer);
		mViewPager.setOffscreenPageLimit(10);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setCurrentItem(index);
	}
}
