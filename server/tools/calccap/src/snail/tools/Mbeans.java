package snail.tools;

import java.util.*;
import javax.management.*;

public class Mbeans extends Tool {

	@Override
	public void build(Options options) {
		options.add(Jmxc.options());
		options.add(new String[] {
				null, "domains ...", "domains list", null}
		);
	}

	@Override
	public void run(Options options) throws Exception {
		Jmxc jmxc = Jmxc.connect(options);
		try {
			if (!options.hasToken()) {
				System.out.println("mbeans:");
				print( jmxc.queryNames(null) );
			}
			else {
				for (String domain : options.getTokens()) {
					System.out.println("mbeans: " + domain);
					print( jmxc.queryNames(domain) );
				}
			}
		} finally {
			jmxc.close();
		}
	}

	private void print(Set<ObjectName> mbeans) {
		ObjectName [] objectNames = mbeans.toArray(new ObjectName[mbeans.size()]);
		Arrays.sort(objectNames);

		for (ObjectName objectName : objectNames)
			System.out.println("	" + objectName);
	}
}
