package locojoy.game.mt3.timer;

import java.util.TimerTask;

import locojoy.game.mt3.sdk.ifly.Token;

/**
 * TokenÐøÆÚ
 * 
 * @author liangyanpeng
 *
 */
public class TokenTimer extends TimerTask {

	@Override
	public void run() {
		Token.getInstance().createToken();
	}

}
