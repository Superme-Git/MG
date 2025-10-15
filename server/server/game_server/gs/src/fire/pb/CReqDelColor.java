
package fire.pb;
import org.apache.log4j.Logger;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CReqDelColor__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CReqDelColor extends __CReqDelColor__ {
	public static final Logger logger = Logger.getLogger("SYSTEM");
	@Override
	protected void process() {
//		删除衣柜配色方案
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if(roleid < 0) {
			return;
		}
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				xbean.Properties prop=xtable.Properties.get(roleid);
				if(prop==null){
					return false;
				}
				java.util.List<xbean.RoleColorType> list=prop.getColorroomlist();
				if(removeindex<0||removeindex>=list.size()){
					return false;
				}
				list.remove(removeindex);
				
//				SReqDelColor sReqDelColor=new SReqDelColor();
//				gnet.link.Onlines.getInstance().send(roleid, sReqDelColor);
				CReqColorRoomView.sendColorRoomInfo(roleid);
				logger.info("角色id "+roleid+"删除衣橱的染色方案 "+removeindex);
				return true;
			}
		}.submit();
		
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 786536;

	public int getType() {
		return 786536;
	}

	public int removeindex; // 删除索引

	public CReqDelColor() {
	}

	public CReqDelColor(int _removeindex_) {
		this.removeindex = _removeindex_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(removeindex);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		removeindex = _os_.unmarshal_int();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CReqDelColor) {
			CReqDelColor _o_ = (CReqDelColor)_o1_;
			if (removeindex != _o_.removeindex) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += removeindex;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(removeindex).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CReqDelColor _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = removeindex - _o_.removeindex;
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

