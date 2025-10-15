
package fire.pb.fushi;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReqFushiInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReqFushiInfo extends __SReqFushiInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812491;

	public int getType() {
		return 812491;
	}

	public int balance; // 游戏币个数，包含赠送游戏币
	public int genbalance; // 赠送游戏币个数
	public int firstsave; // 是否满足首次充值，1满足，0不满足
	public int saveamt; // 累计充值金额

	public SReqFushiInfo() {
	}

	public SReqFushiInfo(int _balance_, int _genbalance_, int _firstsave_, int _saveamt_) {
		this.balance = _balance_;
		this.genbalance = _genbalance_;
		this.firstsave = _firstsave_;
		this.saveamt = _saveamt_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(balance);
		_os_.marshal(genbalance);
		_os_.marshal(firstsave);
		_os_.marshal(saveamt);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		balance = _os_.unmarshal_int();
		genbalance = _os_.unmarshal_int();
		firstsave = _os_.unmarshal_int();
		saveamt = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReqFushiInfo) {
			SReqFushiInfo _o_ = (SReqFushiInfo)_o1_;
			if (balance != _o_.balance) return false;
			if (genbalance != _o_.genbalance) return false;
			if (firstsave != _o_.firstsave) return false;
			if (saveamt != _o_.saveamt) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += balance;
		_h_ += genbalance;
		_h_ += firstsave;
		_h_ += saveamt;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(balance).append(",");
		_sb_.append(genbalance).append(",");
		_sb_.append(firstsave).append(",");
		_sb_.append(saveamt).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SReqFushiInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = balance - _o_.balance;
		if (0 != _c_) return _c_;
		_c_ = genbalance - _o_.genbalance;
		if (0 != _c_) return _c_;
		_c_ = firstsave - _o_.firstsave;
		if (0 != _c_) return _c_;
		_c_ = saveamt - _o_.saveamt;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

