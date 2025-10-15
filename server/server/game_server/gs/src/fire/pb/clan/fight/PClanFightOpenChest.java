package fire.pb.clan.fight;

import fire.pb.map.Npc;
import fire.pb.map.SceneNpcManager;

/**
 * 拾取公会战宝箱  by changhao
 */
public class PClanFightOpenChest extends mkdb.Procedure
{
	long clanfightid;
	long roleid;
	long npckey;
	
	public PClanFightOpenChest(long clanfightid, long roleid, long npckey)
	{
		this.clanfightid = clanfightid;
		this.roleid = roleid;
		this.npckey = npckey;
	}
	
	@Override
	/***
	 * locs clanfightid -> roleid by changhao
	 */
	protected boolean process() throws Exception
	{
		Npc npc = SceneNpcManager.getNpcByKey(npckey);
		if (npc == null)
		{
			return false;
		}
		
		fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(clanfightid, false);
		if (bf != null)
		{
			bf.openChest(roleid, npckey, npc.getNpcID());
		}
		
		return true;
	}
}
