
package fire.pb.clan.fight;

// {{{ RPCGEN_IMPORT_BEGIN
// {{{ DO NOT EDIT THIS
import com.locojoy.base.Marshal.OctetsStream;
import com.locojoy.base.Marshal.MarshalException;

abstract class __CBattleFieldRankList__ extends mkio.Protocol { }

// DO NOT EDIT THIS }}}
// RPCGEN_IMPORT_END }}}

/**
 * 获取战场中的积分排行 by changhao
 */
public class CBattleFieldRankList extends __CBattleFieldRankList__ {
	@Override
	protected void process() {
		// protocol handle
		
		final long roleid = gnet.link.Onlines.getInstance().findRoleid(this);
		if (roleid < 0)
			return;
		
		mkdb.Procedure battlefieldranklist = new mkdb.Procedure()
		{
			@Override
			protected boolean process()  throws Exception
			{
				Long c1 = xtable.Roleid2clanfightid.select(roleid); //检测是否在战场中 by changhao
				if (c1 == null)
				{
					return false;
				}
				
				xbean.EClanFightStatistics statistics = xtable.Roleid2clanfightstatistics.select(roleid);
				if (statistics == null)
				{
					return false;
				}
				
				long cur = System.currentTimeMillis();
				if (cur < statistics.getLastcbattlefieldrankliststamp() + 20000) //做下时间限制 by changhao
				{
					return false;
				}
				
				SBattleFieldRankList msg = new SBattleFieldRankList();
				msg.myrank = -1;
				
				fire.pb.clan.fight.ClanFightBattleField bf = fire.pb.clan.fight.ClanFightFactory.getClanFightBattleField(c1, true);
				if (bf != null)
				{
					msg.clanscore1 = bf.getClanfightBean().getClanscore1();
					msg.clanscroe2 = bf.getClanfightBean().getClanscore2();
					
					int which = bf.getSideByRoleId(roleid);
					if (which != 0 && which != 1)
					{
						return false;
					}
					
					java.util.List<xbean.BattleFieldScroeRank> list = null;
					java.util.Map<Long, Integer> map = null;
					if (which == 0)
					{
						list = bf.getClanfightBean().getClan1roleranklist();
						map = bf.getClanfightBean().getClan1rolerankmap();
					}
					else if (which == 1)
					{
						list = bf.getClanfightBean().getClan2roleranklist();
						map = bf.getClanfightBean().getClan2rolerankmap();					
					}
					else
					{
						return false;
					}
					
					Integer rank = map.get(roleid);
					if (rank != null)
					{
						msg.myrank = rank;
						xbean.BattleFieldScroeRank r = list.get(rank);
						if (r != null)
						{
							msg.myscore = r.getScroe();
						}
					}
					
					int count = 0;
					for (xbean.BattleFieldScroeRank e : bf.getClanfightBean().getClan1roleranklist())
					{
						if (count >= 50) //限制每次只传50个 by changhao
						{
							break;
						}
						
						fire.pb.clan.fight.RoleBattleFieldRank r = new fire.pb.clan.fight.RoleBattleFieldRank();
						String name = bf.getClanfightBean().getEnterroleids().get(e.getRoleid());
						if (name != null)
						{
							r.rolename = name;
							r.rolescroe = e.getScroe();
							msg.ranklist1.add(r);
							count ++;							
						}
					}
					
					count = 0;
					for (xbean.BattleFieldScroeRank e : bf.getClanfightBean().getClan2roleranklist())
					{
						if (count >= 50) //限制每次只传50个 by changhao
						{
							break;
						}
						
						fire.pb.clan.fight.RoleBattleFieldRank r = new fire.pb.clan.fight.RoleBattleFieldRank();
						String name = bf.getClanfightBean().getEnterroleids().get(e.getRoleid());
						if (name != null)
						{
							r.rolescroe = e.getScroe();
							r.rolename = name;
							msg.ranklist2.add(r);
							count ++;
						}
					}
					
					psendWhileCommit(roleid, msg);
				}
				
				return true;
			}
		};
		
		battlefieldranklist.submit();			
	}

	// {{{ RPCGEN_DEFINE_BEGIN
	// {{{ DO NOT EDIT THIS
	public static final int PROTOCOL_TYPE = 808538;

	public int getType() {
		return 808538;
	}


	public CBattleFieldRankList() {
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
		if (_o1_ instanceof CBattleFieldRankList) {
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

	public int compareTo(CBattleFieldRankList _o_) {
		if (_o_ == this) return 0;
		int _c_ = 0;
		return _c_;
	}

	// DO NOT EDIT THIS }}}
	// RPCGEN_DEFINE_END }}}

}

