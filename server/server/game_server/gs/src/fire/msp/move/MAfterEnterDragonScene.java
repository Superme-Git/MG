
package fire.msp.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MAfterEnterDragonScene__ extends mkio.Protocol { }

/** protocol name="GRoleModelChange" type="4023" maxsize="65535" prior="1">  
		<variable name="roleid" type="long"/>
		<variable name="newshapeid" type="int"/> 换装值
		<variable name="teammembers" type="set" value="long"/>
	</protocol
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MAfterEnterDragonScene extends __MAfterEnterDragonScene__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724920;

	public int getType() {
		return 724920;
	}

	public long roleid;
	public long ownerid;
	public int mapid;

	public MAfterEnterDragonScene() {
	}

	public MAfterEnterDragonScene(long _roleid_, long _ownerid_, int _mapid_) {
		this.roleid = _roleid_;
		this.ownerid = _ownerid_;
		this.mapid = _mapid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(ownerid);
		_os_.marshal(mapid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		ownerid = _os_.unmarshal_long();
		mapid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MAfterEnterDragonScene) {
			MAfterEnterDragonScene _o_ = (MAfterEnterDragonScene)_o1_;
			if (roleid != _o_.roleid) return false;
			if (ownerid != _o_.ownerid) return false;
			if (mapid != _o_.mapid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)ownerid;
		_h_ += mapid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(ownerid).append(",");
		_sb_.append(mapid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MAfterEnterDragonScene _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(ownerid - _o_.ownerid);
		if (0 != _c_) return _c_;
		_c_ = mapid - _o_.mapid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

