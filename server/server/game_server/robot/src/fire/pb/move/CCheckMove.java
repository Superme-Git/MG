
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CCheckMove__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CCheckMove extends __CCheckMove__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790435;

	public int getType() {
		return 790435;
	}

	public fire.pb.move.Pos curpos; // 人物当前点
	public java.util.LinkedList<fire.pb.move.Pos> poslist; // 路径上的关键点
	public long sceneid; // 场景id

	public CCheckMove() {
		curpos = new fire.pb.move.Pos();
		poslist = new java.util.LinkedList<fire.pb.move.Pos>();
	}

	public CCheckMove(fire.pb.move.Pos _curpos_, java.util.LinkedList<fire.pb.move.Pos> _poslist_, long _sceneid_) {
		this.curpos = _curpos_;
		this.poslist = _poslist_;
		this.sceneid = _sceneid_;
	}

	public final boolean _validator_() {
		if (!curpos._validator_()) return false;
		for (fire.pb.move.Pos _v_ : poslist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(curpos);
		_os_.compact_uint32(poslist.size());
		for (fire.pb.move.Pos _v_ : poslist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(sceneid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		curpos.unmarshal(_os_);
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.move.Pos _v_ = new fire.pb.move.Pos();
			_v_.unmarshal(_os_);
			poslist.add(_v_);
		}
		sceneid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CCheckMove) {
			CCheckMove _o_ = (CCheckMove)_o1_;
			if (!curpos.equals(_o_.curpos)) return false;
			if (!poslist.equals(_o_.poslist)) return false;
			if (sceneid != _o_.sceneid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += curpos.hashCode();
		_h_ += poslist.hashCode();
		_h_ += (int)sceneid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(curpos).append(",");
		_sb_.append(poslist).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

