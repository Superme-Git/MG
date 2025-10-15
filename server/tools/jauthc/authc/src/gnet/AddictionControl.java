
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __AddictionControl__ extends mkio.Protocol { }

/** ∑¿≥¡√‘
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class AddictionControl extends __AddictionControl__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 556;

	public int getType() {
		return 556;
	}

	public int zoneid;
	public int userid;
	public int rate;
	public int msg;
	public java.util.ArrayList<gnet.GPair> data;

	public AddictionControl() {
		userid = 0;
		rate = 0;
		msg = 0;
		data = new java.util.ArrayList<gnet.GPair>();
	}

	public AddictionControl(int _zoneid_, int _userid_, int _rate_, int _msg_, java.util.ArrayList<gnet.GPair> _data_) {
		this.zoneid = _zoneid_;
		this.userid = _userid_;
		this.rate = _rate_;
		this.msg = _msg_;
		this.data = _data_;
	}

	public final boolean _validator_() {
		for (gnet.GPair _v_ : data)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(zoneid);
		_os_.marshal(userid);
		_os_.marshal(rate);
		_os_.marshal(msg);
		_os_.compact_uint32(data.size());
		for (gnet.GPair _v_ : data) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		zoneid = _os_.unmarshal_int();
		userid = _os_.unmarshal_int();
		rate = _os_.unmarshal_int();
		msg = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			gnet.GPair _v_ = new gnet.GPair();
			_v_.unmarshal(_os_);
			data.add(_v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof AddictionControl) {
			AddictionControl _o_ = (AddictionControl)_o1_;
			if (zoneid != _o_.zoneid) return false;
			if (userid != _o_.userid) return false;
			if (rate != _o_.rate) return false;
			if (msg != _o_.msg) return false;
			if (!data.equals(_o_.data)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += zoneid;
		_h_ += userid;
		_h_ += rate;
		_h_ += msg;
		_h_ += data.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(zoneid).append(",");
		_sb_.append(userid).append(",");
		_sb_.append(rate).append(",");
		_sb_.append(msg).append(",");
		_sb_.append(data).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

