package fire.pb.friends;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CXshGiveGift__ extends mkio.Protocol { }

/** 赠送说不得大师礼物
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
public class CXshGiveGift extends __CXshGiveGift__ {

	@Override
	protected void process() {
		final long selfRoleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (selfRoleId < 0)
			return;
		PXshGiveGift proc = new PXshGiveGift(selfRoleId, XSHUtils.SXH_ROLE_ID, itemid, itemnum, content, force);
		proc.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 806649;

	public int getType() {
		return 806649;
	}

	public int itemid; // 赠送道具的id
	public byte itemnum; // 赠送道具的数量
	public java.lang.String content; // 附加留言
	public byte force; // 0:赠送(如果不是双向好友,服务器返回错误,需要二次确认是否强行赠送) 1:强行赠送(非双向好友时赠送)

	public CXshGiveGift() {
		content = "";
	}

	public CXshGiveGift(int _itemid_, byte _itemnum_, java.lang.String _content_, byte _force_) {
		this.itemid = _itemid_;
		this.itemnum = _itemnum_;
		this.content = _content_;
		this.force = _force_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(itemid);
		_os_.marshal(itemnum);
		_os_.marshal(content, "UTF-16LE");
		_os_.marshal(force);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		itemid = _os_.unmarshal_int();
		itemnum = _os_.unmarshal_byte();
		content = _os_.unmarshal_String("UTF-16LE");
		force = _os_.unmarshal_byte();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CXshGiveGift) {
			CXshGiveGift _o_ = (CXshGiveGift)_o1_;
			if (itemid != _o_.itemid) return false;
			if (itemnum != _o_.itemnum) return false;
			if (!content.equals(_o_.content)) return false;
			if (force != _o_.force) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += itemid;
		_h_ += (int)itemnum;
		_h_ += content.hashCode();
		_h_ += (int)force;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(itemid).append(",");
		_sb_.append(itemnum).append(",");
		_sb_.append("T").append(content.length()).append(",");
		_sb_.append(force).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}
}
