2010-4-01 刘东学
强化xmlio,并进行测试

完善：
// 对于XML文本，将原始的XML文本还原出来。
// 对于二进制XML对象，转换成逻辑等价的XML字符串。
void CFileReader::SaveAsString( std::wstring& str);

新增：
// 保存为二进制XML数据（转换器接口）
int CFileWriter::SaveAsOctets( PFS::CDataBlock& rOctetsData );

2009-5-23 刘东学
优化xmlio,为不推荐使用的API增加deprecated消息提示。

2009-8-27 刘东学
应用单独的堆管理器实现xmliolib的内存管理.以减少xmliolib的堆与其他模块的堆之间的交叠冲突.从而提高内存管理的效率.

20090310 徐辉
CINode 添加 GetSubText方法

======================================================================================================================================================
针对 XML文件的读取操作，

CFileReader读取文件流程

OpenFile				打开PFS中的XML文件
OpenNativeFile				打开原始文件中的XML文件
OpenFile( PFS::CBaseFile& bf);		打开PFS中的文件对象
OpenData				打开一份内存数据

打开完成后，GetRootNode可以得到根节点的元素对象CINode

CINode 的方法
GetType			得到元素类型
GetChildren 		得到所有的子元素
GetAttributeNames 	得到所以的属性列表
GetAttribute		得到某个属性的值
GetText			得到文件值
GetName			得到元素名字

CFileWriter写XML文件的流程

CONode GetRootNode();				得到根节点元素

操作完成后，调用
Save( const std::wstring& filename); 		保存到PFS
SaveNative( const std::wstring& filename);	保存到原始目录
Save( PFS::CBaseFile& bf);			保存到PFS文件对象
SaveData( GNET::Octets& data);			保存到内存


CONode的方法

bool SetAttribute( const std::wstring& name, const std::wstring& value); 	设置属性
CONode CreateSubElement( const std::wstring& name);				创建子元素
bool InsertText( const std::wstring& text);					设置文本
bool InsertCData( const std::wstring& data);					添加CDATA数据
