package fire.pb.huoban;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fire.pb.map.RoleManager;
import fire.pb.npc.SHeroBaseInfo;
import fire.pb.npc.SHeroWeekFreeInfo;
import fire.pb.util.CalendarUtil;
import fire.log.Logger;

public class HuoBanColumn {

	private static final Logger logger = Logger.getLogger("HUOBAN");

	final long roleId;
	final boolean readonly;
	final xbean.HuoBanColumn huobancolumn;

	public static HuoBanColumn getHuoBanColumn(long roleId, boolean readonly) {
		if (xtable.Properties.select(roleId) == null) {
			logger.error("构造HuoBanColumn时，角色 " + roleId + " 不存在。");
			return null;
		}
		xbean.HuoBanColumn col = null;
		if (readonly)
			col = xtable.Huobancolumns.select(roleId);
		else
			col = xtable.Huobancolumns.get(roleId);

		if (col == null) {
			if (readonly)
				col = xbean.Pod.newHuoBanColumnData();
			else {
				col = xbean.Pod.newHuoBanColumn();
				xtable.Huobancolumns.insert(roleId, col);
			}
		}

		return new HuoBanColumn(roleId, col, readonly);
	}

	private HuoBanColumn(long roleId, xbean.HuoBanColumn column,
			boolean readonly) {
		this.roleId = roleId;
		this.huobancolumn = column;
		this.readonly = readonly;
	}

	public HuoBan getHuoBan(int huobanId) {
		xbean.HuoBanInfo info = huobancolumn.getHuobans().get(huobanId);
		if (info == null)
			return null;

		return HuoBan.getHuoBan(info, readonly);
	}

	public HuoBanDetailInfo getHuoBanDetailInfo(int huoBanId) {
		HuoBanDetailInfo sDetailInfo = new HuoBanDetailInfo();

		HuoBan huoban = getHuoBan(huoBanId);
		if (huoban == null)
			return null;

		sDetailInfo.huobanid = huoBanId;
		sDetailInfo.infight = huoban.getInfight();
		sDetailInfo.weekfree = huoban.getWeekFree();
		sDetailInfo.state = huoban.getHuoBanFinalState();
		Map<Integer, Float> finalAttrs = huoban.getL2Attrs(roleId);
		for (Map.Entry<Integer, Float> entry : finalAttrs.entrySet()) {
			sDetailInfo.datas.add(entry.getValue().intValue());
		}

		return sDetailInfo;
	}

	public HuoBanInfo getHuoBanInfo(int huoBanId) {
		HuoBanInfo sInfo = new HuoBanInfo();

		HuoBan huoban = getHuoBan(huoBanId);
		if (huoban == null)
			return null;

		sInfo.huobanid = huoBanId;
		sInfo.infight = huoban.getInfight();
		sInfo.state = huoban.getHuoBanFinalState();
		sInfo.weekfree = huoban.getWeekFree();

		return sInfo;
	}

	
	public int getHuoBanInfoByType(final int type, List<Integer> huobanKeys, List<Integer> nowhuobanKeys) {
		for (xbean.HuoBanInfo huobanInfo : huobancolumn.getHuobans().values()) {
			if (huobanInfo.getState() == 0)
				continue;
			if (huobanKeys.contains(huobanInfo.getId()))
				continue;
			if (nowhuobanKeys.contains(huobanInfo.getId()))
				continue;
			if (huobanInfo.getType() == type) {
				return huobanInfo.getId();
			}
		}
		
		for (xbean.HuoBanInfo huobanInfo : huobancolumn.getHuobans().values()) {
			if (huobanInfo.getState() == 0)
				continue;
			if (huobanKeys.contains(huobanInfo.getId()))
				continue;
			if (nowhuobanKeys.contains(huobanInfo.getId()))
				continue;
			return huobanInfo.getId();
		}

		return 0;
	}
	
	/**
	 * 更新周免的伙伴
	 */
	public void setWeekFreeHuoBan() {
		List<Integer> weekFreeHuoban = new ArrayList<Integer>();
		//获取本周免费的伙伴
		SHeroWeekFreeInfo weekfree = Module.getInstance().getHeroWeekFreeInfo();
		if (weekfree == null) {
			Module.logger.error(new StringBuilder().append("week free is null:").append(roleId));
			return;
		}
		
		weekFreeHuoban.addAll(weekfree.getHeros());
		do {
			int vipCount = huobancolumn.getViphuoban();
			if (vipCount == 0) break;

			for (int i = 0; i < vipCount; i ++) {
				Integer huobanid = weekfree.vipheros.get(i);
				if (huobanid != null)
					weekFreeHuoban.add(huobanid);
			}
		} while (false);
		
		long nowTime = System.currentTimeMillis();
		
		for (Integer huobanId : getAllHuoBans().keySet()) {
			HuoBan huoban = getHuoBan(huobanId);
			int nState = huoban.getHuoBanState();
			Long finalState = huoban.getHuoBanFinalState();
			
			if (weekFreeHuoban.contains(huobanId)) {
				switch (nState) {
				case 0:
					huoban.setHuoBanState(2);
					huoban.setHuoBanWeekIndex(weekfree.getId());
					Module.logger.info(new StringBuffer().append("set weekfree: ").append("roleid:").append(roleId).append("huobans:").append(huobanId));
					break;
				case 3:
					if (finalState > 0L) {
						//此处必须判断修改是在同一周进行的,是在同一周就不加天数了
//						if (!fire.pb.util.DateValidate.inTheSameWeek(huoban.getSetTimes(), nowTime)) {
						if (huoban.getHuoBanWeekIndex() != weekfree.getId()) {
							long addtime = CalendarUtil.getThisWeekSunday() - nowTime;
							huoban.addHuoBanTimes(addtime);
							huoban.setHuoBanWeekIndex(weekfree.getId());
							Module.logger.info(new StringBuffer().append("set weekfree add: ").append("roleid:").append(roleId).append("huobans:").append(huobanId)
									.append(",state").append(huoban.getHuoBanFinalState()));
						}
					}
					else {
						huoban.setHuoBanState(2);
						huoban.setHuoBanWeekIndex(weekfree.getId());
					}
					break;
				default:
					break;
				}
			}
			else {
				switch (nState) {
				case 2:
					huoban.setHuoBanState(0);
					huoban.setHuoBanWeekIndex(weekfree.getId());
					Module.logger.info(new StringBuffer().append("unset weekfree: ").append("roleid:").append(roleId).append("huobans:").append(huobanId));
					break;
				default:
					break;
				}
			}
		}
	}

	public void sendHuoBanZhenRong() {
		SZhenrongInfo szhenrongInfo = new SZhenrongInfo();
		HuoBanZhenRongColumn zhenrongCol = HuoBanZhenRongColumn.getZhenRongColumn(roleId, false);
		
		szhenrongInfo.dangqianzhenrong = zhenrongCol.getCurrentZhenRong();
		Map<Integer, xbean.HuoBanZhenrongInfo> szhenrong = zhenrongCol.getZhenRong();
		
		//获取本周免费的伙伴
		SHeroWeekFreeInfo weekfree = Module.getInstance().getHeroWeekFreeInfo();
		if (weekfree == null) {
			logger.debug("weekfree is null");
			return;
		}
		
		List<Integer> vipHuoban = new ArrayList<Integer>();
		do {
			int vipCount = huobancolumn.getViphuoban();
			if (vipCount == 0) break;

			for (int i = 0; i < vipCount; i ++) {
				Integer huobanid = weekfree.vipheros.get(i);
				if (huobanid != null)
					vipHuoban.add(huobanid);
			}
		} while (false);
		
		for (Map.Entry<Integer, xbean.HuoBanZhenrongInfo> zr : szhenrong.entrySet()) {
			// 判断伙伴是否到期
			ZhenrongInfo zInfo = new ZhenrongInfo();
			zInfo.zhenfa = zr.getValue().getZhenfa();

			List<Integer> nowhuobanKeys = new ArrayList<Integer>(4);
			nowhuobanKeys.addAll(zr.getValue().getHuoban());
			for (Integer huobanId : zr.getValue().getHuoban()) {
				HuoBan huoban = getHuoBan(huobanId);
				if (huoban == null)
					return;

				Long nState = huoban.getHuoBanFinalState();
				if (nState == 2L) {
					if (!weekfree.getHeros().contains(huobanId) && !vipHuoban.contains(huobanId)) {
						nState = 0L;
					}
				}
				
				if (nState == 0L) { // 未解锁
					// 该伙伴已经到期,使用另一个同类型的伙伴
					nowhuobanKeys.remove(huobanId);
					int tmpId = getHuoBanInfoByType(huoban.getType(), zInfo.huobanlist, nowhuobanKeys);
					if (tmpId != 0) {
						zInfo.huobanlist.add(tmpId);
					}
				} else {
					zInfo.huobanlist.add(huobanId);
				}
			}
			zr.getValue().getHuoban().clear();
			zr.getValue().getHuoban().addAll(zInfo.huobanlist);

			szhenrongInfo.zhenrongxinxi.put(zr.getKey(), zInfo);
			
			if (zr.getKey() == zhenrongCol.getCurrentZhenRong()) {
				//设置当前战斗阵容
				setFightHuoBans(zInfo.huobanlist);
			}
		}
		
//		xdb.Procedure.psendWhileCommit(roleId, szhenrongInfo);
		mkdb.Transaction.tsendWhileCommit(roleId, szhenrongInfo);
	}

	public void sendHuoBanList() {
		SHuobanList snd = new SHuobanList();
		
		Map<Integer, SHeroBaseInfo> huobanMap = Module.getInstance().getHuoBanBaseInfo();
		
		final int roleLevel = xtable.Properties.selectLevel(roleId);
		List<Integer> weekFreeHuoban = new ArrayList<Integer>();
		//获取本周免费的伙伴
		SHeroWeekFreeInfo weekfree = Module.getInstance().getHeroWeekFreeInfo();
		if (weekfree == null) {
			logger.info("weekfree is null:" + roleId);
			return;
		}
		
		weekFreeHuoban.addAll(weekfree.getHeros());
		do {
			int vipCount = huobancolumn.getViphuoban();
			if (vipCount == 0) break;

			for (int i = 0; i < vipCount; i ++) {
				Integer huobanid = weekfree.vipheros.get(i);
				if (huobanid != null)
					weekFreeHuoban.add(huobanid);
			}
		} while (false);
		
		long nowTime = System.currentTimeMillis();
		
		for (Integer heroId : huobanMap.keySet()) {
			HuoBanInfo sInfo = new HuoBanInfo();
			
			HuoBan huoban = getHuoBan(heroId);
			if (huoban == null) {
				//表里新添加的伙伴,玩家数据库里没有,添加到数据库里
				xbean.HuoBanInfo heroInfo = null;
				heroInfo = xbean.Pod.newHuoBanInfo();
				SHeroBaseInfo hero = Module.getInstance().getHuoBanBaseInfo().get(heroId);
				
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
				}
				
				heroInfo.setId(heroId);
				heroInfo.setLevel(roleLevel);
				heroInfo.setInfight(0);
				heroInfo.setType(hero.getType());
				
				addHuoBan(heroId, heroInfo);
				huoban = getHuoBan(heroId);
			}
			
			sInfo.huobanid = heroId;
			sInfo.infight = huoban.getInfight();
			sInfo.weekfree = huoban.getWeekFree();
			
			sInfo.state = huoban.getHuoBanFinalState();
			snd.huobans.add(sInfo);
			if (snd.huobans.size() >= 200) {
				if (mkdb.Transaction.current() != null) {
//					xdb.Procedure.psendWhileCommit(roleId, snd);
					mkdb.Transaction.tsendWhileCommit(roleId, snd);
				}
				else
					gnet.link.Onlines.getInstance().send(roleId, snd);
				snd = new SHuobanList();
			}
		}
		
		if (snd.huobans.size() > 0) {
//			xdb.Procedure.psendWhileCommit(roleId, snd);
			mkdb.Transaction.tsendWhileCommit(roleId, snd);
		}
	}

	public void sendHuoBanListOnChange() {
		setWeekFreeHuoBan();
		sendHuoBanZhenRong();
		sendHuoBanList();
	}

	public void addHuoBan(int huobanId, xbean.HuoBanInfo huobanInfo) {
		if (huobancolumn.getHuobans().containsKey(huobanId)) {
			huobancolumn.getHuobans().remove(huobanId);
		}
		huobancolumn.getHuobans().put(huobanId, huobanInfo);
	}

	public boolean setFightHuoBans(List<Integer> fights) {
		for (Integer huobanId : fights) {
			HuoBan huoban = getHuoBan(huobanId);
			if (!huoban.canFight())
				return false;
		}

		huobancolumn.getFighthuobans().clear();
		huobancolumn.getFighthuobans().addAll(fights);

		fire.pb.map.Role role = RoleManager.getInstance().getRoleByID(roleId);
		if (role != null)
			role.setHuobanSize(huobancolumn.getFighthuobans().size());
		return true;
	}

	public List<Integer> getFightHuobanKeys() {
		//先刷一下周免伙伴
		setWeekFreeHuoBan();
		
		List<Integer> huobanKeys = new ArrayList<Integer>(4);
		List<Integer> nowhuobanKeys = new ArrayList<Integer>(4);
		nowhuobanKeys.addAll(huobancolumn.getFighthuobans());

		boolean change = false;
		for (Integer huobanId : huobancolumn.getFighthuobans()) {
			HuoBan huoban = getHuoBan(huobanId);
			if (huoban == null)
				return null;

			Long nState = huoban.getHuoBanFinalState();
			if (nState == 0L) { // 未解锁
				// 该伙伴已经到期,使用另一个同类型的伙伴
				nowhuobanKeys.remove(huobanId);
				int tmpId = getHuoBanInfoByType(huoban.getType(), huobanKeys, nowhuobanKeys);
				if (tmpId != 0) {
					huobanKeys.add(tmpId);
					change = true;
				}
			} else {
				huobanKeys.add(huobanId);
			}
		}

		huobancolumn.getFighthuobans().clear();
		huobancolumn.getFighthuobans().addAll(huobanKeys);
		
		if (change) {
			HuoBanZhenRongColumn zhenrongCol = HuoBanZhenRongColumn.getZhenRongColumn(roleId, false);
			zhenrongCol.setCurrentZhenRongHuoBan(huobanKeys);
			
			sendHuoBanZhenRong();
			sendHuoBanList();
		}
		
		return huobanKeys;
	}

	public Map<Integer, HuoBan> getAllHuoBans() {
		Map<Integer, HuoBan> huobans = new HashMap<Integer, HuoBan>();
		for (Entry<Integer, xbean.HuoBanInfo> entry : huobancolumn.getHuobans().entrySet()) {
			huobans.put(entry.getKey(),	HuoBan.getHuoBan(entry.getValue(), readonly));
		}

		return huobans;
	}
	
	public void setVipHuoBanCnt(final int cnt) {
		this.huobancolumn.setViphuoban(cnt);
	}
	
	public int getVipHuoBanCnt() {
		return this.huobancolumn.getViphuoban();
	}
}
