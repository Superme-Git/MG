package locojoy.game.mt3.task;

/**
 * 磁盘写文件异步自行线程
 * 
 * @author liangyanpeng
 *
 */
public class StorageTask2 extends DiskStorage {
	
	protected byte[] fileBytes;
	
	public StorageTask2(byte[] fileBytes, String filePath,String fileName) {
		super();
		super.fileName = fileName;
		super.filePath = filePath;
		this.fileBytes = fileBytes;
	}

	@Override
	public void asyncMethod() {
		storageDisk(fileBytes, null);
	}

}
