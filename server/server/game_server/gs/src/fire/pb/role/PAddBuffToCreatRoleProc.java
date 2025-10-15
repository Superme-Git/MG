package fire.pb.role;

import fire.pb.PropConf;
import fire.pb.attr.EffectType;
import fire.pb.buff.continual.ConstantlyBuff;
import fire.pb.item.SItemBuff;
import fire.pb.skill.BuffUnit;
import fire.script.JavaScript;
import fire.script.SceneJSEngine;

public class PAddBuffToCreatRoleProc extends mkdb.Procedure{
	private final long roleId;
	
	public PAddBuffToCreatRoleProc(long roleId){
		this.roleId = roleId;
	}

	@Override
	protected boolean process() throws Exception {
		fire.pb.buff.BuffRoleImpl rolebuff = new fire.pb.buff.BuffRoleImpl(roleId, false );
		
		ConstantlyBuff hpbuff = genBuff(32037,rolebuff);
		if(hpbuff != null){
			rolebuff.addCBuffWithSP(hpbuff);
		}
		
		ConstantlyBuff mpbuff = genBuff(32038,rolebuff);
		if(mpbuff != null){
			rolebuff.addCBuffWithSP(mpbuff);
		}
		
		return true;
	}
	
	
	private ConstantlyBuff genBuff(int itemid, fire.pb.buff.BuffRoleImpl rolebuff) {
		SItemBuff sItemBuff =  fire.pb.main.ConfigManager.getInstance().getConf(
			fire.pb.item.SItemBuff.class).get(itemid);
		if(sItemBuff == null)
		{
//			BattleField.logger.error("ERROR 战斗内使用物品：物品buff不存在。RoleId：" + roleId+" ;ItemKey: " + itemKey);
			return null;
		}
		
		BuffUnit buffArg = null;
		try {
			buffArg = fire.pb.item.Module.GetItemBuff(sItemBuff.outskill_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (buffArg == null)
		{
//			BattleField.logger.error("ERROR 战斗内使用物品：物品buff解析错误。RoleId：" + roleId+" ;ItemKey: " + itemKey);
			return null;
		}
		JavaScript js =null;
		js = buffArg.effectJavascriptMap.get( EffectType.HP_ABL );
		if ( js == null ) {
			js = buffArg.effectJavascriptMap.get( EffectType.MP_ABL );
		}
//		if ( js == null ) {
//			js = buffArgs[0].effectJsMap.get( EffectType.PET_LOYALTY_ABL );
//		}
		boolean bSp = false;
		if ( js == null ) {//怒气直接加
			js = buffArg.effectJavascriptMap.get( EffectType.SP_ABL );
			if ( js != null )
				bSp = true;					
		}
		if ( js == null )
			throw new NullPointerException( "配置有误" );
		double val = js.eval( new SceneJSEngine() ,null,null);
		if(bSp == true)
		{
			fire.pb.effect.RoleImpl role = new fire.pb.effect.RoleImpl( rolebuff.getRoleId(), false );
			role.addSp((int)val, PropConf.Battle.BATTLEENTER_SP_MAX);
			final fire.pb.attr.SRefreshRoleData data = new fire.pb.attr.SRefreshRoleData();
			data.datas.put(fire.pb.attr.AttrType.SP, (float)role.getSp());
			mkdb.Procedure.psendWhileCommit(rolebuff.getRoleId(), data);
			return null;
		}
		
		ConstantlyBuff oldbuff = rolebuff.getBuff(buffArg.buffIndex);
		long oldval = 0;
		if(oldbuff != null)
			oldval = oldbuff.getAmount();
		ConstantlyBuff newbuff = fire.pb.buff.Module.getInstance().createConstantlyBuff( buffArg.buffIndex );
		newbuff.setAmount( (long)val + oldval );
		return newbuff;
	}
}
