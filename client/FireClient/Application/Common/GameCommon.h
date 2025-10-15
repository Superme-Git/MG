/************************************************************************/
/*                     游戏相关的一些定义                               */
/************************************************************************/
#ifndef _FIRECLIENT_GAMECOMMON_H_
#define _FIRECLIENT_GAMECOMMON_H_

namespace ColorDefine
{
	const DWORD Aqua=0xff00ffff; //浅绿色
	const DWORD Azure=0xfff0ffff;//天蓝色
	const DWORD Beige=0xfff5f5dc;//米色
	const DWORD Bisque=0xffffe4c4;//桔黄色
	const DWORD Black=0xff000000; //黑色
	const DWORD Blue=0xff0000ff;  //蓝色
	const DWORD Brown=0xffa52a2a; //褐色
	const DWORD Burlywood=0xffdeb887; //实木色
	const DWORD Cadetblue=0xff5f9ea0; //军兰色
	const DWORD Chartreuse=0xff7fff00; //黄绿色
	const DWORD Chocolate=0xffd2691e; //巧可力色
	const DWORD Coral=0xffff7f50; //珊瑚色
	const DWORD Cornflowerblue=0xff6495ed; //菊兰色
	const DWORD Cornsilk=0xfffff8dc; //米绸色
	const DWORD Crimson=0xffdc143c; //暗深红色
	const DWORD Cyan=0xff00ffff; //青色
	const DWORD Darkblue=0xff00008b; //暗蓝色
	const DWORD Darkcyan=0xff008b8b; //暗青色
	const DWORD Darkgoldenrod=0xffb8860b; //暗金黄色
	const DWORD Darkgray=0xffa9a9a9; //暗灰色
	const DWORD Darkgreen=0xff6400; //暗绿色
	const DWORD Darkgrey=0xffa9a9a9; //暗灰色
	const DWORD Darkkhaki=0xffbdb76b; //暗黄褐色
	const DWORD Darkmagenta=0xff8b008b; //暗洋红
	const DWORD Darkolivegreen=0xff556b2f; //暗橄榄绿
	const DWORD Darkorange=0xffff8c00; //暗桔黄色
	const DWORD Darkorchid=0xff9932cc; //暗紫色
	const DWORD Darkred=0xff8b0000; //暗红色
	const DWORD Darksalmon=0xffe9967a; //暗肉色
	const DWORD Darkseagreen=0xff8fbc8f; //暗海兰色
	const DWORD Darkslateblue=0xff483d8b; //暗灰蓝色
	const DWORD Darkslategray=0xff2f4f4f; //暗瓦灰色
	const DWORD Darkslategrey=0xff2f4f4f; //暗瓦灰色
	const DWORD Darkturquoise=0xff00ced1; //暗宝石绿
	const DWORD Darkviolet=0xff9400d3; //暗紫罗兰色
	const DWORD Deeppink=0xffff1493; //深粉红色
	const DWORD Deepskyblue=0xff00bfff; //深天蓝色
	const DWORD Dimgray=0xff696969; //暗灰色
	const DWORD Dimgrey=0xff696969; //暗灰色
	const DWORD Dodgerblue=0xff1e90ff; //闪兰色
	const DWORD Firebrick=0xffb22222; //火砖色
	const DWORD Floralwhite=0xfffffaf0; //花白色
	const DWORD Forestgreen=0xff228b22; //森林绿
	const DWORD Fuchsia=0xffff00ff; //紫红色
	const DWORD Gainsboro=0xffdcdcdc; //淡灰色
	const DWORD Ghostwhite=0xfff8f8ff; //幽灵白
	const DWORD Gold=0xffffd700; //金色
	const DWORD Goldenrod=0xffdaa520; //金麒麟色
	const DWORD Gray=0xff808080; //灰色
	const DWORD Green=0xff00ff00; //绿色
	const DWORD Greenyellow=0xffadff2f; //黄绿色
	const DWORD Grey=0xff808080; //灰色
	const DWORD Honeydew=0xfff0fff0; //蜜色
	const DWORD Hotpink=0xffff69b4; //热粉红色
	const DWORD Indianred=0xffcd5c5c; //印第安红
	const DWORD Indigo=0xff4b0082; //靛青色
	const DWORD Ivory=0xfffffff0; //象牙色
	const DWORD Khaki=0xfff0e68c; //黄褐色
	const DWORD Lavender=0xffe6e6fa; //淡紫色
	const DWORD Lavenderblush=0xfffff0f5; //淡紫红
	const DWORD Lawngreen=0xff7cfc00; //草绿色
	const DWORD Lemonchiffon=0xfffffacd; //柠檬绸色
	const DWORD Lightblue=0xffadd8e6; //亮蓝色
	const DWORD Lightcoral=0xfff08080; //亮珊瑚色
	const DWORD Lightcyan=0xffe0ffff; //亮青色
	const DWORD Lightgoldenrodyellow=0xfffafad2; //亮金黄色
	const DWORD Lightgray=0xffd3d3d3; //亮灰色
	const DWORD Lightgreen=0xff90ee90; //亮绿色
	const DWORD Lightgrey=0xffd3d3d3; //亮灰色
	const DWORD Lightpink=0xffffb6c1; //亮粉红色
	const DWORD Lightsalmon=0xffffa07a; //亮肉色
	const DWORD Lightseagreen=0xff20b2aa; //亮海蓝色
	const DWORD Lightskyblue=0xff87cefa; //亮天蓝色
	const DWORD Lightslategray=0xff778899; //亮蓝灰
	const DWORD Lightslategrey=0xff778899; //亮蓝灰
	const DWORD Lightsteelblue=0xffb0c4de; //亮钢兰色
	const DWORD Lightyellow=0xffffffe0; //亮黄色
	const DWORD Lime=0xfffafad2; //酸橙色
	const DWORD Limegreen=0xff32cd32; //橙绿色
	const DWORD Linen=0xfffaf0e6; //亚麻色
	const DWORD Magenta=0xffff00ff; //红紫色
	const DWORD Maroon=0xff800000; //粟色
	const DWORD Mediumaquamarine=0xff66cdaa; //中绿色
	const DWORD Mediumblue=0xff0000cd; //中兰色
	const DWORD Mediumorchid=0xffba55d3; //中粉紫色
	const DWORD Mediumpurple=0xff9370db; //中紫色
	const DWORD Mediumseagreen=0xff3cb371; //中海蓝
	const DWORD Mediumslateblue=0xff7b68ee; //中暗蓝色
	const DWORD Mediumspringgreen=0xff00fa9a; //中春绿色
	const DWORD Mediumturquoise=0xff48d1cc; //中绿宝石
	const DWORD Mediumvioletred=0xffc71585; //中紫罗兰色
	const DWORD Midnightblue=0xff191970; //中灰兰色
	const DWORD Mintcream=0xfff5fffa; //薄荷色
	const DWORD Mistyrose=0xffffe4e1; //浅玫瑰色
	const DWORD Moccasin=0xffffe4b5; //鹿皮色
	const DWORD Navajowhite=0xffffdead; //纳瓦白
	const DWORD Navy=0xff80; //海军色
	const DWORD Oldlace=0xfffdf5e6; //老花色
	const DWORD Olive=0xff808000; //橄榄色
	const DWORD Olivedrab=0xff6b8e23; //深绿褐色
	const DWORD Orange=0xffffa500; //橙色
	const DWORD Orangered=0xffff4500; //红橙色
	const DWORD Orchid=0xffda70d6; //淡紫色
	const DWORD Palegoldenrod=0xffeee8aa; //苍麒麟色
	const DWORD Palegreen=0xff98fb98; //苍绿色
	const DWORD Paleturquoise=0xffafeeee; //苍宝石绿
	const DWORD Palevioletred=0xffdb7093; //苍紫罗兰色
	const DWORD Papayawhip=0xffffefd5; //番木色
	const DWORD Peachpuff=0xffffdab9; //桃色
	const DWORD Peru=0xffcd853f; //秘鲁色
	const DWORD Pink=0xffffc0cb; //粉红色
	const DWORD Plum=0xffdda0dd; //洋李色
	const DWORD Powderblue=0xffb0e0e6; //粉蓝色
	const DWORD Purple=0xff800080; //紫色
	const DWORD Red=0xffff0000; //红色
	const DWORD Rosybrown=0xffbc8f8f; //褐玫瑰红
	const DWORD Royalblue=0xff41690; //皇家蓝
	const DWORD Saddlebrown=0xff8b4513; //重褐色
	const DWORD Salmon=0xfffa8072; //鲜肉色
	const DWORD Sandybrown=0xfff4a460; //沙褐色
	const DWORD Seagreen=0xff2e8b57; //海绿色
	const DWORD Seashell=0xfffff5ee; //海贝色
	const DWORD Sienna=0xffa0522d; //赭色
	const DWORD Silver=0xffc0c0c0; //银色
	const DWORD Skyblue=0xff87ceeb; //天蓝色
	const DWORD Slateblue=0xff6a5acd; //石蓝色
	const DWORD Slategray=0xff708090; //灰石色
	const DWORD Slategrey=0xff708090; //灰石色
	const DWORD Snow=0xfffffafa; //雪白色
	const DWORD Springgreen=0xff00ff7f; //春绿色
	const DWORD Steelblue=0xff4682b4; //钢兰色
	const DWORD Tan=0xffd2b48c; //茶色
	const DWORD Teal=0xff8080; //水鸭色
	const DWORD Thistle=0xffd8bfd8; //蓟色
	const DWORD Tomato=0xffff6347; //西红柿色
	const DWORD Turquoise=0xff40e0d0; //青绿色
	const DWORD Violet=0xffee82ee; //紫罗兰色
	const DWORD Wheat=0xfff5deb3; //浅黄色
	const DWORD White=0xffffffff; //白色
	const DWORD Whitesmoke=0xfff5f5f5; //烟白色
	const DWORD Yellow=0xffffff00; //黄色
}

// 9个种族
enum eSchoolType
{
	eSchoolNull = 0,
	eSchoolZhanShi = 11,	//战士
	eSchoolShengQiShi = 12,	//圣骑士
	eSchoolLieRen = 13,	//猎人
	eSchoolDeLuYi = 14,	//德鲁伊
	eSchoolFaShi = 15,	//法师
	eSchoolMuShi = 16,	//牧师
	eSchoolSaMan = 17,	//萨满
	eSchoolDaoZei = 18,	//盗贼
	eSchoolShuShi = 19,	//术士
	eSchoolMax,
};

enum eGameState
{
	eGameStateNull = 0,
	eGameStateLogin,			//登陆状态
	eGameStateRunning,			//进入游戏
	eGameStateBattleDemo,		//观看战斗录像
	eGameStateEditBattleAni,	//战斗动画编辑
	eGameStateMax
};

//性别
enum eSexType
{
	eSexMale = 1,
	eSexFemale,
};

//一键分享的平台
enum eShareSDK
{
	eShareSDK_SinaWeibo = 1,			// 新浪微博
	eShareSDK_TencentWeibo = 2,			// 腾讯微博
	eShareSDK_Douban = 3,				// 豆瓣
	eShareSDK_QZone = 4,				// QQ空间
	eShareSDK_Renren = 5,				// 人人网
	eShareSDK_KaiXin = 6,				// 开心网
	eShareSDK_Facebook = 7,				// Facebook
	eShareSDK_Twitter = 8,				// Twitter
	eShareSDK_Evernote = 9,				// 印象笔记
	eShareSDK_FourSquare = 10,			// FourSquare
	eShareSDK_GooglePlus = 11,			// Google+
	eShareSDK_Instagram = 12,			// Instagram
	eShareSDK_LinkedIn = 13,				// 领英
	eShareSDK_Tumblr = 14,				// Tumblr
	eShareSDK_Email = 15,				// 邮件
	eShareSDK_ShortMessage = 16,			// 信息
	eShareSDK_Wechat = 17,				// 微信好友
	eShareSDK_WechatMoments = 18,		// 微信朋友圈
	eShareSDK_QQ = 19,					// QQ好友
	eShareSDK_Instapaper = 20,			// Instapaper
	eShareSDK_Pocket = 21,				// Pocket
	eShareSDK_YouDao = 22,				// 有道云笔记
	eShareSDK_Pinterest = 23,			// Pinterest
	eShareSDK_Flickr = 24,				// Flickr
	eShareSDK_Dropbox = 25,				// Dropbox
	eShareSDK_VKontakte = 26,			// VK
	eShareSDK_WechatFavorite = 27,		// 微信收藏
	eShareSDK_Yixin = 28,				// 易信好友
	eShareSDK_YixinMoments = 29,			// 易信朋友圈
	eShareSDK_Mingdao = 30,				// 明道
	eShareSDK_Line = 31,					// Line
	eShareSDK_WhatsApp = 32,				// WhatsApp
	eShareSDK_KakaoTalk = 33,			// KakaoTalk
	eShareSDK_KakaoStory = 34,			// KakaoStory
	eShareSDK_FacebookMessenger = 35,	// FacebookMessenger
	eShareSDK_Bluetooth = 36,			// 蓝牙
	eShareSDK_Alipay = 37,				// 支付宝好友
};

//一键分享的类型
enum eShareType
{
	eShareType_Text = 1,				// 分享纯文字
	eShareType_Picture = 2,				// 分享图片
	eShareType_WebUrl = 3,				// 分享链接
};

//队伍状态
enum eTeamMemberState
{
	eTeamMemberNULL = 0,		//无
	eTeamMemberNormal = 1,		//正常队伍状态
	eTeamMemberAbsent = 2,		//暂离状态
	eTeamMemberReturn = 3,		//归队状态
	eTeamMemberFallline = 4,		//掉线
	eTeamMemberMax,
};


#endif
