package com.mslibs.widget;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class CListViewAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private int mItemResource;
	private int mHeaderResource = 0;
	private int mFooterResource = 0;
	private int mSingleResource = 0;
	private int mGetMoreResource = 0;

	private int mSelectedResourcem = 0;
	private int mSelectedIndex = -1;

	public void setSelectedResourcem(int res) {
		mSelectedResourcem = res;
	}

	public void setSelectedIndex(int i) {
		mSelectedIndex = i;
	}

	public boolean isNotMore = false;

	private OnClickListener onItemOnclickLinstener = null;
	private OnClickListener onGetMoreClickListener = null;

	public boolean ItemViewEmptyInvisible = false;
	private ArrayList<ArrayList<CListViewParam>> mDateList;

	// 仅对打开子版块使用
	public int mChildForumResource = 0;
	public int isOpenChildForumTag;

	public CListViewAdapter(Activity activity, int resouce) {
		mItemResource = resouce;
		mInflater = (LayoutInflater) activity.getBaseContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
	}

	public CListViewAdapter(Context context, int resouce) {
		mItemResource = resouce;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData(ArrayList<ArrayList<CListViewParam>> LVPs) {
		mDateList = LVPs;
	}

	public void setItemOnclickLinstener(OnClickListener onClickListener) {
		onItemOnclickLinstener = onClickListener;
	}

	public void setGetMoreResource(int resouce) {
		mGetMoreResource = resouce;
	}

	public void setHeaderResource(int resouce) {
		mHeaderResource = resouce;
	}

	public void setFooterResource(int resouce) {
		mFooterResource = resouce;
	}

	public void setSingleResource(int resouce) {
		mSingleResource = resouce;
	}

	public void setGetMoreClickListener(OnClickListener onClickListener) {
		onGetMoreClickListener = onClickListener;
	}

	@Override
	public int getCount() {
		return mDateList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDateList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View newView = null;
		if (newView == null) {
			if (mDateList.size() == 1) {
				// 单个数据
				if (mSingleResource != 0) {
					newView = mInflater.inflate(mSingleResource, null);

					if (onItemOnclickLinstener != null) {
						newView.setOnClickListener(onItemOnclickLinstener);
					}
				}
			} else {
				// 多个数据
				if (position == 0) {
					// 加载头部
					if (mHeaderResource != 0) {
						newView = mInflater.inflate(mHeaderResource, null);

						if (onItemOnclickLinstener != null) {
							newView.setOnClickListener(onItemOnclickLinstener);
						}
					}
				} else if (position == mDateList.size() - 1) {

					if (isNotMore == true) {
						// 加载尾部
						if (mFooterResource != 0) {
							newView = mInflater.inflate(mFooterResource, null);

							if (onItemOnclickLinstener != null) {
								newView.setOnClickListener(onItemOnclickLinstener);
							}
						}
					} else {
						// 加载更多
						if (mGetMoreResource != 0) {
							newView = mInflater.inflate(mGetMoreResource, null);

							if (onGetMoreClickListener != null) {
								newView.setOnClickListener(onGetMoreClickListener);
							}
						}
					}
				}
			}

			// 加载正常行
			if (newView == null && mItemResource != 0) {
				newView = mInflater.inflate(mItemResource, null);

				if (onItemOnclickLinstener != null) {
					newView.setOnClickListener(onItemOnclickLinstener);
				}
			}
		}

		// 绑定编号
		newView.setTag(position);

		bindItemData(newView, mDateList.get(position));

		// 仅对打开子版块处理
		if (mChildForumResource != 0) {
			View childView = newView.findViewById(mChildForumResource);
			if (position != isOpenChildForumTag) {
				childView.setVisibility(View.GONE);
			} else {
				childView.setVisibility(View.VISIBLE);
			}
		}

		// 选择状态
		if (mSelectedResourcem > 0) {
			View v = newView.findViewById(mSelectedResourcem);
			if (v != null) {
				if (mSelectedIndex == position) {
					v.setVisibility(View.VISIBLE);
				} else {
					v.setVisibility(View.GONE);
				}
			}
		}

		return newView;
	}

	// 绑定行的数据
	private void bindItemData(View view, ArrayList<CListViewParam> LVP) {
		for (int i = 0; i < LVP.size(); i++) {
			View v = view.findViewById(LVP.get(i).getItemRsID());
			bindViewData(v, LVP.get(i));
		}
	}

	// 绑定单个控件的数据
	private void bindViewData(View view, CListViewParam iLVP) {
		if (view != null) {
			// 设置点击事件
			if (iLVP.getOnclickListner() != null) {
				view.setOnClickListener(iLVP.getOnclickListner());
			}

			// 设置是否显示
			if (iLVP.isSetVisibility()) {
				if (iLVP.getVisibility() == true) {
					view.setVisibility(View.VISIBLE);
				} else {
					view.setVisibility(View.GONE);
				}
			} else {
				if (ItemViewEmptyInvisible) {
					if (iLVP.getDate() == null || iLVP.getDate().toString().length() == 0) {
						view.setVisibility(View.GONE);
					}
				}
			}

			// 设置Tag值
			if (iLVP.getItemTag() != null)
				view.setTag(iLVP.getItemTag());

			// 设置Date值
			if (iLVP.getDate() != null && iLVP.getDate().toString().length() > 0) {

				if (view instanceof Button) {
					if (iLVP.getDate() instanceof String) {
						((Button) view).setText(iLVP.getDate().toString());
					} else if (iLVP.getDate() instanceof Integer) {
						((Button) view).setBackgroundResource(Integer.parseInt(iLVP.getDate()
								.toString()));
					}
				} else if (view instanceof ImageButton) {
					((ImageButton) view).setImageResource(Integer.parseInt(iLVP.getDate()
							.toString()));
				} else if (view instanceof TextView) {
					if (iLVP.getDate() instanceof Spanned) {
						((TextView) view).setText((Spanned) iLVP.getDate());
						((TextView) view).setMovementMethod(LinkMovementMethod.getInstance());
					} else {
						((TextView) view).setText(iLVP.getDate().toString());
					}
				} else if (view instanceof CSpannedTextViewBase) {
					// VolleyLog.d("CListViewAdapter: instanceof CSpannedTextViewBase");
					if (iLVP.getDate() != null) {
					}
					((CSpannedTextViewBase) view).setData(iLVP.getDate());
				} else if (view instanceof ImageView) {
					ImageView imgView = (ImageView) view;
					imgView.setImageResource(Integer.parseInt(iLVP.getDate().toString()));

					if (iLVP.getImgAsync()) {
						// VolleyLog.d("getImgAsync: %s", iLVP.getItemTag());
						if (iLVP.getItemTag() != null) {

							int resource = 0;
							if (iLVP.getDate() != null) {
								resource = (Integer) iLVP.getDate();
							}
							UrlImageViewHelper.setUrlDrawable((ImageView) view, iLVP.getItemTag()
									.toString(), resource);
						}
					}

					if (iLVP.getImgRoundCorner() > 0) {
						imgView.setDrawingCacheEnabled(true);
						Bitmap bitmap = imgView.getDrawingCache();
						imgView.setDrawingCacheEnabled(false);
						if (bitmap != null) {
							imgView.setImageBitmap(toRoundCorner(bitmap, iLVP.getImgRoundCorner()));
						} else {
							// VolleyLog.d("-----------------------bitmap is null");
						}
					}

				} else if (view instanceof ProgressBar) {
					((ProgressBar) view).setProgress(Integer.parseInt(iLVP.getDate().toString()));
				}
			}
		}
	}

	// 处理图像圆角
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
}
