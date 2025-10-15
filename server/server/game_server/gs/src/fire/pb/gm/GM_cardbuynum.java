package fire.pb.gm;

public class GM_cardbuynum extends GMCommand {

	@Override
	boolean exec(String[] args) {

		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		final int num;
		final long roleid;
		num = Integer.parseInt(args[0]);
		if (num < 0) {
			return false;
		}

		if (args.length >= 2)
			roleid = Long.valueOf(args[1]);
		else
			roleid = getGmroleid();

		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				xbean.RoleUpdateModelNum roleUpdateModelNum = xtable.Roleupdatemodelnumtab.get(roleid);
				if(roleUpdateModelNum==null){
					roleUpdateModelNum=xbean.Pod.newRoleUpdateModelNum();
					xtable.Roleupdatemodelnumtab.insert(roleid, roleUpdateModelNum);
				}
				roleUpdateModelNum.setTradingbuyfushinum(num);
				return true;
			}
		}.submit();

		return true;
	}

	@Override
	String usage() {
		return "cardbuynum num";
	}

}
