package fire.pb.gm;

public class GM_answer extends GMCommand {

	@Override
	boolean exec(String[] args) {
		long roleId = getGmroleid();
		if (args.length == 0) {
			return false;
		} else {
			Integer answerType = Integer.parseInt(args[0]);
			Integer questionid = Integer.parseInt(args[1]);
			Integer answerid = Integer.parseInt(args[2]);
			new mkdb.Procedure() {
				@Override
				protected boolean process() {
					if (answerType != 0) {
						new fire.pb.mission.instance.PAnswerQuestion(roleId,
								questionid, answerid).call();
					}

					return true;
				}
			}.submit();
		}
		return true;
	}

	@Override
	String usage() {
		return "answer [type] [questionid] [answerid]";
	}

}
