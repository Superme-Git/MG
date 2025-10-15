package fire.pb.scene.manager;

import java.util.HashMap;
import java.util.Map;

import fire.pb.npc.SNpcShare;
import fire.pb.scene.config.TemplateNpc;

public class TemplateNpcManager
{
	Map<Integer,TemplateNpc> tnpcs = new HashMap<Integer,TemplateNpc>();
	
	private static volatile TemplateNpcManager instance;	
	private TemplateNpcManager(){}
	public static TemplateNpcManager getInstance(){
		if(null == instance){instance = new TemplateNpcManager();}		
		
		return instance;
	}	
	
	
	public TemplateNpc getTemplateNpc(int npcbaseId)
	{
		return tnpcs.get(npcbaseId);
	}
	
	public Map<Integer,TemplateNpc> getTemplateNpcs()
	{
		return tnpcs;
	}
	
	public void init()
	{
		Map<Integer, SNpcShare> npcShareMap =
				fire.pb.main.ConfigManager.getInstance().getConf( fire.pb.npc.SNpcShare.class );
		for(Map.Entry<Integer, SNpcShare> entry : npcShareMap.entrySet())
		{
			TemplateNpc tnpc = new TemplateNpc(entry.getKey());
			tnpc.setMiniMapVisible((entry.getValue().getMinimapshow() != null && !entry.getValue().equals("")));
			tnpcs.put(entry.getKey(), tnpc);
		}
	}
}
