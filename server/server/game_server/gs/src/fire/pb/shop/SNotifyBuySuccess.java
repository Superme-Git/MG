
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SNotifyBuySuccess__ extends mkio.Protocol { }

/** 用于客户端处理购买成功时通知
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SNotifyBuySuccess extends __SNotifyBuySuccess__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810657;

	public int getType() {
		return 810657;
	}

	public int notifytype; // 参考ShopBuyType
	public java.lang.String name; // 名称
	public int number; // 数量
	public int money; // 金额
	public int currency; // 参考MoneyType
	public int itemorpet; // 1道具 2宠物
	public java.lang.String units; // 单位

	public SNotifyBuySuccess() {
		name = "";
		units = "";
	}

	public SNotifyBuySuccess(int _notifytype_, java.lang.String _name_, int _number_, int _money_, int _currency_, int _itemorpet_, java.lang.String _units_) {
		this.notifytype = _notifytype_;
		this.name = _name_;
		this.number = _number_;
		this.money = _money_;
		this.currency = _currency_;
		this.itemorpet = _itemorpet_;
		this.units = _units_;
	}

	public final boolean _validator_() {
		if (notifytype < 1 || notifytype > 6) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(notifytype);
		_os_.marshal(name, "UTF-16LE");
		_os_.marshal(number);
		_os_.marshal(money);
		_os_.marshal(currency);
		_os_.marshal(itemorpet);
		_os_.marshal(units, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		notifytype = _os_.unmarshal_int();
		name = _os_.unmarshal_String("UTF-16LE");
		number = _os_.unmarshal_int();
		money = _os_.unmarshal_int();
		currency = _os_.unmarshal_int();
		itemorpet = _os_.unmarshal_int();
		units = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SNotifyBuySuccess) {
			SNotifyBuySuccess _o_ = (SNotifyBuySuccess)_o1_;
			if (notifytype != _o_.notifytype) return false;
			if (!name.equals(_o_.name)) return false;
			if (number != _o_.number) return false;
			if (money != _o_.money) return false;
			if (currency != _o_.currency) return false;
			if (itemorpet != _o_.itemorpet) return false;
			if (!units.equals(_o_.units)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += notifytype;
		_h_ += name.hashCode();
		_h_ += number;
		_h_ += money;
		_h_ += currency;
		_h_ += itemorpet;
		_h_ += units.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(notifytype).append(",");
		_sb_.append("T").append(name.length()).append(",");
		_sb_.append(number).append(",");
		_sb_.append(money).append(",");
		_sb_.append(currency).append(",");
		_sb_.append(itemorpet).append(",");
		_sb_.append("T").append(units.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

