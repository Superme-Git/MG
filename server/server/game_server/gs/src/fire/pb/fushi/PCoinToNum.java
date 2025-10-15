

package fire.pb.fushi;

import mkdb.Procedure;


public class PCoinToNum extends Procedure {

	int userid;
	
	public PCoinToNum(int userId) {
		this.userid = userId;
	}
	@Override
	protected boolean process() throws Exception {

		xbean.ActiveUserInfo userInfo = xtable.Activeusertable.get(userid);
		if (userInfo == null)
			return true;
		if (userInfo.getConvertchargetotalamouttolong()==1)
			return true;
		long realValue = userInfo.getChargetotalrmb();//只有totalrmb可能溢出,在这里做下处理
		if (realValue < 0){
			realValue = Integer.MAX_VALUE * 2L + realValue;
		}
		userInfo.setChargetotalamountlong(userInfo.getChargetotalamount()+userInfo.getChargetotalamountlong());
		userInfo.setChargetotalrmblong(realValue+userInfo.getChargetotalrmblong());
		userInfo.setChargetotalrmbuntillastdaylong(userInfo.getChargetotalrmbuntillastday()+userInfo.getChargetotalrmbuntillastdaylong());
		userInfo.setConvertchargetotalamouttolong(1);
		return true;
	}

	
}

