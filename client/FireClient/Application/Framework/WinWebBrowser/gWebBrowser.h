#ifndef __G_WEB_BROWSER__
#define __G_WEB_BROWSER__

class CMyWebBrowser;
class gWebBrowser
{
private:
	gWebBrowser();
	~gWebBrowser();
	static gWebBrowser* m_Inst;

public:
	static gWebBrowser* getInstance();
	static void destroyInstance();
	void init();
	void destroy();

	void setUrl(const char* _url, float sw, float sh);
	void setVisible(bool _show);

	void update();

protected:
	bool				m_listen;
	CMyWebBrowser*		m_pBrowser;
};

#endif // __G_WEB_BROWSER__
