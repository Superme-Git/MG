package fire.pb.battle;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

import mkdb.Procedure;
import xbean.CameraInfo;

import com.locojoy.base.Marshal.OctetsStream;

import fire.pb.battle.livedie.LiveDieMange;
import fire.pb.main.Gs;
import fire.pb.util.OctetsUtil;
import fire.pb.PropConf;
/*
 pvp
 游戏区服ID—录像类型id—客方队长roleid—主方队长roleid-时间
 pve
 游戏区服ID—录像类型id—(任务id,或者关卡ID)—主方队长roleid-时间
 */

public class BattleCameraInfo  extends Procedure{
	public static final Logger logger = Logger.getLogger("BATTLE");
	public static final int MISSION_MAX_CAMERA_SPASE_TIME = 5*60*1000; // 任务录像最小间隔时间
	//pvp从100开始
	public static final int CAMERA_TYPE_MISSION = 1; // 任务
	public static final int CAMERA_TYPE_BINGFENG = 2; // 冰封王座
	public static final int CAMERA_TYPE_LIVEDIE = 100; //生死战
	public static final int CAMERA_TYPE_PVP1 = 101; //

	private OctetsStream stream = null;
	private byte cameraType; // 录像类型id
	private long guestID; // pvp 为客方队长roleid pve时为(任务id,或者关卡ID)
	private long hostID; // 主方队长roleid
	private long endTime; // 时间
	private int costTime; // 战斗时间长度
	private int sizeAfterZip; // zip文件长度
	private byte[] bytes = null;
	
	public BattleCameraInfo(OctetsStream stream, byte cameraType, long guestID,
			long hostID, long endTime, int costTime) {
		this.stream = stream;
		this.cameraType = cameraType;
		this.guestID = guestID;
		this.hostID = hostID;
		this.endTime = endTime;
		this.costTime = costTime;
	}

	public OctetsStream getStream() {
		return stream;
	}

	public byte[] getByte() {
		return bytes;
	}

	public void setLocationID(OctetsStream stream) {
		this.stream = stream;
	}

	public byte getCameraType() {
		return cameraType;
	}

	public void setCameraType(byte cameraType) {
		this.cameraType = cameraType;
	}

	public long getGuestID() {
		return guestID;
	}

	public void setGuestID(int guestID) {
		this.guestID = guestID;
	}

	public long getHostID() {
		return hostID;
	}

	public void setHostID(int hostID) {
		this.hostID = hostID;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getCostTime() {
		return costTime;
	}

	public void setCostTime(int costTime) {
		this.costTime = costTime;
	}

	public int getSizeAfterZip() {
		return sizeAfterZip;
	}

	public void setSizeAfterZip(int sizeAfterZip) {
		this.sizeAfterZip = sizeAfterZip;
	}
	
	public String getCameraFileName() {
		return  Gs.serverid+"_"+cameraType+"_"+guestID+"_"+hostID+"_"+endTime+PropConf.Battle.CAMERA_FILE_ULR_TAIL;
	}
	
	public String getCameraFileUrl() {
		return PropConf.Battle.CAMERA_FILE_ULR_HEAD+getCameraFileName();
	}

	public boolean ZipStream()
	{
		if(bytes != null)
			return true;
		bytes = zip(stream.getBytes());
		return bytes != null;  
	}
	
	public static byte[] zip(byte[] buf) {
		if (buf == null || buf.length == 0)
			return null;		
		ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
		BufferedOutputStream bufferOut = new BufferedOutputStream(byteArrayOut, buf.length);
		GZIPOutputStream zipOut;
		try {
			zipOut = new GZIPOutputStream(bufferOut);
			zipOut.write(buf);
			zipOut.flush();
			zipOut.close();
			return byteArrayOut.toByteArray();
		} catch (IOException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		return null;
	}/**/
	
	public boolean process()
	{ 
		switch(cameraType)
		{
			case CAMERA_TYPE_MISSION:
			{
				CameraInfo cameraInfo = xtable.Missioncameras.get(guestID);   
				if(cameraInfo !=null)
				{
					cameraInfo.setEndtime(getEndTime());
					cameraInfo.setSizebeforezip(stream.getBytes().length);
					cameraInfo.setSizeafterzip(sizeAfterZip);
					cameraInfo.setCamerafileurl(getCameraFileUrl());
				}
				else
				{
					cameraInfo = xbean.Pod.newCameraInfo();
					cameraInfo.setEndtime(getEndTime());
					cameraInfo.setSizebeforezip(stream.getBytes().length);
					cameraInfo.setSizeafterzip(sizeAfterZip);
					cameraInfo.setCamerafileurl(getCameraFileUrl());
					xtable.Missioncameras.insert(guestID, cameraInfo);
				}
			}
			break;
			case CAMERA_TYPE_BINGFENG:
			{
				CameraInfo cameraInfo = xtable.Missioncameras.get(guestID);   
				if(cameraInfo !=null)
				{
					cameraInfo.setEndtime(getCostTime());
					cameraInfo.setSizebeforezip(stream.getBytes().length);
					cameraInfo.setSizeafterzip(sizeAfterZip);
					cameraInfo.setCamerafileurl(getCameraFileUrl());
				}
				else
				{
					cameraInfo = xbean.Pod.newCameraInfo();
					cameraInfo.setEndtime(getCostTime());
					cameraInfo.setSizebeforezip(stream.getBytes().length);
					cameraInfo.setSizeafterzip(sizeAfterZip);
					cameraInfo.setCamerafileurl(getCameraFileUrl());
					xtable.Missioncameras.insert(guestID, cameraInfo);
				}
			}
			break;
			case CAMERA_TYPE_LIVEDIE://生死战
			{
				saveLiveDieInfo();
			}
			break;
			case CAMERA_TYPE_PVP1:
			{
				
			}
			break;
		}
		//BattleField.writeFile(getByte(), "./"+getCameraFileName());
		return true;	
	}
	
	/**
	 * 处理生死战存储逻辑
	 */
	public void saveLiveDieInfo(){
		xbean.LDVideoRoleInfoDes ld=LiveDieMange.ldvideoroleinfodeslistall.get(getCameraFileName());
		if(ld!=null){
			ld.setSaveresult(1);//设置为保存
			ld.setBeforevideosize(stream.getBytes().length);
			ld.setAftervideosize(sizeAfterZip);
			ld.setVideourl(getCameraFileUrl());
			logger.info("生死战录像保存，保存已经上传到录像服务器，双方对手id"+hostID+"\t"+guestID+"\t录像id"+getCameraFileName());
			
			//更新数据库值
			mkdb.Procedure proc=new mkdb.Procedure(){
				@Override
				protected boolean process() throws Exception {
					xbean.LDVideoRoleInfoDesList lDVideoRoleInfoDesList=xtable.Ldvideoroleinfodeslisttab.get(1);
					if(lDVideoRoleInfoDesList!=null&&lDVideoRoleInfoDesList.getLdvideoroleinfodeslistall()!=null){
						for(xbean.LDVideoRoleInfoDes desdb:lDVideoRoleInfoDesList.getLdvideoroleinfodeslistall()){
							if(desdb.getVideoid().equals(getCameraFileName())){
								desdb.setSaveresult(1);//设置为保存
								desdb.setBeforevideosize(stream.getBytes().length);
								desdb.setAftervideosize(sizeAfterZip);
								desdb.setVideourl(getCameraFileUrl());
								break;
							}
						}
					}
					return true;
				}
			};
			mkdb.Procedure.pexecute(proc);
		}else{
			logger.error("保存生死战录像错误,录像名称\t"+this.getCameraFileName());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
