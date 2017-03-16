package com.carson.constant;

import android.net.Uri;

import com.parttime.net.GroupSettingRequest;
import com.parttime.pojo.BaseUser;
import com.parttime.pojo.GroupDescription;
import com.quark.model.GuangchangModle;
import com.quark.model.HuanxinUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ConstantForSaveList {
	public static boolean re_load = true;// 是否重新加载
	public static int pageNumber = 1;// 当前页数
	public static ArrayList<GuangchangModle> jianzhis = new ArrayList<>();// 保存的list
	public static boolean change_city = false;// 默认没有切换城市
	public static Uri uploadUri;// 保存图库中裁剪后的图片路径(有的手机不能传输uri 如htc)
	// 所有的请求可以设置请求超时如:
	// stringRequest
	// .setRetryPolicy(new
	// DefaultRetryPolicy(ConstantForSaveList.DEFAULTRETRYTIME*1000, 1, 1.0f));
	public static int DEFAULTRETRYTIME = 300;// 设置全局加载超时时间

	public static String userId;// 用户ID(u667 or c228)
	public static String userName;// 用户姓名(张三)

	public static int listViewIndex;// 记住广场listview离开是光标所处位置
	public static int listViewTop;
	public static String[] nativeBankList = { "中国工商银行", "中国农业银行", "中国建设银行",
			"中国邮政储蓄银行", "中国银行", "招商银行", "交通银行", "中国光大银行", "平安银行", "中国民生银行",
			"兴业银行", "中信银行", "浦发银行", "广发银行", "成都银行", "上海银行", "深圳发展银行", "西安银行",
			"中国人民银行", "华夏银行" };
	public static long regist_time;// 注册时的验证码监听时间
	public static final String CARSON_CALL_NUMBER = "0755-23742220";// 联系客服电话
	public static ArrayList<HuanxinUser> usersNick = new ArrayList<>();
    //缓存群组的活动的报名人员列表
    public static Map<String,GroupSettingRequest.AppliantResult> groupAppliantCache = new HashMap<>();
    //缓存群组的成员UseId和picture的映射，进入禁言管理的头像加载；1. 从活动群群设置进入，2.从普通群管理进入
    public static Map<String, BaseUser> userIdUserCache = new HashMap<>();
    //缓存群组的类型
    public static Map<String, GroupDescription> groupDescriptionMapCache = new HashMap<>();
    //别名缓存
    public static HashMap<String , Map<String , String >> aliasCache = new HashMap<>();
    //免扰缓存
    public static HashSet<String> disturbCache = new HashSet<>();
    //禁言缓存
    public static HashSet<String> gagCache = new HashSet<>();
}
