

package fire.pb.compensation;

public class CompensationCondition {

	private final int conditionid;
	private final int[] args;
	public CompensationCondition(String[] args) {

		super();
		this.conditionid = Integer.parseInt(args[0]);
		this.args = new int[args.length -1];
		for (int i = 0; i < args.length - 1; i++) {
			this.args[i] = Integer.parseInt(args[i+1]);
		}
	}
	
	public int getConditionid() {
	
		return conditionid;
	}
	
	public int[] getArgs() {
	
		return args;
	}
	
}

