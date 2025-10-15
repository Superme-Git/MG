package fire.pb.item;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fire.msp.move.GRoleEquipChange;
import fire.pb.GsClient;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.item.equip.WeaponItem;
import fire.pb.skill.SkillConstant;
import fire.pb.util.RolePropConf;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

public abstract class EquipItem extends ItemBase {
	public enum EquipError {
		LEVEL_NOT_SUIT, NO_ERROR, POS_NOT_SUIT, SEX_NOT_SUIT, SHAPE_NOT_SUIT, SCHOOL_NOT_SUIT, ZERO_ENDURE
	}

	private xbean.Equip equipAttr;

	public EquipItem(ItemMgr im, int itemid) {
		super(im, itemid);
		equipAttr = xbean.Pod.newEquip();
		equipAttr.setEquiplevel(itemAttr.getLevel());
		Long nextkey = xtable.Equips.insert(equipAttr);
		itemData.setExtid(nextkey);
	}

	public EquipItem(ItemMgr im, int itemid, mkdb.Bean extinfo) {
		super(im, itemid);
		equipAttr = xbean.Pod.newEquip();
		equipAttr.setEquiplevel(itemAttr.getLevel());
		Long nextkey = xtable.Equips.insert(equipAttr);
		itemData.setExtid(nextkey);
		setExtinfo(extinfo);
	}

	public EquipItem(ItemMgr im, xbean.Item item) {
		super(im, item);
		if (!item.isData()) {
			equipAttr = xtable.Equips.get(item.getExtid());
		} else {
			equipAttr = xtable.Equips.select(item.getExtid());
		}
		if (equipAttr == null) {
			Module.getInstance().getLogger().error("数据缺失");
		}
	}

	public EquipError canEquipment(final int pos, final int roleLevel,
			final int rolesex, final int shape, final int school) {
		if (pos != getEquipPos())
			return EquipError.POS_NOT_SUIT;
		if (equipAttr.getEndure() <= 0)
			return EquipError.ZERO_ENDURE;
		int requirelevel = itemAttr.needlevel;
		if (equipAttr.getEffect() == SkillConstant.SPECIAL_SKILL_LESS_LV_LIMIT) {
			requirelevel = requirelevel > 5 ? requirelevel - 5 : 1;
		}
		if (equipAttr.getEffect() == SkillConstant.SPECIAL_SKILL_NO_LV_LIMIT)
			requirelevel = 1;

		ArrayList<Integer> shapes = ((EquipItemShuXing) itemAttr).roleNeed;
		if (shapes != null && !shapes.isEmpty()
				&& !shapes.contains(RolePropConf.getShapeidByXshapeid(shape))) {
			return EquipError.SHAPE_NOT_SUIT;
		}

		if (roleLevel < requirelevel)
			return EquipError.LEVEL_NOT_SUIT;
		if (!isNeedSex(rolesex))
			return EquipError.SEX_NOT_SUIT;
		if (!isNeedSchool(school))
			return EquipError.SCHOOL_NOT_SUIT;
		
		return EquipError.NO_ERROR;
	}

	public Map<Integer, Integer> getBaseAttr() {
		Map<Integer, Integer> baseAttr = new HashMap<Integer, Integer>();
		baseAttr.putAll(equipAttr.getAttr()); // 基础属性
		baseAttr.putAll(equipAttr.getAddattr()); // 附加属性
		return baseAttr;
	}

	public int getCurMaxEndure() {
		return equipAttr.getCurmaxendure();
	}

	protected void getEffects(java.util.Map<Integer, Float> effects) {
		// 从配置表里读取基础属性 TODO 去掉原来的基础属性添加方式,改成从数据库里读,以前的是在表里配置的.
		Map<Integer, Integer> baseAttr = this.getBaseAttr();
		for (Entry<Integer, Integer> attr : baseAttr.entrySet()) {
			int key = attr.getKey();
			int value = attr.getValue();
			effects.put(key, (float) value);
		}

		for (Entry<Integer, xbean.EnhancementData> e : equipAttr
				.getEnhancement().entrySet()) {
			// 增加附魔属性 by changhao
			for (Entry<Integer, Integer> attr : e.getValue().getEnhancementattr().entrySet()) {
				int key = attr.getKey();
				float value = (float) attr.getValue();
				Float v = effects.get(key);
				if (v == null) {
					effects.put(key, (float) value);
				} else {
					effects.put(key, v + value);
				}
			}
		}

		if (equipAttr.getDiamonds().size() > 0) {
			for (Integer gemID : equipAttr.getDiamonds()) {
				GemItem.addGemProp(gemID, effects, false);
			}
		}
	}

	public void getEffectsAndBuffs(java.util.Map<Integer, Float> effects,
			List<ConstantlyBuffConfig> buffs) {
		getEffects(effects);
	}

	public Octets getEmptyTips() {
		if (os == null) {
			os = new OctetsStream();
			os.marshal(0);
			os.marshal(0);
			os.marshal(equipAttr.getEndure());
			os.marshal(equipAttr.getCurmaxendure());
			os.marshal(0);
			os.marshal(0);
			os.marshal(0);
			try {
				os.marshal(new Octets(equipAttr.getProducer().getBytes(
						"UTF-16LE")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} 

			os.marshal(0);
			os.marshal(0);
			os.marshal(0);

			os.marshal(0);

			os.marshal(0);

			os.marshal(0);
			os.marshal(0);
			os.marshal(0);
		}
		return os;
	}

	public int getEndure() {
		return equipAttr.getEndure();
	}

	public xbean.Equip getEquipAttr() {
		return equipAttr;
	}

	public int getEquipPos() {
		return getEquipType();
	}

	public abstract int getEquipType();

	@Override
	public xbean.Equip getExtInfo() {
		return equipAttr;
	}

	protected int getGrowattr(final int type) {
		// 从配置表里读取基础属性
		Map<Integer, Integer> baseAttr = this.getBaseAttr();

		final Integer value = baseAttr.get(type);
		if (value == null)
			return 0;
		return value.intValue();
	};

	@Override
	public EquipItemShuXing getItemAttr() {
		return (EquipItemShuXing) itemAttr;
	}

	@Override
	public String getName() {
		return itemAttr.name;
	}

	protected int getFailTimes() {
		return equipAttr.getRepairtimes();
	}
	
	@Override
	public Octets getTips() {
		return getTipsAgain();
	}

	public Octets getTipsAgain() {
		if (os == null) {
			os = new OctetsStream();

			Map<Integer, Integer> baseMap = equipAttr.getAttr();
			int baseAttrNum = baseMap.size();
			os.marshal(baseAttrNum);

			Iterator<Entry<Integer, Integer>> baseAttr = baseMap.entrySet()
					.iterator();
			while (baseAttr.hasNext()) {
				Entry<Integer, Integer> current = baseAttr.next();
				os.marshal(current.getKey() - current.getKey() % 10);
				os.marshal(current.getValue());
			}

			// 附加属性
			Map<Integer, Integer> addMap = equipAttr.getAddattr();
			int addNum = addMap.size();
			os.marshal(addNum);

			Iterator<Entry<Integer, Integer>> addAttr = addMap.entrySet()
					.iterator();
			while (addAttr.hasNext()) {
				Entry<Integer, Integer> current = addAttr.next();
				os.marshal(current.getKey() - current.getKey() % 10);
				os.marshal(current.getValue());
			}

			os.marshal(equipAttr.getSkill());
			os.marshal(equipAttr.getEffect());

			// 镶嵌宝石
			int diamondNum = equipAttr.getDiamonds().size();
			os.marshal(diamondNum);
			if (diamondNum > 0) {
				for (Integer diamondID : equipAttr.getDiamonds()) {
					os.marshal(diamondID);
				}
			}

			os.marshal(equipAttr.getEndure());
			os.marshal(equipAttr.getMaxendure());
			os.marshal(equipAttr.getRepairtimes());
			os.marshal(equipAttr.getEquipscore());
			try {
				os.marshal(new Octets(equipAttr.getProducer().getBytes(
						"UTF-16LE")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			int size = equipAttr.getEnhancement().size();
			os.marshal(size);
			for (Entry<Integer, xbean.EnhancementData> e : equipAttr
					.getEnhancement().entrySet()) {
				// 附魔属性 by changhao
				Map<Integer, Integer> enhancementAttr = e.getValue()
						.getEnhancementattr();
				int enhancementAttrNum = enhancementAttr.size();
				os.marshal(enhancementAttrNum);

				Iterator<Entry<Integer, Integer>> enhanceAttr = enhancementAttr
						.entrySet().iterator();
				while (enhanceAttr.hasNext()) {
					Entry<Integer, Integer> current = enhanceAttr.next();
					os.marshal(current.getKey() - current.getKey() % 10);
					os.marshal(current.getValue());
				}

				os.marshal(e.getValue().getEnhancementtime()); // 写入附魔结束时间 by
																// changhao
			}
		}
		return os;
	}

	public int getTreasure() {
		return this.equipAttr.getTreasure();
	}

	private boolean isEffectItem() {
		return this instanceof WeaponItem;
	}

	private boolean isNeedSex(int rolesex) {
		int needSex = ((EquipItemShuXing) itemAttr).needSex;
		if (needSex == 0)
			return true;
		return rolesex == needSex;
	}
	
	private boolean isNeedSchool(Integer school) {
		List<Integer> schoolLst = Module.itemSchoolData.get(getItemId());
		if (schoolLst == null)
			return true;
		
		if (schoolLst.get(0) == 0)
			return true;
		
		if (schoolLst.contains(school))
			return true;
		
		return false;
	}

	private void loseEffect() {
		if (isEffectItem()) {
			GRoleEquipChange notifymap = new GRoleEquipChange();
			notifymap.roleid = roleid;
			notifymap.pos = getEquipPos();
			notifymap.itemid = 0;
			notifymap.ride = -1;
			notifymap.effect = -1;
			GsClient.pSendWhileCommit(notifymap);
			fire.pb.team.Team team = fire.pb.team.TeamManager
					.selectTeamByRoleId(roleid);
			if (team != null) {
				team.updateTeamMemberComponents2Others(roleid);
			}
		}
		refreshEquipBuff();
	}

	public boolean loseEndure(int lose) {
		if (equipAttr.isData()) {
			Module.logger.error("装备属性为只读");
			return false;
		}
		if (equipAttr.getEndure() <= 0) {
			Module.logger.error("装备耐久为" + equipAttr.getEndure());
			return false;
		}
		Module.logger.debug("物品耐久为" + equipAttr.getExtendure());
		int alllose = lose + equipAttr.getExtendure();
		if (alllose >= 100) {
			setEndure(equipAttr.getEndure() - alllose / 100);
		}
		equipAttr.setExtendure(alllose % 100);
		if (equipAttr.getEndure() < 0)
			equipAttr.setEndure(0);
		Module.logger.debug("物品" + this.getPosition() + "的ext耐久为"
				+ equipAttr.getExtendure());

		Module.logger.debug("装备耐久=" + equipAttr.getEndure());
		return true;
	}

	@Override
	public void onDeleted() {
		xtable.Equips.remove(this.itemData.getExtid());
	}

	@Override
	public void onInserted() {
	}

	private void refreshEquipBuff() {
		fire.pb.skill.SceneSkillRole role = fire.pb.skill.SkillManager
				.getSceneSkillRole(roleid);
		java.util.HashMap<Integer, Float> effects = new java.util.HashMap<Integer, Float>();
		java.util.ArrayList<fire.pb.buff.continual.ConstantlyBuffConfig> cbuffs = new java.util.ArrayList<fire.pb.buff.continual.ConstantlyBuffConfig>();
		this.getEffectsAndBuffs(effects, cbuffs);
		fire.pb.skill.Result r = role.unequip(this.getEquipType(), cbuffs);
		fire.pb.attr.SRefreshRoleData snd = new fire.pb.attr.SRefreshRoleData();
		snd.datas = (java.util.HashMap<Integer, Float>) r.getChangedAttrs();
		mkdb.Procedure.psendWhileCommit(roleid, snd);
	}

	private void resumeEffect() {
		if (isEffectItem()) {
			GRoleEquipChange notifymap = new GRoleEquipChange();
			notifymap.roleid = roleid;
			notifymap.pos = getEquipPos();
			notifymap.itemid = itemData.getId();
			notifymap.ride = -1;
			notifymap.effect = -1;
			GsClient.pSendWhileCommit(notifymap);
			fire.pb.team.Team team = fire.pb.team.TeamManager
					.selectTeamByRoleId(roleid);
			if (team != null) {
				team.updateTeamMemberComponents2Others(roleid);
			}
		}
		refreshEquipBuff();
	}

	public void SetAddAttr(Map<Integer, Integer> addAttrs) {
		// 装备的附加属性
		this.equipAttr.getAddattr().putAll(addAttrs);
	}

	public void SetBaseAttr(Map<Integer, Integer> baseAttrs) {
		this.equipAttr.getAttr().putAll(baseAttrs);
	}

	protected void setCurMaxEndure(int endure) {
		if (equipAttr.isData()) {
			throw new RuntimeException("EquipItem is read only");
		}
		if (endure >= 0 && endure != equipAttr.getRepairtimes()) {
			equipAttr.setCurmaxendure(endure);
			if (roleid != 0) {
				SRefreshMaxNaiJiu send = new SRefreshMaxNaiJiu();
				send.packid = this.packid;
				send.keyinpack = this.keyinpack;
				send.maxendure = equipAttr.getCurmaxendure();
				mkdb.Procedure.psendWhileCommit(roleid, send);
			}
		}
	}

	public void setEndure(int endure) {
		if (equipAttr.isData()) {
			throw new RuntimeException("EquipItem is read only");
		}
		if (endure == equipAttr.getEndure()) {
			return;
		}
		if (equipAttr.getEndure() < 1 && endure < 1) {
			return;
		}
		int oldendure = equipAttr.getEndure();
		if (endure > equipAttr.getCurmaxendure()) {
			equipAttr.setEndure(equipAttr.getCurmaxendure());
			equipAttr.setExtendure(0);
		} else {
			equipAttr.setEndure(endure);
		}
		if (getPackId() == BagTypes.EQUIP) {
			if (oldendure <= 0 && equipAttr.getEndure() > 0) {
				resumeEffect();
			} else if (oldendure > 0 && equipAttr.getEndure() <= 0) {
				loseEffect();
			}
		}

		SRefreshNaiJiu send = new SRefreshNaiJiu();
		send.packid = packid;
		fire.pb.item.EquipNaiJiu ee = new fire.pb.item.EquipNaiJiu();
		ee.endure = equipAttr.getEndure();
		ee.keyinpack = this.keyinpack;
		send.data.add(ee);
		mkdb.Procedure.psendWhileCommit(roleid, send);
	}

	public void setEnhancementAttr(int type, Map<Integer, Integer> addAttrs,
			long time) {
		xbean.EnhancementData data = xbean.Pod.newEnhancementData();
		data.setEnhancementtime(time);
		data.getEnhancementattr().putAll(addAttrs);

		this.equipAttr.getEnhancement().put(type, data);
	}

	public void setEquipEndure() {
		EquipItemShuXing equipAttr = (EquipItemShuXing) itemAttr;
		getEquipAttr().setEndure(equipAttr.getMaxnaijiu());
		getEquipAttr().setCurmaxendure(equipAttr.getMaxnaijiu());
		getEquipAttr().setMaxendure(equipAttr.getMaxnaijiu());
	}

	private void setExtinfo(mkdb.Bean extinfo) {
		if (extinfo instanceof xbean.Equip) {
			equipAttr = (xbean.Equip) extinfo;
			Long nextkey = xtable.Equips.insert(equipAttr);
			itemData.setExtid(nextkey);
		}
	}

	protected void setFailTimes(final int times) {
		if (equipAttr.isData()) {
			throw new RuntimeException("EquipItem is read only");
		}
		if (times >= 0 && times != equipAttr.getRepairtimes()) {
			equipAttr.setRepairtimes(times);
			if (roleid != 0) {
				SXiuLiFailTimes send = new SXiuLiFailTimes();
				send.packid = this.packid;
				send.keyinpack = this.keyinpack;
				send.failtimes = equipAttr.getRepairtimes();
				mkdb.Procedure.psendWhileCommit(roleid, send);
			}
		}
	}

	public int updateEnhancementTimeOut() {
		final long now = Calendar.getInstance().getTimeInMillis();
		java.util.List<Integer> removes = new java.util.ArrayList<Integer>();
		for (java.util.Map.Entry<Integer, xbean.EnhancementData> e : this.equipAttr.getEnhancement().entrySet()) {
			long t = e.getValue().getEnhancementtime();

			if (t != 0 && now > t) {
				removes.add(e.getKey());
			}
		}

		for (Integer i : removes) {
			this.equipAttr.getEnhancement().remove(i);
		}

		return removes.size();
	}
	
	public void changeEquipID(ItemMgr im, int itemid) {
		itemAttr = im.getAttr(itemid);
		this.itemData.setId(itemid);
		this.itemData.setTypeid(itemAttr.getTypeid());
		setFlag(getIniFlag());
	}
	
	@Override
	public void onTimeout() {
		super.onTimeout();
		loseEffect();
		
		/*boolean inbattle = isInBattle();
		if (!inbattle) {
			loseEffect();
		} else {
			new PSetBattleEndProcedure(roleid, new mkdb.Procedure() {
				@Override protected boolean process() {
					if (roleid == 0)
						return false;
					ItemMaps bag = Module.getInstance().getItemMaps(roleid, packid, false);
					if (bag == null)
						return false;
					ItemBase item = bag.getItem(keyinpack);
					if (item == null)
						return false;
					if (item instanceof EquipItem)
						((EquipItem)item).loseEffect();
					else if (item instanceof TimeOutItem) {
						((EquipItem)((TimeOutItem)item).getEffectItem()).loseEffect();
					}
					return true;
				}
			});
		}*/
	}
}
