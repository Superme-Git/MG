
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SSubmit2Npc__ extends mkio.Protocol { }

/** 通知客户端提交任务物品或者任务宠物
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SSubmit2Npc extends __SSubmit2Npc__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795455;

	public int getType() {
		return 795455;
	}

	public int questid; // 提交的相关的任务或者其他相关id
	public long npckey; // 提交的npc
	public int submittype; // 提交类型: 道具0 或者宠物1
	public java.util.ArrayList<Integer> availableids; // 可以提交的道具ID 或者宠物ID
	public int availablepos; // 开放提交的格子数量

	public SSubmit2Npc() {
		availableids = new java.util.ArrayList<Integer>();
	}

	public SSubmit2Npc(int _questid_, long _npckey_, int _submittype_, java.util.ArrayList<Integer> _availableids_, int _availablepos_) {
		this.questid = _questid_;
		this.npckey = _npckey_;
		this.submittype = _submittype_;
		this.availableids = _availableids_;
		this.availablepos = _availablepos_;
	}

	public final boolean _validator_() {
		if (questid < 0) return false;
		if (npckey <= 0) return false;
		if (submittype < 0) return false;
		for (Integer _v_ : availableids)
			if (_v_.intValue() < 0) return false;
		if (availablepos < 0 || availablepos > 5) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(questid);
		_os_.marshal(npckey);
		_os_.marshal(submittype);
		_os_.compact_uint32(availableids.size());
		for (Integer _v_ : availableids) {
			_os_.marshal(_v_);
		}
		_os_.marshal(availablepos);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questid = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		submittype = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			availableids.add(_v_);
		}
		availablepos = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SSubmit2Npc) {
			SSubmit2Npc _o_ = (SSubmit2Npc)_o1_;
			if (questid != _o_.questid) return false;
			if (npckey != _o_.npckey) return false;
			if (submittype != _o_.submittype) return false;
			if (!availableids.equals(_o_.availableids)) return false;
			if (availablepos != _o_.availablepos) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questid;
		_h_ += (int)npckey;
		_h_ += submittype;
		_h_ += availableids.hashCode();
		_h_ += availablepos;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(submittype).append(",");
		_sb_.append(availableids).append(",");
		_sb_.append(availablepos).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

