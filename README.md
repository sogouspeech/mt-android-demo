# 搜狗知音在线翻译sdk示例demo
## 1、获取token

传入appId、appKey、uuid(设备唯一标志)构建ZhiyinInitInfo对象zhiyinInitInfo；在子线程中调用SogouTranslate.init(this.getApplicationContext(), zhiyinInitInfo)获取token，init返回值代表获取token成功与否。


## 2、进行翻译
SogouTranslate.init(this.getApplicationContext(), zhiyinInitInfo)返回true后，构建SogouTranslate对象sogouTranslate，调用sogouTranslate.translate(requestConfig)进行翻译。requestConfig要传入原语言文本、原语言语种、目标语言语种，如下所示：
```
 public TranslateRequestConfig(String content, String fromCode, String destCode) {
        this.content = content;
        this.fromCode = fromCode;
        this.destCode = destCode;
 }
```
语种列表如下：
```
public static final String CHINESE = "zh-cmn-Hans";
    public static final String ENGLISH = "en";
    public static final String JAPANESE = "ja";
    public static final String KOREAN = "ko";
    public static final String FRENCH = "fr";
    public static final String SPANISH = "es";
    public static final String RUSSIAN = "ru";
    public static final String GERMAN = "de";
```
注意每次调用sogouTranslate.translate传入的文本长度不能大于2048字节。
## 3、展示结果
```
public interface TranslateListener {
    //翻译结果回调
    void onNext(TranslateTextResponse value);
    //翻译过程中出现错误回调
    void onError(Throwable t);
    //翻译过程结束回调
    void onCompleted();
}
```
## 4、清理工作
在onCompleted()或者onError(Throwable t)中调用sogouTranslate.release();
