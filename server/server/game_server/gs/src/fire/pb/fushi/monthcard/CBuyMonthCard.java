
package fire.pb.fushi.monthcard;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CBuyMonthCard__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 购买月卡 by changhao
 */
public class CBuyMonthCard extends __CBuyMonthCard__ {
	@Override
	protected void process() {
		// protocol handle
		
 		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
		{
			return;
		}
		
		mkdb.Procedure buymonthcard = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				if (fire.pb.fushi.Module.GetPayServiceType() == 0) //普通服才能购买月卡  by changhao
				{
					boolean ok = fire.pb.fushi.MonthCardManager.getInstance().CheckMonthCardLimit(roleid);
					if (ok)
					{
						fire.pb.talk.MessageMgr.sendMsgNotify(roleid, 190067, null);					
					}
					else
					{
						xbean.Properties prop = xtable.Properties.select(roleid);
						if (prop != null)
						{
							fire.pb.fushi.MonthCardManager.getInstance().ProcessMonthCard(prop.getUserid(), roleid);						
						}
					}					
				}
							
				return true;
			}
		};
		buymonthcard.submit();		
		
		
		
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 812687;

	public int getType() {
		return 812687;
	}


	public CBuyMonthCard() {
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
		if (_o1_ instanceof CBuyMonthCard) {
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

	public int compareTo(CBuyMonthCard _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

