
package fire.pb.move;

import robot.LoginRole;
import robot.LoginRoleMgr;
import robot.task.CircleTask;
import robot.task.ClanCopyTask;
import robot.task.ClanFightTask;
import robot.task.ShiLianTask;
import robot.task.ZhuXian;

import com.locojoy.base.Marshal.MarshalException;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SAddUserScreen__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SAddUserScreen extends __SAddUserScreen__ {
	@Override
	protected void process() {
		LoginRole role =  LoginRoleMgr.findLoginRole(this);
		if(role == null)
			return;
		if(role.getRoleTask() instanceof ZhuXian)//剧情任务
			role.onRoleAddProtocol(this);
		if(role.getRoleTask() instanceof CircleTask)//循环任务
			role.onRoleAddProtocol(this);
		if(role.getRoleTask() instanceof ShiLianTask)//冠军试炼任务
			role.onRoleAddProtocol(this);
		if(role.getRoleTask() instanceof ShiLianTask)//冠军试炼任务
			role.onRoleAddProtocol(this);
		if(role.getRoleTask() instanceof ClanFightTask)//公会战
			role.onRoleAddProtocol(this);
		if(role.getRoleTask() instanceof ClanCopyTask)//公会副本
			role.onRoleAddProtocol(this);
		
		for(RoleBasic rolebase : rolelist)
		{
			RoleBasicOctets octets = new RoleBasicOctets();
			try
			{
				octets.unmarshal(new OctetsStream(rolebase.rolebasicoctets));
				if(octets.roleid != role.roleId)
					continue;
				role.setRoleBase(octets);
				
			} catch (MarshalException e)
			{
				e.printStackTrace();
			}
		}
		
		/*TimerRunning tr = TimerRunning.fromProtocol( this);
		if( null != tr)
			tr.processSAddUserScreen( this);*/
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790437;

	public int getType() {
		return 790437;
	}

	public java.util.LinkedList<fire.pb.move.RoleBasic> rolelist; // 角色
	public java.util.LinkedList<fire.pb.move.NpcBasic> npclist; // NPC

	public SAddUserScreen() {
		rolelist = new java.util.LinkedList<fire.pb.move.RoleBasic>();
		npclist = new java.util.LinkedList<fire.pb.move.NpcBasic>();
	}

	public SAddUserScreen(java.util.LinkedList<fire.pb.move.RoleBasic> _rolelist_, java.util.LinkedList<fire.pb.move.NpcBasic> _npclist_) {
		this.rolelist = _rolelist_;
		this.npclist = _npclist_;
	}

	public final boolean _validator_() {
		for (fire.pb.move.RoleBasic _v_ : rolelist)
			if (!_v_._validator_()) return false;
		for (fire.pb.move.NpcBasic _v_ : npclist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(rolelist.size());
		for (fire.pb.move.RoleBasic _v_ : rolelist) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(npclist.size());
		for (fire.pb.move.NpcBasic _v_ : npclist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.move.RoleBasic _v_ = new fire.pb.move.RoleBasic();
			_v_.unmarshal(_os_);
			rolelist.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.move.NpcBasic _v_ = new fire.pb.move.NpcBasic();
			_v_.unmarshal(_os_);
			npclist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SAddUserScreen) {
			SAddUserScreen _o_ = (SAddUserScreen)_o1_;
			if (!rolelist.equals(_o_.rolelist)) return false;
			if (!npclist.equals(_o_.npclist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolelist.hashCode();
		_h_ += npclist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolelist).append(",");
		_sb_.append(npclist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

