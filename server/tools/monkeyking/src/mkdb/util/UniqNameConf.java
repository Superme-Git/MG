package mkdb.util;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public final class UniqNameConf {
	private int localId;
	private mkio.MkioConf xioConf;

	public UniqNameConf(Element self) throws Exception {
		localId = Integer.parseInt(self.getAttribute("localId"));
		xioConf = mkio.MkioConf.loadInChildNodes(self);
	}

	/**
	 * �Ӷ����������ļ���װ��Ψһ�����á�
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static UniqNameConf load(String file) throws Exception {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
		return new UniqNameConf(doc.getDocumentElement());
	}

	public mkio.MkioConf getXioConf() {
		return xioConf;
	}

	public int getLocalId() {
		return localId;
	}
}
