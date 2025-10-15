
package fire.pb.team.teammelon;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CTeamRollMelon__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CTeamRollMelon extends __CTeamRollMelon__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure teamrollmelon = new mkdb.Procedure() 
		{
			@Override
			/*加锁顺序team by changhao*/
			protected boolean process()
			{	
				xbean.Properties roleprop = xtable.Properties.select(roleid);
				
				Long battlemelonid = xtable.Roleid2battlemelonid.select(roleid);
				if (battlemelonid == null)
				{
					psend(roleid, new fire.pb.team.STeamError(fire.pb.team.TeamError.SelfNotInTeam));
					fire.pb.team.TeamManager.logger.debug("CTeamRollMelon: "+roleid);
					return true;					
				}
				
				xbean.ETeamMelon eteammelon = xtable.Battlemelonid2melon.get(battlemelonid);
				if (eteammelon == null)
				{
					psend(roleid, new fire.pb.team.STeamError(fire.pb.team.TeamError.SelfNotInTeam));
					fire.pb.team.TeamManager.logger.debug("CTeamRollMelon:没有奖励可分配"+roleid);
					return true;					
				}
				
				xbean.TeamMelon teammelon = eteammelon.getMelonid2melons().get(melonid);
				if (teammelon == null)
				{
					psend(roleid, new fire.pb.team.STeamError(fire.pb.team.TeamError.SelfNotInTeam));
					fire.pb.team.TeamManager.logger.debug("CTeamRollMelon:没有奖励可分配"+roleid);
					return true;					
				}
				
				//此人没有资格ROLL点 by changhao
				Integer rollpoint = teammelon.getMelonroleids().get(roleid);
				if (rollpoint == null)
				{
					psend(roleid, new fire.pb.team.STeamError(fire.pb.team.TeamError.SelfNotInTeam));
					fire.pb.team.TeamManager.logger.debug("CTeamRollMelon:没有奖励可分配"+roleid);
					return true;	
				}
				
				Integer alreadroll = teammelon.getOpmelonroleids().get(roleid);
				if (alreadroll != null)
				{
					fire.pb.team.TeamManager.logger.debug("CTeamRollMelon:已经ROLL过了"+roleid);
					return true;
				}
				
				if (status == 0) //某人如果放弃了 by changhao
				{					
					teammelon.getMelonroleids().put(roleid, 0);
				}
				
				int num = teammelon.getOpnum();
				num ++;
				teammelon.setOpnum(num);
				
				teammelon.getOpmelonroleids().put(roleid, teammelon.getMelonroleids().get(roleid));
				
				fire.pb.team.teammelon.SOneTeamRollMelonInfo msg = new fire.pb.team.teammelon.SOneTeamRollMelonInfo();
				msg.itemid = teammelon.getItemid();
				msg.melonid = melonid;
				msg.rollinfo.roleid = roleid;
				msg.rollinfo.rolename = roleprop.getRolename();
				msg.rollinfo.roll = teammelon.getMelonroleids().get(roleid);
				
				for (Long e : eteammelon.getMelonerlist()) //发给所有队员最终的这个人ROLL点情况 by changhao
				{
					//TeamManager.logger.info("roll点:发送给roleid:" + e + "点数:" + msg.rollinfo.roll);
					mkdb.Procedure.psendWhileCommit(e, msg);			
				}
				
				for (Long e : eteammelon.getWatchmelonerlist()) //发给观看 ROLL点的人 by changhao
				{
					if (e != null)
						mkdb.Procedure.psendWhileCommit(e, msg);			
				}
				
				if (num >= teammelon.getMelonroleids().size())
				{
					new PTeamRollMelonInfo(battlemelonid, 0).call();
				}
				
				return true;
			}
			
		};
		teamrollmelon.submit();
	}
	
	/*
	 * 
	 * 最终决定谁的点最大 by changhao
	 */
	public long calcMaxRollPoint(java.util.ArrayList<Long> roleids, xbean.TeamMelon teammelon)
	{
		int max = 0;
		long maxroleid = 0;
		for (Long roleid : roleids)
		{
			Integer rollpoint = teammelon.getMelonroleids().get(roleid);
			if (rollpoint >= max)
			{
				max = rollpoint;
				maxroleid = roleid;
			}
		}
		
		return maxroleid;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794523;

	public int getType() {
		return 794523;
	}

	public long melonid; // 分赃id by changhao
	public int status; // 1是ROLL 0是放弃 by changhao

	public CTeamRollMelon() {
	}

	public CTeamRollMelon(long _melonid_, int _status_) {
		this.melonid = _melonid_;
		this.status = _status_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(melonid);
		_os_.marshal(status);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		melonid = _os_.unmarshal_long();
		status = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CTeamRollMelon) {
			CTeamRollMelon _o_ = (CTeamRollMelon)_o1_;
			if (melonid != _o_.melonid) return false;
			if (status != _o_.status) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)melonid;
		_h_ += status;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(melonid).append(",");
		_sb_.append(status).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CTeamRollMelon _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(melonid - _o_.melonid);
		if (0 != _c_) return _c_;
		_c_ = status - _o_.status;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

