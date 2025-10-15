
package fire.pb.move;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class RoleBasic implements Marshal {
	public com.locojoy.base.Octets rolebasicoctets; // 角色数据
	public fire.pb.move.Pos pos; // 当前坐标
	public byte posz; // 当前z坐标
	public java.util.LinkedList<fire.pb.move.Pos> poses; // 个数不同代表的含义不同。1：走路的目的坐标；2：一段跳起始坐标；4：二段跳起始坐标*2

	public RoleBasic() {
		rolebasicoctets = new com.locojoy.base.Octets();
		pos = new fire.pb.move.Pos();
		poses = new java.util.LinkedList<fire.pb.move.Pos>();
	}

	public RoleBasic(com.locojoy.base.Octets _rolebasicoctets_, fire.pb.move.Pos _pos_, byte _posz_, java.util.LinkedList<fire.pb.move.Pos> _poses_) {
		this.rolebasicoctets = _rolebasicoctets_;
		this.pos = _pos_;
		this.posz = _posz_;
		this.poses = _poses_;
	}

	public final boolean _validator_() {
		if (!pos._validator_()) return false;
		for (fire.pb.move.Pos _v_ : poses)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(rolebasicoctets);
		_os_.marshal(pos);
		_os_.marshal(posz);
		_os_.compact_uint32(poses.size());
		for (fire.pb.move.Pos _v_ : poses) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		rolebasicoctets = _os_.unmarshal_Octets();
		pos.unmarshal(_os_);
		posz = _os_.unmarshal_byte();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.move.Pos _v_ = new fire.pb.move.Pos();
			_v_.unmarshal(_os_);
			poses.add(_v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof RoleBasic) {
			RoleBasic _o_ = (RoleBasic)_o1_;
			if (!rolebasicoctets.equals(_o_.rolebasicoctets)) return false;
			if (!pos.equals(_o_.pos)) return false;
			if (posz != _o_.posz) return false;
			if (!poses.equals(_o_.poses)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolebasicoctets.hashCode();
		_h_ += pos.hashCode();
		_h_ += (int)posz;
		_h_ += poses.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("B").append(rolebasicoctets.size()).append(",");
		_sb_.append(pos).append(",");
		_sb_.append(posz).append(",");
		_sb_.append(poses).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

