
package fire.pb.battle.livedie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mkdb.Procedure;

import org.apache.log4j.Logger;

import xbean.BattleInfo;
import xbean.Fighter;
import xbean.PetInfo;
import fire.pb.PAddExpProc;
import fire.pb.PropRole;
import fire.pb.activity.award.RewardMgr;
import fire.pb.attr.AttrType;
import fire.pb.attr.SRefreshPetData;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.battle.BattleCameraInfo;
import fire.pb.battle.BattleEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.battle.BattleType;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.item.SRefreshNaiJiu;
import fire.pb.main.ConfigManager;
import fire.pb.map.Transfer;
import fire.pb.move.SRoleEnterScene;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
import fire.pb.title.STitleConfig;
import fire.pb.title.Title;
import fire.pb.util.StringConstant;
import gnet.link.Onlines;

public class LiveDieEndHandler extends BattleEndHandler {
	public static final Logger logger = Logger.getLogger("BATTLE");
	
	public boolean handleDeath() {
		// 切磋其实没有任何惩罚,但是要把人物状态恢复原样
		Collection<xbean.Fighter> fighters = battle.getFighters().values();
		List<xbean.Fighter> hostFighters = new ArrayList<Fighter>();
		List<xbean.Fighter> guestFighters = new ArrayList<Fighter>();
		long guestLeader = 0;
		// 血和魔全部还原
		for (xbean.Fighter f : fighters) {
			if (f.getFightertype() == Fighter.FIGHTER_ROLE) {
				xbean.Properties prop = xtable.Properties.get(f.getUniqueid());
				prop.setHp(f.getInihp());
				prop.setMp(f.getInimp());
				prop.setSp(f.getInisp());

				fire.pb.battle.Fighter fighterObject = battle.getFighterobjects().get(f.getBattleindex());
				SRefreshRoleData srrd = new SRefreshRoleData();
				srrd.datas.put(AttrType.HP, (float) f.getInihp());
				srrd.datas.put(AttrType.MP, (float) f.getInimp());
				srrd.datas.put(AttrType.SP, (float) f.getInisp());
				srrd.datas.put(AttrType.UP_LIMITED_HP, (float) fighterObject.getEffectRole().getUplimithp());
				Onlines.getInstance().send(f.getUniqueid(), srrd);
			}
			if (f.getFightertype() == Fighter.FIGHTER_PET) {
				int masterIndex = f.getBattleindex() - 5;
				int petkey = new PropRole(battle.getFighters().get(masterIndex).getUniqueid(), true).getFightpetkey();
				recoverPetHpMPWithSP(battle, f.getBattleindex(),petkey);
			}
			if (1 <= f.getBattleindex() && f.getBattleindex() <= BattleField.A_SIDE_FIGHTER_NUM) {
				if (f.getFightertype() == Fighter.FIGHTER_ROLE)
					hostFighters.add(f);
			} else if (f.getFightertype() == Fighter.FIGHTER_ROLE){
				guestFighters.add(f);
				if (f.getBattleindex() == BattleField.GUEST_FIRST_ID) 
					guestLeader = f.getUniqueid();
			}

		}
		long hostroleid = battle.getInifighters().get(BattleField.HOST_FIRST_ID).getUniqueid();
		long guestroleid = battle.getInifighters().get(BattleField.GUEST_FIRST_ID).getUniqueid();
		String hostname=new PropRole(hostroleid,true).getName();
		String guestname=new PropRole(guestroleid,true).getName();
		
		
		switch (battle.getBattleresult()) {
		case BattleField.BATTLE_WIN:
//			handleLiveDieWin(hostFighters, guestFighters, battle,true);
			//退钱给下战书的人
			fire.pb.item.Pack bag = new fire.pb.item.Pack(hostroleid, false);
			int cost=LiveDieMange.getLiveDieCostMoney();
			bag.addSysMoney(cost, "生死战退回押金", fire.log.enums.YYLoggerTuJingEnum.tujing_Value_shengsizhanyajin, 0);
			//给好友提示 这个只有应战方有
			MessageMgr.psendSystemMessageToRole(hostroleid, 162085, null);
			dealTitle(guestFighters,hostname);//处理称号
			dealAward(hostFighters);//处理经验
			//处理耐久
			dealLostEquipEndure(guestFighters);
			//系统提示
			MessageMgr.sendSystemMsg(162069,Arrays.asList(hostname,hostname,guestname));
			break;
		case BattleField.BATTLE_LOSE:
		case BattleField.BATTLE_DRAW://平局算下战书的人输
//			handleLiveDieWin(guestFighters, hostFighters, battle,false);
			dealTitle(hostFighters,guestname);//处理称号
			dealAward(guestFighters);//处理经验
			//处理耐久
			dealLostEquipEndure(hostFighters);
			//系统提示
			MessageMgr.sendSystemMsg(162069,Arrays.asList(guestname,guestname,hostname));
			break;
		default:
			break;
		}
		//处理生死战结束后的逻辑
		//删除挑战记录
		LiveDieMange.removePlayerLiveDieInfoBattleEnd(hostroleid);
		
		//移除战斗记录
		LiveDieMange.liveDieRoleids.remove(hostroleid);
		LiveDieMange.liveDieFightRoleids.remove(hostroleid);
		
		
		//这里处理传送
		long failroleid=0;
		if(battle.getBattleresult()==BattleField.BATTLE_LOSE||battle.getBattleresult()==BattleField.BATTLE_DRAW){
			failroleid=battle.getInifighters().get(BattleField.HOST_FIRST_ID).getUniqueid();
		}else{
			failroleid=battle.getInifighters().get(BattleField.GUEST_FIRST_ID).getUniqueid();
		}
		//这里处理一下生死战的传送问题
		//这里顺便处理一下这些玩家传送到指定地图
		Team team=TeamManager.selectTeamByRoleId(failroleid);
		if(team!=null){
			for(long id:team.getReturningMemberIds()){
				LiveDieEndHandler.forceLeave(id);
			}
			LiveDieEndHandler.forceLeave(failroleid);
		}else{
			LiveDieEndHandler.forceLeave(failroleid);
		}
	
		
		
		return true;
	}
	
	
	/**
	 * 生死战奖励
	 * @param fighters
	 */
	public void dealAward(List<Fighter> fighters){
		for (xbean.Fighter f : fighters) {
			long roleid = f.getUniqueid();
			int awardId=LiveDieMange.getLiveDieAwardId();
			Map<String, Object> paras = new HashMap<String, Object>();
//			paras.put(RewardMgr.IS_LEADER,  1);
			RewardMgr.getInstance().distributeAllAward(roleid, awardId, paras, fire.log.enums.YYLoggerTuJingEnum.tujing_Value_livediebattle, awardId, PAddExpProc.OTHER, "生死战奖励" + awardId);
		}
	}
	
	
	
	/**
	 * 处理称谓
	 * @param fighters
	 * @param name
	 */
	public void dealTitle(List<Fighter> fighters,String name){
		Map<Integer, STitleConfig> titiledatamap = ConfigManager.getInstance().getConf(fire.pb.title.STitleConfig.class);
		for (xbean.Fighter f : fighters) {
			long roleid = f.getUniqueid();
			Title title = new Title(roleid, false);
			String titleName = "";
			int new_titleid = LiveDieMange.getLiveDieTitle();
			STitleConfig titledata = titiledatamap.get(new_titleid);
			if (titledata != null) {
				titleName = name+StringConstant.ID2String("的", StringConstant.的)+titledata.titlename;
				if (!title.roleHaveTitle(new_titleid)) {
					title.addTitle(new_titleid, titleName, -1);
				}
				if (title.roleHaveTitle(new_titleid)) {
					xbean.Properties prop = xtable.Properties.get(roleid);
					xbean.TitleInfo value = prop.getTitles().get(new_titleid);
					if (!titleName.equals(value.getTitlename())) {
						title.removeTitle(new_titleid);
						title.addTitle(new_titleid, titleName, -1);
					}
				}
			}
		}

		
	
	
	}
	
	
	
	/**
	 * 失败方损失耐久
	 * @param fighters
	 */
	public void dealLostEquipEndure(List<Fighter> fighters){
		for (xbean.Fighter f : fighters) {
			long roleid = f.getUniqueid();
//			//这里顺便处理一下这些玩家传送到指定地图
//			transformRoleids.add(roleid);
			
			fire.pb.item.Equip equip = (fire.pb.item.Equip) fire.pb.item.Module.getInstance().getItemMaps(roleid, fire.pb.item.BagTypes.EQUIP, false);
			if (equip == null){
				return;
			}

			SRefreshNaiJiu send = new SRefreshNaiJiu();
			send.packid = fire.pb.item.BagTypes.EQUIP;
			int times = LiveDieMange.getLiveDieLoseDurable();
			
			for (fire.pb.item.ItemBase bi : equip) {
				if (bi instanceof fire.pb.item.EquipItem) {
					fire.pb.item.EquipItem eitem = (fire.pb.item.EquipItem)bi;
					if (eitem.getEndure() <= 0) {
						continue;
					}
					
					if (times != 0) {
						int before = eitem.getEquipAttr().getEndure();
						final int lostEndure = Math.max(5, eitem.getCurMaxEndure() * times);
						if (eitem.loseEndure(lostEndure)) {
							if (before != eitem.getEquipAttr().getEndure()) {
								fire.pb.item.EquipNaiJiu endure = new fire.pb.item.EquipNaiJiu();
								endure.keyinpack = bi.getKey();
								endure.endure = eitem.getExtInfo().getEndure();
								send.data.add(endure);
							}
						}
					}
				}
			}
			//您因死亡损失了10%的装备耐久
//			MessageMgr.psendMsgNotifyWhileCommit(roleid, 162084, Arrays.asList(times+""));
			MessageMgr.psendSystemMessageToRole(roleid, 162084, Arrays.asList(times+""));
			MessageMgr.psendSystemMessageToRole(roleid, 162086, null);
			
			mkdb.Procedure.psendWhileCommit(roleid, send);
			
			fire.pb.effect.RoleImpl erole = new fire.pb.effect.RoleImpl(roleid, false);
			erole.setSp(0);
			
		}
	}
	
	@Override
	protected boolean handleCamera() {
		long hostroleid = battle.getInifighters().get(BattleField.HOST_FIRST_ID).getUniqueid();
		long guestroleid = battle.getInifighters().get(BattleField.GUEST_FIRST_ID).getUniqueid();
		BattleField.outBattlevideoProcess(battle,(byte)BattleCameraInfo.CAMERA_TYPE_LIVEDIE,guestroleid, hostroleid);
		return true;
	}
	
	/**
	 * 处理生死战录像逻辑
	 * @param name
	 * @param battle
	 */
	public static void dealLiveDieEnd(String name,long hostroleid,long guestroleid,xbean.BattleInfo battle){
		try {
			if(name!=null){
				xbean.LDVideoRoleInfoDes  lDVideoRoleInfoDes = xbean.Pod.newLDVideoRoleInfoDes();//录像信息
				createLDRoleInfoDes(hostroleid,lDVideoRoleInfoDes.getRole1(),battle.getLivedieteam());//对手队长信息
				createLDRoleInfoDes(guestroleid,lDVideoRoleInfoDes.getRole2(),battle.getLivedieteam());//对手队长信息
				createLDTeamRoleInfoDeslist(hostroleid,lDVideoRoleInfoDes.getTeamlist1(),battle.getLivedieteam());//对手队伍成员信息
				createLDTeamRoleInfoDeslist(guestroleid,lDVideoRoleInfoDes.getTeamlist2(),battle.getLivedieteam());//对手队伍成员信息
				lDVideoRoleInfoDes.setBattleresult(battle.getBattleresult()); // 1胜利  0失败
				lDVideoRoleInfoDes.setRosenum(0); // 点赞次数
				lDVideoRoleInfoDes.setVideoid(name); // 录像id
				lDVideoRoleInfoDes.setSaveresult(0); // 0没有保存   1保存成功
				lDVideoRoleInfoDes.setFighttime(System.currentTimeMillis());
				LiveDieMange.ldvideoroleinfodeslistall.put(name, lDVideoRoleInfoDes);
				//处理一下排行榜
				mkdb.Procedure.pexecute(new PSaveLiveDieInfoProc(lDVideoRoleInfoDes));
				logger.info("生死战录像保存，双方对手id"+hostroleid+"\t"+guestroleid+"\t录像id"+name);
			}else{
				logger.error("生死战录像保存失败，双方对手id"+hostroleid+"\t"+guestroleid);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("生死战录像保存失败，双方对手id"+hostroleid+"\t"+guestroleid+e.toString());
		}
	}
	
	
	
	public static void createLDRoleInfoDes(long id,xbean.LDRoleInfoDes lDRoleInfoDes,int isteamfight){
		PropRole propRole = new PropRole(id, true);
		lDRoleInfoDes.setRoleid(propRole.getRoleId());//角色Id
		lDRoleInfoDes.setRolename(propRole.getName());//角色名
		lDRoleInfoDes.setShape(propRole.getShape());//角色造型
		lDRoleInfoDes.setLevel(propRole.getLevel());//角色等级
		lDRoleInfoDes.setSchool(propRole.getSchool());//门派
		Team team = TeamManager.selectTeamByRoleId(id);
		if(team!=null&&isteamfight==1){
			//判断是否是单人生死战
			lDRoleInfoDes.setTeamnum(team.getTeamInfo().getMembers().size()+1);//队伍当前人数
			lDRoleInfoDes.setTeamnummax(TeamManager.MAX_MEMBER_COUNT+1);//队伍最大人数
		}else{
			lDRoleInfoDes.setTeamnum(0);
			lDRoleInfoDes.setTeamnummax(0);
		}
	}
	
	public static void createLDTeamRoleInfoDeslist(long id,java.util.List<xbean.LDTeamRoleInfoDes> list,int isteamfight){
		Team team = TeamManager.selectTeamByRoleId(id);
		if(team!=null&&isteamfight==1){
			for(long roleid:team.getAllMemberIds()){
				PropRole propRole = new PropRole(roleid, true);
				xbean.LDTeamRoleInfoDes lDTeamRoleInfoDes = xbean.Pod.newLDTeamRoleInfoDes();//对手队伍成员信息
				lDTeamRoleInfoDes.setRoleid(propRole.getRoleId());//角色Id
				lDTeamRoleInfoDes.setRolename(propRole.getName());//角色名
				lDTeamRoleInfoDes.setShape(propRole.getShape());//角色造型
				lDTeamRoleInfoDes.setLevel(propRole.getLevel());//角色等级
				lDTeamRoleInfoDes.setSchool(propRole.getSchool());//门派
				list.add(lDTeamRoleInfoDes);
			}
		}
	}
	
	
	
	
	
	
	
	@Override
	protected void transformAfterBattle() {
//		for (long roleId : transformRoleids) {
//			forceLeave(roleId);
//		}
	}
	
	/**
	 * 地图传送
	 * @param roleId
	 */
	public static void forceLeave(long roleId) {
//		Team team = TeamManager.selectTeamByRoleId(roleId);
//		if (team != null) {
//			if (team.isTeamMember(roleId) && team.isNormalMember(roleId)) {
//				return;
//			}
//		}
//		Transfer.justGotoWhileCommit(roleId, LiveDieMange.getLiveDieMap(), 38, 29, SRoleEnterScene.CHEFU);
		Transfer.justGotoRandomWhileCommit(roleId, LiveDieMange.getLiveDieMap(),36,26,55,45, SRoleEnterScene.CHEFU);
	}
	


	private void handleLiveDieWin(List<Fighter> hostFighters, List<Fighter> guestFighters, BattleInfo battle,boolean hostwin) {
		if (hostFighters == null || guestFighters == null){
			throw new IllegalArgumentException("切磋数据错误");
		}
		sendLiveDieMsg(hostFighters, guestFighters);// 发送消息
	}

	private void sendLiveDieMsg(List<Fighter> hostFighters, List<Fighter> guestFighters) {

		long hostroleid = 0;
		long guestroleid = 0;
		int hostMsgID = 0;
		int guestMsgID = 0;
		if (battle.getBattleresult() == BattleField.BATTLE_WIN) {
			hostroleid = battle.getInifighters().get(BattleField.HOST_FIRST_ID).getUniqueid();
			guestroleid = battle.getInifighters().get(BattleField.GUEST_FIRST_ID).getUniqueid();
		} else {
			hostroleid = battle.getInifighters().get(BattleField.GUEST_FIRST_ID).getUniqueid();
			guestroleid = battle.getInifighters().get(BattleField.HOST_FIRST_ID).getUniqueid();
		}
		if (TeamManager.isInTeam(hostroleid)) {
			if (TeamManager.isInTeam(guestroleid))
				hostMsgID = 120070;
			else{
				hostMsgID = 120068;
			}
			guestMsgID = 120069;
		} else {
			if (TeamManager.isInTeam(guestroleid)){
				hostMsgID = 120070;
			}else{
				hostMsgID = 120068;
			}
			guestMsgID = 120069;
		}
		sendToAllRoles(hostroleid, guestMsgID, guestFighters);
		sendToAllRoles(guestroleid, hostMsgID, hostFighters);
	}

	private void sendToAllRoles(long roleid, int msgID, List<Fighter> fighters) {

		PropRole role = new PropRole(roleid, true);
		int level = role.getLevel();
		String name = role.getName();
		String school = CircleTaskManager.getInstance().getSchoolName(role.getSchool());
		List<String> paras = new ArrayList<String>();
		paras.add(name);
		paras.add(String.valueOf(level));
		paras.add(school);
		for (xbean.Fighter f : fighters) {
			long guestid = f.getUniqueid();
			MessageMgr.sendMsgNotify(guestid, msgID, paras);
		}
	}

	@Override
	protected boolean handleAward() {

		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean handleOutBattle(int index,int petkey) throws Exception {
		xbean.Fighter f = battle.getFighters().get(index);
		long roleid = f.getUniqueid();
		if (f.getFightertype() == Fighter.FIGHTER_ROLE) {
			// 玩家的hpmp都恢复
			xbean.Properties prop = xtable.Properties.get(roleid);
			prop.setHp(f.getInihp());
			prop.setMp(f.getInimp());
			prop.setSp(f.getInisp());
			fire.pb.battle.Fighter fighterObject = battle.getFighterobjects().get(f.getBattleindex());
			SRefreshRoleData srrd = new SRefreshRoleData();
			srrd.datas.put(AttrType.HP, (float) f.getInihp());
			srrd.datas.put(AttrType.MP, (float) f.getInimp());
			srrd.datas.put(AttrType.SP, (float) f.getInisp());
			srrd.datas.put(AttrType.UP_LIMITED_HP, (float) fighterObject.getEffectRole().getUplimithp());
			Procedure.psendWhileCommit(roleid, srrd);
			// 宠物的也要恢复
			final int petIndex = f.getBattleindex() + 5;
			// 如果玩家的宠物也参战了
			if (battle.getFighters().containsKey(petIndex)) {
				recoverPetHpMPWithSP(battle, petIndex,petkey);
			}
			// 如果玩家逃跑了,发送失败的消息
			List<xbean.Fighter> senderIDs = new ArrayList<xbean.Fighter>();
			senderIDs.add(f);
			long enemyID = 0;
			int msgid = 0;
			if (f.getIshost())
				enemyID = battle.getInifighters().get(BattleField.GUEST_FIRST_ID).getUniqueid();
			else
				enemyID = battle.getInifighters().get(BattleField.HOST_FIRST_ID).getUniqueid();
			if (TeamManager.isInTeam(enemyID))
				msgid = 120071;
			else
				msgid = 120069;
			sendToAllRoles(enemyID, msgid, senderIDs);
		}
		if (f.getFightertype() == Fighter.FIGHTER_PET) {
			recoverPetHpMPWithSP(battle, index,petkey);
		}
		return super.handleOutBattle(index,petkey);
	}

	public static void recoverPetHpMPWithSP(xbean.BattleInfo battleInfo, int petIndex,int petkey) {

		xbean.Fighter petFighter = battleInfo.getFighters().get(petIndex);
		if (petFighter == null) {
			return;
		}
		xbean.Fighter masterFighter = battleInfo.getFighters().get(petFighter.getBattleindex() - 5);
		if (masterFighter == null) {
			return;
		}
		long masterID = masterFighter.getUniqueid();
		SRefreshPetData srpd = new SRefreshPetData();
		PetColumn petColumn = new PetColumn(masterID, PetColumnTypes.PET, false);
		PetInfo petInfo = petColumn.getPetInfo(petkey);
		if (petInfo == null)
			return;
		petInfo.setHp(petFighter.getInihp());
		petInfo.setMp(petFighter.getInimp());
		srpd.columnid = PetColumnTypes.PET;
		srpd.petkey = petkey;
		srpd.datas.put(AttrType.HP, (float) petInfo.getHp());
		srpd.datas.put(AttrType.MP, (float) petInfo.getMp());
		Onlines.getInstance().send(masterID, srpd);

	}

	public static void refreshPetHpMp(long masterID, fire.pb.battle.Fighter petFighter) {
		SRefreshPetData srpd = new SRefreshPetData();
		PropRole prole = new PropRole(masterID, true);
		int petId = prole.getFightpetkey();
		srpd.columnid = PetColumnTypes.PET;
		srpd.petkey = petId;
		srpd.datas.put(AttrType.HP, (float) petFighter.getEffectRole().getMaxHp());
		srpd.datas.put(AttrType.MP, (float) petFighter.getEffectRole().getMaxMp());
		Onlines.getInstance().send(masterID, srpd);
	}


	
}
