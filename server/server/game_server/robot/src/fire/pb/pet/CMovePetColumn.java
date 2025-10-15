
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CMovePetColumn__ extends mkio.Protocol { }

/** 客户端请求，在宠物栏(角色宠物栏，仓库宠物栏)之间移动宠物,即存取宠物 功能
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CMovePetColumn extends __CMovePetColumn__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788448;

	public int getType() {
		return 788448;
	}

	public int srccolumnid; // 源宠物栏
	public int petkey; // 存或取的宠物key
	public int dstcolumnid; // 目的宠物栏
	public long npckey; // 仓库npckey

	public CMovePetColumn() {
	}

	public CMovePetColumn(int _srccolumnid_, int _petkey_, int _dstcolumnid_, long _npckey_) {
		this.srccolumnid = _srccolumnid_;
		this.petkey = _petkey_;
		this.dstcolumnid = _dstcolumnid_;
		this.npckey = _npckey_;
	}

	public final boolean _validator_() {
		if (srccolumnid < 1 || srccolumnid > 2) return false;
		if (petkey < 1) return false;
		if (dstcolumnid < 1 || dstcolumnid > 2) return false;
		if (npckey < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(srccolumnid);
		_os_.marshal(petkey);
		_os_.marshal(dstcolumnid);
		_os_.marshal(npckey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		srccolumnid = _os_.unmarshal_int();
		petkey = _os_.unmarshal_int();
		dstcolumnid = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CMovePetColumn) {
			CMovePetColumn _o_ = (CMovePetColumn)_o1_;
			if (srccolumnid != _o_.srccolumnid) return false;
			if (petkey != _o_.petkey) return false;
			if (dstcolumnid != _o_.dstcolumnid) return false;
			if (npckey != _o_.npckey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += srccolumnid;
		_h_ += petkey;
		_h_ += dstcolumnid;
		_h_ += (int)npckey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(srccolumnid).append(",");
		_sb_.append(petkey).append(",");
		_sb_.append(dstcolumnid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CMovePetColumn _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = srccolumnid - _o_.srccolumnid;
		if (0 != _c_) return _c_;
		_c_ = petkey - _o_.petkey;
		if (0 != _c_) return _c_;
		_c_ = dstcolumnid - _o_.dstcolumnid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

