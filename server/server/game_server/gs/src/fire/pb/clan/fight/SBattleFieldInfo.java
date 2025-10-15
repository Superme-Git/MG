
package fire.pb.clan.fight;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SBattleFieldInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SBattleFieldInfo extends __SBattleFieldInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808540;

	public int getType() {
		return 808540;
	}

	public java.lang.String clanname1; // 公会名字1 by changhao
	public java.lang.String clanname2; // 公会名字2 by changhao
	public long clanid1; // 公会1ID by changhao
	public long clanid2; // 公会2ID by changhao

	public SBattleFieldInfo() {
		clanname1 = "";
		clanname2 = "";
	}

	public SBattleFieldInfo(java.lang.String _clanname1_, java.lang.String _clanname2_, long _clanid1_, long _clanid2_) {
		this.clanname1 = _clanname1_;
		this.clanname2 = _clanname2_;
		this.clanid1 = _clanid1_;
		this.clanid2 = _clanid2_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(clanname1, "UTF-16LE");
		_os_.marshal(clanname2, "UTF-16LE");
		_os_.marshal(clanid1);
		_os_.marshal(clanid2);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		clanname1 = _os_.unmarshal_String("UTF-16LE");
		clanname2 = _os_.unmarshal_String("UTF-16LE");
		clanid1 = _os_.unmarshal_long();
		clanid2 = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SBattleFieldInfo) {
			SBattleFieldInfo _o_ = (SBattleFieldInfo)_o1_;
			if (!clanname1.equals(_o_.clanname1)) return false;
			if (!clanname2.equals(_o_.clanname2)) return false;
			if (clanid1 != _o_.clanid1) return false;
			if (clanid2 != _o_.clanid2) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += clanname1.hashCode();
		_h_ += clanname2.hashCode();
		_h_ += (int)clanid1;
		_h_ += (int)clanid2;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(clanname1.length()).append(",");
		_sb_.append("T").append(clanname2.length()).append(",");
		_sb_.append(clanid1).append(",");
		_sb_.append(clanid2).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

