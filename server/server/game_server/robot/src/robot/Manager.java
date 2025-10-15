package robot;

import mkio.ILoginIns;
import mkio.LoginManager;

public class Manager extends LoginManager {
	@Override
	protected void onAddSession(ILoginIns session) {
		super.onAddSession(session);
		Stat.getInstance().onAddSession();
	}

	@Override
	protected void onDelSession(ILoginIns session, Throwable e) {
		super.onDelSession(session, e);
		Stat.getInstance().onDelSession();
	}
}
