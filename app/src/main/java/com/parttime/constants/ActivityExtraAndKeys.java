package com.parttime.constants;

/**
 *
 * Created by luhua on 15/7/12.
 */
public class ActivityExtraAndKeys {

    public static final String USER_ID = "user_id";
    public static final String FROM_WHERE = "from_where";
    public static final String name = "name";
    public static final String chatType = "chatType";

    public static final String TO_ACTIVITY_SETTING = "to_activity_setting";

    public static final int LOAD_PICTURE = 0x0010;

    public static class ExtraLogin{
        public static String key = "";
    }

    public static class ChatGroupNotice{
        public static String GROUP_NOTICE_CONTENT="Group_Notice_Content";
    }

    public static class GroupSetting{
        public static String ADMITTED = "Admitted"; //已录取
        public static String PENDING = "pending"; //待处理
        public static String GROUPID = "groupId";
        public static String GROUPOWNER = "group_OWNER";
    }

    /**
     * 通讯录
     */
    public static class Addressbook{
        public static String MEMBER = "member";
    }

    public static class UserDetail{
        public static String FROM_AND_STATUS = "from_and_status";
        public static String SELECTED_USER_ID = "select_user_id";
    }

    public static class ImageShow{
        public static String PICTURES = "pictures";
    }

    public static class GroupUpdateRemark{
        public static String USER_NAME = "USER_NAME";
    }


}
