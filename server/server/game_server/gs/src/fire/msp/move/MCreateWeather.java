
package fire.msp.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MCreateWeather__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MCreateWeather extends __MCreateWeather__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724932;

	public int getType() {
		return 724932;
	}

	public byte weatherid; // 天气Id
	public long sceneid;
	public java.lang.String tipsparm;
	public long remaintime;

	public MCreateWeather() {
		tipsparm = "";
	}

	public MCreateWeather(byte _weatherid_, long _sceneid_, java.lang.String _tipsparm_, long _remaintime_) {
		this.weatherid = _weatherid_;
		this.sceneid = _sceneid_;
		this.tipsparm = _tipsparm_;
		this.remaintime = _remaintime_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(weatherid);
		_os_.marshal(sceneid);
		_os_.marshal(tipsparm, "UTF-16LE");
		_os_.marshal(remaintime);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		weatherid = _os_.unmarshal_byte();
		sceneid = _os_.unmarshal_long();
		tipsparm = _os_.unmarshal_String("UTF-16LE");
		remaintime = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MCreateWeather) {
			MCreateWeather _o_ = (MCreateWeather)_o1_;
			if (weatherid != _o_.weatherid) return false;
			if (sceneid != _o_.sceneid) return false;
			if (!tipsparm.equals(_o_.tipsparm)) return false;
			if (remaintime != _o_.remaintime) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)weatherid;
		_h_ += (int)sceneid;
		_h_ += tipsparm.hashCode();
		_h_ += (int)remaintime;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(weatherid).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append("T").append(tipsparm.length()).append(",");
		_sb_.append(remaintime).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

