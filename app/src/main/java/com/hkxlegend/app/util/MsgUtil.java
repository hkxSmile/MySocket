package com.hkxlegend.app.util;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * 模拟词库
 *
 * @author huangkexiang
 * @since 17/3/3
 */

public class MsgUtil {

    public static String returnMsg(String topic) {

        if (TextUtils.isEmpty(topic)) {
            return null;
        }

        String msg = "抱歉,小p正在学习成长,暂时不懂您说的什么";

        boolean isEnglish = Pattern.compile("[a-zA-Z]").matcher(topic).find();
        if (isEnglish) {
            msg = "你好我是中国小p,暂时不懂英文,O(∩_∩)O谢谢";
        }

        if (topic.contains("好")) {
            msg = "你好我好大家好 嘿嘿";
        } else if (topic.contains("喜欢")) {
            msg = "喜欢你嗯呢";
        } else if (topic.contains("吃")) {
            msg = "吃吃吃 大吃货哈哈";
        } else if (topic.contains("什么")) {
            msg = "什么什么?没干什么 - -";
        } else if (topic.contains("想")) {
            msg = "想啊想啊";
        } else if (topic.contains("哈")) {
            msg = "笑啥捏~~";
        } else if (topic.contains("啊")) {
            msg = "啊啊啊啊啊~~ O(∩_∩)O ";
        } else if (topic.contains("哪里")) {
            msg = "我在我的世界里 你在哪里";
        } else if (topic.contains("嘿")) {
            msg = "笑啥捏~~";
        } else if (topic.contains("?") || topic.contains("啥")) {
            msg = "??";
        } else if (topic.contains("谁")) {
            msg = "小p 小p";
        } else if (topic.contains("hi")) {
            msg = "hihi";
        } else if (topic.contains("呵")) {
            msg = "呀呀?";
        } else if (topic.contains("你是")) {
            msg = "我叫小p 嘿嘿";
        }

        switch (topic) {
            case "你好":
                msg = "您好,在的";
                break;
            case "你叫什么":
                msg = "我叫小p 嘿嘿";
                break;
            case "再见":
                msg = "再见,期待下次和你聊天";
                break;
            case "你是谁":
                msg = "我叫小p 嘿嘿";
                break;
        }

        return msg;
    }
}
