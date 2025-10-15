package fire.pb.gm;

import java.lang.reflect.Method;

public class GM_hotdeploy extends GMCommand {
	@Override
	boolean exec(String[] args) {

		if (args.length < 3) {
			sendToGM(usage());
			return false;
		}
		ExtraCommandClassLoader myClassLoader = new ExtraCommandClassLoader(
				args[0], args[1]);
		try {
			Class<?> cls = myClassLoader.loadClass("fire.pb.gm.GM_" + args[2]);
			Object object = cls.newInstance();
			((GMCommand) object).setGmroleid(getGmroleid());
			((GMCommand) object).setGmUserid(getGmUserid());
			((GMCommand) object).setGmLocalsid(getGmLocalsid());

			Method m = cls.getDeclaredMethod("exec", String[].class);
			m.setAccessible(true);

			String[] newargs = new String[args.length - 3];
			System.arraycopy(args, 3, newargs, 0, newargs.length);

			m.invoke(object, new Object[] { newargs });

		} catch (Exception e) {
			logger.error("hotdeploy class error", e);
		}
		return true;
	}

	@Override
	String usage() {

		return "hotdeploy path jarfile classname";
	}

}
