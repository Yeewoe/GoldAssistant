package com.parttime.mine;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.parttime.base.LocalInitActivity;
import com.parttime.common.Image.ContactImageLoader;
import com.parttime.net.BaseRequest;
import com.parttime.net.Callback;
import com.parttime.net.ErrorHandler;
import com.parttime.pojo.Fans;
import com.parttime.widget.CreditView;
import com.qingmu.jianzhidaren.R;
import com.quark.common.JsonUtil;
import com.quark.common.Url;
import com.quark.volley.VolleySington;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;

/**
 * Created by cjz on 2015/7/16.
 */
public class MyFansActivity extends LocalInitActivity implements XListView.IXListViewListener{
    private static final int PAGE_SIZE = 10;
    @ViewInject(R.id.xlv_my_fans)
    private XListView lv;

    private MyFansAdapter adapter;
    private List<Fans> fanses = new ArrayList<Fans>();

    private int pageIndex = 1;
    private int totalPage;
    private int totalRow;

    private String cId;

    private Callback cbAdd = new Callback() {
        @Override
        public void success(Object obj) {
            showWait(false);
            JSONObject json = (JSONObject) obj;
            try {
                List<Fans> fs  = new ArrayList<Fans>();
                JSONObject followerPage = json.getJSONObject("followerPage");
                if(followerPage != null){
                    pageIndex = followerPage.getInt("pageNumber") + 1;
                    totalPage = followerPage.getInt("totalPage");
                    totalRow = followerPage.getInt("totalRow");
                    JSONArray list = followerPage.getJSONArray("list");
                    Gson gson = new Gson();
                    for(int i = 0; i < list.length(); ++i){
                        String s = list.getJSONObject(i).toString();
                        Fans fans = gson.fromJson(s, Fans.class);
                        fs.add(fans);
                    }
                    int resultLen = list != null ? list.length() : 0;
//                    updateViews(fs);
                    setDatas(fs, false);
                    updateViews();
                    lv.setLoadOver(resultLen, PAGE_SIZE);
                    lv.stopRefresh();
                    showWait(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Object obj) {
            showWait(false);
            new ErrorHandler(MyFansActivity.this, obj).showToast();
        }
    };

    private Callback cbAppend = new Callback() {
        @Override
        public void success(Object obj) {
            JSONObject json = (JSONObject) obj;
            try {
                List<Fans> fs  = new ArrayList<Fans>();
                JSONObject followerPage = json.getJSONObject("followerPage");
                if(followerPage != null){
                    pageIndex = followerPage.getInt("pageNumber") + 1;
                    totalPage = followerPage.getInt("totalPage");
                    totalRow = followerPage.getInt("totalRow");
                    int pageSize = followerPage.getInt("pageSize");
                    JSONArray list = followerPage.getJSONArray("list");
                    for(int i = 0; i < list.length(); ++i){
                        Fans fans = (Fans) JsonUtil.jsonToBean(list.getJSONObject(i), Fans.class);
                        fs.add(fans);
                    }
                    int resultLen = list != null ? list.length() : 0;
                    setDatas(fs, true);
                    updateViews();
                    lv.setLoadOver(resultLen, PAGE_SIZE);
                    lv.stopLoadMore();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Object obj) {
            lv.stopLoadMore();
        }
    };

    private void setDatas(List<Fans> datas, boolean append){
        if(!append){
            fanses.clear();
        }
        fanses.addAll(datas);
        adapter.notifyDataSetChanged();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_fans);
        ViewUtils.inject(this);
        super.onCreate(savedInstanceState);
        loadLocalData();
        loadData();
    }

    @Override
    protected void initViews() {
        super.initViews();
        left(TextView.class, R.string.back);
        center(R.string.my_fans);

        lv.setPullLoadEnable(true);
        lv.setXListViewListener(this);
        adapter = new MyFansAdapter(this, fanses);
        lv.setAdapter(adapter);
    }

    private void updateViews(){

    }

    private void load(Callback cb){
        Map<String, String> params = new HashMap<String, String>();
        params.put("company_id", getCompanyId());
        params.put("pn", pageIndex + "");
        params.put("page_size", PAGE_SIZE + "");
        new BaseRequest().request(Url.MY_FOLLOWERS_LIST, params, VolleySington.getInstance()
                .getRequestQueue(), cb);
    }


    private void loadData(){
        showWait(true);
        load(cbAdd);
    }

    @Override
    protected ViewGroup getLeftWrapper() {
        return null;
    }

    @Override
    protected ViewGroup getRightWrapper() {
        return null;
    }

    @Override
    protected TextView getCenter() {
        return null;
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        load(cbAdd);
    }

    @Override
    public void onLoadMore() {
        load(cbAppend);
    }

    private class MyFansAdapter extends BaseAdapter {

        private Context context;
        private List<Fans> datas;
        private LayoutInflater inflater;
        private String male, female;

        public MyFansAdapter(Context context, List<Fans> datas) {
            this.context = context;
            this.datas = datas;
            inflater = LayoutInflater.from(context);
            male = "(" + context.getString(R.string.male) + ")";
            female = "(" + context.getString(R.string.female) + ")";
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_my_fans, lv, false);
                holder.ivHead = (ImageView) convertView.findViewById(R.id.iv_head);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvSincereMoney = (TextView) convertView.findViewById(R.id.tv_sincere_moneys);
                holder.tvCertStatus = (TextView) convertView.findViewById(R.id.tv_cert_status);
                holder.cv = (CreditView) convertView.findViewById(R.id.cv);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            Fans fans = (Fans) getItem(position);
//            holder.ivHead.setImageResource(R.drawable.ic_launcher);
            holder.ivHead.setTag(R.id.picture, fans.picture_1);
            ContactImageLoader.loadNativePhoto(fans.user_name, fans.picture_1, holder.ivHead, VolleySington.getInstance().getRequestQueue());
            holder.tvName.setText(fans.user_name + " " + (fans.sex == 1 ? male : female));
            holder.tvSincereMoney.setText(context.getString(fans.earnest_money == 1 ? R.string.sincere_money_paid : R.string.sincere_money_not_paid));
            holder.tvCertStatus.setText(context.getString(fans.certification == 2 ? R.string.real_name_certed : R.string.real_name_not_certed));

            //在此处适配CreditView，最大传10
            int credit = fans.creditworthiness / 10;
            holder.cv.credit(credit > 10 ? 10 : credit);
            return convertView;
        }

        private class ViewHolder {
            public ImageView ivHead;
            public TextView tvName;
            public TextView tvSincereMoney;
            public TextView tvCertStatus;
            public TextView tvCredit;
            public CreditView cv;
        }
    }
}
