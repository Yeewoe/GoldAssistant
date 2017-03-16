package com.parttime.publish.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.parttime.common.Image.ContactImageLoader;
import com.parttime.publish.vo.JobBrokerListVo;
import com.parttime.utils.CheckUtils;
import com.parttime.utils.SharePreferenceUtil;
import com.qingmu.jianzhidaren.R;
import com.quark.http.image.CircularImage;
import com.quark.http.image.LoadImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by wyw on 2015/7/26.
 */
public class JobBrokerListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<JobBrokerListVo> mData;
    private RequestQueue queue;

    public JobBrokerListAdapter(Context context, RequestQueue queue) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mData = new ArrayList<>();
        this.queue = queue;
    }

    public void setAll(List<JobBrokerListVo> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addAll(List<JobBrokerListVo> jobManageListVoList) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.addAll(jobManageListVoList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mData != null ? mData.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return mData != null ? mData.get(i).companyId : 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_job_broker_list, null);
            viewHolder = new ViewHolder();
            viewHolder.mTxtBrokerName = (TextView) view.findViewById(R.id.txt_broker_name);
            viewHolder.mTxtRankNo = (TextView) view.findViewById(R.id.txt_rank_no);
            viewHolder.mTxtFans = (TextView) view.findViewById(R.id.txt_broker_fans);
            viewHolder.mTxtBrokerType = (TextView) view.findViewById(R.id.txt_broker_type);
            viewHolder.mImgViRankPic = (ImageView) view.findViewById(R.id.imgvi_rank_pic);
            viewHolder.mImgViHead = (CircularImage) view.findViewById(R.id.imgvi_head);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        JobBrokerListVo jobBrokerListVo = mData.get(i);
        viewHolder.mTxtBrokerName.setText(jobBrokerListVo.name);
        viewHolder.mTxtRankNo.setText("" + (i + 1));
        switch (i + 1) {
            case 1:
                viewHolder.mImgViRankPic.setImageDrawable(mContext.getResources().getDrawable(R.drawable.broker_rank_1));
                viewHolder.mImgViRankPic.setVisibility(View.VISIBLE);
                viewHolder.mTxtRankNo.setTextColor(mContext.getResources().getColor(R.color.broker_rank_1));
                break;
            case 2:
                viewHolder.mImgViRankPic.setImageDrawable(mContext.getResources().getDrawable(R.drawable.broker_rank_2));
                viewHolder.mImgViRankPic.setVisibility(View.VISIBLE);
                viewHolder.mTxtRankNo.setTextColor(mContext.getResources().getColor(R.color.broker_rank_2));
                break;
            case 3:
                viewHolder.mImgViRankPic.setImageDrawable(mContext.getResources().getDrawable(R.drawable.broker_rank_3));
                viewHolder.mImgViRankPic.setVisibility(View.VISIBLE);
                viewHolder.mTxtRankNo.setTextColor(mContext.getResources().getColor(R.color.broker_rank_3));
                break;
            default:
                viewHolder.mImgViRankPic.setVisibility(View.GONE);
                viewHolder.mTxtRankNo.setTextColor(mContext.getResources().getColor(R.color.common_gray_5));
                break;
        }
        ContactImageLoader.loadNativePhoto(String.valueOf(jobBrokerListVo.companyId), jobBrokerListVo.picInfo, viewHolder.mImgViHead, queue);
        viewHolder.mTxtFans.setText(mContext.getString(R.string.job_broker_fans_format, jobBrokerListVo.fans));
        viewHolder.mTxtBrokerType.setText(CheckUtils.isEmpty(jobBrokerListVo.hireType) || jobBrokerListVo.hireType.equals("null") ? mContext.getString(R.string.none) : jobBrokerListVo.hireType);

//        viewHolder.mTxtJobTitle.setText(jobManageListVo.jobTitle);
//        viewHolder.mTxtView.setText(mContext.getString(R.string.job_manage_view_count_format, jobManageListVo.view));
//        viewHolder.mTxtHand.setText(mContext.getString(R.string.job_manage_hand_count_format, jobManageListVo.hand));
//        viewHolder.mTxtHire.setText(mContext.getString(R.string.job_manage_hire_count_format, jobManageListVo.hire));

        return view;
    }



    private class ViewHolder {
        private TextView mTxtBrokerName;
        private TextView mTxtRankNo;
        private TextView mTxtFans;
        private TextView mTxtBrokerType;
        private ImageView mImgViRankPic;
        private CircularImage mImgViHead;
    }

}
