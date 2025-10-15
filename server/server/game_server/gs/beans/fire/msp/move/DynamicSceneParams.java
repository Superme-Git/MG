
package fire.msp.move;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class DynamicSceneParams implements Marshal {
	public int mazeid; // 地图取哪个maze
	public java.util.LinkedList<fire.msp.move.CreateNpcInfo> addnpcs; // 需要添加的npcs
	public java.util.LinkedList<Integer> delnpcs; // 需要删除的npcs
	public java.util.HashMap<Integer,Integer> npcstates; // key:npcid   value:show = 0,hide = 1

	public DynamicSceneParams() {
		addnpcs = new java.util.LinkedList<fire.msp.move.CreateNpcInfo>();
		delnpcs = new java.util.LinkedList<Integer>();
		npcstates = new java.util.HashMap<Integer,Integer>();
	}

	public DynamicSceneParams(int _mazeid_, java.util.LinkedList<fire.msp.move.CreateNpcInfo> _addnpcs_, java.util.LinkedList<Integer> _delnpcs_, java.util.HashMap<Integer,Integer> _npcstates_) {
		this.mazeid = _mazeid_;
		this.addnpcs = _addnpcs_;
		this.delnpcs = _delnpcs_;
		this.npcstates = _npcstates_;
	}

	public final boolean _validator_() {
		for (fire.msp.move.CreateNpcInfo _v_ : addnpcs)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(mazeid);
		_os_.compact_uint32(addnpcs.size());
		for (fire.msp.move.CreateNpcInfo _v_ : addnpcs) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(delnpcs.size());
		for (Integer _v_ : delnpcs) {
			_os_.marshal(_v_);
		}
		_os_.compact_uint32(npcstates.size());
		for (java.util.Map.Entry<Integer, Integer> _e_ : npcstates.entrySet()) {
			_os_.marshal(_e_.getKey());
			_os_.marshal(_e_.getValue());
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		mazeid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.msp.move.CreateNpcInfo _v_ = new fire.msp.move.CreateNpcInfo();
			_v_.unmarshal(_os_);
			addnpcs.add(_v_);
		}
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			int _v_;
			_v_ = _os_.unmarshal_int();
			delnpcs.add(_v_);
		}
		for (int size = _os_.uncompact_uint32(); size > 0; --size) {
			int _k_;
			_k_ = _os_.unmarshal_int();
			int _v_;
			_v_ = _os_.unmarshal_int();
			npcstates.put(_k_, _v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof DynamicSceneParams) {
			DynamicSceneParams _o_ = (DynamicSceneParams)_o1_;
			if (mazeid != _o_.mazeid) return false;
			if (!addnpcs.equals(_o_.addnpcs)) return false;
			if (!delnpcs.equals(_o_.delnpcs)) return false;
			if (!npcstates.equals(_o_.npcstates)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += mazeid;
		_h_ += addnpcs.hashCode();
		_h_ += delnpcs.hashCode();
		_h_ += npcstates.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(mazeid).append(",");
		_sb_.append(addnpcs).append(",");
		_sb_.append(delnpcs).append(",");
		_sb_.append(npcstates).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

