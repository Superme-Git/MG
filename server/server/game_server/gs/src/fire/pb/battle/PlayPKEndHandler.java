
package fire.pb.battle;

import gnet.link.Onlines;
import mkdb.Procedure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fire.pb.PropRole;
import fire.pb.attr.AttrType;
import fire.pb.attr.SRefreshPetData;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.battle.BattleEndHandler;
import fire.pb.battle.BattleField;
import fire.pb.circletask.CircleTaskManager;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.talk.MessageMgr;
import fire.pb.team.TeamManager;
import xbean.BattleInfo;
import xbean.Fighter;
import xbean.PetInfo;

public class PlayPKEndHandler extends BattleEndHandler {
	public boolean handleDeath() {
		// 切磋其实没有任何惩罚,但是要把人物状态恢复原样,并且计算擂台战斗力
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
		switch (battle.getBattleresult()) {
		case PlayPKManage.PK_PLAY_WIN:
			handleQiecuoWin(hostFighters, guestFighters, battle,true);
			break;
		case PlayPKManage.PK_PLAY_LOSE:
			handleQiecuoWin(guestFighters, hostFighters, battle,false);
			break;
		default:
			break;
		}
		
		return true;
	}


	private void handleQiecuoWin(List<Fighter> hostFighters, List<Fighter> guestFighters, BattleInfo battle,boolean hostwin) {
		if (hostFighters == null || guestFighters == null){
			throw new IllegalArgumentException("切磋数据错误");
		}
		sendPlayPKMsg(hostFighters, guestFighters);// 发送消息
	}

	private void sendPlayPKMsg(List<Fighter> hostFighters, List<Fighter> guestFighters) {

		long hostroleid = 0;
		long guestroleid = 0;
		int hostMsgID = 0;
		int guestMsgID = 0;
		if (battle.getBattleresult() == PlayPKManage.PK_PLAY_WIN) {
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
			//处理历程
			fire.pb.course.CourseManager.achieveCourse(guestid, fire.pb.course.CourseType.PLAY_PK_COURSE, 1, 0);
		}
	}
	
	@Override
	protected boolean handleCamera() {
		return true;
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
