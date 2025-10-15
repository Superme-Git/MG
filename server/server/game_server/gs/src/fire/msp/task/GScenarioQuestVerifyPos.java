
package fire.msp.task;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GScenarioQuestVerifyPos__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GScenarioQuestVerifyPos extends __GScenarioQuestVerifyPos__ {
	@Override
	protected void process() {
		// protocol handle
		fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID( roleid );
		if ( role != null ) {
			fire.pb.scene.sPos.GridPos pos = role.getPos().toGridPos();
			if ( pos.getX() >= left && pos.getX() <= right
					&& pos.getY() >= top && pos.getY() <= bottom ) {
				MScenarioQuestVerifyPosSucc succ = new MScenarioQuestVerifyPosSucc();
				succ.roleid = roleid;
				succ.scenarioquestid = scenarioquestid;
				fire.pb.scene.SceneClient.pSend( succ );
				return;
			}
		}
		MScenarioQuestVerifyPosFail fail = new MScenarioQuestVerifyPosFail();
		fail.roleid = roleid;
		fail.scenarioquestid = scenarioquestid;
		fire.pb.scene.SceneClient.pSend( fail );
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 739906;

	public int getType() {
		return 739906;
	}

	public long roleid;
	public int mapid;
	public int left;
	public int right;
	public int top;
	public int bottom;
	public int scenarioquestid;

	public GScenarioQuestVerifyPos() {
	}

	public GScenarioQuestVerifyPos(long _roleid_, int _mapid_, int _left_, int _right_, int _top_, int _bottom_, int _scenarioquestid_) {
		this.roleid = _roleid_;
		this.mapid = _mapid_;
		this.left = _left_;
		this.right = _right_;
		this.top = _top_;
		this.bottom = _bottom_;
		this.scenarioquestid = _scenarioquestid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(mapid);
		_os_.marshal(left);
		_os_.marshal(right);
		_os_.marshal(top);
		_os_.marshal(bottom);
		_os_.marshal(scenarioquestid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		mapid = _os_.unmarshal_int();
		left = _os_.unmarshal_int();
		right = _os_.unmarshal_int();
		top = _os_.unmarshal_int();
		bottom = _os_.unmarshal_int();
		scenarioquestid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GScenarioQuestVerifyPos) {
			GScenarioQuestVerifyPos _o_ = (GScenarioQuestVerifyPos)_o1_;
			if (roleid != _o_.roleid) return false;
			if (mapid != _o_.mapid) return false;
			if (left != _o_.left) return false;
			if (right != _o_.right) return false;
			if (top != _o_.top) return false;
			if (bottom != _o_.bottom) return false;
			if (scenarioquestid != _o_.scenarioquestid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += mapid;
		_h_ += left;
		_h_ += right;
		_h_ += top;
		_h_ += bottom;
		_h_ += scenarioquestid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(mapid).append(",");
		_sb_.append(left).append(",");
		_sb_.append(right).append(",");
		_sb_.append(top).append(",");
		_sb_.append(bottom).append(",");
		_sb_.append(scenarioquestid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GScenarioQuestVerifyPos _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = mapid - _o_.mapid;
		if (0 != _c_) return _c_;
		_c_ = left - _o_.left;
		if (0 != _c_) return _c_;
		_c_ = right - _o_.right;
		if (0 != _c_) return _c_;
		_c_ = top - _o_.top;
		if (0 != _c_) return _c_;
		_c_ = bottom - _o_.bottom;
		if (0 != _c_) return _c_;
		_c_ = scenarioquestid - _o_.scenarioquestid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

