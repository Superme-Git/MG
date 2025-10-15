// 下列 ifdef 块是创建使从 DLL 导出更简单的
//宏的标准方法。此 DLL 中的所有文件都是用命令行上定义的 GXWENCODER2_EXPORTS
// 符号编译的。在使用此 DLL 的
//任何其他项目上不应定义此符号。这样，源文件中包含此文件的任何其他项目都会将 
// GXWENCODER2_API 函数视为是从此 DLL 导入的，而此 DLL 则将用此宏定义的
// 符号视为是被导出的。
#ifdef GXWENCODER2_EXPORTS
#define GXWENCODER2_API __declspec(dllexport)
#else
#define GXWENCODER2_API __declspec(dllimport)
#endif

	struct GxWEncoderParamater
	{
		std::string		m_xmlfilename;
		std::string		m_outputpath;		// 生成cpp文件的输出路径
		std::string		m_includefile;
		std::string		m_cppappand;
		std::string		m_filenameprefix;
		std::string		m_textformatfile;
		std::string		m_namespaces;
		std::string		m_textresxmlfile;
		std::string		m_inlfilepath;		// 生成inl文件的输出路径, 默认同m_outputpath
		bool			m_bCrateAutoptr;
		bool			m_bPakMode;
		int				m_nGenMode;			// 代码生成模式
	};

	class GXWENCODER2_API CGxWEncoder
	{
		GxWEncoderParamater m_param;
	public:
		enum
		{
			eGenMode_Code = 0,				// 生成完整的代码
			eGenMode_Frame,					// 生成框架代码, 以及控件配置接口的调用
			eGenMode_Both,					// 同时生成以上两种代码, 并用宏GXWE_GENERATEMODE_FRAME的定义与否来控制是用frame模式代码还是code模式代码
		};
	public:
		CGxWEncoder();
		virtual ~CGxWEncoder();
	public:
		void SetXmlFileName( const std::string& xmlfilename);
		void SetOutputPath( const std::string& outputpath);
		void SetIncludeFiles( const std::string& includefiles);
		void SetCppAppandCode( const std::string& cppappand);
		void SetFileNamePrefix( const std::string& filenameprefix);
		void SetTextFormatFile( const std::string& textformatfile);
		void SetNamespaces( const std::string& namespaces);
		void SetTextResXmlFile( const std::string& textresxmlfile);
		void SetInlFilePath( const std::string& inlfilepath);
		void SetCreateAutoPtr( bool bCreate);
		void SetPackMode( bool bPackMode);
		void SetGenMode( int mode);

		std::string GetXmlFileName() const;
		std::string GetOutputPath() const;
		std::string GetIncludeFiles() const;
		std::string GetCppAppandCode() const;
		std::string GetFileNamePrefix() const;
		std::string GetTextFormatFile() const;
		std::string GetNamespaces() const;
		std::string GetTextResXmlFile() const;
		std::string GetInlFilePath() const;
		bool GetCreateAutoPtr() const;
		bool GetPackMode() const;
		int GetGenMode() const;

		int Encode();
	};
