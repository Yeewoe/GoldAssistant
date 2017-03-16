/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.parttime.IM;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.carson.constant.ConstantForSaveList;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.GroupReomveListener;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.NormalFileMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VideoMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.chatuidemo.activity.BaiduMapActivity;
import com.easemob.chatuidemo.activity.BaseActivity;
import com.easemob.chatuidemo.activity.ForwardMessageActivity;
import com.easemob.chatuidemo.adapter.ExpressionAdapter;
import com.easemob.chatuidemo.adapter.ExpressionPagerAdapter;
import com.easemob.chatuidemo.adapter.VoicePlayClickListener;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.chatuidemo.utils.ImageUtils;
import com.easemob.chatuidemo.utils.SmileUtils;
import com.easemob.chatuidemo.widget.ExpandGridView;
import com.easemob.chatuidemo.widget.PasteEditText;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMLog;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.parttime.IM.activitysetting.GroupResumeSettingActivity;
import com.parttime.addresslist.NormalGroupSettingActivity;
import com.parttime.constants.ActionConstants;
import com.parttime.constants.ActivityExtraAndKeys;
import com.parttime.constants.ApplicationConstants;
import com.parttime.constants.SharedPreferenceConstants;
import com.parttime.guide.GuideChatActivity;
import com.parttime.net.DefaultCallback;
import com.parttime.net.GroupSettingRequest;
import com.parttime.net.HuanXinRequest;
import com.parttime.net.UserDetailRequest;
import com.parttime.pojo.GroupDescription;
import com.parttime.utils.IntentManager;
import com.parttime.utils.SharePreferenceUtil;
import com.qingmu.jianzhidaren.BuildConfig;
import com.qingmu.jianzhidaren.R;
import com.quark.jianzhidaren.ApplicationControl;
import com.quark.model.HuanxinUser;
import com.quark.quanzi.UserInfo;
import com.quark.us.ShareMyJianZhiActivity;
import com.quark.volley.VolleySington;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天页面
 *
 */
public class ChatActivity extends BaseActivity implements OnClickListener {

    private static String TAG = "ChatActivity";

    private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
    public static final int REQUEST_CODE_CONTEXT_MENU = 3;
    private static final int REQUEST_CODE_MAP = 4;
    public static final int REQUEST_CODE_TEXT = 5;
    public static final int REQUEST_CODE_VOICE = 6;
    public static final int REQUEST_CODE_PICTURE = 7;
    public static final int REQUEST_CODE_LOCATION = 8;
    public static final int REQUEST_CODE_NET_DISK = 9;
    public static final int REQUEST_CODE_FILE = 10;
    public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
    public static final int REQUEST_CODE_PICK_VIDEO = 12;
    public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
    public static final int REQUEST_CODE_VIDEO = 14;
    public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
    public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
    public static final int REQUEST_CODE_SEND_USER_CARD = 17;
    public static final int REQUEST_CODE_CAMERA = 18;
    public static final int REQUEST_CODE_LOCAL = 19;
    public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
    public static final int REQUEST_CODE_GROUP_DETAIL = 21;
    public static final int REQUEST_CODE_SELECT_VIDEO = 23;
    public static final int REQUEST_CODE_SELECT_FILE = 24;
    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;
    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
    public static final int RESULT_CODE_OPEN = 4;
    public static final int RESULT_CODE_DWONLOAD = 5;
    public static final int RESULT_CODE_TO_CLOUD = 6;
    public static final int RESULT_CODE_EXIT_GROUP = 7;
    public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final String COPY_IMAGE = "EASEMOBIMG";
    public static final String COM_CARSON_SHARE_JIANZHI = "com.carson.share.jianzhi";

    public static ChatActivity activityInstance = null;

    private View recordingContainer;
    private ImageView micImage;
    private TextView recordingHint;
    private ListView listView;
    private PasteEditText mEditTextContent;
    private View buttonSetModeKeyboard;
    private View buttonSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    // private ViewPager expressionViewpager;
    private LinearLayout emojiIconContainer;
    private LinearLayout btnContainer,activityDetailContainer;
    private ImageView locationImgview , activityManagementImgview, noticeStatusImg;
    private View more;
    private ImageView iv_emoticons_normal;
    private ImageView iv_emoticons_checked;
    private RelativeLayout edittextLayout;
    private ProgressBar loadmorePB;
    private Button btnMore;
    private LinearLayout container_zidingyi_msg;// 分享兼职布局,商家端隐藏、客户端显示
    private TextView nameVeiw,memberNum;
    private RelativeLayout topLayout;
    private ChatBottomBarHelper chatBottomBarHelper;

    private ClipboardManager clipboard;
    private InputMethodManager manager;
    private EMConversation conversation;
    private NewMessageBroadcastReceiver receiver;
    private ViewPager expressionViewpager;
    private MessageAdapter adapter;
    private VoiceRecorder voiceRecorder;
    private GroupListener groupListener;
    private ReceiveBroadCast receiveBroadCast;// 注册监听是否分享我的兼职
    private SharePreferenceUtil sp;

    private ResumeStatusChangeReceiver resumeReceiver;

    RequestQueue queue = VolleySington.getInstance().getRequestQueue();
    private Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 切换msg切换图片
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };

    // 给谁发送消息
    protected String toChatUsername;
    private File cameraFile;
    public static int resendPos;
    private boolean isloading;
    private final int pagesize = 20;
    private boolean haveMoreData = true;
    private int position;
    private List<String> reslist;
    private Drawable[] micImages;
    private int chatType;
    public String playMsgId;
    protected EMGroup group;
    private String user_id;// id 商家or用户
    protected String noticeContent;
    protected GroupDescription groupDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sp = SharePreferenceUtil.getInstance(ApplicationControl.getInstance());
        initView();
        setUpView();

        // 注册广播监听是否分享了我的兼职
        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(COM_CARSON_SHARE_JIANZHI);
        registerReceiver(receiveBroadCast, filter);
        if(group != null) {
            String description = group.getDescription();
            noticeContent = group.getDescription();
            if(! TextUtils.isEmpty(description)) {
                try {
                    description = URLDecoder.decode(description, "UTF-8");
                    groupDescription = new Gson().fromJson(description, GroupDescription.class);
                } catch (IllegalStateException | JsonSyntaxException | UnsupportedEncodingException ignore) {
                    Log.e(TAG, "description format is error , description = " + description);
                }
            }
        }
//        ConstantForSaveList.groupAppliantCache.get(toChatUsername) == null
        if(chatType != CHATTYPE_SINGLE ){ //群聊


            try {
                if (groupDescription != null && (groupDescription.type == GroupDescription.ACTIVITY_GROUP ||
                        groupDescription.type == GroupDescription.ACTIVITY_CONSULTATION_GROUP)) {
                    activityDetailContainer.setVisibility(View.VISIBLE);
                    Map<String,GroupDescription> cache = ConstantForSaveList.groupDescriptionMapCache;
                    GroupDescription groupDescription = cache.get(toChatUsername);
                    if(groupDescription != null && groupDescription.isNew) {
                        noticeStatusImg.setVisibility(View.VISIBLE);
                    }
                    //获取报名列表
                    getGroupApliantResult(toChatUsername);
                }
            }catch(IllegalStateException | JsonSyntaxException ignore){
            }
        }


    }

    /**
     * initView
     */
    protected void initView() {
        recordingContainer = findViewById(R.id.recording_container);
        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);
        listView = (ListView) findViewById(R.id.list);
        mEditTextContent = (PasteEditText) findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittextLayout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
        btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
        activityDetailContainer = (LinearLayout) findViewById(R.id.activity_management_container);
        activityManagementImgview = (ImageView) findViewById(R.id.imgv_activity_management);
        noticeStatusImg = (ImageView) findViewById(R.id.notice_update_status);
        locationImgview = (ImageView) findViewById(R.id.btn_location);
        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);
        loadmorePB = (ProgressBar) findViewById(R.id.pb_load_more);
        btnMore = (Button) findViewById(R.id.btn_more);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        topLayout = (RelativeLayout) findViewById(R.id.top_bar);
        // 商家端隐藏分享兼职、用户端显示分享兼职
        container_zidingyi_msg = (LinearLayout) findViewById(R.id.container_zidingyi_msg);
        container_zidingyi_msg.setVisibility(View.GONE);
        more = findViewById(R.id.more);
//        edittextLayout.setBackgroundResource(R.drawable.input_bar_bg_normal);

        // 动画资源文件,用于录制语音时
        micImages = new Drawable[] {
                getResources().getDrawable(R.drawable.record_animate_01),
                getResources().getDrawable(R.drawable.record_animate_02),
                getResources().getDrawable(R.drawable.record_animate_03),
                getResources().getDrawable(R.drawable.record_animate_04),
                getResources().getDrawable(R.drawable.record_animate_05),
                getResources().getDrawable(R.drawable.record_animate_06),
                getResources().getDrawable(R.drawable.record_animate_07),
                getResources().getDrawable(R.drawable.record_animate_08),
                getResources().getDrawable(R.drawable.record_animate_09),
                getResources().getDrawable(R.drawable.record_animate_10),
                getResources().getDrawable(R.drawable.record_animate_11),
                getResources().getDrawable(R.drawable.record_animate_12),
                getResources().getDrawable(R.drawable.record_animate_13),
                getResources().getDrawable(R.drawable.record_animate_14),
        };

        // 表情list
        reslist = getExpressionRes(35);
        // 初始化表情viewpager
        List<View> views = new ArrayList<>();
        View gv1 = getGridChildView(1);
        View gv2 = getGridChildView(2);
        views.add(gv1);
        views.add(gv2);
        expressionViewpager.setAdapter(new ExpressionPagerAdapter(views));
        edittextLayout.requestFocus();
        voiceRecorder = new VoiceRecorder(micImageHandler);
        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
//        mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    edittextLayout
//                            .setBackgroundResource(R.drawable.input_bar_bg_active);
//                } else {
//                    edittextLayout
//                            .setBackgroundResource(R.drawable.input_bar_bg_normal);
//                }
//
//            }
//        });
        mEditTextContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                edittextLayout
//                        .setBackgroundResource(R.drawable.input_bar_bg_active);
                moreGone();
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
            }
        });
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setUpView() {

        Intent intent = getIntent();
        if(intent != null){
            boolean toActivitySetting = intent.getBooleanExtra(ActivityExtraAndKeys.TO_ACTIVITY_SETTING,false);
            if(toActivitySetting){
                String groupId = intent.getStringExtra(ActivityExtraAndKeys.GroupSetting.GROUPID);
                Intent i = new Intent(this, GroupResumeSettingActivity.class);
                i.putExtra(ActivityExtraAndKeys.GroupSetting.GROUPID, groupId);
                startActivity(i);
            }
        }

        activityInstance = this;
        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_checked.setOnClickListener(this);
        btnMore.setOnClickListener(this);

        nameVeiw = (TextView) findViewById(R.id.name);
        memberNum = (TextView) findViewById(R.id.number);

        // position = getIntent().getIntExtra("position", -1);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE))
                .newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
        // 判断单聊还是群聊
        chatType = getIntent().getIntExtra(ActivityExtraAndKeys.chatType, CHATTYPE_SINGLE);
        if (isSingleChat()) { // 单聊
            toChatUsername = getIntent().getStringExtra("userId");
            //chatBottomBarHelper = new ChatBottomBarHelper(this);
            if (toChatUsername != null && !"".equals(toChatUsername)) {
                if (ApplicationConstants.JZDR.equals(toChatUsername)) {
                    sp.saveSharedPreferences(ApplicationConstants.JZDR + "realname", "兼职达人团队");
                    findViewById(R.id.container_contact_detail).setVisibility(View.GONE);
                } else if (ApplicationConstants.CAIWU.equals(toChatUsername)) {
                    sp.saveSharedPreferences(ApplicationConstants.CAIWU + "realname", "财务小助手");
                    findViewById(R.id.container_contact_detail).setVisibility(View.GONE);
                } else if (ApplicationConstants.DINGYUE.equals(toChatUsername)) {
                    sp.saveSharedPreferences(ApplicationConstants.DINGYUE + "realname", "活动小助手");
                    findViewById(R.id.container_contact_detail).setVisibility(View.GONE);
                } else if (ApplicationConstants.KEFU.equals(toChatUsername)) {
                    sp.saveSharedPreferences(ApplicationConstants.KEFU + "realname", "兼职达人客服");
                    findViewById(R.id.container_contact_detail).setVisibility(View.GONE);
                    chatBottomBarHelper = new ChatBottomBarHelper(this);
                } else if (ApplicationConstants.TONGZHI.equals(toChatUsername)) {
                    sp.saveSharedPreferences(ApplicationConstants.TONGZHI + "realname", "活动小助手");
                    findViewById(R.id.container_contact_detail).setVisibility(View.GONE);
                }
            }

            // 设置标题名字
            // 如果之前存在则读取本地数据,反之网络获取
            String chatName = sp.loadStringSharedPreference(toChatUsername + "realname", "");
            if (!"".equals(chatName)) {
                nameVeiw.setText(chatName);
            } else {
                getNick(toChatUsername, nameVeiw);
            }
        } else {
            // 群聊
            findViewById(R.id.container_right2_image).setVisibility(View.VISIBLE);
            findViewById(R.id.container_group_notice).setVisibility(View.VISIBLE);
            findViewById(R.id.container_contact_detail).setVisibility(View.GONE);
            findViewById(R.id.container_voice_call).setVisibility(View.GONE);
            toChatUsername = getIntent().getStringExtra("groupId");
            group = EMGroupManager.getInstance().getGroup(toChatUsername);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final EMGroup returnGroup = EMGroupManager.getInstance()
                                .getGroupFromServer(toChatUsername);
                        // 更新本地数据
                        EMGroupManager.getInstance().createOrUpdateLocalGroup(
                                returnGroup);
                        if (group != null) {
                            ChatActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setGroupChatTitle();
                                    String description = returnGroup.getDescription();
                                    updateGroupNoticeStatus(description);
                                }
                            });

                        }
                    }catch (Exception ignore){

                    }
                }
            }).start();
            if (group != null) {
                setGroupChatTitle();
            }
            // conversation =
            // EMChatManager.getInstance().getConversation(toChatUsername,true);
        }
        conversation = EMChatManager.getInstance().getConversation(
                toChatUsername);
        // 把此会话的未读数置为0
        conversation.resetUnreadMsgCount();
        // 2015-4-7新增加载消息
        // **************************开始************************************
        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
        // 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            if (isSingleChat()) {
                conversation.loadMoreMsgFromDB(msgId, pagesize);
            } else {
                conversation.loadMoreGroupMsgFromDB(msgId, pagesize);
            }
        }
        // **************************开始************************************
        adapter = new MessageAdapter(this, toChatUsername, chatType);
        // 显示消息
        listView.setAdapter(adapter);
        // ==================点击显示个人信息==================
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                EMConversation conversation = adapter
                        .getEMConversationItem();
                String username = conversation.getUserName();

                if (username.equals(ApplicationControl.getInstance()
                        .getUserName()))
                    Toast.makeText(ChatActivity.this, "你自己", Toast.LENGTH_SHORT).show();
                else {
                    // 进入个人资料
                    Intent intent = new Intent(ChatActivity.this,
                            UserInfo.class);
                    // startActivity(intent);
                }
            }
        });

        listView.setOnScrollListener(new ListScrollListener());
        int count = listView.getCount();
        if (count > 0) {
            listView.setSelection(count - 1);
        }

        listView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                moreGone();
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
                return false;
            }
        });

        resumeReceiver = new ResumeStatusChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ActionConstants.ACTION_RESUME_STATUS_CHANGE);
        registerReceiver(resumeReceiver,filter);

        // 注册接收消息广播
        receiver = new NewMessageBroadcastReceiver();
        receiver.chatId = new StringBuilder(toChatUsername+"");
        IntentFilter intentFilter = new IntentFilter(EMChatManager
                .getInstance().getNewMessageBroadcastAction());
        // 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
        intentFilter.setPriority(5);
        registerReceiver(receiver, intentFilter);

        // 注册一个ack回执消息的BroadcastReceiver
        IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
                .getInstance().getAckMessageBroadcastAction());
        ackMessageIntentFilter.setPriority(5);
        registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

        // 注册一个消息送达的BroadcastReceiver
        IntentFilter deliveryAckMessageIntentFilter = new IntentFilter(
                EMChatManager.getInstance()
                        .getDeliveryAckMessageBroadcastAction());
        deliveryAckMessageIntentFilter.setPriority(5);
        registerReceiver(deliveryAckMessageReceiver,
                deliveryAckMessageIntentFilter);

        // 监听当前会话的群聊解散被T事件
        groupListener = new GroupListener();
        EMGroupManager.getInstance().addGroupChangeListener(groupListener);

        // show forward message if the message is not null
        String forward_msg_id = getIntent().getStringExtra("forward_msg_id");
        if (forward_msg_id != null) {
            // 显示发送要转发的消息
            forwardMessage(forward_msg_id);
        }

    }

    private void updateGroupNoticeStatus(String description) {
        if(! TextUtils.isEmpty(description)) {
            try {
                description = URLDecoder.decode(description, "UTF-8");
                groupDescription = new Gson().fromJson(description, GroupDescription.class);
            } catch (IllegalStateException | JsonSyntaxException | UnsupportedEncodingException ignore) {
                Log.e(TAG, "description format is error , description = " + description);
            }
        }
        Map<String,GroupDescription> cache = ConstantForSaveList.groupDescriptionMapCache;
        GroupDescription gd = cache.get(toChatUsername);
        if(gd != null && ! groupDescription.info.equals(gd.info)) {
            gd.info = groupDescription.info;
            noticeStatusImg.setVisibility(View.VISIBLE);
        }
    }

    private boolean isSingleChat() {
        return chatType == CHATTYPE_SINGLE;
    }

    private void setGroupChatTitle() {
        //数量包括群主
        int memberCount = group.getAffiliationsCount();
        nameVeiw.setText(group.getGroupName());
        memberNum.setText(getString(R.string.group_member_number, memberCount > 0 ? memberCount : 1));
    }

    /**
     * onActivityResult
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE_EXIT_GROUP) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case RESULT_CODE_COPY: // 复制消息
                    EMMessage copyMsg = (adapter.getItem(data
                            .getIntExtra("position", -1))).message;
                    // clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
                    // ((TextMessageBody) copyMsg.getBody()).getMessage()));
                    clipboard.setText(((TextMessageBody) copyMsg.getBody())
                            .getMessage());
                    break;
                case RESULT_CODE_DELETE: // 删除消息
                    EMMessage deleteMsg = adapter.getItem(data
                            .getIntExtra("position", -1)).message;
                    conversation.removeMessage(deleteMsg.getMsgId());
                    adapter.refresh();
                    listView.setSelection(data.getIntExtra("position",
                            adapter.getCount()) - 1);
                    break;

                case RESULT_CODE_FORWARD: // 转发消息
                    EMMessage forwardMsg = adapter.getItem(data
                            .getIntExtra("position", 0)).message;
                    Intent intent = new Intent(this, ForwardMessageActivity.class);
                    intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
                    startActivity(intent);
                    break;

                default:
                    break;
            }
        }
        if (resultCode == RESULT_OK) { // 清空消息
            if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
                // 清空会话
                EMChatManager.getInstance().clearConversation(toChatUsername);
                adapter.refresh();
            } else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (cameraFile != null && cameraFile.exists())
                    sendPicture(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_SELECT_VIDEO) { // 发送本地选择的视频
                int duration = data.getIntExtra("dur", 0);
                String videoPath = data.getStringExtra("path");
                File file = new File(PathUtil.getInstance().getImagePath(),
                        "thvideo" + System.currentTimeMillis());
                Bitmap bitmap = null;
                FileOutputStream fos = null;
                try {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                    if (bitmap == null) {
                        EMLog.d("chatactivity",
                                "problem load video thumbnail bitmap,use default icon");
                        bitmap = BitmapFactory.decodeResource(getResources(),
                                R.drawable.app_panel_video_icon);
                    }
                    fos = new FileOutputStream(file);

                    bitmap.compress(CompressFormat.JPEG, 100, fos);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        fos = null;
                    }
                    if (bitmap != null) {
                        bitmap.recycle();
                        bitmap = null;
                    }

                }
                sendVideo(videoPath, file.getAbsolutePath(), duration / 1000);

            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            } else if (requestCode == REQUEST_CODE_SELECT_FILE) { // 发送选择的文件
                if (data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        sendFile(uri);
                    }
                }

            } else if (requestCode == REQUEST_CODE_MAP) { // 地图
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    more(more);
                    sendLocationMsg(latitude, longitude, "", locationAddress);
                } else {
                    Toast.makeText(this, "无法获取到您的位置信息！", Toast.LENGTH_SHORT).show();
                }
                // 重发消息
            } else if (requestCode == REQUEST_CODE_TEXT
                    || requestCode == REQUEST_CODE_VOICE
                    || requestCode == REQUEST_CODE_PICTURE
                    || requestCode == REQUEST_CODE_LOCATION
                    || requestCode == REQUEST_CODE_VIDEO
                    || requestCode == REQUEST_CODE_FILE) {
                resendMessage();
            } else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
                // 粘贴
                if (!TextUtils.isEmpty(clipboard.getText())) {
                    String pasteText = clipboard.getText().toString();
                    if (pasteText.startsWith(COPY_IMAGE)) {
                        // 把图片前缀去掉，还原成正常的path
                        sendPicture(pasteText.replace(COPY_IMAGE, ""));
                    }

                }
            } else if (requestCode == REQUEST_CODE_ADD_TO_BLACKLIST) { // 移入黑名单
                EMMessage deleteMsg = (EMMessage) adapter.getItem(data
                        .getIntExtra("position", -1)).message;
                addUserToBlacklist(deleteMsg.getFrom());
            } else if (conversation.getMsgCount() > 0) {
                adapter.refresh();
                setResult(RESULT_OK);
            } else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
                adapter.refresh();
            }
        }
    }

    /**
     * 消息图标点击事件
     *
     * @param view View
     */
    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
            String s = mEditTextContent.getText().toString();
            sendText(s);
        } else if (id == R.id.btn_take_picture) {
            selectPicFromCamera();// 点击照相图标
        } else if (id == R.id.btn_picture) {
            selectPicFromLocal(); // 点击图片图标
        } else if (id == R.id.btn_location) { // 位置
            startActivityForResult(new Intent(this, BaiduMapActivity.class),
                    REQUEST_CODE_MAP);
        } else if (id == R.id.iv_emoticons_normal) { // 点击显示表情框
            more.setVisibility(View.VISIBLE);
            iv_emoticons_normal.setVisibility(View.INVISIBLE);
            iv_emoticons_checked.setVisibility(View.VISIBLE);
            btnContainer.setVisibility(View.GONE);
            emojiIconContainer.setVisibility(View.VISIBLE);
            hideKeyboard();
        } else if (id == R.id.iv_emoticons_checked) { // 点击隐藏表情框
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
            btnContainer.setVisibility(View.VISIBLE);
            emojiIconContainer.setVisibility(View.GONE);
            moreGone();

        } else if (id == R.id.btn_video) {
            // 点击摄像图标
            /*Intent intent = new Intent(ChatActivity.this,
                    ImageGridActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);*/
        } else if (id == R.id.btn_file) { // 点击文件图标
            //selectFileFromLocal();

        } else if (id == R.id.btn_voice_call) { // 点击语音电话图标
            /*if (!EMChatManager.getInstance().isConnected())
                Toast.makeText(this, "尚未连接至服务器，请稍后重试", Toast.LENGTH_SHORT).show();
            else
                startActivity(new Intent(ChatActivity.this,
                        VoiceCallActivity.class).putExtra("username",
                        toChatUsername).putExtra("isComingCall", false));*/

        }else if(id == R.id.imgv_activity_management){ //活动管理
            IntentManager.openJobDetailActivity(activityInstance,0,toChatUsername);
        } else if (id == R.id.btn_zidingyi_msg) {
            // 先到选择我的收藏界面,选择兼职发送
            Intent intent = new Intent(ChatActivity.this,
                    ShareMyJianZhiActivity.class);
            intent.putExtra("isFromShare", true);
            startActivity(intent);
            btnContainer.setVisibility(View.GONE);// 隐藏弹出框
            // 发送自定义图文消息
            // sendZiDingyiMsg("荷尔美试吃促销 110元/日", "1000人/日", "深圳", "05-17",
            // "05-27", "1001");
        }else if(id == R.id.btn_more){
            more(view);
        }
    }

    /**
     * 注册广播用于处理切换城市位置更新兼职信息
     */
    class ReceiveBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 得到广播中得到的数据，并显示出来
            String activity_id = intent.getExtras().getString("activity_id");
            String title = intent.getExtras().getString("title");
            int pay = intent.getExtras().getInt("pay");
            int pay_type = intent.getExtras().getInt("pay_type");
            String job_place = intent.getExtras().getString("job_place");
            String start_time = intent.getExtras().getString("start_time");
            int left_count = intent.getExtras().getInt("left_count");
            // 发送自定义图文消息
            sendZiDingyiMsg(activity_id, title, pay, pay_type, job_place,
                    start_time, left_count);

        }

    }

    /**
     * 照相获取图片
     */
    public void selectPicFromCamera() {
        if (!CommonUtils.isExitsSdcard()) {
            Toast.makeText(getApplicationContext(), "SD卡不存在，不能拍照", Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(),
                ApplicationControl.getInstance().getUserName()
                        + System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
                        MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * 选择文件
     */
    private void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * 从图库获取图片
     */
    public void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    /**
     * 发送文本消息
     *
     * @param content
     *            message content
     */
    private void sendText(String content) {
        if(isGag()){
            return ;
        }

        if (content.length() > 0) {
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP)
                message.setChatType(ChatType.GroupChat);
            TextMessageBody txtBody = new TextMessageBody(content);
            // 设置消息body
            message.addBody(txtBody);
            // 设置要发给谁,用户username或者群聊groupid
            message.setReceipt(toChatUsername);
            // 把messgage加到conversation中
            conversation.addMessage(message);
            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            mEditTextContent.setText("");
            setResult(RESULT_OK);

        }
    }

    /**
     * 发送语音
     *
     * @param filePath String
     * @param fileName String
     * @param length String
     * @param isResend boolean
     */
    private void sendVoice(String filePath, String fileName, String length,
                           boolean isResend) {
        if(isGag()){
            return ;
        }
        if (!(new File(filePath).exists())) {
            return;
        }
        try {
            final EMMessage message = EMMessage
                    .createSendMessage(EMMessage.Type.VOICE);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP)
                message.setChatType(ChatType.GroupChat);
            message.setReceipt(toChatUsername);
            int len = Integer.parseInt(length);
            VoiceMessageBody body = new VoiceMessageBody(new File(filePath),
                    len);
            message.addBody(body);

            conversation.addMessage(message);
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            setResult(RESULT_OK);
            // send file
            // sendVoiceSub(filePath, fileName, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送图片
     *
     * @param filePath String
     */
    private void sendPicture(final String filePath) {
        if(isGag()){
            return ;
        }
        String to = toChatUsername;
        // create and add image message in view
        final EMMessage message = EMMessage
                .createSendMessage(EMMessage.Type.IMAGE);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(ChatType.GroupChat);

        message.setReceipt(to);
        ImageMessageBody body = new ImageMessageBody(new File(filePath));
        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
        // body.setSendOriginalImage(true);
        message.addBody(body);
        conversation.addMessage(message);

        listView.setAdapter(adapter);
        adapter.refresh();
        listView.setSelection(listView.getCount() - 1);
        setResult(RESULT_OK);
        // more(more);
    }

    /**
     * 发送视频消息
     */
    private void sendVideo(final String filePath, final String thumbPath,
                           final int length) {
        final File videoFile = new File(filePath);
        if (!videoFile.exists()) {
            return;
        }
        try {
            EMMessage message = EMMessage
                    .createSendMessage(EMMessage.Type.VIDEO);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP)
                message.setChatType(ChatType.GroupChat);
            String to = toChatUsername;
            message.setReceipt(to);
            VideoMessageBody body = new VideoMessageBody(videoFile, thumbPath,
                    length, videoFile.length());
            message.addBody(body);
            conversation.addMessage(message);
            listView.setAdapter(adapter);
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            setResult(RESULT_OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage Uri
     */
    private void sendPicByUri(Uri selectedImage) {
        if(isGag()){
            return ;
        }
        // String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage, null, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex("_data");
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendPicture(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, "找不到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendPicture(file.getAbsolutePath());
        }

    }

    /**
     * 发送位置信息
     *
     * @param latitude double
     * @param longitude double
     * @param imagePath String
     * @param locationAddress String
     */
    private void sendLocationMsg(double latitude, double longitude,
                                 String imagePath, String locationAddress) {
        if(isGag()){
            return ;
        }
        EMMessage message = EMMessage
                .createSendMessage(EMMessage.Type.LOCATION);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(ChatType.GroupChat);
        LocationMessageBody locBody = new LocationMessageBody(locationAddress,
                latitude, longitude);
        message.addBody(locBody);
        message.setReceipt(toChatUsername);
        conversation.addMessage(message);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setSelection(listView.getCount() - 1);
        setResult(RESULT_OK);

    }

    /**
     * 发送自定义图文消息
     *
     */
    public void sendZiDingyiMsg(String activityId, String title, int pay,
                                int pay_type, String jobPlace, String jobStartTime, int leftCount) {
        if (title.length() > 0) {
            EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
            // 如果是群聊，设置chattype,默认是单聊
            if (chatType == CHATTYPE_GROUP)
                message.setChatType(ChatType.GroupChat);
            TextMessageBody txtBody = new TextMessageBody(title);
            // 设置消息body
            message.addBody(txtBody);
            // 增加自定义的拓展消息属性 0是没有 1是表示有额外属性
            message.setAttribute("activityExtra", "1");
            message.setAttribute("activityId", activityId);
            message.setAttribute("activityTitle", title);
            String temp = null;
            // 日薪(0),时薪(1)
            if (pay_type == 0) {
                temp = "元/天";
            } else if (pay_type == 1) {
                temp = "元/小时";
            }
            message.setAttribute("activityXinZi", pay + temp);

            message.setAttribute("activityJobPlace", jobPlace);

            message.setAttribute("activityStartTime", jobStartTime);
            message.setAttribute("leftCount", String.valueOf(leftCount));
            // 设置要发给谁,用户username或者群聊groupid
            message.setReceipt(toChatUsername);
            // 把messgage加到conversation中
            conversation.addMessage(message);
            // 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);
            mEditTextContent.setText("");
            setResult(RESULT_OK);

        }
    }

    /**
     * 发送文件
     *
     * @param uri Uri
     */
    private void sendFile(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = getContentResolver().query(uri, projection, null,
                        null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(getApplicationContext(), "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        if (file.length() > 10 * 1024 * 1024) {
            Toast.makeText(getApplicationContext(), "文件不能大于10M", Toast.LENGTH_SHORT).show();
            return;
        }

        // 创建一个文件消息
        EMMessage message = EMMessage.createSendMessage(EMMessage.Type.FILE);
        // 如果是群聊，设置chattype,默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(ChatType.GroupChat);

        message.setReceipt(toChatUsername);
        // add message body
        NormalFileMessageBody body = new NormalFileMessageBody(new File(
                filePath));
        message.addBody(body);
        conversation.addMessage(message);
        listView.setAdapter(adapter);
        adapter.refresh();
        listView.setSelection(listView.getCount() - 1);
        setResult(RESULT_OK);
    }

    /**
     * 重发消息
     */
    private void resendMessage() {
        if(isGag()){
            return ;
        }
        EMMessage msg = conversation.getMessage(resendPos);
        // msg.setBackSend(true);
        msg.status = EMMessage.Status.CREATE;

        adapter.refresh();
        listView.setSelection(resendPos);
    }

    /**
     * 显示语音图标按钮
     *
     * @param view View
     */
    public void setModeVoice(View view) {

        edittextLayout.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
        btnMore.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.VISIBLE);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        btnContainer.setVisibility(View.VISIBLE);
        emojiIconContainer.setVisibility(View.GONE);
        moreGone();
        hideKeyboard();

    }

    /**
     * 显示键盘图标
     *
     * @param view View
     */
    public void setModeKeyboard(View view) {
        // mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener()
        // {
        // @Override
        // public void onFocusChange(View v, boolean hasFocus) {
        // if(hasFocus){
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        // }
        // }
        // });
        edittextLayout.setVisibility(View.VISIBLE);
        moreGone();
        view.setVisibility(View.GONE);
        buttonSetModeVoice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        mEditTextContent.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(mEditTextContent.getText())) {
            btnMore.setVisibility(View.VISIBLE);
            buttonSend.setVisibility(View.GONE);
        } else {
            btnMore.setVisibility(View.GONE);
            buttonSend.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 联系人详情
     *
     * @param view View
     */
    public void contactDetail(View view) {
		/*startActivityForResult(
				new Intent(this, AlertDialog.class)
						.putExtra("titleIsCancel", true)
						.putExtra("msg", "是否清空所有聊天记录")
                        .putExtra("cancel", true),
				REQUEST_CODE_EMPTY_HISTORY);*/
        //startActivity(new Intent(this, PersonAssessDetailActivity.class));
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(toChatUsername);
        IntentManager.intentToUseDetail(ChatActivity.this, toChatUsername,null,userIds,null);

    }

    /**
     * 显示群组通知
     * @param view View
     */
    public void showGroupNotice(View view){
        noticeStatusImg.setVisibility(View.GONE);
        new ChatActivityHelper().showGroupNotice(this, nameVeiw);
    }

    /**
     * 点击进入群组详情
     *
     * @param view View
     */
    public void toGroupDetails(View view) {
        if (groupDescription != null && (groupDescription.type == GroupDescription.ACTIVITY_GROUP/* ||
                groupDescription.type == GroupDescription.ACTIVITY_CONSULTATION_GROUP*/)) {
            startActivityForResult(
                    (new Intent(this, GroupResumeSettingActivity.class).putExtra(
                            "groupId", toChatUsername)), REQUEST_CODE_GROUP_DETAIL);
        }else{
            startActivityForResult(
                    (new Intent(this, NormalGroupSettingActivity.class).putExtra(
                            "groupId", toChatUsername)), REQUEST_CODE_GROUP_DETAIL);
        }
    }

    /**
     * 显示或隐藏图标按钮页
     *
     * @param view View
     */
    public void more(View view) {
        if(BuildConfig.DEBUG){
            Log.i(TAG,"click more");
        }
        if (more.getVisibility() == View.GONE) {
            micImageHandler.post(new Runnable() {
                @Override
                public void run() {
                    more.setVisibility(View.VISIBLE);
                    btnContainer.setVisibility(View.VISIBLE);
                    emojiIconContainer.setVisibility(View.GONE);

                }
            });
            micImageHandler.post(new Runnable() {
                     @Override
                     public void run() {
                         btnMore.setBackgroundResource(R.drawable.type_select_btn_pressed);
                         hideKeyboard();
                     }
                 });

        } else {
            if (emojiIconContainer.getVisibility() == View.VISIBLE) {
                btnContainer.setVisibility(View.VISIBLE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
            } else {
                moreGone();
                btnMore.setBackgroundResource(R.drawable.type_select_btn);
            }

        }

    }

    private void moreGone() {
        more.setVisibility(View.GONE);
        btnMore.setBackgroundResource(R.drawable.type_select_btn);
    }

    /**
     * 点击文字输入框
     *
     * @param v View
     */
    public void editClick(View v) {
        listView.setSelection(listView.getCount() - 1);
        if (more.getVisibility() == View.VISIBLE) {
            moreGone();
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        }

    }

    private class ResumeStatusChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(!isSingleChat()) {
                adapter.refresh();
            }
        }
    }

    /**
     * 消息广播接收者
     *
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        public StringBuilder chatId;

        @Override
        public void onReceive(Context context, Intent intent) {
            // 记得把广播给终结掉
            abortBroadcast();

            String username = intent.getStringExtra("from");
            String msgid = intent.getStringExtra("msgid");
            // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
            EMMessage message = EMChatManager.getInstance().getMessage(msgid);
            // 如果是群聊消息，获取到group id
            if (message.getChatType() == ChatType.GroupChat) {
                username = message.getTo();
            }
            if (!username.equals(chatId.toString())) {
                if(!ConstantForSaveList.disturbCache.contains(message.getTo())) {
                    notifyNewMessage(message);
                }
                // 消息不是发给当前会话，return
                return;
            }

            // conversation =
            // EMChatManager.getInstance().getConversation(toChatUsername);
            // 通知adapter有新消息，更新ui
            adapter.refresh();
            listView.setSelection(listView.getCount() - 1);

        }
    }

    /**
     * 消息回执BroadcastReceiver
     */
    private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();

            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            EMConversation conversation = EMChatManager.getInstance()
                    .getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null) {
                    msg.isAcked = true;
                }
            }
            adapter.notifyDataSetChanged();

        }
    };

    /**
     * 消息送达BroadcastReceiver
     */
    private BroadcastReceiver deliveryAckMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            abortBroadcast();

            String msgid = intent.getStringExtra("msgid");
            String from = intent.getStringExtra("from");
            EMConversation conversation = EMChatManager.getInstance()
                    .getConversation(from);
            if (conversation != null) {
                // 把message设为已读
                EMMessage msg = conversation.getMessage(msgid);
                if (msg != null) {
                    msg.isDelivered = true;
                }
            }

            adapter.notifyDataSetChanged();
        }
    };
    private PowerManager.WakeLock wakeLock;

    /**
     * 按住说话listener
     *
     */
    class PressToSpeakListen implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (!CommonUtils.isExitsSdcard()) {
                        Toast.makeText(ChatActivity.this, "发送语音需要sdcard支持！",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        wakeLock.acquire();
                        if (VoicePlayClickListener.isPlaying)
                            VoicePlayClickListener.currentPlayListener
                                    .stopPlayVoice();
                        recordingContainer.setVisibility(View.VISIBLE);
                        recordingHint
                                .setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                        voiceRecorder.startRecording(null, toChatUsername,
                                getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                        v.setPressed(false);
                        if (wakeLock.isHeld())
                            wakeLock.release();
                        if (voiceRecorder != null)
                            voiceRecorder.discardRecording();
                        recordingContainer.setVisibility(View.INVISIBLE);
                        Toast.makeText(ChatActivity.this, R.string.recoding_fail,
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        recordingHint
                                .setText(getString(R.string.release_to_cancel));
                        recordingHint
                                .setBackgroundResource(R.drawable.recording_text_hint_bg);
                    } else {
                        recordingHint
                                .setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                    }
                    return true;
                }
                case MotionEvent.ACTION_UP:
                    v.setPressed(false);
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (wakeLock.isHeld())
                        wakeLock.release();
                    if (event.getY() < 0) {
                        // discard the recorded audio.
                        voiceRecorder.discardRecording();

                    } else {
                        // stop recording and send voice file
                        try {
                            int length = voiceRecorder.stopRecoding();
                            if (length > 0) {
                                sendVoice(voiceRecorder.getVoiceFilePath(),
                                        voiceRecorder
                                                .getVoiceFileName(toChatUsername),
                                        Integer.toString(length), false);
                            } else if (length == EMError.INVALID_FILE) {
                                Toast.makeText(getApplicationContext(), "无录音权限",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "录音时间太短",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ChatActivity.this, "发送失败，请检测服务器是否连接",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                    return true;
                default:
                    recordingContainer.setVisibility(View.INVISIBLE);
                    if (voiceRecorder != null)
                        voiceRecorder.discardRecording();
                    return false;
            }
        }
    }

    /**
     * 获取表情的gridview的子view
     *
     * @param i int
     * @return View
     */
    private View getGridChildView(int i) {
        View view = View.inflate(this, R.layout.expression_gridview, null);
        ExpandGridView gv = (ExpandGridView) view.findViewById(R.id.gridview);
        List<String> list = new ArrayList<>();
        if (i == 1) {
            List<String> list1 = reslist.subList(0, 20);
            list.addAll(list1);
        } else if (i == 2) {
            list.addAll(reslist.subList(20, reslist.size()));
        }
        list.add("delete_expression");
        final ExpressionAdapter expressionAdapter = new ExpressionAdapter(this,
                1, list);
        gv.setAdapter(expressionAdapter);
        gv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String filename = expressionAdapter.getItem(position);
                try {
                    // 文字输入框可见时，才可输入表情
                    // 按住说话可见，不让输入表情
                    if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {

                        if (! "delete_expression".equals(filename)) { // 不是删除键，显示表情
                            // 这里用的反射，所以混淆的时候不要混淆SmileUtils这个类
                            Class clz = Class
                                    .forName("com.easemob.chatuidemo.utils.SmileUtils");
                            Field field = clz.getField(filename);
                            mEditTextContent.append(SmileUtils.getSmiledText(
                                    ChatActivity.this, (String) field.get(null)));
                        } else { // 删除文字或者表情
                            if (!TextUtils.isEmpty(mEditTextContent.getText())) {

                                int selectionStart = mEditTextContent
                                        .getSelectionStart();// 获取光标的位置
                                if (selectionStart > 0) {
                                    String body = mEditTextContent.getText()
                                            .toString();
                                    String tempStr = body.substring(0,
                                            selectionStart);
                                    int i = tempStr.lastIndexOf("[");// 获取最后一个表情的位置
                                    if (i != -1) {
                                        CharSequence cs = tempStr.substring(i,
                                                selectionStart);
                                        if (SmileUtils.containsKey(cs
                                                .toString()))
                                            mEditTextContent.getEditableText()
                                                    .delete(i, selectionStart);
                                        else
                                            mEditTextContent.getEditableText()
                                                    .delete(selectionStart - 1,
                                                            selectionStart);
                                    } else {
                                        mEditTextContent.getEditableText()
                                                .delete(selectionStart - 1,
                                                        selectionStart);
                                    }
                                }
                            }

                        }
                    }
                } catch (Exception ignore) {
                }

            }
        });
        return view;
    }

    public List<String> getExpressionRes(int getSum) {
        List<String> reslist = new ArrayList<>();
        for (int x = 1; x <= getSum; x++) {
            String filename = "ee_" + x;

            reslist.add(filename);

        }
        return reslist;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
        EMGroupManager.getInstance().removeGroupChangeListener(groupListener);
        try{
            unregisterReceiver(resumeReceiver);
            resumeReceiver = null;
        }catch (Exception ignore){
        }
        // 注销广播
        try {
            unregisterReceiver(receiver);
            receiver = null;
        } catch (Exception ignore) {
        }
        try {
            unregisterReceiver(ackMessageReceiver);
            ackMessageReceiver = null;
            unregisterReceiver(deliveryAckMessageReceiver);
            deliveryAckMessageReceiver = null;
        } catch (Exception ignore) {
        }
        if (receiveBroadCast != null) {
            unregisterReceiver(receiveBroadCast);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean guideShow = sp.loadBooleanSharedPreference(
                SharedPreferenceConstants.CHAT_GUIDE_NOT_SHOW,
                false);
        if(! guideShow && groupDescription != null && groupDescription.type == GroupDescription.ACTIVITY_GROUP ){
            sp.saveSharedPreferences(SharedPreferenceConstants.CHAT_GUIDE_NOT_SHOW, true);
            startActivity(new Intent(this, GuideChatActivity.class));
        }

        if (group != null){
            setGroupChatTitle();
            if(group.getOwner().equals(EMChatManager.getInstance().getCurrentUser())){
                //获取群成员的别名列表
                getGroupAlias();
            }
        }
        adapter.refresh();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wakeLock.isHeld())
            wakeLock.release();
        if (VoicePlayClickListener.isPlaying
                && VoicePlayClickListener.currentPlayListener != null) {
            // 停止语音播放
            VoicePlayClickListener.currentPlayListener.stopPlayVoice();
        }

        try {
            // 停止录音
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                recordingContainer.setVisibility(View.INVISIBLE);
            }
        } catch (Exception ignore) {
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 加入到黑名单
     *
     * @param username Strings
     */
    private void addUserToBlacklist(String username) {
        try {
            EMContactManager.getInstance().addUserToBlackList(username, false);
            Toast.makeText(getApplicationContext(), "移入黑名单成功", Toast.LENGTH_SHORT).show();
        } catch (EaseMobException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "移入黑名单失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 返回
     *
     * @param view View
     */
    public void back(View view) {
        finish();
    }

    /**
     * 覆盖手机返回键
     */
    @Override
    public void onBackPressed() {
        if (more.getVisibility() == View.VISIBLE) {
            moreGone();
            iv_emoticons_normal.setVisibility(View.VISIBLE);
            iv_emoticons_checked.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * listview滑动监听listener
     *
     */
    private class ListScrollListener implements OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_IDLE:
                    if (view.getFirstVisiblePosition() == 0 && !isloading
                            && haveMoreData) {
                        loadmorePB.setVisibility(View.VISIBLE);
                        // sdk初始化加载的聊天记录为20条，到顶时去db里获取更多
                        List<EMMessage> messages;
                        try {
                            // 获取更多messges，调用此方法的时候从db获取的messages
                            // sdk会自动存入到此conversation中
                            if (isSingleChat())
                                messages = conversation.loadMoreMsgFromDB(adapter
                                        .getItem(0).message.getMsgId(), pagesize);
                            else
                                messages = conversation.loadMoreGroupMsgFromDB(
                                        adapter.getItem(0).message.getMsgId(), pagesize);
                        } catch (Exception e1) {
                            loadmorePB.setVisibility(View.GONE);
                            return;
                        }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException ignore) {
                        }
                        if (messages.size() != 0) {
                            // 刷新ui
                            adapter.notifyDataSetChanged();
                            listView.setSelection(messages.size() - 1);
                            if (messages.size() != pagesize)
                                haveMoreData = false;
                        } else {
                            haveMoreData = false;
                        }
                        loadmorePB.setVisibility(View.GONE);
                        isloading = false;

                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        // 点击notification bar进入聊天页面，保证只有一个聊天页面
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }

    /**
     * 转发消息
     *
     * @param forward_msg_id String
     */
    protected void forwardMessage(String forward_msg_id) {
        EMMessage forward_msg = EMChatManager.getInstance().getMessage(
                forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
            case TXT:
                // 获取消息内容，发送消息
                String content = ((TextMessageBody) forward_msg.getBody())
                        .getMessage();
                sendText(content);
                break;
            case IMAGE:
                // 发送图片
                String filePath = ((ImageMessageBody) forward_msg.getBody())
                        .getLocalUrl();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        // 不存在大图发送缩略图
                        filePath = ImageUtils.getThumbnailImagePath(filePath);
                    }
                    sendPicture(filePath);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 监测群组解散或者被T事件
     *
     */
    class GroupListener extends GroupReomveListener {

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(ChatActivity.this, "你被群创建者从此群中移除", Toast.LENGTH_SHORT)
                                .show();
                        if (NormalGroupSettingActivity.instance != null)
                            NormalGroupSettingActivity.instance.finish();
                        finish();
                    }
                }
            });
        }

        @Override
        public void onGroupDestroy(final String groupId, String groupName) {
            // 群组解散正好在此页面，提示群组被解散，并finish此页面
            runOnUiThread(new Runnable() {
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(ChatActivity.this, "当前群聊已被群创建者解散", Toast.LENGTH_SHORT)
                                .show();
                        if (NormalGroupSettingActivity.instance != null)
                            NormalGroupSettingActivity.instance.finish();
                        finish();
                    }
                }
            });
        }

    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    public void getNick(final String id, final TextView name) {
        new HuanXinRequest().getHuanxinUserList(String.valueOf(id), queue, new DefaultCallback() {
            @Override
            public void success(Object obj) {
                super.success(obj);
                if (obj instanceof ArrayList) {
                    @SuppressLint("Unchecked")
                    ArrayList<HuanxinUser> list = (ArrayList<HuanxinUser>) obj;
                    if (list.size() == 1) {
                        if (name != null) {
                            HuanxinUser us = list.get(0);
                            name.setText(us.getName());
                            sp.saveSharedPreferences(id + "realname", us.getName());
                        }
                    }
                }
            }
        });
    }

    public void getGroupApliantResult(final String id){
        try {
            new GroupSettingRequest().getUserList(id, queue, new DefaultCallback() {
                @Override
                public void success(Object obj) {
                    super.success(obj);
                    if (obj instanceof GroupSettingRequest.AppliantResult) {
                        GroupSettingRequest.AppliantResult result = (GroupSettingRequest.AppliantResult) obj;
                        List<GroupSettingRequest.UserVO> userList = result.userList;
                        adapter.refresh();
                    }
                }
            });
        }catch(Exception e){
            Log.e(TAG, Log.getStackTraceString(e));
        }

    }


    private void getGroupAlias() {
        new UserDetailRequest().getUserRemarkList(toChatUsername,queue,new DefaultCallback(){
            @Override
            public void success(Object obj) {
               if(obj != null){
                   @SuppressWarnings("unchecked")
                   ArrayList<UserDetailRequest.AliasVO> aliasVOs = (ArrayList<UserDetailRequest.AliasVO>)obj;
                   if(aliasVOs.size() > 0){
                       HashMap<String, String> tempMap = new HashMap<>();
                       for(UserDetailRequest.AliasVO aliasVO : aliasVOs){
                           if(aliasVO == null){
                               continue;
                           }
                           tempMap.put(aliasVO.userId,aliasVO.alias);
                       }
                       Map<String,String> aliasCache = ConstantForSaveList.aliasCache.get(toChatUsername);
                       if(aliasCache != null && aliasCache.size() == tempMap.size()){
                           boolean reflash = false;
                           for(Map.Entry<String ,String> entry : tempMap.entrySet()){
                               if(! aliasCache.containsKey(entry.getKey())){
                                    reflash = true;
                                   break;
                               }
                           }
                           if(reflash){
                               ConstantForSaveList.aliasCache.put(toChatUsername, tempMap);
                               adapter.reflashAliasName(tempMap);
                           }
                       }else {
                           ConstantForSaveList.aliasCache.put(toChatUsername, tempMap);
                           adapter.reflashAliasName(tempMap);
                       }
                   }
               }
            }
        });
    }

    /**
     * 判断是否禁言
     * @return false: 没有禁言
     *         true:被禁言
     */
    public boolean isGag(){
        if(isSingleChat()){
            return false;
        }
        if(ConstantForSaveList.gagCache != null){
            boolean contain = ConstantForSaveList.gagCache.contains(toChatUsername);
            if(contain){
                Toast.makeText(this,"您已被禁言",Toast.LENGTH_LONG).show();
            }
            return contain;
        }
        return false;
    }

}
