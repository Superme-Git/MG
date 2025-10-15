

package fire.pb.mission.treasuremap;

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.concurrent.TimeUnit;

import fire.log.Logger;
import fire.pb.PAddExpProc;
import fire.pb.activity.timernpc.TimerNpcService;
import fire.pb.battle.PNewBattle;
import fire.pb.item.ItemShuXing;
import fire.pb.item.SDirectUseItem;
import fire.pb.main.ConfigManager;
import fire.pb.map.FindTreasureGift;
import fire.pb.map.SceneManager;
import fire.pb.mission.SPlayXianJingCG;
import fire.pb.skill.Module;
import fire.pb.skill.scene.PSceneSkill;
import fire.pb.skill.scene.SceneSkillConfig;
import fire.pb.talk.MessageMgr;
import fire.pb.util.BagUtil;
import fire.pb.util.MessageUtil;
import mkdb.Procedure;

public class PEndDigTreasureMap extends Procedure  {
	private final long roleId;
	
	static Logger logger = Logger.getLogger("TASK");
	
	public PEndDigTreasureMap(final long roleId) {
		this.roleId = roleId;
	}
	
	@Override
	protected boolean process() throws Exception {
		xbean.TreasureMapInfo info = xtable.Opentreasuremaptable.get(roleId);
		if (info == null) {
			return false;
		}
		
		int awardType = info.getAwardid(); //奖励id或者事件id
		if (awardType == -1)
			return false;
		
		fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
		int suc = bag.removeItemWithKey(info.getItemkey(), 1, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_wabaouse, 0, "Treasuremap used success");
		if (suc != 1) {
			return false;
		}
		
		if (awardType > 10) { //发物品
			FindTreasureGift TreasureGift = BaoTuMapManager.getInstance().mapGift.get(info.getTableindex());
			int realAdd = BagUtil.addItem(roleId, awardType, 1, "treasuremap", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_wabao, awardType);
			if (realAdd == 1) {
				ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(awardType);
				if (attr == null) {
					return false;
				}
				
				MessageMgr.psendMsgNotifyWhileCommit(roleId, TreasureGift.getItemMessageId, null);
				
				NavigableMap<Integer, SDirectUseItem> sDircUseItem = ConfigManager.getInstance().getConf(SDirectUseItem.class);
				SDirectUseItem sUseItem = sDircUseItem.get(awardType);
				if (sUseItem == null) {
					MessageUtil.psendAddItemWhileCommit(roleId, awardType, realAdd);
				}
				
				if (attr.rare == 1) {
					List<String> param = BaoTuMapManager.getInstance().getItemNotice(awardType, roleId);
					SceneManager.sendAll(MessageMgr.getMsgNotify(TreasureGift.noticeId, 0, param));
				}
				
//				if (TreasureGift.noticeItemList != null) {
//					String [] str = TreasureGift.getNoticeItemList().split(";");
//					for (String strTmp : str) {
//						if (awardType == Integer.parseInt(strTmp)) {
//							List<String> param = TreasureMapManager.getInstance().getItemNotice(awardType, roleId);
//							SceneManager.sendAll(Message.getMsgNotify(TreasureGift.noticeId, 0, param));
//						}
//					}
//				}
			}
			
			checkNextId(info.getItemid());
		}
		else if (awardType == 0) { //挖到金钱
			FindTreasureGift TreasureGift = BaoTuMapManager.getInstance().mapGift.get(info.getTableindex());
			//发银币
			fire.pb.activity.award.RewardMgr
					.getInstance().distributeAllAward(roleId, TreasureGift.getMoneyAwardId(), null,
							fire.log.enums.YYLoggerTuJingEnum.tujing_Value_wabao, TreasureGift.getMoneyAwardId(),
							PAddExpProc.OTHER_QUEST, "藏宝图奖励");
			checkNextId(info.getItemid());
		}
		
		else if(awardType < 10) { //触发事件
			GiftByEvent eventAward = BaoTuMapManager.getInstance().getEventGift(awardType);
			if (eventAward == null) {
				logger.error(roleId + ",触发事件id错误:" + awardType);
				return false;
			}
			logger.info(roleId + ",触发事件:" + eventAward.name);
			switch (eventAward.type) {
			case 1: //刷怪
				if (eventAward.group == 0) {
					logger.error("事件刷怪组没有配置," + eventAward.name);
					return false;
				}
				
				EventTimerNpcData eventData = BaoTuMapManager.getInstance().getEventTimerNpcData(eventAward.group);
				if (eventData == null) {
					logger.error("事件刷怪组数据读取错误," + eventAward.name);
					return false;
				}
				
				EventTimerGroupData eventGroupData = BaoTuMapManager.getInstance().getEventTimerGroupData(eventData, roleId);
				if (eventGroupData == null) {
					logger.error("事件刷怪组详细数据读取错误," + eventAward.name);
					return false;
				}
				
				if (eventGroupData.delaysec > 0) {
					mkdb.Executor.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							TimerNpcService.getInstance().createTimerNpcByData(eventGroupData, eventAward.noticeId, roleId);
						}
					}, eventGroupData.delaysec, TimeUnit.SECONDS);
					
					List<String> param = new ArrayList<String>(1);
					param.add(String.valueOf(eventGroupData.delaysec));
					SceneManager.sendAll(MessageMgr.getMsgNotify(eventGroupData.delaynoticeid, 0, param));
				}
				else {
					TimerNpcService.getInstance().createTimerNpcByData(eventGroupData, eventAward.noticeId, roleId);
				}
				
				MessageMgr.psendMsgNotifyWhileCommit(roleId, eventAward.messageId, null);
				checkNextId(info.getItemid());
				break;
			case 2: //进入战斗
				MessageMgr.psendMsgNotifyWhileCommit(roleId, eventAward.messageId, null);
				Procedure.pexecuteWhileCommit(new PNewBattle(roleId, eventAward.battleId, new BaotuBattleEndHandler(eventAward.getAwardId())));
				break;
			case 3: //遭遇陷阱
				SceneSkillConfig skillconfig = fire.pb.skill.Module.getInstance().getSceneSkillConfig(eventAward.skillId);
				if(skillconfig == null)
				{
					Module.logger.error("ERROR: Scene Skill is not exist. roleId = "+roleId+", skillId = " + eventAward.skillId);
					break;
				}
				Procedure.pexecuteWhileCommit(new PSceneSkill(roleId, eventAward.skillId, roleId, 1));
				MessageMgr.psendMsgNotifyWhileCommit(roleId, eventAward.messageId, null);
				SPlayXianJingCG xianjingCg = new SPlayXianJingCG();
				Procedure.psendWhileCommit(roleId, xianjingCg);
				checkNextId(info.getItemid());
				break;
			default:
				break;
			}
		}
		
		info.setAwardid(-1);
		fire.pb.course.CourseManager.achieveUpdate(roleId, fire.pb.course.CourseType.WABAO_COURSE);
		return true;
	}
	
	
	public void checkNextId(int itemid) {
		if (itemid == 331300) {
			fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
			//331300, 331301
			int num = bag.getBagItemNum(331300);
	        if (0 < num) {
	        	ItemShuXing attr = fire.pb.item.Module.getInstance().getItemManager().getAttr(331300);
	        	List<String> para = new ArrayList<String>(1);
	        	para.add(attr.getName());
	        	MessageMgr.psendMsgNotifyWhileCommit(roleId, 150191, para);
	            return;
	        }
		}
	}

}
