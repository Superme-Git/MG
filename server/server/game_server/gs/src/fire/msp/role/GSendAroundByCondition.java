
package fire.msp.role;
import gnet.link.Onlines;

import java.util.Iterator;

import fire.pb.scene.manager.RoleManager;
import fire.pb.scene.movable.Role;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSendAroundByCondition__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSendAroundByCondition extends __GSendAroundByCondition__ {
	@Override
	protected void process() {
		// protocol handle
		Role r = RoleManager.getInstance().getRoleByID(roleid);
		if(null == r) return;
		if(null == r.getScene()) return;
		if (!exceptstates.isEmpty()){
			java.util.Map<Long, Role> all = r.getAroundRoles();
			if (!all.isEmpty()){
				Iterator<Long> iter = all.keySet().iterator();
				while (iter.hasNext()){
					Long roleid = iter.next();
					Role role = all.get(roleid);
					for (int state : exceptstates){
						if (role.checkSceneState(state)){
							iter.remove();
							break;
						}
					}
				}
				if (!all.isEmpty()){
					Onlines.getInstance().sendOctets(all.keySet(), protype, protocol);
				}
			}
		}
		else{
			r.sendOctetsAround(protype, protocol);
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730910;

	public int getType() {
		return 730910;
	}

	public long roleid; // roleid
	public java.util.ArrayList<Integer> exceptstates; // 九屏内除了此状态的玩家.
	public int protype; // 协议的类型
	public com.locojoy.base.Octets protocol; // 协议marshal完的数据

	public GSendAroundByCondition() {
		exceptstates = new java.util.ArrayList<Integer>();
		protocol = new com.locojoy.base.Octets();
	}

	public GSendAroundByCondition(long _roleid_, java.util.ArrayList<Integer> _exceptstates_, int _protype_, com.locojoy.base.Octets _protocol_) {
		this.roleid = _roleid_;
		this.exceptstates = _exceptstates_;
		this.protype = _protype_;
		this.protocol = _protocol_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.compact_uint32(exceptstates.size());
		for (Integer _v_ : exceptstates) {
			_os_.marshal(_v_);
		}
		_os_.marshal(protype);
		_os_.marshal(protocol);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			exceptstates.add(_v_);
		}
		protype = _os_.unmarshal_int();
		protocol = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSendAroundByCondition) {
			GSendAroundByCondition _o_ = (GSendAroundByCondition)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!exceptstates.equals(_o_.exceptstates)) return false;
			if (protype != _o_.protype) return false;
			if (!protocol.equals(_o_.protocol)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += exceptstates.hashCode();
		_h_ += protype;
		_h_ += protocol.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(exceptstates).append(",");
		_sb_.append(protype).append(",");
		_sb_.append("B").append(protocol.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

