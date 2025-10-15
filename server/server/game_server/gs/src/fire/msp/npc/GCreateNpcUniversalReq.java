
package fire.msp.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GCreateNpcUniversalReq__ extends mkio.Protocol { }

/** GS 发送地图模块 请求创建NPC
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GCreateNpcUniversalReq extends __GCreateNpcUniversalReq__ {
	@Override
	protected void process() {
		// protocol handle
		
		if (!npclist.isEmpty()){
			// 创建NPC
			java.util.List<fire.msp.npc.NpcInfo> resultlist = new java.util.LinkedList<fire.msp.npc.NpcInfo>();
			
			for (CreateNpcInfoUniverse info:npclist){
				java.util.List<fire.msp.npc.NpcInfo> rlist = fire.pb.scene.manager.SceneNpcManager.getInstance().createNpc(info);
				// 失败返回GS
				if (rlist == null || rlist.isEmpty() || rlist.size() < info.num){
					MCreateNpcUniversalRet mret = new MCreateNpcUniversalRet();
					mret.interfaceid = interfaceid;
					mret.result = 0;
					fire.pb.scene.SceneClient.pSend(mret);
					break;
				}
				// 成功
				resultlist.addAll(rlist);
			}
			//同步到GS NPC的信息
			MUpdateNpcInfo mUpdateNpcInfo = new MUpdateNpcInfo();
			mUpdateNpcInfo.npcs.addAll(resultlist);
			fire.pb.scene.SceneClient.pSend(mUpdateNpcInfo);
			
			if (interfaceid > 0){
				MCreateNpcUniversalRet mret = new MCreateNpcUniversalRet();
				mret.interfaceid = interfaceid;
				mret.result = 1;
				mret.npclist.addAll(resultlist);
				fire.pb.scene.SceneClient.pSend(mret);
			}
		}
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730416;

	public int getType() {
		return 730416;
	}

	public long interfaceid; // 逻辑接口ID，不需要返回的，interfaceid = 0
	public java.util.LinkedList<fire.msp.npc.CreateNpcInfoUniverse> npclist; // GS召NPC的要求条件
	public int toufangareatype; // 0-普通的投放区域,1-新的投放区域,包含1级轻功

	public GCreateNpcUniversalReq() {
		npclist = new java.util.LinkedList<fire.msp.npc.CreateNpcInfoUniverse>();
	}

	public GCreateNpcUniversalReq(long _interfaceid_, java.util.LinkedList<fire.msp.npc.CreateNpcInfoUniverse> _npclist_, int _toufangareatype_) {
		this.interfaceid = _interfaceid_;
		this.npclist = _npclist_;
		this.toufangareatype = _toufangareatype_;
	}

	public final boolean _validator_() {
		if (interfaceid < 0) return false;
		for (fire.msp.npc.CreateNpcInfoUniverse _v_ : npclist)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(interfaceid);
		_os_.compact_uint32(npclist.size());
		for (fire.msp.npc.CreateNpcInfoUniverse _v_ : npclist) {
			_os_.marshal(_v_);
		}
		_os_.marshal(toufangareatype);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		interfaceid = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.msp.npc.CreateNpcInfoUniverse _v_ = new fire.msp.npc.CreateNpcInfoUniverse();
			_v_.unmarshal(_os_);
			npclist.add(_v_);
		}
		toufangareatype = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GCreateNpcUniversalReq) {
			GCreateNpcUniversalReq _o_ = (GCreateNpcUniversalReq)_o1_;
			if (interfaceid != _o_.interfaceid) return false;
			if (!npclist.equals(_o_.npclist)) return false;
			if (toufangareatype != _o_.toufangareatype) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)interfaceid;
		_h_ += npclist.hashCode();
		_h_ += toufangareatype;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(interfaceid).append(",");
		_sb_.append(npclist).append(",");
		_sb_.append(toufangareatype).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

