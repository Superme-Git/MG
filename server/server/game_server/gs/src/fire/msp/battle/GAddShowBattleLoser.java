
package fire.msp.battle;

import fire.pb.scene.movable.Monstershow;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __GAddShowBattleLoser__ extends mkio.Protocol { }

/** battle conduct protocol end
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class GAddShowBattleLoser extends __GAddShowBattleLoser__ {
	@Override
	protected void process() {
		// protocol handle
		Monstershow m = (Monstershow)
		fire.pb.scene.manager.SceneNpcManager.getInstance().getNpcByKey( npckey);
		m.addLoser(losers);
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 727903;

	public int getType() {
		return 727903;
	}

	public long npckey; // 地图上的npckey
	public java.util.LinkedList<Long> losers; // 失败者

	public GAddShowBattleLoser() {
		losers = new java.util.LinkedList<Long>();
	}

	public GAddShowBattleLoser(long _npckey_, java.util.LinkedList<Long> _losers_) {
		this.npckey = _npckey_;
		this.losers = _losers_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npckey);
		_os_.compact_uint32(losers.size());
		for (Long _v_ : losers) {
			_os_.marshal(_v_);
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npckey = _os_.unmarshal_long();
		for (int _size_ = _os_.uncompact_uint32(); _size_ > 0; --_size_) {
			long _v_;
			_v_ = _os_.unmarshal_long();
			losers.add(_v_);
		}
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof GAddShowBattleLoser) {
			GAddShowBattleLoser _o_ = (GAddShowBattleLoser)_o1_;
			if (npckey != _o_.npckey) return false;
			if (!losers.equals(_o_.losers)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)npckey;
		_h_ += losers.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npckey).append(",");
		_sb_.append(losers).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

