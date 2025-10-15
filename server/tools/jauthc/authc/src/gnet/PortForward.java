
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __PortForward__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class PortForward extends __PortForward__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 109;

	public int getType() {
		return 109;
	}

	public int command; // 命令码。
	public int portsid; // 映射编号，由发起方(eConnect)分配。
	public int code; // 命令参数，具体含义由相应命令决定。
	public com.locojoy.base.Octets data; // 命令参数，具体含义由相应命令决定。

	public PortForward() {
		data = new com.locojoy.base.Octets();
	}

	public PortForward(int _command_, int _portsid_, int _code_, com.locojoy.base.Octets _data_) {
		this.command = _command_;
		this.portsid = _portsid_;
		this.code = _code_;
		this.data = _data_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(command);
		_os_.marshal(portsid);
		_os_.marshal(code);
		_os_.marshal(data);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		command = _os_.unmarshal_int();
		portsid = _os_.unmarshal_int();
		code = _os_.unmarshal_int();
		data = _os_.unmarshal_Octets();
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof PortForward) {
			PortForward _o_ = (PortForward)_o1_;
			if (command != _o_.command) return false;
			if (portsid != _o_.portsid) return false;
			if (code != _o_.code) return false;
			if (!data.equals(_o_.data)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += command;
		_h_ += portsid;
		_h_ += code;
		_h_ += data.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(command).append(",");
		_sb_.append(portsid).append(",");
		_sb_.append(code).append(",");
		_sb_.append("B").append(data.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

