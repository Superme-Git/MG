package fire.pb.huoban;

import java.util.ArrayList;
import java.util.List;

import fire.pb.npc.SHeroBaseInfo;
import fire.pb.npc.SHeroWeekFreeInfo;

import org.apache.log4j.Logger;

public class HuoBanFactory {
	
	private static HuoBanFactory _Instance;
	private HuoBanFactory(){}
	
	Logger logger = Logger.getLogger("HUOBAN");
	
	public static final int LOCK = 0;			//锁定状态
	public static final int UNLOCK = 1;			//解锁状态
	public static final int WEEKFREE = 2;		//本周免费
	public static final int FREEDAYS = 3;		//免费天数
	
	public synchronized static HuoBanFactory getFactory() {
		if (_Instance == null)
			_Instance = new HuoBanFactory();
		
		return _Instance;
	}
	
	public void initRoleHuoban(long roleId) {
		HuoBanColumn huobancol = HuoBanColumn.getHuoBanColumn(roleId, false);
		if (huobancol == null)
			return;
		
		List<Integer> weekFreeHuoban = new ArrayList<Integer>();
		//获取本周免费的伙伴
		SHeroWeekFreeInfo weekfree = Module.getInstance().getHeroWeekFreeInfo();
		if (weekfree == null) {
			logger.info("weekfree is null");
			return;
		}
		
		weekFreeHuoban.addAll(weekfree.getHeros());
		do {
			int vipCount = huobancol.huobancolumn.getViphuoban();
			if (vipCount == 0) break;

			for (int i = 0; i < vipCount; i ++) {
				Integer huobanid = weekfree.vipheros.get(i);
				if (huobanid != null)
					weekFreeHuoban.add(huobanid);
			}
		} while (false);
		
		int rolelevel = xtable.Properties.selectLevel(roleId);
		
		long nowTime = System.currentTimeMillis();
		
		for (int heroId : Module.getInstance().getHuoBanBaseInfo().keySet()) {
			try {
				SHeroBaseInfo hero = Module.getInstance().getHuoBanBaseInfo().get(heroId);
				xbean.HuoBanInfo heroInfo = xbean.Pod.newHuoBanInfo();
				
				if (weekFreeHuoban.contains(heroId)) {
					heroInfo.setState(WEEKFREE);
					heroInfo.setSettimes(nowTime);
					heroInfo.setWeekindex(weekfree.getId());
				}
				else {
					heroInfo.setState(LOCK);
					heroInfo.setSettimes(nowTime);
					heroInfo.setWeekindex(weekfree.getId());
				}

				//设置永久解锁的伙伴
				if (hero.creater_give == 1) {
					heroInfo.setState(UNLOCK);
					heroInfo.setSettimes(nowTime);
					heroInfo.setWeekindex(weekfree.getId());
				}
				else if (hero.creater_give == 2) { //设置出战
					heroInfo.setState(UNLOCK);
					heroInfo.setSettimes(nowTime);
					heroInfo.setWeekindex(weekfree.getId());
				}
				
				heroInfo.setId(heroId);
				heroInfo.setLevel(rolelevel);
				heroInfo.setInfight(0);
				heroInfo.setType(hero.getType());
				
				huobancol.addHuoBan(heroId, heroInfo);
			} 
			catch (Exception e) {
				e.printStackTrace();
				logger.error("ERROR: Init huoban info config fail. huobanid = " + heroId + e.toString());
			}
		}
	}

}
