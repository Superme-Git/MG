
package cross;
import fire.pb.cross.DeliverProtocolProcesser;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CommonDataTransfer__ extends mkio.Protocol { }

/** 与user无关的信息传递
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CommonDataTransfer extends __CommonDataTransfer__ {
	@Override
	protected void process() {
		// protocol handle
		DeliverProtocolProcesser.process(dtype, content);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 231;

	public int getType() {
		return 231;
	}

	public final static int TO_CROSS = 1;
	public final static int FROM_CROSS = 2;

	public int zoneid; // 跨服gs原值返回
	public int dtype;
	public byte flag;
	public com.locojoy.base.Octets content;

	public CommonDataTransfer() {
		content = new com.locojoy.base.Octets();
	}

	public CommonDataTransfer(int _zoneid_, int _dtype_, byte _flag_, com.locojoy.base.Octets _content_) {
		this.zoneid = _zoneid_;
		this.dtype = _dtype_;
		this.flag = _flag_;
		this.content = _content_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(zoneid);
		_os_.marshal(dtype);
		_os_.marshal(flag);
		_os_.marshal(content);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		zoneid = _os_.unmarshal_int();
		dtype = _os_.unmarshal_int();
		flag = _os_.unmarshal_byte();
		content = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CommonDataTransfer) {
			CommonDataTransfer _o_ = (CommonDataTransfer)_o1_;
			if (zoneid != _o_.zoneid) return false;
			if (dtype != _o_.dtype) return false;
			if (flag != _o_.flag) return false;
			if (!content.equals(_o_.content)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += zoneid;
		_h_ += dtype;
		_h_ += (int)flag;
		_h_ += content.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(zoneid).append(",");
		_sb_.append(dtype).append(",");
		_sb_.append(flag).append(",");
		_sb_.append("B").append(content.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

