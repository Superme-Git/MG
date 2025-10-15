

package fire.pb.mbean.beans;

import java.util.Map;

import fire.pb.mbean.AbstractRequestHandler;


public class GetAccCharList extends AbstractRequestHandler {


	public GetAccCharList(String name) {

		super(name);

	}

	@Override
	protected Map<Object, Object> handleRequest(Map<?, ?> paras) {
		try {
			return failedMsg("userid查询方法淘汰");
		} catch (Exception e) {
			return failedMsg(e.getMessage());
		}
	}

}

