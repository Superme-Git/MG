
package fire.msp.npc;
import fire.pb.instancezone.faction.PCheckMapNpcs;
// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __MCountNpcSize__ extends mkio.Protocol { }

/** 统计动态场景里npc的数量后执行的存储过程操作
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class MCountNpcSize extends __MCountNpcSize__ {
	@Override
	protected void process() {
		// protocol handle
		fire.pb.instancezone.faction.PCheckMapNpcs checkNpc = new PCheckMapNpcs(ownerid, npcnum, saveid);
		if (mkdb.Transaction.current() != null) {
			checkNpc.call();
		}
		else {
			checkNpc.submit();
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 730442;

	public int getType() {
		return 730442;
	}

	public int npcnum;
	public long ownerid;
	public int saveid;

	public MCountNpcSize() {
	}

	public MCountNpcSize(int _npcnum_, long _ownerid_, int _saveid_) {
		this.npcnum = _npcnum_;
		this.ownerid = _ownerid_;
		this.saveid = _saveid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(npcnum);
		_os_.marshal(ownerid);
		_os_.marshal(saveid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		npcnum = _os_.unmarshal_int();
		ownerid = _os_.unmarshal_long();
		saveid = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof MCountNpcSize) {
			MCountNpcSize _o_ = (MCountNpcSize)_o1_;
			if (npcnum != _o_.npcnum) return false;
			if (ownerid != _o_.ownerid) return false;
			if (saveid != _o_.saveid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += npcnum;
		_h_ += (int)ownerid;
		_h_ += saveid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(npcnum).append(",");
		_sb_.append(ownerid).append(",");
		_sb_.append(saveid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(MCountNpcSize _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = npcnum - _o_.npcnum;
		if (0 != _c_) return _c_;
		_c_ = Long.signum(ownerid - _o_.ownerid);
		if (0 != _c_) return _c_;
		_c_ = saveid - _o_.saveid;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

