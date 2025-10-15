package fire.pb.gm;

import fire.pb.PAddExpProc;

public class GM_addexpbuff extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 2)
			return false;

		final long roleid = Long.valueOf(args[0]);
		final long delexpnum = Long.valueOf(args[1]);
		if (delexpnum <= 0)
			return false;
		new mkdb.Procedure() {
			protected boolean process() throws Exception {

				xbean.Properties prop = xtable.Properties.get(roleid);
				if (prop == null) {
					sendToGM("角色" + roleid + "不存在");
					return false;
				}
				long oldexp = prop.getExp();
				long directdelexp = Math.max(0,
						Math.min(prop.getExp(), delexpnum));
				new PAddExpProc(roleid, -directdelexp, PAddExpProc.OTHER,
						"addexpbuff直接扣除").call();
				String logstr = "给角色加吸经验buff，先扣除现有经验。" + roleid + "原有经验："
						+ oldexp + "，减少经验：" + directdelexp + "，剩余经验："
						+ prop.getExp();
				sendToGM(logstr);
				fire.pb.buff.Module.logger.info(logstr);
				long resumedelexp = delexpnum - directdelexp;
				if (resumedelexp <= 0)
					return true;
				return true;
			};
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "//addexpbuff [roleid] [delexpnum]";
	}

}
