package fire.pb.mission.instance.line;

public class Grade {
	private final int grade;
	private final int minlevel;
	private final int maxlevel;
	public Grade(int grade, int minlevel, int maxlevel) {
		this.grade = grade;
		this.minlevel = minlevel;
		this.maxlevel = maxlevel;
	}
	
	public int getGrade() {
		return grade;
	}
	public int getMinlevel() {
		return minlevel;
	}
	public int getMaxlevel() {
		return maxlevel;
	}
}
