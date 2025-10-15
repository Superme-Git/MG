package fire.pb.battle.pvp;

import mkdb.Transaction;
import fire.pb.battle.pvp3.PPvP3ModifyRoleInfoHandler;
import fire.pb.battle.pvp5.PPvP5ModifyRoleInfoHandler;
import fire.pb.event.Event;
import fire.pb.event.EventHandler;
import fire.pb.event.ModifyRoleNameEvent;

/**
 * PvP事件处理
 * @author XGM
 */
public class PvPEventHandler implements EventHandler {

	static class SingletonHolder {
		static PvPEventHandler singleton = new PvPEventHandler();
	}

	public static PvPEventHandler getInstance() {
		return SingletonHolder.singleton;
	}

	public void init() {
		fire.pb.event.Poster.getPoster().listenEvent(this, ModifyRoleNameEvent.class);
	}

	@Override
	public void onEvent(Event e) {

		if (e instanceof ModifyRoleNameEvent) {
			// 处理角色改名事件
			ModifyRoleNameEvent event = (ModifyRoleNameEvent) e;
			PPvP3ModifyRoleInfoHandler proc3 = new PPvP3ModifyRoleInfoHandler(event.getRoleid(), event.getNewName());
			PPvP5ModifyRoleInfoHandler proc5 = new PPvP5ModifyRoleInfoHandler(event.getRoleid(), event.getNewName());
			if (Transaction.current() != null) {
				mkdb.Procedure.pexecuteWhileCommit(proc3);
				mkdb.Procedure.pexecuteWhileCommit(proc5);
			} else {
				proc3.submit();
				proc5.submit();
			}
		}
	}

}
