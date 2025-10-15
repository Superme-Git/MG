
package fire.msp.circletask;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MCreatePosForCTGoto__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MCreatePosForCTGoto extends __MCreatePosForCTGoto__ {
	@Override
	protected void process() {
		boolean isnpcgive = isnpc > 0 ? true : false;
		new PCreatePosForCT(roleid,npckey,mapid,posx,posy,ctconfid,dstnpcid,cttype,isnpcgive).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 741908;

	public int getType() {
		return 741908;
	}

	public long roleid;
	public long npckey;
	public int mapid;
	public int posx;
	public int posy;
	public int ctconfid; // 任务id
	public int dstnpcid;
	public int cttype; // 循环类型
	public int isnpc;

	public MCreatePosForCTGoto() {
	}

	public MCreatePosForCTGoto(long _roleid_, long _npckey_, int _mapid_, int _posx_, int _posy_, int _ctconfid_, int _dstnpcid_, int _cttype_, int _isnpc_) {
		this.roleid = _roleid_;
		this.npckey = _npckey_;
		this.mapid = _mapid_;
		this.posx = _posx_;
		this.posy = _posy_;
		this.ctconfid = _ctconfid_;
		this.dstnpcid = _dstnpcid_;
		this.cttype = _cttype_;
		this.isnpc = _isnpc_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(roleid);
		_os_.marshal(npckey);
		_os_.marshal(mapid);
		_os_.marshal(posx);
		_os_.marshal(posy);
		_os_.marshal(ctconfid);
		_os_.marshal(dstnpcid);
		_os_.marshal(cttype);
		_os_.marshal(isnpc);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		npckey = _os_.unmarshal_long();
		mapid = _os_.unmarshal_int();
		posx = _os_.unmarshal_int();
		posy = _os_.unmarshal_int();
		ctconfid = _os_.unmarshal_int();
		dstnpcid = _os_.unmarshal_int();
		cttype = _os_.unmarshal_int();
		isnpc = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MCreatePosForCTGoto) {
			MCreatePosForCTGoto _o_ = (MCreatePosForCTGoto)_o1_;
			if (roleid != _o_.roleid) return false;
			if (npckey != _o_.npckey) return false;
			if (mapid != _o_.mapid) return false;
			if (posx != _o_.posx) return false;
			if (posy != _o_.posy) return false;
			if (ctconfid != _o_.ctconfid) return false;
			if (dstnpcid != _o_.dstnpcid) return false;
			if (cttype != _o_.cttype) return false;
			if (isnpc != _o_.isnpc) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)npckey;
		_h_ += mapid;
		_h_ += posx;
		_h_ += posy;
		_h_ += ctconfid;
		_h_ += dstnpcid;
		_h_ += cttype;
		_h_ += isnpc;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(mapid).append(",");
		_sb_.append(posx).append(",");
		_sb_.append(posy).append(",");
		_sb_.append(ctconfid).append(",");
		_sb_.append(dstnpcid).append(",");
		_sb_.append(cttype).append(",");
		_sb_.append(isnpc).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MCreatePosForCTGoto _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = mapid - _o_.mapid;
		if (0 != _c_) return _c_;
		_c_ = posx - _o_.posx;
		if (0 != _c_) return _c_;
		_c_ = posy - _o_.posy;
		if (0 != _c_) return _c_;
		_c_ = ctconfid - _o_.ctconfid;
		if (0 != _c_) return _c_;
		_c_ = dstnpcid - _o_.dstnpcid;
		if (0 != _c_) return _c_;
		_c_ = cttype - _o_.cttype;
		if (0 != _c_) return _c_;
		_c_ = isnpc - _o_.isnpc;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

