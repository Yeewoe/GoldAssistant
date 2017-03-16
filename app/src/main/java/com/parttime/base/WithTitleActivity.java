package com.parttime.base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.BaseActivity;

import java.lang.reflect.Constructor;

/**
 * Created by cjz on 2015/7/12.
 */
public abstract class WithTitleActivity extends BaseActivity {

    public ViewGroup leftWrapper;
    public ViewGroup rightWrapper;
    public TextView center;


    protected abstract ViewGroup getLeftWrapper();
    protected abstract ViewGroup getRightWrapper();
    protected abstract TextView getCenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    protected void initViews(){

    }


    protected void center(int txtId){
        if(center == null){
            center = getCenter();
            if(center == null){
                center = (TextView) findViewById(R.id.tv_title);
            }
        }
        center.setText(txtId);
    }

    protected void center(String txt){
        if(center == null){
            center = getCenter();
            if (center == null) {
                center = (TextView) findViewById(R.id.tv_title);
            }
        }
        center.setText(txt);
    }

    protected void left(Class clz, int resId){
        left(clz, resId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void left(Class clz, int resId, View.OnClickListener onClickListener){
        if(leftWrapper == null) {
            leftWrapper = getLeftWrapper();
            if(leftWrapper == null){
                leftWrapper = (ViewGroup) findViewById(R.id.fl_title_wrapper_left);
            }
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams(leftWrapper);
        View v = null;
        if(TextView.class.equals(clz)){
            v = mkTv(resId);

        }else if(ImageButton.class.equals(clz)){
            v = mkIb(resId);


        }
        if(layoutParams != null) {
            v.setLayoutParams(layoutParams);
        }
        v.setOnClickListener(onClickListener);

        leftWrapper.removeAllViews();
        leftWrapper.addView(v);
    }

    protected void right(Class clz, int resId){
        right(clz, resId, null);
    }

    protected void right(Class clz, int resId, View.OnClickListener onClickListener){
        if(rightWrapper == null) {
            rightWrapper = getRightWrapper();
            if(rightWrapper == null){
                rightWrapper = (ViewGroup) findViewById(R.id.fl_title_wrapper_right);
            }
        }
        ViewGroup.LayoutParams layoutParams = getLayoutParams(rightWrapper);
        View v = null;

        if(TextView.class.equals(clz)){
            v = mkTv(resId);
        }else if(ImageButton.class.equals(clz)){
            v = mkIb(resId);
        }else if(ImageView.class.equals(clz)){
            v = mkIV(resId);
        }
        if(layoutParams != null) {
            if(ImageView.class.equals(clz)){
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)layoutParams;
                params.height = (int)getResources().getDimension(R.dimen.star_width);
                params.width = (int)getResources().getDimension(R.dimen.star_width);
                params.setMargins(0,
                        (int)getResources().getDimension(R.dimen.star_width)*2/3,
                        0,
                        (int)getResources().getDimension(R.dimen.star_width)*2/3);
                v.setLayoutParams(layoutParams);
            }else {
                v.setLayoutParams(layoutParams);
            }
        }
        v.setOnClickListener(onClickListener);
        rightWrapper.removeAllViews();
        rightWrapper.addView(v);
    }

    protected TextView mkTv(int resId){
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(getResources().getColor(R.color.bottom_text_color_normal));
        tv.setText(resId);
        return tv;
    }

    protected ImageButton mkIb(int resId){
        ImageButton ib = new ImageButton(this);
        ib.setBackgroundColor(0x00000000);
        ib.setImageResource(resId);
        return ib;
    }

    protected ImageView mkIV(int resId){
        ImageView iv = new ImageView(this);
        iv.setBackgroundResource(resId);
        return iv;
    }

    private ViewGroup.LayoutParams getLayoutParams(ViewGroup vg){

        try {
            Class clz = vg.getClass();

            if (clz != null) {
                String clzName = clz.getName() + "$" + "LayoutParams";
                clz = Class.forName(clzName);
                if(clz != null) {
                    Constructor c = clz.getConstructor(int.class, int.class);
                    if (c != null) {
                        ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) c.newInstance(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        return lp;
                    }
                }
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }
}
