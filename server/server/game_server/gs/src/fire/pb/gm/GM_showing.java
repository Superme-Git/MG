package fire.pb.gm;

public class GM_showing extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length == 0) {
			xbean.ShowingMission st = xtable.Showingmission
					.select(getGmroleid());
			final int showing;
			final boolean isTeamLeader;
			if (st == null) {
				showing = 0;
				isTeamLeader = true;
			} else {
				showing = st.getMissionid();
				isTeamLeader = st.getIsleader();
			}
			sendToGM(String.valueOf(showing) + ","
					+ String.valueOf(isTeamLeader));
			return true;
		}
		return false;
	}

	@Override
	String usage() {
		return null;
	}

}
