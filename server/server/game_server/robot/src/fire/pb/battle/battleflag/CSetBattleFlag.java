
package fire.pb.battle.battleflag;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSetBattleFlag__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSetBattleFlag extends __CSetBattleFlag__ {
	@Override
	protected void process() {
		// protocol handle
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 793889;

	public int getType() {
		return 793889;
	}

	public byte opttype; // 0 add, 1 delete, 2 modify 3 clearall
	public byte index; // fighterid
	public java.lang.String flag;

	public CSetBattleFlag() {
		flag = "";
	}

	public CSetBattleFlag(byte _opttype_, byte _index_, java.lang.String _flag_) {
		this.opttype = _opttype_;
		this.index = _index_;
		this.flag = _flag_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(opttype);
		_os_.marshal(index);
		_os_.marshal(flag, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		opttype = _os_.unmarshal_byte();
		index = _os_.unmarshal_byte();
		flag = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSetBattleFlag) {
			CSetBattleFlag _o_ = (CSetBattleFlag)_o1_;
			if (opttype != _o_.opttype) return false;
			if (index != _o_.index) return false;
			if (!flag.equals(_o_.flag)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)opttype;
		_h_ += (int)index;
		_h_ += flag.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(opttype).append(",");
		_sb_.append(index).append(",");
		_sb_.append("T").append(flag.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

