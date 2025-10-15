

package fire.pb.activity.award;

import fire.pb.main.IModule;

public class Module implements IModule {
	@Override
	public void exit() {
	}

	@Override
	public void init() throws Exception {
		AwardMgr.getInstance().init();
	}
}

