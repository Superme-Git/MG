package fire.pb;
import fire.log.YYLogger;
import fire.pb.buff.BuffConstant;
import fire.pb.item.RoleAddPointEvent;
import mkdb.Procedure;
import xbean.BasicFightProperties;

/**
 * 角色属性加点存储过程
 *
 */
public class PAddPointProc extends mkdb.Procedure{
	private final long roleId;
	private final CAddPointToAttr protocol;
	
	public PAddPointProc(long roleid, final CAddPointToAttr protocol){
		this.roleId = roleid;
		this.protocol = protocol;
	}
	
	@Override
	public boolean process(){
		// 数据合法性判断
		if (null == protocol)
			return false;
		if (!protocol._validator_())
			return false;
		if (protocol.agi == 0 && protocol.cons == 0 
				&& protocol.endu == 0
				&& protocol.iq == 0 
				&& protocol.str==0)
			return false;
		
		if(fire.pb.buff.Module.existState(roleId, BuffConstant.StateType.STATE_BATTLE_FIGHTER))
			return false;
		
		fire.pb.effect.Role role = new fire.pb.effect.RoleImpl(roleId);
		java.util.Map<Integer,Float> res = role.addPoints(protocol.cons, protocol.str, protocol.agi, protocol.endu, protocol.iq);
		if (res != null){
			final fire.pb.attr.SRefreshRoleData send = new fire.pb.attr.SRefreshRoleData();
			send.datas.putAll(res);
			Procedure.psendWhileCommit(roleId, send);
			
			//刷新人物加点信息
			final fire.pb.attr.SRefreshPointType refresh = new fire.pb.attr.SRefreshPointType();

			xbean.Properties prop = xtable.Properties.get(roleId);
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
			
			Procedure.psendWhileCommit(roleId, refresh);
			
			fire.pb.event.Poster.getPoster().dispatchEvent(new RoleAddPointEvent(roleId));
			
			//运营日志
			writeYYLogger(bfp);
			return true;
		}
		return false;
	}

	/**
	 * 运营日志
	 * @param bfp
	 */
	private void writeYYLogger(BasicFightProperties bfp) {
		YYLogger.characLog(roleId, bfp.getCons(), bfp.getIq(), bfp.getStr(), bfp.getEndu(), bfp.getAgi());
	}
}
