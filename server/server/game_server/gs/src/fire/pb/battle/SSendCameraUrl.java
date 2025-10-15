
package fire.pb.battle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSendCameraUrl__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSendCameraUrl extends __SSendCameraUrl__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793734;

	public int getType() {
		return 793734;
	}

	public long battleid; // 战斗信息表id
	public byte ishave; // 0无, 1有
	public int sizebeforezip; // 压缩前长度
	public long sizeafterzip; // 压缩后长度
	public java.lang.String battlecameraurl; // 0拒绝  1接受

	public SSendCameraUrl() {
		battlecameraurl = "";
	}

	public SSendCameraUrl(long _battleid_, byte _ishave_, int _sizebeforezip_, long _sizeafterzip_, java.lang.String _battlecameraurl_) {
		this.battleid = _battleid_;
		this.ishave = _ishave_;
		this.sizebeforezip = _sizebeforezip_;
		this.sizeafterzip = _sizeafterzip_;
		this.battlecameraurl = _battlecameraurl_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(battleid);
		_os_.marshal(ishave);
		_os_.marshal(sizebeforezip);
		_os_.marshal(sizeafterzip);
		_os_.marshal(battlecameraurl, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		battleid = _os_.unmarshal_long();
		ishave = _os_.unmarshal_byte();
		sizebeforezip = _os_.unmarshal_int();
		sizeafterzip = _os_.unmarshal_long();
		battlecameraurl = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSendCameraUrl) {
			SSendCameraUrl _o_ = (SSendCameraUrl)_o1_;
			if (battleid != _o_.battleid) return false;
			if (ishave != _o_.ishave) return false;
			if (sizebeforezip != _o_.sizebeforezip) return false;
			if (sizeafterzip != _o_.sizeafterzip) return false;
			if (!battlecameraurl.equals(_o_.battlecameraurl)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)battleid;
		_h_ += (int)ishave;
		_h_ += sizebeforezip;
		_h_ += (int)sizeafterzip;
		_h_ += battlecameraurl.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(battleid).append(",");
		_sb_.append(ishave).append(",");
		_sb_.append(sizebeforezip).append(",");
		_sb_.append(sizeafterzip).append(",");
		_sb_.append("T").append(battlecameraurl.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

