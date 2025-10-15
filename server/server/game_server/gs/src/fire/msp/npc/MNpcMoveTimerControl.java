
package fire.msp.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MNpcMoveTimerControl__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MNpcMoveTimerControl extends __MNpcMoveTimerControl__ {
	@Override
	protected void process() {
		if(action == START)
		{
			fire.pb.npc.NpcAI.NpcAITimer.getInstance().start();
			return;
		}
		
		fire.pb.npc.NpcAI.NpcAITimer.getInstance().end();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730434;

	public int getType() {
		return 730434;
	}

	public final static int END = 0;
	public final static int START = 1;

	public int action;

	public MNpcMoveTimerControl() {
	}

	public MNpcMoveTimerControl(int _action_) {
		this.action = _action_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(action);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		action = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MNpcMoveTimerControl) {
			MNpcMoveTimerControl _o_ = (MNpcMoveTimerControl)_o1_;
			if (action != _o_.action) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += action;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(action).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MNpcMoveTimerControl _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = action - _o_.action;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

