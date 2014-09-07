package com.young.common.view;

import com.young.modules.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgersssDialog extends Dialog {
	private ImageView img;
	private TextView txt;

	public ProgersssDialog(Context context) {
		super(context, R.style.progress_dialog);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.progress_dialog, null);
		img = (ImageView) view.findViewById(R.id.progress_dialog_img);
		txt = (TextView) view.findViewById(R.id.progress_dialog_txt);
		Animation anim = AnimationUtils.loadAnimation(context,
				R.anim.loading_dialog_progressbar);
		img.setAnimation(anim);
		txt.setText("正在删除城市...");
		setContentView(view);
	}

	public void setMsg(String msg) {
		txt.setText(msg);
	}

	public void setMsg(int msgId) {
		txt.setText(msgId);
	}

}