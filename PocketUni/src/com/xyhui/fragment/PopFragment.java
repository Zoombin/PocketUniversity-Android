package com.xyhui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xyhui.R;
import com.xyhui.activity.app.TupopList;

public class PopFragment extends Fragment {

	private PullToRefreshListView pop_view_listview;
	private TupopList tuPopListView;
	private Activity mActivity;
	private int type;
	private int sid;
	private int page;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		if (bundle != null) {
			type = bundle.getInt("type", 0);
		}
		
		View v = inflater.inflate(R.layout.fragment_pop, null);
		
		pop_view_listview = (PullToRefreshListView) v.findViewById(R.id.pop_view_listview);
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if (tuPopListView != null) {
			tuPopListView.search(type, sid, page);
		} else {
			tuPopListView = new TupopList(pop_view_listview, mActivity, type, sid, page);
		}
	}
}
