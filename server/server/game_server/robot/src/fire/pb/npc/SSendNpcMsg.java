
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendNpcMsg__ extends mkio.Protocol { }

/** 服务器发送给客户端，npc提示信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendNpcMsg extends __SSendNpcMsg__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795454;

	public int getType() {
		return 795454;
	}

	public long npckey;
	public int npcid;
	public int msgid;
	public java.util.ArrayList<Long> args;

	public SSendNpcMsg() {
		args = new java.util.ArrayList<Long>();
	}

	public SSendNpcMsg(long _npckey_, int _npcid_, int _msgid_, java.util.ArrayList<Long> _args_) {
		this.npckey = _npckey_;
		this.npcid = _npcid_;
		this.msgid = _msgid_;
		this.args = _args_;
	}

	public final boolean _validator_() {
		if (npckey <= 0) return false;
		if (npcid <= 0) return false;
		if (msgid <= 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(npcid);
		_os_.marshal(msgid);
		_os_.compact_uint32(args.size());
		for (Long _v_ : args) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		npcid = _os_.unmarshal_int();
		msgid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			args.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendNpcMsg) {
			SSendNpcMsg _o_ = (SSendNpcMsg)_o1_;
			if (npckey != _o_.npckey) return false;
			if (npcid != _o_.npcid) return false;
			if (msgid != _o_.msgid) return false;
			if (!args.equals(_o_.args)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += npcid;
		_h_ += msgid;
		_h_ += args.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(npcid).append(",");
		_sb_.append(msgid).append(",");
		_sb_.append(args).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

