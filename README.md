# Suiteki —— 一个可以获取小米手环的AuthKey的类库

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
>>
>分析并得出**AuthKey**
>>

#### 日志文件的位置
```
"/storage/emulated/0/Android/data/com.mi.health/files/log/XiaomiFit.device.log"//小米运动健康
"/storage/emulated/0/Android/data/com.xiaomi.wearable/files/log/Wearable.log"//小米穿戴,小米穿戴的Log中没有Model,不能获取所属型号
```

## 引进方法
>把**Suiteki.jar**放置到**Project**下的**libs**文件夹内
>>
>并在**build.gradle**中添加下面的**代码**
>>
>这个项目用了OkHttp

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
    Suiteki.getAuthKey()//里面放入Log的内容(类型为String)返回数据类型为String
    Suiteki.getAuthKeyList()//里面也是放入Log内容(类型为String)返回数据类型为String[]
    Suiteki.getModel()//先放入AuthKey类型为String)再放入Log内容(类型为String)返回数据类型为String
    Suiteki.isMoreAuthkey()//里面放入Log内容, 返回boolean类型
    Suiteki.getKey()//需要提交code
    Suiteki.getModelName()//放入型号，getModel返回的就是型号
```
>第一个函数可以用在**小米运动健康**和**小米穿戴**上
>>
>第二个函数**只能**用在**小米运动健康**上
>>
>第三个函数也**只能**用在**小米运动健康**上
>>

## 编译方法
>先进行"Make Project"
>>
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
>>
>运行该命令后你可以在**/Suiteki/build/libs/**中找到 **Suieki.jar**了


## 获取code
>链接:
>>
```
https://account.xiaomi.com/fe/service/oauth2/authorize?skip_confirm=false&client_id=2882303761517383915&pt=0&scope=1+6000+16001+20000&redirect_uri=https%3A%2F%2Fhm.xiaomi.com%2Fwatch.do&_locale=zh_CN&response_type=code
```
>>
>通过这个链接登录小米账号以后
>>
>会返回一个链接
>>
```
https://hm.xiaomi.com/watch.do?code=C4_434BB7ADBA171F615DD9956881E21E7A
```
>>
>"code="后面的就是code
>>
>往getKey()里面放入code就会返回一个String[]
>>
>注意返回String[]里面一个数据里面包含 AuthKey 和 Mac地址,注意分开
