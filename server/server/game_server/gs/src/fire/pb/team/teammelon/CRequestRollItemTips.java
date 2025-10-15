
package fire.pb.team.teammelon;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestRollItemTips__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}
/***
 * 
 * 请求roll item tips 信息 by changhao
 *
 */
public class CRequestRollItemTips extends __CRequestRollItemTips__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		Long battlemelonid = xtable.Roleid2battlemelonid.select(roleid);
		if (battlemelonid != null)
		{
			xbean.ETeamMelon teammelon = xtable.Battlemelonid2melon.select(battlemelonid);
			if (teammelon != null)
			{
				xbean.TeamMelon melon = teammelon.getMelonid2melons().get(this.melonid);
				if (melon != null)
				{
					xbean.Item item = melon.getItemdata();
					if (item != null)
					{
						fire.pb.item.ItemBase basicitem = fire.pb.item.Module.getInstance().getItemManager().toItemBase(item, 0, 0, 0);
						if (basicitem != null)
						{
							fire.pb.talk.SChatItemTips msg = new fire.pb.talk.SChatItemTips();
							msg.tips = basicitem.getTips(); 
							
							fire.pb.talk.DisplayInfo displayInfo = new fire.pb.talk.DisplayInfo();
							msg.displayinfo = displayInfo;
							msg.displayinfo.displaytype = fire.pb.talk.DisplayInfo.DISPLAY_ROLL_ITEM;
							msg.displayinfo.counterid = item.getId();
							gnet.link.Onlines.getInstance().send(roleid, msg);			
						}					
					}				
				}				
			}
		}
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794525;

	public int getType() {
		return 794525;
	}

	public long melonid;

	public CRequestRollItemTips() {
	}

	public CRequestRollItemTips(long _melonid_) {
		this.melonid = _melonid_;
	}

	public final boolean _validator_() {
		return true;
	}

	public OctetsStream marshal(OctetsStream _os_) {
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		_os_.marshal(melonid);
		return _os_;
	}

	public OctetsStream unmarshal(OctetsStream _os_) throws MarshalException {
		melonid = _os_.unmarshal_long();
		if (!_validator_()) {
			throw new VerifyError("validator failed");
		}
		return _os_;
	}

	public boolean equals(Object _o1_) {
		if (_o1_ == this) return true;
		if (_o1_ instanceof CRequestRollItemTips) {
			CRequestRollItemTips _o_ = (CRequestRollItemTips)_o1_;
			if (melonid != _o_.melonid) return false;
			return true;
		}
		return false;
	}

	public int hashCode() {
		int _h_ = 0;
		_h_ += (int)melonid;
		return _h_;
	}

	public String toString() {
		StringBuilder _sb_ = new StringBuilder();
		_sb_.append("(");
		_sb_.append(melonid).append(",");
		_sb_.append(")");
		return _sb_.toString();
	}

	public int compareTo(CRequestRollItemTips _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		_c_ = Long.signum(melonid - _o_.melonid);
		if (0 != _c_) return _c_;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

