
package gnet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GMShutup__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GMShutup extends __GMShutup__ {
	@Override
	protected void process() {
		// protocol handle
		new mkdb.Procedure(){
			public boolean process(){
				final long now = java.util.Calendar.getInstance().getTimeInMillis();
				xtable.Userid2forbidtime.remove(dstuserid);
				xbean.forbidinfo forbid = xbean.Pod.newforbidinfo();
				forbid.setForbidtime(forbid_time*1000L + now);
				forbid.setReason(reason.getString("UTF-16LE"));
				xtable.Userid2forbidtime.add(dstuserid, forbid);
				return true;
			}
			
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 356;

	public int getType() {
		return 356;
	}

	public int gmuserid;
	public int localsid;
	public int dstuserid;
	public int forbid_time;
	public com.locojoy.base.Octets reason;

	public GMShutup() {
		reason = new com.locojoy.base.Octets();
	}

	public GMShutup(int _gmuserid_, int _localsid_, int _dstuserid_, int _forbid_time_, com.locojoy.base.Octets _reason_) {
		this.gmuserid = _gmuserid_;
		this.localsid = _localsid_;
		this.dstuserid = _dstuserid_;
		this.forbid_time = _forbid_time_;
		this.reason = _reason_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(gmuserid);
		_os_.marshal(localsid);
		_os_.marshal(dstuserid);
		_os_.marshal(forbid_time);
		_os_.marshal(reason);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		gmuserid = _os_.unmarshal_int();
		localsid = _os_.unmarshal_int();
		dstuserid = _os_.unmarshal_int();
		forbid_time = _os_.unmarshal_int();
		reason = _os_.unmarshal_Octets();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GMShutup) {
			GMShutup _o_ = (GMShutup)_o1_;
			if (gmuserid != _o_.gmuserid) return false;
			if (localsid != _o_.localsid) return false;
			if (dstuserid != _o_.dstuserid) return false;
			if (forbid_time != _o_.forbid_time) return false;
			if (!reason.equals(_o_.reason)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += gmuserid;
		_h_ += localsid;
		_h_ += dstuserid;
		_h_ += forbid_time;
		_h_ += reason.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(gmuserid).append(",");
		_sb_.append(localsid).append(",");
		_sb_.append(dstuserid).append(",");
		_sb_.append(forbid_time).append(",");
		_sb_.append("B").append(reason.size()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

