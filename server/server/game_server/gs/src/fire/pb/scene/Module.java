package fire.pb.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fire.pb.EnterWorldThread;
import fire.pb.battle.BattleCameraThread;
import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;
import fire.pb.map.GsMapThread;
import fire.pb.map.SceneNpcManager;
import fire.pb.scene.manager.TemplateNpcManager;
import fire.pb.shop.srv.market.MarketThread;

import org.apache.log4j.Logger;

public class Module implements ModuleInterface {
	static public final Logger logger = Logger.getLogger("MAPMAIN");		

	public Module() {	
	}

	@Override
	public void init() throws Exception{
		logger.info("map模块初始化开始");		
		TemplateNpcManager.getInstance().init();
		SceneManager.getInstance().init();
		logger.info("map模块初始化完成");
		
		MapThread.getInstance().start();
		GsMapThread.getInstance().start();
		SendProtocolThread.getInstance().start();
		EnterWorldThread.getInstance().start();
		MarketThread.getInstance().start();
		BattleCameraThread.getInstance().start();
		
		//加载npcAI的文件
		fire.pb.scene.npcai.AIManager.getInstance().loadConfig();
		
		/*//加载摇钱树npc
		Map<Long, Long> addKey = new HashMap<Long, Long>();
		try {
			new mkdb.Procedure() {
				protected boolean process() throws Exception {
					xtable.Yaoqianshutables.getTable().browse(
							new mkdb.TTable.IWalk<Long, xbean.YaoQianShuInfo>() {
								@Override
								public boolean onRecord(Long npckey, xbean.YaoQianShuInfo yaoqianshuInfo) {
									long newnpckey = SceneNpcManager.getInstance().getNextId();
									SceneNpcManager.createNpcByPos(newnpckey, fire.pb.item.groceries.YaoQianShuItem.YAOQIANSHUNPC, "", yaoqianshuInfo.getMapid(), 
											0, yaoqianshuInfo.getPosx(), yaoqianshuInfo.getPosy(), 4, 1800000);
									addKey.put(npckey, newnpckey);
									return true;
								}
							});
					return true;
				};
			}.submit().get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			new mkdb.Procedure() {
				protected boolean process() throws Exception {
					for (Map.Entry<Long, Long> npcMaps : addKey.entrySet()) {
						xbean.YaoQianShuInfo yaoqianInfo = xtable.Yaoqianshutables.get(npcMaps.getKey());
						if (yaoqianInfo != null) {
							xtable.Yaoqianshutables.remove(npcMaps.getKey());
							xtable.Yaoqianshutables.insert(npcMaps.getValue(), yaoqianInfo);
						}
					}
					return true;
				}
			}.submit().get();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		//加载摇钱树npc
		Map<Long, Map<Long, Long>> removeKey = new HashMap<Long, Map<Long, Long>>();
		try {
			new mkdb.Procedure() {
				protected boolean process() throws Exception {
					xtable.Roleyaoqianshutables.getTable().browse(
							new mkdb.TTable.IWalk<Long, xbean.RoleYaoQianShuInfoMaps>() {
								@Override
								public boolean onRecord(Long roleid, xbean.RoleYaoQianShuInfoMaps yaoqianshuInfo) {
									
									Map<Long, Long> removeKK = removeKey.get(roleid);
									if (removeKK == null) {
										removeKK = new HashMap<Long, Long>();
										removeKey.put(roleid, removeKK);
									}
									
									for (Map.Entry<Long, xbean.RoleYaoQianShuInfo> yqInfo : yaoqianshuInfo.getYaoqianshumaps().entrySet()) {
										Long newnpckey = SceneNpcManager.getInstance().getNextId();
										SceneNpcManager.createNpcByPos(newnpckey, fire.pb.item.groceries.YaoQianShuItem.YAOQIANSHUNPC, "", yqInfo.getValue().getMapid(), 
												0, yqInfo.getValue().getPosx(), yqInfo.getValue().getPosy(), 3, 1800000);
										
										xtable.Roleyaoqianshuinfos.insert(newnpckey, roleid);
										
										if (removeKK.containsKey(newnpckey))
											removeKK.remove(newnpckey);
										else if (yqInfo.getKey() != newnpckey)
											removeKK.put(yqInfo.getKey(), newnpckey);
									}
									
									return true;
								}
							});
					return true;
				};
			}.submit().get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			new mkdb.Procedure() {
				protected boolean process() throws Exception {
					for (Map.Entry<Long, Map<Long, Long>> npcMaps : removeKey.entrySet()) {
						xbean.RoleYaoQianShuInfoMaps yaoqianInfo = xtable.Roleyaoqianshutables.get(npcMaps.getKey());
						if (yaoqianInfo != null) {
							
							for (Map.Entry<Long, Long> npckey : npcMaps.getValue().entrySet()) {
								xbean.RoleYaoQianShuInfo yqInfo = yaoqianInfo.getYaoqianshumaps().get(npckey.getKey());
								if (yqInfo != null) {
									yaoqianInfo.getYaoqianshumaps().remove(npckey.getKey());
									yaoqianInfo.getYaoqianshumaps().put(npckey.getValue(), yqInfo);
								}
							}
						}
					}
					return true;
				}
			}.submit().get();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void exit() {
		MapThread.getInstance().shutdown();
		MapThread.getInstance().clear();
		SendProtocolThread.getInstance().shutdown();
		SendProtocolThread.getInstance().clear();
		EnterWorldThread.getInstance().shutdown();
		EnterWorldThread.getInstance().clear();
		MarketThread.getInstance().shutdown();
		MarketThread.getInstance().clear();
	}

	@Override
	public ReloadResult reload() throws Exception
	{
		return new ReloadResult(false,"module" + this.getClass().getName() + "not support reload");
	}

}
