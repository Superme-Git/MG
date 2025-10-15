
package fire.msp.move;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MCreateDynamicScene__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MCreateDynamicScene extends __MCreateDynamicScene__ {
	@Override
	protected void process() {
		// protocol handle
		if(sceneid == 0L)
			return;
		if (mkdb.Transaction.current() != null) {
			new PTriggerEventAfterSceneCreate(sceneid,ownerid,scenetype).call();
		}
		else {
			new PTriggerEventAfterSceneCreate(sceneid,ownerid,scenetype).submit();
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724912;

	public int getType() {
		return 724912;
	}

	public long sceneid; // 场景模块创建的场景ID
	public long ownerid; // 副本的所有者ID(可能是角色ID,也可能是团体ID)
	public int scenetype; // 副本场景的类型，如果为0，表示为静态场景

	public MCreateDynamicScene() {
	}

	public MCreateDynamicScene(long _sceneid_, long _ownerid_, int _scenetype_) {
		this.sceneid = _sceneid_;
		this.ownerid = _ownerid_;
		this.scenetype = _scenetype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(sceneid);
		_os_.marshal(ownerid);
		_os_.marshal(scenetype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		sceneid = _os_.unmarshal_long();
		ownerid = _os_.unmarshal_long();
		scenetype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MCreateDynamicScene) {
			MCreateDynamicScene _o_ = (MCreateDynamicScene)_o1_;
			if (sceneid != _o_.sceneid) return false;
			if (ownerid != _o_.ownerid) return false;
			if (scenetype != _o_.scenetype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)sceneid;
		_h_ += (int)ownerid;
		_h_ += scenetype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(sceneid).append(",");
		_sb_.append(ownerid).append(",");
		_sb_.append(scenetype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MCreateDynamicScene _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(sceneid - _o_.sceneid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(ownerid - _o_.ownerid);
		if (0 != _c_) return _c_;
		_c_ = scenetype - _o_.scenetype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

