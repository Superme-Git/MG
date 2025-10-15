
package fire.pb.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SNPCMoveTo__ extends mkio.Protocol { }

/** <protocol  name="SRoleJumpStop" type="4048" maxsize="65535" prior="1">	服务器广播角色跳跃停止
		<variable name="roleid" type="long"/>		 角色ID	
		<variable name="sceneID" type="long"/>   场景id
	</protocol>
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SNPCMoveTo extends __SNPCMoveTo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 790482;

	public int getType() {
		return 790482;
	}

	public long npckey; // npckey
	public int speed; // 速度
	public fire.pb.move.Pos destpos; // 目的点

	public SNPCMoveTo() {
		destpos = new fire.pb.move.Pos();
	}

	public SNPCMoveTo(long _npckey_, int _speed_, fire.pb.move.Pos _destpos_) {
		this.npckey = _npckey_;
		this.speed = _speed_;
		this.destpos = _destpos_;
	}

	public final boolean _validator_() {
		if (!destpos._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(speed);
		_os_.marshal(destpos);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		speed = _os_.unmarshal_int();
		destpos.unmarshal(_os_);
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SNPCMoveTo) {
			SNPCMoveTo _o_ = (SNPCMoveTo)_o1_;
			if (npckey != _o_.npckey) return false;
			if (speed != _o_.speed) return false;
			if (!destpos.equals(_o_.destpos)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += speed;
		_h_ += destpos.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(speed).append(",");
		_sb_.append(destpos).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SNPCMoveTo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = speed - _o_.speed;
		if (0 != _c_) return _c_;
		_c_ = destpos.compareTo(_o_.destpos);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

