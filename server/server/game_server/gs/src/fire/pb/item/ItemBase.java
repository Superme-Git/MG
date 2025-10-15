package fire.pb.item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import mkdb.Procedure;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import fire.log.YYLogger;
import fire.log.beans.OpPetTraBean;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.WorldTime;
import fire.pb.attr.EffectType;
import fire.pb.attr.SRefreshPetData;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.item.Commontext.UseResult;
import fire.pb.master.MasterManager;
import fire.pb.pet.PetColumnTypes;
import fire.pb.shop.utils.MarketUtils;
import fire.pb.util.ParseObjIDInBattle;
import fire.pb.util.ParseObjIDOutBattle;

public abstract class ItemBase {

	public static int ITEM_LOCK_MSG = 141078;

	protected ItemShuXing itemAttr;

	protected int packid = BagTypes.EMPTY;

	protected xbean.Item itemData;

	protected int keyinpack = 0;

	protected OctetsStream os = null;

	protected long roleid = 0;

	ItemBase(ItemMgr itemMgr, int itemId) {
		itemAttr = itemMgr.getAttr(itemId);
		this.itemData = xbean.Pod.newItem();
		this.itemData.setId(itemId);
		this.itemData.setTypeid(itemAttr.getTypeid());
		setFlag(getIniFlag());
	}

	ItemBase(ItemMgr im, xbean.Item item) {
		itemAttr = im.getAttr(item.getId());
		this.itemData = item;
	}

	protected Commontext.UseResult _onUse(int num, UseItemHandler handler) {
		if (handler == null)
			return Commontext.UseResult.FAIL;
		return handler.onUse(getOwnerid(), this, num);
	}

	protected abstract void onDeleted();

	protected abstract void onInserted();

	public void bind() {
		final int f = this.itemData.getFlags() | fire.pb.Item.BIND;
		this.itemData.setFlags(f);
	}

	/**
	 * 将物品绑定并通知客户端
	 */
	public void bindAndNotify() {
		bind();
		SRefreshItemFlag ref = new SRefreshItemFlag(keyinpack, itemData.getFlags(), packid);
		mkdb.Procedure.psendWhileCommit(roleid, ref);
	}

	/**
	 * 物品已经失效
	 * 
	 * @return
	 */
	public boolean isTimeout() {
		return isTimeout(itemData);
	}

	public static boolean isTimeout(final xbean.Item dataItem) {
		if (!hasFlag(dataItem, fire.pb.Item.TIMEOUT))
			return false;
		return WorldTime.getInstance().getTimeInMillis() > dataItem.getLoseeffecttime();
	}

	private static boolean hasFlag(final xbean.Item itemData, final int flag) {
		return (itemData.getFlags() & flag) != 0;
	}

	public boolean canStall() {
		return ((itemData.getFlags() & (fire.pb.Item.ONSTALL | fire.pb.Item.BIND | fire.pb.Item.CANNOTONSTALL)) == 0);
	}

	public boolean canTrans() {
		return (itemData.getFlags() & (fire.pb.Item.BIND | fire.pb.Item.ONSTALL)) == 0;
	}

	public boolean canUse(long roleId, int idType, long objId) {
		ParseObjIDOutBattle battleObj = new ParseObjIDOutBattle(itemAttr.outbattle);
		if ((getFlags() & fire.pb.Item.ONSTALL) != 0)
			return false;
		if (idType == IDType.ROLE) {
			if (objId == roleId) {
				return battleObj.role;
			}
			if (battleObj.otherrole) {
				return true;
			}
			if (battleObj.masterandapprentice) {
				if (MasterManager.getInstance().isMasterApprenticeship(roleId, objId))
					return true;
			}
			if (battleObj.teammate) {
				final fire.pb.team.Team team = fire.pb.team.TeamManager.selectTeamByRoleId(roleId);
				if (team != null && team.isInTeam(objId)) {
					return true;
				}
			}
		} else if (idType == IDType.PET) {
			if (battleObj.petself) {
				return true;
			}
		} else if (idType == IDType.ITEM) {
			if (battleObj.role) {
				return true;
			}
		}
		return false;
	}

	public ItemShuXing getItemAttr() {
		return itemAttr;
	}

	public int getPackId() {
		return packid;
	}

	public xbean.Item getDataItem() {
		return itemData;
	}

	public mkdb.Bean getExtInfo() {
		return null;
	}

	public int getFlags() {
		return itemData.getFlags();
	}

	int getIniFlag() {
		int flag = 0;
		if (itemAttr.pickupbind)
			flag |= fire.pb.Item.BIND;
		if (!MarketUtils.isPayService()) {
			if (itemAttr.cansale == 0)
				flag |= fire.pb.Item.CANNOTONSTALL;
		} else {
			if (itemAttr.dcansale == 0)
				flag |= fire.pb.Item.CANNOTONSTALL;
		}
		return flag;
	}

	public int getItemId() {
		return itemData.getId();
	}

	public String getItemLog() {
		return new StringBuilder().append(getItemId()).append(",").append(getNumber()).append(",").append(getUniqId())
				.append(";").toString();
	}

	public int getKey() {
		return keyinpack;
	}

	public String getName() {
		return itemAttr.name;
	}

	public int getNumber() {
		return ItemMaps.getItemNum(itemData);
	}

	public long getOwnerid() {
		return roleid;
	}

	public int getPosition() {
		return itemData.getPosition();
	}

	public long getTimeout() {
		return itemData.getLoseeffecttime();
	}

	public Octets getTips() {
		final mkdb.Bean tipsdata = getExtInfo();
		if (tipsdata == null)
			return null;
		if (os == null) {
			os = new OctetsStream();
			os.marshal((mkdb.Bean) tipsdata);
		}
		return os;
	}

	public long getUniqId() {
		if (roleid == 0)
			throw new RuntimeException("no uniqueid");
		return itemData.getUniqueid();
	}

	protected List<Effect> getAppendEffects(int num) {
		return new ArrayList<Effect>();
	}

	protected UseItemHandler getUseItemHandler() {
		return null;
	}

	public boolean isBind() {
		return ((itemData.getFlags() & fire.pb.Item.BIND) == fire.pb.Item.BIND) && roleid > 0;
	}

	public final void onDelete(YYLoggerTuJingEnum e) {
		final WorldTime now = WorldTime.getInstance();
		final xbean.Item xItem = itemData.toData();
		if (itemAttr.recycletime != 0 && xItem.getUniqueid() != 0) {
			mkdb.Procedure.pexecuteWhileCommit(new Procedure() {

				@Override
				protected boolean process() throws Exception {

					// 玩家可以在界面主动找回的
					if (e == YYLoggerTuJingEnum.tujing_Value_diuqi
							|| e == YYLoggerTuJingEnum.tujing_Value_fenjie) {
						// uniqId进入道具可回收列表
						xbean.Itemrecoverlist itemRecoverList = xtable.Itemrecover.get(roleid);
						if (itemRecoverList == null) {
							itemRecoverList = xbean.Pod.newItemrecoverlist();
							xtable.Itemrecover.insert(roleid, itemRecoverList);
						}
						if (itemRecoverList.getUniqids().contains(xItem.getUniqueid()) == false) {
							itemRecoverList.getUniqids().add(xItem.getUniqueid());
							// 只保留50个
							if (itemRecoverList.getUniqids().size() > 50) {
								itemRecoverList.getUniqids().remove(0);
							}
						}
					}

					// 道具进入回收站
					now.add(Calendar.DAY_OF_YEAR, itemAttr.recycletime);
					xbean.DiscardItem di = xtable.Itemrecyclebin.get(xItem.getUniqueid());
					if (di == null) {
						di = xbean.Pod.newDiscardItem();
						xtable.Itemrecyclebin.insert(xItem.getUniqueid(), di);
						Module.logger.debug("物品回收站添加物品, id=" + xItem.getUniqueid());
					}
					xbean.ItemRecycleDate key = new xbean.ItemRecycleDate(now.get(Calendar.YEAR), now.get(Calendar.DAY_OF_YEAR));
					xbean.ItemRecycleIndex iri = xtable.Itemrecycleidx.get(key);
					if (iri == null) {
						iri = xbean.Pod.newItemRecycleIndex();
						xtable.Itemrecycleidx.insert(key, iri);
					}
					iri.getDayrecycle().add(xItem.getUniqueid());
					di.setDeletedate(now.getTimeInMillis());
					di.getItem().setExtid(xItem.getExtid());
					di.getItem().setFlags(xItem.getFlags());
					di.getItem().setId(xItem.getId());
					di.getItem().setNumber(xItem.getNumber());
					di.getItem().getNumbermap().putAll(xItem.getNumbermap());
					di.getItem().setPosition(xItem.getPosition());
					di.getItem().setTimeout(xItem.getTimeout());
					di.getItem().setUniqueid(xItem.getUniqueid());
					return true;
				}
			});
		} else {
			onDeleted();
		}
	}

	public final void onInsert() {
		if (itemAttr.getValidtime() > 0) {
			long timeout = WorldTime.getInstance().getTimeInMillis() + itemAttr.getValidtime() * 1000;
			setTimeout(timeout);
			if (timeout / (60 * 1000) < Integer.MAX_VALUE) {
				/*
				 * mkdb.Executor.getInstance().schedule(new Runnable() {
				 * @Override
				 * public void run() {
				 * new mkdb.Procedure(){
				 * @Override
				 * protected boolean process() throws Exception {
				 * if (roleid == 0)
				 * return true;
				 * ItemMaps bag = Module.getInstance().getItemMaps(roleid, packid, false);
				 * if (bag == null)
				 * return true;
				 * ItemBase item = bag.getItem(keyinpack);
				 * if (item == null)
				 * return true;
				 * item.onTimeout();
				 * xbean.TimeoutItemInfo timeoutInfo = xtable.Timeoutitem.get(itemData.getId());
				 * if (timeoutInfo != null) {
				 * timeoutInfo.getItemuid().remove(Long.valueOf(itemData.getUniqueid()));
				 * }
				 * Module.logger.info(new
				 * StringBuilder().append("role:").append(roleid).append(" item timeout:").append(item.getItemId()));
				 * return true;
				 * }
				 * }.submit();
				 * }
				 * }, (int)(itemAttr.getValidtime()/60), TimeUnit.MINUTES);
				 * xbean.TimeoutItemInfo timeoutInfo = xtable.Timeoutitem.get(itemData.getId());
				 * if (timeoutInfo == null) {
				 * timeoutInfo = xbean.Pod.newTimeoutItemInfo();
				 * xtable.Timeoutitem.insert(itemData.getId(), timeoutInfo);
				 * }
				 * timeoutInfo.getItemuid().add(Long.valueOf(itemData.getUniqueid()));
				 */
			} else
				Module.logger.error("超时时间溢出");
		}
		onInserted();
	}

	public final Commontext.UseResult onUse(int num) {
		return _onUse(num, getUseItemHandler());
	}

	public final Commontext.UseResult onUse(int num, UseItemHandler handler) {
		if (handler == null)
			handler = getUseItemHandler();
		return _onUse(num, handler);
	}

	public boolean onUseInBattle(int usertype, int aimrelation) {
		if (itemAttr == null) {
			Module.logger.debug("未找到该物品的使用属性");
			return false;
		}
		if ((itemAttr.inbattleuseto & usertype) == 0)
			return false;
		if (itemAttr.inbattle == 0)
			return false;
		ParseObjIDInBattle objInBattle = new ParseObjIDInBattle(itemAttr.inbattle);
		return objInBattle.validTarget(aimrelation);
	}

	protected void setPackId(int packId) {
		this.packid = packId;
	}

	public void setFlag(final int flag) {
		itemData.setFlags(itemData.getFlags() | flag);
	}

	protected void setKey(int keyInPack) {
		this.keyinpack = keyInPack;
	}

	protected void setOwnerid(final long roleId) {
		this.roleid = roleId;
	}

	protected UseResult appendToItem(final int dstPackId, final int dstItemKey, final int usedNum) {
		return Commontext.UseResult.FAIL;
	}

	protected UseResult appendToPet(final int petKey, final int useNum) {
		List<Effect> effects = getAppendEffects(useNum);
		for (Effect effect : effects) {
			if (effect.effectid == EffectType.PET_LIFE_ABL) {
				fire.pb.effect.PetImpl epet = new fire.pb.effect.PetImpl(roleid, petKey);
				boolean isForever = fire.pb.pet.Module.getInstance().getPetManager().isPetLifeForever(epet.getPetinfo().getId());
				if (epet.getPet().getLife() >= epet.getPet().getMaxLife() || isForever) {
					fire.pb.talk.MessageMgr.psendMsgNotifyWhileRollback(roleid, 142929, null);
					return Commontext.UseResult.FAIL;
				}
			}
		}
		fire.pb.skill.SceneSkillRole srole = fire.pb.skill.SkillManager.getSceneSkillRole(roleid);
		java.util.Map<Integer, Float> changemap = srole.useItem2Pet(petKey, getItemId(), effects);
		if (!changemap.isEmpty()) {
			SRefreshPetData petdata = new SRefreshPetData();
			petdata.columnid = PetColumnTypes.PET;
			petdata.petkey = petKey;
			petdata.datas = (HashMap<Integer, Float>) changemap;
			mkdb.Procedure.psendWhileCommit(roleid, petdata);
			// 运营日志
			writeYYLogger(useNum);
			return Commontext.UseResult.SUCC;
		}
		return Commontext.UseResult.FAIL;
	}

	/**
	 * 运营日志
	 * 
	 * @param useNum
	 */
	private void writeYYLogger(final int useNum) {
		OpPetTraBean opPetTraBean = new OpPetTraBean(getItemId(), useNum, fire.pb.attr.AttrType.PET_LIFE);
		YYLogger.petTraLog(roleid, opPetTraBean);
	}

	protected UseResult appendToRole(final long roleId, final int usenum) {
		fire.pb.skill.SceneSkillRole sobjrole = fire.pb.skill.SkillManager.getSceneSkillRole(roleId);
		java.util.Map<Integer, Float> changemap = sobjrole.useItem(getAppendEffects(usenum));
		if (!changemap.isEmpty()) {
			SRefreshRoleData roledata = new SRefreshRoleData();
			roledata.datas = (HashMap<Integer, Float>) changemap;
			mkdb.Procedure.psendWhileCommit(roleId, roledata);
			return Commontext.UseResult.SUCC;
		}
		return UseResult.FAIL;
	}

	/**
	 * 判断物品是否是时效性物品
	 * 
	 * @return
	 */
	public boolean isTimeoutItem() {
		return itemAttr.getValidtime() > 0;
	}

	/**
	 * 设置物品有效时间
	 * 
	 * @param timeout
	 * @return
	 */
	boolean setTimeout(long timeout) {
		if (!isTimeoutItem()) {
			return false;
		}
		if (itemData.isData() || hasFlag(itemData, fire.pb.Item.TIMEOUT))
			return false;
		itemData.setFlags(itemData.getFlags() | fire.pb.Item.TIMEOUT);
		SRefreshItemFlag ref = new SRefreshItemFlag(keyinpack, itemData.getFlags(), packid);
		mkdb.Procedure.psendWhileCommit(roleid, ref);
		itemData.setLoseeffecttime(timeout);
		sendTimeout();
		return true;
	}

	/**
	 * 给客户端发送过期时间
	 */
	public void sendTimeout() {
		if ((getFlags() & fire.pb.Item.TIMEOUT) == 0) {
			return;
		}
		SRefreshItemTimeout send = new SRefreshItemTimeout();
		send.packid = packid;
		send.itemkey = keyinpack;
		if (WorldTime.getInstance().getTimeInMillis() > itemData.getLoseeffecttime())
			send.timeout = -1;
		else
			send.timeout = itemData.getLoseeffecttime() - WorldTime.getInstance().getTimeInMillis();
		mkdb.Procedure.psendWhileCommit(roleid, send);
	}

	/**
	 * 通知客户端物品已经过期
	 */
	public void onTimeout() {
		SRefreshItemTimeout send = new SRefreshItemTimeout();
		send.packid = packid;
		send.itemkey = keyinpack;
		send.timeout = -1;
		if (mkdb.Transaction.current() != null) {
			mkdb.Procedure.psendWhileCommit(roleid, send);
		} else
			mkdb.Procedure.psend(roleid, send);
	}

	/**
	 * 是否是珍品
	 * 
	 * @author yangtao
	 * @dateTime 2016年9月20日 下午1:55:21
	 * @version 1.0
	 * @return
	 */
	public boolean isTreasure() {
		//杂货表
		if (this instanceof GroceryItem) {
			fire.pb.item.GroceryItemShuXing groceryattr = (fire.pb.item.GroceryItemShuXing) getItemAttr();
			if (groceryattr.getTreasure() == 1) {
				return true;
			}
			return false;
		}
		 //装备
		if (this instanceof EquipItem) {
			fire.pb.item.EquipItem equipItem = (fire.pb.item.EquipItem) this;
			if (equipItem.getTreasure() == 1) {
				return true;
			}
			return false;
		}
		//宠物物品
		if (this instanceof PetItem) {
			fire.pb.item.PetItemShuXing petItem = (fire.pb.item.PetItemShuXing) getItemAttr();
			if (petItem.getTreasure() == 1) {
				return true;
			}
			return false;
		}
		return false;
	}
}
