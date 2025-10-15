
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReplaceGemFromEquip__ extends mkio.Protocol { }

/** 客户端申请替换宝石
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReplaceGemFromEquip extends __CReplaceGemFromEquip__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787759;

	public int getType() {
		return 787759;
	}

	public int equipitemkey; // 装备的keyinpack
	public byte equipbag; // 为0为背包栏，为1为装备栏
	public int gemindex; // 宝石槽索引
	public int gemitemkey; // 宝石的keyinpack,成功后扣除指定的这颗宝石

	public CReplaceGemFromEquip() {
	}

	public CReplaceGemFromEquip(int _equipitemkey_, byte _equipbag_, int _gemindex_, int _gemitemkey_) {
		this.equipitemkey = _equipitemkey_;
		this.equipbag = _equipbag_;
		this.gemindex = _gemindex_;
		this.gemitemkey = _gemitemkey_;
	}

	public final boolean _validator_() {
		if (equipitemkey < 1) return false;
		if (gemindex < 0) return false;
		if (gemitemkey < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(equipitemkey);
		_os_.marshal(equipbag);
		_os_.marshal(gemindex);
		_os_.marshal(gemitemkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		equipitemkey = _os_.unmarshal_int();
		equipbag = _os_.unmarshal_byte();
		gemindex = _os_.unmarshal_int();
		gemitemkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReplaceGemFromEquip) {
			CReplaceGemFromEquip _o_ = (CReplaceGemFromEquip)_o1_;
			if (equipitemkey != _o_.equipitemkey) return false;
			if (equipbag != _o_.equipbag) return false;
			if (gemindex != _o_.gemindex) return false;
			if (gemitemkey != _o_.gemitemkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += equipitemkey;
		_h_ += (int)equipbag;
		_h_ += gemindex;
		_h_ += gemitemkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(equipitemkey).append(",");
		_sb_.append(equipbag).append(",");
		_sb_.append(gemindex).append(",");
		_sb_.append(gemitemkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CReplaceGemFromEquip _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = equipitemkey - _o_.equipitemkey;
		if (0 != _c_) return _c_;
		_c_ = equipbag - _o_.equipbag;
		if (0 != _c_) return _c_;
		_c_ = gemindex - _o_.gemindex;
		if (0 != _c_) return _c_;
		_c_ = gemitemkey - _o_.gemitemkey;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

