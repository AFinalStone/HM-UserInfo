package com.hm.iou.userinfo.leftmenu;

/**
 * Created by hjy on 2018/5/29.
 */

public enum ModuleType {

    AUTHENTICATION("home_left_menu_authentication", "实名认证"),
    BANK_CARD("home_left_menu_bank_card", "实卡"),
    EMAIL("home_left_menu_email", "邮件"),
    SIGHATURE_LIST("home_left_menu_signature_list", "签名"),
    TASK("home_left_menu_task", "任务"),
    MY_COLLECT("home_left_menu_0006", "我的收藏"),
    MY_SIGNATURE("home_left_menu_0007", "我的签章"),
    MY_CLOUD_SPACE("home_left_menu_0008", "我的空间"),
    MY_WALLET("home_left_menu_0009", "我的钱包"),
    ABOUT_SOFT("home_left_menu_00010", "关于软件");

    private String value;
    private String desc;

    ModuleType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
