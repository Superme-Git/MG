package snail.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class Place extends Task {
	@Override
	public void execute() throws BuildException {
	}

	private String name;
	private String value;

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
