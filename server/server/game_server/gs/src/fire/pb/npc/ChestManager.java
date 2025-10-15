package fire.pb.npc;

import java.util.HashMap;
import java.util.Map;

import fire.pb.main.ConfigManager;

public class ChestManager
{
	private final static ChestManager _instance = new ChestManager();
	
	private Map<Integer,ChestLib> libs = new HashMap<Integer, ChestLib>();
	
	public Map<Integer,SBaoxiang> chests;
	
	public static ChestManager getInstance()
	{
		return _instance;
	}
	
	private ChestManager(){}
	
	public void init()
	{
		chests = ConfigManager.getInstance().getConf(SBaoxiang.class);
		
		for(SBaoxiang chest : chests.values())
		{
			ChestLib lib = libs.get(chest.getBaoxianglist());
			if(lib == null)
			{
				lib = new ChestLib(chest.getBaoxianglist());
				libs.put(lib.libId, lib);
			}
			lib.chests.put(chest.id, chest);
		}
	}
	
	public ChestLib getChestLib(int libid)
	{
		return libs.get(libid);
	}
	public SBaoxiang getChestConfig(int chestid)
	{
		return chests.get(chestid);
	}
	
	
}
