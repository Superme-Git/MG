
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSubmit2Npc__ extends mkio.Protocol { }

/** 客户端请求提交任务物品或者任务宠物
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSubmit2Npc extends __CSubmit2Npc__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795456;

	public int getType() {
		return 795456;
	}

	public int questid; // 提交物品相关的任务或者其他相关id
	public long npckey; // npckey
	public int submittype; // 提交的类型
	public java.util.ArrayList<fire.pb.npc.SubmitUnit> things; // 提交的道具key 或者宠物的key

	public CSubmit2Npc() {
		things = new java.util.ArrayList<fire.pb.npc.SubmitUnit>();
	}

	public CSubmit2Npc(int _questid_, long _npckey_, int _submittype_, java.util.ArrayList<fire.pb.npc.SubmitUnit> _things_) {
		this.questid = _questid_;
		this.npckey = _npckey_;
		this.submittype = _submittype_;
		this.things = _things_;
	}

	public final boolean _validator_() {
		if (questid < 0) return false;
		if (npckey < 0) return false;
		if (submittype < 0) return false;
		for (fire.pb.npc.SubmitUnit _v_ : things)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(questid);
		_os_.marshal(npckey);
		_os_.marshal(submittype);
		_os_.compact_uint32(things.size());
		for (fire.pb.npc.SubmitUnit _v_ : things) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		questid = _os_.unmarshal_int();
		npckey = _os_.unmarshal_long();
		submittype = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.npc.SubmitUnit _v_ = new fire.pb.npc.SubmitUnit();
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
		if (_o1_ instanceof CSubmit2Npc) {
			CSubmit2Npc _o_ = (CSubmit2Npc)_o1_;
			if (questid != _o_.questid) return false;
			if (npckey != _o_.npckey) return false;
			if (submittype != _o_.submittype) return false;
			if (!things.equals(_o_.things)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += questid;
		_h_ += (int)npckey;
		_h_ += submittype;
		_h_ += things.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(questid).append(",");
		_sb_.append(npckey).append(",");
		_sb_.append(submittype).append(",");
		_sb_.append(things).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

