
package fire.pb.circletask.anye;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSubmitThings__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSubmitThings extends __CSubmitThings__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807455;

	public int getType() {
		return 807455;
	}

	public int taskpos; // 任务栏位
	public int taskid; // 任务id
	public long taskrole; // 任务所属角色id(帮别人提交物品时用到)
	public int submittype; // 提交的类型 1道具 2宠物 3金钱
	public java.util.ArrayList<fire.pb.circletask.anye.SubmitThing> things; // 提交的道具key 或者宠物的key

	public CSubmitThings() {
		things = new java.util.ArrayList<fire.pb.circletask.anye.SubmitThing>();
	}

	public CSubmitThings(int _taskpos_, int _taskid_, long _taskrole_, int _submittype_, java.util.ArrayList<fire.pb.circletask.anye.SubmitThing> _things_) {
		this.taskpos = _taskpos_;
		this.taskid = _taskid_;
		this.taskrole = _taskrole_;
		this.submittype = _submittype_;
		this.things = _things_;
	}

	public final boolean _validator_() {
		if (taskpos < 0) return false;
		if (taskid < 0) return false;
		if (taskrole < 0) return false;
		if (submittype < 0) return false;
		for (fire.pb.circletask.anye.SubmitThing _v_ : things)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(taskpos);
		_os_.marshal(taskid);
		_os_.marshal(taskrole);
		_os_.marshal(submittype);
		_os_.compact_uint32(things.size());
		for (fire.pb.circletask.anye.SubmitThing _v_ : things) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		taskpos = _os_.unmarshal_int();
		taskid = _os_.unmarshal_int();
		taskrole = _os_.unmarshal_long();
		submittype = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.circletask.anye.SubmitThing _v_ = new fire.pb.circletask.anye.SubmitThing();
			_v_.unmarshal(_os_);
			things.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSubmitThings) {
			CSubmitThings _o_ = (CSubmitThings)_o1_;
			if (taskpos != _o_.taskpos) return false;
			if (taskid != _o_.taskid) return false;
			if (taskrole != _o_.taskrole) return false;
			if (submittype != _o_.submittype) return false;
			if (!things.equals(_o_.things)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += taskpos;
		_h_ += taskid;
		_h_ += (int)taskrole;
		_h_ += submittype;
		_h_ += things.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(taskpos).append(",");
		_sb_.append(taskid).append(",");
		_sb_.append(taskrole).append(",");
		_sb_.append(submittype).append(",");
		_sb_.append(things).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

