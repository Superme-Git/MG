
package fire.pb.npc;
import java.util.ArrayList;
import java.util.List;
import fire.pb.team.Team;
import fire.pb.team.TeamManager;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAbandonMacth__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAbandonMacth extends __CAbandonMacth__ {
	@Override
	protected void process() {
		// protocol handle
		final Long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.npcBattleInfoCol npcBattle = xtable.Npcbattleinfo.get(npckey); 
				if (npcBattle == null)
					return false;
				if (!npcBattle.getBattleroles().containsKey(roleid))
					return false;
				//从匹配列表里删除
				npcBattle.getBattleroles().remove(roleid);
				xtable.Role2npcbattle.remove(roleid);
				
				//给队员发送队长已经取消的消息
				List<Long> allRoles = new ArrayList<Long>();
				Team team = TeamManager.selectTeamByRoleId(roleid);
				if (team != null) {
					allRoles.addAll(team.getNormalMemberIds());
					mkdb.Procedure.psend(allRoles, new SMacthResult(npckey, 0));
				}
				
				if (npcBattle.getBattleroles().size() == 0)
					xtable.Npcbattleinfo.remove(npckey);
				
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795670;

	public int getType() {
		return 795670;
	}

	public long npckey; // npckey

	public CAbandonMacth() {
	}

	public CAbandonMacth(long _npckey_) {
		this.npckey = _npckey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAbandonMacth) {
			CAbandonMacth _o_ = (CAbandonMacth)_o1_;
			if (npckey != _o_.npckey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAbandonMacth _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

