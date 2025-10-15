package snail.tools;

public class Kill extends Tool {

	@Override
	public void build(Options options) {
		options.add(Jmxc.options());
	}

	@Override
	public void run(Options options) throws Exception {
		Jmxc jmxc = Jmxc.connect(options);
		kill(jmxc);
	}

	public static void kill(Jmxc jmxc) throws Exception {
		try {
			String [] signature = new String[] { "java.lang.String" };
			Object [] params = new Object[1];
			params[0] = "iamsure";
			try {
				jmxc.invoke("mkdb:type=Mkdb", "shutdown", params, signature);
			} catch ( Throwable x ) {
				// skip all
			}
		} finally {
			jmxc.close();
		}
	}
}
