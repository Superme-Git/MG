
package fire.pb.product;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CMakeEquip__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CMakeEquip extends __CMakeEquip__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid=gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleid<0) return;
		
		final PMakeEquip pmakeEquip = new PMakeEquip(roleid, equipid, maketype);
		pmakeEquip.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 803451;

	public int getType() {
		return 803451;
	}

	public int equipid; // 装备ID
	public short maketype; // 打造类型 0 普通打造; 1 强化打造

	public CMakeEquip() {
	}

	public CMakeEquip(int _equipid_, short _maketype_) {
		this.equipid = _equipid_;
		this.maketype = _maketype_;
	}

	public final boolean _validator_() {
		if (equipid < 1) return false;
		if (maketype < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(equipid);
		_os_.marshal(maketype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		equipid = _os_.unmarshal_int();
		maketype = _os_.unmarshal_short();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CMakeEquip) {
			CMakeEquip _o_ = (CMakeEquip)_o1_;
			if (equipid != _o_.equipid) return false;
			if (maketype != _o_.maketype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += equipid;
		_h_ += maketype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(equipid).append(",");
		_sb_.append(maketype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CMakeEquip _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = equipid - _o_.equipid;
		if (0 != _c_) return _c_;
		_c_ = maketype - _o_.maketype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

