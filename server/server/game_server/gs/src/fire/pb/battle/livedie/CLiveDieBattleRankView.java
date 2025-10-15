
package fire.pb.battle.livedie;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CLiveDieBattleRankView__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CLiveDieBattleRankView extends __CLiveDieBattleRankView__ {
	@Override
	protected void process() {
		// 请求生死战排行界面
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleid < 0) {
			return;
		}
		new PLiveDieBattleRankView(roleid, modeltype).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793842;

	public int getType() {
		return 793842;
	}

	public int modeltype; // 类型

	public CLiveDieBattleRankView() {
	}

	public CLiveDieBattleRankView(int _modeltype_) {
		this.modeltype = _modeltype_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(modeltype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		modeltype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CLiveDieBattleRankView) {
			CLiveDieBattleRankView _o_ = (CLiveDieBattleRankView)_o1_;
			if (modeltype != _o_.modeltype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += modeltype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CLiveDieBattleRankView _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = modeltype - _o_.modeltype;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

