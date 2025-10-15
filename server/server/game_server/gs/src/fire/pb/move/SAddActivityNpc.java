
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SAddActivityNpc__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SAddActivityNpc extends __SAddActivityNpc__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790463;

	public int getType() {
		return 790463;
	}

	public java.util.ArrayList<Integer> npcids;
	public java.util.ArrayList<fire.pb.move.Pos> poslist; // 需要添加的npc的坐标

	public SAddActivityNpc() {
		npcids = new java.util.ArrayList<Integer>();
		poslist = new java.util.ArrayList<fire.pb.move.Pos>();
	}

	public SAddActivityNpc(java.util.ArrayList<Integer> _npcids_, java.util.ArrayList<fire.pb.move.Pos> _poslist_) {
		this.npcids = _npcids_;
		this.poslist = _poslist_;
	}

	public final boolean _validator_() {
		for (fire.pb.move.Pos _v_ : poslist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(npcids.size());
		for (Integer _v_ : npcids) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(poslist.size());
		for (fire.pb.move.Pos _v_ : poslist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			npcids.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.move.Pos _v_ = new fire.pb.move.Pos();
			_v_.unmarshal(_os_);
			poslist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SAddActivityNpc) {
			SAddActivityNpc _o_ = (SAddActivityNpc)_o1_;
			if (!npcids.equals(_o_.npcids)) return false;
			if (!poslist.equals(_o_.poslist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npcids.hashCode();
		_h_ += poslist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcids).append(",");
		_sb_.append(poslist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

