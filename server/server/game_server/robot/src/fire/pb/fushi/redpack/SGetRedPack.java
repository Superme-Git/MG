
package fire.pb.fushi.redpack;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SGetRedPack__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SGetRedPack extends __SGetRedPack__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812537;

	public int getType() {
		return 812537;
	}

	public int modeltype; // 红包类型
	public java.lang.String redpackid; // 红包Id
	public int state; // 红包状态
	public int successflag; // 领取成功标志  0失败    1成功
	public int fushinum; // 符石数量

	public SGetRedPack() {
		redpackid = "";
	}

	public SGetRedPack(int _modeltype_, java.lang.String _redpackid_, int _state_, int _successflag_, int _fushinum_) {
		this.modeltype = _modeltype_;
		this.redpackid = _redpackid_;
		this.state = _state_;
		this.successflag = _successflag_;
		this.fushinum = _fushinum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(modeltype);
		_os_.marshal(redpackid, "UTF-16LE");
		_os_.marshal(state);
		_os_.marshal(successflag);
		_os_.marshal(fushinum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		modeltype = _os_.unmarshal_int();
		redpackid = _os_.unmarshal_String("UTF-16LE");
		state = _os_.unmarshal_int();
		successflag = _os_.unmarshal_int();
		fushinum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SGetRedPack) {
			SGetRedPack _o_ = (SGetRedPack)_o1_;
			if (modeltype != _o_.modeltype) return false;
			if (!redpackid.equals(_o_.redpackid)) return false;
			if (state != _o_.state) return false;
			if (successflag != _o_.successflag) return false;
			if (fushinum != _o_.fushinum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += modeltype;
		_h_ += redpackid.hashCode();
		_h_ += state;
		_h_ += successflag;
		_h_ += fushinum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype).append(",");
		_sb_.append("T").append(redpackid.length()).append(",");
		_sb_.append(state).append(",");
		_sb_.append(successflag).append(",");
		_sb_.append(fushinum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

