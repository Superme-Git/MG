
package fire.pb.shop;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CMarketContainerBrowse__ extends mkio.Protocol { }

/** 摆摊容器浏览
*/
// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CMarketContainerBrowse extends __CMarketContainerBrowse__ {
	@Override
	protected void process() {
		// protocol handle
		long roleId = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleId < 0)
			return;
		if (!fire.pb.shop.srv.market.Market.useMarketCheck(roleId))
			return;
		
/*		
		fire.pb.item.ItemColumn itemcol = fire.pb.item.Module.getInstance().getItemColumn(roleId, fire.pb.item.BagTypes.MARKET, true);
		fire.pb.pet.PetColumn petcol = new fire.pb.pet.PetColumn(roleId, fire.pb.pet.PetColumnTypes.MARKETPET,true);
		SMarketContainerBrowse browse = new SMarketContainerBrowse();
		browse.items = itemcol.getBagInfo().items;
		browse.pets = petcol.getAllProtocolPets();
		gnet.link.Onlines.getInstance().send(roleId, browse);
		*/
		new mkdb.Procedure() {
			protected boolean process() {
				if (fire.pb.main.Gs.isMergeServer()) {
					// 查询表中此角色是否有取回临时拍卖道具的历史
					Integer val = xtable.Takebackmarketcontainertab.select(roleId);
					if (val == null) {
						fire.pb.shop.srv.market.MarketManager.getInstance().sendTempMarketContainer(roleId);
						if (fire.pb.shop.srv.market.MarketManager.LOG.isInfoEnabled()) {
							StringBuilder sbd = new StringBuilder();
							sbd.append("role=").append(roleId).append(", 浏览道具返回临时背包！");
							fire.pb.shop.srv.market.MarketManager.LOG.info(sbd.toString());
						}
						return true;
					}
				}
				fire.pb.shop.srv.market.MarketManager.getInstance().sendMarketContainerByRoleId(roleId);
				return true;
			}
		}.submit();
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 810647;

	public int getType() {
		return 810647;
	}


	public CMarketContainerBrowse() {
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
		if (_o1_ instanceof CMarketContainerBrowse) {
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

	public int compareTo(CMarketContainerBrowse _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

