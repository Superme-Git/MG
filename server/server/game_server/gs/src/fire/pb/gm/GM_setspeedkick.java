package fire.pb.gm;

public class GM_setspeedkick extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("参数格式错误：" + usage());
			return false;
		}

		int kick = Integer.parseInt(args[0]);

		mkdb.Procedure set = new mkdb.Procedure() {
			@Override
			public boolean process()
			{
				fire.pb.scene.movable.MoveUnit.NEED_KICK.set(kick);
				return true;
			}
		};

		set.submit();

		return true;
	}

	@Override
	String usage() {
		return null;
	}

}
