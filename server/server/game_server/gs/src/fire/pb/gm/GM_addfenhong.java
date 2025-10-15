package fire.pb.gm;

import mkdb.Procedure;
import fire.pb.clan.ClanUtils;
import fire.pb.clan.SBonusQuery;

public class GM_addfenhong extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final int fenhong;
		final long roleid;
		fenhong = Integer.parseInt(args[0]);
		if (fenhong == 0 || fenhong > 1L << 60 || fenhong < (1L << 60) * -1) {
			return false;
		}
		if(fenhong<0){
			return false;
		}
		if (args.length >= 2)
			roleid = Long.valueOf(args[1]);
		else
			roleid = getGmroleid();

		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleid, false);
				if (clanInfo == null || !clanInfo.getMembers().containsKey(roleid)) {
					return false;
				}
				xbean.ClanMemberInfo clanMemberInfo  = clanInfo.getMembers().get(roleid);
				if(clanMemberInfo==null){
					return false;
				}
				//判断溢出问题
				if(Integer.MAX_VALUE-clanMemberInfo.getOwnbonus()<=fenhong){
					clanMemberInfo.setOwnbonus(Integer.MAX_VALUE);
				}else{
					clanMemberInfo.setOwnbonus(clanMemberInfo.getOwnbonus()+fenhong);
				}
				SBonusQuery sBonusQuery=new SBonusQuery();
				sBonusQuery.bonus=clanMemberInfo.getOwnbonus();
				Procedure.psendWhileCommit(roleid, sBonusQuery);
				return true;
			}
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "addfenhong fenhong";
	}

}
