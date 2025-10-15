package fire.pb.gm;

import fire.pb.pet.PSetFightPetProc;

public class GM_fightpet extends GMCommand {

	@Override
	boolean exec(String[] args) {
		int petkey = 1;
		if (args.length == 1)
			petkey = Integer.valueOf(args[0]);
		GM_addpet addpet = new GM_addpet();
		addpet.setGmUserid(getGmUserid());
		addpet.setGmroleid(getGmroleid());
		addpet.exec(new String[] { "500001" });
		final PSetFightPetProc setproc = new PSetFightPetProc(getGmroleid(),
				petkey, true);
		setproc.submit();
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
