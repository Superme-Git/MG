
package fire.pb.talk;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SExpMessageTips__ extends mkio.Protocol { }

/** 这个是用来提示有多种加经验的情况
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SExpMessageTips extends __SExpMessageTips__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 792447;

	public int getType() {
		return 792447;
	}

	public int messageid; // 提示id
	public long expvalue; // 总的经验值
	public java.util.HashMap<Integer,Long> messageinfo; // key 为子提示id, value为子提示的参数值

	public SExpMessageTips() {
		messageinfo = new java.util.HashMap<Integer,Long>();
	}

	public SExpMessageTips(int _messageid_, long _expvalue_, java.util.HashMap<Integer,Long> _messageinfo_) {
		this.messageid = _messageid_;
		this.expvalue = _expvalue_;
		this.messageinfo = _messageinfo_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(messageid);
		_os_.marshal(expvalue);
		_os_.compact_uint32(messageinfo.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : messageinfo.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		messageid = _os_.unmarshal_int();
		expvalue = _os_.unmarshal_long();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			long _v_;
			_v_ = _os_.unmarshal_long();
			messageinfo.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SExpMessageTips) {
			SExpMessageTips _o_ = (SExpMessageTips)_o1_;
			if (messageid != _o_.messageid) return false;
			if (expvalue != _o_.expvalue) return false;
			if (!messageinfo.equals(_o_.messageinfo)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += messageid;
		_h_ += (int)expvalue;
		_h_ += messageinfo.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(messageid).append(",");
		_sb_.append(expvalue).append(",");
		_sb_.append(messageinfo).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

