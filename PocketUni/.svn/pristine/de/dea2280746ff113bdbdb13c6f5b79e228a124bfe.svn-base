package com.xyhui.activity.app;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Maplist;
import com.xyhui.types.Maplists;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class NavMapViewActivity extends FLActivity {
	private Button btn_back;
	private Button btn_more;
	private TextView text_map_title;

	private LocationClient mLocationClient;
	private BDLocationListener myListener;

	private Maplist map;
	private ArrayList<Maplist> maps;

	private MapView mMapView;
	private MapController mMapController;

	private OverlayTest ov;

	private Drawable marker;

	private MyLocationOverlay myLocationOverlay;
	private LocationData locData = new LocationData();

	@Override
	public void init() {
		PuApp.get().initMapManager();
		initLocClient();
		if (getIntent().hasExtra(Params.INTENT_EXTRA.MAP)) {
			map = getIntent().getParcelableExtra(Params.INTENT_EXTRA.MAP);
		} else {
			finish();
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_nav_map_view);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_more = (Button) findViewById(R.id.btn_more);
		text_map_title = (TextView) findViewById(R.id.text_map_title);
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

		btn_more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 地点列表
				Intent intent = new Intent(mActivity, NavAddressListActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.MAP, maps);
				startActivity(intent);
			}
		});
	}

	@Override
	public void ensureUi() {
		text_map_title.setText(map.title);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapController = mMapView.getController();
		initMapView();

		mMapView.getController().enableClick(true);

		marker = getResources().getDrawable(R.drawable.icon_marka);
		mMapView.getOverlays().clear();
		ov = new OverlayTest(marker, mMapView);
		mMapView.getOverlays().add(ov);

		showProgress();
		new Api(callback, mActivity).ditu(PuApp.get().getToken(), map.id, 1000, 1);
	}

	private void initLocClient() {
		myListener = new MyLocationListener();
		mLocationClient = new LocationClient(PuApp.get()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setPriority(LocationClientOption.GpsFirst);
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.disableCache(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	private void initMapView() {
		mMapView.setLongClickable(false);
		mMapView.getController().setZoom(17);
		mMapView.setBuiltInZoomControls(true);
	}

	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();

			Maplists items = JSONUtils.fromJson(response, Maplists.class);

			if (null != items && null != items.data && items.data.size() > 0) {
				maps = items.data;

				for (int i = 0; i < maps.size(); i++) {
					Maplist map = maps.get(i);
					VolleyLog.d("got location:(%s, %s, %s)", map.title, map.lat, map.lng);
					Double dlat, dlng;

					try {
						dlat = Double.parseDouble(map.lat);
						dlng = Double.parseDouble(map.lng);
					} catch (Exception e) {
						VolleyLog.e(e, "BAD LOCATION: (%s, %s)", map.lat, map.lng);
						NotificationsUtil.ToastBottomMsg(mActivity, "数据格式有误");
						continue;
					}

					int lat = (int) Math.round(dlat * 1E6);
					int lng = (int) Math.round(dlng * 1E6);

					OverlayItem item = new OverlayItem(new GeoPoint(lat, lng), map.title,
							map.title);
					item.setMarker(marker);
					ov.addItem(item);
				}

				mMapController.setCenter(ov.getCenter());
			}

			if (mMapView != null) {
				mMapView.refresh();
			}
		}
	};

	@Override
	protected void onPause() {
		if (null != mMapView) {
			mMapView.onPause();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (null != mMapView) {
			mMapView.onResume();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		cleanOnDestroy();
		super.onDestroy();
	}

	public void cleanOnDestroy() {
		if (null != mMapView) {
			mMapView.destroy();
			mMapView = null;
		}

		if (null != mLocationClient) {
			mLocationClient.unRegisterLocationListener(myListener);
			mLocationClient.stop();
			mLocationClient = null;
			myListener = null;
		}
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			int locType = location.getLocType();
			VolleyLog.d("onReceiveLocation location.getLocType()= %d", locType);

			if (locType == BDLocation.TypeNetWorkLocation || locType == BDLocation.TypeGpsLocation
					|| locType == BDLocation.TypeCacheLocation
					|| locType == BDLocation.TypeOffLineLocation) {
				VolleyLog.d("got location:(%s, %s)", location.getLatitude(),
						location.getLongitude());
				locData.latitude = location.getLatitude();
				locData.longitude = location.getLongitude();
				locData.accuracy = location.getRadius();
				locData.direction = location.getDerect();

				myLocationOverlay = new MyLocationOverlay(mMapView);
				mMapView.getOverlays().add(myLocationOverlay);
				myLocationOverlay.setData(locData);
				mMapView.refresh();
			}
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {

		}
	}

	class OverlayTest extends ItemizedOverlay<OverlayItem> {
		public OverlayTest(Drawable marker, MapView mapView) {
			super(marker, mapView);
		}

		@Override
		protected boolean onTap(int index) {
			super.onTap(index);
			Toast.makeText(mActivity, getItem(index).getTitle(), Toast.LENGTH_SHORT).show();
			return true;
		}

		@Override
		public boolean onTap(GeoPoint pt, MapView mapView) {
			super.onTap(pt, mapView);
			return false;
		}
	}
}
