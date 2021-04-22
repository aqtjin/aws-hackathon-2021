# aws-hackathon-2021

## Demo 
 - Link:http://miaosha/resources/listitem.html
 - Login :111
 - Password: 111

## 项目背景
为了让更多人了解并培养使用亚马逊电子书的习惯，我们设计并实现了一个模拟亚马逊的电子书促销系统。
 - 前提假设：
    1. 假定所依赖的其他系统完好，使用模拟数据，比如库存系统。所模拟的数据库包括user_info, user password, items_info, items_stock, order_info, promo_info, sequence_info。
    2. 用户信息和密码表分开用以保护用户信息安全，性别输入为0/1的形式，商品信息和库存分开用来保护商品信息安全，sequence_info流水号用于解决事务createorder下单回滚的情况。
    3. 用户既可以是买家下单买书，也可以是卖家上架书籍售卖，买书或者卖书均需要登陆（基于token实现分布式会话）。
    4. 优惠书籍和原价书籍分区展示。优惠券类型设置为“折扣”，反映在“抢购折扣”页面，此页面展示电子书打折后的价格，详见下图1.<p><img src="https://lh6.googleusercontent.com/c31fLkLiC0AbPUNI7HYZcsFOJ-EDgmsi4qwENDERR_JXbRWX0BTjAADIRaFzpYuzHNSBU6POeA0Um3FXeCbwuWXVa0IAtYhc7QbbyHzUJ9BJcGJEmq7D3MkUa3vzJUCE-YYUWoC4" height='350px'/>（图1）</p>
    5. 全部书籍(All Books)区域包含即将开始秒杀，秒杀已经结束和正在秒杀阶段的全部书籍，点击buy按钮可查看活动进展。若秒杀活动还未开始，页面显示活动开始时间和倒计时，折扣价格和原价，下单按钮被disable直到秒杀活动开始。<p><img src="https://lh4.googleusercontent.com/3_zuir9Y5Mk0h6YYwnWkhoVsWsrvlohB21ib9q6HBl-n-F-nA1nJrqVW55hesNGQ32uz5b1rf0LODACCPcLthuh7hEYgMJ-Cfxr2Lb_P4EoF6a2bWERh5fhR9yYXYFm75xlNJjP4" height='350px'/>(图2）</p>
    6. 考虑到是电子书，每次下单，订购数目默认是1。
    7. 秒杀系统的主页无需登录即可浏览折扣书籍和全部在售书籍，当点击下单时若用户未登陆则需要注册/登陆后下单。

## 项目架构
<p><img src="https://lh4.googleusercontent.com/DMGgOsoPa7qcSVXJYCqrBJUrgCZip8zrwLaYHvxAxJfXoL8UE0fZ4Df-1dD87_q7H0qZYGGWuZ7dg6vjh6zolorcuZ26Ydxg-e49ZhPraHnoB4OjzI-rneZISySlmRE1wf9udPrU" height='500px'/></p>

<p><img src="https://lh6.googleusercontent.com/JKUjlibeMeKDInI-oV0bqY54_UVshWOdfoD9T0hgWKD2K5gcfDCEt-oyDoJXSfS8c3zswgSNpWJsBW9QeQGQAX6pNXAzyh42Jn0T01KJHrtFvKEg8TStSK8fDsgI6n9n6XZpLF_a" height='350px'/></p>

### 数据模型
<p><img src="https://lh4.googleusercontent.com/zcax9S87bG2p3XPIXJOBWdD9F8mKAw7OK5vtHCeUxod6KNtVqi3pUBLKJIbSJGbG4W7WKYehM6IpHyJYIxlbqCjH7Law06m5z72macjRMOiDZhPwh9WWOU4fvzNMbIlgI1VdBIB253c" height='500px'/></p>

### DAO/Service/Controller模型
<p><img src="https://lh4.googleusercontent.com/ENG2BFvfJ1X7WAB_Ohfm4Gxq3G21u9LiHEUiS1k4A4_JBb127HDqv4V4hrgXZGkmICtCqRDXl2MwlAlN_3VbOHieMr-4YFc0fJwL8QcknXAPUnpt8tWCYCCYvdTx4amUdmubBFM9Yz4" height='500px'/></p>


## 项目细节
 - 使用配置mybatis自动生成器来生成文件，在mybatis-generator.xml配置文件中在对应生成表类名配置中加入
 `enableCountByExample="false"enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false"selectByExampleQueryId="false"`
 避免生成不常用方法。
 - 前端ajax调用接口获取验证码 html/getotp.html，出现跨域请求问题 解决方法：`@CrossOrigin(origins = {"*"}, allowCredentials = "true") allowedHeaders`允许前端将 token 放入 header 做 session 共享的跨域请求。allowCredentials 授信后，需前端也设置 xhfFields 授信才能实现跨域 session 共享。 `xhrFields: {withCredentials: true}`
 - 统一前端返回格式`CommonReturnType {status: xx ,object:xx}`dataobject -> 与数据库对应的映射对象; model -> 用于业务逻辑service的领域模型对象; viewobject -> 用于前端交互的模型对象。
 - 使用 hibernate-validator 通过注解来完成模型参数校验。
 - insertSelective 中设置 `keyProperty="id" useGeneratedKeys="true"` 使得插入完后的DO生成自增id。insertSelective与insert区别：insertSelective对应的sql语句加入了NULL校验，即只会插入数据不为null的字段值（null的字段依赖于数据库字段默认值）insert则会插入所有字段，会插入null。
 - 数据库设计规范，设计时字段要设置为not null，并设置默认值，避免唯一索引在null情况下失效等类似场景。
 - 使用聚合模型在itemModel加入PromoModel. PromoModel，若不为空表示其有未结束的秒杀活动；在orderModel中加入promoId，若不为空，则以秒杀方式下单。

## 部署流程
### 数据库部署
 - 使用**AWS RDS**工具直接方便部署MySQL数据库。图片存放在**AWS S3**中，图片链接存放在RDS的商品信息表中。
### 水平扩展
 - **Nginx部署前端静态资源**。用户通过nginx/html/resources访问前端静态页面。而Ajax请求则会通过Nginx反向代理到三台不同的应用服务器。
 - **三台EC2部署后端项目打成的jar包**。使用./deploy.sh &即可在后台启动，使用tail -f nohup.out即可查看项目启动、运行的信息。
 - **基于Token实现分布式会话**。用UUID生成登录凭证token，然后将生成的token作为KEY，UserModel作为VALUE存入到Redis服务器。
### 三级缓存
#### 一级缓存：
OpenResty对Nginx进行了扩展，集成了lua开发环境。使用OpenResty的Shared dict，把压力转移到了Nginx服务器，后面两个Tomcat服务器压力减小。同时减少了与后面两个Tomcat服务器、Redis服务器和数据库服务器的网络I/O，当网络I/O成为瓶颈时，Shared dict提供了优化的方法。
#### 二级缓存：
在Redis的前面再添加一层“本地热点”缓存，使用了Google的Guava Cache方案利用本地JVM的内存存放多次查询的数据。Guava Cache除了线程安全外，还可以控制超时时间，提供淘汰机制。
#### 三级缓存：
修改ItemController.getItem接口，先从Redis服务器获取，若没有，则从数据库查询并存到Redis服务。有的话直接用。
## 接口规范
### **登陆**
POST /login
#### **请求体**

参数名|类型|描述
--|:--:|--:
telephone|string|用户名
password|string|密码

#### **请求示例**
```
{  
    "telephone": "robot",  
    "password": "robot"
}
```
#### **响应示例**
```
200 OK {   
    "access_token": "xxx"
    }
```
#### **异常示例**
用户名不存在或者密码错误：
403 Forbidden
```
{  
    "code": "USER_AUTH_FAIL",  
    "message": "用户名或密码错误"
}
```

<p><img src="https://lh4.googleusercontent.com/SwYx1TNm7a3Jw2z7Piq7n9MsP2EtcJazUuYzIKa-0cjHAsIUivVIjSu6nwUD5Xs3iSX_9Ho7znmZMiHiAnwSl9pqKR6Cj3j5bNeV6obQ89kKFf4yTFR784Wgu8EgaS-jFBZYINAq" height='350px'/></p>

### **注册**
POST /register
#### **请求体**

参数名|类型|描述
--|:--:|--:
telephone|string|用户名
password|string|密码
code|string|验证码
gender|int|性别
name|string|姓名 

#### **请求示例**
```
POST /register
{  
    "telephone": "robot",	
    “otpCode”:”12345”,	
    “name”:”robot”,	
    “gender”:1,  
    "password": "robot"
}
```
#### **响应示例**
`200 OK{   null}`
#### **异常示例**
短信验证码不匹配：
```
403 Forbidden
{  
    "code": "USER_AUTH_FAIL",  
    "message": "Registration Failed! 用户名或密码错误"
 }
 ```
 
<p><img src="https://lh4.googleusercontent.com/IbGdQv6r4Ltx_IiEApLJdYoYizAC_MtW0doBsen02COSjRlWU8nZhP8uqM-mMgQVCHun6I4T714ANbrbEMZZDrhXbW3YXUmcFUt2Ea4_cdqfi_4tcNrzeKLYAY8n0FXFU6QOds-P" height='350px'/></p>


### **商品列表**
get /listitem
#### **请求示例**
```
get/listitem
```
#### **响应示例**
```
200 OK
[  
    {    
        description: "by Erin French",
        id: 8,
        imgUrl: "https://miaoshas3.s3.amazonaws.com/item_img/id_8.jpg",
        price: 69.99,
        promoId: 9,
        promoPrice: 12.99,
        promoStatus: 1,
        sales: 897,
        startDate: "2021-04-23 10:03:00",
        stock: 454,
        title: "FINDING FREEDOM"
      }
      {
        description: "by Judy Batalion",
        id: 11,
        imgUrl: "https://miaoshas3.s3.amazonaws.com/item_img/id_11.jpg",
        price: 99.87,
        promoId: null,
        promoPrice: null,
        promoStatus: 0,
        sales: 6580,
        startDate: null,
        stock: 52,
        title: "THE LIGHT OF DAYS"
      }
 ]
```

 <p><img src="https://lh4.googleusercontent.com/lnnrCuzCyGs_bcjhKpDOh8aDSk-PfBYRCcuHC9DJT6Tm2AqrAQ-y2KCjkDfyyUNtlyKIDtFhdP21DKCzFMPxZS_C7mTaM3qM_rN5SN4a40XIuoZMTGLRHqTECHUZOKDq56Y2xNuB" height='350px'/></p>
 

### **商品详情**
GET /getitem
#### **请求示例：**
```
GET /item/get?id=1
```
#### **响应示例**
```
{
    description: "by Cynthia D'Aprix Sweeney",
    id: 6,
    imgUrl: "https://miaoshas3.s3.amazonaws.com/item_img/id_6.jpg",
    price: 19.88,
    promoId: 7,
    promoPrice: 10.99,
    promoStatus: 2,
    sales: 600,
    startDate: "2021-04-21 10:53:00",
    stock: 7324,
    title: "GOOD COMPANY"
}
```

 <p><img src="https://lh5.googleusercontent.com/1jeuxj-px8cY8VofIlg_xWkmhNs-thEtLwi2BKlR5dUumAAHl0Vu5xbDfJ3QVsB7af9wZw6aFUmN5PTwQ-NHH65MwKqxLU_InEavxK-1P7W_Nea3uR6elqjED6xFCI1ZZu6ifidy" height='350px'/></p>
 

### **下单**
POST /createorder
#### **请求体**

参数名|类型|描述
--|:--:|--:
item_id|string|商品id
amount|int|下单数量（1）
promoId|string|促销活动id

#### **请求示例**
```
POST "/order/createorder?token="+token,
{ 
    "itemId":”1”, 
    "amount":1, 
    "promoId":”2”
}
```
#### **响应示例**
```
200 OK
{  
    null
}
```



## 需求文档

### 优惠券/秒杀
页面 - siqi  
注册/登陆 修改  
主页面：领取商品 - 只有一种商品 - 脚本 - 点击跳转 - 登陆 - 领取优惠券 - 跳转商品购买  修改  
领取优惠券 - 只有一种优惠券 - 脚本 新写  

### 数据库 -
#### rds:
coupon - 一种，用户 （一一对应）  
user - coupon column  
product - 图片地址，描述 - 存储在另外服务器，属性里保存链接  
orders- 结算历史记录  
#### 后端
springboot 框架管理 Mybatis|JPA  
#### 中间件
redis - 管理队列｜数据缓存


## Training Session Notes - 4.10

- 4.10 培训
- 4.10-4.22 做题时间，提交作业，完成比赛： 队伍基本信息，code，上传到GitHub，发送项目链接（公开权限），短视频介绍作品理念（模拟第三方卖家，5分钟）用于评审，ppt（决赛展示，详细介绍
- 4.23-4.24 评审
- 4.25上午 决赛

### 比赛题目

- 1/3 online knowledge sharing -- 清楚表达idea，最好能有feature，demo给大家（实时性）
- 2/3 mocked promotion system for amazon Kindle store
    - 可以注重实现也可以有创意
    - 不需要Kindle api
    - 需要简单demo
    - 秒杀系统：高并发 - load balancer, mq
    - 要有前端，后端是加分项（推荐系统）
- 3/3 全球购后台管理系统
    - 翻译：商品名称，介绍，价格本地化，税费（预留字段，不考察）
    - 商品属性：图片，国家，库存，卖家id
    - 实现接口：添加商品，修改商品，查询，批量操作，订单管理，畅销前三名
    - 基本功能希望都能实现，跨国卖家其他需求实现是加分项







