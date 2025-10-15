package mylogic;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mkdb.util.Dbx;
import mkdb.util.Dbx.Table;
import xmerge.IPreprocess;
import xmerge.XmergeConf;

import com.locojoy.base.Octets;
import com.locojoy.base.Marshal.MarshalException;
import com.locojoy.base.Marshal.OctetsStream;


public class PropertiesPreProcess implements IPreprocess {
	
	class TitleWalker implements mkdb.Storage.IWalk{
		//private long count = 0;
        Table table = null;
        Map<OctetsStream, OctetsStream> modifiedProps = null;
		public TitleWalker(Table src_table,Map<OctetsStream, OctetsStream> props) {
              table = src_table;
              modifiedProps = props;
		}

		@Override
		public boolean onRecord(byte[] key, byte[] data) {
//			++count;
//			if (count > XmergeConf.MAX_WALK_COUNT) {
//				table.save();
//				count = 0;
//			}
			OctetsStream src_key = OctetsStream.wrap(Octets.wrap(key));
			OctetsStream src_value = OctetsStream.wrap(Octets.wrap(data));
			xbean.Properties properties = xbean.Pod.newPropertiesData();
			boolean changed = false;
			try {
				properties.unmarshal(src_value);
				
				//这里注释掉移除鲜花宝贝称谓的代码，让合服的每个服鲜花宝贝称谓留住一天再清除
//				if(properties.getTitles().containsKey(40)){
//					properties.getTitles().remove(40);
//					if(properties.getTitle() == 40){
//						properties.setTitle(-1);
//					}
//					changed = true;
//				}
//				if(properties.getTitles().containsKey(41)){
//					properties.getTitles().remove(41);
//					if(properties.getTitle() == 41){
//						properties.setTitle(-1);
//					}
//					changed = true;
//				}
//				if(properties.getTitles().containsKey(42)){
//					properties.getTitles().remove(42);
//					if(properties.getTitle() == 42){
//						properties.setTitle(-1);
//					}
//					changed = true;
//				}
//				if(properties.getTitles().containsKey(43)){
//					properties.getTitles().remove(43);
//					if(properties.getTitle() == 43){
//						properties.setTitle(-1);
//					}
//					changed = true;
//				}
				
				int kejutitle = 0;
				if (properties.getTitles().containsKey(17)){
					kejutitle = 17;
				}else if (properties.getTitles().containsKey(18)) {
					kejutitle = 18;
				}else if (properties.getTitles().containsKey(19)) {
					kejutitle = 19;
				}else if (properties.getTitles().containsKey(20)) {
					kejutitle = 20;
				}else if (properties.getTitles().containsKey(21)) {
					kejutitle = 21;
				}else if (properties.getTitles().containsKey(22)) {
					kejutitle = 22;
				}else if (properties.getTitles().containsKey(25)) {
					kejutitle = 25;
				}else if (properties.getTitles().containsKey(26)) {
					kejutitle = 26;
				}
				if (kejutitle > 0){
					changed = true;
					properties.getTitles().remove(kejutitle);
					if (properties.getTitle() == kejutitle)
						properties.setTitle(-1);
				}
				src_value.clear();
				properties.marshal(src_value);
			} catch (MarshalException e) {
				
				e.printStackTrace();
			}
			if (changed) 
				modifiedProps.put(src_key, src_value);
			return true;
		}
		
		
		
	}
	
	@Override
	public boolean preProcess(Dbx srcdb, Table src_table, Dbx dstdb, Table dst_table) {

		Map<OctetsStream, OctetsStream> props = new HashMap<OctetsStream, OctetsStream>();
		long count = 0;
		src_table.walk(new TitleWalker(src_table, props));
		if (!props.isEmpty()){
			for (Entry<OctetsStream, OctetsStream> entry : props.entrySet()) {
				++count;
				if (count > XmergeConf.MAX_WALK_COUNT) {
					src_table.save();
					count = 0;
				}
				src_table.replace(entry.getKey(), entry.getValue());
			}
			src_table.save();
		}
		props.clear();
		count = 0;
		dst_table.walk(new TitleWalker(dst_table, props));
		if (!props.isEmpty()){
			for (Entry<OctetsStream, OctetsStream> entry : props.entrySet()) {
				++count;
				if (count > XmergeConf.MAX_WALK_COUNT) {
					dst_table.save();
					count = 0;
				}
				dst_table.replace(entry.getKey(), entry.getValue());
			}
			dst_table.save();
		}
		return true;
	}

}

