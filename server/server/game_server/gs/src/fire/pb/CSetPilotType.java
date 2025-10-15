
package fire.pb;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSetPilotType__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSetPilotType extends __CSetPilotType__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleId = gnet.link.Onlines.getInstance().findRoleid( this );
		if (roleId < 0)
			return ;
		new mkdb.Procedure()
		{
			protected boolean process() throws Exception {
				final xbean.Properties roleProp = xtable.Properties.get(roleId);
				if (null == roleProp)
					return false;
				roleProp.setPilot(pilottype);
				
				// 运营日志
				fire.log.YYLogger.usrFlgLog(roleId, "E1 : " + pilottype);
				
				return true;
			};
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786543;

	public int getType() {
		return 786543;
	}

	public int pilottype; // 适配引导类型  0新手 1老手

	public CSetPilotType() {
	}

	public CSetPilotType(int _pilottype_) {
		this.pilottype = _pilottype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(pilottype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		pilottype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSetPilotType) {
			CSetPilotType _o_ = (CSetPilotType)_o1_;
			if (pilottype != _o_.pilottype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += pilottype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(pilottype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CSetPilotType _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = pilottype - _o_.pilottype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

