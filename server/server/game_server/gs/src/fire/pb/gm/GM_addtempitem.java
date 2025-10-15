package fire.pb.gm;

public class GM_addtempitem extends GMCommand {

	@Override
	boolean exec(String[] args) {
		final Integer id, number;
		id = Integer.valueOf(args[0]);
		if (args.length >= 2)
			number = Integer.valueOf(args[1]);
		else
			number = 1;
		final fire.pb.item.Module itemmodule = (fire.pb.item.Module) fire.pb.main.ModuleManager
				.getInstance().getModuleByName("item");
		if (itemmodule != null) {
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					return fire.pb.item.Module.getInstance().addItemToTemp(
							getGmroleid(), id, number, "gm") != 0;
				}
			}.submit();
		}
		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
