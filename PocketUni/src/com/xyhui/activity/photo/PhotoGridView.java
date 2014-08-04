package com.xyhui.activity.photo;

import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;

import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.widget.CActivity;
import com.mslibs.widget.CGridView;
import com.mslibs.widget.CListViewParam;
import com.xyhui.R;
import com.xyhui.activity.ImageZoomActivity;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Photo;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;

public class PhotoGridView extends CGridView {
	private String mUserID;
	private String mAlbumID;
	private String mPasswd;

	private ArrayList<String> photolist = new ArrayList<String>();
	
	private Activity mActivity;

	public PhotoGridView(GridView gv, Activity activity, String albumID,
			String user_id, String password) {
		super(gv, activity);
		mActivity = activity;
		mUserID = user_id;
		mAlbumID = albumID;
		mPasswd = password;
		actionType = IDLE;
		initListViewStart();
	}

	public void ensureUi() {
		super.ensureUi();
	}

	private void showSelectDialog(final Photo photo) {
		Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("请选择");

		builder.setItems(R.array.photo_click_admin,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0: {
							// 查看
//							Intent intent = new Intent(getContext(),
//									ImageZoomActivity.class);
//							intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_URL,
//									photo.getOrig());
//							getContext().startActivity(intent);
							Intent intent = new Intent(getContext(), PhotosActivity.class);
							intent.putStringArrayListExtra("PHOTOS", photolist);
							intent.putExtra("PHOTO", photo.getOrig());
							getContext().startActivity(intent);
							break;
						}
						case 1: {
							// 设为封面
							new Api(photoCallBack, getContext()).setCover(PuApp
									.get().getToken(), photo.getId());
							break;
						}
						case 2: {
							// 修改
							updatePhoto(photo);
							break;
						}
						case 3: {
							// 删除
							new Api(photoCallBack, getContext()).delPhoto(PuApp
									.get().getToken(), photo.getId());
							break;
						}

						}
						dialog.cancel();
					}
				}).show();
	}

	public void updatePhoto(final Photo photo) {
		final EditText et = new EditText(mActivity);
		new AlertDialog.Builder(mActivity).setView(et)
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (et.getText().toString().trim().equals("")) {
							NotificationsUtil.ToastTopMsg(mActivity, "请输入新名字！");
							return;
						}
						new Api(photoCallBack, getContext()).updatePhoto(PuApp
								.get().getToken(), photo.getId(), et.getText()
								.toString().trim(), mAlbumID);
						dialog.dismiss();
					}
				}).setPositiveButton("取消", null).setTitle("请输入新名字").show();
	}

	CallBack photoCallBack = new CallBack() {
		@Override
		public void onSuccess(String response) {
			super.onSuccess(response);
			((CActivity) getContext()).dismissProgress();

			try {
				JSONObject jsonObject = new JSONObject(response);
				int status = jsonObject.optInt("status");
				if (status == 1) {
					// 成功
					NotificationsUtil.ToastTopMsg(mActivity, "操作成功！");
				} else {
					// 失败
					NotificationsUtil.ToastTopMsg(mActivity,
							jsonObject.optString("msg"));
				}

			} catch (Exception e) {
				// TODO: handle exception
			}
			actionType = IDLE;
			// 通知更新
			{
				Intent intent = new Intent(Params.INTENT_ACTION.PHOTOLIST);
				getContext().sendBroadcast(intent);
			}
			{
				Intent intent = new Intent(Params.INTENT_ACTION.ALBUMLIST);
				getContext().sendBroadcast(intent);
			}
		}

		public void onFailure(String message) {
			super.onFailure(message);
			((CActivity) getContext()).dismissProgress();
			actionType = IDLE;
		};
	};

	@Override
	public void initListItemResource() {
		this.setListItemResource(R.layout.grid_item_event_photo);
	}

	@Override
	public ArrayList<CListViewParam> matchListItem(Object obj, int index) {

		final Photo item = (Photo) obj;

		ArrayList<CListViewParam> LVP = new ArrayList<CListViewParam>();

		CListViewParam v = new CListViewParam(R.id.img_photo,
				R.drawable.img_default, true);
		v.setOnclickLinstener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String user_id = new PrefUtil().getPreference(Params.LOCAL.UID);
				//是否是自己的相册
				if (user_id.equals(mUserID)) {
					showSelectDialog(item);

				} else {
					// 查看
//					Intent intent = new Intent(getContext(),
//							ImageZoomActivity.class);
//					intent.putExtra(Params.INTENT_EXTRA.WEBVIEW_URL,
//							item.getOrig());
//					getContext().startActivity(intent);
					Intent intent = new Intent(getContext(), PhotosActivity.class);
					intent.putStringArrayListExtra("PHOTOS", photolist);
					intent.putExtra("PHOTO", item.getOrig());
					getContext().startActivity(intent);
				}
			}
		});
		v.setItemTag(item.getThumb());
		v.setImgAsync(true);
		LVP.add(v);

		return LVP;
	}

	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			com.xyhui.types.PhotoList photoList = JSONUtils.fromJson(response,
					com.xyhui.types.PhotoList.class);
			if (photoList.getStatus() == 0) {
				// 失败
				NotificationsUtil.ToastTopMsg(mActivity, photoList.getMsg());
			} else {
				// 成功
				Photo[] photos = photoList.getData().getPhotos();
				getListItems().clear();
				getmDateList().clear();

				if (photos != null) {
					for (int i = 0; i < photos.length; i++) {
						getListItems().add(photos[i]);
						photolist.add(photos[i].getOrig());
					}
				}
				initListViewFinish();
			}

			((CActivity) getContext()).dismissProgress();
		}

		@Override
		public void onFailure(String message) {
			((CActivity) getContext()).dismissProgress();
		}
	};

	@Override
	public void asyncData() {
		((CActivity) getContext()).showProgress();
		new Api(callback, getContext()).getAlbum(PuApp.get().getToken(),
				mAlbumID, mUserID, mPasswd);
	}
}
