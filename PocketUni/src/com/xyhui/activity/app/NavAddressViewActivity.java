package com.xyhui.activity.app;

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
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.types.Maplist;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class NavAddressViewActivity extends FLActivity {
	private Button btn_back;

	private TextView text_address_title;

	private Maplist map;

	private MapView mMapView;

	private LocationClient mLocationClient;
	private BDLocationListener myListener;

	private ItemizedOverlay<OverlayItem> ov;

	private Drawable marker;
	private int cLat;
	private int cLng;

	private MyLocationOverlay myLocationOverlay;
	private LocationData locData = new LocationData();

	private MKSearch mSearch;

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
		setContentView(R.layout.activity_nav_address_view);

		btn_back = (Button) findViewById(R.id.btn_back);
		text_address_title = (TextView) findViewById(R.id.text_address_title);
		mMapView = (MapView) findViewById(R.id.bmapView);
		marker = getResources().getDrawable(R.drawable.icon_marka);
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
		initMapView();
		mSearch = new MKSearch();
		mSearch.init(PuApp.get().getBMapMgr(), new MKSearchListener() {

			@Override
			public void onGetPoiDetailSearchResult(int type, int error) {
			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
				if (error != 0 || res == null) {
					Toast.makeText(NavAddressViewActivity.this, "抱歉，未找到导航结果", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if (null == mMapView) {
					return;
				}

				RouteOverlay routeOverlay = new RouteOverlay(NavAddressViewActivity.this, mMapView);
				routeOverlay.setData(res.getPlan(0).getRoute(0));
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(routeOverlay);
				mMapView.getOverlays().add(myLocationOverlay);

				mMapView.refresh();
				// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
				mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(),
						routeOverlay.getLonSpanE6());
				mMapView.getController().animateTo(res.getStart().pt);
			}

			public void onGetAddrResult(MKAddrInfo res, int error) {

			}

			public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {

			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {

			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {

			}
		});

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
		text_address_title.setText(map.title);

		try {
			Double dlat = 0.0, dlng = 0.0;

			dlat = Double.parseDouble(map.lat);
			dlng = Double.parseDouble(map.lng);

			cLat = (int) Math.round(dlat * 1E6);
			cLng = (int) Math.round(dlng * 1E6);
		} catch (Exception e) {
			VolleyLog.e(e, "BAD LOCATION: (%s, %s)", map.lat, map.lng);
			NotificationsUtil.ToastBottomMsg(mActivity, "数据格式有误");
			finish();
		}

		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		GeoPoint point = new GeoPoint((int) (cLat), (int) (cLng));

		// 设置地图中心点为目标地址(默认会显示北京地图)
		mMapView.getController().setCenter(point);

		mMapView.setLongClickable(false);
		mMapView.getController().setZoom(16);
		mMapView.getController().enableClick(true);
		mMapView.setBuiltInZoomControls(true);

		mMapView.getOverlays().clear();
		ov = new ItemizedOverlay<OverlayItem>(marker, mMapView);
		mMapView.getOverlays().add(ov);

		myLocationOverlay = new MyLocationOverlay(mMapView);
		mMapView.getOverlays().add(myLocationOverlay);

		OverlayItem item = new OverlayItem(new GeoPoint(cLat, cLng), map.title, map.title);
		item.setMarker(marker);
		ov.addItem(item);

		mMapView.refresh();
	}

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
				VolleyLog.d("got location: (%s, %s) ", location.getLatitude(),
						location.getLongitude());
				locData.latitude = location.getLatitude();
				locData.longitude = location.getLongitude();
				locData.accuracy = location.getRadius();
				locData.direction = location.getDerect();
				myLocationOverlay.setData(locData);
				MKPlanNode stNode = new MKPlanNode();
				stNode.pt = new GeoPoint((int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6));
				MKPlanNode enNode = new MKPlanNode();
				enNode.pt = new GeoPoint((int) (cLat), (int) (cLng));
				mSearch.walkingSearch("我的位置", stNode, map.title, enNode);
				mMapView.refresh();
			}
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {

		}
	}
}
