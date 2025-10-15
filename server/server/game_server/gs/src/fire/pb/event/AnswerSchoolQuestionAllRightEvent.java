package fire.pb.event;

public class AnswerSchoolQuestionAllRightEvent implements Event {
	private final long roleid;
	public AnswerSchoolQuestionAllRightEvent( final long roleid ) {
		this.roleid = roleid;
	}
	@Override
	public long getRoleid() {
		return roleid;
	}

}
