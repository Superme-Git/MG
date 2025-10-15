package fire.pb.item.food;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fire.pb.PReqRoleInfoProc;
import fire.pb.PropConf;
import fire.pb.attr.AttrType;
import fire.pb.attr.EffectType;
import fire.pb.attr.SRefreshPetData;
import fire.pb.attr.SRefreshRoleData;
import fire.pb.buff.BuffConstant;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.item.Commontext;
import fire.pb.item.FoodItem;
import fire.pb.item.ItemBase;
import fire.pb.item.ItemMgr;
import fire.pb.item.SItemBuff;
import fire.pb.item.UseItemHandler;
import fire.pb.item.Commontext.UseResult;
import fire.pb.pet.PetColumn;
import fire.pb.pet.PetColumnTypes;
import fire.pb.skill.BuffUnit;
import fire.pb.skill.Result;
import fire.pb.talk.MessageMgr;
import fire.script.JavaScript;
import fire.script.SceneJSEngine;

public class AutoRecoveryItem extends FoodItem
{
	AutoRecoveryItem( ItemMgr im, int itemid, mkdb.Bean extinfo ) {
		super( im, itemid, extinfo );
	}
	
	AutoRecoveryItem( ItemMgr im, int itemid ) {
		super( im, itemid );
	}
	
	AutoRecoveryItem( ItemMgr im, xbean.Item item ) {
		super( im, item );
	}
	
	@Override
	public UseItemHandler getUseItemHandler() {
		return new AutoRecoveryUseHandler();
	}
	
	private static class AutoRecoveryUseHandler implements UseItemHandler {

		@Override
		public UseResult onUse(long roleId, ItemBase bi, int usednum) {
			fire.pb.buff.BuffRoleImpl rolebuff = new fire.pb.buff.BuffRoleImpl(roleId, false);
			
			SItemBuff sItemBuff = fire.pb.main.ConfigManager.getInstance()
					.getConf(fire.pb.item.SItemBuff.class).get(bi.getItemId());
			if (sItemBuff == null) {
				return Commontext.UseResult.FAIL;
			}

			BuffUnit buffArg = null;
			try {
				buffArg = fire.pb.item.Module
						.GetItemBuff(sItemBuff.outskill_id);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (buffArg == null) {
				return Commontext.UseResult.FAIL;
			}
			JavaScript js = null;
			js = buffArg.effectJavascriptMap.get(EffectType.HP_ABL);
			if (js == null) {
				js = buffArg.effectJavascriptMap.get(EffectType.MP_ABL);
			}
			
			boolean bSp = false;
			if (js == null) {// 怒气直接加
				js = buffArg.effectJavascriptMap.get(EffectType.SP_ABL);
				if (js != null)
					bSp = true;
			}
			if (js == null)
				throw new NullPointerException("配置有误");
			Double val = js.eval(new SceneJSEngine(),null,null);
			if (bSp == true) {
				fire.pb.effect.RoleImpl role = new fire.pb.effect.RoleImpl(
						rolebuff.getRoleId(), false);
				role.addSp(val.intValue(), PropConf.Battle.BATTLEENTER_SP_MAX);
				final fire.pb.attr.SRefreshRoleData data = new fire.pb.attr.SRefreshRoleData();
				data.datas.put(fire.pb.attr.AttrType.SP, (float) role.getSp());
				mkdb.Procedure.psendWhileCommit(rolebuff.getRoleId(), data);
				return null;
			}

			long itemAmount = 0;
			ConstantlyBuff itemBuff = rolebuff.getBuff(buffArg.buffIndex);
			if (itemBuff != null) {
				itemAmount = itemBuff.getAmount();
			}
			
			ConstantlyBuff newbuff = fire.pb.buff.Module.getInstance()
					.createConstantlyBuff(buffArg.buffIndex);
			newbuff.setAmount(val.intValue() + itemAmount);
			
			rolebuff.addCBuffWithSP(newbuff);
			if (!(fire.pb.buff.Module.existState(roleId, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER))) {
				checkBuff(roleId, newbuff);
			}
			if (newbuff.getId() == BuffConstant.ROLE_RECOVER_HP) {
				List<String> para = new ArrayList<String>(1);
				para.add(String.valueOf(val.intValue()));
				MessageMgr.psendMsgNotify(roleId, 150093, para);
				mkdb.Procedure.pexecuteWhileCommit(new PReqRoleInfoProc(roleId));
			}
			else if (newbuff.getId() == BuffConstant.ROLE_RECOVER_MP) {
				List<String> para = new ArrayList<String>(1);
				para.add(String.valueOf(val.intValue()));
				MessageMgr.psendMsgNotify(roleId, 150094, para);
				mkdb.Procedure.pexecuteWhileCommit(new PReqRoleInfoProc(roleId));
			}
			
			return Commontext.UseResult.SUCC;
		}

		private void checkBuff(long roleId, ConstantlyBuff buff) {
			fire.pb.buff.BuffRoleImpl bri = new fire.pb.buff.BuffRoleImpl( roleId, false );
			fire.pb.effect.RoleImpl role = new fire.pb.effect.RoleImpl( roleId, false );
			
			SRefreshRoleData sendrole = new SRefreshRoleData();
			if ( buff.getId() ==  BuffConstant.ROLE_RECOVER_HP ) {
			//	int res = role.getMaxHp() - role.getHp();
				long res = (long)(role.getUplimithp() - role.getHp()) ;
				if ( buff.getAmount() < res )
					res = buff.getAmount();
				if ( res > 0 ) {
					role.addHp((int)res );
					sendrole.datas.put( AttrType.HP, (float)role.getHp() );
					buff.setAmount( buff.getAmount() - res );
					if ( buff.getAmount() == 0 ) {
						bri.removeCBuffWithSP( BuffConstant.ROLE_RECOVER_HP );
					} else {
						Result result = new Result(true);
						result.addAddedBuff(buff);
						bri.psendSBuffChangeResult(result);
					}
				}
			}
			if ( buff.getId() == BuffConstant.ROLE_RECOVER_MP ) {
				long res = (long)(role.getMaxMp() - role.getMp());
				if ( buff.getAmount() < res )
					res = buff.getAmount();
				if ( res > 0 ) {
					role.addMp((int)res );
					sendrole.datas.put( AttrType.MP, (float)role.getMp() );
					buff.setAmount( buff.getAmount() - res );
					if ( buff.getAmount() == 0 ) {
						bri.removeCBuffWithSP( BuffConstant.ROLE_RECOVER_MP );
					} else {
						Result result = new Result(true);
						result.addAddedBuff(buff);
						bri.psendSBuffChangeResult(result);
					}
				}			
			}
			if (!sendrole.datas.isEmpty())
				mkdb.Procedure.psendWhileCommit( roleId, sendrole );
			
		
			fire.pb.effect.PetImpl pet = null;		
			PetColumn depotcol = new PetColumn(roleId,PetColumnTypes.PET,false);
			Set<Integer> existIds = depotcol.getPetsMap().keySet();
			if(existIds.size() > 0)
			{
				for(int  petkey: existIds)
				{
//					Integer petkey = protocolpet.key;//xtable.Properties.selectFightpetkey( roleId );
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
						if (buff.getId() ==  BuffConstant.ROLE_RECOVER_MP) {
							long res = (long)(pet.getMaxMp() - pet.getMp()) ;
							if ( buff.getAmount() < res )
								res = buff.getAmount();
							if ( res > 0 ) {
								pet.addMp((int)res );
								sendpet.datas.put( AttrType.MP, (float)pet.getMp() );
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
