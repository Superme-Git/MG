========================================================================
    STATIC LIBRARY : Libpak Project Overview
========================================================================

Libpak - 用于解析修仙（MHZX）资源包的精简静态库。

修仙引擎使用的资源包为.pak文件，实质为.zip包。
每一个.pak文件有一个pak.meta.xml文件描述其中的资源。

一个2D角色资源包，一般由body.pak,color.pak,hair.pak,weapon.pak及相关的XML配置文件等组成。
以角色c001为例，目录结构如下：

character
│  c001.action.xml
│  c001.ani.xml
│  c001.audio.xml
│
├─c001
│      body.pak
│      color.pak
│      hair.pak
│      weapon.pak
│      weapon01.pak
│      weapon02.pak
│      weapon03.pak
│      weapon04.pak
│      weapon05.pak
│      weapon06.pak
│      weapon07.pak
│      weapon08.pak
│      weapon09.pak
│      weapon10.pak

c001.ani.xml,c001.action.xml,c001.audio.xml等文件定义了c001目录下的若干pak包之间的关联关系。

本工程(Libpak)只负责解析并处理body.pak、color.pak、hair.pak、weapon.pak等包的内容，并支持读
取pak中的图片像素信息供调用方使用，而不负责解析c001.ani.xml,c001.action.xml,c001.audio.xml等
XML文件的定义的应用逻辑关系。

因此，本工程也适用于解析修仙的/npc、/monster、/pak、/ui等资源包。


/////////////////////////////////////////////////////////////////////////////
