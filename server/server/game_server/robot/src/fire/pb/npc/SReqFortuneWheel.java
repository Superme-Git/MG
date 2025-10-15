
package fire.pb.npc;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __SReqFortuneWheel__ extends mkio.Protocol { }

/** 服务器发送抽奖
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class SReqFortuneWheel extends __SReqFortuneWheel__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 795445;

	public int getType() {
		return 795445;
	}

	public long npckey; // npckey为npc的唯一ID
	public int serviceid; // 当前转盘服务id
	public java.util.ArrayList<fire.pb.npc.ForturneWheelType> itemids; // 供选择的12个物品,可能是钱或经验
	public int index; // 最后获得的物品下标
	public byte flag; // 0 大转盘  1翻盘子

	public SReqFortuneWheel() {
		itemids = new java.util.ArrayList<fire.pb.npc.ForturneWheelType>();
	}

	public SReqFortuneWheel(long _npckey_, int _serviceid_, java.util.ArrayList<fire.pb.npc.ForturneWheelType> _itemids_, int _index_, byte _flag_) {
		this.npckey = _npckey_;
		this.serviceid = _serviceid_;
		this.itemids = _itemids_;
		this.index = _index_;
		this.flag = _flag_;
	}

	public final boolean _validator_() {
		if (npckey < 0) return false;
		for (fire.pb.npc.ForturneWheelType _v_ : itemids)
			if (!_v_._validator_()) return false;
		if (index < 0) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.marshal(serviceid);
		_os_.compact_uint32(itemids.size());
		for (fire.pb.npc.ForturneWheelType _v_ : itemids) {
			_os_.marshal(_v_);
		}
		_os_.marshal(index);
		_os_.marshal(flag);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		serviceid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.npc.ForturneWheelType _v_ = new fire.pb.npc.ForturneWheelType();
			_v_.unmarshal(_os_);
			itemids.add(_v_);
		}
		index = _os_.unmarshal_int();
		flag = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof SReqFortuneWheel) {
			SReqFortuneWheel _o_ = (SReqFortuneWheel)_o1_;
			if (npckey != _o_.npckey) return false;
			if (serviceid != _o_.serviceid) return false;
			if (!itemids.equals(_o_.itemids)) return false;
			if (index != _o_.index) return false;
			if (flag != _o_.flag) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += serviceid;
		_h_ += itemids.hashCode();
		_h_ += index;
		_h_ += (int)flag;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(serviceid).append(",");
		_sb_.append(itemids).append(",");
		_sb_.append(index).append(",");
		_sb_.append(flag).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

