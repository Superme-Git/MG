
package fire.msp.move;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GWeatherChange__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GWeatherChange extends __GWeatherChange__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 724931;

	public int getType() {
		return 724931;
	}

	public byte weatherid; // 天气Id
	public java.lang.String tipsparm; // tips参数
	public long sceneid;

	public GWeatherChange() {
		tipsparm = "";
	}

	public GWeatherChange(byte _weatherid_, java.lang.String _tipsparm_, long _sceneid_) {
		this.weatherid = _weatherid_;
		this.tipsparm = _tipsparm_;
		this.sceneid = _sceneid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(weatherid);
		_os_.marshal(tipsparm, "UTF-16LE");
		_os_.marshal(sceneid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		weatherid = _os_.unmarshal_byte();
		tipsparm = _os_.unmarshal_String("UTF-16LE");
		sceneid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GWeatherChange) {
			GWeatherChange _o_ = (GWeatherChange)_o1_;
			if (weatherid != _o_.weatherid) return false;
			if (!tipsparm.equals(_o_.tipsparm)) return false;
			if (sceneid != _o_.sceneid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)weatherid;
		_h_ += tipsparm.hashCode();
		_h_ += (int)sceneid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(weatherid).append(",");
		_sb_.append("T").append(tipsparm.length()).append(",");
		_sb_.append(sceneid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

