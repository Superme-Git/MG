package fire.pb.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.PReqRoleInfoProc;
import fire.pb.PropConf;
import fire.pb.attr.AttrType;
import fire.pb.attr.EffectType;
import fire.pb.attr.SRefreshPetData;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.buff.continual.ConstantlyBuffConfig;
import fire.pb.item.Commontext.UseResult;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.Result;
import fire.pb.talk.MessageMgr;
import fire.script.JavaScript;
import fire.script.SceneJSEngine;

public abstract class FoodItem extends ItemBase {
	protected xbean.RecoveryAttr quality; // 品质
	
	public FoodItem(ItemMgr im, int itemid) {
		super(im, itemid);
		if (!checkHasQuality()) {
			quality = null;
			return;
		}
		quality = xbean.Pod.newRecoveryAttr();
		quality.setQuality(genQuality());
		insertQuality();
	}

	public FoodItem(ItemMgr im, int itemid, mkdb.Bean extinfo) {
		super(im, itemid);
		if (!checkHasQuality()) {
			quality = null;
			return;
		}
		quality = xbean.Pod.newRecoveryAttr();
		quality.setQuality(genQuality());
		insertQuality();
		setExtinfo(extinfo);
	}

	private void insertQuality() {
		final long extkey = xtable.Quality.insert(quality);
		itemData.setExtid(extkey);
	}

	private final boolean checkHasQuality() {
		FoodItemAttr fattr = (FoodItemAttr) itemAttr;
		return fattr.是否有品质 == 1;
	}

	private void setExtinfo(mkdb.Bean extinfo) {
		if (!checkHasQuality())
			return;
		if (extinfo instanceof xbean.RecoveryAttr) {
			quality = (xbean.RecoveryAttr) extinfo;
			insertQuality();
		}
	}

	public FoodItem(ItemMgr im, xbean.Item item) {
		super(im, item);
		if (!checkHasQuality()) {
			quality = null;
			return;
		}
		quality = xtable.Quality.select(item.getExtid());
		if (quality == null) {
			Module.getInstance().getLogger().error(new StringBuffer().append("食品库不完整,请检查原因:").append(item.getId()));
		}
	}

	
	public void setQuality(int q) {
		if (quality != null) {
			quality.setQuality(q);
		}
	}

	@Override
	public void onDeleted() {
		if (!checkHasQuality()) {
			return;
		}
		if (fire.pb.main.Gs.isDebug() && this.itemData.getExtid() <= 0)
			throw new RuntimeException("数据错误");
		xtable.Quality.remove(itemData.getExtid());
	}

	@Override
	public void onInserted() {
	}

	// 生成物品品质
	private int genQuality() {
		return fire.pb.util.Misc.getRandomBetween(1, 100);
	}

	@Override
	protected List<Effect> getAppendEffects(int num) {
		List<Effect> effectlist = new ArrayList<Effect>();
		if (!checkHasQuality()) {
			if (itemAttr == null || !(itemAttr instanceof FoodItemAttr)) {
				return effectlist;
			}
			FoodItemAttr ia = (FoodItemAttr) itemAttr;
			if (ia.getAddhp() != 0) {
				effectlist.add(new Effect(EffectType.HP_ABL,
						(float) (ia.addhp * num)));
			}

			if (ia.getAddmp() != 0) {
				effectlist.add(new Effect(EffectType.MP_ABL,
						(float) (ia.addmp * num)));
			}


		} else {
			List<Effect> effects = QualityToEffect.getInstance().getEffect(
					itemData.getId(), quality.getQuality());
			for (Effect effect : effects) {
				effectlist.add(new Effect(effect.effectid, effect.value * num));
			}
		}
		return effectlist;
	}

	@Override
	public xbean.RecoveryAttr getExtInfo() {
		return quality;
	}

	public int getQuality() {
		if (quality == null)
			return 0;
		return quality.getQuality();
	}

	@Override
	protected UseResult appendToRole(final long roleId, final int usenum) {
		UseResult ret = super.appendToRole(roleId, usenum);
		return ret;
	}

	@Override
	public Octets getTips() {
		if (os == null) {
			os = new OctetsStream();

			if (quality != null)
				os.marshal(quality.getQuality());
			else
				os.marshal(0);
		}
		return os;
	}

	@Override
	public UseItemHandler getUseItemHandler() {
		return new FoodUseHandler();
	}

	private static class FoodUseHandler implements UseItemHandler {
		@SuppressWarnings("null")
		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			fire.pb.buff.BuffRoleImpl rolebuff = new fire.pb.buff.BuffRoleImpl(
					roleId, false);
			SItemBuff sItemBuff = fire.pb.main.ConfigManager.getInstance()
					.getConf(fire.pb.item.SItemBuff.class).get(bi.getItemId());
			if (sItemBuff == null) {
				return Commontext.UseResult.FAIL;
			}
			List<BuffUnit> buffArgs = null;
			try {
				buffArgs = Module.getItemBuffs(sItemBuff.outskill_id);
				if (buffArgs == null || buffArgs.size() < 1) {
					return Commontext.UseResult.FAIL;
				}
				for (BuffUnit buffarg : buffArgs) {
					if (buffarg == null)
						continue;
					ConstantlyBuffConfig buffconf = fire.pb.buff.Module
							.getInstance()
							.getDefaultCBuffConfig(buffarg.buffIndex);
					if (buffconf != null) {
						int nqulity = 1;

						if (bi instanceof FoodItem) {
							nqulity = ((FoodItem) bi).getQuality();
						}
						
						JavaScript js = null;
						js = buffarg.effectJavascriptMap.get(EffectType.HP_ABL);
						if (js == null) {
							js = buffarg.effectJavascriptMap.get(EffectType.MP_ABL);
						}
						boolean bSp = false;
						if (js == null) {// 怒气直接加
							js = buffarg.effectJavascriptMap.get(EffectType.SP_ABL);
							if (js != null)
								bSp = true;
						}
						if (js == null)
							throw new NullPointerException("配置有误");
						SceneJSEngine jsengine = new SceneJSEngine();
						jsengine.setQuality(nqulity);
						Double val = js.eval(jsengine,null,null);
						if (bSp == true) {
							fire.pb.effect.RoleImpl role = new fire.pb.effect.RoleImpl(
									rolebuff.getRoleId(), false);
							if (role.getSp() >= PropConf.Battle.BATTLEENTER_SP_MAX) {
								MessageMgr.psendMsgNotify(roleId, 160251, null);
								return Commontext.UseResult.FAIL;
							}
							
							role.addSp(val.intValue(), PropConf.Battle.BATTLEENTER_SP_MAX);
							final fire.pb.attr.SRefreshRoleData data = new fire.pb.attr.SRefreshRoleData();
							data.datas.put(fire.pb.attr.AttrType.SP, (float) role.getSp());
							mkdb.Procedure.psendWhileCommit(rolebuff.getRoleId(), data);
							return Commontext.UseResult.SUCC;
						}

						ConstantlyBuff oldbuff = rolebuff.getBuff(buffarg.buffIndex);
						long oldval = 0;
						if (oldbuff != null)
							oldval = oldbuff.getAmount();

						ConstantlyBuff buff = fire.pb.buff.Module.getInstance()
								.createConstantlyBuff(buffarg.buffIndex);
						buff.setAmount(val.longValue() + oldval);

//						ConstantlyBuff buff = genBuff(buffarg, rolebuff, nqulity);
						if (buff != null) {
							rolebuff.addCBuffWithSP(buff);
							checkBuff(roleId, buff);
							if (buff.getId() == BuffConstant.ROLE_RECOVER_HP) {
								List<String> para = new ArrayList<String>(1);
								para.add(String.valueOf(val.intValue()));
								MessageMgr.psendMsgNotify(roleId, 150093, para);
								mkdb.Procedure.pexecuteWhileCommit(new PReqRoleInfoProc(
												roleId));
							} else if (buff.getId() == BuffConstant.ROLE_RECOVER_MP) {
								List<String> para = new ArrayList<String>(1);
								para.add(String.valueOf(val.intValue()));
								MessageMgr.psendMsgNotify(roleId, 150094, para);
								mkdb.Procedure.pexecuteWhileCommit(new PReqRoleInfoProc(
												roleId));
							}
						}
					} else {
						throw new NullPointerException("道具战斗外使用带了一次性buff！");
						
					}
				}
				return Commontext.UseResult.SUCC;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Commontext.UseResult.FAIL;
		}

		private void checkBuff(long roleId, ConstantlyBuff buff) {
			fire.pb.buff.BuffRoleImpl bri = new fire.pb.buff.BuffRoleImpl(
					roleId, false);
			fire.pb.effect.RoleImpl role = new fire.pb.effect.RoleImpl(
					roleId, false);

			SRefreshRoleData sendrole = new SRefreshRoleData();

			if (buff.getId() == BuffConstant.ROLE_RECOVER_HP) {
				// int res = role.getMaxHp() - role.getHp();
				// 恢复角色血量
				long res = (long) (role.getUplimithp() - role.getHp());
				if (buff.getAmount() < res)
					res = buff.getAmount();
				if (res > 0) {
					role.addHp((int) res);
					sendrole.datas.put(AttrType.HP, (float) role.getHp());
					buff.setAmount(buff.getAmount() - res);
					if (buff.getAmount() == 0) {
						bri.removeCBuffWithSP(BuffConstant.ROLE_RECOVER_HP);
					} else {
						Result result = new Result(true);
						result.addAddedBuff(buff);
						bri.psendSBuffChangeResult(result);
					}
				}
			}
			if (buff.getId() == BuffConstant.ROLE_RECOVER_MP) {
				// 恢复角色魔法
				long res = (long) (role.getMaxMp() - role.getMp());
				if (buff.getAmount() < res)
					res = buff.getAmount();
				if (res > 0) {
					role.addMp((int) res);
					sendrole.datas.put(AttrType.MP, (float) role.getMp());
					buff.setAmount(buff.getAmount() - res);
					if (buff.getAmount() == 0) {
						bri.removeCBuffWithSP(BuffConstant.ROLE_RECOVER_MP);
					} else {
						Result result = new Result(true);
						result.addAddedBuff(buff);
						bri.psendSBuffChangeResult(result);
					}
				}
			}
			if (!sendrole.datas.isEmpty())
				mkdb.Procedure.psendWhileCommit(roleId, sendrole);

			fire.pb.effect.PetImpl pet = null;		
			PetColumn depotcol = new PetColumn(roleId,PetColumnTypes.PET,false);
			Set<Integer> existIds = depotcol.getPetsMap().keySet();
			if(existIds.size() > 0)
			{
				for(int  petkey: existIds)
				{
					if (petkey != -1 && petkey != 0 )
					{
						pet = new fire.pb.effect.PetImpl( roleId, petkey, false );
						SRefreshPetData sendpet = new SRefreshPetData();
						if (buff.getId() == BuffConstant.ROLE_RECOVER_HP) {
							long res = (long)(pet.getMaxHp() - pet.getHp()) ;
							if ( buff.getAmount() < res )
								res = buff.getAmount();
							if ( res > 0 ) {
								pet.addHp((int)res );
								sendpet.datas.put( AttrType.HP, (float)pet.getHp() );
								buff.setAmount( buff.getAmount() - res );
								if ( buff.getAmount() == 0 ) {
									bri.removeCBuffWithSP(  BuffConstant.ROLE_RECOVER_MP);
								} else {
									Result result = new Result(true);
									result.addAddedBuff(buff);
									bri.psendSBuffChangeResult(result);
								}
							}
						}
						if (buff.getId() == BuffConstant.ROLE_RECOVER_MP) {
							long res = (long)(pet.getMaxMp() - pet.getMp()) ;
							if ( buff.getAmount() < res )
								res = buff.getAmount();
							if ( res > 0 ) {
								pet.addMp((int)res );
								sendpet.datas.put( AttrType.MP, (float)pet.getMp() );
								buff.setAmount( buff.getAmount() - res );
								if ( buff.getAmount() == 0 ) {
									bri.removeCBuffWithSP(BuffConstant.ROLE_RECOVER_MP);
								} else {
									Result result = new Result(true);
									result.addAddedBuff(buff);
									bri.psendSBuffChangeResult(result);
								}
							}
						}

						if (!sendpet.datas.isEmpty()) {
							sendpet.petkey = petkey;
							sendpet.columnid = fire.pb.pet.PetColumnTypes.PET;
							mkdb.Procedure.psendWhileCommit( roleId, sendpet );
						}
					}
				}
			}
		}
	}
}
