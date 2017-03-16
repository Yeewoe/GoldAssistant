package com.parttime.publish.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parttime.publish.vo.JobManageListVo;
import com.qingmu.jianzhidaren.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wyw on 2015/7/26.
 */
public class JobManageListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<JobManageListVo> mData;

    public JobManageListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mData = new ArrayList<>();
    }

    public void setAll(List<JobManageListVo> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void addAll(List<JobManageListVo> jobManageListVoList) {
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
        return mData != null ? mData.get(i).jobId : 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.item_job_manage_list, null);
            viewHolder = new ViewHolder();
            viewHolder.mTxtJobTitle = (TextView) view.findViewById(R.id.txt_job_title);
            viewHolder.mTxtView = (TextView) view.findViewById(R.id.txt_view);
            viewHolder.mTxtHand = (TextView) view.findViewById(R.id.txt_hand);
            viewHolder.mTxtHire = (TextView) view.findViewById(R.id.txt_hire);
            viewHolder.mImgViUrgentTag = (ImageView) view.findViewById(R.id.imgvi_urgent_tag);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        JobManageListVo jobManageListVo = mData.get(i);
        viewHolder.mTxtJobTitle.setText(jobManageListVo.jobTitle);
        viewHolder.mTxtView.setText(mContext.getString(R.string.job_manage_view_count_format, jobManageListVo.view));
        viewHolder.mTxtHand.setText(mContext.getString(R.string.job_manage_hand_count_format, jobManageListVo.hand));
        viewHolder.mTxtHire.setText(mContext.getString(R.string.job_manage_hire_count_format, jobManageListVo.hire));
        viewHolder.mImgViUrgentTag.setVisibility(jobManageListVo.isUrgent ? View.VISIBLE : View.GONE);

        return view;
    }



    private class ViewHolder {
        private TextView mTxtJobTitle;
        private TextView mTxtView;
        private TextView mTxtHand;
        private TextView mTxtHire;
        private ImageView mImgViUrgentTag;
    }
}
