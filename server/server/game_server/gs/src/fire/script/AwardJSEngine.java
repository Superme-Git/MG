package fire.script;

public class AwardJSEngine extends AbstractJSEngine {

	public void SetAgrs(int num) {
		put("num", num);
		put("RoleLv", num);
	}
}
