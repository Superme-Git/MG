package fire.pb.gm;

import fire.pb.instancezone.InstanceZone;
import fire.pb.instancezone.InstanceZoneFactory;

public class GM_finishsave extends GMCommand {
	@Override
	boolean exec(String[] args) {
		new mkdb.Procedure() {
			protected boolean process() throws Exception {
				InstanceZone instzone = InstanceZoneFactory
						.getRoleCurInstanceZone(getGmroleid(), false);
				int saveId = instzone.getCurActiveSave();
				if (instzone.triggerSaveCompleted(saveId))
					instzone.gmSaveCompleted(saveId);
				return true;
			};
		}.submit();
		return true;
	}

	@Override
	String usage() {
		return "//finishsave ��ɵ�ǰ����";
	}
}
