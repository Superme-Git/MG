package fire.pb.gm;

/**
 * 清空杀怪获取的奖励次数
 * @author Administrator
 *
 */
public class GM_clearjl extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				long roleid = getGmroleid();
				xtable.Roletimernpcinfos.remove(roleid);
				xtable.Roleeventnpcinfos.remove(roleid);
				return true;
			}
		}.submit();
		
		return true;
	}

	@Override
	String usage() {
		return "//clearjl";
	}

}
