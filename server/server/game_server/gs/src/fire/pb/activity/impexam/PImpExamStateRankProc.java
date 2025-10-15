package fire.pb.activity.impexam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fire.pb.PAddExpProc;
import fire.pb.activity.award.RewardMgr;
import fire.pb.activity.award.RewardObjs;
import fire.pb.battle.pvp5.PvP5Helper;
import fire.pb.game.SImperialExamStateRankReward;
import fire.pb.item.Pack;
import fire.pb.main.ConfigManager;
import fire.pb.talk.MessageMgr;
import fire.pb.title.STitleConfig;
import fire.pb.title.Title;
import mkdb.Procedure;
import xbean.Pod;


//计算智力试练全国考试前十名
public class PImpExamStateRankProc extends Procedure{
	
	private Map<Integer, SImperialExamStateRankReward> ieStateConf = ConfigManager.getInstance().getConf(SImperialExamStateRankReward.class);
	
	public PImpExamStateRankProc(){

	}

	@Override
	protected boolean process() throws Exception {
		xbean.ImpExamStateRecord iesRecord = null;
		iesRecord = xtable.Impexamstatetbl.get(1);
		if(iesRecord == null){
			iesRecord = Pod.newImpExamStateRecord();
			xtable.Impexamstatetbl.insert(1, iesRecord);
		}
		
		java.util.List<xbean.ImpExamStatePassRole> rankList = new ArrayList<xbean.ImpExamStatePassRole>();
		rankList.addAll(iesRecord.getPassrolelist());
		if(rankList.size() > 1){
			Collections.sort(rankList, new ImpExamStateComparator());
		}
		
		for (int i = 0; i < rankList.size(); i++){
			xbean.ImpExamStatePassRole iesRecReward = rankList.get(i);
			long roleId = iesRecReward.getRoleid();
			
			java.util.List<String> rankRewardStrings = new java.util.ArrayList<String>();

			SImperialExamStateRankReward sIesRankRewardData = null;
			int rewardId = 0;
			int rIndex = 0;
			if(i<10){
				sIesRankRewardData = ieStateConf.get(i + 1);
				rewardId = sIesRankRewardData.getRewardid1();
				rIndex = i + 1;
			}else{
				sIesRankRewardData = ieStateConf.get(11);
				rewardId = sIesRankRewardData.getRewardid1();
				rIndex = 11;
			}
			// 发奖时每个人单独处理,避免锁一群人
			mkdb.Procedure proc = createAwardProc(roleId, sIesRankRewardData.getTitleid(), rIndex, rewardId);
			mkdb.Procedure.pexecuteWhileCommit(proc);
			
			rankRewardStrings.add(String.valueOf(i + 1));
			fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 190035, rankRewardStrings);
		}

		java.util.List<String> rankNameList = new ArrayList<String>();
		String roleName0 = null;
		if(rankList.size() > 0){
			xbean.ImpExamStatePassRole iesRecReward0 = rankList.get(0);
			if(iesRecReward0 != null){
				long roleId0 = iesRecReward0.getRoleid();
				roleName0 = xtable.Properties.selectRolename(roleId0);
				//rankNameList.add(0, roleName0);
			}
		}
		String roleName1 = null;
		if(rankList.size() > 1){
			xbean.ImpExamStatePassRole iesRecReward1 = rankList.get(1);
			if(iesRecReward1 != null){
				long roleId1 = iesRecReward1.getRoleid();
				roleName1 = xtable.Properties.selectRolename(roleId1);
				//rankNameList.add(1, roleName1);
			}
		}
		String roleName2 = null;
		if(rankList.size() > 2){
			xbean.ImpExamStatePassRole iesRecReward2 = rankList.get(2);
			if(iesRecReward2 != null){
				long roleId2 = iesRecReward2.getRoleid();
				roleName2 = xtable.Properties.selectRolename(roleId2);
				//rankNameList.add(2, roleName2);
			}
		}
		if(rankList.size() > 0){
			if(roleName2 != null && roleName1 != null && roleName0 != null){
				rankNameList.add(0, roleName0 + ",");
				rankNameList.add(1, roleName1 + ",");
				rankNameList.add(2, roleName2);
			}else if(roleName2 == null && roleName1 != null && roleName0 != null){
				rankNameList.add(0, roleName0 + ",");
				rankNameList.add(1, roleName1);
				rankNameList.add(2, "");
			}else if(roleName2 == null && roleName1 == null && roleName0 != null){
				rankNameList.add(0, roleName0);
				rankNameList.add(1, "");
				rankNameList.add(2, "");
			}
			//必须有玩家排名才提示
			MessageMgr.sendSystemMsg(190008,rankNameList);
		}
		
		return true;
	}
	
	/**
	 * 创建角色奖励的处理
	 * @param roleId
	 * @param titleId
	 * @param rIndex
	 * @param rewardId
	 * @return
	 */
	private mkdb.Procedure createAwardProc(long roleId, int titleId, int rIndex, int rewardId) {
		mkdb.Procedure proc = new mkdb.Procedure() {
			@Override
			protected boolean process() {
				Map<String, Object> paras = new HashMap<String, Object>();
				// add award
				Map<Integer, RewardObjs> result = RewardMgr.getInstance().distributeAllAward(roleId, rewardId, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_zhihuishilian, 0,
						PAddExpProc.OTHER_QUEST, "智慧试练", false);
				
				if(rIndex <= 10 && titleId > 0){
					//前10名才有称号
					// add title
					Title title = new Title(roleId, true);
					if (!title.roleHaveTitle(titleId))
						Title.addTitle(roleId, titleId, "", -1);
				}
				return true;
			}
		};
		return proc;
	}
	
}
