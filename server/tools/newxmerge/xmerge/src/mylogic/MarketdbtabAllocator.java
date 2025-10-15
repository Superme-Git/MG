package mylogic;

import xmerge.IAllocator;
import java.util.Random;

public class MarketdbtabAllocator implements IAllocator {

	@Override
	public Object allocateKey(String tableName, Object oldKey) {
		// TODO Auto-generated method stub
		return String.valueOf(new Random().nextLong());
	}

}
