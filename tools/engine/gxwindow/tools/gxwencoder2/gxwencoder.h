// ���� ifdef ���Ǵ���ʹ�� DLL �������򵥵�
//��ı�׼�������� DLL �е������ļ��������������϶���� GXWENCODER2_EXPORTS
// ���ű���ġ���ʹ�ô� DLL ��
//�κ�������Ŀ�ϲ�Ӧ����˷��š�������Դ�ļ��а������ļ����κ�������Ŀ���Ὣ 
// GXWENCODER2_API ������Ϊ�ǴӴ� DLL ����ģ����� DLL ���ô˺궨���
// ������Ϊ�Ǳ������ġ�
#ifdef GXWENCODER2_EXPORTS
#define GXWENCODER2_API __declspec(dllexport)
#else
#define GXWENCODER2_API __declspec(dllimport)
#endif

	struct GxWEncoderParamater
	{
		std::string		m_xmlfilename;
		std::string		m_outputpath;		// ����cpp�ļ������·��
		std::string		m_includefile;
		std::string		m_cppappand;
		std::string		m_filenameprefix;
		std::string		m_textformatfile;
		std::string		m_namespaces;
		std::string		m_textresxmlfile;
		std::string		m_inlfilepath;		// ����inl�ļ������·��, Ĭ��ͬm_outputpath
		bool			m_bCrateAutoptr;
		bool			m_bPakMode;
		int				m_nGenMode;			// ��������ģʽ
	};

	class GXWENCODER2_API CGxWEncoder
	{
		GxWEncoderParamater m_param;
	public:
		enum
		{
			eGenMode_Code = 0,				// ���������Ĵ���
			eGenMode_Frame,					// ���ɿ�ܴ���, �Լ��ؼ����ýӿڵĵ���
			eGenMode_Both,					// ͬʱ�����������ִ���, ���ú�GXWE_GENERATEMODE_FRAME�Ķ����������������frameģʽ���뻹��codeģʽ����
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
