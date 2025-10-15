package fire.pb.gm;

import fire.pb.mission.activelist.RoleLiveness;

public class GM_yf extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {

			@Override
			protected boolean process() throws Exception {
				RoleLiveness actrole = RoleLiveness.getRoleLiveness(getGmroleid());
				if (actrole != null) {
					actrole.calcYingFuExpGM();
				}
				
				return true;
			}
			
		}.submit();
		return true;
	}

	@Override
	String usage() {
		// TODO Auto-generated method stub
		return null;
	}

}
