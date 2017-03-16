package com.quark.guanli;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;

/**
 * 发布兼职
 * 
 * @author Administrator
 * 
 */
public class PublishActivity extends BaseActivity {

	@ViewInject(R.id.type_paifa)
	Button type_paifa;
	@ViewInject(R.id.type_cxiao)
	Button type_cxiao;
	@ViewInject(R.id.type_jiajiao)
	Button type_jiajiao;
	@ViewInject(R.id.type_fwy)
	Button type_fwy;
	@ViewInject(R.id.type_liyi)
	Button type_liyi;
	@ViewInject(R.id.type_bary)
	Button type_bary;
	@ViewInject(R.id.type_mote)
	Button type_mote;
	@ViewInject(R.id.type_zhuchi)
	Button type_zhuchi;
	@ViewInject(R.id.type_fanyi)
	Button type_fanyi;
	@ViewInject(R.id.type_gongzuorenyuan)
	Button type_gongzuorenyuan;
	@ViewInject(R.id.type_huawu)
	Button type_huawu;
	@ViewInject(R.id.type_chongchang)
	Button type_chongchang;
	@ViewInject(R.id.type_yanyi)
	Button type_yanyi;
	@ViewInject(R.id.type_fangtan)
	Button type_fangtan;
	@ViewInject(R.id.type_qita)
	Button type_qita;
	public static PublishActivity intanse;
	private SharedPreferences sp;
	private RelativeLayout topLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_partjob_type);
		ViewUtils.inject(this);
		sp = getSharedPreferences("jrdr.setting", MODE_PRIVATE);
		topLayout = (RelativeLayout) findViewById(R.id.top_title_layout);
			topLayout.setBackgroundColor(getResources().getColor(
					R.color.guanli_common_color));
		TextView tv = (TextView) findViewById(R.id.title);
		tv.setText("发布兼职");
		setBackButton();
		intanse = this;
	}

	@OnClick({ R.id.type_paifa, R.id.type_cxiao, R.id.type_jiajiao,
			R.id.type_fwy, R.id.type_liyi, R.id.type_bary, R.id.type_mote,
			R.id.type_zhuchi, R.id.type_fanyi, R.id.type_qita,
			R.id.type_gongzuorenyuan, R.id.type_huawu, R.id.type_yanyi,
			R.id.type_fangtan, R.id.type_chongchang })
	public void typeOnClick(View view) {
		switch (view.getId()) {
		// case R.id.type_quanbu:
		// publicPartjob(type_quanbu);
		// break;
		case R.id.type_paifa:
			publicPartjob(type_paifa);
			break;
		case R.id.type_cxiao:
			publicPartjob(type_cxiao);
			break;
		case R.id.type_jiajiao:
			publicPartjob(type_jiajiao);
			break;
		case R.id.type_fwy:
			publicPartjob(type_fwy);
			break;
		case R.id.type_liyi:
			publicPartjob(type_liyi);
			break;
		case R.id.type_bary:
			publicPartjob(type_bary);
			break;
		case R.id.type_mote:
			publicPartjob(type_mote);
			break;
		case R.id.type_zhuchi:
			publicPartjob(type_zhuchi);
			break;
		case R.id.type_fanyi:
			publicPartjob(type_fanyi);
			break;
		case R.id.type_gongzuorenyuan:
			publicPartjob(type_gongzuorenyuan);
			break;
		case R.id.type_huawu:
			publicPartjob(type_huawu);
			break;
		case R.id.type_chongchang:
			publicPartjob(type_chongchang);
			break;
		case R.id.type_yanyi:
			publicPartjob(type_yanyi);
			break;
		case R.id.type_fangtan:
			publicPartjob(type_fangtan);
			break;
		case R.id.type_qita:
			publicPartjob(type_qita);
			break;

		default:
			break;
		}
	}

	public void publicPartjob(final Button button) {
		Intent intent = new Intent();
		intent.putExtra("type", button.getText().toString());
		intent.setClass(PublishActivity.this, WritePartjobActivity.class);
		startActivity(intent);
	}

}
