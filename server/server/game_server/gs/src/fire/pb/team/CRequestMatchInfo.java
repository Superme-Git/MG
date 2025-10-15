
package fire.pb.team;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CRequestMatchInfo__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/***
 * 请求匹配信息
 * @author changhao
 *
 */
public class CRequestMatchInfo extends __CRequestMatchInfo__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		mkdb.Procedure requestmatchinfo = new mkdb.Procedure()
		{
			@Override
			protected boolean process()
			{
				SRequestMatchInfo msg = new SRequestMatchInfo();
				
				xbean.ETeamMatch ematch = xtable.Targetid2teammatch.get(0);
				if (ematch == null)
				{
					msg.playermatchnum = 0;
					msg.teammatchnum = 0;
				}
				else
				{
					msg.teammatchnum = ematch.getTeammatchdatalist().size();
					msg.playermatchnum = ematch.getRolematchdatalist().size();
				}
				
				mkdb.Procedure.psendWhileCommit(roleid, msg);
				
				return true;
			}
		};
		
		requestmatchinfo.submit();
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 794512;

	public int getType() {
		return 794512;
	}


	public CRequestMatchInfo() {
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
		if (_o1_ instanceof CRequestMatchInfo) {
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

	public int compareTo(CRequestMatchInfo _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

