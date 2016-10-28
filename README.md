# AccessibilityServiceSample

在网上看到Android的一些插件的开发，其中用到最主要的一个类就是AccessibilityService类。

今天就来学习使用该类。

1、首先要创建一个服务类继承AccessibilityService，其中，需要实现两个方法：
onAccessibilityEvent(AccessibilityEvent accessibilityEvent)
onInterrupt()
前者用来监听手机当前窗口状态改变  比如 Activity跳转，内容变化，按钮点击等事件
后者，辅助服务被关闭时调用该方法。

2、在AndroidManifest.xml文件中配置该服务

<service android:name=".accessibility.MySetService"
    android:enabled="true"
    android:label="@string/app_name"
    android:permission="android.permission.BIND_ACCESSIBILITY_SERVICE">
    <intent-filter>
        <action android:name = "android.accessibilityservice.AccessibilityService"/>
    </intent-filter>
    <meta-data
        android:name="android.accessibilityservice"
        android:resource="@xml/myaccessibilityxml"/>
</service>

其中lable对应到手机设置里面辅助服务的名称

配置<intent-filter>,其name为固定的:
android.accessibilityservice.AccessibilityService
声明BIND_ACCESSIBILITY_SERVICE权限,以便系统能够绑定该服务(4.0版本后要求)

所以上面这段代码是固定的。

3、然后在res下面创建xml文件夹，创建myaccessibilityxml文件

<?xml version="1.0" encoding="utf-8"?>
<accessibility-service
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:accessibilityEventTypes="typeWindowStateChanged"
    android:accessibilityFeedbackType="feedbackGeneric"
    android:accessibilityFlags="flagDefault"
    android:canRetrieveWindowContent="true"
    android:notificationTimeout = "100"
    android:description="@string/access"
    />

这个文件没法实现自动完成，需要一个一个敲出来，蛋疼：

accessibilityEventTypes:表示该服务对界面中的哪些变化感兴趣,即哪些事件通知,比如窗口打开,滑动,焦点变化,长按等.具体的值可以在AccessibilityEvent类中查到,如typeAllMask表示接受所有的事件通知.
accessibilityFeedbackType:表示反馈方式,比如是语音播放,还是震动
canRetrieveWindowContent:表示该服务能否访问活动窗口中的内容.也就是如果你希望在服务中获取窗体内容的化,则需要设置其值为true.
notificationTimeout:接受事件的时间间隔,通常将其设置为100即可.
packageNames:表示对该服务是用来监听哪个包的产生的事件

这属于静态配置服务，还有一种动态配置的方法，适合4.0以下，估计现在也没有了。

通过setServiceInfo(AccessibilityServiceInfo info)为其配置信息,除此之外,通过该方法可以在运行期间动态修改服务配置.需要注意,该方法只能用来配置动态属性:eventTypes,feedbackType,flags,notificaionTimeout及packageNames.

通常是在onServiceConnected()进行配置,如下代码:

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
    */
}

4、我在这个例子中，只是打印监听手机当前的类型

所以只是实现了

@Override
public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
    Toast.makeText(MySetService.this,accessibilityEvent.getEventType()+"类		型",Toast.LENGTH_SHORT).show();
}

5、然后就是MainActivity中的写法：

findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(new AccessibilityManager.AccessibilityStateChangeListener() {
            @Override
            public void onAccessibilityStateChanged(boolean b) {
                if(b){
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,"找到我的设置 打开它",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                    Toast.makeText(MainActivity.this,"找到我的设置 打开它",Toast.LENGTH_LONG).show();
                }
            }
        });

        if(accessibilityManager.isEnabled()){

        }else{
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(MainActivity.this,"找到我的设置 打开它",Toast.LENGTH_LONG).show();
        }
    }
});

AccessibilityManager是系统级别的服务,用来管理AccessibilityService服务,比如分发事件,查询系统中服务的状态等等。
