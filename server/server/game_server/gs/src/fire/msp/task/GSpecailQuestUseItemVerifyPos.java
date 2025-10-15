
package fire.msp.task;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GSpecailQuestUseItemVerifyPos__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GSpecailQuestUseItemVerifyPos extends __GSpecailQuestUseItemVerifyPos__ {
	@Override
	protected void process() {
		fire.pb.scene.movable.Role role = fire.pb.scene.manager.RoleManager.getInstance().getRoleByID( roleid );
		if(null == role) return;
		if(null == role.getScene()) return;
		
		if ( role.getScene().getMapID() == mapid ) {
			final fire.pb.scene.sPos.GridPos rolepos = role.getPos().toGridPos();
			if ( Math.abs( xpos - rolepos.getX() ) + Math.abs( ypos - rolepos.getY() ) 
					< 10 ) {
				MSpecailQuestUseItemVerifyPosSucc send = new MSpecailQuestUseItemVerifyPosSucc();
				send.roleid = roleid;
				send.useitemid = useitemid;
				send.specailquestid = specailquestid;
				fire.pb.scene.SceneClient.pSend( send );
				return;
			}
		}
		MSpecailQuestUseItemVerifyPosFail send = new MSpecailQuestUseItemVerifyPosFail();
		send.roleid = roleid;
		send.useitemid = useitemid;
		send.specailquestid = specailquestid;
		fire.pb.scene.SceneClient.pSend( send );
		return;
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 739897;

	public int getType() {
		return 739897;
	}

	public long roleid;
	public int mapid;
	public int xpos;
	public int ypos;
	public int specailquestid;
	public int useitemid;

	public GSpecailQuestUseItemVerifyPos() {
	}

	public GSpecailQuestUseItemVerifyPos(long _roleid_, int _mapid_, int _xpos_, int _ypos_, int _specailquestid_, int _useitemid_) {
		this.roleid = _roleid_;
		this.mapid = _mapid_;
		this.xpos = _xpos_;
		this.ypos = _ypos_;
		this.specailquestid = _specailquestid_;
		this.useitemid = _useitemid_;
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
		_os_.marshal(xpos);
		_os_.marshal(ypos);
		_os_.marshal(specailquestid);
		_os_.marshal(useitemid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		mapid = _os_.unmarshal_int();
		xpos = _os_.unmarshal_int();
		ypos = _os_.unmarshal_int();
		specailquestid = _os_.unmarshal_int();
		useitemid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GSpecailQuestUseItemVerifyPos) {
			GSpecailQuestUseItemVerifyPos _o_ = (GSpecailQuestUseItemVerifyPos)_o1_;
			if (roleid != _o_.roleid) return false;
			if (mapid != _o_.mapid) return false;
			if (xpos != _o_.xpos) return false;
			if (ypos != _o_.ypos) return false;
			if (specailquestid != _o_.specailquestid) return false;
			if (useitemid != _o_.useitemid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += mapid;
		_h_ += xpos;
		_h_ += ypos;
		_h_ += specailquestid;
		_h_ += useitemid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(mapid).append(",");
		_sb_.append(xpos).append(",");
		_sb_.append(ypos).append(",");
		_sb_.append(specailquestid).append(",");
		_sb_.append(useitemid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GSpecailQuestUseItemVerifyPos _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = mapid - _o_.mapid;
		if (0 != _c_) return _c_;
		_c_ = xpos - _o_.xpos;
		if (0 != _c_) return _c_;
		_c_ = ypos - _o_.ypos;
		if (0 != _c_) return _c_;
		_c_ = specailquestid - _o_.specailquestid;
		if (0 != _c_) return _c_;
		_c_ = useitemid - _o_.useitemid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

