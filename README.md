# Suiteki —— 一个可以获取小米手环的AuthKey的类库
![](https://sky233.ml/images/app.png)
------

>[引进方法](#引进方法)
>>
>[食用方法](#食用方法)
>>
>[获取code](#获取code)


## 引进方法
``` Java
dependencies {
    ...
    implementation('ml.sky233.suiteki:Suiteki:1.0.3')   
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
}
```

``` java
    import static ml.sky233.Suiteki.*;
```

## 食用方法
|            方法            | 返回数据类型 |                               返回数据内容                               |
| :------------------------: | :----------: | :----------------------------------------------------------------------: |
|   getAuthKey(String log)   |    String    |                    "e55d3c32ad0ecaa040192f226467dc9a"                    |
| getAuthKeyList(String log) |   String[]   |   [e55d3c32ad0ecaa040192f226467dc9a,77b1592dd8ef60e1296420ed3d133d8e]    |
|    getModel(String log)    |   String[]   |                          "hmpace.motion.v6nfc"                           |
| isMoreAuthkey(String log)  |   boolean    |                                   true                                   |
| getHuamiToken(String code) |   String[]   | [e55d3c32ad0ecaa040192f226467dc9a\nFA:F0:84:6E:E0:87\nFA:F0:84:6E:E0:87] |
| getModelName(String name)  |    String    |                            "小米手环6 NFC版"                             |

## 获取code
>小米账号登录ZeppLife的链接:
>>https://account.xiaomi.com/fe/service/oauth2/authorize?skip_confirm=false&client_id=2882303761517383915&pt=0&scope=1+6000+16001+20000&redirect_uri=https%3A%2F%2Fhm.xiaomi.com%2Fwatch.do&_locale=zh_CN&response_type=code
>
>登录小米账号后会重定向一个类似下面的链接:
>
>>https://hm.xiaomi.com/watch.do?code=C4_434BB7ADBA171F615DD9956881E21E7A
>
>其中Code为
>
>>C4_434BB7ADBA171F615DD9956881E21E7A

