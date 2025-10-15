
package fire.pb.battle.livedie;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SLiveDieBattleGiveRose__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SLiveDieBattleGiveRose extends __SLiveDieBattleGiveRose__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793845;

	public int getType() {
		return 793845;
	}

	public java.lang.String vedioid; // 录像id
	public int rosenum; // 次数
	public int roseflag; // 是否可以点赞 0可以  1不可

	public SLiveDieBattleGiveRose() {
		vedioid = "";
	}

	public SLiveDieBattleGiveRose(java.lang.String _vedioid_, int _rosenum_, int _roseflag_) {
		this.vedioid = _vedioid_;
		this.rosenum = _rosenum_;
		this.roseflag = _roseflag_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(vedioid, "UTF-16LE");
		_os_.marshal(rosenum);
		_os_.marshal(roseflag);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		vedioid = _os_.unmarshal_String("UTF-16LE");
		rosenum = _os_.unmarshal_int();
		roseflag = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SLiveDieBattleGiveRose) {
			SLiveDieBattleGiveRose _o_ = (SLiveDieBattleGiveRose)_o1_;
			if (!vedioid.equals(_o_.vedioid)) return false;
			if (rosenum != _o_.rosenum) return false;
			if (roseflag != _o_.roseflag) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += vedioid.hashCode();
		_h_ += rosenum;
		_h_ += roseflag;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append("T").append(vedioid.length()).append(",");
		_sb_.append(rosenum).append(",");
		_sb_.append(roseflag).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

