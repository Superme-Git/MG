
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqChargeMoney__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqChargeMoney extends __CReqChargeMoney__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786512;

	public int getType() {
		return 786512;
	}

	public int num; // 充值数目,不同平台单位可能不一样.对于91来说,是元
	public java.lang.String ordersnplatform; // 平台的唯一字符串序列号

	public CReqChargeMoney() {
		ordersnplatform = "";
	}

	public CReqChargeMoney(int _num_, java.lang.String _ordersnplatform_) {
		this.num = _num_;
		this.ordersnplatform = _ordersnplatform_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(num);
		_os_.marshal(ordersnplatform, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		num = _os_.unmarshal_int();
		ordersnplatform = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReqChargeMoney) {
			CReqChargeMoney _o_ = (CReqChargeMoney)_o1_;
			if (num != _o_.num) return false;
			if (!ordersnplatform.equals(_o_.ordersnplatform)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += num;
		_h_ += ordersnplatform.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(num).append(",");
		_sb_.append("T").append(ordersnplatform.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

