
package fire.pb.circletask.anye;
import fire.pb.circletask.CircleTaskManager;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CAbandonAnYe__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CAbandonAnYe extends __CAbandonAnYe__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		CircleTaskManager.logger.info("收到放弃暗夜任务消息.角色[" + roleid + "]要放弃暗夜任务:" + questid);
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				RoleAnYeTask rayt = new RoleAnYeTask(roleid);
				rayt.abandonTask();
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807460;

	public int getType() {
		return 807460;
	}

	public int questid; // 放弃的任务id(暗夜马戏团1080000)

	public CAbandonAnYe() {
	}

	public CAbandonAnYe(int _questid_) {
		this.questid = _questid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(questid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CAbandonAnYe) {
			CAbandonAnYe _o_ = (CAbandonAnYe)_o1_;
			if (questid != _o_.questid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CAbandonAnYe _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = questid - _o_.questid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

