2010-4-01 ����ѧ
ǿ��xmlio,�����в���

���ƣ�
// ����XML�ı�����ԭʼ��XML�ı���ԭ������
// ���ڶ�����XML����ת�����߼��ȼ۵�XML�ַ�����
void CFileReader::SaveAsString( std::wstring& str);

������
// ����Ϊ������XML���ݣ�ת�����ӿڣ�
int CFileWriter::SaveAsOctets( PFS::CDataBlock& rOctetsData );

2009-5-23 ����ѧ
�Ż�xmlio,Ϊ���Ƽ�ʹ�õ�API����deprecated��Ϣ��ʾ��

2009-8-27 ����ѧ
Ӧ�õ����Ķѹ�����ʵ��xmliolib���ڴ����.�Լ���xmliolib�Ķ�������ģ��Ķ�֮��Ľ�����ͻ.�Ӷ�����ڴ�����Ч��.

20090310 ���
CINode ��� GetSubText����

======================================================================================================================================================
��� XML�ļ��Ķ�ȡ������

CFileReader��ȡ�ļ�����

OpenFile				��PFS�е�XML�ļ�
OpenNativeFile				��ԭʼ�ļ��е�XML�ļ�
OpenFile( PFS::CBaseFile& bf);		��PFS�е��ļ�����
OpenData				��һ���ڴ�����

����ɺ�GetRootNode���Եõ����ڵ��Ԫ�ض���CINode

CINode �ķ���
GetType			�õ�Ԫ������
GetChildren 		�õ����е���Ԫ��
GetAttributeNames 	�õ����Ե������б�
GetAttribute		�õ�ĳ�����Ե�ֵ
GetText			�õ��ļ�ֵ
GetName			�õ�Ԫ������

CFileWriterдXML�ļ�������

CONode GetRootNode();				�õ����ڵ�Ԫ��

������ɺ󣬵���
Save( const std::wstring& filename); 		���浽PFS
SaveNative( const std::wstring& filename);	���浽ԭʼĿ¼
Save( PFS::CBaseFile& bf);			���浽PFS�ļ�����
SaveData( GNET::Octets& data);			���浽�ڴ�


CONode�ķ���

bool SetAttribute( const std::wstring& name, const std::wstring& value); 	��������
CONode CreateSubElement( const std::wstring& name);				������Ԫ��
bool InsertText( const std::wstring& text);					�����ı�
bool InsertCData( const std::wstring& data);					���CDATA����
