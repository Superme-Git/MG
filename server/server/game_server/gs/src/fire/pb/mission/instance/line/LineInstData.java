package fire.pb.mission.instance.line;

import java.util.List;

public class LineInstData {

	private final String instname;
	private final List<Integer> step;
	
	public LineInstData(final String instname, final List<Integer> step) {
		this.instname = instname;
		this.step = step;
	}

	public String getInstname() {
		return instname;
	}

	public List<Integer> getStep() {
		return step;
	}

}
