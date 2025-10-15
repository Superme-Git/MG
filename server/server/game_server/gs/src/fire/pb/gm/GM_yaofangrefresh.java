package fire.pb.gm;

public class GM_yaofangrefresh extends GMCommand {

	@Override
	boolean exec(String[] args) {
		new fire.pb.clan.PClanMedicItem().submit();
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
