
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __STakeBackTempMarketContainerItem__ extends mkio.Protocol { }

/** 拍卖临时背包,合服后取出道具回复
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class STakeBackTempMarketContainerItem extends __STakeBackTempMarketContainerItem__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810669;

	public int getType() {
		return 810669;
	}

	public int succ; // 0失败，1成功

	public STakeBackTempMarketContainerItem() {
	}

	public STakeBackTempMarketContainerItem(int _succ_) {
		this.succ = _succ_;
	}

	public final boolean _validator_() {
		if (succ < 0 || succ > 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(succ);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		succ = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof STakeBackTempMarketContainerItem) {
			STakeBackTempMarketContainerItem _o_ = (STakeBackTempMarketContainerItem)_o1_;
			if (succ != _o_.succ) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += succ;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(succ).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(STakeBackTempMarketContainerItem _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = succ - _o_.succ;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

