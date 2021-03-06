package com.xyhui.activity.group;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.types.Response;
import com.xyhui.utils.Params;
import com.xyhui.widget.CallbackBundle;
import com.xyhui.widget.FLActivity;
import com.xyhui.widget.OpenFileDialog;

public class GroupDocUploadActivity extends FLActivity {

	private static int openfileDialogId = 0;

	private Button btn_back;

	private Button btn_select_file;

	private TextView text_title;

	private String file;

	private LinearLayout btn_upload;

	private String mGroupId;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.GROUP_ID)) {
			mGroupId = getIntent().getStringExtra(Params.INTENT_EXTRA.GROUP_ID);
		} else {
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_doc_upload);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_select_file = (Button) findViewById(R.id.btn_select_file);

		text_title = (TextView) findViewById(R.id.text_title);

		btn_upload = (LinearLayout) findViewById(R.id.btn_upload);

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

		btn_select_file.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(openfileDialogId);
			}
		});

		btn_upload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(file)) {
					return;
				}

				showProgress("提示", "正在上传,请稍候...");

				new Api(callback, mActivity).uploadfile(PuApp.get().getToken(), mGroupId, file);
			}
		});
	}

	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();

			Response r = JSONUtils.fromJson(response, Response.class);

			if (null != r) {
				if (!TextUtils.isEmpty(r.response)) {
					NotificationsUtil.ToastBottomMsg(mActivity, r.response);
					finish();
				} else if (!TextUtils.isEmpty(r.message)) {
					NotificationsUtil.ToastBottomMsg(mActivity, r.message);
				}
			}
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == openfileDialogId) {
			Map<String, Integer> images = new HashMap<String, Integer>();
			images.put(OpenFileDialog.ROOT, R.drawable.filedialog_root);
			images.put(OpenFileDialog.PARENT, R.drawable.filedialog_folder_up);
			images.put(OpenFileDialog.CURRENT, R.drawable.filedialog_folder);
			images.put(OpenFileDialog.EMPTY, R.drawable.filedialog_root);
			Dialog dialog = OpenFileDialog.createDialog(id, this, "选择文件", new CallbackBundle() {
				@Override
				public void callback(Bundle bundle) {
					String filepath = bundle.getString("path");
					Uri uri = Uri.parse(filepath);
					String filename = uri.getLastPathSegment();
					file = filepath;
					text_title.setText(filepath);
					VolleyLog.d("%s : %s", filepath, filename);
				}
			}, ".jpg;.jpeg;.png;.doc;.docx;.bmp;.zip;.rar;.xls;.ppt;.xlsx;.pptx;.pdf;", images);
			return dialog;
		}
		return null;
	}

	@Override
	public void ensureUi() {
	}

}
