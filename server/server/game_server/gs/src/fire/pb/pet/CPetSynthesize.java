
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPetSynthesize__ extends mkio.Protocol { }

/** 客户端请求宠物合成
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPetSynthesize extends __CPetSynthesize__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;

		PPetSynthesizeProc proc = new PPetSynthesizeProc(roleid, petkey1, petkey2);
		proc.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788517;

	public int getType() {
		return 788517;
	}

	public int petkey1; // 宠物key
	public int petkey2; // 宠物key

	public CPetSynthesize() {
	}

	public CPetSynthesize(int _petkey1_, int _petkey2_) {
		this.petkey1 = _petkey1_;
		this.petkey2 = _petkey2_;
	}

	public final boolean _validator_() {
		if (petkey1 < 1) return false;
		if (petkey2 < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petkey1);
		_os_.marshal(petkey2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey1 = _os_.unmarshal_int();
		petkey2 = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPetSynthesize) {
			CPetSynthesize _o_ = (CPetSynthesize)_o1_;
			if (petkey1 != _o_.petkey1) return false;
			if (petkey2 != _o_.petkey2) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey1;
		_h_ += petkey2;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey1).append(",");
		_sb_.append(petkey2).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPetSynthesize _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petkey1 - _o_.petkey1;
		if (0 != _c_) return _c_;
		_c_ = petkey2 - _o_.petkey2;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

