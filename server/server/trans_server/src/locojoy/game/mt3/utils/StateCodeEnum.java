package locojoy.game.mt3.utils;

/**
 * ×´Ì¬Âë
 *
 */
public enum StateCodeEnum {

	SUCCESS("200"), SVC_ERROR("500"), SPEECH_ISNULL("405");

	private String code;
	
	private StateCodeEnum(String code) {
		this.code = code;
	}

	public String value() {
		return this.code;
	}
	
	
}