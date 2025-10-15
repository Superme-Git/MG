
package fire.pb.clan;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class ClanEventInfo implements Marshal {
	public java.lang.String eventtime; // 时间
	public java.lang.String eventinfo; // 内容
	public int eventtype; // 类型
	public long eventvalue; // 值

	public ClanEventInfo() {
		eventtime = "";
		eventinfo = "";
	}

	public ClanEventInfo(java.lang.String _eventtime_, java.lang.String _eventinfo_, int _eventtype_, long _eventvalue_) {
		this.eventtime = _eventtime_;
		this.eventinfo = _eventinfo_;
		this.eventtype = _eventtype_;
		this.eventvalue = _eventvalue_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(eventtime, "UTF-16LE");
		_os_.marshal(eventinfo, "UTF-16LE");
		_os_.marshal(eventtype);
		_os_.marshal(eventvalue);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		eventtime = _os_.unmarshal_String("UTF-16LE");
		eventinfo = _os_.unmarshal_String("UTF-16LE");
		eventtype = _os_.unmarshal_int();
		eventvalue = _os_.unmarshal_long();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof ClanEventInfo) {
			ClanEventInfo _o_ = (ClanEventInfo)_o1_;
			if (!eventtime.equals(_o_.eventtime)) return false;
			if (!eventinfo.equals(_o_.eventinfo)) return false;
			if (eventtype != _o_.eventtype) return false;
			if (eventvalue != _o_.eventvalue) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += eventtime.hashCode();
		_h_ += eventinfo.hashCode();
		_h_ += eventtype;
		_h_ += (int)eventvalue;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(eventtime.length()).append(",");
		_sb_.append("T").append(eventinfo.length()).append(",");
		_sb_.append(eventtype).append(",");
		_sb_.append(eventvalue).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

