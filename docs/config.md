全局配置推荐在Application中设置
```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initNet("http://182.92.97.186/") {
            converter(JsonConvert()) // 转换器
            cacheEnabled() // 开启缓存
        }
    }
}
```

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        initNet("http://182.92.97.186/") {
            converter(JsonConvert()) // 转换器
            cacheEnabled() // 开启缓存
        }
    }
}
```


initNet 作用域内可选函数

<img src="https://i.imgur.com/k0aoQoK.png" width="60%"/>

## 动态配置

单例对象[NetConfig](api/net/com.drake.net/-net-config/index.md)存储了初始化时的配置, 可以后期动态读写.

例如Retrofit的`baseUrl`功能就可以直接修改`NetConfig.host`
