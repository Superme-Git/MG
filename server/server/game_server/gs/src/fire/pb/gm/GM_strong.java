package fire.pb.gm;

import xbean.BasicFightProperties;
import fire.log.enums.YYLoggerTuJingEnum;
import fire.pb.PropRole;
import fire.pb.fushi.FushiConst;
import fire.pb.fushi.PAddFuShi;
import fire.pb.gm.GM_addlevel.PGmAddLevel;
import fire.pb.item.RoleAddPointEvent;
import fire.pb.skill.PInbornWithValidProc;
import mkdb.Procedure;

public class GM_strong extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final int value;
		if (args.length >= 1) {
			value = Integer.parseInt(args[0]);
		} else {
			value = 25000;
		}

		final long roleId = getGmroleid();
		final int userId = getGmUserid();

		try {
			new mkdb.Procedure() {
				@Override
				protected boolean process() throws Exception {
					fire.pb.item.Pack bag = new fire.pb.item.Pack(roleId, false);
					
					// 添加银币
					bag.addSysMoney(500000000, "GM指令加钱", fire.log.enums.YYLoggerTuJingEnum.GM, 0);
					
					// 添加金币
					bag.addSysGold(500000000, "GM指令加钱", fire.log.enums.YYLoggerTuJingEnum.GM, 0);
					
					// 添加符石
					new PAddFuShi(userId, roleId, 10000000, FushiConst.SYS_FUSHI, YYLoggerTuJingEnum.GM).call();
					
					PropRole prole = new PropRole(roleId, true);
					int level = (95 - prole.getLevel()) > 0 ? (95 - prole.getLevel()) : -1;
					if (level > 0)
						new PGmAddLevel(getGmUserid(), roleId, level, true).call();

					fire.pb.effect.RoleImpl role = new fire.pb.effect.RoleImpl(roleId);
					java.util.Map<Integer, Float> res = role.gmAddPoints(value, value, value, value, value);
					if (res != null) {
						final fire.pb.attr.SRefreshRoleData send = new fire.pb.attr.SRefreshRoleData();
						send.datas.putAll(res);
						Procedure.psendWhileCommit(roleId, send);

						final fire.pb.attr.SRefreshPointType refresh = new fire.pb.attr.SRefreshPointType();

						xbean.Properties prop = xtable.Properties.get(roleId);
						BasicFightProperties bfp = prop.getBfp();

						refresh.bfp.agi = (short) bfp.getAgi();
						refresh.bfp.cons = (short) bfp.getCons();
						refresh.bfp.endu = (short) bfp.getEndu();
						refresh.bfp.iq = (short) bfp.getIq();
						refresh.bfp.str = (short) bfp.getStr();

						refresh.bfp.agi_save.putAll(prop.getAddpointfp().getAgi_save());
						refresh.bfp.cons_save.putAll(prop.getAddpointfp().getCons_save());
						refresh.bfp.endu_save.putAll(prop.getAddpointfp().getEndu_save());
						refresh.bfp.iq_save.putAll(prop.getAddpointfp().getIq_save());
						refresh.bfp.str_save.putAll(prop.getAddpointfp().getStr_save());

						refresh.point.putAll(prop.getPoint());
						refresh.pointscheme = prop.getScheme();
						refresh.schemechanges = prop.getSchemechanges();

						Procedure.psendWhileCommit(roleId, refresh);

						fire.pb.event.Poster.getPoster().dispatchEvent(new RoleAddPointEvent(roleId));
						sendToGM("每样点数增加了：" + value + "点");
						new PInbornWithValidProc(roleId, -1).call();
						return true;
					}

					return true;
				}
			}.submit();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
