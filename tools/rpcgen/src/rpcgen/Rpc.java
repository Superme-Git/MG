package rpcgen;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;
import rpcgen.types.Bean;
import rpcgen.types.Type;

public class Rpc extends Protocol {
	// define
	private String timeout;
	private String argument;
	private String result;

	private Bean implementInc; // 不使用Protocol中的实现。
	// compile
	private Bean argumentBean;
	private Bean resultBean;

	public Rpc(Namespace space, Element self, short typeHigh) {
		super(space, self, typeHigh, true);

		timeout = self.getAttribute("timeout");
		argument = self.getAttribute("argument");
		result = self.getAttribute("result");

		implementInc = new Bean(super.getName()) {
			@Override public String getInclude() { return null; }
			@Override public boolean isBean() { return false; }
		};
		implementInc.setSpace(space);
		implementInc.parse(self);
	}

	@Override
	public void compile() {
		resultBean = (Bean)rpcgen.types.Type.compile(getSpace(), result);
		argumentBean = (Bean)rpcgen.types.Type.compile(getSpace(), argument);
		implementInc.compile();
	}

	@Override
	public void dependBeans(Set<Bean> beans) {
		Set<Type> deps = new HashSet<Type>();
		argumentBean.depends(deps);
		resultBean.depends(deps);
		for (Type type : deps)
			if (type instanceof Bean)
				beans.add((Bean)type);
		beans.add(implementInc);
	}

	public Bean getArgumentBean() {
		return argumentBean;
	}

	public Bean getResultBean() {
		return resultBean;
	}

	@Override
	public Bean getImplementBean() {
		return this.implementInc;
	}

	public String getTimeout() {
		return timeout;
	}
}
