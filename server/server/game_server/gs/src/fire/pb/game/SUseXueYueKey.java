
package fire.pb.game;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SUseXueYueKey__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SUseXueYueKey extends __SUseXueYueKey__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810370;

	public int getType() {
		return 810370;
	}

	public int npckid; // npcid
	public long npckey; // npckey为npc的唯一ID
	public int mapid;
	public int xpos;
	public int ypos;

	public SUseXueYueKey() {
	}

	public SUseXueYueKey(int _npckid_, long _npckey_, int _mapid_, int _xpos_, int _ypos_) {
		this.npckid = _npckid_;
		this.npckey = _npckey_;
		this.mapid = _mapid_;
		this.xpos = _xpos_;
		this.ypos = _ypos_;
	}

	public final boolean _validator_() {
		if (npckey < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckid);
		_os_.marshal(npckey);
		_os_.marshal(mapid);
		_os_.marshal(xpos);
		_os_.marshal(ypos);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckid = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		mapid = _os_.unmarshal_int();
		xpos = _os_.unmarshal_int();
		ypos = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SUseXueYueKey) {
			SUseXueYueKey _o_ = (SUseXueYueKey)_o1_;
			if (npckid != _o_.npckid) return false;
			if (npckey != _o_.npckey) return false;
			if (mapid != _o_.mapid) return false;
			if (xpos != _o_.xpos) return false;
			if (ypos != _o_.ypos) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npckid;
		_h_ += (int)npckey;
		_h_ += mapid;
		_h_ += xpos;
		_h_ += ypos;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(mapid).append(",");
		_sb_.append(xpos).append(",");
		_sb_.append(ypos).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(SUseXueYueKey _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = npckid - _o_.npckid;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = mapid - _o_.mapid;
		if (0 != _c_) return _c_;
		_c_ = xpos - _o_.xpos;
		if (0 != _c_) return _c_;
		_c_ = ypos - _o_.ypos;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

