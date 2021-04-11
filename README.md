# aws-hackathon-2021

##需求文档

###优惠券/秒杀
页面 - siqi  
注册/登陆 修改  
主页面：领取商品 - 只有一种商品 - 脚本 - 点击跳转 - 登陆 - 领取优惠券 - 跳转商品购买  修改  
领取优惠券 - 只有一种优惠券 - 脚本 新写  

###数据库 -
####rds:
coupon - 一种，用户 （一一对应）  
user - coupon column  
product - 图片地址，描述 - 存储在另外服务器，属性里保存链接  
orders- 结算历史记录  
####后端
springboot 框架管理 Mybatis|JPA  
####中间件
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

### 如何开发

- Working Backwards - 以客户为中心，从客户角度出发进行产品设计
- Start with asking Customer Questions
- Use data to understand and document customer's pain-points
- RD/ Mockup - dynamic
- DevOps
- Agile & Scrum

### 下午讨论

- 前端：semantic ui， 首页（登陆，注册），优惠券领取，促销，个人信息（历史订单，购物车）
- 后端：mybatis | hibernate /springboot
- 中间件：redis/kafka|dynamo db｜rds | amazon translate

- 4.10 - 调研：ml, big data, aws全家桶
- 4.11 - 需求文档

- 创建github - 邀请两个人， 留下开发记录
- pr
- 下次会议4.11下午两点







