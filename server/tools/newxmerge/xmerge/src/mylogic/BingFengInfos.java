package mylogic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import xmerge.IMerge;

import com.locojoy.base.Marshal.OctetsStream;

public class BingFengInfos implements IMerge{

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		
		xbean.BingFengInfo srcBingFengInfo = xbean.Pod.newBingFengInfoData(); 
		xbean.BingFengInfo dstBingFengInfo = xbean.Pod.newBingFengInfoData(); 
		srcBingFengInfo.unmarshal(src_value);
		dstBingFengInfo.unmarshal(dest_value);
		
		
		java.util.Map<Integer, Integer> tempStagetime=new HashMap<Integer, Integer>(); // 记录每个关卡最快通关的时间
		java.util.Map<Integer, Integer> tempStageround=new HashMap<Integer, Integer>(); // 记录每个关卡最快通关的回合数
		java.util.Map<Integer, Long> tempStagebest=new HashMap<Integer, Long>(); // 记录每个关卡最快通关的角色的id
		
		for(Entry<Integer, Long> entry:dstBingFengInfo.getStagebest().entrySet()){
			Integer key=entry.getKey();
			//判断回合，在判断时间
			Integer dstStageround=dstBingFengInfo.getStageround().get(key);
			Integer srcStageround=srcBingFengInfo.getStageround().get(key);
			if(dstStageround==null){
				continue;
			}
			if(srcStageround!=null){
				//比较成绩
				if(srcStageround<dstStageround){
					tempStagetime.put(key, srcBingFengInfo.getStagetime().get(key));
					tempStageround.put(key, srcBingFengInfo.getStageround().get(key));
					tempStagebest.put(key, srcBingFengInfo.getStagebest().get(key));
				}else if(srcStageround==dstStageround){
					Integer dstStagetime=dstBingFengInfo.getStagetime().get(key);
					Integer srcStagetime=srcBingFengInfo.getStagetime().get(key);
					if(srcStagetime<dstStagetime){
						tempStagetime.put(key, srcBingFengInfo.getStagetime().get(key));
						tempStageround.put(key, srcBingFengInfo.getStageround().get(key));
						tempStagebest.put(key, srcBingFengInfo.getStagebest().get(key));
					}
				}
			}
		}
		java.util.Map<Integer, Integer> tempStagetime1=new HashMap<Integer, Integer>(); // 记录每个关卡最快通关的时间
		java.util.Map<Integer, Integer> tempStageround1=new HashMap<Integer, Integer>(); // 记录每个关卡最快通关的回合数
		java.util.Map<Integer, Long> tempStagebest1=new HashMap<Integer, Long>(); // 记录每个关卡最快通关的角色的id
		
		tempStagetime1.putAll(srcBingFengInfo.getStagetime());
		tempStageround1.putAll(srcBingFengInfo.getStageround());
		tempStagebest1.putAll(srcBingFengInfo.getStagebest());
		tempStagetime1.putAll(dstBingFengInfo.getStagetime());
		tempStageround1.putAll(dstBingFengInfo.getStageround());
		tempStagebest1.putAll(dstBingFengInfo.getStagebest());
		tempStagetime1.putAll(tempStagetime);
		tempStageround1.putAll(tempStageround);
		tempStagebest1.putAll(tempStagebest);
		
		dstBingFengInfo.getStagetime().putAll(tempStagetime1);
		dstBingFengInfo.getStageround().putAll(tempStageround1);
		dstBingFengInfo.getStagebest().putAll(tempStagebest1);
		
		//合职业排行
		for(Entry<Integer, xbean.SchoolStageTime> entry:dstBingFengInfo.getSchoolstage().entrySet()){
			int key=entry.getKey();
			xbean.SchoolStageTime value_dst=entry.getValue();
			xbean.SchoolStageTime value_src=srcBingFengInfo.getSchoolstage().get(key);
			if(value_src!=null){
				changeData(value_dst,value_src);
				//把原数据库的值也赋值成目的库，这样后面可以直接覆盖
				value_src.getStagetime().clear();
				value_src.getStagetime().putAll(value_dst.getStagetime());
				value_src.getStagebest().clear();
				value_src.getStagebest().putAll(value_dst.getStagebest());
			}
		}
		
		Map<Integer, xbean.SchoolStageTime> temp=new HashMap<Integer, xbean.SchoolStageTime>();
		temp.putAll(srcBingFengInfo.getSchoolstage());
		temp.putAll(dstBingFengInfo.getSchoolstage());
		dstBingFengInfo.getSchoolstage().putAll(temp);
		
		dest_value.clear();
		dstBingFengInfo.marshal(dest_value);
		
		
		return true;
	}
	
	public void changeData(xbean.SchoolStageTime value_dst,xbean.SchoolStageTime value_src){
		java.util.Map<Integer, Integer> tempStagetime=new HashMap<Integer, Integer>(); // 记录每个关卡最快通关的时间
		java.util.Map<Integer, Integer> tempStageround=new HashMap<Integer, Integer>(); // 记录每个关卡最快通关的回合数
		java.util.Map<Integer, Long> tempStagebest=new HashMap<Integer, Long>(); // 记录每个关卡最快通关的角色的id
		for(Entry<Integer, Long> entry:value_dst.getStagebest().entrySet()){
			Integer key=entry.getKey();
			//判断回合，在判断时间
			Integer dstStageround=value_dst.getStageround().get(key);
			Integer srcStageround=value_src.getStageround().get(key);
			if(dstStageround==null){
				continue;
			}
			if(srcStageround!=null){
				//比较成绩
				if(srcStageround<dstStageround){
					tempStagetime.put(key, value_src.getStagetime().get(key));
					tempStageround.put(key, value_src.getStageround().get(key));
					tempStagebest.put(key, value_src.getStagebest().get(key));
				}else if(srcStageround==dstStageround){
					Integer dstStagetime=value_dst.getStagetime().get(key);
					Integer srcStagetime=value_src.getStagetime().get(key);
					if(srcStagetime<dstStagetime){
						tempStagetime.put(key, value_src.getStagetime().get(key));
						tempStageround.put(key, value_src.getStageround().get(key));
						tempStagebest.put(key, value_src.getStagebest().get(key));
					}
				}
			}
		}
		java.util.Map<Integer, Integer> tempStagetime1=new HashMap<Integer, Integer>(); // 记录每个关卡最快通关的时间
		java.util.Map<Integer, Integer> tempStageround1=new HashMap<Integer, Integer>(); // 记录每个关卡最快通关的回合数
		java.util.Map<Integer, Long> tempStagebest1=new HashMap<Integer, Long>(); // 记录每个关卡最快通关的角色的id
		
		tempStagetime1.putAll(value_src.getStagetime());
		tempStageround1.putAll(value_src.getStageround());
		tempStagebest1.putAll(value_src.getStagebest());
		tempStagetime1.putAll(value_dst.getStagetime());
		tempStageround1.putAll(value_dst.getStageround());
		tempStagebest1.putAll(value_dst.getStagebest());
		tempStagetime1.putAll(tempStagetime);
		tempStageround1.putAll(tempStageround);
		tempStagebest1.putAll(tempStagebest);
		
		value_dst.getStagetime().putAll(tempStagetime1);
		value_dst.getStageround().putAll(tempStageround1);
		value_dst.getStagebest().putAll(tempStagebest1);
		
	}
	
	
	
}