
package fire.pb.pet;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CPetRecoverList__ extends mkio.Protocol { }

/** 客户端请求宠物找回列表
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CPetRecoverList extends __CPetRecoverList__ {
	@Override
	protected void process() {
		final long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				// 发送宠物找回列表
				SPetRecoverList send = new SPetRecoverList();

				// 通过key在回收站中找到对应的宠物
				xbean.Petrecoverlist petRecoverList = xtable.Petrecover.get(roleId);
				if (petRecoverList != null) {
					java.util.List<Long> removeList = new java.util.LinkedList<Long>();
					long now = System.currentTimeMillis();
					for (Long uniqId : petRecoverList.getUniqids()) {
						xbean.DiscardPet dpet = xtable.Petrecyclebin.select(uniqId);
						if (dpet != null) {
							// 判断是否过期,如果过期就不发
							long day = fire.pb.pet.Module.OUTTIME_OF_PET; // 保留时间:天数
							long validTime = day * 24L * 3600L * 1000L; // 保留时间:毫秒数
							long elapseTime = now - dpet.getDeletedate(); // 过去时间:毫秒数
							long remainTime = validTime - elapseTime; // 剩余时间:毫秒数
							if (remainTime > 0) {
								int cost = -1; // -1:表示没有这个宠物的配置
								PetAttr petAttrConf = Module.getInstance().getPetManager().getAttr(dpet.getPet().getId());
								if (petAttrConf != null) {
									cost = petAttrConf.getRecovercost();
								}
								PetRecoverInfoBean info = new PetRecoverInfoBean();
								info.petid = dpet.getPet().getId();
								info.uniqid = uniqId;
								info.remaintime = (int) (remainTime / 1000); // 剩余时间:秒数
								info.cost = cost;
								send.pets.add(info);
							}
						} else {
							// 移除无效的
							removeList.add(uniqId);
						}
					}
					// 移除无效的列表
					petRecoverList.getUniqids().removeAll(removeList);
				}
				mkdb.Procedure.psendWhileCommit(roleId, send);
				return true;
			}
		}.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 788583;

	public int getType() {
		return 788583;
	}


	public CPetRecoverList() {
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
		if (_o1_ instanceof CPetRecoverList) {
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

	public int compareTo(CPetRecoverList _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

