package com.xyhui.activity.group;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.mslibs.utils.JSONUtils;
import com.mslibs.utils.NotificationsUtil;
import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.PuApp;
import com.xyhui.activity.more.PhoneBindingActivity;
import com.xyhui.api.Api;
import com.xyhui.api.CallBack;
import com.xyhui.api.Client;
import com.xyhui.types.DetailGroup;
import com.xyhui.types.DetailGroups;
import com.xyhui.types.GroupTopic;
import com.xyhui.types.GroupTopics;
import com.xyhui.types.Response;
import com.xyhui.utils.Params;
import com.xyhui.utils.PrefUtil;
import com.xyhui.utils.SpanUtils;
import com.xyhui.widget.FLActivity;

public class GroupViewActivity extends FLActivity {
	private Button btn_back;
	private Button btn_more;

	private ImageView img_avatar;
	private TextView text_group_name;
	private TextView text_group_info;

	private Button btn_group;

	private LinearLayout btn_params_blog;
	private TextView text_params_blog;

	private LinearLayout btn_params_event;
	private TextView text_params_event;

	private LinearLayout btn_params_dynamic;
	private TextView text_params_dynamic;

	private LinearLayout btn_params_user;
	private TextView text_params_user;

	private TextView text_notice;
	private TextView text_intro;

	private LinearLayout blog_layout;
	private LinearLayout new_bloglist_layout;
	private LinearLayout blog_getmore_layout;

	private LinearLayout unverify_layout;
	private TextView text_unverify;

	private String mGroupId;

	private DetailGroup mDetailGroup;

	private int mIsMemeber = 0;
	private boolean isAdmin = false;

	private boolean isCreator = false;

	public static final String IS_GROUP_CREATOR = "is_group_creator";

	private Builder builder;

	@Override
	public void init() {
		if (getIntent().hasExtra(Params.INTENT_EXTRA.GROUP_ID)) {
			mGroupId = getIntent().getStringExtra(Params.INTENT_EXTRA.GROUP_ID);
			VolleyLog.d("got groupid:%s", mGroupId);
		} else {
			VolleyLog.d("no groupid");
			finish();
			return;
		}
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_group_view);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_more = (Button) findViewById(R.id.btn_more);
		img_avatar = (ImageView) findViewById(R.id.img_avatar);
		text_group_name = (TextView) findViewById(R.id.text_group_name);
		text_group_info = (TextView) findViewById(R.id.text_group_info);
		btn_group = (Button) findViewById(R.id.btn_group);
		btn_params_blog = (LinearLayout) findViewById(R.id.btn_params_blog);
		text_params_blog = (TextView) findViewById(R.id.text_params_blog);
		btn_params_event = (LinearLayout) findViewById(R.id.btn_params_event);
		text_params_event = (TextView) findViewById(R.id.text_params_event);
		btn_params_dynamic = (LinearLayout) findViewById(R.id.btn_params_dynamic);
		text_params_dynamic = (TextView) findViewById(R.id.text_params_dynamic);
		btn_params_user = (LinearLayout) findViewById(R.id.btn_params_user);
		text_params_user = (TextView) findViewById(R.id.text_params_user);
		text_notice = (TextView) findViewById(R.id.text_notice);
		text_notice.setOnClickListener(clickListener);
		text_intro = (TextView) findViewById(R.id.text_intro);
		blog_layout = (LinearLayout) findViewById(R.id.blog_layout);
		new_bloglist_layout = (LinearLayout) findViewById(R.id.new_bloglist_layout);
		blog_getmore_layout = (LinearLayout) findViewById(R.id.blog_getmore_layout);
		unverify_layout = (LinearLayout) findViewById(R.id.unverify_layout);
		text_unverify = (TextView) findViewById(R.id.text_unverify);
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.text_notice: {
				Intent intent = new Intent(mActivity,
						GroupNoticeListActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.GROUP_ID, mGroupId);
				startActivity(intent);
			}
				break;

			default:
				break;
			}
		}
	};

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
				showPanel();
			}
		});

		btn_group.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Button btn = (Button) v;
				btn.setEnabled(false);

				int isJoin = (Integer) btn.getTag();
				if (isJoin == 1) {
					// 离开部落
					new AlertDialog.Builder(mActivity)
							.setMessage("确定要退出这个部落吗?")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											showProgress();
											new Api(joincallback, mActivity)
													.quitgroup(PuApp.get()
															.getToken(),
															mGroupId);
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int whichButton) {
											btn.setEnabled(true);
											dialog.dismiss();
										}
									}).setCancelable(false).show();
				} else {
					String mobile = new PrefUtil()
							.getPreference(Params.LOCAL.MOBILE);
					if (TextUtils.isEmpty(mobile)) {
						btn.setEnabled(true);

						new AlertDialog.Builder(mActivity)
								.setTitle("您还没有绑定手机, 现在去绑定吗?")
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {
												Intent intent = new Intent(
														mActivity,
														PhoneBindingActivity.class);
												startActivity(intent);
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton) {
											}
										}).show();
					} else {
						// 加入部落
						showProgress();
						new Api(joincallback, mActivity).joingroup(PuApp.get()
								.getToken(), mGroupId);
					}
				}
			}
		});

		blog_getmore_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开文章列表
				Intent intent = new Intent(mActivity,
						GroupBlogListActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.GROUP_ID, mGroupId);
				intent.putExtra(Params.INTENT_EXTRA.GROUPMEMBER, mIsMemeber);
				startActivity(intent);
			}
		});

		btn_params_blog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开文章列表
				Intent intent = new Intent(mActivity,
						GroupBlogListActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.GROUP_ID, mGroupId);
				intent.putExtra(Params.INTENT_EXTRA.GROUPMEMBER, mIsMemeber);
				startActivity(intent);
			}
		});

		btn_params_event.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 活动
				// 打开文章列表
				Intent intent = new Intent(mActivity,
						GroupEventListActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.GROUP_ID, mGroupId);
				startActivity(intent);
			}
		});

		btn_params_user.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 打开成员列表
				Intent intent = new Intent(mActivity,
						GroupUserListActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.GROUP_ID, mGroupId);
				intent.putExtra(Params.INTENT_EXTRA.GROUPMEMBER, mIsMemeber);
				startActivity(intent);
			}
		});

		btn_params_dynamic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 动态
				Intent intent = new Intent(mActivity,
						GroupDynamicListActivity.class);
				intent.putExtra(Params.INTENT_EXTRA.GROUP_ID, mGroupId);
				startActivity(intent);
			}
		});

		unverify_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 无事件，屏蔽后面的点击事件
			}
		});
	}

	@Override
	public void ensureUi() {
		unverify_layout.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		loadGroup();
	}

	public void loadGroup() {
		showProgress();
		new Api(callback, mActivity).group(PuApp.get().getToken(), mGroupId);
	}

	private void showPanel() {
		builder = new AlertDialog.Builder(mActivity);
		builder.setTitle("选择操作");

		if (isAdmin) {
			builder.setItems(R.array.group_operate_admin,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {

							Intent intent = new Intent();

							switch (which) {
							case 0:
								// 邀请好友
								intent.setClass(mActivity,
										GroupInviteListActivity.class);
								intent.putExtra(Params.INTENT_EXTRA.GROUP_ID,
										mGroupId);
								startActivity(intent);
								break;
							case 1:
								// 修改公告
								intent.setClass(mActivity,
										GroupNoticeEditActivity.class);
								intent.putExtra(Params.INTENT_EXTRA.GROUP_ID,
										mGroupId);
								startActivity(intent);
								break;
							case 2:
								// 部落设置
								intent.setClass(mActivity,
										GroupEditActivity.class);
								intent.putExtra(Params.INTENT_EXTRA.GROUP,
										mDetailGroup);
								startActivity(intent);
								break;
							case 3:
								// 成员管理
								intent.setClass(mActivity,
										GroupMemberManageActivity.class);
								intent.putExtra(Params.INTENT_EXTRA.GROUP_ID,
										mGroupId);
								intent.putExtra(IS_GROUP_CREATOR, isCreator);
								startActivity(intent);
								break;
							}

							dialog.cancel();
						}
					}).show();
		} else {
			builder.setItems(R.array.group_operate_normal,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if (0 == which) {
								// 邀请好友
								Intent intent = new Intent(mActivity,
										GroupInviteListActivity.class);
								intent.putExtra(Params.INTENT_EXTRA.GROUP_ID,
										mGroupId);
								startActivity(intent);
							}

							dialog.cancel();
						}
					}).show();
		}
	}

	CallBack callback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();
			DetailGroups group = JSONUtils.fromJson(response,
					DetailGroups.class);

			if (null == group)
				return;

			if (!TextUtils.isEmpty(group.message)) {
				unverify_layout.setVisibility(View.VISIBLE);
				text_unverify.setText(group.message);
				btn_more.setVisibility(View.GONE);
				return;
			}

			mDetailGroup = group.data;

			if (mDetailGroup == null)
				return;

			if (mDetailGroup.isadmin == 1) {
				isAdmin = true;
			} else {
				isAdmin = false;
			}

			String localUid = new PrefUtil().getPreference(Params.LOCAL.UID);
			isCreator = mDetailGroup.uid.equals(localUid);

			btn_group.setTag(mDetailGroup.ismember);
			if (mDetailGroup.ismember == 1) {
				mIsMemeber = 1;
				btn_group
						.setBackgroundResource(R.drawable.btn_selector_group_leave);
				btn_more.setVisibility(View.VISIBLE);
			} else {
				btn_group
						.setBackgroundResource(R.drawable.btn_selector_group_join);
				btn_more.setVisibility(View.GONE);
				btn_params_blog.setEnabled(false);
				btn_params_event.setEnabled(false);
				btn_params_user.setEnabled(false);
				btn_params_dynamic.setEnabled(false);
				blog_layout.setVisibility(View.GONE);
			}

			btn_group.setVisibility(View.VISIBLE);
			btn_group.setEnabled(true);

			if (!TextUtils.isEmpty(mDetailGroup.logo)
					&& !"default.gif".equalsIgnoreCase(mDetailGroup.logo)) {
				String logo = Client.UPLOAD_URL + mDetailGroup.logo;
				VolleyLog.d(logo);
				UrlImageViewHelper.setUrlDrawable(img_avatar, logo,
						R.drawable.group_avatar);
			}

			text_group_name.setText(mDetailGroup.name);

			String info = "";

			if (mDetailGroup.tags != null) {
				info += "标签: " + TextUtils.join(" ", mDetailGroup.tags);
			}
			if (mDetailGroup.usrename != null) {
				info += "\n部落主席: " + mDetailGroup.usrename;
			}

			info += "\n创建时间：" + SpanUtils.getTimeString(mDetailGroup.ctime);

			if (!TextUtils.isEmpty(mDetailGroup.schoolname)) {
				info += "\n学校: " + mDetailGroup.schoolname;
			}

			if (mDetailGroup.cname0 != null) {
				info += "\n分类: " + mDetailGroup.cname0;
				if (mDetailGroup.cname1 != null) {
					info += " " + mDetailGroup.cname1;
				}
			}

			text_group_info.setText(info);

			// text_params_blog.setText(mDetailGroup.topiccount);
			//
			// text_params_event.setText(mDetailGroup.filecount);
			//
			// text_params_user.setText(mDetailGroup.membercount);
			//
			// text_params_event.setText(mDetailGroup.filecount);

			text_notice.setText(mDetailGroup.announce);

			text_intro.setText(mDetailGroup.intro);

			showProgress();
			new Api(bloglistcallback, mActivity).grouptopic(PuApp.get()
					.getToken(), mGroupId, 5, 1);
		}
	};

	CallBack bloglistcallback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();

			GroupTopics items = JSONUtils.fromJson(response, GroupTopics.class);
			LayoutInflater mInflater = (LayoutInflater) getBaseContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			new_bloglist_layout.removeAllViews();
			if (items != null && items.data != null && !items.data.isEmpty()) {
				for (int i = 0; i < items.data.size(); i++) {
					GroupTopic item = (GroupTopic) items.data.get(i);

					View blogItemView = mInflater.inflate(
							R.layout.list_item_group_blog, null);
					blogItemView.setTag(item);
					blogItemView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							GroupTopic item = (GroupTopic) v.getTag();
							// 打开文章浏览
							Intent intent = new Intent(mActivity,
									GroupBlogViewListActivity.class);
							intent.putExtra(Params.INTENT_EXTRA.BLOG_ID,
									item.id);
							intent.putExtra(Params.INTENT_EXTRA.GROUP_ID,
									mGroupId);
							startActivity(intent);
						}
					});

					if (blogItemView != null) {
						TextView text_title = (TextView) blogItemView
								.findViewById(R.id.text_title);
						text_title.setText(item.title);

						TextView text_info = (TextView) blogItemView
								.findViewById(R.id.text_info);
						text_info.setText(item.getDesc());
					}

					new_bloglist_layout.addView(blogItemView);
				}
			}

		}
	};

	CallBack joincallback = new CallBack() {
		@Override
		public void onSuccess(String response) {
			dismissProgress();

			Response r = JSONUtils.fromJson(response, Response.class);

			if (null != r) {
				if (!TextUtils.isEmpty(r.response)) {
					NotificationsUtil.ToastBottomMsg(mActivity, r.response);
					loadGroup();
				} else if (!TextUtils.isEmpty(r.message)) {
					NotificationsUtil.ToastBottomMsg(mActivity, r.message);
				}
			}
		}
	};

	CallBack delcallback = new CallBack() {
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
}
