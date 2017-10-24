# Little Quiz Platform

## 项目定位

一个小小的测试平台，此平台目的有二
- 作为 Spring 体系技术栈学习过程的练手项目，结合测试，Git版本管理争取做一次完整、规范的开发流程尝试
- 构建一个答题系统，主要搜集一些笔试面试题目，方便以后复习；同时也准备搜集一些智力测试题，可以测测智商；最后搜集一些脑筋急转弯，测测大脑的灵活性...将来可以更丰富

## 技术选型

- 主体代码选择Spring 体系中 Spring MVC 、Spring Boot、Spring Security、Spring Data。
- 数据库方面想过使用已经比较熟悉的MySQL，但是我想走出舒适区，那还是选择我不熟悉的MongoDB,尝尝鲜,
使用MongoDB作为主体数据库；另外使用redis作为session存储介质，这样利于分布式
（ :-D，虽然没有几个用户，但是我的目的是锻炼技术 ），同时也作为缓存的承载

## 项目架构

### --功能说明

#### ----**前台**

未登录用户在首页，首页轮播的是系统随机调的几道题

登录用户可以按标签、类型选择一套试卷，试卷由试题库生成，试题库由爬虫生成或者手工录入。答题完成后可以查看正确答案以及得分情况

#### ----**后台**

后台主要有用户管理功能，试题管理功能，标签管理功能，试卷管理功能

### --主体代码

- config 配置类
- controller 控制器类
- entity 数据库实体类
- i18n 国际化需要的类
- repository 仓库类
- service 服务类
- util 工具类
- vo 表示层需要的类比如一些表单对象

## 项目流程

- 项目需求分析：搞清楚这个项目的定位、目的是什么，要达到什么效果
- 功能测试 ：进行技术选型，要完成项目需要哪些功能，进行测试，以备后面开发所需
- 项目架构：整个项目前后台业务划分，代码应该如何规划，数据实体对象、url、权限等等的设计
- 具体业务实现：进行编码，要符合代码规范
- 单元测试：可以采取每完成一个模块就进行单元测试或者最后集中进行单元测试，我为了进度选择后者
- 集成测试：所有模块都完成后进行集成测试
- 用户测试：找几个人用用，看看能不能提出一些意见或者找到bug
- 重构优化：加入缓存，代码复用，改善代码整洁度等等