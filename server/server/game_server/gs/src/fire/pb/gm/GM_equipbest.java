package fire.pb.gm;

public class GM_equipbest extends GMCommand {

	@Override
	boolean exec(String[] args) {

		new mkdb.Procedure() {
			@Override
			protected boolean process() {
				final fire.pb.item.ItemMgrImp im = (fire.pb.item.ItemMgrImp) fire.pb.item.Module
						.getInstance().getItemManager();
				im.equipAllBest(getGmroleid());
				return true;
			}

		}.submit();

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
