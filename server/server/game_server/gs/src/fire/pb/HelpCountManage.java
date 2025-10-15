package fire.pb;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import sun.misc.MessageUtils;
import fire.log.Logger;
import fire.pb.activity.award.RewardMgr;
import fire.pb.common.SCommon;
import fire.pb.game.MoneyType;
import fire.pb.main.ConfigManager;
import fire.pb.mission.Module;
import fire.pb.role.NextExpAndResMoney;
import fire.pb.talk.MessageMgr;
import fire.pb.util.MessageUtil;

public class HelpCountManage {
	Logger logger = Logger.getLogger("SYSTEM");
	public static HelpCountManage instance;
    public static HelpCountManage getInstance() {
        if (instance == null) {
        	instance = new HelpCountManage();
        }
        return instance;
    }
    
//    public static final int SHENGWANGVALUE_MAX=2000;//声望最大值
//    public static final int FACTIONVALUE_MAX=500;//最大帮贡值
    
    
    Map<Integer, NextExpAndResMoney> expMap = fire.pb.main.ConfigManager.getInstance().getConf(fire.pb.role.NextExpAndResMoney.class);
    

    /**
     * 获得经验最大值（ 获得援助战斗经验的最大次数，这里已经是次数，不是具体的经验值了）
     * @param rid
     * @return
     */
    public long getExpValueMax(long rid){
//    	//公式360*RoleLv*10
    	Integer curlevel = xtable.Properties.selectLevel(rid);
		if (curlevel == null)
			return 0;
//		//计算经验
//		long expAward = (long) (360*curlevel*10.0);
//		return expAward;
    	NextExpAndResMoney nextExpAndResMoney=expMap.get(curlevel);
    	if(nextExpAndResMoney!=null){
    		return nextExpAndResMoney.getHelpfightnum();
    	}
    	return 0;
    }
    
    /**
     * 获得声望最大值
     * @return
     */
    public int getShengWangValueMax(long rid){
    	Integer curlevel = xtable.Properties.selectLevel(rid);
		if (curlevel == null)
			return 0;
		NextExpAndResMoney nextExpAndResMoney=expMap.get(curlevel);
    	if(nextExpAndResMoney!=null){
    		return nextExpAndResMoney.getShengwangmax();
    	}
    	return 0;
    }
    
    /**
     * 获得帮贡最大值
     * @return
     */
    public int getClanValueMax(long rid){
    	Integer curlevel = xtable.Properties.selectLevel(rid);
		if (curlevel == null)
			return 0;
		NextExpAndResMoney nextExpAndResMoney=expMap.get(curlevel);
    	if(nextExpAndResMoney!=null){
    		return nextExpAndResMoney.getClanvaluemax();
    	}
    	return 0;
    }
    
    /**
     * 援助物品次数  最大次数
     * @param rid
     * @return
     */
    public int getHelpGiveItemNumMax(long rid){
    	Integer curlevel = xtable.Properties.selectLevel(rid);
		if (curlevel == null)
			return 0;
		NextExpAndResMoney nextExpAndResMoney=expMap.get(curlevel);
    	if(nextExpAndResMoney!=null){
    		return nextExpAndResMoney.getHelpgiveitemnum();
    	}
    	return 0;
    }
    
    /**
     * 是否可以援助物品
     * @param roleid
     * @return
     */
    public boolean canAddHelpGiveItemNum(long roleid){
    	int max=getHelpGiveItemNumMax(roleid);
    	int value=0;
    	xbean.helpcount helpcount = xtable.Helpcount.select(roleid);
    	if(helpcount!=null){
    		value=helpcount.getHelpgiveitemnum();
    	}
    	if(value>=max){
    		return false;
    	}
    	return true;
    }
    
    
    /**
     * 求助物品次数  最大次数
     * @param rid
     * @return
     */
    public int getHelpItemNumMax(long rid){
    	Integer curlevel = xtable.Properties.selectLevel(rid);
    	if (curlevel == null)
    		return 0;
    	NextExpAndResMoney nextExpAndResMoney=expMap.get(curlevel);
    	if(nextExpAndResMoney!=null){
    		return nextExpAndResMoney.getHelpitemnum();
    	}
    	return 0;
    }
    
    
    
    /**
     * 援助经验公式
     * @param level
     * @return
     */
    public static long getAddExpValueByHelpFight(long roleid){
    	SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(324);
		if(conf==null){
			return 0;
		}
		int awardid=Integer.parseInt(conf.getValue());
		long awardexp=RewardMgr.getInstance().getAwardExpdistributeAllAward(roleid, awardid, null,true);
		return awardexp;
    }
    
    
    /**
     * 援助物品奖励经验
     * @param level
     * @return
     */
    public static long getAddExpValueByHelpItem(long roleid){
    	SCommon conf=ConfigManager.getInstance().getConf(SCommon.class).get(325);
		if(conf==null){
			return 0;
		}
		int awardid=Integer.parseInt(conf.getValue());
		long awardexp=RewardMgr.getInstance().getAwardExpdistributeAllAward(roleid, awardid, null,true);
		return awardexp;
    }
    
    
    
    /**
     * 添加援助物品次数
     * @param roleid
     * @param reason
     * @return
     */
    public long addHelpGiveItemNum(long roleid,int addnum,String reason){
    	if(addnum<=0){
    		return 0;
    	}
    	xbean.helpcount helpcount = xtable.Helpcount.select(roleid);
    	int addvalue=getHelpGiveItemNumMax(roleid);
    	if(helpcount != null){
    		addvalue=getHelpGiveItemNumMax(roleid)-helpcount.getHelpgiveitemnum();
    	}
    	if(addvalue<=0){
    		addvalue=0;
    		return addvalue;
    	}
    	if(addvalue>0){
    		if(addnum>addvalue){
    			addnum=addvalue;
    		}
    		new PAddHelpCountProc(roleid,0,0,0,addnum,0).call();
    		logger.info("玩家角色id\t"+roleid+"\t援助物品次数\t"+(addnum<addvalue?addnum:addvalue)+"产出说明\t"+reason);
    	}
    	return addnum;
    	
    }
    
    /**
     * 求助物品次数
     * @param roleid
     * @param reason
     * @return
     */
    public long addHelpItemNum(long roleid,int addnum,String reason){
    	if(addnum<=0){
    		return 0;
    	}
    	xbean.helpcount helpcount = xtable.Helpcount.select(roleid);
    	int addvalue=getHelpItemNumMax(roleid);
    	if(helpcount != null){
    		addvalue=getHelpItemNumMax(roleid)-helpcount.getHelpitemnum();
    	}
    	if(addvalue<=0){
    		addvalue=0;
    		return addvalue;
    	}
    	if(addvalue>0){
    		if(addnum>addvalue){
    			addnum=addvalue;
    		}
    		new PAddHelpCountProc(roleid,0,0,0,0,addnum).call();
    		logger.info("玩家角色id\t"+roleid+"\t求助物品次数\t"+(addnum<addvalue?addnum:addvalue)+"产出说明\t"+reason);
    	}
    	return addnum;
    	
    }
    
    
    
   /**
    * 添加援助经验，返回可以添加的经验值
    * @param roleid
    * @param addexpvalue
    * @param reason  产出说明
    * @return
    */
    public long addExpValue(long roleid,long addexpvalue,String reason){
    	if(addexpvalue<=0){
    		return 0;
    	}
    	xbean.helpcount helpcount = xtable.Helpcount.select(roleid);
    	long addvalue=getExpValueMax(roleid);
    	if(helpcount != null){
    		addvalue=getExpValueMax(roleid)-helpcount.getExpvalue();
    	}
    	if(addvalue<=0){
    		addvalue=0;
    		fire.pb.talk.MessageMgr.sendMsgNotify(roleid,166110,null);
    		return addvalue;
    	}
    	if(addvalue>0){
    		if(addexpvalue>addvalue){
    			addexpvalue=addvalue;
    		}
    		new PAddHelpCountProc(roleid,addexpvalue,0,0).call();
    		logger.info("玩家角色id\t"+roleid+"\t援助经验\t"+(addexpvalue<addvalue?addexpvalue:addvalue)+"产出说明\t"+reason);
//       	 160316 <T t="你获得了"></T><T t="$parameter1$" c="ff00ff00"></T><T t="援助经验。"></T>
    		//和策划沟通，这条提示信息不要了，不然客户端会有两条加经验的信息，误导玩家！
    		//Message.sendMsgNotify(roleid,160316,Arrays.asList(addexpvalue+""));
    	}
    	if(addexpvalue==0){
    		fire.pb.talk.MessageMgr.sendMsgNotify(roleid,166110,null);
    	}
    	
    	fire.pb.course.CourseManager.achieveUpdate(roleid, fire.pb.course.CourseType.YUAN_ZHU_ZHAN_DOU);
    	
    	return addexpvalue;
    }
    
    
    /**
     * 添加援助声望，返回可以添加的声望值
     * @param roleid
     * @param addshengwangvalue
     * @param reason  产出说明
     * @return
     */
    public int addShengWangValue(long roleid,int addshengwangvalue,String reason){
    	if(addshengwangvalue<=0){
    		return 0;
    	}
    	xbean.helpcount helpcount = xtable.Helpcount.select(roleid);
    	int addvalue=getShengWangValueMax(roleid);
    	if(helpcount != null){
    		addvalue=getShengWangValueMax(roleid)-helpcount.getShengwangvalue();
    	}
    	if(addvalue<=0){
    		addvalue=0;
    		fire.pb.talk.MessageMgr.sendMsgNotify(roleid,166106,null);
    		return addvalue;
    	}
    	if(addvalue>0){
    		if(addshengwangvalue>addvalue){
    			addshengwangvalue=addvalue;
    		}
    		new PAddHelpCountProc(roleid,0,addshengwangvalue,0).call();
    		logger.info("玩家角色id\t"+roleid+"\t援助声望\t"+(addshengwangvalue<addvalue?addshengwangvalue:addvalue)+"产出说明\t"+reason);
//        	160315 <T t="你获得了"></T><T t="$parameter1$" c="ff00ff00"></T><T t="援助声望。"></T>
//    		Message.sendMsgNotify(roleid,160315,Arrays.asList(addshengwangvalue+""));
    	}
    	if(addshengwangvalue==0){
    		fire.pb.talk.MessageMgr.sendMsgNotify(roleid,166106,null);
    	}
    	
    	return addshengwangvalue;
    }
    
    /**
     * 添加援助帮贡，返回可以添加的帮贡值
     * @param roleid
     * @param addfactionvalue
     * @param reason  产出说明
     * @return
     */
    public int addFactionValue(long roleid,int addfactionvalue,String reason){
    	if(addfactionvalue<=0){
    		return 0;
    	}
    	xbean.helpcount helpcount = xtable.Helpcount.select(roleid);
    	int addvalue=getClanValueMax(roleid);
    	if(helpcount != null){
    		addvalue=getClanValueMax(roleid)-helpcount.getClanvalue();
    	}
    	if(addvalue<=0){
    		addvalue=0;
    		fire.pb.talk.MessageMgr.sendMsgNotify(roleid,166107,null);
    		return addvalue;
    	}
    	if(addvalue>0){
    		if(addfactionvalue>addvalue){
    			addfactionvalue=addvalue;
    		}
    		new PAddHelpCountProc(roleid,0,0,addfactionvalue).call();
    		logger.info("玩家角色id\t"+roleid+"\t援助帮贡\t"+(addfactionvalue<addvalue?addfactionvalue:addvalue)+"产出说明\t"+reason);
//       	 160317 <T t="你获得了"></T><T t="$parameter1$" c="ff00ff00"></T><T t="援助公会贡献。"></T>
    		MessageMgr.psendMsgNotifyWhileCommit(roleid,160064,Arrays.asList(addfactionvalue+""));
    	}
    	if(addfactionvalue==0){
    		fire.pb.talk.MessageMgr.sendMsgNotify(roleid,166107,null);
    	}
    	
    	return addfactionvalue;
    }
    
    /**
     * 添加公会援助次数，有没有公会的逻辑在外面判断，这里不做处理
     * @param roleid
     * @param reason
     * @return
     */
    public int addFactionHelpNum(long roleid,String reason){
    	new PAddHelpCountProc(roleid,1).call();
    	logger.info("玩家角色id\t"+roleid+"\t援助次数1\t"+"产出说明\t"+reason);
    	return 1;
    }
    
    /**
     * 添加到玩家身上的声望，用于马戏团的援助
     * @param roleid
     * @param reson
     * @param tujing
     * @param type 1 交宝 2 战斗
     */
    public void addShengWangToRole(long roleid,String reson,fire.log.enums.YYLoggerTuJingEnum tujing,int type){
    	int shenwang = 2;
    	if(type == 1) {
    		//交宝
    		final SCommon cc = RoleConfigManager.getRoleCommonConfig(349);
		 	shenwang = (cc == null ? 2 : Integer.parseInt(cc.getValue()));
    	} else if(type == 2){
    		//战斗
    		final SCommon cc = RoleConfigManager.getRoleCommonConfig(348);
		 	shenwang = (cc == null ? 2 : Integer.parseInt(cc.getValue()));
    	}
    	int resultsw=HelpCountManage.getInstance().addShengWangValue(roleid, shenwang, reson);
		long addsw = 0;
		if(resultsw>0){
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleid, false);
			long realAdd = bag.addSysCurrency(resultsw,MoneyType.MoneyType_ShengWang, "awardID:" + 0,tujing, 0);
			//没有任务，计算相关援助统计
			if(realAdd > 0){
				addsw = realAdd;
				//MessageUtil.pSendAddOrRemoveCurrency(roleid, realAdd,MoneyType.MoneyType_ShengWang);
				java.util.List<String> para = new LinkedList<String>();
				para.add(String.valueOf(realAdd));
				if(type == 1) {
					MessageMgr.psendMsgNotifyWhileCommit(roleid, 166125, para);
				} else if(type == 2){
					MessageMgr.psendMsgNotifyWhileCommit(roleid, 166124, para);
				}
			} else {
				Module.logger.error("援助奖励，帮公会成员:addSysCurrency shengwang error!realAdd=" + realAdd);
			}
		}
    }
    
    

}
