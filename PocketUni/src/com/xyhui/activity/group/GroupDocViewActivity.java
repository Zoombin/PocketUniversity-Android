package com.xyhui.activity.group;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.api.Client;
import com.xyhui.types.GroupDoc;
import com.xyhui.utils.DownloadImpl;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class GroupDocViewActivity extends FLActivity {
	private Button btn_back;
	private ImageView img_fileicon;
	private TextView text_title;
	private TextView text_info;
	private LinearLayout btn_download;

	private GroupDoc doc;

	private TextView text_path;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.DOC)) {
			doc = getIntent().getParcelableExtra(Params.INTENT_EXTRA.DOC);
		} else {
			VolleyLog.d("no doc");
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_doc_view);

		btn_back = (Button) findViewById(R.id.btn_back);
		img_fileicon = (ImageView) findViewById(R.id.img_fileicon);
		text_title = (TextView) findViewById(R.id.text_title);
		text_info = (TextView) findViewById(R.id.text_info);
		btn_download = (LinearLayout) findViewById(R.id.btn_download);
		text_path = (TextView) findViewById(R.id.text_path);
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

		btn_download.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse(doc.fileurl);
				String filename = uri.getLastPathSegment();
				String url = Client.REAL_UPLOAD_URL + doc.fileurl;

				DownloadImpl download = new DownloadImpl(mActivity, url, doc.name, filename);
				download.startDownload();
			}
		});
	}

	@Override
	public void ensureUi() {
		// 文件log 根据类型选择图片

		String[] file_types = new String[] { "doc", "aac", "wmv", "psd", "mpg", "jpeg", "docx",
				"wav", "png", "mpeg", "mov", "html", "avi", "txt", "php", "mp4", "m4a", "gzip",
				"aiff", "tiff", "rtf", "pdf", "js", "gif", "ai", "zip", "tar", "raw", "m4v",
				"jpg", "css" };
		String default_file_type = "generic";

		int v = -1;

		for (int i = 0; i < file_types.length; i++) {
			if (file_types[i].equalsIgnoreCase(doc.filetype)) {
				v = i;
				break;
			}
		}
		if (v > 0) {
			default_file_type = file_types[v];
		}
		Resources resources = getResources();
		int imageResource = resources.getIdentifier(getPackageName() + ":drawable/"
				+ default_file_type, null, null);

		text_title.setText(doc.name);

		text_info.setText(doc.getDesc());

		Uri uri = Uri.parse(doc.fileurl);
		String filename = uri.getLastPathSegment();
		String path = "/" + Environment.DIRECTORY_DOWNLOADS + "/" + filename;

		text_path.setText("文件下载后，将保存到您的外部存储空间：\n" + path);

		img_fileicon.setImageResource(imageResource);
	}

}
