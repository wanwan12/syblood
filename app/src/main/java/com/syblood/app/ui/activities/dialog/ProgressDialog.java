package com.syblood.app.ui.activities.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.syblood.app.R;


public class ProgressDialog extends Dialog
{
	/**
	 * 按比例缩小
	 */
	public final static int SCALE_DIVIDE = -1;
	/**
	 * 按比例放大
	 */
	public final static int SCALE_MULITIED = 1;

	/**
	 * 不缩放
	 */
	public final static int SCALE_NONE = 0;

	private final int MSG_WHAT_BUTTON1 = 101;

	private final int MSG_WHAT_BUTTON2 = 102;

	private final int MSG_WHAT_TITLE = 103;

	private final int MSG_WHAT_MESSAGE = 104;

	private final int MSG_WHAT_PERCENT = 105;

	private final int MSG_WHAT_VALUETEXT = 106;

	private final int MSG_WHAT_PROGRESSBAR = 107;

	private final int MSG_WHAT_MAXFILECOUNT = 108;

	private final int MSG_WHAT_MAXFILEPOSITION = 109;

	private View.OnClickListener btn1Listener = null;
	private View.OnClickListener btn2Listener = null;

	private int scaleValue = 1;
	private String unit = "";
	private int scaleType = 0;

	private Context _context = null;

	private TextView txtV_title = null;
	private TextView txtV_percent = null;
	private TextView txtV_value = null;
	private TextView txtV_count = null;
	private TextView txtV_message = null;
	private ProgressBar pb_progress = null;
	private TextView txtV_btn1 = null;
	private TextView txtV_btn2 = null;
	private long maxValue = 0;
	private long progressValue = 0;
	private int percent = 0;
	private int position = 0;
	private int maxCount = 0;

	public ProgressDialog(Context context)
	{
		super(context, R.style.ProgressBar_Style);
		this._context = context;
		this.setContentView(R.layout.progress_dialog_layout_modify);

		this.txtV_message = (TextView) this.findViewById(R.id.txtV_message);
		this.txtV_percent = (TextView) this.findViewById(R.id.txtV_percent);
		this.txtV_value = (TextView) this.findViewById(R.id.txtV_value);
		this.txtV_count = (TextView) this.findViewById(R.id.txtV_count);
		this.txtV_title = (TextView) this.findViewById(R.id.txtV_title);
		this.txtV_btn1 = (TextView) this.findViewById(R.id.txtV_btn1);
		this.txtV_btn2 = (TextView) this.findViewById(R.id.txtV_btn2);
		this.txtV_btn1.setVisibility(View.GONE);
		this.txtV_btn2.setVisibility(View.GONE);
		this.pb_progress = (ProgressBar) this.findViewById(R.id.pb_progress);
	}

	/**
	 * 设置按钮1（左边按钮）
	 *
	 * @param text
	 *            按钮显示文本
	 * @param listener
	 *            按钮对应的回调方法
	 */
	public void setButton1(String text, View.OnClickListener listener)
	{
		this.btn1Listener = listener;
		Message msg = new Message();
		msg.what = this.MSG_WHAT_BUTTON1;
		Bundle bld = new Bundle();
		bld.putString("Visible", "VISIBLE");
		bld.putString("Text", text);
		bld.putBoolean("Listener", true);
		msg.setData(bld);
		this.mhandler.sendMessage(msg);
	}

	/**
	 * 设置按钮2（右边按钮）
	 *
	 * @param text
	 *            按钮显示文本
	 * @param listener
	 *            按钮对应的回调方法
	 */
	public void setButton2(String text, View.OnClickListener listener)
	{
		this.btn2Listener = listener;
		Message msg = new Message();
		msg.what = this.MSG_WHAT_BUTTON2;
		Bundle bld = new Bundle();
		bld.putString("Visible", "VISIBLE");
		bld.putString("Text", text);
		bld.putBoolean("Listener", true);
		msg.setData(bld);
		this.mhandler.sendMessage(msg);
	}

	/**
	 * 设置进度最大值
	 * 
	 * @param maxValue
	 */
	public void setProgressMaxValue(int maxValue)
	{
		this.maxValue = maxValue;
		Bundle bld = new Bundle();
		bld.putInt("MaxValue", maxValue);
		Message msg = new Message();
		msg.what = MSG_WHAT_VALUETEXT;
		msg.setData(bld);
		this.mhandler.sendMessage(msg);
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title)
	{
		Bundle bld = new Bundle();
		bld.putString("Title", title);
		Message msg = new Message();
		msg.what = MSG_WHAT_TITLE;
		msg.setData(bld);
		this.mhandler.sendMessage(msg);
	}

	/**
	 * 设置显示的消息
	 * 
	 * @param message
	 */
	public void setMessage(String message)
	{
		Bundle bld = new Bundle();
		bld.putString("Message", message);
		Message msg = new Message();
		msg.what = MSG_WHAT_MESSAGE;
		msg.setData(bld);
		this.mhandler.sendMessage(msg);
	}

	public void setProgressValueVisible(boolean visible)
	{
		if(visible) txtV_value.setVisibility(View.VISIBLE);
		else txtV_value.setVisibility(View.GONE);
	}

	/**
	 * 设置当前进度值
	 * 
	 * @param value
	 */
	public void setProgressValue(int value)
	{
		this.progressValue = value;
		
		Bundle bld = new Bundle();
		bld.putInt("Value", value);
		Message msg = new Message();
		msg.what = MSG_WHAT_PROGRESSBAR;
		msg.setData(bld);
		this.mhandler.sendMessage(msg);
	}

	public void setMaxFileCount(int value)
	{
		Bundle bld = new Bundle();
		bld.putInt("count", value);
		Message msg = new Message();
		msg.what = MSG_WHAT_MAXFILECOUNT;
		msg.setData(bld);
		this.mhandler.sendMessage(msg);
	}

	public void setFilePosition(int value)
	{
		Bundle bld = new Bundle();
		bld.putInt("count", value);
		Message msg = new Message();
		msg.what = MSG_WHAT_MAXFILEPOSITION;
		msg.setData(bld);
		this.mhandler.sendMessage(msg);
	}

	/**
	 * 设置进度文本的显示方式
	 * 
	 * @param scaleType
	 *            按倍数缩小或放大value值 (SCALE_DIVIDE ， SCALE_MULITIED 或 SCALE_NONE)
	 * @param scaleValue
	 *            放大或缩小的倍数
	 * @param unit
	 *            显示的单位（例如：KB,MB,GB等）
	 */
	public void setValueTextType(int scaleType, int scaleValue, String unit)
	{
		this.scaleValue = scaleValue;
		this.unit = unit;
		this.scaleType = scaleType;
	}

	public Handler mhandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Bundle bld = msg.getData();
			switch (msg.what)
			{
				case MSG_WHAT_BUTTON1:
					if(bld.getString("Visible").equalsIgnoreCase("VISIBLE"))
					{
						txtV_btn1.setVisibility(View.VISIBLE);
					}
					else
					{
						txtV_btn1.setVisibility(View.INVISIBLE);
					}					
					txtV_btn1.setText(bld.getString("Text"));
					if(bld.getBoolean("Listener"))
					{
						txtV_btn1.setOnClickListener(btn1Listener);
					}
					break;
				case MSG_WHAT_BUTTON2:
					if(bld.getString("Visible").equalsIgnoreCase("VISIBLE"))
					{
						txtV_btn2.setVisibility(View.VISIBLE);
					}
					else
					{
						txtV_btn2.setVisibility(View.INVISIBLE);
					}					
					txtV_btn2.setText(bld.getString("Text"));
					if(bld.getBoolean("Listener"))
					{
						txtV_btn2.setOnClickListener(btn2Listener);
					}
					break;
				case MSG_WHAT_VALUETEXT:
					pb_progress.setMax(bld.getInt("MaxValue"));
					break;
				case MSG_WHAT_TITLE:					
					txtV_title.setText(bld.getString("Title"));
					break;
				case MSG_WHAT_MESSAGE:
					txtV_message.setText(bld.getString("Message"));
					break;
				case MSG_WHAT_PROGRESSBAR:
					pb_progress.setProgress(bld.getInt("Value"));
					if (maxValue != 0)
					{
						percent = (int)((progressValue * 100) / maxValue);
						txtV_percent.setText(percent + "%");
						switch (scaleType)
						{
							case ProgressDialog.SCALE_DIVIDE:
								txtV_value.setText((int) (progressValue / scaleValue) + unit + "/" + (int) (maxValue / scaleValue) + unit);
								break;
							case ProgressDialog.SCALE_MULITIED:
								txtV_value.setText((int) (progressValue * scaleValue) + unit + "/" + (int) (maxValue * scaleValue) + unit);
								break;
							case ProgressDialog.SCALE_NONE:
								txtV_value.setText(progressValue + unit + "/" + maxValue + unit);
								break;
						}
					}
					
					break;
				case MSG_WHAT_PERCENT:					
					txtV_percent.setText(bld.getInt("Percent") + "%");
					break;
				case MSG_WHAT_MAXFILECOUNT:
					txtV_count.setVisibility(View.VISIBLE);
					maxCount = bld.getInt("count", 1);
					txtV_count.setText("0/" + maxCount);
					break;
				case MSG_WHAT_MAXFILEPOSITION:
					position = bld.getInt("count", 1);
					txtV_count.setText(position + "/" + maxCount);
					break;
			}
		}
	};
}
