
package fire.pb;
import fire.pb.util.DateValidate;
import mkdb.Procedure;


// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqPointSchemeTime__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqPointSchemeTime extends __CReqPointSchemeTime__ {
	@Override
	protected void process() {
		// protocol handle
		long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId <= 0)
			return;
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				final xbean.Properties prop = xtable.Properties.get(roleId);
				if (null == prop)
					return false;
				
				long currentTime = System.currentTimeMillis();
				if (!DateValidate.inTheSameDay(prop.getSchemechangetimes(), currentTime)) {
					prop.setSchemechangetimes(currentTime);
					prop.setSchemechanges(0);
				}
				
				SReqPointSchemeTime refresh = new SReqPointSchemeTime();
				refresh.schemetimes = prop.getSchemechanges();
				Procedure.psendWhileCommit(roleId, refresh);
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786541;

	public int getType() {
		return 786541;
	}


	public CReqPointSchemeTime() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReqPointSchemeTime) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CReqPointSchemeTime _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

