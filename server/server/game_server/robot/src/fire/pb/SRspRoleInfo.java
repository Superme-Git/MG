
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRspRoleInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRspRoleInfo extends __SRspRoleInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786509;

	public int getType() {
		return 786509;
	}

	public java.util.HashMap<Integer,Long> hpmpstore;
	public int wencaivalue; // 值
	public int wuxun; // 功力值
	public int gongde; // 公德值
	public long honour; // 荣誉值
	public int reqkey; // 请求类型 1表示请求人物信息界面;2 表示战斗结束

	public SRspRoleInfo() {
		hpmpstore = new java.util.HashMap<Integer,Long>();
	}

	public SRspRoleInfo(java.util.HashMap<Integer,Long> _hpmpstore_, int _wencaivalue_, int _wuxun_, int _gongde_, long _honour_, int _reqkey_) {
		this.hpmpstore = _hpmpstore_;
		this.wencaivalue = _wencaivalue_;
		this.wuxun = _wuxun_;
		this.gongde = _gongde_;
		this.honour = _honour_;
		this.reqkey = _reqkey_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(hpmpstore.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : hpmpstore.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		_os_.marshal(wencaivalue);
		_os_.marshal(wuxun);
		_os_.marshal(gongde);
		_os_.marshal(honour);
		_os_.marshal(reqkey);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			long _v_;
			_v_ = _os_.unmarshal_long();
			hpmpstore.put(_k_, _v_);
		}
		wencaivalue = _os_.unmarshal_int();
		wuxun = _os_.unmarshal_int();
		gongde = _os_.unmarshal_int();
		honour = _os_.unmarshal_long();
		reqkey = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRspRoleInfo) {
			SRspRoleInfo _o_ = (SRspRoleInfo)_o1_;
			if (!hpmpstore.equals(_o_.hpmpstore)) return false;
			if (wencaivalue != _o_.wencaivalue) return false;
			if (wuxun != _o_.wuxun) return false;
			if (gongde != _o_.gongde) return false;
			if (honour != _o_.honour) return false;
			if (reqkey != _o_.reqkey) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += hpmpstore.hashCode();
		_h_ += wencaivalue;
		_h_ += wuxun;
		_h_ += gongde;
		_h_ += (int)honour;
		_h_ += reqkey;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(hpmpstore).append(",");
		_sb_.append(wencaivalue).append(",");
		_sb_.append(wuxun).append(",");
		_sb_.append(gongde).append(",");
		_sb_.append(honour).append(",");
		_sb_.append(reqkey).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

