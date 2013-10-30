```
 ____                                  ____                 ___  __      
/\  _`\                 __            /\  _`\             /'___\/\ \__   
\ \ \L\_\  __     ___  /\_\   __  _   \ \,\L\_\     ___  /\ \__/\ \ ,_\  
 \ \  _\//'__`\ /' _ `\\/\ \ /\ \/'\   \/_\__ \    / __`\\ \ ,__\\ \ \/  
  \ \ \//\  __/ /\ \/\ \\ \ \\/>  </     /\ \L\ \ /\ \L\ \\ \ \_/ \ \ \_ 
   \ \_\\ \____\\ \_\ \_\\ \_\/\_/\_\    \ `\____\\ \____/ \ \_\   \ \__\
    \/_/ \/____/ \/_/\/_/ \/_/\//\/_/     \/_____/ \/___/   \/_/    \/__/
```                         

-----------------------                                                                         


#### 一、简介

HotSpot Launcher是一个用于快速生成HotSpot虚拟机启动参数的Eclipse插件。

目前最新的HotSpot虚拟机各种参数（product、manageable、develop、diagnostic、experimental...）已经超过1000个，即使对于比较熟悉虚拟机的同学，要记住它们也十分困难，每次去globals.hpp看或者用-XX:PrintFlagsFinal来查也不方便。如果你有类似的需求，这个小插件应该能带来一些便利。

**下载地址**：[org.fenixsoft.hotspot.launcher_1.0.0.201310291631.jar](https://raw.github.com/fenixsoft/hotspot.launcher/master/dest/plugins/org.fenixsoft.hotspot.launcher_1.0.0.201310291631.jar)


#### 二、快速上手：

- 安装：把插件直接扔到Eclipse的dropins文件夹即可，其他link等安装方式请参考Eclipse文档。

+ HotSpot Launcher增强了Eclipse原有Run/Debug Configurations中的Argument页签，如下图所示，原来的Argument页签变为了HS Argument，下面增加了Basic、Advance两个页签，在Basic页签中包含了Eclipse原有的全部功能。 

![image](https://raw.github.com/fenixsoft/hotspot.launcher/master/doc/sshot-1.png)

+ 在Advacne页签中，提供了HotSpot虚拟机所有参数树形列表显示，在Basic页签的VM Argument文本框中填写的内容，已经被勾选上，如下图所示（选择过滤仅显示被选中的参数）

![image](https://raw.github.com/fenixsoft/hotspot.launcher/master/doc/sshot-2.png)

+ 在Advacne页签中，可以勾选需要打开的虚拟机参数，选中参数后，树形列表下方提供了该参数作用的简要说明。上方的文本框是参数快速过滤，后面4个按钮作用为“参数类型过滤（譬如仅显示debug和experimental的参数）”、“仅显示被选中的参数”、“全部展开”和“全部搜索”。

![image](https://raw.github.com/fenixsoft/hotspot.launcher/master/doc/sshot-3.png)

+ 勾选参数后，可在Value列输入参数值，Default列是默认值供参考，转回Basic页签时将会把勾选的生成成HotSpot虚拟机的参数文本，显示在Basic页签的VM Argument文本框中。


#### 三、版权

- 本插件遵循LGPL开源协议
- 参数描述来源于[OpenJDK](http://openjdk.java.net/)源码的share/vm/runtime/globals.hpp文件
- 部分图标来源于[Eclipse.org](http://www.eclipse.org/)


#### 四、反馈

- 提 issue 或者 pull-request
- 邮箱反馈：[icyfenix@gmail.com](mailto:icyfenix@gmail.com?subject=对HotSpot Lanucher程序有建议)


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/fenixsoft/hotspot.launcher/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

