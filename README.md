# Android-Weather
安卓实战的期末项目--和风天气系统，前端使用了安卓布局

后端使用了android的java代码，以及后端使用IDEA搭建的服务器

开发环境为：Android Studio + IDEA

技术栈为：SSM + SpringBoot + FastJson + Android + Gradle

需要自己使用ssm搭建后端服务器，用于保存三级城市信息，调用第三方api接口，将第三方的JSON数据回显到android的移动端

该项目在多线程并行时有几处代码可以进行优化

项目难点：android发送网络请求时，需要开设一个子线程，这里使用的OkHttp类向和风天气系统api发送请求，在返回数据时，需要做到同步接收数据。
