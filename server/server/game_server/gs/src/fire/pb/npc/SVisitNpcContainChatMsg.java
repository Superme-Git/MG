
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SVisitNpcContainChatMsg__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SVisitNpcContainChatMsg extends __SVisitNpcContainChatMsg__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795526;

	public int getType() {
		return 795526;
	}

	public long npckey; // npckey为npc的唯一ID
	public java.util.ArrayList<Integer> services; // services 为npc所提供的服务 IDs.
	public java.util.ArrayList<Integer> scenarioquests; // 剧情任务
	public int msgid; // 消息提示ID
	public java.util.ArrayList<com.locojoy.base.Octets> parameters; // 参数

	public SVisitNpcContainChatMsg() {
		services = new java.util.ArrayList<Integer>();
		scenarioquests = new java.util.ArrayList<Integer>();
		parameters = new java.util.ArrayList<com.locojoy.base.Octets>();
	}

	public SVisitNpcContainChatMsg(long _npckey_, java.util.ArrayList<Integer> _services_, java.util.ArrayList<Integer> _scenarioquests_, int _msgid_, java.util.ArrayList<com.locojoy.base.Octets> _parameters_) {
		this.npckey = _npckey_;
		this.services = _services_;
		this.scenarioquests = _scenarioquests_;
		this.msgid = _msgid_;
		this.parameters = _parameters_;
	}

	public final boolean _validator_() {
		if (npckey < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.compact_uint32(services.size());
		for (Integer _v_ : services) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(scenarioquests.size());
		for (Integer _v_ : scenarioquests) {
			_os_.marshal(_v_);
		}
		_os_.marshal(msgid);
		_os_.compact_uint32(parameters.size());
		for (com.locojoy.base.Octets _v_ : parameters) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			services.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			scenarioquests.add(_v_);
		}
		msgid = _os_.unmarshal_int();
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
		if (_o1_ instanceof SVisitNpcContainChatMsg) {
			SVisitNpcContainChatMsg _o_ = (SVisitNpcContainChatMsg)_o1_;
			if (npckey != _o_.npckey) return false;
			if (!services.equals(_o_.services)) return false;
			if (!scenarioquests.equals(_o_.scenarioquests)) return false;
			if (msgid != _o_.msgid) return false;
			if (!parameters.equals(_o_.parameters)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += services.hashCode();
		_h_ += scenarioquests.hashCode();
		_h_ += msgid;
		_h_ += parameters.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(services).append(",");
		_sb_.append(scenarioquests).append(",");
		_sb_.append(msgid).append(",");
		_sb_.append(parameters).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

