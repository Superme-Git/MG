package fire.pb.http.handler;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;

import net.sf.json.JSONObject;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import fire.log.Logger;
import fire.pb.http.manager.GMSeachManage;
import fire.pb.http.util.LocalHttpUtils;
import fire.pb.main.Gs;

/**
 * GM查询在线角色信息 author yebin @ 2016年1月19日
 */
public class GMHttpSelectHandler implements HttpHandler {
	public static Logger logger = Logger.getLogger("GMHTTP");

	private static final int GM_GET_ROLE_INFO = 1;	// 角色信息查询
	private static final int GM_GET_PETS_INFO = 2;	// 宠物信息查询
	private static final int GM_GET_GUILD_INFO = 3;	// 公会信息查询

	@Override
	public void handle(HttpExchange exchange) {
		try {
			JSONObject jsonObj = new JSONObject();
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			final OutputStream os = exchange.getResponseBody();
			Map<String, String> params = LocalHttpUtils.getParameters(exchange);
			final URI uri = exchange.getRequestURI();
			final String path = uri.getPath();
			logger.info("localhttpserver url=" + path + " params=" + params);
			if (params == null) {
				setResponseInfo(jsonObj, 99);
				os.write(jsonObj.toString().getBytes());
				return;
			}

			String infoType = params.get("infoType");
			String serverId = params.get("serverId");
			String targetId = params.get("roleId");

			if (targetId == null) {
				targetId = params.get("guildId");
			}

			if (infoType == null || serverId == null || targetId == null) {
				setResponseInfo(jsonObj, 99);
				os.write(jsonObj.toString().getBytes());
				logger.error("GM_GetOnlineInfoHandler param is error");
				return;
			}

			// 判断输入的参数是否正确
			if (!Gs.serverid.equals(serverId)) {
				setResponseInfo(jsonObj, 98);
				os.write(jsonObj.toString().getBytes());
				return;
			}

			int nInfoType = -1;
			long nTargetId = -1;
			try {
				nInfoType = Integer.parseInt(infoType);
				nTargetId = Long.parseLong(targetId);
			} catch (Exception e) {
				e.printStackTrace();
				setResponseInfo(jsonObj, 97);
				os.write(jsonObj.toString().getBytes());
				logger.error("param is error");
				return;
			}

			String result = getResultInfo(nInfoType, nTargetId);
			if (result.length() == 0) {
				setResponseInfo(jsonObj, 96);
				os.write(jsonObj.toString().getBytes());
				logger.error("result is error");
				return;
			}
			os.write(result.getBytes());
		} catch (Exception e) {
			logger.error("GM_HttpToolHandler exception=", e);
		} finally {
			exchange.close();
		}
	}

	public String getResultInfo(int infoType, long nTargetId) {
		switch (infoType) {
		case GM_GET_ROLE_INFO: // 角色信息查询
			return GMSeachManage.getRoleDetail(nTargetId);
		case GM_GET_PETS_INFO: // 宠物信息查询
			return GMSeachManage.getPetDetail(nTargetId);
		case GM_GET_GUILD_INFO: // 公会信息查询
			return GMSeachManage.getFactionDetail((int) nTargetId);
		}
		return "";
	}

	private void setResponseInfo(JSONObject jsonobj, int code) {
		String info = "";
		switch (code) {
		case 1:
			info = "成功";
			break;
		case 2:
			info = "签名错误";
			break;
		case 3:
			info = "角色ID不存在";
			break;
		default:
			info = "其他";
			break;
		}

		jsonobj.accumulate("Code", code);
		jsonobj.accumulate("Text", info);
	}
}
