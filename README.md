# Suiteki —— 一个可以获取小米手环的AuthKey的类库

>[引进方法](#引进方法)
>>
>[食用方法](#食用方法)
>>
>[编译方法](#编译方法)

## 引进方法
>把**Suiteki.jar**放置到**Project**下的**libs**文件夹内
>>
>并在**build.gradle**中添加下面的**代码**

``` java
dependencies {
    implementation files('libs\\Suiteki.jar')   
}
```
>点击**Sync Now**,并等待一会
>在java源文件中引进这个库即可使用,下为代码

``` java
    import ml.sky233.Suiteki;
```

## 食用方法
>有三个函数
``` java
    Suiteki.getAuthKey()//里面放入Log的内容(类型为String)返回数据类型为String
    Suiteki.getAuthKeyList()//里面也是放入Log内容(类型为String)返回数据类型为String[]
    Suiteki.getModel()//先放入AuthKey类型为String)再放入Log内容(类型为String)返回数据类型为String
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
