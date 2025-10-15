
package fire.pb.item;

import fire.pb.talk.MessageMgr;
import fire.pb.util.CheckName;
import java.io.UnsupportedEncodingException;
import java.util.Map;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CModifyDepotName__ extends mkio.Protocol { }

/** 修改仓库名称
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CModifyDepotName extends __CModifyDepotName__ {
	@Override
	protected void process() {
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;

		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.Properties prop = xtable.Properties.get(roleId);
				if (prop == null) {
					return false;
				}
				
				int length = 0;
				try {
					length = (new String(depotname.getBytes("gb2312"), "ISO-8859-1")).length();
				} catch (UnsupportedEncodingException e) {
					return false;
				}

				if (length > 10) {
					MessageMgr.psendMsgNotifyWhileRollback(roleId, 145627, null);
					return false;
				} else if (length < 2) {
					MessageMgr.psendMsgNotifyWhileRollback(roleId, 145627, null);
					return false;
				}
				
				int resultCode = fire.pb.util.CheckName.checkValid(depotname);
				if (resultCode != CheckName.WORD_LEGAL) {
					MessageMgr.psendMsgNotifyWhileRollback(roleId, 144663, null);
					return false;
				}
				
				Map<Integer, String> depotnameinfo = prop.getDepotnameinfo();
				if(depotnameinfo != null){
					depotnameinfo.put(depotindex, depotname);
					SModifyDepotName sModifyDepotName = new SModifyDepotName(0, depotindex, depotname);
					psendWhileCommit(roleId, sModifyDepotName);
					return true;
				}
				return false;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787771;

	public int getType() {
		return 787771;
	}

	public int depotindex;
	public java.lang.String depotname;

	public CModifyDepotName() {
		depotname = "";
	}

	public CModifyDepotName(int _depotindex_, java.lang.String _depotname_) {
		this.depotindex = _depotindex_;
		this.depotname = _depotname_;
	}

	public final boolean _validator_() {
		if (depotindex < 1) return false;
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(depotindex);
		_os_.marshal(depotname, "UTF-16LE");
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		depotindex = _os_.unmarshal_int();
		depotname = _os_.unmarshal_String("UTF-16LE");
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CModifyDepotName) {
			CModifyDepotName _o_ = (CModifyDepotName)_o1_;
			if (depotindex != _o_.depotindex) return false;
			if (!depotname.equals(_o_.depotname)) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += depotindex;
		_h_ += depotname.hashCode();
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(depotindex).append(",");
		_sb_.append("T").append(depotname.length()).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}
