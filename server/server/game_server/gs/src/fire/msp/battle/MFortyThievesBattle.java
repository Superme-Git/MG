
package fire.msp.battle;

import fire.pb.battle.specialevent.PFortyThievesBattle;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MFortyThievesBattle__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MFortyThievesBattle extends __MFortyThievesBattle__ {
	@Override
	protected void process() {
		// protocol handle
		new PFortyThievesBattle(roleid, npckey, areaconfig).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 727899;

	public int getType() {
		return 727899;
	}

	public long roleid;
	public long npckey; // 四十大盗的npckey
	public int areaconfig; // 四十大盗NPC所在地点的暗雷区域等级

	public MFortyThievesBattle() {
	}

	public MFortyThievesBattle(long _roleid_, long _npckey_, int _areaconfig_) {
		this.roleid = _roleid_;
		this.npckey = _npckey_;
		this.areaconfig = _areaconfig_;
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
		_os_.marshal(areaconfig);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		roleid = _os_.unmarshal_long();
		npckey = _os_.unmarshal_long();
		areaconfig = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MFortyThievesBattle) {
			MFortyThievesBattle _o_ = (MFortyThievesBattle)_o1_;
			if (roleid != _o_.roleid) return false;
			if (npckey != _o_.npckey) return false;
			if (areaconfig != _o_.areaconfig) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)roleid;
		_h_ += (int)npckey;
		_h_ += areaconfig;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(roleid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(areaconfig).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MFortyThievesBattle _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(roleid - _o_.roleid);
		if (0 != _c_) return _c_;
		_c_ = Long.signum(npckey - _o_.npckey);
		if (0 != _c_) return _c_;
		_c_ = areaconfig - _o_.areaconfig;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

