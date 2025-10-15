package fire.pb.gm;

import xbean.ClanMemberInfo;
import fire.pb.clan.ClanPositionType;
import fire.pb.clan.ClanUtils;


public class GM_setclanposition extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		int position=Integer.parseInt(args[0]);
		long roleId=Long.parseLong(args[1]);
		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				xbean.ClanInfo clanInfo = ClanUtils.getClanInfoById(roleId, false);
				if(clanInfo==null){
					return false;
				}
				ClanMemberInfo member = clanInfo.getMembers().get(roleId);
				long masterId=clanInfo.getClanmaster();
				ClanMemberInfo mastermember = clanInfo.getMembers().get(clanInfo.getClanmaster());
				if(member==null){
					return false;
				}
				int oldposition=member.getClanposition();
				// 职位从这里开始变化
				if (position == ClanPositionType.ClanMaster) {
					// 现任会长任命下任会长
					clanInfo.setClanmaster(roleId);
					if(mastermember!=null){
						mastermember.setClanposition(ClanPositionType.ClanMember);
						// 称谓变更为成员
						ClanUtils.changeClanTitleByPosition(masterId, ClanPositionType.ClanMaster,ClanPositionType.ClanMember);
					}
				} else if (position == ClanPositionType.ClanViceMaster) {
					// 任命副会长
					clanInfo.setClanvicemaster(roleId);
				}
				//这里需要处理原来的职位是不是副会长，如果是需要把副会长标志设置为空
				if(oldposition==ClanPositionType.ClanViceMaster){
					clanInfo.setClanvicemaster(0);
				}
				// 称谓变更
				ClanUtils.changeClanTitleByPosition(roleId, member.getClanposition(), position);
				// 更新成员职位，须在公会职位对应数量变更之后
				member.setClanposition(position);
				return true;
			}
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "setclanpostion position roleId";
	}

}
