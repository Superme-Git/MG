
package fire.msp;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MRoleOfflineScene__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MRoleOfflineScene extends __MRoleOfflineScene__ {
	@Override
	protected void process() {
		new PRoleOffline(roleid, mapinfo,laststaticmapinfo,qili).submit();
		
		gnet.link.Onlines.getInstance().remove(roleid);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 720899;

	public int getType() {
		return 720899;
	}

	public long roleid;
	public java.lang.String rolename;
	public fire.msp.RoleMapInfo mapinfo;
	public fire.msp.RoleMapInfo laststaticmapinfo;
	public int qili; // 当前气力（轻功）

	public MRoleOfflineScene() {
		rolename = "";
		mapinfo = new fire.msp.RoleMapInfo();
		laststaticmapinfo = new fire.msp.RoleMapInfo();
	}

	public MRoleOfflineScene(long _roleid_, java.lang.String _rolename_, fire.msp.RoleMapInfo _mapinfo_, fire.msp.RoleMapInfo _laststaticmapinfo_, int _qili_) {
		this.roleid = _roleid_;
		this.rolename = _rolename_;
		this.mapinfo = _mapinfo_;
		this.laststaticmapinfo = _laststaticmapinfo_;
		this.qili = _qili_;
	}

	public final boolean _validator_() {
		if (!mapinfo._validator_()) return false;
		if (!laststaticmapinfo._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(rolename, "UTF-16LE");
		_os_.marshal(mapinfo);
		_os_.marshal(laststaticmapinfo);
		_os_.marshal(qili);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		rolename = _os_.unmarshal_String("UTF-16LE");
		mapinfo.unmarshal(_os_);
		laststaticmapinfo.unmarshal(_os_);
		qili = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MRoleOfflineScene) {
			MRoleOfflineScene _o_ = (MRoleOfflineScene)_o1_;
			if (roleid != _o_.roleid) return false;
			if (!rolename.equals(_o_.rolename)) return false;
			if (!mapinfo.equals(_o_.mapinfo)) return false;
			if (!laststaticmapinfo.equals(_o_.laststaticmapinfo)) return false;
			if (qili != _o_.qili) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += rolename.hashCode();
		_h_ += mapinfo.hashCode();
		_h_ += laststaticmapinfo.hashCode();
		_h_ += qili;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append("T").append(rolename.length()).append(",");
		_sb_.append(mapinfo).append(",");
		_sb_.append(laststaticmapinfo).append(",");
		_sb_.append(qili).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

