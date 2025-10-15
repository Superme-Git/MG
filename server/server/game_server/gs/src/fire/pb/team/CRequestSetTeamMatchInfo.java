
package fire.pb.team;
import fire.pb.main.ConfigManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestSetTeamMatchInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 设置匹配信息
 * @author changhao
 *
 */
public class CRequestSetTeamMatchInfo extends __CRequestSetTeamMatchInfo__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure requestsetteammatchinfo = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{	
				if (!checkLevel()) //检查等级 by changhao
				{
					psend(roleid, new STeamError(TeamError.LevelError));
					TeamManager.logger.debug("CRequestSetTeamMatchInfo匹配等级设置错误 "+roleid);
					return true;					
				}				
				
				if (targetid != 0)
				{
					if (fire.pb.fushi.Module.GetPayServiceType() == 1) //点卡服务器 by changhao
					{
						DSTeamMatchInfo config = ConfigManager.getInstance().getConf(DSTeamMatchInfo.class).get(targetid);
						if (config == null)
						{
							psend(roleid, new STeamError(TeamError.NoTarget));
							TeamManager.logger.debug("CRequestSetTeamMatchInfo:目标ID错误 "+roleid);
							return true;					
						}							
					}
					else
					{
						STeamMatchInfo config = ConfigManager.getInstance().getConf(STeamMatchInfo.class).get(targetid);
						if (config == null)
						{
							psend(roleid, new STeamError(TeamError.NoTarget));
							TeamManager.logger.debug("CRequestSetTeamMatchInfo:目标ID错误 "+roleid);
							return true;					
						}							
					}				
				}
				
				Long teamid = xtable.Roleid2teamid.select(roleid);
				
				Team team = null;
				if(teamid != null)
				{
					team = TeamManager.getTeamByTeamID(teamid);	//有队先把队伍锁住 by changhao
					team.getTeamInfo().setTargetid(targetid);
					team.getTeamInfo().setMinlevel(levelmin);
					team.getTeamInfo().setMaxlevel(levelmax);
				}
				
				Long roleidteamId = xtable.Roleid2teamid.get(roleid);
				if (teamid != roleidteamId)
				{
					psend(roleid, new STeamError(TeamError.UnkownError));
					TeamManager.logger.debug("CRequestSetTeamMatchInfo:队伍ID有变化 "+roleid);
					return true;
				}
								
				SRequestTeamMatch msg = new SRequestTeamMatch();
				msg.levelmin = levelmin;
				msg.levelmax = levelmax;
				msg.targetid = targetid;
				msg.typematch = 3;
				
				mkdb.Procedure.psendWhileCommit(roleid, msg);
				
				return true;
			}
			
		};
		requestsetteammatchinfo.submit();	
	}
	
	private boolean checkLevel() //检查等级相关错误 by changhao
	{
		if (this.levelmin > this.levelmax)
		{
			return false;
		}
		
		if (this.levelmin <= 0 || this.levelmax <= 0)
		{
			return false;
		}
		
		//取表数据检查是否范围内 by changhao
		
		return true;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794499;

	public int getType() {
		return 794499;
	}

	public int targetid; // 目标id by changhao
	public int levelmin; // 等级下限 如果是个人匹配这个忽略 by changhao
	public int levelmax; // 等级上限 如果是个人匹配这个忽略 by changhao

	public CRequestSetTeamMatchInfo() {
	}

	public CRequestSetTeamMatchInfo(int _targetid_, int _levelmin_, int _levelmax_) {
		this.targetid = _targetid_;
		this.levelmin = _levelmin_;
		this.levelmax = _levelmax_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(targetid);
		_os_.marshal(levelmin);
		_os_.marshal(levelmax);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		targetid = _os_.unmarshal_int();
		levelmin = _os_.unmarshal_int();
		levelmax = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestSetTeamMatchInfo) {
			CRequestSetTeamMatchInfo _o_ = (CRequestSetTeamMatchInfo)_o1_;
			if (targetid != _o_.targetid) return false;
			if (levelmin != _o_.levelmin) return false;
			if (levelmax != _o_.levelmax) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += targetid;
		_h_ += levelmin;
		_h_ += levelmax;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(targetid).append(",");
		_sb_.append(levelmin).append(",");
		_sb_.append(levelmax).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestSetTeamMatchInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = targetid - _o_.targetid;
		if (0 != _c_) return _c_;
		_c_ = levelmin - _o_.levelmin;
		if (0 != _c_) return _c_;
		_c_ = levelmax - _o_.levelmax;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

