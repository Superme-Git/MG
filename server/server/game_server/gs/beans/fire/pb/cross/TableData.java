
package fire.pb.cross;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class TableData implements Marshal {
	public java.lang.String tablename;
	public com.locojoy.base.Octets valuedata;
	public com.locojoy.base.Octets keydata;

	public TableData() {
		tablename = "";
		valuedata = new com.locojoy.base.Octets();
		keydata = new com.locojoy.base.Octets();
	}

	public TableData(java.lang.String _tablename_, com.locojoy.base.Octets _valuedata_, com.locojoy.base.Octets _keydata_) {
		this.tablename = _tablename_;
		this.valuedata = _valuedata_;
		this.keydata = _keydata_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(tablename, "UTF-16LE");
		_os_.marshal(valuedata);
		_os_.marshal(keydata);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		tablename = _os_.unmarshal_String("UTF-16LE");
		valuedata = _os_.unmarshal_Octets();
		keydata = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof TableData) {
			TableData _o_ = (TableData)_o1_;
			if (!tablename.equals(_o_.tablename)) return false;
			if (!valuedata.equals(_o_.valuedata)) return false;
			if (!keydata.equals(_o_.keydata)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += tablename.hashCode();
		_h_ += valuedata.hashCode();
		_h_ += keydata.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(tablename.length()).append(",");
		_sb_.append("B").append(valuedata.size()).append(",");
		_sb_.append("B").append(keydata.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

