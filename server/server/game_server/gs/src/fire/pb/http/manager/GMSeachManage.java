package fire.pb.http.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fire.pb.attr.AttrType;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.clan.ClanUtils;
import fire.pb.http.beans.AttrBean;
import fire.pb.http.beans.BagBean;
import fire.pb.http.beans.BagItem;
import fire.pb.http.beans.ClanMemberBean;
import fire.pb.http.beans.MoneyBean;
import fire.pb.http.beans.PetBagBean;
import fire.pb.http.beans.PetInfoBean;
import fire.pb.http.beans.SkillBean;
import fire.pb.item.BagTypes;
import fire.pb.item.EquipItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMaps;
import fire.pb.item.Module;
import fire.pb.main.Gs;
import fire.pb.pet.Pet;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import net.sf.json.JSONObject;
import xbean.BasicFightProperties;

/**
 * 运营gm查询管理
 *
 */
public class GMSeachManage {

	/**
	 * 获得玩家详情信息
	 * @return
	 */
	public static String getRoleDetail(long roleid) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			xbean.Properties prop = xtable.Properties.select(roleid);
			if (prop == null) {
				return "";
			}
			map.put("userid", prop.getUserid());// 用户id
			xbean.UserDeviceInfo userDeviceInfo = xtable.Userdeviceinfotab.select(prop.getUserid());
			if (userDeviceInfo != null) {
				map.put("username", userDeviceInfo.getUsername());// 用户名称
			}

			map.put("roleid", roleid);// 角色id
			map.put("name", prop.getRolename());// 角色名称
			map.put("level", prop.getLevel());// 等级
			map.put("school", prop.getSchool());// 职业
			map.put("shapeid", prop.getShape());// 模型
			map.put("title", prop.getTitle());// 称号

			int allybNum = 0;// 充值符石数量
			int curyb = 0;
			if (fire.pb.fushi.Module.getIsYYBUser(prop.getUserid())) {
				// 记录符石数,并判断是否需要返符石
				xbean.YybFushiNums yybFs = xtable.Yybfushi.select(prop.getUserid());
				if (yybFs != null) {
					xbean.YybFushiNum yybFushiNum = yybFs.getRolefushi().get(roleid);
					if (yybFushiNum != null) {
						allybNum = yybFushiNum.getSaveamt();// 充值符石数量
						curyb = yybFushiNum.getBalance();// 可交易符石数量
					}
				}
			} else {
				xbean.YbNums ybNums = xtable.Fushinum.select(prop.getUserid());
				if (ybNums != null) {
					xbean.YbNum ybNum = ybNums.getRoleyb().get(roleid); //得到角色对应的符石数量 by changhao
					if (ybNum != null) {
						allybNum = ybNum.getNopresentnum();// 充值符石数量
						curyb = ybNum.getNum();// 可交易符石数量
					}
				}
			}

			map.put("allybNum", allybNum);// 充值符石数量
			map.put("curyb", curyb);// 可交易符石数量
			map.put("createtime", new Date(prop.getCreatetime()).toString());// 账号创建时间
			map.put("onlinetime", new Date(prop.getOnlinetime()).toString());// 角色上次登录时间
			map.put("offlinetime", new Date(prop.getOfflinetime()).toString());// 角色上次离线时间

			// 实时查询：增加人物光环当前状态
			int defaultForm = prop.getDefultform();
			Map<Integer, xbean.FormBean> formMap = prop.getFormationsmap();
			xbean.FormBean fmb = formMap.get(defaultForm);
			if (fmb != null) {
				map.put("light_id", defaultForm);// 光环id
				map.put("light_level", fmb.getLevel());// 光环等级
				map.put("light_exp", fmb.getExp());// 光环经验
			}

			String factionname = "没有公会";
			Long factionid = xtable.Roleidclan.select(roleid);
			if (factionid != null) {
				xbean.ClanInfo clanInfo = xtable.Clans.select(factionid);
				if (clanInfo != null) {
					factionname = clanInfo.getClanname();
					factionid = (long) clanInfo.getIndex();
				}
			} else {
				factionid = 0l;
			}
			map.put("factionid", factionid);// 公会id
			map.put("factionname", factionname);// 公会名称

			// 属性相关
			map.put("hp", prop.getHp());
			map.put("mp", prop.getMp());
			map.put("sp", prop.getSp());

			fire.pb.effect.RoleImpl erole = new fire.pb.effect.RoleImpl(roleid, true);

			// 已分配的五项基本属性
			BasicFightProperties bfp = prop.getBfp();

			map.put("agi", bfp.getAgi());// 敏捷
			map.put("cons", bfp.getCons());// 体质
			map.put("endu", bfp.getEndu());// 耐力
			map.put("iq", bfp.getIq());// 智力
			map.put("str", bfp.getStr());// 力量

			// map.put("agi_save", prop.getAddpointfp().getAgi_save());//已分配敏捷
			// map.put("cons_save", prop.getAddpointfp().getCons_save());//已分配体质
			// map.put("endu_save", prop.getAddpointfp().getEndu_save());//已分配耐力
			// map.put("iq_save", prop.getAddpointfp().getIq_save());//已分配智力
			// map.put("str_save", prop.getAddpointfp().getStr_save());//已分配力量

			map.put("qianvalue", prop.getPoint().get(prop.getScheme()));// 潜能。未分配点数
			map.put("factionvalue", ClanUtils.getCurrentClanPoint(roleid));// 帮贡信息

			// map.put("petindex", prop.getFightpetkey());//当前战斗宠物
			// map.put("smoney", prop.getSmoney());// 储备金
			// map.put("showpet", prop.getShowpetkey());//当前展示宠物
			// map.put("uplimithp", erole.getUplimithp());//受伤后的血量最大值
			map.put("maxhp", erole.getMaxHp());// 最大生命上限
			map.put("maxmp", erole.getMaxMp());// 最大法力
			// map.put("maxsp", erole.getMaxSp());//最大怒气

			map.put("hit", erole.getHit());// 命中值
			map.put("damage", erole.getAttack());// 攻击
			map.put("defend", erole.getDefend());// 防御
			map.put("speed", erole.getSpeed());// 速度
			// map.put("dodge", erole.getDodge());//躲避值
			map.put("magicattack", erole.getMagicAttack());// 法术攻击力
			map.put("magicdef", erole.getMagicDef());// 法术防御力
			// map.put("enlimit", erole.getEnlimit());//活力上限
			// map.put("pflimit", erole.getPflimit());//体力上限
			map.put("medical", erole.getAttrById(AttrType.MEDICAL));// 治疗强度
			map.put("seal", erole.getAttrById(AttrType.SEAL));// 控制命中
			map.put("unseal", erole.getAttrById(AttrType.UNSEAL));// 控制抗性
			// map.put("phy_critc_level", erole.getAttrById(AttrType.PHY_CRITC_LEVEL));//物理暴击等级
			// map.put("anti_phy_critc_level", erole.getAttrById(AttrType.ANTI_PHY_CRITC_LEVEL));//物理抗性等级
			// map.put("magic_critc_level", erole.getAttrById(AttrType.MAGIC_CRITC_LEVEL));//魔法暴击等级
			// map.put("anti_magic_critc_level", erole.getAttrById(AttrType.ANTI_MAGIC_CRITC_LEVEL));//魔法抗性等级
			// map.put("phy_critc_pct", erole.getAttrById(AttrType.PHY_CRIT_PCT));//物理暴击程度（初始为200%,即2倍普通伤害）
			// map.put("magic_critc_pct", erole.getAttrById(AttrType.MAGIC_CRIT_PCT));//魔法暴击程度（初始为200%）
			// map.put("heal_critc_level", erole.getAttrById(AttrType.HEAL_CRIT_LEVEL));//治疗暴击等级
			// map.put("heal_critc_pct", erole.getAttrById(AttrType.HEAL_CRIT_PCT));//治疗暴击程度
			// map.put("anti_critc_level", erole.getAttrById(AttrType.ANTI_CRIT_LEVEL));//暴击抗性等级

			// map.put("nexp", erole.getNextExp());//升级经验
			map.put("exp", prop.getExp());// 人物的经验
			// map.put("energy", prop.getEnergy());//活力


			fire.pb.buff.BuffRoleImpl bri = new fire.pb.buff.BuffRoleImpl(roleid, true);
			ConstantlyBuff buff = bri.getBuff(BuffConstant.ROLE_RECOVER_HP);
			if (buff != null) {
				map.put("hp_recover", buff.getAmount());// 生命储备
			}
			ConstantlyBuff buffmp = bri.getBuff(BuffConstant.ROLE_RECOVER_MP);
			if (buffmp != null) {
				map.put("mp_recover", buffmp.getAmount());// 魔法储备
			}
			// 代币相关
			List<MoneyBean> moneylists = new ArrayList<MoneyBean>();
			ItemMaps bag1 = Module.getInstance().getItemMaps(roleid, BagTypes.BAG, true);
			java.util.HashMap<Byte, Long> currency = bag1.getPackInfo().currency;
			for (Entry<Byte, Long> entry : currency.entrySet()) {
				if (entry != null) {
					MoneyBean moneyBean = new MoneyBean(entry.getKey(), entry.getValue());
					moneylists.add(moneyBean);
				}
			}
			map.put("moneyinfos", moneylists);

			// 背包道具相关
			List<BagBean> bags = new ArrayList<BagBean>();
			for (int i = BagTypes.BAG; i <= BagTypes.TEMP; i++) {
				BagBean bagbean = new BagBean();
				bagbean.setBagtype(i);
				ItemMaps bagContainer = Module.getInstance().getItemMaps(roleid, i, true);
				for (fire.pb.Item item : bagContainer.getPackInfo().items) {
					if (item == null) {
						continue;
					}
					ItemBase basicItem = bagContainer.getItem(item.key);
					if (basicItem == null) {
						continue;
					}
					if (basicItem instanceof EquipItem) {
						EquipItem equip = (EquipItem) basicItem;
						BagItem bagitem = new BagItem();
						bagitem.setKey(item.key);// 装备key
						bagitem.setItemname(equip.getName());// 装备名称
						bagitem.setNum(1);// 物品数量
						bagitem.setEquiptype(equip.getEquipType());// 装备类型
						bagitem.setColor(equip.getItemAttr().nquality);// 装备品级
						bagitem.setEquiplevel(equip.getEquipAttr().getEquiplevel());// 装备等级
						// 基础属性
						Map<Integer, Integer> baseatt = equip.getEquipAttr().getAttr();
						for (Entry<Integer, Integer> entry : baseatt.entrySet()) {
							if (entry != null) {
								AttrBean appendAttrBean = new AttrBean(entry.getKey(), entry.getValue());
								bagitem.getBaseattr().add(appendAttrBean);// 附加属性
							}
						}
						bagitem.setDiamonds(equip.getEquipAttr().getDiamonds());// 宝石属性
						Map<Integer, Integer> addatt = equip.getEquipAttr().getAddattr();
						for (Entry<Integer, Integer> entry : addatt.entrySet()) {
							if (entry != null) {
								AttrBean appendAttrBean = new AttrBean(entry.getKey(), entry.getValue());
								bagitem.getAppendattr().add(appendAttrBean);// 附加属性
							}
						}
						bagitem.setEffect(equip.getEquipAttr().getEffect());// 装备特效
						bagitem.setEndure(equip.getEquipAttr().getEndure());// 装备耐久
						bagitem.setEquipscore(equip.getEquipAttr().getEquipscore());// 装备评分

						bagbean.getBagitems().add(bagitem);
					} else {
						BagItem bagitem = new BagItem();
						bagitem.setKey(item.key);
						bagitem.setItemname(basicItem.getName());
						bagitem.setNum(basicItem.getNumber());// 物品数量
						bagbean.getBagitems().add(bagitem);
					}
				}
				bags.add(bagbean);
			}
			map.put("bags", bags);

			// 技能信息
			List<SkillBean> skilllist = new ArrayList<SkillBean>();
			xbean.SkillRole srole = xtable.Skillroles.select(roleid);
			for (Map.Entry<Integer, Integer> entry : srole.getInborns().entrySet()) {
				SkillBean skillBean = new SkillBean(entry.getKey(), entry.getValue());
				skilllist.add(skillBean);
			}
			map.put("skills", skilllist);

			// 拍卖行
			getRoleInfoAboutMarket(roleid, map);

			JSONObject jsonObject = JSONObject.fromObject(map);
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取角色信息时获取拍卖物品信息 yebin added @ 2016年8月4日 下午8:49:18
	 * @param roleid
	 * @param map
	 */
	private final static void getRoleInfoAboutMarket(long roleid, Map<String, Object> map) {
		ItemMaps itemcol = Module.getInstance().getItemMaps(roleid, BagTypes.MARKET, true);
		ArrayList<BagBean> marketItems = new ArrayList<>();
		BagBean bagbean = new BagBean();
		bagbean.setBagtype(BagTypes.MARKET);
		for (ItemBase basicItem : itemcol) {
			if (basicItem == null) {
				continue;
			}

			if (basicItem instanceof EquipItem) {
				EquipItem equip = (EquipItem) basicItem;
				BagItem bagitem = new BagItem();
				bagitem.setKey(basicItem.getKey());// 装备key
				bagitem.setItemname(equip.getName());// 装备名称
				bagitem.setNum(1);// 物品数量
				bagitem.setEquiptype(equip.getEquipType());// 装备类型
				bagitem.setColor(equip.getItemAttr().nquality);// 装备品级
				bagitem.setEquiplevel(equip.getEquipAttr().getEquiplevel());// 装备等级
				// 基础属性
				Map<Integer, Integer> baseatt = equip.getEquipAttr().getAttr();
				for (Entry<Integer, Integer> entry : baseatt.entrySet()) {
					if (entry != null) {
						AttrBean appendAttrBean = new AttrBean(entry.getKey(), entry.getValue());
						bagitem.getBaseattr().add(appendAttrBean);// 附加属性
					}
				}
				bagitem.setDiamonds(equip.getEquipAttr().getDiamonds());// 宝石属性
				Map<Integer, Integer> addatt = equip.getEquipAttr().getAddattr();
				for (Entry<Integer, Integer> entry : addatt.entrySet()) {
					if (entry != null) {
						AttrBean appendAttrBean = new AttrBean(entry.getKey(), entry.getValue());
						bagitem.getAppendattr().add(appendAttrBean);// 附加属性
					}
				}
				bagitem.setEffect(equip.getEquipAttr().getEffect());// 装备特效
				bagitem.setEndure(equip.getEquipAttr().getEndure());// 装备耐久
				bagitem.setEquipscore(equip.getEquipAttr().getEquipscore());// 装备评分

				bagbean.getBagitems().add(bagitem);
			} else {
				BagItem bagitem = new BagItem();
				bagitem.setKey(basicItem.getKey());
				bagitem.setItemname(basicItem.getName());
				bagitem.setNum(basicItem.getNumber());// 物品数量

				bagbean.getBagitems().add(bagitem);
			}
		}
		marketItems.add(bagbean);
		map.put("marketitems", marketItems);

		PetColumn petcol = new PetColumn(roleid, PetColumnTypes.MARKETPET, true);
		List<PetBagBean> marketPets = new ArrayList<PetBagBean>();
		PetBagBean petBagBean = new PetBagBean();
		petBagBean.setType(BagTypes.MARKET);
		for (Pet pet : petcol.getPets()) {
			xbean.PetInfo petInfo = pet.getPetInfo();
			if (petInfo == null) {
				continue;
			}

			PetInfoBean petInfoBean = new PetInfoBean();
			petBagBean.getPets().add(petInfoBean);
			petInfoBean.setKey(petInfo.getKey());
			petInfoBean.setUniqId(petInfo.getUniqid());// 宠物唯一ID
			petInfoBean.setId(petInfo.getId());// 宠物ID
			petInfoBean.setFightflag(0);
			petInfoBean.setName(petInfo.getName());// 宠物名称
			petInfoBean.setLv(petInfo.getLevel());// 宠物等级
			petInfoBean.setColor(petInfo.getKind());// 宠物品级
			petInfoBean.setGrowRate(petInfo.getGrowrate());// 宠物成长
			petInfoBean.setAttackApt(petInfo.getBornattackapt());// 攻击资质
			petInfoBean.setDefendApt(petInfo.getBorndefendapt());// 防御资质
			petInfoBean.setPhyforceApt(petInfo.getBornphyforceapt());// 体力资质
			petInfoBean.setMagicApt(petInfo.getBornmagicapt());// 法术资质
			petInfoBean.setSpeedApt(petInfo.getBornspeedapt());// 速度资质
			petInfoBean.setPoint(petInfo.getPoint());// 剩余潜力点
			petInfoBean.setCons(petInfo.getBfp().getCons());// 体质
			petInfoBean.setIq(petInfo.getBfp().getIq());// 智力
			petInfoBean.setStr(petInfo.getBfp().getStr());// 力量
			petInfoBean.setEndu(petInfo.getBfp().getEndu());// 耐力
			petInfoBean.setAgi(petInfo.getBfp().getAgi());// 敏捷

			fire.pb.effect.Role epet = new fire.pb.effect.PetImpl(petInfo);
			petInfoBean.setMaxhp(epet.getMaxHp());// 生命
			petInfoBean.setMaxmp(epet.getMaxMp());// 魔法
			petInfoBean.setAtk(epet.getAttack());// 物理攻击
			petInfoBean.setDef(epet.getDefend());// 物理防御
			petInfoBean.setSpeed(epet.getSpeed());// 速度
			petInfoBean.setMatk(epet.getMagicAttack());// 法术攻击
			petInfoBean.setMdef(epet.getMagicDef());// 法术防御
			for (xbean.PetSkill skill : petInfo.getSkills()) {
				petInfoBean.getSkillid().add(skill.getSkillid());
			}
		}
		marketPets.add(petBagBean);
		map.put("marketpets", marketPets);
	}

	/**
	 * 获得宠物信息
	 * 
	 * @param roleid
	 * @return
	 */
	public static String getPetDetail(long roleid) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			xbean.Properties prop = xtable.Properties.select(roleid);
			if (prop == null) {
				return "";
			}
			int fightPetKey = xtable.Properties.selectFightpetkey(roleid);

			map.put("userid", prop.getUserid());// 用户id
			map.put("roleid", roleid);// 角色id
			map.put("name", prop.getRolename());// 角色名称

			List<PetBagBean> bags = new ArrayList<PetBagBean>();
			for (int i = PetColumnTypes.PET; i <= PetColumnTypes.DEPOT; i++) {
				PetBagBean petBagBean = new PetBagBean();
				petBagBean.setType(i);
				PetColumn petcol = new PetColumn(roleid, i, true);
				for (Pet pet : petcol.getPets()) 
				{
					xbean.PetInfo petInfo = pet.getPetInfo();
					if (petInfo == null) {
						continue;
					}

					PetInfoBean petInfoBean = new PetInfoBean();
					petBagBean.getPets().add(petInfoBean);
					petInfoBean.setKey(petInfo.getKey());
					petInfoBean.setUniqId(petInfo.getUniqid());// 宠物唯一ID
					petInfoBean.setId(petInfo.getId());// 宠物ID
					if (fightPetKey == petInfo.getKey()) {
						petInfoBean.setFightflag(1);
					} else {// 是否参战
						petInfoBean.setFightflag(0);
					}
					petInfoBean.setName(petInfo.getName());// 宠物名称
					petInfoBean.setLv(petInfo.getLevel());// 宠物等级
					petInfoBean.setColor(petInfo.getKind());// 宠物品级
					petInfoBean.setGrowRate(petInfo.getGrowrate());// 宠物成长
					petInfoBean.setAttackApt(petInfo.getBornattackapt());// 攻击资质
					petInfoBean.setDefendApt(petInfo.getBorndefendapt());// 防御资质
					petInfoBean.setPhyforceApt(petInfo.getBornphyforceapt());// 体力资质
					petInfoBean.setMagicApt(petInfo.getBornmagicapt());// 法术资质
					petInfoBean.setSpeedApt(petInfo.getBornspeedapt());// 速度资质
					petInfoBean.setPoint(petInfo.getPoint());// 剩余潜力点

					petInfoBean.setCons(petInfo.getBfp().getCons());// 体质
					petInfoBean.setIq(petInfo.getBfp().getIq());// 智力
					petInfoBean.setStr(petInfo.getBfp().getStr());// 力量
					petInfoBean.setEndu(petInfo.getBfp().getEndu());// 耐力
					petInfoBean.setAgi(petInfo.getBfp().getAgi());// 敏捷


					fire.pb.effect.Role epet = new fire.pb.effect.PetImpl(petInfo);
					petInfoBean.setMaxhp(epet.getMaxHp());// 生命
					petInfoBean.setMaxmp(epet.getMaxMp());// 魔法
					petInfoBean.setAtk(epet.getAttack());// 物理攻击
					petInfoBean.setDef(epet.getDefend());// 物理防御
					petInfoBean.setSpeed(epet.getSpeed());// 速度
					petInfoBean.setMatk(epet.getMagicAttack());// 法术攻击
					petInfoBean.setMdef(epet.getMagicDef());// 法术防御
					for (xbean.PetSkill skill : petInfo.getSkills()) {
						petInfoBean.getSkillid().add(skill.getSkillid());
					}
				}
				bags.add(petBagBean);
			}
			map.put("petbags", bags);
			JSONObject jsonObject = JSONObject.fromObject(map);
			// System.out.println(jsonObject.toString());
			return jsonObject.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 查询公会信息
	 * 
	 * @param factionId
	 * @return
	 */
	public static String getFactionDetail(int factionId) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			xbean.ClanInfo clanInfo = fire.pb.clan.Module.clanListMap.get(factionId);
			if (clanInfo == null) {
				return "";
			}
			xbean.Properties properties = xtable.Properties.select(clanInfo.getClanmaster());
			clanInfo = ClanUtils.getClanInfoById(clanInfo.getClanmaster(), true);
			map.put("factionId", factionId);// 公会ID
			map.put("factionname", clanInfo.getClanname());// 公会名称
			map.put("factionlevel", clanInfo.getClanlevel());// 公会等级
			map.put("membersnum", clanInfo.getMembers().size());// 公会人数
			map.put("factionmoney", clanInfo.getMoney());// 公会资金
			map.put("mastername", properties.getRolename());// 公会会长
			map.put("factionrank", clanInfo.getClanrank());// 公会排名

			List<ClanMemberBean> members = new ArrayList<ClanMemberBean>();
			for (Map.Entry<Long, xbean.ClanMemberInfo> memberEntry : clanInfo.getMembers().entrySet()) {
				ClanMemberBean factionMemberBean = new ClanMemberBean();
				factionMemberBean.setRoleid(memberEntry.getKey());
				xbean.Properties prop = xtable.Properties.select(memberEntry.getKey());
				factionMemberBean.setName(prop.getRolename());// 角色名称
				factionMemberBean.setLevel(prop.getLevel());// 角色等级
				factionMemberBean.setSchool(prop.getSchool());// 角色职业
				factionMemberBean.setPosition(memberEntry.getValue().getClanposition());// 公会职位
				factionMemberBean.setAddtime(new Date(memberEntry.getValue().getJointime()).toString());// 加入公会时间

				xbean.RoleClanPoint clanpoint = ClanUtils.getRoleClanPoint(memberEntry.getKey(), true);
				if (clanpoint != null) {
					factionMemberBean.setCurrentContribution(clanpoint.getCurrentclanpoint());// 现有贡献
					factionMemberBean.setHistoryContribution(clanpoint.getHistoryclanpoint());// 历史帮贡
					factionMemberBean.setWeekContribution(memberEntry.getValue().getWeekclanpoint());// 周帮贡
				}
				members.add(factionMemberBean);
			}

			map.put("members", members);

			JSONObject jsonObject = JSONObject.fromObject(map);
			// System.out.println(jsonObject.toString());
			return jsonObject.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取空间大师礼物剩余数量
	 * 
	 * @author yangtao
	 * @dateTime 2016年6月22日 下午5:33:47
	 * @version 1.0
	 * @return
	 */
	public static Map<String, Object> getSpaceGiftNum() {
		Map<String, Object> map = new HashMap<String, Object>();
		int num = 0;
		xbean.RoleSpace rs = xtable.Rolespaces.select(Long.MAX_VALUE);
		if (rs != null) {
			num = rs.getGift();
		}
		map.put("Amount", num);
		return map;
	}
}
