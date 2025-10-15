
package snail.ant;

import org.apache.tools.ant.*;
import java.io.*;
import java.util.*;

/*
 * 编译时，需要把 ant.jar 加到 Libraries 中。
 * eclipse 安装路径中有这个库, 如：
 * c:\eclipse\plugins\org.apache.ant_VERSION\lib\ant.jar
 */
public class Conf extends Task {
	class Transfer {
		private String lineSeparatorSaved;
		private String section = null;
		private LineNumberReader in;
		private PrintStream out;

		private Map<String, Map<String, String> > sections;
		private Map<String, Set<String> > rewrites;

		Transfer() throws Exception {
			// init sections from properties
			sections = new HashMap<String, Map<String, String> >();
			for (Place p : places) {
				String [] ss = p.getName().split(":");
				if (ss.length != 2)
					throw new BuildException("invalid conf.place name of'"
						+ p.getName() + "'! sample->'section:confname'");

				String sn = ss[0].trim();
				String cn = ss[1].trim();
				Map<String, String> confs = sections.get(sn);
				if (null == confs)
					sections.put(sn, confs = new HashMap<String, String>());
				confs.put(cn, p.getValue());
			}
			rewrites = new HashMap<String, Set<String> >();

			lineSeparatorSaved = System.getProperty("line.separator");
			System.setProperty("line.separator", "\n");

			in = new LineNumberReader(new InputStreamReader(new FileInputStream(src), encoding));
			out = new PrintStream(new FileOutputStream(dest), false, encoding);
		}

		// return confMap that not used
		private void setSection(String sn) {
			String oldSec = section;
			section = sn;

			Map<String, String> confs = sections.get(oldSec);
			if (null == confs) return;

			// add conf when section changed
			boolean added = false;
			Set<String> used = rewrites.get(oldSec);
			for (Map.Entry<String, String> e : confs.entrySet())
				if (null == used || !used.contains(e.getKey())) {
					out.println(e.getKey() + " = " + e.getValue());
					added = true;
				}
			if (added) out.println();
		}

		private int find(String s, String chars) {
			for (int i = 0; i < s.length(); ++i)
				if (chars.indexOf(s.charAt(i)) >= 0)
					return i;
			return -1;
		}

		// skip unknown format
		private String rewrite(String line) {
			Map<String, String> confs = sections.get(section); // it is fine if section is null
			if (null == confs) return line;

			String [] nv = line.split("=");
			if (nv.length != 2) return line;

			String cn = nv[0].trim();
			String vnew = confs.get(cn);
			if (null == vnew) return line;

			int commentIndex = find(nv[1].trim(), "#; \t"); // skip leading white space. trailing is free.
			String comment = commentIndex >= 0 ? nv[1].substring(commentIndex) : "";
			{
				// save used cn
				Set<String> rconfs = rewrites.get(section);
				if (null == rconfs)
					rewrites.put(section, rconfs = new HashSet<String>());
				rconfs.add(cn);
			}
			return nv[0] + "= " + vnew + comment;
		}

		private void run() throws Exception {
			String line = null;
			while ( (line = in.readLine()) != null) {
				if (line.isEmpty()) {
					out.println(line);
					continue;
				}

				switch (line.charAt(0)) {
				case '#': case ';':
					out.println(line);
					break;

				case '[': // [section] # comment
					{
					out.println(line);
					int endIndex = line.indexOf(']');
					if (endIndex >= 0) setSection(line.substring(1, endIndex));
					// else skip unknown format
					}
					break;

				default:
					out.println(rewrite(line));
					break;
				}
			}

			// add new section before close
			for (Map.Entry<String, Map<String, String>> e: sections.entrySet()) {
				if (rewrites.keySet().contains(e.getKey())) continue;
				out.println();
				out.println("[" + e.getKey() + "]");
				for (Map.Entry<String, String> kv : e.getValue().entrySet())
					out.println(kv.getKey() + " = " + kv.getValue());
			}
			in.close();
			out.close();
			System.setProperty("line.separator", lineSeparatorSaved);
		}
	}

	// ant interface
	@Override
	public void execute() throws BuildException {
		try {
			new Transfer().run();
		} catch (Exception ex) {
			throw new BuildException(ex);
		}
	}

	private String src;
	private String dest;
	private String encoding = "GBK";
	private Set<Place> places = new HashSet<Place>();

	public void addPlace(Place place) {
		places.add(place);
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
