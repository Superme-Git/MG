
package fire.pb.battle.livedie;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SLiveDieBattleWatchView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SLiveDieBattleWatchView extends __SLiveDieBattleWatchView__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793841;

	public int getType() {
		return 793841;
	}

	public java.util.ArrayList<fire.pb.battle.livedie.LDRoleInfoWatchDes> rolewatchlist; // 观战对手信息

	public SLiveDieBattleWatchView() {
		rolewatchlist = new java.util.ArrayList<fire.pb.battle.livedie.LDRoleInfoWatchDes>();
	}

	public SLiveDieBattleWatchView(java.util.ArrayList<fire.pb.battle.livedie.LDRoleInfoWatchDes> _rolewatchlist_) {
		this.rolewatchlist = _rolewatchlist_;
	}

	public final boolean _validator_() {
		for (fire.pb.battle.livedie.LDRoleInfoWatchDes _v_ : rolewatchlist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(rolewatchlist.size());
		for (fire.pb.battle.livedie.LDRoleInfoWatchDes _v_ : rolewatchlist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.livedie.LDRoleInfoWatchDes _v_ = new fire.pb.battle.livedie.LDRoleInfoWatchDes();
			_v_.unmarshal(_os_);
			rolewatchlist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SLiveDieBattleWatchView) {
			SLiveDieBattleWatchView _o_ = (SLiveDieBattleWatchView)_o1_;
			if (!rolewatchlist.equals(_o_.rolewatchlist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += rolewatchlist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(rolewatchlist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

