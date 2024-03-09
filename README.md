# idea-git-codereview-plugin
idea代码检视插件

## 功能：
代码检视时，可以直接在IDEA中选中代码，填写检视意见，然后以issue进行提交，统一在issue进行查看修改，方便检视意见的跟踪，避免检视完了最后就忘记了问题  
支持github,gitlab,gitee  

以gitee为例：  
1.申请token,选择相关权益，创建issue,仓库信息查询，用户信息查询

<img src="https://gitee.com/bluedarkni/images/raw/master/codereview-guide/token.png"/>  

2.仓库设置token,没有配置token时，使用会提示配置token,选择对应的仓库类型 github,gitlab,gitee  ,api domain 选填，如果api的域名和仓库域名不一样就需要填写。github好像就是不一样的  
<img src="https://gitee.com/bluedarkni/images/raw/master/codereview-guide/use1.png"/>    
<img src="https://gitee.com/bluedarkni/images/raw/master/codereview-guide/token_set.png"/>    
3.配置好了token之后，右键选中需要检视的代码，就可以填写检视意见
<img src="https://gitee.com/bluedarkni/images/raw/master/codereview-guide/issue1.png"/>  

<img src="https://gitee.com/bluedarkni/images/raw/master/codereview-guide/issue2.png"/>  
4.web页面追踪issue信息
<img src="https://gitee.com/bluedarkni/images/raw/master/codereview-guide/issue3.png"/>    

5.github,gitlab 操作同上，申请对应的token即可，api domain 选填，如果仓库api的域名和仓库域名不一样就需要填写。github是不一样的(api.github.com,仓库域名是github.com) ，
私服的话需要找对应人员确认下api的域名，填写正确即可
