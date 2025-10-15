package mkdb.logs;

/**
 * NOTE:<br>
 * (1) ������OnChanged()�ص�������������������Add�¼�������ʱ����лص���<br>
 *     �����һ�������У�������ɾ������������¸ü�¼�����ǵ����Ҳ���лص���<br>
 *     <-- �����������xdb�Ŀ�����޷���þ�ֵ������޷�����Ϊ����������Note��OnChanged()�ص� --> <br>
 *     ��Ҳ��Ϊʲô������OnChanged()������OnAdded()��ԭ��;-)  <br>
 * (2) ��������OnChanged()��ָ��¼û����ɾ�����Ǽ�¼�����ݱ仯�ˣ��仯��ϸ�ڿ���ͨ��note�õ� <br>
 */
public interface Listener {
	/**
	 * ������¼ �� ���Ǽ�¼ʱ�����лص�
	 */
	public void onChanged(Object key);
	/**
	 * ɾ����¼ʱ�����лص�
	 */
	public void onRemoved(Object key);
	/**
	 * ��¼���˵�ʱ����лص�������Map��Set���͵ļ�¼��߻���Note��Ϣ
	 */
	public void onChanged(Object key, String fullVarName, Note note);
}
