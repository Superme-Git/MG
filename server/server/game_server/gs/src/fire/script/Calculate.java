package fire.script;

public class Calculate {
	
	public static boolean getResult(int v1, String expr) {
		String[] strings = expr.split("[0-9]");
		if (strings.length > 0) {
			int v2 = Integer.valueOf(expr.substring(strings[0].length()));
			switch (strings[0]) {
			case ">": return v1 > v2;
			case "<": return v1 < v2;
			case ">=": return v1 >= v2;
			case "<=": return v1 <= v2;
			case "==": return v1 == v2;
			}
		}
		throw new IllegalArgumentException("ERROR:" + expr);
	}
}
