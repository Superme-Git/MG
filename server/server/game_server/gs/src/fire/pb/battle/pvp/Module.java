package fire.pb.battle.pvp;

import fire.pb.main.ModuleInterface;
import fire.pb.main.ReloadResult;

/**
 * PvP模块
 * @author XGM
 */
public class Module implements ModuleInterface {

	@Override
	public void exit() {
		for (PvPControl c : PvPControlManager.getAllControl()) {
			if (c.isActivityTime()) {
				c.end();
			}
		}
	}

	@Override
	public void init() throws Exception {
		PvPEventHandler.getInstance().init();
	}

	@Override
	public ReloadResult reload() throws Exception {
		return new ReloadResult(false, "module" + this.getClass().getName() + "not support reload");
	}

}
