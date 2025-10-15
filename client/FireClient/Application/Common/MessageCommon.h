#pragma once

#include "Utils2.h"

#define  MAX_BUTTON_NUM 20

 
enum eMsgType
{
	eMsgType_Normal,
	eMsgType_Team,				//组队相关
	eMsgType_Friend,				//好友相关
	eMsgType_Family,				//
	eMsgType_Faction,			//
	eMsgType_FactionBattle,		//
	eMsgType_Trade,				//交易相关
	eMsgType_Marriage,			//
	eMsgType_PK,					//PK相关
	eMsgType_GoIntoMap,			//进地图确认
	eMsgType_Contract,			//团队协约
	eMsgType_TeamSummon,			//
	eMsgType_Game,				//游戏
	eMsgType_Activity,			//活动
	eMsgType_FriendChat, 		//好友聊天最小化
	eMsgType_YeXiDiYing,			//
	eMsgType_GroupChat,			//群聊天最小化
	eMsgType_FeedFamilyBoss,		//
	eMsgType_FeedFactionBoss,	//
	eMsgType_Master,				//
    eMsgType_Keju,       		//
    eMsgType_VipBuy,     		//VIP次数购买
	eMsgType_Max
};

//二次确认框类型
enum eConfirmType
{
	eConfirmNormal,
	eConfirmOK,						//只有单个确定按钮
	eConfirmResetPoint,				//洗点
	eConfirmDropItem,				//丢弃物品
	eConfirmNormalRepair,			//
	eConfirmPearlRepair,			//
	eConfirmSetFlyFlag,				//
	eConfirmInitDuel,				//
	eConfirmInitPK,					//发起PK
	eConfirmGiven,					//给予确认
	eConfirmDivorce,				//
	eConfirmFamilyExpel,			//
	eConfirmFactionSummon,			//
	eConfirmChangeBidding,			//
	eConfirmFBAppoint,				//
	eConfirmBattleEscape,			//战斗逃跑
	eConfirmKeJuSignUp,				//
	eConfirmKeJuGiveUp,				//
	eConfirmFightChief,				//
	eConfirmBuyItem,				//购买确认
	eConfirmStoreName,				//
	eConfirmCleanTempBag,			//清空临时包裹
	eConfirmWeddingCard,			//确认送出请帖
	eConfirmSellShop,				//出售商店
	eConfirmPetShopRelated,			//宠物商店相关
	eConfirmItemShopRelated,		//物品商店相关
	eConfirmPetSkillBook,			//宠物技能书
	eConfirmLevel3Drug,				//使用3级药
	eConfirmTakeTianZun,			//
	eConfirmPayTianZun,				//
	eConfirmFreePet,				//放生宝宝
	eConfirmLevelUp,				//升级确认
	eConfirmAddGroup,       		//同意加入群确定
	eConfirmExchangeRepu,			//声望值兑换
	eConfirmMasterOutGoing,			//
	eConfirmExitDream,      		//
	eConfirmReturnDream,    		//
	eConfirmBuyStuff,				//购买确认
	eConfirmInLay,					//
	eConfirmBuyGem,					//
	eConfirmQuench,					//
	eConfirmRefineEquip,			//
	eConfirmOpenShop,				//商会确认开店
	eConfirmAcupointUp,				//
	eConfirmExpelFamily,			//
	eConfirmAcution,				//竞拍号码确认
	eConfirmFamilyApply,			//
	eConfirmLeaveFamily,			//
	ePurifyAllDrug,					//
	eConfirmRevokeInfo,				//撤销征友消息确认
	eConfirmStopKite,				//
	eConfirmGivePet,				//给予宠物确认
	eConfirmDemonSlayerTask,		//
	eConfirmLeaderJHSummon,			//
	eConfirmSaveAntique,			//
	eConfirmDelAntique,				//
	eConfirmDZXY,					//
	eConfirmRecast,					//
	eConfirmRecast1,				//
	eDrawPetStar,					//
	eConfirmBanTalk,				//禁言确认
	eConfirmRepairAll,				//全部修理确认
	eConfirmSendTrumpet,    		//使用喇叭发言确认
	eConfirmLeaveBoat,				//
	eConfirmBuyBaoChan,				//
	eConfirmUseFElement,			//
    eConfirmMixBindItem,			//
	eConfirmAcceptZaxueLiansheng,	//
	eConfirmVoteLeave,				//
	eConfirmLeaveFaction,			//
	eConfirmSaveAccout,				//确定保存账号
	eConfirmExitGame,				//确定退出游戏
	eConfirmApplyWulindahui,		//
	eConfirmCharge,					//确定支付
	eConfirmEnterInstance,			//确认进入副本
	eConfirmSupplyFlag,				//
	eConfirmExpel5Days,				//
	eConfirmDropSkill,				//丢弃技能
	eConfirmApplyAntialise,			//确定退到角色选择启用新的抗锯齿设置
	eConfirmPetEvolve,				//
	eConfirmGemLevelUp,				//
	eConfirmGemSmelt,				//
	eConfirmQGEffectItem,			//
	eConfirmCloseReplaceEquip,		//确定关闭替换装备
	eConfirmCloseReplaceEquipTip,	//确定关闭替换装备提醒
	eConfirmCloseRemindUseItem,		//确定关闭大红大蓝提示界面
	eConfirmCloseRemindUseItemTip,	//确定关闭大红大蓝提示
	eConfirmAbandonTask,			//放弃任务
	eConfirmDeleteColorScheme,		//确定删除配色
	eConfirmReportRole,				//举报玩家确认
	eConfirmLottery,				//公测抽奖
	eConfirmEnterPetIsland,			//确定进入宠物迷城
	eConfirmSelectNetType,			//确定选择进入游戏的网络类型
	eConfirmXunBaoSell,				//
	eConfirmBuyStallBoard,			//
    eConfirmOpenPetIsland,			//
    eConfirmCombineItem,			//确定合成道具
    eConfirmAllCombineItem,			//确定合成所有道具
    eConfirmGotoHighLevelMap,		//确定传送至比自己等级高的地级
    eConfirmBreakFriend,			//确认解除好友关系
    eConfirmTeamLeaderEnterFuben,   //副本进度不同时进入副本的确认
    eConfirmLoginFail
};

inline std::wstring GetConfirmTitleByType(eConfirmType type)
{
	switch(type)
	{
	case eConfirmNormal:
	case eConfirmNormalRepair:
		return MHSD_UTILS::GETSTRING(1979).c_str();
	case eConfirmResetPoint:		//洗点
		return MHSD_UTILS::GETSTRING(1980).c_str();
	case eConfirmDropItem:			//丢弃物品
		return MHSD_UTILS::GETSTRING(1981).c_str();
	case eConfirmPearlRepair:		//
		return MHSD_UTILS::GETSTRING(1982).c_str();
	case eConfirmSetFlyFlag:		//
		return MHSD_UTILS::GETSTRING(1983).c_str();
	case eConfirmInitDuel:			//
		return MHSD_UTILS::GETSTRING(1984).c_str();
	case eConfirmInitPK:			//发起PK
		return MHSD_UTILS::GETSTRING(1985).c_str();
	case eConfirmGiven:				//给予确认
		return MHSD_UTILS::GETSTRING(1986).c_str();
	case eConfirmDivorce:			//
		return MHSD_UTILS::GETSTRING(1987).c_str();
	case eConfirmFamilyExpel:		//
		return MHSD_UTILS::GETSTRING(1988).c_str();
	case eConfirmFactionSummon:		//
		return MHSD_UTILS::GETSTRING(1989).c_str();
	case eConfirmChangeBidding:		//
		return MHSD_UTILS::GETSTRING(1990).c_str();
	case eConfirmFBAppoint:			//
		return MHSD_UTILS::GETSTRING(1991).c_str();
	case eConfirmBattleEscape:		//战斗逃跑
		return MHSD_UTILS::GETSTRING(1992).c_str();
	case eConfirmKeJuSignUp:		//
		return MHSD_UTILS::GETSTRING(1993).c_str();
	case eConfirmKeJuGiveUp:		//
		return MHSD_UTILS::GETSTRING(1994).c_str();	
	case eConfirmFightChief:		//
		return MHSD_UTILS::GETSTRING(1995).c_str();	
	case eConfirmBuyItem:			//购买物品
		return MHSD_UTILS::GETSTRING(1996).c_str();	
	case eConfirmStoreName:			//
		return MHSD_UTILS::GETSTRING(1997).c_str();	
	case eConfirmCleanTempBag:		//清空临时包裹
		return MHSD_UTILS::GETSTRING(1998).c_str();	
	case eConfirmWeddingCard:		//确认送出请帖
		return MHSD_UTILS::GETSTRING(1999).c_str();	
	case eConfirmSellShop:			//出售商店功能确认
		return MHSD_UTILS::GETSTRING(2000).c_str();	
	case eConfirmPetShopRelated:	//商会功能相关(宠物)
		return MHSD_UTILS::GETSTRING(2001).c_str();	
	case eConfirmItemShopRelated:	//商会功能相关(物品)
		return MHSD_UTILS::GETSTRING(2002).c_str();		
	case eConfirmPetSkillBook:		//宠物技能书
		return MHSD_UTILS::GETSTRING(2003).c_str();
	case eConfirmLevel3Drug:		//使用3级药确认
		return MHSD_UTILS::GETSTRING(2004).c_str();
	case eConfirmTakeTianZun:		//
		return MHSD_UTILS::GETSTRING(2005).c_str();	
	case eConfirmPayTianZun:		//
		return MHSD_UTILS::GETSTRING(2006).c_str();		
	case eConfirmFreePet:			//宠物放生确认
		return MHSD_UTILS::GETSTRING(2007).c_str();	
	case eConfirmLevelUp:			//升级确认
		return MHSD_UTILS::GETSTRING(2008).c_str();		
	case eConfirmAddGroup:			//同意加入群确定
		return MHSD_UTILS::GETSTRING(2009).c_str();	
	case eConfirmExchangeRepu:		//
		return MHSD_UTILS::GETSTRING(2010).c_str();
	case eConfirmMasterOutGoing:	//
		return MHSD_UTILS::GETSTRING(2011).c_str();	
	case eConfirmBuyStuff:			//购买确认
		return MHSD_UTILS::GETSTRING(2012).c_str();		
	case eConfirmInLay:				//镶嵌宝石确认
		return MHSD_UTILS::GETSTRING(2013).c_str();	
	case eConfirmBuyGem:			//购买宝石确认
		return MHSD_UTILS::GETSTRING(2014).c_str();	
	case eConfirmOpenShop:			//开启商店确认
		return MHSD_UTILS::GETSTRING(2015).c_str();	
	case eConfirmAcupointUp:		//
		return MHSD_UTILS::GETSTRING(2016).c_str();		
	case eConfirmExpelFamily:		//
		return MHSD_UTILS::GETSTRING(2017).c_str();	
	case eConfirmAcution:			//竞拍确认
		return MHSD_UTILS::GETSTRING(2018).c_str();	
	case eConfirmFamilyApply:		//
		return MHSD_UTILS::GETSTRING(2019).c_str();
	case eConfirmLeaveFamily:		//
		return MHSD_UTILS::GETSTRING(2020).c_str();
	case ePurifyAllDrug:			//
		return MHSD_UTILS::GETSTRING(2021).c_str();
	case eConfirmStopKite:			//
		return MHSD_UTILS::GETSTRING(2022).c_str();
	case eConfirmGivePet:			//
		return MHSD_UTILS::GETSTRING(2023).c_str();
	case eConfirmLeaderJHSummon:	//
		return MHSD_UTILS::GETSTRING(2024).c_str();
	case eConfirmRecast:			//
		return MHSD_UTILS::GETSTRING(2025).c_str();			
	case eConfirmRefineEquip:		//
		return MHSD_UTILS::GETSTRING(2026).c_str();			
	case eConfirmBanTalk:			//禁言确认
		return MHSD_UTILS::GETSTRING(2027).c_str();			
	case eConfirmLeaveBoat:			//
		return MHSD_UTILS::GETSTRING(2028).c_str();
	case eConfirmUseFElement:		//
		return MHSD_UTILS::GETSTRING(2029).c_str();		
	case eConfirmVoteLeave:			//
		return MHSD_UTILS::GETSTRING(2030).c_str();			
	case eConfirmLeaveFaction:		//
		return MHSD_UTILS::GETSTRING(2031).c_str();		
	case eConfirmEnterInstance:		//
		return MHSD_UTILS::GETSTRING(2032).c_str();
	case eConfirmSupplyFlag:		//
		return MHSD_UTILS::GETSTRING(2033).c_str();
	case eConfirmReportRole:		//
		return MHSD_UTILS::GETSTRING(2569).c_str();
	default:
		return MHSD_UTILS::GETSTRING(2034).c_str();
	}
}
