
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __COneKeyMoveTempToBag__ extends mkio.Protocol { }

/** 一键把物品从临时背包移动到背包
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class COneKeyMoveTempToBag extends __COneKeyMoveTempToBag__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787760;

	public int getType() {
		return 787760;
	}

	public int srckey; // 物品key,-1表示所有物品
	public int number; // 数量,-1表示全部
	public int dstpos; // 目标位置,-1,自动选择
	public long npcid;

	public COneKeyMoveTempToBag() {
		npcid = -1;
	}

	public COneKeyMoveTempToBag(int _srckey_, int _number_, int _dstpos_, long _npcid_) {
		this.srckey = _srckey_;
		this.number = _number_;
		this.dstpos = _dstpos_;
		this.npcid = _npcid_;
	}

	public final boolean _validator_() {
		if (srckey < 1) return false;
		if (number < -1) return false;
		if (dstpos < -1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(srckey);
		_os_.marshal(number);
		_os_.marshal(dstpos);
		_os_.marshal(npcid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		srckey = _os_.unmarshal_int();
		number = _os_.unmarshal_int();
		dstpos = _os_.unmarshal_int();
		npcid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof COneKeyMoveTempToBag) {
			COneKeyMoveTempToBag _o_ = (COneKeyMoveTempToBag)_o1_;
			if (srckey != _o_.srckey) return false;
			if (number != _o_.number) return false;
			if (dstpos != _o_.dstpos) return false;
			if (npcid != _o_.npcid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += srckey;
		_h_ += number;
		_h_ += dstpos;
		_h_ += (int)npcid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(srckey).append(",");
		_sb_.append(number).append(",");
		_sb_.append(dstpos).append(",");
		_sb_.append(npcid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(COneKeyMoveTempToBag _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = srckey - _o_.srckey;
		if (0 != _c_) return _c_;
		_c_ = number - _o_.number;
		if (0 != _c_) return _c_;
		_c_ = dstpos - _o_.dstpos;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npcid - _o_.npcid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

