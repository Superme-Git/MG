
package fire.msp;

import java.util.Set;

import gnet.link.Onlines;
import fire.pb.scene.manager.RoleManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSendAllBySchool__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSendAllBySchool extends __GSendAllBySchool__ {
	@Override
	protected void process() {
		// protocol handle
		Set<Long> schoolroles = RoleManager.getInstance().getSchoolRoles(schoolid);
		if (schoolroles!=null&&schoolroles.size()>0) 
		Onlines.getInstance().sendOctets(schoolroles, protype, protocol);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 720901;

	public int getType() {
		return 720901;
	}

	public int protype; // 协议的类型
	public com.locojoy.base.Octets protocol; // 协议marshal完的数据
	public int schoolid; // 门派ID

	public GSendAllBySchool() {
		protocol = new com.locojoy.base.Octets();
	}

	public GSendAllBySchool(int _protype_, com.locojoy.base.Octets _protocol_, int _schoolid_) {
		this.protype = _protype_;
		this.protocol = _protocol_;
		this.schoolid = _schoolid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(protype);
		_os_.marshal(protocol);
		_os_.marshal(schoolid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		protype = _os_.unmarshal_int();
		protocol = _os_.unmarshal_Octets();
		schoolid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSendAllBySchool) {
			GSendAllBySchool _o_ = (GSendAllBySchool)_o1_;
			if (protype != _o_.protype) return false;
			if (!protocol.equals(_o_.protocol)) return false;
			if (schoolid != _o_.schoolid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += protype;
		_h_ += protocol.hashCode();
		_h_ += schoolid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(protype).append(",");
		_sb_.append("B").append(protocol.size()).append(",");
		_sb_.append(schoolid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

