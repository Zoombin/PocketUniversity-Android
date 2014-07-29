package com.xyhui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xyhui.R;
import com.xyhui.activity.TabWeiboActivity;
import com.xyhui.activity.app.ChargeActivity;
import com.xyhui.activity.app.ShakeActivity;
import com.xyhui.activity.weibo.WeiboEditActivity;
import com.xyhui.utils.Params;

/**
 * menu对话框
 * @author 烨
 *
 */
public class MenuDialog implements OnClickListener {

	private Dialog dialog;
	private Activity activity;
	
	public MenuDialog(Activity activity) {
		this.activity = activity;
		
		LayoutInflater mInflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.dlg_share, null);
		
		LinearLayout item_weibo = (LinearLayout) view.findViewById(R.id.item_weibo);
		LinearLayout item_shake = (LinearLayout) view.findViewById(R.id.item_shake);
		LinearLayout item_charge = (LinearLayout) view.findViewById(R.id.item_charge);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		item_weibo.setOnClickListener(this);
		item_shake.setOnClickListener(this);
		item_charge.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		
		dialog = new Dialog(activity, R.style.ShareDialogStyleBottom);
		dialog.setContentView(view);
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.item_weibo:
			// 打开发微博窗体
			intent.setClass(activity, WeiboEditActivity.class);
			intent.putExtra(Params.INTENT_EXTRA.WEIBO_EDIT, Params.INTENT_VALUE.WEIBO_NEW);
			activity.startActivity(intent);
			dialog.dismiss();
			break;
		case R.id.item_shake:
			intent.setClass(activity, ShakeActivity.class);
			activity.startActivity(intent);
			dialog.dismiss();
			break;
		case R.id.item_charge:
			intent.setClass(activity, ChargeActivity.class);
			activity.startActivity(intent);
			dialog.dismiss();
			break;
		case R.id.btn_cancel:
			dialog.dismiss();
			break;
		default:
			break;
		}
	}
}
