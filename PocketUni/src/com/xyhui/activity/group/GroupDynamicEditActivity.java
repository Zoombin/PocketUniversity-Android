package com.xyhui.activity.group;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.mslibs.utils.ImageUtils;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Response;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class GroupDynamicEditActivity extends FLActivity {
	private Button btn_back;
	private Button btn_send;
	private TextView navbar_TitleText;
	private EditText edit_title;
	private ImageView img_split;
	private EditText edit_content;
	private TextView text_counter;

	private ImageView img_photo;
	private Button toolbar_photo;
	private Button toolbar_topic;
	private Button toolbar_at;
	private Button toolbar_swf;
	private Button toolbar_link;
	private Button toolbar_face;

	private LinearLayout layout_faces;
	// private String[] face_names = new String[] { "tongue", "smile", "lol",
	// "loveliness",
	// "titter",
	// "biggrin", "shy", "sweat", "yun", "ku", "yiwen", "mad", "fendou", "funk",
	// "cry",
	// "sad", "ha", "huffy", "pig", "kiss", "victory", "ok", "handshake",
	// "cake", "hug",
	// "beer", "call", "time", "moon" };
	private String[] face_names = new String[] { "tongue", "smile", "lol",
			"loveliness", "titter", "biggrin", "shy", "sweat", "yun", "ku",
			"88", "mad", "fendou", "funk", "cry", "sad", "ha", "huffy", "pig",
			"guzhang", "victory", "ok", "tu", "cake", "hug", "beer", "call",
			"time", "moon", "hei" };
	private boolean face_is_open = false;

	private String groupIP;

	@Override
	public void init() {
		groupIP = getIntent().getStringExtra(Params.INTENT_EXTRA.GROUP_ID);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_weibo_edit);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_send = (Button) findViewById(R.id.btn_send);
		navbar_TitleText = (TextView) findViewById(R.id.navbar_TitleText);
		edit_title = (EditText) findViewById(R.id.edit_title);
		img_split = (ImageView) findViewById(R.id.img_split);
		edit_content = (EditText) findViewById(R.id.edit_content);

		text_counter = (TextView) findViewById(R.id.text_counter);

		img_photo = (ImageView) findViewById(R.id.img_photo);
		img_photo.setVisibility(View.GONE);
		toolbar_photo = (Button) findViewById(R.id.toolbar_photo);
		toolbar_photo.setVisibility(View.GONE);
		toolbar_topic = (Button) findViewById(R.id.toolbar_topic);
		toolbar_topic.setVisibility(View.GONE);
		toolbar_at = (Button) findViewById(R.id.toolbar_at);
		toolbar_at.setVisibility(View.GONE);
		toolbar_swf = (Button) findViewById(R.id.toolbar_swf);
		img_photo.setVisibility(View.GONE);
		toolbar_link = (Button) findViewById(R.id.toolbar_link);
		toolbar_link.setVisibility(View.GONE);
		toolbar_face = (Button) findViewById(R.id.toolbar_face);

		layout_faces = (LinearLayout) findViewById(R.id.layout_faces);
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
		btn_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				btn_send.setEnabled(false);

				// 提交
				String content = edit_content.getText().toString().trim();

				if (content.length() > 140) {
					NotificationsUtil.ToastBottomMsg(mActivity, "内容超过140个字符");
					return;
				}

				hideSoftInput(edit_content);
				showProgress("正在提交请求", "请稍候...");

				RequestParams params = PuApp.get().getToken();
					new Api(callback, mActivity).addFeed(params, groupIP,
							content);
			}
		});

		edit_content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (face_is_open) {
					toolbar_face
							.setBackgroundResource(R.drawable.btn_selector_edit_face);
					getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
											| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
					layout_faces.setVisibility(View.GONE);

					face_is_open = false;
				}
			}
		});

		edit_content.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				int length = edit_content.getText().length();

				text_counter.setText("字数：" + length + "/140");
				if (length > 140) {
					btn_send.setEnabled(false);
					text_counter.setTextColor(Color.rgb(255, 0, 0));
				} else if (length > 0) {
					btn_send.setEnabled(true);
					text_counter.setTextColor(Color.rgb(153, 153, 153));
				} else {
					btn_send.setEnabled(false);
					text_counter.setTextColor(Color.rgb(153, 153, 153));
				}
			}

		});

		toolbar_face.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (face_is_open) {
					toolbar_face
							.setBackgroundResource(R.drawable.btn_selector_edit_face);

					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
											| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
					layout_faces.setVisibility(View.GONE);

					face_is_open = false;
				} else {
					toolbar_face
							.setBackgroundResource(R.drawable.btn_selector_edit_keyboard);

					getWindow()
							.setSoftInputMode(
									WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
											| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
					layout_faces.setVisibility(View.VISIBLE);
					GroupDynamicEditActivity.super.hideSoftInput(edit_content);

					face_is_open = true;
				}
			}
		});

		OnClickListener faceOCL = new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView img_face = (ImageView) v;
				String face_name = img_face.getTag().toString();
				String CurrentStr = edit_content.getText().toString();
				String SharpStr = CurrentStr + "[" + face_name + "]";
				edit_content.setText(SharpStr);
				edit_content.setSelection(SharpStr.length(), SharpStr.length());
			}
		};
		for (int i = 0; i < face_names.length; i++) {
			Resources resources = getResources();
			int imageResource = resources.getIdentifier(getPackageName()
					+ ":id/img_fece" + (i + 1), null, null);
			ImageView img_face = (ImageView) findViewById(imageResource);
			if (img_face != null) {
				img_face.setTag(face_names[i]);
				img_face.setOnClickListener(faceOCL);
			}
		}

	}

	@Override
	public void ensureUi() {
		layout_faces.setVisibility(View.GONE);
		btn_send.setEnabled(false);
		super.openKeyboard();

		toolbar_face.setTag("");
			navbar_TitleText.setText("发布动态");

			edit_title.setVisibility(View.GONE);
			img_split.setVisibility(View.GONE);
			edit_content.setHint("说点什么吧");
			toolbar_swf.setVisibility(View.GONE);
			toolbar_link.setVisibility(View.GONE);

	}

	CallBack callback = new CallBack() {

		@Override
		public void onSuccess(String response) {
			dismissProgress();
			int status = 0;
			String msg = "";
			try {
				JSONObject jsonObject = new JSONObject(response);
				status = jsonObject.optInt("status");
				msg = jsonObject.optString("msg");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			btn_send.setEnabled(true);

			if (status > 0) {
				NotificationsUtil.ToastBottomMsg(mActivity, "操作成功");
				Intent intent = new Intent()
						.setAction(Params.INTENT_ACTION.DYNAMICLIST);
				sendBroadcast(intent);

				finish();
			} else if (0 == status) {
				NotificationsUtil.ToastBottomMsg(mActivity, msg);
			} else {
				Response r = JSONUtils.fromJson(response, Response.class);

				if (null != r) {
					if (!TextUtils.isEmpty(r.message)) {
						NotificationsUtil.ToastBottomMsg(mActivity, r.message);
					} else {
						NotificationsUtil.ToastBottomMsg(mActivity, "操作失败");
					}
				}
			}
		}

		@Override
		public void onFailure(String message) {
			dismissProgress();
			btn_send.setEnabled(true);
			NotificationsUtil.ToastBottomMsg(mActivity, "操作失败");
		}
	};

	CallBack uploadcallback = new CallBack() {

		@Override
		public void onSuccess(String response) {
			int reponse_id = 0;
			try {
				reponse_id = Integer.parseInt(response);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}

			if (reponse_id > 0) {
				VolleyLog.d("success:%d", reponse_id);
			} else {
				VolleyLog.e("error");
				NotificationsUtil.ToastBottomMsg(mActivity, "操作失败");
			}
		}

		@Override
		public void onFailure(String message) {
			dismissProgress();
			NotificationsUtil.ToastBottomMsg(mActivity, "操作失败");
		}
	};

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
