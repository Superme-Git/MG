
package fire.pb.ranklist.getrankinfo;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SFactionRankInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SFactionRankInfo extends __SFactionRankInfo__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810262;

	public int getType() {
		return 810262;
	}

	public long factionkey; // 公会id by changhao
	public java.lang.String lastname; // 曾用名 by changhao
	public java.lang.String title; // 宗旨 by changhao
	public long factionmasterid; // 会长id by changhao

	public SFactionRankInfo() {
		lastname = "";
		title = "";
	}

	public SFactionRankInfo(long _factionkey_, java.lang.String _lastname_, java.lang.String _title_, long _factionmasterid_) {
		this.factionkey = _factionkey_;
		this.lastname = _lastname_;
		this.title = _title_;
		this.factionmasterid = _factionmasterid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(factionkey);
		_os_.marshal(lastname, "UTF-16LE");
		_os_.marshal(title, "UTF-16LE");
		_os_.marshal(factionmasterid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		factionkey = _os_.unmarshal_long();
		lastname = _os_.unmarshal_String("UTF-16LE");
		title = _os_.unmarshal_String("UTF-16LE");
		factionmasterid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SFactionRankInfo) {
			SFactionRankInfo _o_ = (SFactionRankInfo)_o1_;
			if (factionkey != _o_.factionkey) return false;
			if (!lastname.equals(_o_.lastname)) return false;
			if (!title.equals(_o_.title)) return false;
			if (factionmasterid != _o_.factionmasterid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)factionkey;
		_h_ += lastname.hashCode();
		_h_ += title.hashCode();
		_h_ += (int)factionmasterid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(factionkey).append(",");
		_sb_.append("T").append(lastname.length()).append(",");
		_sb_.append("T").append(title.length()).append(",");
		_sb_.append(factionmasterid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

