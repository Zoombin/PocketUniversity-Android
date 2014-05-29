package com.xyhui.activity.more;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mslibs.utils.VolleyLog;
import com.xyhui.R;
import com.xyhui.activity.event.EventViewActivity;
import com.xyhui.types.QrCode;
import com.xyhui.utils.CameraPreview;
import com.xyhui.utils.Params;
import com.xyhui.widget.FLActivity;

public class QRCodeScanActivity extends FLActivity implements SurfaceHolder.Callback2 {
	private final int FOCUS_INTERVAL = 1500;

	private Button btn_back;
	private Button btn_setting;
	private TextView text_tips;
	private RelativeLayout layout_camera_preview;

	private Camera mCamera;
	private CameraPreview mPreview;
	private SurfaceView mFocusArea;
	private Handler mAutoFocusHandler;
	private ImageScanner mScanner;
	private boolean mBarcodeScanned;
	private boolean mPreviewing = true;// 防止对焦回调在离开当前activity后执行，引起空指针
	private String mEventCode;

	private static final int FOCUS_AREA_DIPS = 200;

	private DrawingThread mDrawingThread;

	// private RelativeLayout mRect;
	// private RelativeLayout.LayoutParams mRectParams;
	private RelativeLayout.LayoutParams mParams;

	static {
		System.loadLibrary("iconv");
	}

	@Override
	public void linkUiVar() {
		setContentView(R.layout.activity_qrcode_scan);

		btn_back = (Button) findViewById(R.id.btn_back);
		btn_setting = (Button) findViewById(R.id.btn_setting);
		text_tips = (TextView) findViewById(R.id.text_tips);
		layout_camera_preview = (RelativeLayout) findViewById(R.id.layout_camera_preview);
	}

	@Override
	public void bindListener() {
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 关闭返回
				releaseCamera();
				finish();
			}
		});

		btn_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LinearLayout linearLayout = new LinearLayout(QRCodeScanActivity.this);
				// 设置活动码
				final EditText input_code = new EditText(QRCodeScanActivity.this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				input_code.setLayoutParams(lp);
				lp.setMargins(60, 30, 60, 30);

				linearLayout.setLayoutParams(lp);
				linearLayout.addView(input_code);
				input_code.setSingleLine(true);
				input_code.setTextSize(18);
				input_code.setHint("输入活动码");

				new AlertDialog.Builder(QRCodeScanActivity.this).setTitle("设置活动码")
						.setView(linearLayout)
						.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								mEventCode = input_code.getText().toString().trim();

								if (TextUtils.isEmpty(mEventCode)) {
									text_tips.setText("请将手机摄像头对准二维码进行扫描");
								} else {
									text_tips.setText("己设置活动管理码\n请将手机摄像头对准二维码进行扫描");
								}
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

							}
						}).show();
			}
		});

	}

	@Override
	public void ensureUi() {
		mAutoFocusHandler = new Handler();

		/* Instance barcode scanner */
		mScanner = new ImageScanner();
		mScanner.setConfig(0, Config.X_DENSITY, 3);
		mScanner.setConfig(0, Config.Y_DENSITY, 3);

		mFocusArea = new SurfaceView(mActivity);
		mFocusArea.setZOrderOnTop(true);
		mFocusArea.getHolder().setFormat(PixelFormat.TRANSPARENT);
		mFocusArea.setBackgroundResource(R.drawable.scan_focus);
		int pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, FOCUS_AREA_DIPS,
				getResources().getDisplayMetrics());
		mParams = new RelativeLayout.LayoutParams(pixels, pixels);
		mParams.addRule(RelativeLayout.CENTER_IN_PARENT);

		// mRect = new RelativeLayout(mActivity);
		// mRect.setBackgroundResource(R.drawable.scan_focus);
		// mRectParams = new RelativeLayout.LayoutParams(pixels + 20, pixels + 20);
		// mRectParams.addRule(RelativeLayout.CENTER_IN_PARENT);

		mFocusArea.getHolder().addCallback(this);

		// This is the thread that will be drawing to our surface.
		mDrawingThread = new DrawingThread();
		mDrawingThread.start();

		mEventCode = getIntent().getStringExtra(Params.INTENT_EXTRA.ATTEND_CODE);
		VolleyLog.d(mEventCode);
	}

	@Override
	protected void onResume() {
		super.onResume();

		mPreviewing = true;

		mCamera = getCameraInstance();
		mPreview = new CameraPreview(this, mCamera, previewCb, mAutoFocusCB);
		layout_camera_preview.addView(mPreview);
		// rect.addView(mFocusArea, params);
		// layout_camera_preview.addView(mRect, mRectParams);
		layout_camera_preview.addView(mFocusArea, mParams);

		mBarcodeScanned = false;

		// Let the drawing thread resume running.
		synchronized (mDrawingThread) {
			mDrawingThread.mRunning = true;
			mDrawingThread.notify();
		}
	}

	public void onPause() {
		super.onPause();

		// Make sure the drawing thread is not running while we are paused.
		synchronized (mDrawingThread) {
			mDrawingThread.mRunning = false;
			mDrawingThread.notify();
		}

		releaseCamera();
		layout_camera_preview.removeAllViews();
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
		}
		return c;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mPreviewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable mAutoFocus = new Runnable() {
		public void run() {
			if (mPreviewing) {
				mCamera.autoFocus(mAutoFocusCB);
			}
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			int pic_width = size.width;
			int pic_height = size.height;

			int pixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
					FOCUS_AREA_DIPS, getResources().getDisplayMetrics());

			int layout_width = layout_camera_preview.getWidth();
			int layout_height = layout_camera_preview.getHeight();

			int crop_width = pixels * pic_width / layout_height;
			int crop_height = pixels * pic_height / layout_width;

			Image barcode = new Image(pic_width, pic_height, "Y800");
			barcode.setData(data);
			barcode.setCrop(pic_width / 2 - crop_width / 2, pic_height / 2 - crop_height / 2,
					crop_width, crop_height);

			VolleyLog.d("pic_width: %d ,pic_height: %d, \nlayout_width: %d, layout_height:%d",
					pic_width, pic_height, layout_width, layout_height);

			int result = mScanner.scanImage(barcode);

			if (result != 0) {
				SymbolSet syms = mScanner.getResults();

				String code = null;

				for (Symbol sym : syms) {
					VolleyLog.d("barcode result: %s", sym.getData());
					code = sym.getData();

					mBarcodeScanned = true;
				}

				if (mBarcodeScanned && !TextUtils.isEmpty(code)) {
					releaseCamera();
					QrCode qrcode = new QrCode();

					Pattern p = Pattern.compile("xyhui://([a-z]+)/([0-9a-zA-Z]+)/?([^/]*)");
					Matcher m = p.matcher(code);
					if (m.find()) {
						MatchResult mr = m.toMatchResult();

						String type = mr.group(1);
						String id = mr.group(2);
						String title = mr.group(3);
						VolleyLog.d("group 1:%s 2:%s 3:%s", type, id, title);

						try {
							if (!TextUtils.isEmpty(title)) {
								title = URLDecoder.decode(title, "utf-8");
							}
						} catch (UnsupportedEncodingException e) {

						}

						qrcode.id = id;
						qrcode.title = title;

						if ("user".equalsIgnoreCase(type)) {
							if (TextUtils.isEmpty(mEventCode)) {
								qrcode.type = QrCode.ADDFRIEND;
							} else {
								qrcode.type = QrCode.ADMINEVENTCHECKIN;
								qrcode.code = mEventCode;
							}
						} else if ("event".equalsIgnoreCase(type)) {
							qrcode.type = QrCode.EVENTCHECKIN;
						}

						Intent intent = new Intent(mActivity, QRCodeResultActivity.class);
						intent.putExtra(Params.INTENT_EXTRA.QRCODE, qrcode);
						startActivity(intent);
					} else {
						Pattern event = Pattern.compile("http://pocketuni.net/eventf/([0-9]+)");
						Matcher eventMatch = event.matcher(code);
						if (eventMatch.find()) {
							MatchResult eventResult = eventMatch.toMatchResult();
							String eventId = eventResult.group(1);

							if (!TextUtils.isEmpty(eventId)) {
								Intent intent = new Intent(mActivity, EventViewActivity.class);
								intent.putExtra(Params.INTENT_EXTRA.EVENTID, eventId);
								startActivity(intent);
							}
						} else {
							Pattern urlPtn = Pattern
									.compile("((http://|https://){1}[\\w\\.\\-/\\?=&:]+)");
							Matcher urlMatch = urlPtn.matcher(code);
							if (urlMatch.find()) {
								MatchResult urlResult = urlMatch.toMatchResult();
								String url = urlResult.group(0);

								if (!TextUtils.isEmpty(url)) {
									Uri uri = Uri.parse(url);
									Intent intent = new Intent(Intent.ACTION_VIEW, uri);
									startActivity(intent);
								}
							} else {
								qrcode.type = QrCode.OTHER;
								qrcode.code = code;

								Intent intent = new Intent(mActivity, QRCodeResultActivity.class);
								intent.putExtra(Params.INTENT_EXTRA.QRCODE, qrcode);
								startActivity(intent);
							}
						}
					}
				}
			}
		}
	};

	// Mimic continuous auto-focusing
	AutoFocusCallback mAutoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			mAutoFocusHandler.postDelayed(mAutoFocus, FOCUS_INTERVAL);
		}
	};

	// Tracking of a single point that is moving on the screen.
	static final class MovingPoint {
		int x, y, dy;

		void init(int x, int y, int dy) {
			this.x = x;
			this.y = y;
			this.dy = dy;
		}

		void step(int maxHeight) {
			y += dy;
			if (y > maxHeight) {
				y = 0;
			}
		}
	}

	/**
	 * This is a thread that will be running a loop, drawing into the window's surface.
	 */
	class DrawingThread extends Thread {
		// These are protected by the Thread's lock.
		SurfaceHolder mSurface;
		boolean mRunning;
		boolean mActive;
		boolean mQuit;

		// Internal state.
		int mLineWidth;
		int mStep;

		boolean mInitialized;
		final MovingPoint mPoint1 = new MovingPoint();
		final MovingPoint mPoint2 = new MovingPoint();

		static final int NUM_OLD = 1;

		final float[] mOld = new float[NUM_OLD * 4];

		final Paint mBackground = new Paint();
		final Paint mForeground = new Paint();

		@Override
		public void run() {
			mLineWidth = 2;
			mStep = 2;

			// mBackground.setColor(0xff000000);
			mForeground.setColor(0xff3388ff);
			mForeground.setAntiAlias(true);
			mForeground.setStrokeWidth(mLineWidth);

			while (true) {
				// Synchronize with activity: block until the activity is ready
				// and we have a surface; report whether we are active or inactive
				// at this point; exit thread when asked to quit.
				synchronized (this) {
					while (mSurface == null || !mRunning) {
						if (mActive) {
							mActive = false;
							notify();
						}
						if (mQuit) {
							return;
						}
						try {
							wait();
						} catch (InterruptedException e) {
						}
					}

					if (!mActive) {
						mActive = true;
						notify();
					}

					// Lock the canvas for drawing.
					Canvas canvas = mSurface.lockCanvas();
					if (canvas == null) {
						Log.i("WindowSurface", "Failure locking canvas");
						continue;
					}

					// Update graphics.
					if (!mInitialized) {
						mInitialized = true;
						mPoint1.init(0, 0, mStep);
						mPoint2.init(canvas.getWidth(), 0, mStep);
					} else {
						mPoint1.step(canvas.getHeight());
						mPoint2.step(canvas.getHeight());
					}

					canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

					// Draw old lines.
					canvas.drawLine(mOld[0], mOld[1], mOld[2], mOld[3], mForeground);

					// Draw new line.
					canvas.drawLine(mPoint1.x, mPoint1.y, mPoint2.x, mPoint2.y, mForeground);

					mOld[0] = mPoint1.x;
					mOld[1] = mPoint1.y;
					mOld[2] = mPoint2.x;
					mOld[3] = mPoint2.y;

					// All done!
					mSurface.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Make sure the drawing thread goes away.
		synchronized (mDrawingThread) {
			mDrawingThread.mQuit = true;
			mDrawingThread.notify();
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// Tell the drawing thread that a surface is available.
		synchronized (mDrawingThread) {
			mDrawingThread.mSurface = holder;
			mDrawingThread.notify();
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// Don't need to do anything here; the drawing thread will pick up
		// new sizes from the canvas.
	}

	public void surfaceRedrawNeeded(SurfaceHolder holder) {

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// We need to tell the drawing thread to stop, and block until
		// it has done so.
		synchronized (mDrawingThread) {
			mDrawingThread.mSurface = holder;
			mDrawingThread.notify();
			while (mDrawingThread.mActive) {
				try {
					mDrawingThread.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
