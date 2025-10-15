
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CGetPetcolumnInfo__ extends mkio.Protocol { }

/** 客户端请求宠物栏信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CGetPetcolumnInfo extends __CGetPetcolumnInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788446;

	public int getType() {
		return 788446;
	}

	public int columnid; // 宠物栏id
	public long npckey; // 仓库老板的npckey.

	public CGetPetcolumnInfo() {
	}

	public CGetPetcolumnInfo(int _columnid_, long _npckey_) {
		this.columnid = _columnid_;
		this.npckey = _npckey_;
	}

	public final boolean _validator_() {
		if (columnid < 1 || columnid > 2) return false;
		if (npckey < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(columnid);
		_os_.marshal(npckey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		columnid = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CGetPetcolumnInfo) {
			CGetPetcolumnInfo _o_ = (CGetPetcolumnInfo)_o1_;
			if (columnid != _o_.columnid) return false;
			if (npckey != _o_.npckey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += columnid;
		_h_ += (int)npckey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(columnid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CGetPetcolumnInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = columnid - _o_.columnid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

