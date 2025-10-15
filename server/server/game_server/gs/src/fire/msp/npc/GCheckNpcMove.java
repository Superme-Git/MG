
package fire.msp.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCheckNpcMove__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCheckNpcMove extends __GCheckNpcMove__ {
	@Override
	protected void process() {
		//fire.pb.scene.manager.NpcMoveAIManager.getInstance().process(tick);
		fire.pb.scene.npcai.AIManager.getInstance().process(tick);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730431;

	public int getType() {
		return 730431;
	}

	public long tick;

	public GCheckNpcMove() {
	}

	public GCheckNpcMove(long _tick_) {
		this.tick = _tick_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(tick);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		tick = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCheckNpcMove) {
			GCheckNpcMove _o_ = (GCheckNpcMove)_o1_;
			if (tick != _o_.tick) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)tick;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(tick).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(GCheckNpcMove _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(tick - _o_.tick);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

