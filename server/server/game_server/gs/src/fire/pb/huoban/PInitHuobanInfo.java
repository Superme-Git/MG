package fire.pb.huoban;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fire.pb.npc.SHeroBaseInfo;
import fire.pb.npc.SHeroWeekFreeInfo;
import fire.pb.util.CalendarUtil;
import mkdb.Procedure;

public class PInitHuobanInfo extends Procedure {
	
	Logger logger = Logger.getLogger("HUOBAN");
	
	private final long roleid;
	
	public PInitHuobanInfo(final long roleid) {
		this.roleid = roleid;
	}

	@Override
	protected boolean process() throws Exception {

		HuoBanColumn huobancol = HuoBanColumn.getHuoBanColumn(roleid, false);
		if (huobancol == null)
			return false;
		
		List<Integer> weekFreeHuoban = new ArrayList<Integer>();
		//获取本周免费的伙伴
		SHeroWeekFreeInfo weekfree = Module.getInstance().getHeroWeekFreeInfo();
		if (weekfree == null) {
			return false;
		}
		
		do {
			int vipCount = huobancol.getVipHuoBanCnt();
			if (vipCount == 0) break;
			
			for (int i = 0; i < vipCount; i ++) {
				Integer huobanid = weekfree.vipheros.get(i);
				if (huobanid != null)
					weekFreeHuoban.add(huobanid);
			}
		} while (false);
		
		int rolelevel = xtable.Properties.selectLevel(roleid);
		List<Integer> members = new ArrayList<Integer>(4);
		int i = 0;
		long nowTime = System.currentTimeMillis();
		for (int heroId : Module.getInstance().getHuoBanBaseInfo().keySet()) {
			try {
				SHeroBaseInfo hero = Module.getInstance().getHuoBanBaseInfo().get(heroId);
				
				xbean.HuoBanInfo heroInfo = null;
				HuoBan huoban = huobancol.getHuoBan(heroId);
				if (huoban == null) {
					heroInfo = xbean.Pod.newHuoBanInfo();
				}
				else {
					heroInfo = huoban.getHuoBanInfo();
				}
				
				if (weekFreeHuoban.contains(heroId)) {
					if (heroInfo.getState() == HuoBanFactory.LOCK) {
						heroInfo.setState(HuoBanFactory.WEEKFREE);
						heroInfo.setSettimes(nowTime);
						heroInfo.setWeekindex(weekfree.getId());
					}
					else if (heroInfo.getState() == HuoBanFactory.FREEDAYS) {
						heroInfo.setState(HuoBanFactory.FREEDAYS);
						heroInfo.setSettimes(nowTime);
						heroInfo.setWeekindex(weekfree.getId());
						Long nDays = heroInfo.getTimes();
						nDays += CalendarUtil.getTimeByContinueDays(7);
						heroInfo.setTimes(nDays);
					}
				}
				else {
					if (heroInfo.getState() == HuoBanFactory.LOCK) {
						heroInfo.setState(HuoBanFactory.LOCK);
						heroInfo.setSettimes(nowTime);
						heroInfo.setWeekindex(weekfree.getId());
					}
				}

				//设置永久解锁的伙伴
				if (hero.creater_give == 1) {
					heroInfo.setState(HuoBanFactory.UNLOCK);
					heroInfo.setSettimes(nowTime);
					heroInfo.setWeekindex(weekfree.getId());
				}
				else if (hero.creater_give == 2) { //设置出战
					heroInfo.setState(HuoBanFactory.UNLOCK);
					heroInfo.setSettimes(nowTime);
					heroInfo.setWeekindex(weekfree.getId());
					if (i < 4) {
						members.add(heroId);
					}
					i ++;
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
		
		if (members.size() > 0) {
			//开启伙伴有两个接口,一个是升级,一个是完成任务,此处是为防止重置
			if (huobancol.getFightHuobanKeys().size() == 0) {
				boolean suc = huobancol.setFightHuoBans(members);
				if (suc) {
					if (mkdb.Transaction.current() != null) {
						new PUpdateHuoBanZhenRong(roleid, 0, 0, members, PUpdateHuoBanZhenRong.FIRST).call();
					}
					else {
						new PUpdateHuoBanZhenRong(roleid, 0, 0, members, PUpdateHuoBanZhenRong.FIRST).submit();
					}
				}
			}
		}
		
		huobancol.sendHuoBanListOnChange();
		return true;
	}

}
