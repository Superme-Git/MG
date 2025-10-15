
package fire.pb.cross;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SBeginCorssServer__ extends mkio.Protocol { }

/** 服务器和客户端之间的协议 32683 到 32765  start
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SBeginCorssServer extends __SBeginCorssServer__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 819115;

	public int getType() {
		return 819115;
	}

	public java.lang.String account; // 帐号
	public java.lang.String ticket; // 门票
	public java.lang.String crossip;
	public int crossport;
	public int crossnum;

	public SBeginCorssServer() {
		account = "";
		ticket = "";
		crossip = "";
	}

	public SBeginCorssServer(java.lang.String _account_, java.lang.String _ticket_, java.lang.String _crossip_, int _crossport_, int _crossnum_) {
		this.account = _account_;
		this.ticket = _ticket_;
		this.crossip = _crossip_;
		this.crossport = _crossport_;
		this.crossnum = _crossnum_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(account, "UTF-16LE");
		_os_.marshal(ticket, "UTF-16LE");
		_os_.marshal(crossip, "UTF-16LE");
		_os_.marshal(crossport);
		_os_.marshal(crossnum);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		account = _os_.unmarshal_String("UTF-16LE");
		ticket = _os_.unmarshal_String("UTF-16LE");
		crossip = _os_.unmarshal_String("UTF-16LE");
		crossport = _os_.unmarshal_int();
		crossnum = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SBeginCorssServer) {
			SBeginCorssServer _o_ = (SBeginCorssServer)_o1_;
			if (!account.equals(_o_.account)) return false;
			if (!ticket.equals(_o_.ticket)) return false;
			if (!crossip.equals(_o_.crossip)) return false;
			if (crossport != _o_.crossport) return false;
			if (crossnum != _o_.crossnum) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += account.hashCode();
		_h_ += ticket.hashCode();
		_h_ += crossip.hashCode();
		_h_ += crossport;
		_h_ += crossnum;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(account.length()).append(",");
		_sb_.append("T").append(ticket.length()).append(",");
		_sb_.append("T").append(crossip.length()).append(",");
		_sb_.append(crossport).append(",");
		_sb_.append(crossnum).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

