
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SUpdateMemberPosition__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SUpdateMemberPosition extends __SUpdateMemberPosition__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794471;

	public int getType() {
		return 794471;
	}

	public long roleid;
	public fire.pb.team.Pos1 position; // 坐标
	public long sceneid; // 场景ID

	public SUpdateMemberPosition() {
		position = new fire.pb.team.Pos1();
	}

	public SUpdateMemberPosition(long _roleid_, fire.pb.team.Pos1 _position_, long _sceneid_) {
		this.roleid = _roleid_;
		this.position = _position_;
		this.sceneid = _sceneid_;
	}

	public final boolean _validator_() {
		if (!position._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(position);
		_os_.marshal(sceneid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		position.unmarshal(_os_);
		sceneid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SUpdateMemberPosition) {
			SUpdateMemberPosition _o_ = (SUpdateMemberPosition)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!position.equals(_o_.position)) return false;
			if (sceneid != _o_.sceneid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += position.hashCode();
		_h_ += (int)sceneid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(position).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

