
package fire.pb.battle;

import com.locojoy.base.Marshal.Marshal;
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

public class DemoExecute implements Marshal {
	public int attackerid; // 攻击发起者id
	public int hpconsume; // 攻击者耗血，始终为正好了
	public int mpconsume; // 攻击者耗蓝，始终为正好了
	public int spconsume; // 攻击者耗怒，始终为正好了
	public int operationtype; // 操作类型 参考OperationType中的值		如果是操作失败 ACTION_FAILURE
	public int operationid; // 操作的值（使用物品时示物品ID，招唤宠物时为宠物）
	public int msgid; // 当不为0时，为动作时的提示ID
	public java.util.LinkedList<fire.pb.battle.DemoBuff> demobuffs;

	public DemoExecute() {
		demobuffs = new java.util.LinkedList<fire.pb.battle.DemoBuff>();
	}

	public DemoExecute(int _attackerid_, int _hpconsume_, int _mpconsume_, int _spconsume_, int _operationtype_, int _operationid_, int _msgid_, java.util.LinkedList<fire.pb.battle.DemoBuff> _demobuffs_) {
		this.attackerid = _attackerid_;
		this.hpconsume = _hpconsume_;
		this.mpconsume = _mpconsume_;
		this.spconsume = _spconsume_;
		this.operationtype = _operationtype_;
		this.operationid = _operationid_;
		this.msgid = _msgid_;
		this.demobuffs = _demobuffs_;
	}

	public final boolean _validator_() {
		for (fire.pb.battle.DemoBuff _v_ : demobuffs)
			if (!_v_._validator_()) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		_os_.marshal(attackerid);
		_os_.marshal(hpconsume);
		_os_.marshal(mpconsume);
		_os_.marshal(spconsume);
		_os_.marshal(operationtype);
		_os_.marshal(operationid);
		_os_.marshal(msgid);
		_os_.compact_uint32(demobuffs.size());
		for (fire.pb.battle.DemoBuff _v_ : demobuffs) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		attackerid = _os_.unmarshal_int();
		hpconsume = _os_.unmarshal_int();
		mpconsume = _os_.unmarshal_int();
		spconsume = _os_.unmarshal_int();
		operationtype = _os_.unmarshal_int();
		operationid = _os_.unmarshal_int();
		msgid = _os_.unmarshal_int();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			fire.pb.battle.DemoBuff _v_ = new fire.pb.battle.DemoBuff();
			_v_.unmarshal(_os_);
			demobuffs.add(_v_);
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof DemoExecute) {
			DemoExecute _o_ = (DemoExecute)_o1_;
			if (attackerid != _o_.attackerid) return false;
			if (hpconsume != _o_.hpconsume) return false;
			if (mpconsume != _o_.mpconsume) return false;
			if (spconsume != _o_.spconsume) return false;
			if (operationtype != _o_.operationtype) return false;
			if (operationid != _o_.operationid) return false;
			if (msgid != _o_.msgid) return false;
			if (!demobuffs.equals(_o_.demobuffs)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += attackerid;
		_h_ += hpconsume;
		_h_ += mpconsume;
		_h_ += spconsume;
		_h_ += operationtype;
		_h_ += operationid;
		_h_ += msgid;
		_h_ += demobuffs.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(attackerid).append(",");
		_sb_.append(hpconsume).append(",");
		_sb_.append(mpconsume).append(",");
		_sb_.append(spconsume).append(",");
		_sb_.append(operationtype).append(",");
		_sb_.append(operationid).append(",");
		_sb_.append(msgid).append(",");
		_sb_.append(demobuffs).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

}

