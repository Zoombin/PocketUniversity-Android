// filename: OpenFileDialog.java
package com.xyhui.widget;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.xyhui.R;

public class OpenFileDialog {
	public static final String ROOT = "/";
	public static final String PARENT = "..";
	public static final String CURRENT = ".";
	public static final String EMPTY = "";
	private static final String ERROR_MSG = "No rights to access!";

	public static Dialog createDialog(int id, Context context, String title,
			CallbackBundle callback, String suffix, Map<String, Integer> images) {
		return new AlertDialog.Builder(context)
				.setView(new FileSelectView(context, id, callback, suffix, images))
				.setTitle(title).create();
	}

	static class FileSelectView extends ListView implements OnItemClickListener {

		private CallbackBundle mCallback;
		private String mPath = ROOT;
		private List<Map<String, Object>> mList;
		private int mDialogID = 0;

		private String mSuffix;

		private Map<String, Integer> mImageMap;

		public FileSelectView(Context context, int dialogid, CallbackBundle callback,
				String suffix, Map<String, Integer> images) {
			super(context);
			mImageMap = images;
			mSuffix = suffix == null ? "" : suffix.toLowerCase();
			mCallback = callback;
			mDialogID = dialogid;
			setOnItemClickListener(this);
			refreshFileList();
		}

		private String getSuffix(String filename) {
			int dix = filename.lastIndexOf('.');
			if (dix < 0) {
				return "";
			} else {
				return filename.substring(dix + 1);
			}
		}

		private int getImageId(String s) {
			if (mImageMap == null) {
				return 0;
			} else if (mImageMap.containsKey(s)) {
				return mImageMap.get(s);
			} else if (mImageMap.containsKey(EMPTY)) {
				return mImageMap.get(EMPTY);
			} else {
				return 0;
			}
		}

		private int refreshFileList() {
			File[] files = null;
			try {
				files = new File(mPath).listFiles();
			} catch (Exception e) {
				files = null;
			}
			if (files == null) {
				Toast.makeText(getContext(), ERROR_MSG, Toast.LENGTH_SHORT).show();
				return -1;
			}
			if (mList != null) {
				mList.clear();
			} else {
				mList = new ArrayList<Map<String, Object>>(files.length);
			}

			ArrayList<Map<String, Object>> lfolders = new ArrayList<Map<String, Object>>();
			ArrayList<Map<String, Object>> lfiles = new ArrayList<Map<String, Object>>();

			if (!mPath.equals(ROOT)) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", ROOT);
				map.put("path", ROOT);
				map.put("img", getImageId(ROOT));
				mList.add(map);

				map = new HashMap<String, Object>();
				map.put("name", PARENT);
				map.put("path", mPath);
				map.put("img", getImageId(PARENT));
				mList.add(map);
			}

			for (File file : files) {
				if (file.isDirectory() && file.listFiles() != null) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("name", file.getName());
					map.put("path", file.getPath());
					map.put("img", getImageId(CURRENT));
					lfolders.add(map);
				} else if (file.isFile()) {
					String sf = getSuffix(file.getName()).toLowerCase();
					if (mSuffix == null || mSuffix.length() == 0
							|| (sf.length() > 0 && mSuffix.indexOf("." + sf + ";") >= 0)) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("name", file.getName());
						map.put("path", file.getPath());
						map.put("img", getImageId(sf));
						lfiles.add(map);
					}
				}
			}

			mList.addAll(lfolders);
			mList.addAll(lfiles);

			SimpleAdapter adapter = new SimpleAdapter(getContext(), mList,
					R.layout.filedialogitem, new String[] { "img", "name", "path" }, new int[] {
							R.id.filedialogitem_img, R.id.filedialogitem_name,
							R.id.filedialogitem_path });
			setAdapter(adapter);
			return files.length;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			String pt = (String) mList.get(position).get("path");
			String fn = (String) mList.get(position).get("name");
			if (fn.equals(ROOT) || fn.equals(PARENT)) {
				File fl = new File(pt);
				String ppt = fl.getParent();
				if (ppt != null) {
					mPath = ppt;
				} else {
					mPath = ROOT;
				}
			} else {
				File fl = new File(pt);
				if (fl.isFile()) {
					((Activity) getContext()).dismissDialog(mDialogID);

					Bundle bundle = new Bundle();
					bundle.putString("path", pt);
					bundle.putString("name", fn);
					mCallback.callback(bundle);
					return;
				} else if (fl.isDirectory()) {
					mPath = pt;
				}
			}
			refreshFileList();
		}
	}
}
