
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReplaceGem__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReplaceGem extends __CReplaceGem__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787766;

	public int getType() {
		return 787766;
	}

	public int ret; // 0 表示不替换;1 表示替换
	public int srckey; // 在背包栏里源装备的key
	public int deskey; // 在装备栏里目标装备的key

	public CReplaceGem() {
	}

	public CReplaceGem(int _ret_, int _srckey_, int _deskey_) {
		this.ret = _ret_;
		this.srckey = _srckey_;
		this.deskey = _deskey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(ret);
		_os_.marshal(srckey);
		_os_.marshal(deskey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		ret = _os_.unmarshal_int();
		srckey = _os_.unmarshal_int();
		deskey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReplaceGem) {
			CReplaceGem _o_ = (CReplaceGem)_o1_;
			if (ret != _o_.ret) return false;
			if (srckey != _o_.srckey) return false;
			if (deskey != _o_.deskey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += ret;
		_h_ += srckey;
		_h_ += deskey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(ret).append(",");
		_sb_.append(srckey).append(",");
		_sb_.append(deskey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CReplaceGem _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = ret - _o_.ret;
		if (0 != _c_) return _c_;
		_c_ = srckey - _o_.srckey;
		if (0 != _c_) return _c_;
		_c_ = deskey - _o_.deskey;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

