package fire.pb.gm;

/**
 * 清除精英副本奖励次数
 * @author Administrator
 *
 */
public class GM_clearfbjl extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				long roleid = getGmroleid();
				xbean.InstanceInfoCol infoCol = xtable.Roleinstancetask.get(roleid);
				infoCol.setCounts(0);
				
				return true;
			}
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "//clearfbjl";
	}

}
