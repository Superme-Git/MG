
package fire.pb.mission;

import fire.pb.map.DuplicateHelper;
import fire.pb.map.GridPos;
import fire.pb.map.Role;
import fire.pb.map.RoleManager;
import fire.pb.move.DynamicSceneType;
import fire.pb.move.SRoleEnterScene;
import fire.pb.talk.MessageMgr;
import fire.pb.team.PAbsentReturnTeam;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;






// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CDefineTeam__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CDefineTeam extends __CDefineTeam__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid <= 0) {
			return;
		}
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				Long teamId = xtable.Roleid2teamid.select(roleid);
				if (teamId == null)
					return false;
				
				Team team = TeamManager.selectTeamByTeamID(teamId);
				if (team == null)
					return false;
				
				if (answer == 1) {
					//直接进入队长所在的动态场景中
					long leaderroleId = team.getTeamInfo().getTeamleaderid();
					
					Role leaderRole = RoleManager.getInstance().getRoleByID(leaderroleId);
					
					GridPos gridPos = leaderRole.getPos().toGridPos();
					DuplicateHelper.enterDynamicSceneByIdWhileCommit(roleid, leaderRole.getMapId(), leaderRole.getScene(),
							gridPos.getX(), gridPos.getY(),
							DynamicSceneType.NORMAL_SCENE, SRoleEnterScene.CHEFU);
					
					PAbsentReturnTeam absentReturnTeamP = new PAbsentReturnTeam(roleid, 0);	
					mkdb.Executor.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							absentReturnTeamP.submit();
						}
					}, 2, TimeUnit.SECONDS);
				}
				else {
					String roleName = xtable.Properties.selectRolename(roleid);
					if (roleName == null)
						return false;
					List<String> para = new ArrayList<String>(1);
					para.add(roleName);
					for (Long rid : team.getNormalMemberIds()) {
						if (rid != roleid) {
							MessageMgr.psendMsgNotifyWhileCommit(rid, 160211, para);
						}
					}
				}
				return true;
			}
			
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805548;

	public int getType() {
		return 805548;
	}

	public short answer; // 1表示同意, 0表示不同意

	public CDefineTeam() {
	}

	public CDefineTeam(short _answer_) {
		this.answer = _answer_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(answer);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		answer = _os_.unmarshal_short();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CDefineTeam) {
			CDefineTeam _o_ = (CDefineTeam)_o1_;
			if (answer != _o_.answer) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += answer;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(answer).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CDefineTeam _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = answer - _o_.answer;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

