package com.okm_android.main.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.okm_android.main.R;
import com.okm_android.main.View.Button.BootstrapEditText;

public class AlertDialog {
	private Context context;
	private Dialog dialog;
	private LinearLayout lLayout_bg;
	private TextView txt_title;
	private TextView txt_msg;
    private TextView txt_msg_two;
    public BootstrapEditText txt_edit_one;
    public BootstrapEditText txt_edit_two;
	private Button btn_neg;
	private Button btn_pos;
	private ImageView img_line;
	private Display display;
	private boolean showTitle = false;
	private boolean showMsg = false;
	private boolean showPosBtn = false;
	private boolean showNegBtn = false;
    private boolean showMsgEditTwo = false;

	public AlertDialog(Context context) {
		this.context = context;
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		display = windowManager.getDefaultDisplay();
	}

	public AlertDialog builder() {
		View view = LayoutInflater.from(context).inflate(
				R.layout.view_alertdialog, null);

		lLayout_bg = (LinearLayout) view.findViewById(R.id.lLayout_bg);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_title.setVisibility(View.GONE);
		txt_msg = (TextView) view.findViewById(R.id.txt_msg);
		txt_msg.setVisibility(View.GONE);
        txt_msg_two = (TextView)view.findViewById(R.id.txt_msg_two);
        txt_msg_two.setVisibility(View.GONE);
        txt_edit_one = (BootstrapEditText)view.findViewById(R.id.txt_edit_one);
        txt_edit_one.setVisibility(View.GONE);
        txt_edit_two = (BootstrapEditText)view.findViewById(R.id.txt_edit_two);
        txt_edit_two.setVisibility(View.GONE);
		btn_neg = (Button) view.findViewById(R.id.btn_neg);
		btn_neg.setVisibility(View.GONE);
		btn_pos = (Button) view.findViewById(R.id.btn_pos);
		btn_pos.setVisibility(View.GONE);
		img_line = (ImageView) view.findViewById(R.id.img_line);
		img_line.setVisibility(View.GONE);

		dialog = new Dialog(context, R.style.AlertDialogStyle);
		dialog.setContentView(view);

		lLayout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display
				.getWidth() * 0.85), LayoutParams.WRAP_CONTENT));

		return this;
	}

	public AlertDialog setTitle(String title) {
		showTitle = true;
		if ("".equals(title)) {
			txt_title.setText("提示");
		} else {
			txt_title.setText(title);
		}
		return this;
	}

	public AlertDialog setMsgOne(String msg) {
		showMsg = true;
		if ("".equals(msg)) {
			txt_msg.setText("提示");
		} else {
			txt_msg.setText(msg);
		}
		return this;
	}
    public AlertDialog setMsgTwo(String msg) {
        showMsgEditTwo = true;
        if ("".equals(msg)) {
            txt_msg_two.setText("提示");
        } else {
            txt_msg_two.setText(msg);
        }
        return this;
    }
    public AlertDialog setHintOne(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            txt_edit_one.setHint("");
        } else {
            txt_edit_one.setHint(msg);
        }
        return this;
    }
    public AlertDialog setHintTwo(String msg) {
        showMsgEditTwo = true;
        if ("".equals(msg)) {
            txt_edit_two.setHint("");
        } else {
            txt_edit_two.setHint(msg);
        }
        return this;
    }

	public AlertDialog setCancelable(boolean cancel) {
		dialog.setCancelable(cancel);
		return this;
	}

	public AlertDialog setPositiveButton(String text,
			final OnClickListener listener) {
		showPosBtn = true;
		if ("".equals(text)) {
			btn_pos.setText("确定");
		} else {
			btn_pos.setText(text);
		}
		btn_pos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}
    public String getMsgOne() {
       return txt_edit_one.getText().toString().trim();
    }
    public String getMsgTwo() {
        return txt_edit_two.getText().toString().trim();
    }

	public AlertDialog setNegativeButton(String text,
			final OnClickListener listener) {
		showNegBtn = true;
		if ("".equals(text)) {
			btn_neg.setText("取消");
		} else {
			btn_neg.setText(text);
		}
		btn_neg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onClick(v);
				dialog.dismiss();
			}
		});
		return this;
	}

	private void setLayout() {
		if (!showTitle && !showMsg) {
			txt_title.setText("提示");
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showTitle) {
			txt_title.setVisibility(View.VISIBLE);
		}

		if (showMsg) {
			txt_msg.setVisibility(View.VISIBLE);
            txt_edit_one.setVisibility(View.VISIBLE);
		}

        if(showMsg && showMsgEditTwo)
        {
            txt_msg.setVisibility(View.VISIBLE);
            txt_edit_one.setVisibility(View.VISIBLE);
            txt_msg_two.setVisibility(View.VISIBLE);
            txt_edit_two.setVisibility(View.VISIBLE);
        }

		if (!showPosBtn && !showNegBtn) {
			btn_pos.setText("确定");
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
			btn_pos.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}

		if (showPosBtn && showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_right_selector);
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_left_selector);
			img_line.setVisibility(View.VISIBLE);
		}

		if (showPosBtn && !showNegBtn) {
			btn_pos.setVisibility(View.VISIBLE);
			btn_pos.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}

		if (!showPosBtn && showNegBtn) {
			btn_neg.setVisibility(View.VISIBLE);
			btn_neg.setBackgroundResource(R.drawable.alertdialog_single_selector);
		}
	}

	public void show() {
		setLayout();
		dialog.show();
	}
}
