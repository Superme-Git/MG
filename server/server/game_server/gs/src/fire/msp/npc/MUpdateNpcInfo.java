
package fire.msp.npc;

import fire.pb.map.SceneNpcManager;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MUpdateNpcInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MUpdateNpcInfo extends __MUpdateNpcInfo__ {
	@Override
	protected void process() {
		// protocol handle

		new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				for (NpcInfo createinfo : npcs)
				{
					SceneNpcManager.getInstance().createAddNewNpc(createinfo);

				}

				// 如果需要在npc创建后触发一些事件,可以在这个procedure里面写
				pexecuteWhileCommit(new PExecuteAfterNpcCreate(npcs));
				return true;
			}
		}.submit();

	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730397;

	public int getType() {
		return 730397;
	}

	public java.util.LinkedList<fire.msp.npc.NpcInfo> npcs;

	public MUpdateNpcInfo() {
		npcs = new java.util.LinkedList<fire.msp.npc.NpcInfo>();
	}

	public MUpdateNpcInfo(java.util.LinkedList<fire.msp.npc.NpcInfo> _npcs_) {
		this.npcs = _npcs_;
	}

	public final boolean _validator_() {
		for (fire.msp.npc.NpcInfo _v_ : npcs)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.compact_uint32(npcs.size());
		for (fire.msp.npc.NpcInfo _v_ : npcs) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.msp.npc.NpcInfo _v_ = new fire.msp.npc.NpcInfo();
			_v_.unmarshal(_os_);
			npcs.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MUpdateNpcInfo) {
			MUpdateNpcInfo _o_ = (MUpdateNpcInfo)_o1_;
			if (!npcs.equals(_o_.npcs)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npcs.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcs).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

