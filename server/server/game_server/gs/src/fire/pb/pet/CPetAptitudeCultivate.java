
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPetAptitudeCultivate__ extends mkio.Protocol { }

/** 客户端请求资质培养
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPetAptitudeCultivate extends __CPetAptitudeCultivate__ {
	@Override
	protected void process() {
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0) 
			return;

		PPetAptitudeCultivate proc = new PPetAptitudeCultivate(roleid, petkey, aptid, itemkey);
		proc.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788521;

	public int getType() {
		return 788521;
	}

	public int petkey; // 宠物key
	public int aptid; // 资质id
	public int itemkey; // 物品key

	public CPetAptitudeCultivate() {
	}

	public CPetAptitudeCultivate(int _petkey_, int _aptid_, int _itemkey_) {
		this.petkey = _petkey_;
		this.aptid = _aptid_;
		this.itemkey = _itemkey_;
	}

	public final boolean _validator_() {
		if (petkey < 1) return false;
		if (aptid < 0) return false;
		if (itemkey < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(petkey);
		_os_.marshal(aptid);
		_os_.marshal(itemkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		petkey = _os_.unmarshal_int();
		aptid = _os_.unmarshal_int();
		itemkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CPetAptitudeCultivate) {
			CPetAptitudeCultivate _o_ = (CPetAptitudeCultivate)_o1_;
			if (petkey != _o_.petkey) return false;
			if (aptid != _o_.aptid) return false;
			if (itemkey != _o_.itemkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += petkey;
		_h_ += aptid;
		_h_ += itemkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(petkey).append(",");
		_sb_.append(aptid).append(",");
		_sb_.append(itemkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CPetAptitudeCultivate _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		_c_ = aptid - _o_.aptid;
		if (0 != _c_) return _c_;
		_c_ = itemkey - _o_.itemkey;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

