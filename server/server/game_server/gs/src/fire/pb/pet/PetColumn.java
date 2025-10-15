package fire.pb.pet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;

import fire.log.YYLogger;
import fire.pb.DataInit;
import fire.pb.PropRole;
import fire.pb.buff.BuffAgent;
import fire.pb.buff.BuffRoleImpl;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.course.CourseManager;
import fire.pb.course.CourseType;
import fire.pb.effect.PetImpl;
import fire.pb.item.SItemToItem;
import fire.pb.lock.PCheckLock;
import fire.pb.main.ConfigManager;
import fire.pb.ranklist.RankType;
import fire.pb.ranklist.proc.PRankInsertPet;
import fire.pb.ranklist.proc.PRoleZongheRankProc;
import fire.pb.ranklist.proc.RankListManager;
import fire.pb.skill.Result;
import fire.pb.talk.MessageMgr;
import fire.pb.tel.utils.GoodsSafeLocksUtils;
import fire.pb.util.DateValidate;
import mkdb.Procedure;
import mkdb.Mkdb;
import mkdb.util.AutoKey;

public class PetColumn {

	public static final int ADD_REASON_MOVE_BETWEEN_ROLES = -1;// 在角色间移动（例如交易，不是真正生成新的）
	public static final int ADD_REASON_MOVE_BETWEEN_BAGS = 0;// 在同一角色的背包间移动（例如背包仓库，商会上架，不是真正生成新的）
	public static final int ADD_REASON_CATCH = 1;// 捕捉
	public static final int ADD_REASON_PACKAGE = 2;// 宠物迷城
	public static final int ADD_REASON_GM = 6;// 宠物gm加的
	public static final int ADD_REASON_SYNTHESIZE = 8;// 合宠
	public static final int ADD_REASON_SHOP_BUY = 9;// 商店购买
	public static final int ADD_REASON_TASK = 10;// 任务
	public static final int ADD_REASON_CHARGE = 12;// 充值奖励
	public static final int ADD_REASON_ITEM = 13;// 使用道具获得
	public static final int ADD_REASON_ACHIEVE = 14;// 成就获得
	public static final int ADD_REASON_LEIJILOGIN = 15;// 累积登录礼包获得
	public static final int ADD_REASON_SHENSHOUDUIHUAN = 16;// 神兽兑换
	public static final int ADD_REASON_SHENSHOUCHONGZHI = 17;// 神兽重置

	public static final int REMOVE_REASON_MOVE_BETWEEN_ROLES = -1;// 在角色间移动（例如交易，不是真正删除）
	public static final int REMOVE_REASON_MOVE_BETWEEN_BAGS = 0;// 在同一角色的背包间移动（例如背包仓库，商会上架，不是真正删除）
	public static final int REMOVE_REASON_RELEASE = 1;// 放生
	public static final int REMOVE_REASON_TASK = 2;// 交任务
	public static final int REMOVE_REASON_SYNTHESIZE = 5;// 合宠
	public static final int REMOVE_REASON_SELL = 6;// 出售
	public static final int REMOVE_REASON_SHENSHOUCHONGZHI = 7;// 神兽重置

	/**
	 * 角色id
	 */
	public final long roleId;

	/**
	 * 宠物栏id(PetColumnTypes)
	 */
	public final int petColumnId;

	/**
	 * 是否只读
	 */
	private final boolean readOnly;

	/**
	 * 宠物栏中的宠物表
	 */
	private final xbean.Pets pets;

	/**
	 * 宠物栏的配置:id,对应的数据库表,初始大小等
	 */
	private final PetColumnConfig config;

	/**
	 * 构造
	 */
	@SuppressWarnings("unchecked")
	public PetColumn(long roleId, int petColumnId, boolean readOnly) {
		if (roleId <= 0)
			throw new IllegalArgumentException("Error roleId:" + roleId);

		this.readOnly = readOnly;
		this.roleId = roleId;
		this.petColumnId = petColumnId;
		config = getPetColumnConfig(petColumnId);
		if (null == config)
			throw new IllegalArgumentException("Error petColumnId:" + petColumnId);

		final mkdb.TTable<Long, xbean.Pets> table = (mkdb.TTable<Long, xbean.Pets>) mkdb.Mkdb
				.getInstance().getTables().getTable(config.tablename);

		final xbean.Pets petColumn;
		if (readOnly) {
			petColumn = table.select(roleId,
					new mkdb.TField<xbean.Pets, xbean.Pets>() {
						@Override
						public xbean.Pets get(xbean.Pets v) {
							return v.toData();
						}
					});
		} else {
			petColumn = table.get(roleId);
		}

		if (null == petColumn) {
			if (readOnly)
				pets = xbean.Pod.newPetsData();
			else
				pets = xbean.Pod.newPets();
			pets.setCapacity(config.initsize);
			if (!readOnly)
				table.insert(roleId, pets);
		} else {
			this.pets = petColumn;
		}
	}

	/**
	 * 获得宠物map
	 */
	public java.util.Map<Integer, xbean.PetInfo> getPetsMap() {
		if (pets != null)
			return pets.getPetmap();
		return null;
	}

	/**
	 * 获取下一个可用的宠物id
	 */
	private int incNextId() {
		if (readOnly)
			return 0;
		final int id = pets.getNextid() + 1;
		if (id < 0)
			throw new RuntimeException("");
		pets.setNextid(id);
		return id;
	}
	
	public int getPetNum(int petId) {
		int count = 0;
		for (xbean.PetInfo petinfo : getPetsMap().values()) {
			if (petinfo.getId() == petId) {
				if(petNotFightPet(petId)) {
					count++;
				}
			}
		}
		return count;
	}
	
	public int getPetNumByMapping(int petId) {
		int firstpetnum = getPetNum(petId);
		int otherpetnum = 0;
		SItemToItem item2item = ConfigManager.getInstance().getConf(SItemToItem.class).get(petId);
		if(item2item != null) {
			for(Integer curitem : item2item.getItemsid()) {
				int curitemnum = getPetNum(curitem.intValue());
				otherpetnum += curitemnum;
			}
		}
		return firstpetnum + otherpetnum;
	}

	/**
	 * 统计指定id宠物的个数
	 */
	public int countPet(int petId, /* PetTypeEnum */int... types) {
		int count = 0;
		for (xbean.PetInfo pinfo : getPetsMap().values()) {
			if (pinfo.getId() == petId) {
				if (types.length > 0) {
					for (int type : types) {
						if (pinfo.getKind() == type) {
							count++;
							break;
						}
					}
				} else {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 获取宠物栏的当前容量
	 */
	public int getCapacity() {
		return this.pets.getCapacity();
	}

	/**
	 * 宠物栏的容量增加1
	 */
	public void incCapacity() {
		if (readOnly)
			throw new RuntimeException("Invoke must in procedure");
		this.pets.setCapacity(this.pets.getCapacity() + 1);
	}
	
	/**
	 * 宠物栏的容量增加1
	 */
	public void incCapacity(int addsize) {
		if (readOnly)
			throw new RuntimeException("Invoke must in procedure");
		this.pets.setCapacity(this.pets.getCapacity() + addsize);
	}

	/**
	 * 宠物栏的最大容量
	 */
	public int getPetColumnMaxCapacity() {
		return config.maxsize;
	}

	/**
	 * 存储过程中调用,刷新宠物栏的当前容量
	 */
	public void refreshCapacity() {
		final SRefreshPetColumnCapacity send = new SRefreshPetColumnCapacity(this.petColumnId, this.getCapacity());
		Procedure.psendWhileCommit(roleId, send);
	}

	/**
	 * 宠物栏的当前大小
	 */
	public int size() {
		return this.pets.getPetmap().size();
	}

	public int getRemainSize() {
		return getCapacity() - size();
	}

	/***
	 * 创建宠物,在存储过程中使用
	 */
	public static xbean.PetInfo createPet(long roleid, int id, int level,
			List<Integer> skillIds, int type, int colour, boolean bind) {
		return createPet(roleid, id, level, skillIds, type, colour, bind, null, -1l);
	}

	/**
	 * 创建宠物,在存储过程中使用
	 */
	public static xbean.PetInfo createPet(long roleId, int id, int level,
			List<Integer> skillIds, int type, int starId, boolean bind,
			Map<Integer, Object> initAttrs, Long uId) {

		if (level < 0 || level > DataInit.PET_LEVEL_MAX)
			return null;
		final xbean.PetInfo petInfo = xbean.Pod.newPetInfo();
		petInfo.setId(id);
		petInfo.setKind(type);
		petInfo.setIsbinded(bind);
		petInfo.setLevel(level);
		if (uId <= 0) {
			AutoKey<Long> autoKey = Mkdb.getInstance().getTables().getTableSys().getAutoKeys().getAutoKeyLong(Module.PET_AUTOKEY_NAME);
			uId = autoKey.next();
			if (uId == null)
				throw new IllegalArgumentException("Get pet uid equal null");
		}
		petInfo.setUniqid(uId);
		petInfo.setOwnerid(roleId);

		final CalcPetAttr calcAttr = new CalcPetAttr(petInfo);
		// 分配初始点数
		calcAttr.setInitAttrPoint();
		petInfo.getBfp().setStr(petInfo.getBfp().getStr() + level);
		petInfo.getBfp().setCons(petInfo.getBfp().getCons() + level);
		petInfo.getBfp().setEndu(petInfo.getBfp().getEndu() + level);
		petInfo.getBfp().setIq(petInfo.getBfp().getIq() + level);
		petInfo.getBfp().setAgi(petInfo.getBfp().getAgi() + level);

		// 基本资质
		calcAttr.setBornAttackApt(-1);
		calcAttr.setBornDefendApt(-1);
		calcAttr.setBornMagicApt(-1);
		calcAttr.setBornPhyforceApt(-1);
		calcAttr.setBornDodgeApt(-1);
		calcAttr.setBornSpeedApt(-1);

		// 设置宠物的成长,寿命,名字
		calcAttr.setPetAttrByInitAttrs(initAttrs);

		Pet pet = Pet.getPet(petInfo);
		if (skillIds != null) {
			pet.getBattleskills().clear();
			for (int skillId : skillIds) {
				if (!fire.pb.skill.Module.isPetSkill(skillId))
					continue;
				pet.addSkill(skillId, -1, 0, 0);
			}
		} else {
			calcAttr.genPetSkill(petInfo);
		}

		// 设置自动加点方案
		pet.getPetInfo().setAutoaddcons(pet.getPetAttr().addpoint.get(0));
		pet.getPetInfo().setAutoaddiq(pet.getPetAttr().addpoint.get(1));
		pet.getPetInfo().setAutoaddstr(pet.getPetAttr().addpoint.get(2));
		pet.getPetInfo().setAutoaddendu(pet.getPetAttr().addpoint.get(3));
		pet.getPetInfo().setAutoaddagi(pet.getPetAttr().addpoint.get(4));

		// 设置当前等级的属性
		xbean.BasicFightProperties bfp = pet.getPetInfo().getBfp();
		bfp.setCons(bfp.getCons() + level * pet.getPetAttr().addpoint.get(0)); // 体质
		bfp.setIq(bfp.getIq() +     level * pet.getPetAttr().addpoint.get(1)); // 智力
		bfp.setStr(bfp.getStr() +   level * pet.getPetAttr().addpoint.get(2)); // 力量
		bfp.setEndu(bfp.getEndu() + level * pet.getPetAttr().addpoint.get(3)); // 耐力
		bfp.setAgi(bfp.getAgi() +   level * pet.getPetAttr().addpoint.get(4)); // 敏捷

		// 设置剩余潜力点
		int point = level * DataInit.PET_UP_LEVEL_ADD_POINT;
		point -= level * pet.getPetAttr().addpoint.get(0);
		point -= level * pet.getPetAttr().addpoint.get(1);
		point -= level * pet.getPetAttr().addpoint.get(2);
		point -= level * pet.getPetAttr().addpoint.get(3);
		point -= level * pet.getPetAttr().addpoint.get(4);
		pet.setPoint(point);

		// 检测自动加点是否超出总点数
		if (point < 0) {
			int sum = pet.getPetAttr().addpoint.get(0)
					+ pet.getPetAttr().addpoint.get(1)
					+ pet.getPetAttr().addpoint.get(2)
					+ pet.getPetAttr().addpoint.get(3)
					+ pet.getPetAttr().addpoint.get(4);
			String err = String.format("createPet id:%d level:%d setPoint:%d autoadd:%d", id, level, point, sum);
			throw new RuntimeException(err);
		}

		// 修正一下宠物类型,新版宠物类型和ID绑定
		if (petInfo.getKind() != pet.getPetAttr().getKind()) {
			Module.logger.warn("[createPet] roleId:" + roleId
//					+ " key:" + petInfo.getKey() // key由于还没生成,所以不输出
					+ " uniqId:" + petInfo.getUniqid()
					+ " id:" + petInfo.getId() // 配置表id
					+ " name:" + petInfo.getName()
					+ " kind:" + petInfo.getKind()
					+ " fixKind = PetAttr.kind:" + pet.getPetAttr().getKind());
			petInfo.setKind(pet.getPetAttr().getKind());
		}

		pet.online();

		PetImpl epet = new PetImpl(petInfo);
		epet.fullHp();
		epet.fullMp();

		return petInfo;
	}

	/**
	 * 加到宠物栏
	 * @return petKey
	 */
	public int add(xbean.PetInfo petInfo, int reason) {

		if (readOnly)
			return -1;

		if (size() >= getCapacity())
			return -1;

		boolean bSetFight = size() == 0;

		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			switch (reason) {
			case ADD_REASON_MOVE_BETWEEN_ROLES: // 战斗中宠物可以摆摊
			case ADD_REASON_MOVE_BETWEEN_BAGS: // 战斗中宠物可以摆摊
			case ADD_REASON_CATCH:
			case ADD_REASON_SYNTHESIZE:
			case ADD_REASON_SHOP_BUY:
			case ADD_REASON_CHARGE:
			case ADD_REASON_ITEM:
			case ADD_REASON_ACHIEVE:
			case ADD_REASON_LEIJILOGIN:
				bSetFight = false; // 战斗中获得的宠物不设置参战
				break;
			default:
				return -1;
			}
		}

		while (true) {
			int nextId = incNextId();
			if (nextId == 0)
				nextId = incNextId();
			if (null == pets.getPetmap().get(nextId)) {
				petInfo.setKey(nextId);
				pets.getPetmap().put(nextId, petInfo);
				break;
			}
		}

		// 通知任务模块
		fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.PetColumnChange(roleId, petInfo.getId()));
		Pet pet = Pet.getPet(petInfo);
		final SAddPetToColumn sendAdd = new SAddPetToColumn();
		sendAdd.columnid = petColumnId;
		sendAdd.petdata = pet.getProtocolPet();
		Procedure.psendWhileCommit(roleId, sendAdd);

		// 如果是在同一角色的背包间移动，而不是一个新宠物
		if (reason == ADD_REASON_MOVE_BETWEEN_BAGS)
			return petInfo.getKey();

		boolean isMsg = reason > 0;
		changeOwner(petInfo, isMsg); // 记录唯一宠物

		// 如果是在不同角色的背包间移动，而不是一个新宠物
		if (reason == ADD_REASON_MOVE_BETWEEN_ROLES) {
			CourseManager.achieveCourse(roleId, CourseType.HAVE_PAT, pet.getPetAttr().quality, pet.getPetAttr().unusualid);
			return petInfo.getKey();
		}

		pet.updatePetScoreWhileChange(); // 新宠物计算宠物评分

		// 宠物的历程
		CourseManager.achieveCourse(roleId, CourseType.PET_COURSE, petInfo.getId(), 0);
		
		CourseManager.achieveCourse(roleId, CourseType.HAVE_PAT, pet.getPetAttr().quality, pet.getPetAttr().unusualid);
		fire.pb.course.CourseManager.checkAchieveCourse(roleId, fire.pb.course.CourseType.PET_SCORE, pet.getPetInfo().getPetscore());

		// 运营日志
		writeYYLogger(petInfo);
		
		if (bSetFight) {
			new PSetFightPetProc(roleId, petInfo.getKey(), true).call();
		}
		return petInfo.getKey();
	}

	/**
	 * 运营日志
	 * @param petInfo
	 */
	private void writeYYLogger(xbean.PetInfo petInfo) {
		if(petInfo == null) return;
		PropRole pRole = new fire.pb.PropRole(roleId, true);
		fire.log.beans.OpPetBean opPetBean = new fire.log.beans.OpPetBean(petInfo.getId(), 
				petInfo.getLevel(), petInfo.getIsbinded() ? 1 : 0, pRole.getFightpetkey() == petInfo.getKey() ? 1 : 0,
						petInfo.getPetscore(), petInfo.getHp(), petInfo.getMp(), petInfo.getTreasure(),
						petInfo.getUniqid(), petInfo.getBfp().getCons(), petInfo.getBfp().getIq(), 
						petInfo.getBfp().getStr(), petInfo.getBfp().getEndu(), petInfo.getBfp().getAgi(), 
						petInfo.getBornphyforceapt(), petInfo.getBornmagicapt(), petInfo.getBornattackapt(), 
						petInfo.getBorndefendapt(), petInfo.getBornspeedapt(),petInfo.getSkills());
		YYLogger.petLog(roleId, opPetBean);
	}

	/**
	 * 改变宠物主人
	 */
	private void changeOwner(xbean.PetInfo petInfo, boolean isMsg) {
		xbean.UniquePet uniquePet = xtable.Uniquepets.get(petInfo.getUniqid());
		if (uniquePet == null) {
			uniquePet = xbean.Pod.newUniquePet();
			xtable.Uniquepets.insert(petInfo.getUniqid(), uniquePet);
		}
		uniquePet.setRoleid(roleId);
		petInfo.setOwnerid(roleId);

		// 计算宠物是否可以上榜
		mkdb.Procedure.pexecuteWhileCommit(new PRankInsertPet(petInfo.getUniqid(), isMsg));
	}

	/**
	 * 删除宠物
	 * @return 0:成功,<0:失败
	 */
	public int removePet(int petKey, int reason, int param) {

		if (readOnly)
			throw new RuntimeException("readOnly!!");

		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			switch (reason) {
			case REMOVE_REASON_MOVE_BETWEEN_BAGS: // 战斗中宠物可以摆摊
			case REMOVE_REASON_MOVE_BETWEEN_ROLES: // 战斗中宠物可以摆摊
			case REMOVE_REASON_SYNTHESIZE: // 战斗中可以合宠
				break;
			default:
				return PetError.KeyNotFound;
			}
		}

		xbean.PetInfo petInfo = getPetInfo(petKey);
		if (null == petInfo)
			return PetError.KeyNotFound;

		Pet pet = Pet.getPet(petInfo);
		java.util.Map<Integer, xbean.PetInfo> petMap = pets.getPetmap();
		xbean.PetInfo removed = petMap.remove(petKey);
		if (removed == null)
			return PetError.KeyNotFound;
		if (removed != petInfo)
			return PetError.KeyNotFound;

		// 通知任务模块
		fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.PetColumnChange(roleId, removed.getId()));
		// 通知客户端
		final SRemovePetFromCol sendRemove = new SRemovePetFromCol(petColumnId, petKey);
		Procedure.psendWhileCommit(roleId, sendRemove);

		// 真正删除
		if (reason != REMOVE_REASON_MOVE_BETWEEN_BAGS
				&& reason != REMOVE_REASON_MOVE_BETWEEN_ROLES) {
			onRealRemove(pet, reason, param, new HashMap<String, Object>());
		}

		return 0;
	}

	/**
	 * 删除宠物
	 */
	public int removePet(int petKey, int reason) {

		if (readOnly)
			throw new RuntimeException("readOnly!!");

		if (fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			switch (reason) {
			case REMOVE_REASON_MOVE_BETWEEN_BAGS: // 战斗中宠物可以摆摊
			case REMOVE_REASON_MOVE_BETWEEN_ROLES: // 战斗中宠物可以摆摊
			case REMOVE_REASON_SYNTHESIZE: // 战斗中可以合宠
				break;
			default:
				return PetError.KeyNotFound;
			}
		}

		xbean.PetInfo petInfo = getPetInfo(petKey);
		if (null == petInfo)
			return PetError.KeyNotFound;

		Pet pet = Pet.getPet(petInfo);
		java.util.Map<Integer, xbean.PetInfo> petMap = pets.getPetmap();
		xbean.PetInfo removed = petMap.remove(petKey);
		if (removed == null)
			return PetError.KeyNotFound;
		if (removed != petInfo)
			return PetError.KeyNotFound;

		// 通知任务模块
		fire.pb.event.Poster.getPoster().dispatchEvent(new fire.pb.event.PetColumnChange(roleId, removed.getId()));
		// 通知客户端
		final SRemovePetFromCol sendRemove = new SRemovePetFromCol(petColumnId, petKey);
		Procedure.psendWhileCommit(roleId, sendRemove);

		// 真正删除
		if (reason != REMOVE_REASON_MOVE_BETWEEN_BAGS
				&& reason != REMOVE_REASON_MOVE_BETWEEN_ROLES) {
			onRealRemove(pet, reason, 0, new HashMap<String, Object>());
		}

		return 0;
	}

	private void onRealRemove(Pet pet, int reason, int param, Map<String, Object> params) {

		final long uniquePetId = pet.getUniqueId();
		boolean ret = xtable.Uniquepets.remove(uniquePetId);
		if (!ret) {
			Module.logger.error("[onRealRemove] roleId:" + roleId
					+ " petKey:" + pet.getPetInfo().getKey()
					+ " uniqId:" + uniquePetId
					+ " Pet not exist in Uniquepets.");
		}

		// 保存到回收站
		boolean isRecycle = false;
		if (pet.getKind() != PetTypeEnum.WILD) {
			if (pet.getKind() == PetTypeEnum.BABY && pet.getTreasure() == 0) {
				isRecycle = false;
			} else {
				isRecycle = true;
			}
			if (isRecycle) {
				enterRecyclebin(pet, reason);
			}
		}
		// 记录日志
		if (Module.logger.isInfoEnabled()) {
			Module.logger.info("[onRealRemove] roleId:" + roleId
					+ " petKey:" + pet.getPetkey()
					+ " uniqId:" + pet.getUniqueId()
					+ " petId:" + pet.getBaseId()
					+ " name:" + pet.getName()
					+ " petScore:" + pet.getPetInfo().getPetscore()
					+ " isTreasure:" + pet.getTreasure()
					+ " reason:" + reason
					+ " isRecycle:" + isRecycle);
		}

		// 从排行榜删除
		mkdb.Procedure.pexecuteWhileCommit(new mkdb.Procedure() {
			@Override
			public boolean process() {
				removeFromPetscoreRankList(uniquePetId);
				return true;
			}
		});

		// 更新综合排行榜上 by changhao
		mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(pet.getPetInfo().getOwnerid()));
	}

	private void enterRecyclebin(Pet pet, int reason) {
		xbean.DiscardPet dpet = xbean.Pod.newDiscardPet();
		dpet.setRoleid(roleId);
		dpet.setDeletedate(System.currentTimeMillis());
		dpet.setReason(reason);
		OctetsStream os = pet.getPetInfo().marshal(new OctetsStream());
		try {
			dpet.getPet().unmarshal(os);
		} catch (MarshalException e) {
			Module.logger.error(e);
		}
		if (pet.getUniqueId() > 0) {
			// 宠物进入回收站
			xtable.Petrecyclebin.insert(pet.getUniqueId(), dpet);

			// 玩家可以在界面主动找回的
			if (reason == REMOVE_REASON_RELEASE) {
				// uniqId进入宠物可回收列表
				xbean.Petrecoverlist petRecoverList = xtable.Petrecover.get(roleId);
				if (petRecoverList == null) {
					petRecoverList = xbean.Pod.newPetrecoverlist();
					xtable.Petrecover.insert(roleId, petRecoverList);
				}
				if (petRecoverList.getUniqids().contains(pet.getUniqueId()) == false) {
					petRecoverList.getUniqids().add(pet.getUniqueId());
					// 只保留50个
					if (petRecoverList.getUniqids().size() > 50) {
						petRecoverList.getUniqids().remove(0);
					}
				}
			}
		}
	}

	protected void removeFromPetscoreRankList(long uniqPetid) {
		xbean.PetScoreRankList list = xtable.Petscorelist.get(1);
		if (null == list)
			return;

		int index = 0;
		boolean isRemove = false;
		for (xbean.PetScoreListRecord record : list.getRecords()) {
			if (record.getMarshaldata().getUniquepetid() == uniqPetid) {
				isRemove = true;
				break;
			}
			index++;
		}

		if (isRemove) {
			list.getRecords().remove(index);
			xbean.RoleRankNotifyTimeInfo roleInfo = xtable.Roleranknotifytime.get(roleId);
			if (roleInfo == null) {
				roleInfo = xbean.Pod.newRoleRankNotifyTimeInfo();
				roleInfo.getLasttime().put(RankType.ROLE_ZONGHE_RANK, 0L);
				roleInfo.getLasttime().put(RankType.PET_GRADE_RANK, 0L);
			}

			// 之前改过lasttime字段，所以roleinfo不为空，但是getlasttime为空
			if (roleInfo.getLasttime().get(RankType.PET_GRADE_RANK) == null) {
				roleInfo.getLasttime().put(RankType.PET_GRADE_RANK, 0L);
			}

			List<String> param = new ArrayList<String>();
			param.add(RankListManager.getInstance().getNameByType(RankType.PET_GRADE_RANK));
			if (!DateValidate.inTheSameDay(System.currentTimeMillis(), roleInfo.getLasttime().get(RankType.PET_GRADE_RANK))) {
				roleInfo.getLasttime().put(RankType.PET_GRADE_RANK, System.currentTimeMillis());
			}
		}
	}

	/**
	 * 判断是否为参战宠物
	 */
	public boolean petIsFightPet(final int petKey) {
		if (petColumnId != PetColumnTypes.PET)
			return false;
		xbean.PetInfo petInfo = getPetInfo(petKey);
		if (null == petInfo)
			return false;
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (null == prop)
			return false;
		return prop.getFightpetkey() == petInfo.getKey();
	}

	public boolean petNotFightPet(final int petId) {
		xbean.Properties prop = xtable.Properties.select(roleId);
		if (null == prop)
			return false;
		int fightPetKey = prop.getFightpetkey();
		for (xbean.PetInfo petInfo : getPetsMap().values()) {
			if (null == petInfo)
				continue;
			if (petInfo.getId() == petId && petInfo.getKey() != fightPetKey) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断宠物是否是展示宠物
	 */
	public boolean petIsShowPet(final int petKey) {
		if (petColumnId != PetColumnTypes.PET)
			return false;

		xbean.PetInfo info = getPetInfo(petKey);
		if (null == info)
			return false;

		xbean.Properties prop = xtable.Properties.select(roleId);
		if (null == prop)
			return false;
		return prop.getShowpetkey() == petKey;
	}

	/**
	 * 存取
	 * @return <:0 失败,>0:宠物的新key
	 */
	public static int doMovePet(PetColumn srcCol, int srcKey, PetColumn dstCol) {
		if (srcCol.readOnly || dstCol.readOnly)
			throw new RuntimeException("Error argument!!");
		if (srcCol.roleId != dstCol.roleId)
			throw new RuntimeException("Error argument!!");
		if (srcCol.petColumnId == dstCol.petColumnId)
			return PetError.WrongDstCol;
		// 展示或者参战的宠物，不能进行入仓操作.
		if (srcCol.petIsShowPet(srcKey))
			return PetError.ShowPetCantMoveErr;
		if (srcCol.petIsFightPet(srcKey))
			return PetError.FightPetCantMoveErr;

		// 判断源宠物是否存在
		xbean.PetInfo srcPetInfo = srcCol.getPetInfo(srcKey);
		if (null == srcPetInfo)
			return PetError.KeyNotFound;

		// 判断目的宠物栏是否已经满了
		if (dstCol.size() >= dstCol.getCapacity())
			return PetError.PetcolumnFull;

		// 源宠物栏删除
		int ret = srcCol.removePet(srcKey, REMOVE_REASON_MOVE_BETWEEN_BAGS, 0);
		if (ret == 0) {
			ret = dstCol.add(srcPetInfo, ADD_REASON_MOVE_BETWEEN_BAGS);
		}
		return ret > 0 ? ret : PetError.UnkownError;
	}

	/**
	 * 宠物栏添加一个指定ID的宠物
	 * @return petKey
	 */
	public int addPetByID(int id, boolean bind, int reason) {
		PetAttr petAttr = Module.getInstance().getPetManager().getAttr(id);
		if (petAttr != null) {
			return addpet(id, petAttr.getInitlevel(), petAttr.getKind(), null, reason, PetColour.WHITE, bind, null);
		}
		return -1;
	}

	/**
	 * 添加宠物
	 * @return petKey
	 */
	public int addpet(int id, int level, int type, List<Integer> skills,
			int reason, int starId, boolean bind, Map<Integer, Object> initAttrs) {
		final xbean.PetInfo petInfo = createPet(this.roleId, id, level, skills, type, starId, bind, initAttrs, -1l);
		if (null == petInfo)
			return -1;
		return add(petInfo, reason);
	}

	public java.util.ArrayList<fire.pb.Pet> getAllProtocolPets() {
		if (null == pets || null == pets.getPetmap())
			return null;
		java.util.ArrayList<fire.pb.Pet> vecs = new java.util.ArrayList<fire.pb.Pet>();
		for (final xbean.PetInfo petInfo : pets.getPetmap().values()) {
			Pet pet = getPet(petInfo.getKey());
			if (pet != null)
				vecs.add(pet.getProtocolPet());
		}
		return vecs;
	}

	public xbean.PetInfo getPetInfo(final int key) {
		final java.util.Map<Integer, xbean.PetInfo> petMap = pets.getPetmap();
		if (null == petMap)
			return null;

		final xbean.PetInfo petInfo = petMap.get(key);
		if (null == petInfo)
			return null;
		return petInfo;
	}

	public boolean addShowSkillBuff() {
		if (readOnly)
			return false;
		if (petColumnId != PetColumnTypes.PET)
			return false;
		int petKey = xtable.Properties.selectShowpetkey(roleId);
		if (petKey < 0)
			return false;
		return addShowSkillBuff(getPet(petKey));
	}

	private boolean addShowSkillBuff(Pet pet) {
		if (pet == null)
			return false;
		ConstantlyBuff buff = pet.getShowSkillBuff(pet.getBattleskills());
		if (buff == null)
			return false;
		BuffAgent buffAgent = new BuffRoleImpl(roleId);
		buffAgent.addCBuff(buff);
		return true;
	}

	public boolean removeShowSkillBuff() {
		if (readOnly)
			return false;
		BuffAgent buffAgent = new BuffRoleImpl(roleId);
		Result result = buffAgent.removeCBuff(500214);
		if (result == null)
			return false;
		return result.isSuccess();
	}

	/**
	 * 增加技能
	 */
	public boolean addSkill(int petkey, int skillid, int skillExp, int skillType) {
		if (readOnly)
			return false;
		Pet pet = getPet(petkey);
		if (pet == null)
			return false;
		if (pet.hasAnySkill(skillid))
			return false;
		boolean succ = pet.addSkill(skillid, -1, skillExp, skillType);
		if (!succ)
			return false;
		return succ;
	}

	/**
	 * 修改名字
	 */
	public boolean modPetName(final int petKey, String name) {
		if (readOnly)
			return false;
		if (null == name || petColumnId != PetColumnTypes.PET)
			return false;

		xbean.PetInfo petInfo = getPetInfo(petKey);
		if (null == petInfo)
			return false;

		if (petInfo.getName().equals(name))
			return false;

		String oldName = petInfo.getName();
		petInfo.setName(name);

		final SModPetName sendName = new SModPetName(roleId, petKey, name);
		Procedure.psendWhileCommit(roleId, sendName);

		xbean.Properties prop = xtable.Properties.get(roleId);
		if (prop == null)
			return false;

		// 如果修改名字的宠物是正在展示的宠物，通知地图模块展示宠物
		if (prop.getShowpetkey() == petKey) {
			fire.msp.move.GNotifyMapPetInfo send = new fire.msp.move.GNotifyMapPetInfo(
					roleId, Pet.getPet(petInfo).getShowPetInfo());
			fire.pb.GsClient.pSendWhileCommit(send);
		}

		// 更新宠物排行榜上的名字 by changhao
		mkdb.Procedure.pexecuteWhileCommit(new fire.pb.ranklist.proc.PRankInsertPetChangeName(petInfo.getUniqid(), true));

		// 更新综合排行榜上的名字 by changhao
		mkdb.Procedure.pexecuteWhileCommit(new PRoleZongheRankProc(petInfo.getOwnerid()));

		// 记录日志
		if (Module.logger.isDebugEnabled()) {
			Module.logger.debug("[PModPetName] roleId:" + roleId
					+ " petKey:" + petKey
					+ " uniqId:" + petInfo.getUniqid()
					+ " petId:" + petInfo.getId()
					+ " oldName:" + oldName
					+ " newName:" + name);
		}
		return true;
	}

	/**
	 * 宠物是否能放生,存储过程中调用.
	 */
	public boolean checkCanFreePet(final int petKey) {
		if (readOnly)
			return false;
		if (petColumnId != PetColumnTypes.PET)
			return false;
		Pet pet = getPet(petKey);
		if (null == pet)
			return false;
		if (pet.isLocked() != -1)
			return false;
		if (pet.getKind() == PetTypeEnum.SACREDANIMAL)			// 神兽不能放生
			return false;
		final SPetError sendErr = new SPetError();
		if (petIsShowPet(petKey)) {
			sendErr.peterror = PetError.ShowPetCantFree;
			Procedure.psendWhileRollback(roleId, sendErr);
			return false;
		}
		if (petIsFightPet(petKey)) {
			sendErr.peterror = PetError.FightPetCantFree;
			Procedure.psendWhileRollback(roleId, sendErr);
			return false;
		}
		return true;
	}

	/**
	 * 宠物放生
	 */
	public boolean freePet(int petKey) {
		Pet pet = getPet(petKey);
		if (pet == null)
			return false;
		if (!checkCanFreePet(petKey))
			return false;
		// // 橙色以上判断安全锁
		// if (pet.getColor() >= PetColour.ORANGE) {
		// if (!new PCheckLock(roleId).call()) {
		// return false;
		// }
		// }
		// 道具锁判断
		if (GoodsSafeLocksUtils.checkLockStatus(roleId, pet.getPetInfo())) {
			return false;
		}
		int removedPetTScore = pet.getPetAttr().getTreasureScore();
		int removedPetScore = pet.getPetInfo().getPetscore();
		long uniqid = pet.getPetInfo().getUniqid();
		if (removePet(petKey, REMOVE_REASON_RELEASE, 0) == 0) {
			MessageMgr.psendMsgNotifyWhileCommit(roleId, 142258, null);
			// 运营日志
			writeYYLogger(pet.getPetAttr().getId(), removedPetTScore, removedPetScore, uniqid);
			//清除数据
        	GoodsSafeLocksUtils.doClearDataWhileCommit(roleId);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 运营日志
	 * @param petKey
	 * @param pet
	 * @param removedPetTScore
	 * @param removedPetScore
	 * @param uniqid
	 */
	private void writeYYLogger(int petId, int removedPetTScore, int removedPetScore, long uniqid) {
		int isTrea = removedPetTScore > removedPetScore ? 0 : 1;
		YYLogger.petFreeLog(roleId, petId, isTrea, uniqid);
	}

	/**
	 * 用于摆摊交易
	 * @return 0:成功,<0:失败
	 */
	public static int moveBetweenRolePetColumn(final PetColumn srcCol, final PetColumn dstCol, final int petKey) {

		Pet pet = srcCol.getPet(petKey);
		if (pet == null)
			return -1;

		final fire.pb.PropRole prop = new fire.pb.PropRole(dstCol.roleId, false);
		if (prop.getLevel() < pet.getTakeLevel())// 携带等级
			return -1;

		if (srcCol.petIsFightPet(petKey)) // 参战的宠物不能交易
			return -1;

		xbean.PetInfo petInfo = pet.copyPetInfoBean();
		if (null == petInfo)
			return -1;

		if (srcCol.removePet(petKey, REMOVE_REASON_MOVE_BETWEEN_ROLES) == 0
				&& dstCol.add(petInfo, ADD_REASON_MOVE_BETWEEN_ROLES) > 0)
			return 0;

		return -1;
	}

	/**
	 * 获取宠物栏配置
	 */
	public static fire.pb.pet.PetColumnConfig getPetColumnConfig(int petColumnId) {
		return fire.pb.main.ConfigManager.getInstance().getConf(PetColumnConfig.class).get(petColumnId);
	}

	public int getPetColumnType() {
		return petColumnId;
	}

	public Pet getPet(int petkey) {
		return Pet.getPet(roleId, this, petkey, readOnly);
	}

	public List<Pet> getPets() {
		List<Pet> list = new LinkedList<Pet>();
		for (int petKey : pets.getPetmap().keySet())
			list.add(Pet.getPet(roleId, this, petKey, readOnly));
		return list;
	}

	/**
	 * 获取某个宠物栏的所有宠物
	 */
	public static List<Pet> getPetsByColumnType(long roleId, int columnType, boolean readOnly) {
		try {
			List<Pet> pets = new LinkedList<Pet>();
			switch (columnType) {
			case -1:// 身上
			case 0: // 背包
				int column = (columnType == -1) ? PetColumnTypes.PET : PetColumnTypes.DEPOT;
				PetColumn petCol = new PetColumn(roleId, column, readOnly);
				pets.addAll(petCol.getPets());
				break;
			default:
				break;
			}
			return pets;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
