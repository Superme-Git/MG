package fire.pb.clan;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;


import org.apache.log4j.Logger;

import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;
import xbean.ClanInfo;

public class Module implements ModuleInterface {

	public static final Logger logger = Logger.getLogger("CLAN");

	public static Map<Integer, ClanInfo> clanListMap = Collections.synchronizedMap(new TreeMap<>());

	@Override
	public void exit() {
		
	}
	
	@Override
	public void init() throws Exception {
		initClanList();
		
		fire.pb.activity.clanfight.ActivityClanFightManager.getInstance().init(); //建立公会战 对战表 by changhao
	}
	
	
	/**
	 * 装载公会列表
	 */
	public void initClanList() {
		xtable.Clans.getTable().browse(
			new mkdb.TTable.IWalk<Long, xbean.ClanInfo>(){
				@Override
				public boolean onRecord(Long k, ClanInfo v) {
					logger.debug("遍历公会列表 key=" + k + ", 公会名称=" + v.getClanname());
					ClanUtils.addClanToCache(v.toData());
					return true;
				}
			}
		);
	}
	
	
	
	@Override
	public ReloadResult reload() throws Exception
	{
		return new ReloadResult(false,"module" + this.getClass().getName() + "not support reload");
	}
}
