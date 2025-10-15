package fire.pb.scene.npcai;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fire.msp.npc.MNpcMoveTimerControl;
import fire.pb.scene.Scene;
import fire.pb.scene.SceneClient;
import fire.pb.scene.SceneManager;
import fire.pb.scene.manager.SceneNpcManager;
import fire.pb.scene.movable.NPC;
import fire.pb.scene.sPos.GridPos;
import fire.pb.scene.sPos.Position;

public class AIManager 
{
	public void loadConfig()
	{
		final XStream stream = new XStream(new DomDriver());

		try 
		{
			Scene.LOG.info("load npc walk ai info start");
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(getLoadFileName())));
			stream.alias("data", AIConfigList.class);
			stream.alias("item", AIConfig.class);
			
			// 这里必须要加上隐式collection,否则xstream会抛错
			stream.addImplicitCollection(AIConfig.class, "configs");
			stream.addImplicitCollection(AIConfigList.class, "ais");
			stream.useAttributeFor("id", int.class);

			stream.alias("moveto", Config_moveto.class);
			stream.useAttributeFor("x", int.class);
			stream.useAttributeFor("y", int.class);
			stream.useAttributeFor("speed", int.class);
			
			AIConfigList result = (AIConfigList)stream.fromXML(reader);
			config.clear();
			for (AIConfig conf : result.getAis())
			{
				config.put(conf.getAID(), conf);
			}
			Scene.LOG.info("load npc walk ai info end");
			
		} catch (final java.io.FileNotFoundException e) {
			Scene.LOG.info("从" + getLoadFileName() + "加载ai信息失败");
		}
	}
	
	public void summonTest() {

		final int npcid = 10779;
		final Scene s = SceneManager.getInstance().getSceneByID(1007);
		if (null == s)
			return;

		final Position p = new GridPos(360, 270).toPosition();

		final NPC n = SceneNpcManager.getInstance().createNpc(npcid, "testAI", 5);
		n.setName(n.getName() + n.getNpcKey());
		SceneNpcManager.getInstance().summonNpcAndUpdateGS(s, p, n);
	}
	
	public void npcMoveByAiID(long npcKey, int aid, final java.util.Set<Long> roleids) 
	{
		final NPC n = SceneNpcManager.getInstance().getNpcByKey(npcKey);
		if (null == n)
			return;
		
		n.setRelative(roleids);
		
		final AIConfig ac = config.get(aid);
		if(null == ac)
			return;
		
		final NPCAI na = new NPCAI(n, ac);
		na.start();
		addNewAi(na);
	}
	
	public String getLoadFileName()
	{
		return "gamedata/xml/ai.xml"; 
	}
	
	public void process(long tick) {

		java.util.Set<Long> del = new java.util.HashSet<Long>();
		for(NPCAI n : npcais.values())
		{
			if(n.checkOver())
			{
				//Scene.logger.info("=========== over ===================");
				del.add(n.getNpcKey());
				continue;
			}
			
			n.exec(tick);
		}

		for(long l : del)
			npcais.remove(l);
		
		if (npcais.isEmpty()) 
		{
			fire.pb.npc.NpcAI.NpcAITimer.getInstance().end();
			//Scene.logger.info("=========== stop ===================");
		}
	}
	
	private void addNewAi(NPCAI ai) {

		npcais.put(ai.getNpcKey(), ai);
		if (npcais.size() == 1) {
			SceneClient.pSend(new MNpcMoveTimerControl(1));
		}
	}
	
	public static synchronized AIManager getInstance() {

		if (null == instance) {
			if (null == instance)
				instance = new AIManager();
		}
		return instance;
	}

	private AIManager() {

	}

	private static AIManager instance;

	private final java.util.Map<Long, NPCAI> npcais = new java.util.HashMap<Long, NPCAI>();

	private final java.util.Map<Integer, AIConfig> config = new java.util.HashMap<Integer, AIConfig>();
}

