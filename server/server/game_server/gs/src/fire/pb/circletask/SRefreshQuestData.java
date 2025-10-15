
package fire.pb.circletask;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SRefreshQuestData__ extends mkio.Protocol { }

/** 任务数据发生变化时，服务器向客户端发送的刷新消息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SRefreshQuestData extends __SRefreshQuestData__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 807438;

	public int getType() {
		return 807438;
	}

	public int questid; // 刷新的任务id
	public java.util.HashMap<Integer,Long> datas; // 变量值ID，变量值

	public SRefreshQuestData() {
		datas = new java.util.HashMap<Integer,Long>();
	}

	public SRefreshQuestData(int _questid_, java.util.HashMap<Integer,Long> _datas_) {
		this.questid = _questid_;
		this.datas = _datas_;
	}

	public final boolean _validator_() {
		if (questid <= 0) return false;
		for (java.util.Map.Entry<Integer, Long> _e_ : datas.entrySet()) {
			if (_e_.getKey().intValue() <= 0) return false;
		}
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(questid);
		_os_.compact_uint32(datas.size());
		for (java.util.Map.Entry<Integer, Long> _e_ : datas.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questid = _os_.unmarshal_int();
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			long _v_;
			_v_ = _os_.unmarshal_long();
			datas.put(_k_, _v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SRefreshQuestData) {
			SRefreshQuestData _o_ = (SRefreshQuestData)_o1_;
			if (questid != _o_.questid) return false;
			if (!datas.equals(_o_.datas)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questid;
		_h_ += datas.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questid).append(",");
		_sb_.append(datas).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

