package fire.pb.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fire.msp.move.GRoleEquipChange;
import fire.msp.role.GChangeEquipEffect;
import fire.pb.GsClient;
import fire.pb.battle.BattleConfig;
import fire.pb.item.equip.diamond.EquipDiamondMgr;

public class Equip extends ItemMaps {
	public Equip(long roleId, boolean readonly) {
		super(roleId, readonly);
	}

	@Override
	public int addCapacity(int size) {
		throw new UnsupportedOperationException("装备背包大小不能改变");
	}

	public void battleEnd(final int battleConfigId, final Map<Integer, Integer> ac) {
		// 不掉耐久 add by yebin
		BattleConfig battleConfig = fire.pb.battle.Module.getInstance().getBattleConfigs().get(battleConfigId);
		if(battleConfig != null && battleConfig.isNotDecEndure == 1){
			if(Module.logger.isInfoEnabled()){
				Module.logger.info("不掉耐久, battleId=" + battleConfigId);
			}
			return;
		}
		
		if(Module.logger.isDebugEnabled()){
			Module.logger.debug("耐久度降低");
		}
		
		SRefreshNaiJiu send = new SRefreshNaiJiu();
		send.packid = getPackid();
		for (int i = 0; i < getCapacity(); i++) {
			loseEndure(ac, i, send);
		}
		mkdb.Procedure.psendWhileCommit(roleId, send);
		for (ItemBase item : this) {
			if (item instanceof EquipItem) {
				EquipItem ei = (EquipItem) item;
				if (ei.getEndure() <= 5) {
					ArrayList<String> params = new ArrayList<String>();
					params.add(ei.itemAttr.name);
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileCommit(roleId,
							142381, params);
				}
			}
		}
	}

	@Override
	public AddItemResult doAddItem(final ItemBase item, final int p,
			final String reason, final fire.log.enums.YYLoggerTuJingEnum countertype, final int xiangguanid) {
		final int pos;
		if (p == -1) {
			// 自动查找位置
			if (item instanceof EquipItem) {
				EquipItem ei = (EquipItem) item;
				pos = ei.getEquipPos();
			} else
				return AddItemResult.POS_NOT_AVAILABLE;
		} else {
			pos = p;
		}
		return super.doAddItem(item, pos, reason, countertype, xiangguanid);
	}

	@Override
	public int getCapacity() {
		return conf.sizesize;
	}

	public ItemBase getHeaddress() {
		return getItemByPos(EquipItemType.TIRE);
	}

	public ItemBase getHorsedress() {
		return null;// getItemByPos(EquipItem.HEADDRESS-1);
	}

	@Override
	public int getPackid() {
		return BagTypes.EQUIP;
	}

	public ItemBase getWeapon() {
		return getItemByPos(EquipItemType.ARMS);
	}

	@Override
	public boolean isFull() {
		return false;
	}

	private void loseEndure(final Map<Integer, Integer> ac, int pos,
			SRefreshNaiJiu send) {
		for (Map.Entry<Integer, Integer> e : ac.entrySet()) {
			Module.logger.debug("战斗统计消息为id=" + e.getKey() + ", 次数为 "
					+ e.getValue());
		}
		ItemBase item = getItemByPos(pos);

		if (item != null && item instanceof EquipItem) {
			SEquipNaiJiuXiaoHao lose = Module.getInstance().getItemManager()
					.getLoseNaiJiu(pos);
			if (lose == null)
				return;
			EquipItem eitem = (EquipItem) item;
			for (int i = 0; i < lose.yuanyin.size() - 1; i++) {
				int times = lose.cishu.get(i);

				if (times != 0) {
					Integer w = ac.get(lose.yuanyin.get(i));
					if (w != null && w != 0) {
						Module.logger.debug("耐久消息为id=" + lose.yuanyin.get(i)
								+ ", 次数为 " + w + "位置为" + pos);
						int before = eitem.getEquipAttr().getEndure();
						if (eitem.getEndure() > 0
								&& eitem.loseEndure(100 / times * w)) {
							if (before != eitem.getEquipAttr().getEndure()) {
								fire.pb.item.EquipNaiJiu endure = new fire.pb.item.EquipNaiJiu();
								endure.keyinpack = item.getKey();
								endure.endure = eitem.getExtInfo().getEndure();
								send.data.add(endure);
							}
						}
					}
				}
			}

			if (eitem.getExtInfo().getEndure() == 0) {
				fire.pb.skill.SceneSkillRole role = fire.pb.skill.SkillManager
						.getSceneSkillRole(roleId);
				role.removeEquipEffectAndSkillWithSP(eitem);
			}
		}
	}

	public void onUnequip(EquipItem ei) {
		GRoleEquipChange notifymap = new GRoleEquipChange();
		notifymap.roleid = roleId;
		notifymap.pos = ei.getEquipPos();
		notifymap.itemid = 0;
		notifymap.ride = -1;
		notifymap.effect = 0;
		GsClient.pSendWhileCommit(notifymap);

		GChangeEquipEffect equipeffect = new GChangeEquipEffect();
		equipeffect.effect = 0;
		equipeffect.roleid = roleId;
		GsClient.pSendWhileCommit(equipeffect);

		xbean.Properties pProp = xtable.Properties.get(roleId);
		pProp.setEquipeffect(0);

		Long teamId = xtable.Roleid2teamid.get(roleId);
		if (teamId != null)
			mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
				protected boolean process() throws Exception {
					final fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleId);
					if (team != null) {
						team.updateTeamMemberComponents2Others(roleId);
					}
					return true;
				};
			});

		SAllEquipScore equipTotalScore = new SAllEquipScore();
		equipTotalScore.score = Module.getInstance().getEquipTotalScore(roleId);
		mkdb.Procedure.psendWhileCommit(roleId, equipTotalScore);

		if (ei.getExtInfo().getEndure() > 0) {
			fire.pb.skill.SceneSkillRole role = fire.pb.skill.SkillManager
					.getSceneSkillRole(roleId);
			role.removeEquipEffectAndSkillWithSP(ei);
		}

		// 更新玩家综合实力排行榜
		mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PRoleZongheRankProc(roleId));
	}

	@Override
	protected boolean TransIn(ItemBase item, int pos, ItemBase dstitem) {
		if (super.TransIn(item, pos, dstitem)) {
			fire.pb.event.Poster.getPoster().dispatchEvent(
					new fire.pb.event.EquipItemEvent(roleId, item.getItemId()));
			return true;
		}
		return false;
	}

	@Override
	public ItemBase TransOut(final int key, final int number,
			final String reason) {
		ItemBase ret = super.TransOut(key, number, reason);
		if (ret != null) {
			fire.pb.event.Poster.getPoster()
					.dispatchEvent(
							new fire.pb.event.UnequipItemEvent(roleId, ret
									.getItemId()));
		}
		return ret;
	}

	/**
	 * GM减装备耐久度
	 */
	public SRefreshNaiJiu GMDecEndure(float fDecPercent) {
		SRefreshNaiJiu ret = new SRefreshNaiJiu();
		ret.packid = getPackid();
		for (ItemBase ib : this) {
			if (ib == null)
				continue;
			if (ib instanceof EquipItem) {
				EquipItem ei = (EquipItem) ib;
				ei.setEndure((int)(ei.getCurMaxEndure() * fDecPercent));
				EquipNaiJiu ee = new EquipNaiJiu();
				ee.keyinpack = ei.getKey();
				ee.endure = ei.getEndure();
				ret.data.add(ee);
			}
		}
		return ret;
	}
	
	/**
	 * 获取身上装备的耐久度信息
	 */
	public Map<Integer, ArrayList<Integer>> getEquipsGemInfo() {
		Map<Integer, ArrayList<Integer>> map = new java.util.HashMap<Integer, ArrayList<Integer>>();
		for (ItemBase ib : this) {
			if (ib == null)
				continue;
			if (ib instanceof EquipItem) {
				xbean.Equip equipAttr = ((EquipItem) ib).getEquipAttr();
				ArrayList<Integer> gems = new ArrayList<Integer>(equipAttr.getDiamonds());
				
				if(gems.size() != 0){
					map.put(ib.getItemId(), gems);
				}else {
					map.put(ib.getItemId(), new ArrayList<Integer>());
				}
			}
		}
		return map;
	}
	
	public static void checkEquipDiamondCourse(long roleid) {
		Equip equipBag = (Equip) Module.getInstance().getItemMaps(roleid, BagTypes.EQUIP, false);
		Integer diamondLeveMin = Integer.MAX_VALUE;
		Integer diamondNum = 0;
		for (ItemBase ei : equipBag) {
			if (ei == null) {
				break;
			}
			
			xbean.Equip equipAttrOne = ((EquipItem) ei).getEquipAttr();
			List<Integer> diamondsOne = equipAttrOne.getDiamonds();
			
			int dsize = diamondsOne.size();
			
			if (dsize == 0) {
				break;
			}
			
			int max = 0;
			for (int diamondId : diamondsOne) {
				int tmp = EquipDiamondMgr.getDiamondPropMap().get(diamondId).level;
				if (tmp > max)
					max = tmp;
			}
			
			diamondLeveMin = max < diamondLeveMin ? max : diamondLeveMin;
			diamondNum ++;
		}
		
		if (diamondNum == 6) {
			fire.pb.course.CourseManager.checkAchieveCourse(roleid, fire.pb.course.CourseType.XIANG_QIAN_EQUIP_LEVEL, diamondLeveMin);
			fire.pb.course.CourseManager.achieveUpdate(roleid, fire.pb.course.CourseType.XIANG_QIAN_EQUIP);
		}
	}
}
