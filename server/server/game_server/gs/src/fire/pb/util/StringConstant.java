package fire.pb.util;

import java.util.Map;

import fire.pb.main.ConfigManager;
import fire.pb.message.SStringRes;

public class StringConstant {
	public static final int 怪物	=	20;
	public static final int 的=	54;
	public static final int 你好我加你为好友了哦以后常联系 = 55;
	
	public static final int PHYFORCE = 260;
	public static final int SPEED = 261;
	public static final int ATTACK = 262;
	public static final int MAGIC = 263;
	public static final int DEFEND = 264;
	public static final int APTITUDE = 265;
	
	public static final int 人员清理 = 266;
	public static final int 长期不上线 = 267;
	public static final int 表现不积极 = 268;
	public static final int 志趣不合 = 269;
	
	public static final int EVENT_CREATE_STR=270;//270	<T t='$parameter1$' c='ff06cc11'/><T t='创建公会' c='FF693F00'/>
	public static final int EVENT_ADD_STR=271;//271	<T t='恭喜新成员' c='FF693F00'/><T t='$parameter1$' c='ff06cc11'/><T t='加入公会。' c='FF693F00'/>
	public static final int EVENT_QUIT_STR=272;//272	<T t='很遗憾' c='FF693F00'/><T t='$parameter1$' c='ff06cc11'/><T t='离开了公会。' c='FF693F00'/>
	public static final int EVENT_APPOINT_STR=273;//273	<T t='$parameter1$' c='ff06cc11'/><T t='将' c='FF693F00'/><T t='$parameter2$' c='ff06cc11'/><T t='任命为' c='FF693F00'/><T t='$parameter3$' c='ff06cc11'/>
	public static final int EVENT_KICK_STR=274;//274	<T t='$parameter1$' c='ff06cc11'/><T t='因' c='FF693F00'/><T t='$parameter2$' c='ff06cc11'/><T t='被' c='FF693F00'/><T t='$parameter3$' c='ff06cc11'/><T t='踢出公会。' c='FF693F00'/>
	public static final int EVENT_MAINTENANCE_STR=275;//275	<T t='公会维护消耗了' c='FF693F00'/><T t='$parameter1$' c='ff06cc11'/><T t='公会资金。' c='FF693F00'/>
	public static final int EVENT_BUILD_UP_STR=276;//276	<T t='$parameter1$' c='FF693F00'/><T t='提升至' c='FF693F00'/><T t='$parameter2$' c='FF693F00'/><T t='级' c='FF693F00'/>
	public static final int EVENT_BUILD_DOWN_STR=277;//277	<T t='$parameter1$' c='FF693F00'/><T t='效果降至' c='FF693F00'/><T t='$parameter2$' c='FF693F00'/><T t='级' c='FF693F00'/>
	public static final int EVENT_CHANGE_NAME_STR=295;//<T t='$parameter1$' c='ff06cc11'/><T t='将公会名称修改为' c='FF693F00'/><T t='$parameter2$' c='FF693F00'/>
	public static final int EVENT_CHANGE_INST_SYS_STR=313;//经过广大成员历尽千辛万苦的努力，终于通关公会副本$parameter1$,下次活动将默认挑战公会副本$parameter2$.(可在活动面板管理按钮处设置)
	public static final int EVENT_CHANGE_INST_MAN_STR=314;// 公会成员$parameter1$修改挑战公会副本成功，下次公会副本活动将挑战公会副本$parameter2$.(可在活动面板管理按钮处设置)
	public static final int EVENT_IMPEACH_START_STR=315;// <T t='$parameter1$' c='ff005b0f'/><T t='$parameter2$' c='ff005b0f'/><T t='发起了弹劾' c='ff50321a'/>
	public static final int EVENT_IMPEACH_SUCC_STR=316;// <T t='$parameter1$' c='ff005b0f'/><T t='弹劾成功，' c='ff50321a'/><T t='$parameter2$' c='ff005b0f'/><T t='成为新的会长' c='ff50321a'/>
	
	public static final int BUILD_CLAN=278;//公会
	public static final int BUILD_GOLD=279;//金库
	public static final int BUILD_DRUGSTONE=280;//药房
	public static final int BUILD_HOTIAL=281;//旅馆
	public static final int SYSTEM_WORD=282;//系统
	
	public static final int WORLD_REDPACK=301;//	世界红包
	public static final int CLAN_REDPACK=302;//	公会红包
	public static final int TEAM_REDPACK=303;//	队伍红包

	
	
	
	public static final Map<Integer, SStringRes> stringCfgs = ConfigManager.getInstance().getConf(fire.pb.message.SStringRes.class);
	
	public static String ID2String(String defaultStr, int id){
		String result = defaultStr;
		if(StringConstant.stringCfgs != null ){
			SStringRes res = StringConstant.stringCfgs.get(id);
			if(res != null){
				result = res.msg;
			}
		}
		return result;
	}
	
}
