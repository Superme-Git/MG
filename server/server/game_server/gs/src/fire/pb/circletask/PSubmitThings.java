package fire.pb.circletask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.HelpCountManage;
import fire.pb.PAddClanPointProc;
import fire.pb.PAddExpProc;
import fire.pb.circletask.anye.RoleAnYeTask;
import fire.pb.circletask.anye.SubmitThing;
import fire.pb.clan.ClanUtils;
import fire.pb.common.SCommon;
import fire.pb.game.MoneyType;
import fire.pb.item.EquipItem;
import fire.pb.item.FoodItem;
import fire.pb.item.ItemBase;
import fire.pb.main.ConfigManager;
import fire.pb.mission.Module;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.util.MessageUtil;

public class PSubmitThings extends mkdb.Procedure{
	private int taskPos; //暗夜马戏团任务pos位置
	private long submitRoleId;
	private long taskRoleId;
	private ArrayList<SubmitThing> things = new ArrayList<SubmitThing>();
	
	public static Map<Integer, SCommon> commonMap = ConfigManager.getInstance().getConf(SCommon.class);
	
	public PSubmitThings(long submitroleid, long taskroleid, ArrayList<SubmitThing> things, int taskpos){
		this.submitRoleId = submitroleid;
		this.taskRoleId = taskroleid;
		if (things != null && things.size() > 0)
			this.things = things;
		this.taskPos = taskpos;
	}
	
	private void handleHelp(){
		Long taskRoleFaction = 0L;
		taskRoleFaction = xtable.Properties.selectClankey(taskRoleId); //任务所有人的公会id
		Long submitRoleFaction = 0L;
		submitRoleFaction = xtable.Properties.selectClankey(submitRoleId); //提交人公会id
		
		if(taskRoleFaction > 0L && submitRoleFaction > 0L && submitRoleFaction.equals(taskRoleFaction)) {
			//同一公会 公会援助
//			final fire.pb.PropRole psubmitrole = new fire.pb.PropRole(submitRoleId, false);
			long addexp = HelpCountManage.getAddExpValueByHelpItem(submitRoleId);
//			long resultexp=HelpCountManage.getInstance().addExpValue(submitRoleId, 1, "马戏团公会援助奖励");
//			if(resultexp>0){
//				new fire.pb.PAddExpProc(submitRoleId, addexp, PAddExpProc.OTHER,"马戏团公会援助").call();
//			}
			
			int addcount = 5;
			int resultvalue=HelpCountManage.getInstance().addFactionValue(submitRoleId, addcount, "马戏团公会援助奖励");
			if(resultvalue>0){
				new PAddClanPointProc(submitRoleId, resultvalue, YYLoggerTuJingEnum.tujing_Value_huodongjiangli,false, "马戏团公会援助").call();
			}
			HelpCountManage.getInstance().addFactionHelpNum(submitRoleId, "马戏团公会援助");
			//添加援助物品次数
			long result=fire.pb.HelpCountManage.getInstance().addHelpGiveItemNum(submitRoleId, 1, "马戏团援助添加援助物品次数");
			if(result>0){
				new fire.pb.PAddExpProc(submitRoleId, addexp, PAddExpProc.OTHER,"马戏团世界援助").call();
			}
			//添加声望奖励
			fire.pb.HelpCountManage.getInstance().addShengWangToRole(submitRoleId,"马戏团援助添加",YYLoggerTuJingEnum.tujing_Value_huodongjiangli, 1);
			
		} else {
			//不同公会 世界援助
//			final fire.pb.PropRole psubmitrole = new fire.pb.PropRole(submitRoleId, false);
			long addexp = HelpCountManage.getAddExpValueByHelpItem(submitRoleId);
//			long resultexp=HelpCountManage.getInstance().addExpValue(submitRoleId, 1, "马戏团世界援助奖励");
//			if(resultexp>0){
//				new fire.pb.PAddExpProc(submitRoleId, addexp, PAddExpProc.OTHER,"马戏团世界援助").call();
//			}
			//添加援助物品次数
			long result=fire.pb.HelpCountManage.getInstance().addHelpGiveItemNum(submitRoleId, 1, "马戏团援助添加援助物品次数");
			if(result>0){
				new fire.pb.PAddExpProc(submitRoleId, addexp, PAddExpProc.OTHER,"马戏团世界援助").call();
			}
			//添加声望奖励
			fire.pb.HelpCountManage.getInstance().addShengWangToRole(submitRoleId,"马戏团援助添加",YYLoggerTuJingEnum.tujing_Value_huodongjiangli, 1);
		}
		//添加求助物品次数
		fire.pb.HelpCountManage.getInstance().addHelpItemNum(taskRoleId, 1, "马戏团援助添加求助物品次数");
		
		//替换求助信息
		String rolename = xtable.Properties.selectRolename(submitRoleId);
		RoleAnYeTask rayt = new RoleAnYeTask(taskRoleId);
		xbean.AnYeTask ayt = rayt.getTasks().get(taskPos);
		AnYeMaXiTuanConf conf = ConfigManager.getInstance().getConf(AnYeMaXiTuanConf.class).get(ayt.getId());
		if (conf != null) {
			fire.pb.talk.MessageMgr.sendHelpMsg(taskRoleId, taskPos, conf.getTaskname(), rolename);
		}
	}
	
	private boolean checkLevel() {
		if(submitRoleId != taskRoleId){
			int submiterLv = xtable.Properties.selectLevel(submitRoleId);
			//int taskerLv = xtable.Properties.selectLevel(taskRoleId);
			if(submiterLv < getGiveHelpLevel())
				return false;
		}
		return true;
	}
	
	public boolean process(){
		Long teamid = xtable.Roleid2teamid.select(submitRoleId);
		if(teamid != null)
			lock(mkdb.Lockeys.get(xtable.Locks.TEAMLOCK, Arrays.asList(teamid)));
		
		if (things.isEmpty()){
			return false;
		}
		else{
			boolean isHelp = false;
			//先扣submitRoleId的物品，然后完成taskRoleId的任务
			RoleAnYeTask rayt = new RoleAnYeTask(taskRoleId);
			xbean.AnYeTask ayt = rayt.getTasks().get(taskPos);
			if (ayt == null)
				return false;
			if(submitRoleId == taskRoleId) {
				if (ayt.getState() != SpecialQuestState.DONE)
					return false;
			} else {
				isHelp = true;
				if(checkLevel() == false){
					fire.pb.talk.MessageMgr.sendMsgNotify(submitRoleId, 166090, null);
					return false;
				}
				if (ayt.getState() == SpecialQuestState.SUCCESS) {
					fire.pb.talk.MessageMgr.sendMsgNotify(submitRoleId, 166104, null);
					return false;
				}
			}
			
			if (ayt.getKind() == CircTaskClass.CircTask_ItemFind) {
				int itemfindid = (int)ayt.getParam1();//这里获取的是寻找物品子表的索引id
				CircTaskItemFind ctif =  CircleTaskManager.getCircTaskItemFindConf(itemfindid);
				if (null != ctif) {
					if (1 == ctif.needquality) {
						//需要品质，寻找物品(生活技能)
						final fire.pb.PropRole role = new fire.pb.PropRole(taskRoleId, true);
						int roleLevel = role.getLevel();
						//计算出目标品质
						int targetquality = roleLevel * ctif.qualitya / 1000 + ctif.qualityb;
						RoleAnYeTask.logger.debug("角色[" + taskRoleId + "]当前计算需求品质为:" + targetquality + ".");
						boolean isdouble = false;
						for (SubmitThing unit : things) {
							final fire.pb.item.ItemMaps bag = fire.pb.item.Module
									.getInstance().getItemMaps(submitRoleId, fire.pb.item.BagTypes.BAG, false);
							int curitemkey = unit.key;
							int curitemnum = unit.num;
							ItemBase targetitem = bag.getItem(curitemkey);
							if(null != targetitem){
								if(targetitem.getItemId() != ayt.getDstitemid() &&
										false == CircleTaskManager.isItem2Item(ayt.getDstitemid(), targetitem.getItemId())){
									RoleAnYeTask.logger.info("调试用消息： 任务id"+ayt.getId()+"任务类型:"+ayt.getKind()+"提交物品id不对！");
									return false;
									}
								if(targetitem instanceof EquipItem){
									EquipItem submititem = (EquipItem)targetitem;
									if(submititem.getEquipAttr().getDiamonds().size() > 0){
										fire.pb.talk.MessageMgr.sendMsgNotify(submitRoleId, 166001, null);
										RoleAnYeTask.logger.info("调试用消息： 任务id"+ayt.getId()+"任务类型:"+ayt.getKind()+"提交装备有镶嵌宝石,禁止提交！");
										return false;
									}
								}
								if(targetitem instanceof FoodItem){
									FoodItem submititem = (FoodItem)targetitem;
									int curquality = submititem.getQuality();
									if(targetquality <= curquality){
										//记录物品品质，加成奖励
										isdouble = true;
									} else {
										isdouble = false;
									}
									if (bag.removeItemWithKey(curitemkey, curitemnum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 7, "Submit Item For SpecialQuest") != curitemnum){
										RoleAnYeTask.logger.info("调试用消息： 任务 id ：" + ayt.getId() +"任务类型:"+ayt.getKind()+"  删除道具出错!!!");
										return false;
									}
								}
							}					
						}
						rayt.handleTaskOver(taskPos, isdouble);
						if(isHelp){
							handleHelp();
							String submitRoleName = xtable.Properties.selectRolename(submitRoleId);
							if(submitRoleName != null){
								AnYeMaXiTuanConf conf = ConfigManager.getInstance().getConf(AnYeMaXiTuanConf.class).get(ayt.getId());
								if(conf!= null)
									fire.pb.talk.MessageMgr.psendMsgNotify(taskRoleId, 166085, java.util.Arrays.asList(submitRoleName, conf.getTaskname()));
							}
						}
						return true;				
					} else if (0 == ctif.needquality) {
						//不需要品质，寻找物品
						for (SubmitThing unit : things) {
							final fire.pb.item.ItemMaps bag = fire.pb.item.Module
									.getInstance().getItemMaps(submitRoleId,fire.pb.item.BagTypes.BAG, false);
							int curitemkey = unit.key;
							int curitemnum = unit.num;
							ItemBase targetitem = bag.getItem(curitemkey);
							if(null != targetitem){
								if(targetitem.getItemId() != ayt.getDstitemid() &&
										false == CircleTaskManager.isItem2Item(ayt.getDstitemid(), targetitem.getItemId())){
									RoleAnYeTask.logger.info("调试用消息： 任务id"+ayt.getId()+"任务类型:"+ayt.getKind()+"提交物品id不对！");
									return false;
								}
								if(targetitem instanceof EquipItem){
									EquipItem submititem = (EquipItem)targetitem;
									if(submititem.getEquipAttr().getDiamonds().size() > 0){
										fire.pb.talk.MessageMgr.sendMsgNotify(submitRoleId, 166001, null);
										RoleAnYeTask.logger.info("调试用消息： 任务id"+ayt.getId()+"任务类型:"+ayt.getKind()+"提交装备有镶嵌宝石,禁止提交！");
										return false;
									}
								}
								if (bag.removeItemWithKey(curitemkey, curitemnum, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_use, 7, "Submit Item For SpecialQuest") != curitemnum){
									RoleAnYeTask.logger.info("调试用消息： 任务 id ：" + ayt.getId() +"任务类型:"+ayt.getKind()+"  删除道具出错!!!");
									return false;
								}
							}					
						}
						rayt.handleTaskOver(taskPos);
						if(isHelp){
							handleHelp();
							String submitRoleName = xtable.Properties.selectRolename(submitRoleId);
							AnYeMaXiTuanConf conf = ConfigManager.getInstance().getConf(AnYeMaXiTuanConf.class).get(ayt.getId());
							if(conf!= null)
								fire.pb.talk.MessageMgr.psendMsgNotify(taskRoleId, 166085, java.util.Arrays.asList(submitRoleName, conf.getTaskname()));
						}
						return true;
					}
				}
			
			} else if (ayt.getKind() == CircTaskClass.CircTask_PetCatch) {
				final fire.pb.pet.PetColumn petcol = new fire.pb.pet.PetColumn(submitRoleId, PetColumnTypes.PET, false);
				int dstpetid = ayt.getDstitemid();
				final xbean.PetInfo xpet = petcol.getPetInfo(things.get(0).key);//petcol.getPetInfoById(dstpetid);	  
				if (null == xpet || 
						(xpet.getId() != dstpetid && false == CircleTaskManager.isItem2Item(dstpetid, xpet.getId()))){
					RoleAnYeTask.logger.info("调试用消息： 任务id"+ayt.getId()+"任务类型:"+ayt.getKind()+"提交宠物id出错");
					return false;
				} else {
					if(petcol.removePet(xpet.getKey(), PetColumn.REMOVE_REASON_TASK, ayt.getId()) < 0) {
						RoleAnYeTask.logger.info("调试用消息： 任务id"+ayt.getId()+"任务类型:"+ayt.getKind()+"提交宠物扣除出错!");
						return false;
					}
					rayt.handleTaskOver(taskPos);
					if(isHelp){
						handleHelp();
						String submitRoleName = xtable.Properties.selectRolename(submitRoleId);
						AnYeMaXiTuanConf conf = ConfigManager.getInstance().getConf(AnYeMaXiTuanConf.class).get(ayt.getId());
						if(conf!= null)
							fire.pb.talk.MessageMgr.psendMsgNotify(taskRoleId, 166085, java.util.Arrays.asList(submitRoleName, conf.getTaskname()));
					}
					return true;
				}
			}
			return false;
		}
	}
	
	/**
	 * 获得捐献等级
	 * @return
	 */
	public static int getGiveHelpLevel() {
		if (commonMap != null) {
			SCommon conf = commonMap.get(335);
			if (conf != null) {
				return Integer.parseInt(conf.getValue());
			}
		}
		return 30;
	}
	
	
}
