#### HM-Pay

集成了支付，银行卡四要素绑定，消费历史记录，次卡充值，选择支付方式

#### 功能介绍
- 支付，次卡消耗
- 银行卡四要素绑定
- 消费历史记录
- 次卡充值
- 选择支付方式

#### 安装

在工程根目录的build.gradle文件里添加如下maven地址：

```gradle
allprojects {
    repositories {
        ...
        maven { url 'file:///Users/syl/.m2/repository/' }
        ...
    }
}
```

在项目模块的buile.gradle文件里面加入如下依赖：

```gradle
    compile 'com.heima.iou:hmpaylocal:1.0.0'
```

外部引用：

```gradle
    compile 'com.heima.iou:hmbasebizlocal:1.0.0'
```

#### 使用说明

支持的路由

| 页面 | 路由url | 备注 |
| ------ | ------ | ------ |
| 个人中心页面 | hmiou://m.54jietiao.com/person/index|  |
| 修改昵称和性别页面 | hmiou://m.54jietiao.com/person/modify_nickname_sex | 修改成功会发送 |
| 修改我的收入页面 | hmiou://m.54jietiao.com/person/modify_income |  |
| 修改密码页面 | hmiou://m.54jietiao.com/person/modify_pwd |  |
| 解绑微信 | hmiou://m.54jietiao.com/person/unbind_weixin|  |
| 修改手机号，校验登陆密码的页面 | hmiou://m.54jietiao.com/person/change_mobile|  |
| 修改手机号页面 | hmiou://m.54jietiao.com/person/change_mobile_complete?pwd=*|  |
| 修改邮箱，校验邮箱的页面 | hmiou://m.54jietiao.com/person/change_email|  |
| 修改邮箱页面 | hmiou://m.54jietiao.com/person/change_email_complete?sn=*|  |
| 关于我们页面 | hmiou://m.54jietiao.com/person/about|  |
| 终止服务页面 | hmiou://m.54jietiao.com/person/terminate_service|  |
| 注销，删除账号 | hmiou://m.54jietiao.com/person/close_account |  |
| 终止服务页面 | hmiou://m.54jietiao.com/person/terminate_service|  |
| 我的资料页面 | hmiou://m.54jietiao.com/person/my_profile|  |
| 用户修改，上传头像页面 | hmiou://m.54jietiao.com/person/user_avatar|  |
| 设置我的收入页面 | hmiou://m.54jietiao.com/person/my_income|  |
| 云存储空间页面 | hmiou://m.54jietiao.com/person/cloud_space|  |

路由文件

```json
{
  "person": [
    {
      "url": "hmiou://m.54jietiao.com/person/index",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.PersonalCenterFragment"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/modify_nickname_sex",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.ModifyNicknameAndSexActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/modify_pwd",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.ModifyPasswordActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/unbind_weixin",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.UnbindWeixinActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/change_mobile",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.ChangeMobileVerifyActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/change_mobile_complete?pwd=*",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.ChangeMobileCompleteActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/change_email",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.ChangeEmailVerifyActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/change_email_complete?sn=*",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.ChangeEmailCompleteActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/about",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.AboutUsActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/terminate_service",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.TerminateServiceActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/close_account",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.CloseAccountActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/my_profile",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.ProfileActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/user_avatar",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.UserHeaderDetailActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/my_income",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.MyIncomeActivity"
    },
    {
      "url": "hmiou://m.54jietiao.com/person/cloud_space",
      "iclass": "",
      "aclass": "com.hm.iou.userinfo.business.view.CloudSpaceActivity"
    }
  ]
  }
```

#### 集成说明

集成本模块之前，需要保证一下模块已经初始化：

HM-BaseBiz初始化(基础业务模块)，HM-Network（网络框架），HM-Router（路由模块），Logger（日志记录）

#### Author

syl