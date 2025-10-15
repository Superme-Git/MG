package fire.pb.util;

import fire.pb.battle.BattleAimRelation;

// 解析战斗内使用对象id
public class ParseObjIDInBattle {
	public ParseObjIDInBattle( final int relations ) {
		int objid = relations;
		self = (objid & 0x1) == 1;
		objid >>= 1;
		selfPet = (objid & 0x1) == 1;
		objid >>= 1;
		friendRole = (objid & 0x1) == 1;
		objid >>= 1;
		friendPet = (objid & 0x1) == 1;
		objid >>= 1;
		friendNpc = (objid & 0x1) == 1;
		objid >>= 1;
		enermyRole = (objid & 0x1) == 1;
		objid >>= 1;
		enermyPet = (objid & 0x1) == 1;
		objid >>= 1;
		enermyNpc = (objid & 0x1) == 1;
		objid >>= 1;
		enermyMonster = (objid & 0x1) == 1;
		objid >>= 1;
		couple = (objid & 0x1) == 1;
		objid >>= 1;
		brothers = (objid & 0x1) == 1;
		objid >>= 1;
		masterandapprentice = (objid & 0x1) == 1;
		objid >>= 1;
		friendMonster = (objid & 0x1) == 1;
	}
	public final boolean self;
	public final boolean selfPet;
	public final boolean friendRole;
	public final boolean friendPet;
	public final boolean friendNpc;
	public final boolean enermyRole;
	public final boolean enermyPet;
	public final boolean enermyNpc;
	public final boolean enermyMonster;
	public final boolean couple;
	public final boolean brothers;
	public final boolean masterandapprentice;
	public final boolean friendMonster;
	public boolean validTarget(int relation)
	{
		if (relation == BattleAimRelation.SELF && self)
			return true;
		else if(relation == BattleAimRelation.SELF_PET && selfPet)
			return true;
		else if(relation == BattleAimRelation.FRIEND_ROLE && friendRole)
			return true;
		else if(relation == BattleAimRelation.FRIEND_PET && friendPet)
			return true;
		else if(relation == BattleAimRelation.FRIEND_NPC && friendNpc)
			return true;
		else if(relation == BattleAimRelation.ENERMY_ROLE && enermyRole)
			return true;
		else if(relation == BattleAimRelation.ENERMY_PET && enermyPet)
			return true;
		else if(relation == BattleAimRelation.ENERMY_NPC && enermyNpc)
			return true;
		else if(relation == BattleAimRelation.ENERMY_MONSTER && enermyMonster)
			return true;
		else if(relation == BattleAimRelation.COUPLE && couple)
			return true;
		else if(relation == BattleAimRelation.BROTHERS && brothers)
			return true;
		else if(relation == BattleAimRelation.MASTER_STUDENT && masterandapprentice)
			return true;
		else if(relation == BattleAimRelation.FRIEND_MONSTER && friendMonster)
			return true;
		else
			return false;
	}
}
