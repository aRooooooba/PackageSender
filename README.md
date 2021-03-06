# PackageSender
## Android, send my own packages

![icon](https://package-sender-android-app-1258441719.cos.ap-beijing.myqcloud.com/logo.png)

*大家好，欢迎来到aRooba的第一个安卓项目！*

写这个项目的初衷是为了锻炼自己的Android和java技术，而灵感源于校园网登录网关的操作。在我们学校，或者说在大部分学校，连接校园网都需要登录网关才能访问外网，就每次都需要填写用户名密码之类的信息。虽然现在的浏览器都能记住账户，使得登录不那么麻烦，但是我想要更进一步简化这个操作，于是做出了这个软件。

软件用法：

1. 打开app，发现是一片空白。点击右下角加号，可以新建脚本。

2. 输入脚本信息，长按主界面的那条脚本，就可以进入这个脚本的请求列表。当然现在依然是一片空白。标题栏的“编辑”和“删除”都是针对脚本的操作。

3. 点击右下角加号，可以新建请求。这里先重点说明一下各个字段的含义以及格式：

* 目标网址：即请求的目标网址。记得加上http/https协议头。

* 请求类型：暂时只有GET和POST两种，对于登录网关来说足够了。

* 参数：有些网址后面会有参数，在url的"?"之后，格式为"x=1&y=2&z=3..."。

* 请求头：有些服务器会后台检查你的请求头，比如ua，connection等，不过一般不需要。格式同上。

* 请求体：一般放置用户名、密码等相关信息，格式同上。

4. 点击保存即可保存成功。

**这里请求中的各个参数都要严格按照网关登录的标准来。推荐在电脑上下载火狐浏览器，打开网关登录页面，右键这个页面，选择**`“查看元素”`**，会弹出一个开发者工具。点击**`“网络”`**，可以看到网站发送请求的信息。因为从打开开发者工具到现在你没有进行什么操作，所以现在这个页面一般为空。这时你填写好你的信息，点击登录，会看见开发者工具中最上面有一个请求，一般都是POST请求。点击它，会弹出一个详情页，里面的请求网址就是目标网址，如果照搬就不需要填写app中的“参数”字段。请求头可以先不管，如果之后显示发送失败则可以尝试加上请求头。然后点击详情页的**`“参数”`**一项，这里就是请求体了，一般都会有用户名密码的信息，把这个填入请求体字段中即可。**

## 注意，填写需要符合格式！！！

*如果请求信息有误，可以长按这个请求进入编辑页面。*

全部保存以后，回到主界面，点击刚才设置的这个脚本即可执行。

如有任何问题或建议欢迎联系 lxjarooba@foxmail.com

# enjoy it!!!
