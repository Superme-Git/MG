package fire.pb;

import mkdb.Procedure;
import xbean.BasicFightProperties;

/**
 *	切换加点方案
 */
public class PChangePointProc extends mkdb.Procedure{
	private final long roleid;
	private final int schemeid;
	
	public PChangePointProc(final long roleid, final int schemeid){
		this.roleid = roleid;
		this.schemeid = schemeid;
	}
	
	@Override
	public boolean process(){
		if (fire.pb.buff.Module.existState(roleid, fire.pb.buff.BuffConstant.StateType.STATE_BATTLE_FIGHTER)) {
			fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 145104, null);
			return false;
		}
		
		if (schemeid == 0)
			return false;	
		
		xbean.Properties prop = xtable.Properties.get(roleid);
		if (prop.getScheme() == schemeid)
			return false;
		
		fire.pb.effect.Role role = new fire.pb.effect.RoleImpl(roleid);
		java.util.Map<Integer,Float> res = role.changeScheme(schemeid);
		if (res != null)
		{
			final fire.pb.attr.SRefreshRoleData send = new fire.pb.attr.SRefreshRoleData();
			send.datas.putAll(res);
			Procedure.psendWhileCommit(roleid, send);
			
			//刷新人物加点信息
			final fire.pb.attr.SRefreshPointType refresh = new fire.pb.attr.SRefreshPointType();			
			BasicFightProperties bfp = prop.getBfp();
			
			refresh.bfp.agi = (short)bfp.getAgi();
			refresh.bfp.cons = (short)bfp.getCons();
			refresh.bfp.endu = (short)bfp.getEndu();
			refresh.bfp.iq = (short)bfp.getIq();
			refresh.bfp.str = (short)bfp.getStr();			
			
			refresh.bfp.agi_save.putAll(prop.getAddpointfp().getAgi_save());
			refresh.bfp.cons_save.putAll(prop.getAddpointfp().getCons_save());
			refresh.bfp.endu_save.putAll(prop.getAddpointfp().getEndu_save());
			refresh.bfp.iq_save.putAll(prop.getAddpointfp().getIq_save());
			refresh.bfp.str_save.putAll(prop.getAddpointfp().getStr_save());
			
			refresh.point.putAll(prop.getPoint());
			refresh.pointscheme = prop.getScheme();
			refresh.schemechanges = prop.getSchemechanges();
			
			Procedure.psendWhileCommit(roleid, refresh);
			
			return true;
		}
		
		return false;
	}

}
