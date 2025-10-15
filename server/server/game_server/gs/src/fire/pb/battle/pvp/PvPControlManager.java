package fire.pb.battle.pvp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fire.pb.battle.pvp1.PvP1Control;
import fire.pb.battle.pvp3.PvP3Control;
import fire.pb.battle.pvp5.PvP5Control;

/**
 * PvP控制器的管理
 * @author XGM
 */
public class PvPControlManager {

	private static final Map<EPvPType, PvPControl> manager = new HashMap<EPvPType, PvPControl>();

	static {
		register(EPvPType.PVP1, PvP1Control.getInstance());
		register(EPvPType.PVP3, PvP3Control.getInstance());
		register(EPvPType.PVP5, PvP5Control.getInstance());
	}

	/**
	 * 注册控制器
	 * @param e
	 * @param c
	 */
	public static void register(EPvPType e, PvPControl c) {
		manager.put(e, c);
	}

	/**
	 * 获得控制器
	 * @param e
	 * @return
	 */
	public static PvPControl getControl(EPvPType e) {
		return manager.get(e);
	}

	/**
	 * 获得所有控制器
	 * @return
	 */
	public static Collection<PvPControl> getAllControl() {
		return manager.values();
	}
}
