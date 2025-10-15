
package fire.pb.talk;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __STransChatMessageNotify2Client__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class STransChatMessageNotify2Client extends __STransChatMessageNotify2Client__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 792437;

	public int getType() {
		return 792437;
	}

	public int messageid; // 提示id
	public int npcbaseid; // npc id
	public java.util.ArrayList<com.locojoy.base.Octets> parameters; // 参数

	public STransChatMessageNotify2Client() {
		parameters = new java.util.ArrayList<com.locojoy.base.Octets>();
	}

	public STransChatMessageNotify2Client(int _messageid_, int _npcbaseid_, java.util.ArrayList<com.locojoy.base.Octets> _parameters_) {
		this.messageid = _messageid_;
		this.npcbaseid = _npcbaseid_;
		this.parameters = _parameters_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(messageid);
		_os_.marshal(npcbaseid);
		_os_.compact_uint32(parameters.size());
		for (com.locojoy.base.Octets _v_ : parameters) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		messageid = _os_.unmarshal_int();
		npcbaseid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			com.locojoy.base.Octets _v_;
			_v_ = _os_.unmarshal_Octets();
			parameters.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof STransChatMessageNotify2Client) {
			STransChatMessageNotify2Client _o_ = (STransChatMessageNotify2Client)_o1_;
			if (messageid != _o_.messageid) return false;
			if (npcbaseid != _o_.npcbaseid) return false;
			if (!parameters.equals(_o_.parameters)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += messageid;
		_h_ += npcbaseid;
		_h_ += parameters.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(messageid).append(",");
		_sb_.append(npcbaseid).append(",");
		_sb_.append(parameters).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

