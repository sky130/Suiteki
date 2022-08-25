# Suiteki —— 一个可以获取小米手环的AuthKey的类库
![](https://sky233.ml/images/app.png)
---
>[原理](#原理)
>>
>[引进方法](#引进方法)
>>
>[食用方法](#食用方法)
>>
>[编译方法](#编译方法)
>>
>[获取code](#获取code)

## 原理
>通过读取**小米运动健康**和**小米穿戴**的Log日志文件

#### 日志文件的位置
```
"/storage/emulated/0/Android/data/com.mi.health/files/log/XiaomiFit.device.log"
"/storage/emulated/0/Android/data/com.xiaomi.wearable/files/log/Wearable.log"
```

## 引进方法
>把**Suiteki.jar**放置到**Project**下的**libs**文件夹内
>>
>并在**build.gradle**中添加下面的**代码**
>>
>这个项目使用了OkHttp,需要添加类库

``` java
dependencies {
    ...
    implementation files('libs\\Suiteki.jar')   
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
}
```
>点击**Sync Now**,并等待一会
>在java源文件中引进这个库即可使用,下为代码

``` java
    import ml.sky233.Suiteki;
```

## 食用方法
>有个函数
``` java
    Suiteki.getAuthKey(String log)
    Suiteki.getAuthKeyList(String log)
    Suiteki.getModel(String log)
    Suiteki.isMoreAuthkey(String log)
    Suiteki.getHuamiToken(String code)
    Suiteki.getModelName(String name)
```
## 编译方法
>我在**build.gradle**下写了一段任务命令
>>
```
task makeJar(type: Jar) {
    baseName 'Suiteki'
    from('build/intermediates/javac/debug/classes/')
    exclude('test/','BuildConfig.class','R.class')
    exclude{ it.name.startsWith('R$') }
}
```
>请先编译一次项目后运行
>>
>运行该命令后你可以在**/Suiteki/build/libs/**中找到 **Suieki.jar**了


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

