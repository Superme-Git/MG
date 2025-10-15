/**
 * 
 */
package fire.log.enums;

/**
 * 物品消耗方式
 * author yebin @ 2016年3月23日
 */
public enum YYLoggerTuJingEnum {
	NO_USED(0)
	,GM(1)
	,GENERAL(2)//(内部调用,如背包之间取放东西,堆叠,整理等)
	,QUICKSHOT(3) //快照
	,TEMPFULL(4)//临时背包满了
	,CLEAR(5)//清空
	
//===========以下是物品或代币获取方式的枚举值===========
	,tujing_Value_itemuseget(1001)//道具使用
	,tujing_Value_jianglizhongxin(1002)//奖励中心
	,tujing_Value_Task(1003)//任务获得
	,tujing_Value_paimaiget(1004)//摆摊交易
	,tujing_Value_huodongjiangli(1005)//活动奖励
	,tujing_Value_huoyuedujiangli(1006)//活跃度领取
	,tujing_Value_chengjiu(1007)//成就获得
	,tujing_Value_guaji(1008)//暗雷掉落
	,tujing_Value_fubenjiangli(1009)//副本奖励
	,tujing_Value_gonghuiyaofang(1010)//公会药房
	,tujing_Value_libaoduihuan(1011)//礼包码兑换
	,tujing_Value_wupinhecheng(1012)//物品合成
	,tujing_Value_dazaomojuget(1013)//打造磨具
	,tujing_Value_fumoget(1014)//制作附魔
	,tujing_Value_daozaoget(1015)//装备打造
	,tujing_Value_shanghuigoumaiget(1016)//商会购买
	,tujing_Value_shangchenggoumai(1017)//商城购买
	,tujing_Value_jifenduihuanget(1018)//积分兑换
	,tujing_Value_kuaijiegoumaiget(1019)//快捷购买
	,tujing_Value_chongzhiget(1020)//充值获得
	,tujing_Value_fenjieget(1021)//装备分解
	,tujing_Value_ShangChengCS(1022)//商城出售
	,tujing_Value_gonghuifenhong(1023)//公会分红
	,tujing_Value_ScenarioTask(1024)//主线任务
	,tujing_Value_gonghuifuben(1025)//公会副本
	,tujing_Value_zhuanpan(1026)//转盘玩法
	,tujing_Value_huobiduihuan(1027)//货币兑换
	,tujing_Value_richang(1028)//日常副本
	,tujing_Value_baoxiang(1029)//开宝箱
	,tujing_Value_bingfeng(1030)//冰封王座
	,tujing_Value_wabao(1031)//挖宝
	,tujing_Value_xitongzengsong(1032)//系统赠送
	,tujing_Value_friendgiveget(1033)//好友赠送
	,tujing_Value_shenshou(1034)//神兽
	,tujing_Value_livediebattle(1035)//生死战
	,tujing_Value_PVP1(1036)//1v1竞技场
	,tujing_Value_PVP3(1037)//3v3竞技场
	,tujing_Value_PVP5(1038)//5v5竞技场
	,tujing_Value_shengsizhan(1039)//生死战
	,tujing_Value_shengsizhanyajin(1040)//生死战押金
	,tujing_Value_rolljiangli(1041)//roll奖励
	,tujing_Value_rollschoolwheel(1042)//转盘奖励
	,tujing_Value_grab(1043)//每日奖励
	,tujing_Value_chongzhifanli(1043)//充值返利
	,tujing_Value_gonghuixiulianget(1044)//公会技能修炼
	,tujing_Value_tongxinjie(1045)//同心结
	,tujing_Value_zhizuofu(1046)//制作符
	,tujing_Value_guiwang(1047)//鬼王奖励
	,tujing_Value_gonghuijuanzengget(1048)//公会捐赠获得
	,tujing_Value_mailget(1049)//邮件获得
	,tujing_Value_buchangget(1050)//补偿获得
	,tujing_Value_guanjunshilianget(1051)//冠军试炼获得
	,tujing_Value_tongyongduihuanmaget(1052)//通用兑换码获得
	,tujing_Value_dati(1053)//答题获得
	,tujing_Value_lianxudenglu(1054)//连续登陆获得
	,tujing_Value_zhihuishilian(1055)//智慧试练
	,tujing_Value_npcjiangli(1056)//npc奖励
	,tujing_Value_fanpaijiangli(1057)//翻牌奖励
	,tujing_Value_qiandao(1058)//签到奖励
	,tujing_Value_baishi(1059)//拜师奖励
	,tujing_Value_baoshi(1060)//宝石从装备取出来
	,tujing_Value_weibo(1061)//分享微博奖励
	,tujing_Value_battle(1062)//战斗奖励
	,tujing_Value_present(1063)//礼包获得
	,tujing_Value_redpack(1064)//红包获得
	,tujing_Value_shifu(1065)//师傅获得
	,tujing_Value_yaoqianshu(1066)//摇钱树获得
	,tujing_Value_bestow(1067) //vip10赐福
	,tujing_Value_spotcard(1068)//点卡交易
	,tujing_Value_createrole(1069)//创建人物
	,tujing_Value_gm_addfushi(1070)//GM增加符石
	,tujing_Value_monthcard_reward(1071)//月卡奖励 by changhao
	,tujing_Value_liansheng_reward(1072)// 连胜宝箱 by changhao	
	,tujing_Value_clanfight_reward(1073)// clanfight by changhao	
	,tujing_Value_clanfight_dkp(1074)// clanfight by changhao
	,tujing_Value_bind_tel(1075)// yangtao	
	,tujing_Value_changeschool(1075)// 转职获得
	,tujing_Value_changeschoolgem(1076)// 转职转宝石获得
	,tujing_Value_composegem(1077)//宝石合成
	,tujing_Value_itemrecover(1078)//找回道具
	,tujing_Value_balckmarket(1080)//黑市交易
	,tujing_Value_gemfenjieget(1081)//宝石分解
//===========以下是物品或代币消耗方式的枚举值===========
	,tujing_Value_paimai(2101)//摆摊交易
	,tujing_Value_dazao(2102)//装备打造
	,tujing_Value_xiuli(2103)//装备修理
	,tujing_Value_xilian(2104)//宠物洗练
	,tujing_Value_peiyang(2105)//宠物培养
	,tujing_Value_use(2106)//任务失去
	,tujing_Value_itemuse(2107)//道具使用
	,tujing_Value_fenjie(2108)//装备分解
	,tujing_Value_hecheng(2109)//物品合成
	,tujing_Value_composegemcost(2110)//宝石合成
	,tujing_Value_xiangqian(2111)//宝石镶嵌
	,tujing_Value_goumai(2112)//商城购买
	,tujing_Value_skillLevelup(2113)//技能升级
	,tujing_Value_buypack(2114)//开通包裹
	,tujing_Value_buydepot(2115)//开通仓库
	,tujing_Value_jihuohuoban(2116)//解锁伙伴
	,tujing_Value_createclan(2117)//创建公会
	,tujing_Value_shanghuigoumaicost(2118)//商会购买消耗
	,tujing_Value_dazaomoju(2119)//打造模具
	,tujing_Value_fumo(2120)//制作附魔
	,tujing_Value_jifenduihuan(2121)//积分兑换
	,tujing_Value_switchpoint(2122)//加点切换
	,tujing_Value_gonghuijuanzeng(2123)//公会捐赠
	,tujing_Value_gonghuigaiming(2124)//公会改名
	,tujing_Value_kuaijiegoumai(2125)//快捷购买
	,tujing_Value_gonghuilianjin(2126)//公会炼金
	,tujing_Value_ranse(2127)//染色
	,tujing_Value_ChallengeShouxi(2128)//挑战首席
	,tujing_Value_gonghuiyaofanguse(2129)//公会药房消耗
	,tujing_Value_shenmishangren(2130)//神秘商人
	,tujing_Value_shenmixiaofan(2131)//神秘小贩
	,tujing_Value_gonghuixiulian(2132)//公会技能修炼
	,tujing_Value_jiebai(2133)//结拜
	,tujing_Value_wabaouse(2134)//挖宝
	,tujing_Value_diuqi(2135)//丢弃
	,tujing_Value_gonghuifubenshangjiao(2136)//公会副本上交物品
	,tujing_Value_friendgive(2137)//好友赠送
	,tujing_Value_xiazhanshu(2138)//下战书扣除
	,tujing_Value_qianlixunzong(2139)//千里寻踪
	,tujing_Value_qunfaxiaoxi(2140)//下战书扣除
	,tujing_Value_gaiming(2141)//改名消耗
	,tujing_Value_guanghuanshengji(2142)//光环升级消耗
	,tujing_Value_shenshoucost(2143)//神兽
	,tujing_Value_huobiduihuancost(2144)//货币兑换消耗
	,tujing_Value_campaign(2145)//职业领袖竞选 by changhao
	,tujing_Value_forceunlock(2146)//强行解锁
	,tujing_Value_redpackout(2147)//红包发放
	,tujing_Value_renxing(2148) //任性循环任务
	,tujing_Value_dagong(2149)//打工消耗
	,tujing_Value_daypa(2150)//日卡 by changhao
	,tujing_Value_sellspotcard(2151)//点卡交易
	,tujing_Value_jubao(2152)//举报
	,tujing_Value_liveskillcost(2153)//生活技能消耗
	,tujing_Value_worldchat(2154)//世界聊天消耗
	,tujing_Value_worldchat_enery(2155)//世界聊天活力消耗 by changhao
	,tujing_Value_professionleader_titletask(2156)//职业任务称号 by changhao
	,tujing_Value_renxingmaxituan(2157)//任性马戏团
	,tujing_Value_monthcard(2158)//月卡 by changhao
	,tujing_Value_changeschoolcost(2159)// 转职消费
	,tujing_Value_changeschoolweaponcost(2160)//转职转武器消耗
	,tujing_Value_changeschoolgemcost(2161)//转职转宝石消耗
	,tujing_Value_itemrecovercost(2162)//道具找回消耗
	,tujing_Value_petrecovercost(2163)//宠物找回消耗
	,tujing_Value_gemfenjie(2164)//宝石分解
	;
	
	private int tujing;
	private YYLoggerTuJingEnum(int tujingType){
		this.tujing = tujingType;
	}
	
	public int toVal() {
		return tujing;
	}
	
	// 是否是获得类型
	public Boolean isGet() {
		if(tujing > 2100)
			return false;
		else if (tujing > 1000){
			return true;
		}
		return null;
	}
}
