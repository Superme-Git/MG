
package fire.pb.fushi.redpack;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CSendRedPack__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CSendRedPack extends __CSendRedPack__ {
	@Override
	protected void process() {
		// protocol handle
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		final int userid = ((gnet.link.Dispatch) this.getContext()).userid;
		if(roleid < 0||userid<0) {
			return;
		}
		new PSendRedPack(userid,roleid,modeltype,fushinum,redpacknum,redpackdes).submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812534;

	public int getType() {
		return 812534;
	}

	public int modeltype; // 红包类型
	public int fushinum; // 符石数量
	public int redpacknum; // 红包数量
	public java.lang.String redpackdes; // 红包寄语

	public CSendRedPack() {
		redpackdes = "";
	}

	public CSendRedPack(int _modeltype_, int _fushinum_, int _redpacknum_, java.lang.String _redpackdes_) {
		this.modeltype = _modeltype_;
		this.fushinum = _fushinum_;
		this.redpacknum = _redpacknum_;
		this.redpackdes = _redpackdes_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(modeltype);
		_os_.marshal(fushinum);
		_os_.marshal(redpacknum);
		_os_.marshal(redpackdes, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		modeltype = _os_.unmarshal_int();
		fushinum = _os_.unmarshal_int();
		redpacknum = _os_.unmarshal_int();
		redpackdes = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CSendRedPack) {
			CSendRedPack _o_ = (CSendRedPack)_o1_;
			if (modeltype != _o_.modeltype) return false;
			if (fushinum != _o_.fushinum) return false;
			if (redpacknum != _o_.redpacknum) return false;
			if (!redpackdes.equals(_o_.redpackdes)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += modeltype;
		_h_ += fushinum;
		_h_ += redpacknum;
		_h_ += redpackdes.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(modeltype).append(",");
		_sb_.append(fushinum).append(",");
		_sb_.append(redpacknum).append(",");
		_sb_.append("T").append(redpackdes.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

