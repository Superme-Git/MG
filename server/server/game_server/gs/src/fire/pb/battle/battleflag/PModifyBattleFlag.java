package fire.pb.battle.battleflag;

import mkdb.Procedure;

public class PModifyBattleFlag extends Procedure {
	
	public static final int MAX_BATTLEFLAG_SIZE = 10;//创建队伍,判断队长
	public static final int MAX_FRIENDBATTLEFLAG_SIZE = 6;//创建队伍,判断队长
	public static final int MAX_ENEMYBATTLEFLAG_SIZE = 5;//创建队伍,判断队长

	private long roleId;
	private byte opttype; // 0 add, 1 delete, 2 modify
	private byte index; // 0-9 友方, 10-19敌方
	private String flag;

	public PModifyBattleFlag(long roleid, byte opttype,byte index,String flag) {
		this.roleId = roleid;
		this.opttype = opttype;
		this.index = index;
		this.flag = flag;
	}

	@Override
	public boolean process() {
		
		xbean.BattleFlags battleFlags = xtable.Battleflag.get(roleId);
		if(battleFlags == null)
		{
			battleFlags = xbean.Pod.newBattleFlags();
			xtable.Battleflag.insert(roleId, battleFlags);
		}
		
		switch(opttype)
		{
			case 0: //add
			{
				if(flag.length()>8)
					return false;
				if(index < MAX_FRIENDBATTLEFLAG_SIZE)
				{
					if(battleFlags.getFriendflag().size() >=  MAX_FRIENDBATTLEFLAG_SIZE||flag.length()<1)
						return false;
					battleFlags.getFriendflag().add(flag);
				}
				else if(index <MAX_BATTLEFLAG_SIZE + MAX_ENEMYBATTLEFLAG_SIZE)
				{
					if(battleFlags.getEnemyflag().size() >=  MAX_ENEMYBATTLEFLAG_SIZE||flag.length()<1)
						return false;
					battleFlags.getEnemyflag().add(flag);
				}
				else
					return false;
				
			} 
			break;
			case 1://delete
			{
				if(index < MAX_FRIENDBATTLEFLAG_SIZE)
				{
					if(battleFlags.getFriendflag().size() <=  index)
						return false;
					battleFlags.getFriendflag().remove(index);
				}
				else if(index <MAX_BATTLEFLAG_SIZE + MAX_ENEMYBATTLEFLAG_SIZE)
				{
					if(battleFlags.getEnemyflag().size() <= index - MAX_BATTLEFLAG_SIZE)
						return false;
					battleFlags.getEnemyflag().remove(index - MAX_BATTLEFLAG_SIZE);
				}
				else
					return false;
			}
			break;
			case 2://modify
			{
				if(flag.length()>8)
					return false;
				if(index < MAX_FRIENDBATTLEFLAG_SIZE)
				{
					if(battleFlags.getFriendflag().size() <=  index)
						return false;
					if(flag.length()<1)
					{
						battleFlags.getFriendflag().remove(index);
						SModifyBattleFlag send = new SModifyBattleFlag((byte)1, index, flag);
						psend(roleId, send);
						return true;
					}
					else
						battleFlags.getFriendflag().set(index, flag);
				}
				else if(index <MAX_BATTLEFLAG_SIZE + MAX_ENEMYBATTLEFLAG_SIZE)
				{
					if(battleFlags.getEnemyflag().size() <= index - MAX_BATTLEFLAG_SIZE)
						return false;
					if(flag.length()<1)
					{
						battleFlags.getEnemyflag().remove(index - MAX_BATTLEFLAG_SIZE);
						SModifyBattleFlag send = new SModifyBattleFlag((byte)1, index, flag);
						psend(roleId, send);
						return true;
					}
					else
						battleFlags.getEnemyflag().set(index - MAX_BATTLEFLAG_SIZE, flag);
				}
				else
					return false;
			} 
			break;
			default:
				return false;
		}
		
		SModifyBattleFlag send = new SModifyBattleFlag(opttype, index, flag);
		psend(roleId, send);
		fire.pb.talk.MessageMgr.sendMsgNotify(roleId, 180041, null);
		return true;
	}
}
