
package fire.pb.clan.fight;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CBattleFieldScore__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

public class CBattleFieldScore extends __CBattleFieldScore__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		
		mkdb.Procedure battlefieldscore = new mkdb.Procedure()
		{
			@Override
			protected boolean process()  throws Exception
			{
				Long c1 = xtable.Roleid2clanfightid.select(roleid); //检测是否在战场中 by changhao
				if (c1 == null)
				{
					return false;
				}
				
				SBattleFieldScore msg = new SBattleFieldScore();
				msg.myrank = -1;
				
				fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(c1, true);
				if (bf != null)
				{
					msg.clanscore1 = bf.getClanfightBean().getClanscore1();
					msg.clanscroe2 = bf.getClanfightBean().getClanscore2();
					
					ClanFightBattleField.RankInfo info = bf.getRankSorceByRoleId(roleid);
					if (info != null)
					{
						msg.myrank = info.rank;
						msg.myscore = info.scroe;						
					}
					
					psendWhileCommit(roleid, msg);
				}
				
				return true;
			}
		};
		
		battlefieldscore.submit();	
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808535;

	public int getType() {
		return 808535;
	}


	public CBattleFieldScore() {
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
		if (_o1_ instanceof CBattleFieldScore) {
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

	public int compareTo(CBattleFieldScore _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

