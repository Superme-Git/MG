
package fire.pb.item;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CItemRecoverList__ extends mkio.Protocol { }

/** 客户端请求道具找回列表
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CItemRecoverList extends __CItemRecoverList__ {
	@Override
	protected void process() {
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;

		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				// 发送道具找回列表
				SItemRecoverList send = new SItemRecoverList();

				// 通过key在回收站中找到对应的道具
				xbean.Itemrecoverlist itemRecoverList = xtable.Itemrecover.get(roleId);
				if (itemRecoverList != null) {
					long now = System.currentTimeMillis();
					java.util.List<Long> removeList = new java.util.LinkedList<Long>();
					for (Long uniqId : itemRecoverList.getUniqids()) {
						xbean.DiscardItem ditem = xtable.Itemrecyclebin.select(uniqId);
						if (ditem != null) {
//							// 判断是否过期,如果过期就不发
//							fire.pb.item.ItemShuXing attr = Module.getInstance().getItemManager().getAttr(ditem.getItem().getId());
//							if (attr == null) {
//								Module.logger.error("[CItemRecoverList] roleId:" + roleId
//										+ " itemId:" + ditem.getItem().getId()
//										+ " uniqId:" + ditem.getItem().getUniqueid()
//										+ " ItemShuXing not found!");
//								continue;
//							}
//							long day = attr.getRecycletime(); // 保留时间:天数
//							long validTime = day * 24L * 3600L * 1000L; // 保留时间:毫秒数
//							long elapseTime = now - ditem.getDeletedate(); // 过去时间:毫秒数
//							long remainTime = validTime - elapseTime; // 剩余时间:毫秒数
							long remainTime = ditem.getDeletedate() - now;
							if (remainTime > 0) {
								int cost = -1; // -1:表示没有这个道具的配置
								ItemShuXing itemAttrConf = Module.getInstance().getItemManager().getAttr(ditem.getItem().getId());
								if (itemAttrConf != null) {
									cost = itemAttrConf.getRecovercost();
								}
								ItemRecoverInfoBean info = new ItemRecoverInfoBean();
								info.itemid = ditem.getItem().getId();
								info.uniqid = uniqId;
								info.remaintime = (int) (remainTime / 1000); // 剩余时间:秒数
								info.cost = cost;
								send.items.add(info);
							}
						} else {
							// 移除无效的
							removeList.add(uniqId);
						}
					}
					// 移除无效的列表
					itemRecoverList.getUniqids().removeAll(removeList);
				}
				mkdb.Procedure.psendWhileCommit(roleId, send);
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 787793;

	public int getType() {
		return 787793;
	}


	public CItemRecoverList() {
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CItemRecoverList) {
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CItemRecoverList _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

