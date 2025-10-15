
package fire.msp.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MCreateNpcUniversalRet__ extends mkio.Protocol { }

/** 地图模块，返回给GS的创建NPC的详细信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MCreateNpcUniversalRet extends __MCreateNpcUniversalRet__ {
	@Override
	protected void process() {
		// protocol handle
		new mkdb.Procedure() {
			public boolean process(){
				final xbean.CreateNpcExecType cnbean = xtable.Gmcreatenpcuniversal.get(interfaceid);
				if (null == cnbean)
					return false;
				cnbean.getExecinstance().exec(result, npclist);
				xtable.Gmcreatenpcuniversal.remove(interfaceid);
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730417;

	public int getType() {
		return 730417;
	}

	public long interfaceid; // 逻辑接口ID，ID > 0
	public int result; // 召唤NPC结果，0，为失败，1为成功
	public java.util.LinkedList<fire.msp.npc.NpcInfo> npclist; // 地图模块召出的NPC详细信息

	public MCreateNpcUniversalRet() {
		npclist = new java.util.LinkedList<fire.msp.npc.NpcInfo>();
	}

	public MCreateNpcUniversalRet(long _interfaceid_, int _result_, java.util.LinkedList<fire.msp.npc.NpcInfo> _npclist_) {
		this.interfaceid = _interfaceid_;
		this.result = _result_;
		this.npclist = _npclist_;
	}

	public final boolean _validator_() {
		if (interfaceid <= 0) return false;
		if (result < 0 || result > 1) return false;
		for (fire.msp.npc.NpcInfo _v_ : npclist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(interfaceid);
		_os_.marshal(result);
		_os_.compact_uint32(npclist.size());
		for (fire.msp.npc.NpcInfo _v_ : npclist) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		interfaceid = _os_.unmarshal_long();
		result = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.msp.npc.NpcInfo _v_ = new fire.msp.npc.NpcInfo();
			_v_.unmarshal(_os_);
			npclist.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MCreateNpcUniversalRet) {
			MCreateNpcUniversalRet _o_ = (MCreateNpcUniversalRet)_o1_;
			if (interfaceid != _o_.interfaceid) return false;
			if (result != _o_.result) return false;
			if (!npclist.equals(_o_.npclist)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)interfaceid;
		_h_ += result;
		_h_ += npclist.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(interfaceid).append(",");
		_sb_.append(result).append(",");
		_sb_.append(npclist).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

