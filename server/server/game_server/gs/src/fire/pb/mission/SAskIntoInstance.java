
package fire.pb.mission;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SAskIntoInstance__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SAskIntoInstance extends __SAskIntoInstance__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 805538;

	public int getType() {
		return 805538;
	}

	public int msgid; // 消息id
	public java.lang.String teamleadername; // 队长名字
	public int awardtimes; // 奖励剩余次数
	public int step; // 进度id
	public int tlstep; // 队长进度
	public int mystep; // 我的进度
	public int allstep; // 总进度
	public java.util.LinkedList<Integer> steplist; // 我的未完成进度
	public int insttype; // 0 日常副本; 1时光之穴
	public short autoenter; // 1表示自动确认, 0表示不自动确认

	public SAskIntoInstance() {
		teamleadername = "";
		steplist = new java.util.LinkedList<Integer>();
	}

	public SAskIntoInstance(int _msgid_, java.lang.String _teamleadername_, int _awardtimes_, int _step_, int _tlstep_, int _mystep_, int _allstep_, java.util.LinkedList<Integer> _steplist_, int _insttype_, short _autoenter_) {
		this.msgid = _msgid_;
		this.teamleadername = _teamleadername_;
		this.awardtimes = _awardtimes_;
		this.step = _step_;
		this.tlstep = _tlstep_;
		this.mystep = _mystep_;
		this.allstep = _allstep_;
		this.steplist = _steplist_;
		this.insttype = _insttype_;
		this.autoenter = _autoenter_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(msgid);
		_os_.marshal(teamleadername, "UTF-16LE");
		_os_.marshal(awardtimes);
		_os_.marshal(step);
		_os_.marshal(tlstep);
		_os_.marshal(mystep);
		_os_.marshal(allstep);
		_os_.compact_uint32(steplist.size());
		for (Integer _v_ : steplist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(insttype);
		_os_.marshal(autoenter);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		msgid = _os_.unmarshal_int();
		teamleadername = _os_.unmarshal_String("UTF-16LE");
		awardtimes = _os_.unmarshal_int();
		step = _os_.unmarshal_int();
		tlstep = _os_.unmarshal_int();
		mystep = _os_.unmarshal_int();
		allstep = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			steplist.add(_v_);
		}
		insttype = _os_.unmarshal_int();
		autoenter = _os_.unmarshal_short();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SAskIntoInstance) {
			SAskIntoInstance _o_ = (SAskIntoInstance)_o1_;
			if (msgid != _o_.msgid) return false;
			if (!teamleadername.equals(_o_.teamleadername)) return false;
			if (awardtimes != _o_.awardtimes) return false;
			if (step != _o_.step) return false;
			if (tlstep != _o_.tlstep) return false;
			if (mystep != _o_.mystep) return false;
			if (allstep != _o_.allstep) return false;
			if (!steplist.equals(_o_.steplist)) return false;
			if (insttype != _o_.insttype) return false;
			if (autoenter != _o_.autoenter) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += msgid;
		_h_ += teamleadername.hashCode();
		_h_ += awardtimes;
		_h_ += step;
		_h_ += tlstep;
		_h_ += mystep;
		_h_ += allstep;
		_h_ += steplist.hashCode();
		_h_ += insttype;
		_h_ += autoenter;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(msgid).append(",");
		_sb_.append("T").append(teamleadername.length()).append(",");
		_sb_.append(awardtimes).append(",");
		_sb_.append(step).append(",");
		_sb_.append(tlstep).append(",");
		_sb_.append(mystep).append(",");
		_sb_.append(allstep).append(",");
		_sb_.append(steplist).append(",");
		_sb_.append(insttype).append(",");
		_sb_.append(autoenter).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

