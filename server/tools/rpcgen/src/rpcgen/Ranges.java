package rpcgen;

import java.util.*;

public class Ranges {
	public static class Range {
		private int first; // [first, last)
		private int last;

		public Range(String [] pair) {
			first = Integer.parseInt(pair[0]);
			last = pair.length > 1 ? Integer.parseInt(pair[1]) : first + 1;
		}
		public Range(int first, int last) {
			this.first = first;
			this.last = last;
			if (first < 0 || last < 0 || first > last)
				throw new RuntimeException("error new range : " + this);
		}

		private boolean hasIntersection(Range r) {
			return !(r.last <= first || r.first >= last);
		}

		public String toString() {
			if (first + 1 == last)
				return Integer.toHexString(first);
			return Integer.toHexString(first) + "-" + Integer.toHexString(last);
		}

		private boolean include(Range r) {
			return r.first >= first && r.last <= last;
		}
	}

	private List<Range> ranges = new ArrayList<Range>();

	public Ranges() {
		
	}

	public Ranges(String str) {
		for (String s : str.split(",")) {
			if (s.isEmpty())
				continue;
			String[] pair = s.split("[-\\s]+");
			if (pair.length == 0)
				continue;
			if (pair.length > 2)
				throw new RuntimeException("error format: '" + str + "'");
			ranges.add(new Range(pair));
		}
	}

	public void checkAdd(Ranges rs) {
		for (Range r : rs.ranges)
			checkAdd(r);
	}

	public void checkAdd(Range r) {
		for (Range _r : ranges)
			if (_r.hasIntersection(r))
				throw new RuntimeException(ranges + " checkAdd " + r);
		ranges.add(r);
	}

	public void checkAdd(int type) {
		checkAdd(new Range(type, type+1));
	}

	public boolean include(Ranges rs) {
		for (Range r : rs.ranges)
			if (include(r))
				return true;
		return false;
	}
	public boolean include(Range r) {
		for (Range _r : ranges)
			if (_r.include(r))
				return true;
		return false;
	}

	public void assertInclude(Range r) {
		if (include(r))
			return;
		throw new RuntimeException(ranges + " NOT Include " + r);
	}

	public void assertInclude(Ranges rs) {
		for (Range r : rs.ranges)
			assertInclude(r);
	}

	public void assertInclude(int type) {
		assertInclude(new Range(type, type + 1));
	}
}
