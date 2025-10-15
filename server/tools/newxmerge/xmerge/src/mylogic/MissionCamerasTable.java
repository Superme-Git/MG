package mylogic;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class MissionCamerasTable implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		long id=src_key.unmarshal_long();
		xbean.CameraInfo srcCameraInfo = xbean.Pod.newCameraInfoData(); 
		xbean.CameraInfo dstCameraInfo = xbean.Pod.newCameraInfoData(); 
		srcCameraInfo.unmarshal(src_value);
		dstCameraInfo.unmarshal(dest_value);
		if(srcCameraInfo.getEndtime()<dstCameraInfo.getEndtime()){
			dstCameraInfo.setEndtime(srcCameraInfo.getEndtime()); // 
			dstCameraInfo.setSizebeforezip(srcCameraInfo.getSizebeforezip()); // 
			dstCameraInfo.setSizeafterzip(srcCameraInfo.getSizeafterzip()); // 
			dstCameraInfo.setCamerafileurl(srcCameraInfo.getCamerafileurl()); // 
		}
		
		dest_value.clear();
		dstCameraInfo.marshal(dest_value);
		return true;
	}
	
}