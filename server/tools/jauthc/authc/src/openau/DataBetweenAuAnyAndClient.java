
package openau;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __DataBetweenAuAnyAndClient__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class DataBetweenAuAnyAndClient extends __DataBetweenAuAnyAndClient__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 8921;

	public int getType() {
		return 8921;
	}

	public final static int AuAny_TO_CLIENT = 1;
	public final static int CLIENT_TO_AuAny = 2;

	public int userid;
	public int qtype;
	public byte flag;
	public com.locojoy.base.Octets info;
	public int reserved;

	public DataBetweenAuAnyAndClient() {
		info = new com.locojoy.base.Octets();
	}

	public DataBetweenAuAnyAndClient(int _userid_, int _qtype_, byte _flag_, com.locojoy.base.Octets _info_, int _reserved_) {
		this.userid = _userid_;
		this.qtype = _qtype_;
		this.flag = _flag_;
		this.info = _info_;
		this.reserved = _reserved_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(userid);
		_os_.marshal(qtype);
		_os_.marshal(flag);
		_os_.marshal(info);
		_os_.marshal(reserved);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		userid = _os_.unmarshal_int();
		qtype = _os_.unmarshal_int();
		flag = _os_.unmarshal_byte();
		info = _os_.unmarshal_Octets();
		reserved = _os_.unmarshal_int();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof DataBetweenAuAnyAndClient) {
			DataBetweenAuAnyAndClient _o_ = (DataBetweenAuAnyAndClient)_o1_;
			if (userid != _o_.userid) return false;
			if (qtype != _o_.qtype) return false;
			if (flag != _o_.flag) return false;
			if (!info.equals(_o_.info)) return false;
			if (reserved != _o_.reserved) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += userid;
		_h_ += qtype;
		_h_ += (int)flag;
		_h_ += info.hashCode();
		_h_ += reserved;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(userid).append(",");
		_sb_.append(qtype).append(",");
		_sb_.append(flag).append(",");
		_sb_.append("B").append(info.size()).append(",");
		_sb_.append(reserved).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

