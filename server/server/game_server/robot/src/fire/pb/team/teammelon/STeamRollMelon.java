
package fire.pb.team.teammelon;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __STeamRollMelon__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class STeamRollMelon extends __STeamRollMelon__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794522;

	public int getType() {
		return 794522;
	}

	public java.util.LinkedList<fire.pb.team.teammelon.RollMelon> melonlist; // 有可能是连续好几个 by changhao
	public int watcher; // 1是观察者0是能ROLL的 by changhao

	public STeamRollMelon() {
		melonlist = new java.util.LinkedList<fire.pb.team.teammelon.RollMelon>();
	}

	public STeamRollMelon(java.util.LinkedList<fire.pb.team.teammelon.RollMelon> _melonlist_, int _watcher_) {
		this.melonlist = _melonlist_;
		this.watcher = _watcher_;
	}

	public final boolean _validator_() {
		for (fire.pb.team.teammelon.RollMelon _v_ : melonlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(melonlist.size());
		for (fire.pb.team.teammelon.RollMelon _v_ : melonlist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(watcher);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.team.teammelon.RollMelon _v_ = new fire.pb.team.teammelon.RollMelon();
			_v_.unmarshal(_os_);
			melonlist.add(_v_);
		}
		watcher = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof STeamRollMelon) {
			STeamRollMelon _o_ = (STeamRollMelon)_o1_;
			if (!melonlist.equals(_o_.melonlist)) return false;
			if (watcher != _o_.watcher) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += melonlist.hashCode();
		_h_ += watcher;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(melonlist).append(",");
		_sb_.append(watcher).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

