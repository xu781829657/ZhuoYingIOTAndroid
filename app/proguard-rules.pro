# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\liu\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-optimizationpasses 5
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-dontpreverify
#-verbose
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#
##显示行号
#-keepattributes SourceFile,LineNumberTable
#
##### Android默认四大组件/基本类-组件/数据模型混淆配置###
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class com.android.vending.licensing.ILicensingService
#
# # 保持 native 方法不被混淆
#-keepclasseswithmembernames class * {                                           # 保持 native 方法不被混淆
#    native <methods>;
#}
#
## 保持自定义控件类不被混淆，指定格式的构造方法不去混淆
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
##保持自定义组件不被混淆
#-keep public class * extends android.view.View {
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#    public void set*(...);
#}
#
## 保持指定规则的方法不被混淆（Android layout 布局文件中为控件配置的onClick方法不能混淆）
#-keepclassmembers class * extends android.app.Activity {
#    public void *(android.view.View);
#}
#
# # 保持 Parcelable 不被混淆
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
#
##如果有引用v4包可以添加下面这行
#-keep class android.support.v4.** { *; }
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.app.Fragment
#
#
##如果引用了v4或者v7包，可以忽略警告，因为用不到android.support
#-dontwarn android.support.**
#
##保持 Serializable 不被混淆
#-keepnames class * implements java.io.Serializable
#
#
##保持 Serializable 不被混淆并且enum 类也不被混淆
#-keepclassmembers class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private static final java.io.ObjectStreamField[] serialPersistentFields;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
##保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
#-keepclassmembers enum * {
#   public static **[] values();
#   public static ** valueOf(java.lang.String);
# }
#
#
##不混淆资源类
#-keepclassmembers class **.R$* {
#    public static <fields>;
#}
#
## 过滤泛型（不写可能会出现类型转换错误，一般情况把这个加上就是了）
#-keepattributes Signature
##假如项目中有用到注解，应加入这行配置
#-keepattributes *Annotation*
#
#
#
## 保护WebView对HTML页面的API不被混淆
#-keep class **.Webview2JsInterface { *; }
#
## 如果你的项目中用到了webview的复杂操作 ，最好加入
#-keepclassmembers class * extends android.webkit.WebViewClient {
#     public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
#     public boolean *(android.webkit.WebView,java.lang.String);
#}
#
## 如果你的项目中用到了webview的复杂操作 ，最好加入
#-keepclassmembers class * extends android.webkit.WebChromeClient {
#     public void *(android.webkit.WebView,java.lang.String);
#}
#
####第三方jar混淆保留配置###
##achartengine-1.1.0.jar#
#-dontwarn org.achartengine.**
#-keep class org.achartengine.** {*;}
#
##百度地图#
#-dontwarn com.baidu.**
#-keep class com.baidu.** {*;}
#-dontwarn vi.com.gdi.bgl.android.java.**
#-keep class vi.com.gdi.bgl.android.java.** {*;}
#
##commons-codec-1.6.jar#
##commons-logging-1.1.1.jar#
#-dontwarn org.apache.commons.**
#-keep class org.apache.commons.** { *; }
#
#
##fluent-hc-4.2.5.jar#
#-dontwarn org.apache.http.client.fluent.**
#-keep class org.apache.http.client.fluent.** { *; }
#
##GifView.jar#
#-dontwarn com.ant.liao.**
#-keep class com.ant.liao.** { *; }
#
##谷歌gson
#-dontwarn com.google.gson.**
#-keep class com.google.gson.** {*;}
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
#-keep class com.google.** {
#    <fields>;
#    <methods>;
#}
##解析到具体的javabean
#-keep class com.ouzhongiot.ozapp.Model.** { *; }
#-keep class com.ouzhongiot.ozapp.airclean.Aircleanstate { *; }
#-keep class com.ouzhongiot.ozapp.airclean.Aircleanlishishijian { *; }
#-keep class com.ouzhongiot.ozapp.airclean.Aircleanshineikongqi { *; }
#-keep class com.ouzhongiot.ozapp.others.JacksonUtil { *; }
#-keep class com.ouzhongiot.ozapp.airclean.AircleanB1shuju { *; }
#-keep class com.ouzhongiot.ozapp.dryer.DryerData { *; }
#-keep class com.ouzhongiot.ozapp.dryer.DryerShiyongshijian { *; }
#-keep class com.ouzhongiot.ozapp.dryer.LishijiluBean { *; }
#
##hellocharts-library-1.5.8.jar#
#-dontwarn lecho.lib.hellocharts.**
#-keep class lecho.lib.hellocharts.** { *; }
#
#-dontwarn org.apache.http.**
#-keep class org.apache.http.** { *; }
#
##httpmime-4.2.5.jar#
#-dontwarn org.apache.http.entity.mime.**
#-keep class org.apache.http.entity.mine.** { *; }
#
#
##jackson相关
#-dontwarn com.fasterxml.jackson.** #告诉编译器fastjson打包过程中不要提示警告
#-keep class com.fasterxml.jackson.** { *; } #jackson包下的所有类不要混淆，包括类里面的方法
#-keepattributes Signature #这行一定要加上，否则你的object中含有其他对象的字段的时候会抛出ClassCastException
#-keepattributes *Annotation*
#-keep class sun.misc.Unsafe { *; }
#
##json_simple-1.1.jar#
#-dontwarn org.json.simple.**
#-keep class org.json.simple.** { *; }
#
##libammsdk.jar#
#-dontwarn com.tencent.**
#-keep class com.tencent.** { *; }
#
#
##zxing二维码相关#
#-dontwarn com.google.zxing.**
#-keep class com.google.zxing.** {*;}
#
##个推getui#
#-dontwarn com.igexin.**
#-keep class com.igexin.** { *; }
#-keep class org.json.** { *; }

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}