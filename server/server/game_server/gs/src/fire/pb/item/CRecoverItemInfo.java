
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRecoverItemInfo__ extends mkio.Protocol { }

/** 客户端请求查看一个找回道具的信息
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CRecoverItemInfo extends __CRecoverItemInfo__ {
	@Override
	protected void process() {
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;

		new mkdb.Procedure() {
			@Override
			protected boolean process() {

				// 获得可回收uniqId列表
				xbean.Itemrecoverlist itemRecoverList = xtable.Itemrecover.select(roleId);
				if (itemRecoverList == null) {
					return false;
				}

				// 是否存在可回收的uniqId
				if (itemRecoverList.getUniqids().contains(uniqid) == false) {
					return false;
				}

				// 获得丢弃的道具
				xbean.DiscardItem ditem = xtable.Itemrecyclebin.select(uniqid);
				if (ditem == null) {
					return false;
				}

				ItemBase bi = Module.getInstance().getItemManager().toItemBase(ditem.getItem());
				if (bi == null) {
					return false;
				}

				// 发送给客户端
				SRecoverItemInfo send = new SRecoverItemInfo();
				send.uniqid = uniqid;
				com.locojoy.base.Octets tips = bi.getTips();
				if (tips != null) {
					send.tips = tips;
				}
				mkdb.Procedure.psendWhileCommit(roleId, send);
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787797;

	public int getType() {
		return 787797;
	}

	public long uniqid; // 唯一id

	public CRecoverItemInfo() {
	}

	public CRecoverItemInfo(long _uniqid_) {
		this.uniqid = _uniqid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(uniqid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		uniqid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRecoverItemInfo) {
			CRecoverItemInfo _o_ = (CRecoverItemInfo)_o1_;
			if (uniqid != _o_.uniqid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)uniqid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(uniqid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRecoverItemInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(uniqid - _o_.uniqid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

