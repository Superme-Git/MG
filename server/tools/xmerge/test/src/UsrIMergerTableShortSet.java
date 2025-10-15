package src;

import static org.junit.Assert.assertTrue;

import com.locojoy.base.Marshal.OctetsStream;

import mkdb.util.DatabaseMetaData;

/**
 * table_short_set的用户处理测试
 */


public class UsrIMergerTableShortSet implements xmerge.IMerge {

	@Override
	public boolean merge(OctetsStream src_key, OctetsStream src_value,
			OctetsStream dest_value) throws Exception {
		
		Short key = (Short)DatabaseMetaData.getInstance().getTable("table_short_set")
					.getKeyType().unmarshal(src_key); 
		xbean.varSet srcvalue = (xbean.varSet)DatabaseMetaData.getInstance().getTable("table_short_set")
					.getValueType().unmarshal(src_value);
		xbean.varSet destvalue = (xbean.varSet)DatabaseMetaData.getInstance().getTable("table_short_set")
					.getValueType().unmarshal(dest_value);
		
		assertTrue( srcvalue.getV().contains( Integer.valueOf(key.intValue())) );
		assertTrue( destvalue.getV().contains( Integer.valueOf(key.intValue())) );

		destvalue.getV().clear();
		
		destvalue.getV().add(1234);
		
		dest_value = DatabaseMetaData.getInstance().getTable("table_short_set")
		.getValueType().marshal(destvalue);	
		
		if( key.intValue() % 2 == 1)
		{

			return true;
		}		
		else
			return false;
	}

}