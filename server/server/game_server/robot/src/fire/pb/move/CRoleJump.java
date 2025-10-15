
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRoleJump__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRoleJump extends __CRoleJump__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790477;

	public int getType() {
		return 790477;
	}

	public java.util.LinkedList<fire.pb.move.Pos> poslist; // 跳跃之前未check的关键点
	public fire.pb.move.Pos srcpos; // 出发点
	public fire.pb.move.Pos destpos; // 到达点
	public byte jumptype; // 0为普通跳，1为2段跳
	public long sceneid; // 场景id

	public CRoleJump() {
		poslist = new java.util.LinkedList<fire.pb.move.Pos>();
		srcpos = new fire.pb.move.Pos();
		destpos = new fire.pb.move.Pos();
	}

	public CRoleJump(java.util.LinkedList<fire.pb.move.Pos> _poslist_, fire.pb.move.Pos _srcpos_, fire.pb.move.Pos _destpos_, byte _jumptype_, long _sceneid_) {
		this.poslist = _poslist_;
		this.srcpos = _srcpos_;
		this.destpos = _destpos_;
		this.jumptype = _jumptype_;
		this.sceneid = _sceneid_;
	}

	public final boolean _validator_() {
		for (fire.pb.move.Pos _v_ : poslist)
			if (!_v_._validator_()) return false;
		if (!srcpos._validator_()) return false;
		if (!destpos._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(poslist.size());
		for (fire.pb.move.Pos _v_ : poslist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(srcpos);
		_os_.marshal(destpos);
		_os_.marshal(jumptype);
		_os_.marshal(sceneid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.move.Pos _v_ = new fire.pb.move.Pos();
			_v_.unmarshal(_os_);
			poslist.add(_v_);
		}
		srcpos.unmarshal(_os_);
		destpos.unmarshal(_os_);
		jumptype = _os_.unmarshal_byte();
		sceneid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRoleJump) {
			CRoleJump _o_ = (CRoleJump)_o1_;
			if (!poslist.equals(_o_.poslist)) return false;
			if (!srcpos.equals(_o_.srcpos)) return false;
			if (!destpos.equals(_o_.destpos)) return false;
			if (jumptype != _o_.jumptype) return false;
			if (sceneid != _o_.sceneid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += poslist.hashCode();
		_h_ += srcpos.hashCode();
		_h_ += destpos.hashCode();
		_h_ += (int)jumptype;
		_h_ += (int)sceneid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(poslist).append(",");
		_sb_.append(srcpos).append(",");
		_sb_.append(destpos).append(",");
		_sb_.append(jumptype).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

