package fire.script;

import java.util.List;

import javax.script.ScriptException;

import fire.pb.util.Parser;

public class JavaScript {

	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}

		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.'
					&& str.charAt(i) != '-') {
				return false;
			}
		}
		return true;
	}

	private String js;

	private Double value = null;

	private int funID = -1;

	public JavaScript(String js) {
		try {
			if (isNumeric(js)) {
				value = Double.valueOf(js);
			} else {
				this.js = "with(Math){ return " + js + ";}";
			}
		} catch (Exception e) {

		}
	}

	public JavaScript(String js, boolean neednoreturn) {
		if (isNumeric(js)) {
			value = Double.valueOf(js);
		} else {
			this.js = "with(Math){ " + js + ";}";
		}
	}

	public JavaScript append(String episode) {
		StringBuffer sb = new StringBuffer(js);
		sb.insert(sb.length() - 1, episode);
		js = sb.toString();
		return this;
	}

	public Double eval(javax.script.ScriptEngine engine) {
		try {
			if (value != null)
				return value;
			Object o = engine.eval(js);
			if (o == null)
				return null;
			if (o instanceof Double)
				return (Double) o;
			if (o instanceof Integer)
				return ((Integer) o).doubleValue();
			else
				throw new ScriptException("Unsupported Type: "
						+ o.getClass().toString());
		} catch (ScriptException e) {
			// AbstractJSEngine.logger.error("JS脚本"+ js+"有错：/n"+
			// Parser.convertStackTrace2String(e));
			System.out.println("JS脚本" + js + "有错：/n"
					+ Parser.convertStackTrace2String(e));
			return 0.0;
		}
	}

	public Double GetDoubleValue() {
		return value;
	}

	public String getJavaScriptString() {
		return js;
	}

	public String getOringinString() {
		return this.js.substring(11, this.js.length() - 1);
	}

	public void parseUsedAttrTypes(List<Integer> usedattrtypea,
			List<Integer> usedattrtypeb) {
		try {
			if (value == null) {
				if (funID > -1)
					return;
				for (int i = 0; i < fire.pb.effect.Module.fightAttrTypeIds.length; i++) {
					String name = fire.pb.effect.Module.getInstance()
							.getEffectNameById(
									fire.pb.effect.Module.fightAttrTypeIds[i]);
					if (js.contains(name + "a")
							&& !usedattrtypea
									.contains(fire.pb.effect.Module.fightAttrTypeIds[i]))
						usedattrtypea
								.add(fire.pb.effect.Module.fightAttrTypeIds[i]);
					if (js.contains(name + "b")
							&& !usedattrtypeb
									.contains(fire.pb.effect.Module.fightAttrTypeIds[i]))
						usedattrtypeb
								.add(fire.pb.effect.Module.fightAttrTypeIds[i]);
				}
				for (int i = 0; i < fire.pb.effect.Module.extfightAttrTypeIds.length; i++) {
					String name = fire.pb.effect.Module
							.getInstance()
							.getEffectNameById(
									fire.pb.effect.Module.extfightAttrTypeIds[i]);
					if (js.contains(name + "a")
							&& !usedattrtypea
									.contains(fire.pb.effect.Module.extfightAttrTypeIds[i]))
						usedattrtypea
								.add(fire.pb.effect.Module.extfightAttrTypeIds[i]);
					if (js.contains(name + "b")
							&& !usedattrtypeb
									.contains(fire.pb.effect.Module.extfightAttrTypeIds[i]))
						usedattrtypeb
								.add(fire.pb.effect.Module.extfightAttrTypeIds[i]);
				}
				Module.registerJavaScriptExpr(js, true, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return js;
	}
}
