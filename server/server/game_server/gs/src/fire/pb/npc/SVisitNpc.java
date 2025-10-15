
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SVisitNpc__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SVisitNpc extends __SVisitNpc__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795434;

	public int getType() {
		return 795434;
	}

	public long npckey; // npckey为npc的唯一ID
	public java.util.ArrayList<Integer> services; // services 为npc所提供的服务 IDs.
	public java.util.ArrayList<Integer> scenarioquests; // 剧情任务

	public SVisitNpc() {
		services = new java.util.ArrayList<Integer>();
		scenarioquests = new java.util.ArrayList<Integer>();
	}

	public SVisitNpc(long _npckey_, java.util.ArrayList<Integer> _services_, java.util.ArrayList<Integer> _scenarioquests_) {
		this.npckey = _npckey_;
		this.services = _services_;
		this.scenarioquests = _scenarioquests_;
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
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SVisitNpc) {
			SVisitNpc _o_ = (SVisitNpc)_o1_;
			if (npckey != _o_.npckey) return false;
			if (!services.equals(_o_.services)) return false;
			if (!scenarioquests.equals(_o_.scenarioquests)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += services.hashCode();
		_h_ += scenarioquests.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(services).append(",");
		_sb_.append(scenarioquests).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

