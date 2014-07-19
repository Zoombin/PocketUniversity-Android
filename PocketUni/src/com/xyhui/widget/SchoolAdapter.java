package com.xyhui.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.xyhui.R;
import com.xyhui.types.School;
import com.xyhui.utils.Utils;

/**
 * 学校Adapter实现
 * 
 */
public class SchoolAdapter extends ArrayAdapter<School> implements
		SectionIndexer {

	private LayoutInflater layoutInflater;
	private EditText query;
	private ImageButton clearSearch;
	private SparseIntArray positionOfSection;
	private SparseIntArray sectionOfPosition;
	private Sidebar sidebar;
	private int res;
	
	public SchoolAdapter(Context context, int resource, List<School> objects,
			Sidebar sidebar) {
		super(context, resource, objects);
		this.res = resource;
		this.sidebar = sidebar;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return position == 0 ? 0 : 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position == 0) {// 搜索框
			if (convertView == null) {
				convertView = layoutInflater.inflate(
						R.layout.search_bar_with_padding, null);
				query = (EditText) convertView.findViewById(R.id.query);
				clearSearch = (ImageButton) convertView
						.findViewById(R.id.search_clear);
				query.addTextChangedListener(new TextWatcher() {
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
						getFilter().filter(s);
						if (s.length() > 0) {
							clearSearch.setVisibility(View.VISIBLE);
							if (sidebar != null)
								sidebar.setVisibility(View.GONE);
						} else {
							clearSearch.setVisibility(View.INVISIBLE);
							if (sidebar != null)
								sidebar.setVisibility(View.VISIBLE);
						}
					}

					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					public void afterTextChanged(Editable s) {
					}
				});
				clearSearch.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						InputMethodManager manager = (InputMethodManager) getContext()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						if (((Activity) getContext()).getWindow()
								.getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
							manager.hideSoftInputFromWindow(
									((Activity) getContext()).getCurrentFocus()
											.getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
						// 清除搜索框文字
						query.getText().clear();
					}
				});
			}
		} else {
			if (convertView == null) {
				convertView = layoutInflater.inflate(res, null);
			}

			TextView tvHeader = (TextView) convertView
					.findViewById(R.id.header);
			TextView tv_schoolname = (TextView) convertView
					.findViewById(R.id.schoolname);
			ImageView iv_schoolselect = (ImageView) convertView
					.findViewById(R.id.schoolselect);

			School school = getItem(position);
			// 设置nick，demo里不涉及到完整user，用username代替nick显示
			String schoolname = school.name;
			String header = Utils.getPinYinHeadChar(school.name);
			if (position == 0 || header != null && !header.equals(Utils.getPinYinHeadChar(getItem(position - 1).name))) {
				if ("".equals(header)) {
					tvHeader.setVisibility(View.GONE);
				} else {
					tvHeader.setVisibility(View.VISIBLE);
					tvHeader.setText(header);
				}
			} else {
				tvHeader.setVisibility(View.GONE);
			}
			tv_schoolname.setText(schoolname);
			// if (unreadMsgView != null)
			iv_schoolselect.setVisibility(View.INVISIBLE);
		}

		return convertView;
	}

	@Override
	public School getItem(int position) {
		return position == 0 ? new School() : super.getItem(position - 1);
	}

	@Override
	public int getCount() {
		// 有搜索框，cout+1
		return super.getCount() + 1;
	}

	public int getPositionForSection(int section) {
		return positionOfSection.get(section);
	}

	public int getSectionForPosition(int position) {
		return sectionOfPosition.get(position);
	}

	@Override
	public Object[] getSections() {
		positionOfSection = new SparseIntArray();
		sectionOfPosition = new SparseIntArray();
		int count = getCount();
		List<String> list = new ArrayList<String>();
		list.add("S");
		positionOfSection.put(0, 0);
		sectionOfPosition.put(0, 0);
		for (int i = 1; i < count; i++) {

			String letter = "afd";// getItem(i).getUname();
			int section = list.size() - 1;
			if (list.get(section) != null && !list.get(section).equals(letter)) {
				list.add(letter);
				section++;
				positionOfSection.put(section, i);
			}
			sectionOfPosition.put(i, section);
		}
		return list.toArray(new String[list.size()]);
	}
}
