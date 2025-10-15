
package fire.pb.fushi.spotcheck;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RoleTradingRecord implements Marshal {
	public java.lang.String tradingid; // 挂单号
	public int tradingtype; // 交易类型  0买入  1出售
	public int num; // 数量
	public int price; // 价格
	public long tradingtime; // 交易时间

	public RoleTradingRecord() {
		tradingid = "";
	}

	public RoleTradingRecord(java.lang.String _tradingid_, int _tradingtype_, int _num_, int _price_, long _tradingtime_) {
		this.tradingid = _tradingid_;
		this.tradingtype = _tradingtype_;
		this.num = _num_;
		this.price = _price_;
		this.tradingtime = _tradingtime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(tradingid, "UTF-16LE");
		_os_.marshal(tradingtype);
		_os_.marshal(num);
		_os_.marshal(price);
		_os_.marshal(tradingtime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		tradingid = _os_.unmarshal_String("UTF-16LE");
		tradingtype = _os_.unmarshal_int();
		num = _os_.unmarshal_int();
		price = _os_.unmarshal_int();
		tradingtime = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleTradingRecord) {
			RoleTradingRecord _o_ = (RoleTradingRecord)_o1_;
			if (!tradingid.equals(_o_.tradingid)) return false;
			if (tradingtype != _o_.tradingtype) return false;
			if (num != _o_.num) return false;
			if (price != _o_.price) return false;
			if (tradingtime != _o_.tradingtime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += tradingid.hashCode();
		_h_ += tradingtype;
		_h_ += num;
		_h_ += price;
		_h_ += (int)tradingtime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(tradingid.length()).append(",");
		_sb_.append(tradingtype).append(",");
		_sb_.append(num).append(",");
		_sb_.append(price).append(",");
		_sb_.append(tradingtime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

