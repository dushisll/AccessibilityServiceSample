package com.dushisll.accessibilityservicesample.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

/**
 * Created by we on 2016/10/28.
 */

//首先需要实现一个继承了AccessibilityService的服务，在AndroidManifest.xml中注册它
public class MySetService extends AccessibilityService {

    //辅助服务被打开后  执行此方法
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        /*
        动态修改服务配置，详见myaccessibilityxml文件
        AccessibilityServiceInfo serviceInfo = new AccessibilityServiceInfo();
        serviceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        serviceInfo.notificationTimeout = 100;
        setServiceInfo(serviceInfo);

        accessibilityEventTypes:表示该服务对界面中的哪些变化感兴趣,即哪些事件通知,比如窗口打开,滑动,焦点变化,长按等.具体的值可以在AccessibilityEvent类中查到,如typeAllMask表示接受所有的事件通知.
        accessibilityFeedbackType:表示反馈方式,比如是语音播放,还是震动
        canRetrieveWindowContent:表示该服务能否访问活动窗口中的内容.也就是如果你希望在服务中获取窗体内容的化,则需要设置其值为true.
        notificationTimeout:接受事件的时间间隔,通常将其设置为100即可.
        packageNames:表示对该服务是用来监听哪个包的产生的事件

        */
    }

    //监听手机当前窗口状态改变  比如 Activity跳转，内容变化，按钮点击等事件
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Toast.makeText(MySetService.this,accessibilityEvent.getEventType()+"类型",Toast.LENGTH_SHORT).show();
    }

    //辅助服务被关闭  执行此方法
    @Override
    public void onInterrupt() {

    }
}
