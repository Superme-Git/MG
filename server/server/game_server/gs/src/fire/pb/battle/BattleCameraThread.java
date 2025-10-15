package fire.pb.battle;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;
import fire.pb.shop.utils.HttpPackage;
import mkdb.ThreadHelper;
import fire.pb.PropConf;
/**
 * 限速器
 * 
 * @author liuxinhua
 *
 */
public class BattleCameraThread extends ThreadHelper {

	private static final Logger logger = Logger.getLogger("BATTLE");

	private final static String THREAD_NAME = "BattleCameraThread";
	private long battleindex = 0;
	private long type = 0;
	
	private BattleCameraThread(String name) {
		super(name);
	}

	static class BattleCameraThreadHolder {
		static BattleCameraThread instance = new BattleCameraThread(THREAD_NAME);
	}

	public static BattleCameraThread getInstance() {
		return BattleCameraThreadHolder.instance;
	}

	@Override
	public void run() {
		while (isRunning() || !protocols.isEmpty()) {
			try {
				BattleCameraInfo p = protocols.poll(1, TimeUnit.SECONDS);
				if (null != p) 
				{
					if(p.ZipStream() != true)
						add(p,false);
					else 
					{
						battleindex = p.getGuestID();
						this.type = p.getCameraType();
						String strFileName = p.getCameraFileName();
						JSONObject params = new JSONObject();
						params.put("filename", strFileName);
						//params.put("speech", Base64.getEncoder().encodeToString(p.getStream().getBytes()));
						params.put("speech", Base64.getEncoder().encodeToString(p.getByte()));
						Map<String, String> headers = new HashMap<String, String>();
						headers.put("Content-Type", "application/json; charset=utf-8");
						logger.debug("发送录像到录像文件服务器:" + PropConf.Battle.CAMERA_FILE_SEVER_ULR +" 文件名为："+strFileName);
						String ret = HttpPackage.postMethod(PropConf.Battle.CAMERA_FILE_SEVER_ULR, headers, params.toString());
						logger.debug("接收到录像文件服务器返回的json字符串:" + ret);
						JSONObject json = JSONObject.fromObject(ret);
						if(json.getInt("code")!=200)
							add(p,false);
						else
						{
							int nbuffsize = json.getInt("buffsize");
							int nfilesize = json.getInt("filesize");
							p.setSizeAfterZip(nfilesize);
							p.submit();
							if(p.getCameraType() == BattleCameraInfo.CAMERA_TYPE_MISSION)
								pveCameras.remove(p.getGuestID());
							if(p.getCameraType() == BattleCameraInfo.CAMERA_TYPE_BINGFENG)
								pvetimeCameras.remove(p.getGuestID(), p.getCostTime());
						}
					}
				}
			} catch (final Exception e) {
				if(this.type == BattleCameraInfo.CAMERA_TYPE_MISSION)
					pveCameras.remove(battleindex);
				if(this.type == BattleCameraInfo.CAMERA_TYPE_BINGFENG)
					pvetimeCameras.remove(battleindex,pvetimeCameras.get(battleindex));

				logger.error("err", e);
			}
		}
	}

	public boolean add(BattleCameraInfo p, boolean bCheak) {
		if(bCheak==true&&protocols.add(p))
		{
			if(p.getCameraType() == BattleCameraInfo.CAMERA_TYPE_MISSION)
				pveCameras.add(p.getGuestID());
			if(p.getCameraType() == BattleCameraInfo.CAMERA_TYPE_BINGFENG)
				pvetimeCameras.put(p.getGuestID(), p.getCostTime());
		}
		return true; 
	}
	
	public boolean haveSameCamera(Long battleid)
	{
		return pveCameras.contains(battleid);
	}

	public boolean haveShortTimeCamera(Long battleid,int time)
	{
		if(time <0)
			return true;
		if(pvetimeCameras.containsKey(battleid))
			return pvetimeCameras.get(battleid).longValue() < time;
		return false;
	}

	public void clear() {
		protocols.clear();
	}
	// 队列长度500, 超过阀值拒绝添加到队列
	private final LinkedBlockingQueue<BattleCameraInfo> protocols = new LinkedBlockingQueue<>(500);
	private List<Long> pveCameras= new ArrayList<Long>(); 
	private Map<Long, Integer> pvetimeCameras= new HashMap<Long, Integer>();
}
