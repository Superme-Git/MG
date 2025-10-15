package fire.pb.gm;

import fire.pb.course.CourseColumn;

public class GM_achieve extends GMCommand {

	@Override
	boolean exec(String[] args) {
		if (args.length < 1) {
			sendToGM("用法为：//acheive roleid 成就id");
			return false;
		}
		
		new mkdb.Procedure() {
			@Override
			protected boolean process() throws Exception {
				long roleid = -1;
				int achieveid = -1;
				if (args.length == 2) {
					roleid = Long.parseLong(args[0]);
					achieveid = Integer.parseInt(args[1]);
				}
				else {
					roleid = getGmroleid();
					achieveid = Integer.parseInt(args[0]);
				}
				
				CourseColumn courseCol = fire.pb.course.CourseManager
						.getCourse(roleid, false);
				courseCol.achieved(achieveid);
				fire.pb.course.CourseManager.getInstance().updateArchiveInfo(roleid, achieveid);

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
