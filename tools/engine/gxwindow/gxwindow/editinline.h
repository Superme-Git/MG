#pragma once

namespace GXWINDOW { 
namespace INLINECLASS {

class CWCharWidth
{
	struct key
	{
		int		font;
		wchar_t	wt;
		
		bool operator<( const key& dst) const
		{
			if( font == dst.font)
				return wt < dst.wt;
			else
				return font < dst.font;
		}
		key( int f, int w) : font( f), wt( w) {}
	};
	typedef std::map<key,int> CWidthMap;
	typedef std::map<int,int> CHeightMap;

	mutable	CWidthMap	m_wmap;
	mutable	CHeightMap	m_hmap;
public:
	CWCharWidth() {	}
	~CWCharWidth() { }
public:
	int GetWCharWidth( const wchar_t wt, int font) const
	{
		key		k( font, wt);
		CWidthMap::const_iterator it = m_wmap.find( k);
		if( it == m_wmap.end())
		{
			int nWidth = CTextDrawManager::GetInstance().GetCharPixelWidth( wt, font);
			m_wmap.insert( std::make_pair( k, nWidth));
			return nWidth;
		}
		return it->second;
	}
	int GetFontHeight( int font) const
	{
		CHeightMap::const_iterator it = m_hmap.find( font);
		if( it == m_hmap.end())
		{
			int nHeight = CTextDrawManager::GetInstance().GetFontHeight( font);
			m_hmap.insert( std::make_pair( font, nHeight));
			return nHeight;
		}
		return it->second;
	}
	void Clear()
	{
		m_wmap.clear();
		m_hmap.clear();
	}
};

struct DividableString
{
public:
	std::wstring			m_str;
public:
	DividableString( const std::wstring& str)
		: m_str( str)
	{
	}
	bool IsUnempty()
	{
		return !m_str.empty();
	}
	bool IsEmpty()
	{
		return m_str.empty();
	}
	std::wstring GetString( int nWidth, int font, const CWCharWidth& ww, int& nout)
	{
		nout = 0;
		std::wstring::size_type count = m_str.size();

		for( std::wstring::size_type i = 0; i < count; i ++)
		{
			wchar_t& c = m_str[i];
			int cw = ww.GetWCharWidth( c, font);
			if( nout + cw < nWidth)
			{
				nout += cw;
				continue;
			}
			if( 0 == i)
				return L"";
			std::wstring strout = m_str.substr( 0, i);
			m_str = m_str.substr( i);
			return strout;
		}
		std::wstring strout = m_str;
		m_str.erase();
		return strout;
	}
	static void GetStringSize( int font, const std::wstring& str, const CWCharWidth& ww, int& w, int& h)
	{
		h = ww.GetFontHeight( font);
		w = GetStringWidth( font, str, ww);
	}
	static int GetStringWidth( int font, const std::wstring& str, const CWCharWidth& ww)
	{
		int nout = 0;
		std::wstring::size_type count = str.size();
		for( std::wstring::size_type i = 0; i < count; i ++)
		{
			const wchar_t& c = str[i];
			nout += ww.GetWCharWidth( c, font);
		}
		return nout;
	}
	static int GetStringBorderPos( int cw, int font, const std::wstring& str, const CWCharWidth& ww)
	{
		int nout = 0;
		std::wstring::size_type count = str.size();
		for( std::wstring::size_type i = 0; i < count; i ++)
		{
			const wchar_t& c = str[i];
			int ow = ww.GetWCharWidth( c, font);
			int nr = nout + ow;
			if( cw >= nout && cw < nr)
			{
				if( (cw - nout) < ( ow / 2 ) )
					return nout;
				else
					return nr;
			}
			nout = nr;
		}
		return nout;

	}
	static void StringAppendChar( std::wstring& str, wchar_t wc, bool bPassword)
	{
		if( bPassword)
		{
			str.append( 1, L'*');
			if( wc > 255)
				str.append( 1, L'*');
		}
		else
			str.append( 1, wc);
	}
};

class CEditElementObject
{
public:
	wchar_t			m_char;
	int				m_nIndex;
	int				m_nBackgroundEffect;
	bool			m_bSelected;
public:
	CEditElementObject()
		: m_nBackgroundEffect( 0), m_bSelected( false)
	{
	}
	~CEditElementObject()
	{
	}
};

class CEditElementVector : public std::list<CEditElementObject>
{
public:
	CEditElementVector() {}
	virtual ~CEditElementVector() {}
};

class CLineObject : public CEditElementVector
{
public:
	int			m_nHeight;
public:
	CLineObject() {}
	~CLineObject() {}
public:
	void SetHeight( int h)
	{
		if( m_nHeight < h)
			m_nHeight = h;
	}
};

typedef std::list<CLineObject> CContainerObject;

class CEditElementContainer   
{
	class CEditElementFormat : public CDecoderTextPiece
	{
		int						m_nRefCount;
	public:
		CEditElementFormat( const CDecoderTextPiece& dtp)
			: CDecoderTextPiece( dtp), m_nRefCount( 0)
		{
		}
		~CEditElementFormat()
		{
		}
	public:
		int AddRef()
		{
			m_nRefCount ++;
			return m_nRefCount;
		}
		int Release()
		{
			m_nRefCount --;
			return m_nRefCount;
		}
	};

	class CEditElementFormatMap 
	{
		int		m_nId;
		typedef std::map<int,CEditElementFormat*>		CEEFMap;

		CEEFMap		m_map;
	public:
		CEditElementFormatMap()
			: m_nId( 0)
		{
		}
		~CEditElementFormatMap()
		{
			Clear();
		}
	private:
		int GetID() 
		{
			return ++ m_nId;
		}
	public:
		int NewFormat( const CDecoderTextPiece& dtp)
		{
			int nID = GetID();
			m_map[ nID] = new CEditElementFormat( dtp);
			return nID;
		}
		CEditElementFormat* GetEditElementFormat( int nID) const
		{
			CEEFMap::const_iterator it = m_map.find( nID);
			if( it == m_map.end())
				return NULL;
			return it->second;
		}
		void ReleaeEditElementFormat( int nID)
		{
			CEEFMap::iterator it = m_map.find( nID);
			if( it == m_map.end())
				return;
			CEditElementFormat*& eef = it->second;

			if( eef->Release() <= 0)
			{
				delete eef;
				m_map.erase( it);
			}
		}
		void Clear()
		{
			for( CEEFMap::iterator it = m_map.begin(); it != m_map.end(); ++ it)
			{
				CEditElementFormat*& eef = it->second;
				delete eef;
			}
			m_map.clear();
		}
	};

	CEditElementFormatMap		m_eefm;
	CWCharWidth					m_ww;
	int							m_nCursorX;
	int							m_nCursorY;

	int			m_nIDReturn;
	int			m_nIDDefault;
public:
	CContainerObject			m_ees;
public:
	CEditElementContainer()
		: m_nCursorX( 0), m_nCursorY( 0)
	{
		InitDefaultEEFM();
	}
	~CEditElementContainer()
	{
	}
private:
	void InitDefaultEEFM()
	{
		{
			m_nIDReturn = m_eefm.NewFormat( CDecoderTextPiece());
			CEditElementFormat* eef = m_eefm.GetEditElementFormat( m_nIDReturn);
			assert( eef);
			eef->AddRef();
		}

		{
			m_nIDDefault = m_eefm.NewFormat( CDecoderTextPiece( L""));
			CEditElementFormat* eef = m_eefm.GetEditElementFormat( m_nIDDefault);
			assert( eef);
			eef->AddRef();
		}
	}
	CLineObject* NewLine( int nDefaultHeight = 10)
	{
		m_ees.push_back( CLineObject());
		CLineObject& lo = m_ees.back();
		lo.m_nHeight = nDefaultHeight;
		return &lo;
	}
	int GetCursorPos()
	{
		int x = m_nCursorX;
		int y = m_nCursorY;
		int nCount = 0;
		
		for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
		{
			CEditElementVector& eev = *it;

			if( y > 0)
			{
				nCount += (int)eev.size();
				y --;
				continue;
			}
			assert( 0 == y);
			nCount += x;
			return nCount;
		} // for( iterator it = begin(); it != end(); ++ it)
		return nCount;
	}
	void ResetCursor( int n)
	{
		m_nCursorX = 0;
		m_nCursorY = 0;

		size_t index = 0;
		for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it, ++ index)
		{
			CEditElementVector& eev = *it;

			int nCount = (int)eev.size();
			if( n > nCount)
			{
				if ( index != m_ees.size() - 1)
				{
					n -= nCount;
					m_nCursorY ++;
					continue;
				}
				else
				{
					m_nCursorX = n - 1;
					break;
				}
			}
			else if( n == nCount)
			{
				if( 0 == n)
				{
					m_nCursorX = 0;
					break;
				}
				assert( !eev.empty());
				CEditElementObject& ee = eev.back();
				//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
				CEditElementFormat* eef = _GetEditElementFormat( ee.m_nIndex);
				assert( eef);
				if( DTPT_RETURN == eef->GetType())
				{
					m_nCursorY ++;
					m_nCursorX = 0;
				}
				else
				{
					m_nCursorX = n;
				}
				break;
			}
			else
			{
				m_nCursorX = n;
				break;
			}
		} // for( iterator it = begin(); it != end(); ++ it)
	}
	void InsertEditElement( const CDecoderTextPiece& dtp, CLineObject& line, int nLimit = 0, int nBackgroundEffect = 0)
	{
		int left = nLimit - GetCharCount();
		if ( left <= 0 && nLimit > 0) 
			return;

		DTP_TYPE type = dtp.GetType();
		switch( type)
		{
		case DTPT_RETURN:
			{
				CEditElementObject	ee;
				//CEditElementFormat* eef = m_eefm.GetEditElementFormat( m_nIDReturn);
				CEditElementFormat* eef = _GetEditElementFormat( m_nIDReturn);
				assert( eef);
				ee.m_nIndex = m_nIDReturn;
				ee.m_nBackgroundEffect = nBackgroundEffect;
				eef->AddRef();
				line.push_back( ee);
				--left;
			}
			break;
		case DTPT_COMPLEX:
		case DTPT_FORMAT_TEXT:
		case DTPT_DEFAULT_TEXT:
			{
				int nid = m_nIDDefault;
				if( DTPT_DEFAULT_TEXT != type)
					nid = m_eefm.NewFormat( dtp);
				//CEditElementFormat* eef = m_eefm.GetEditElementFormat( nid);
				CEditElementFormat* eef = _GetEditElementFormat( nid);
				assert( eef);
				
				CEditElementObject		ee;
				int nCount = nLimit == 0 ? dtp.GetTextLength() : min( dtp.GetTextLength(), left);
				const wchar_t* text = dtp.GetText();
				for( int i = 0; i < nCount; i ++)
				{
					eef->AddRef();
					ee.m_nIndex = nid;
					ee.m_char = text[i];
					ee.m_nBackgroundEffect = nBackgroundEffect;
					line.push_back( ee);
					--left;
				}
			}
			break;
		case DTPT_STATIC_IMAGE:
		case DTPT_ANIMATE_IMAGE:
		case DTPT_BUTTON:
		case DTPT_EMOTION_IMAGE:
		case DTPT_CUSTOM_PIECE:
		{
				int nid = m_eefm.NewFormat( dtp);
				//CEditElementFormat* eef = m_eefm.GetEditElementFormat( nid);
				CEditElementFormat* eef = _GetEditElementFormat( nid);
				assert( eef);

				CEditElementObject		ee;
				ee.m_nIndex = nid;
				ee.m_nBackgroundEffect = nBackgroundEffect;
				eef->AddRef();
				line.push_back( ee);
				--left;
			}
			break;
		default:
			assert( false);
			break;
		}
	}
	void SeparatorFromCursor()
	{
		CLineObject	tempa;
		CLineObject	tempb;

		int nCursor = GetCursorPos();
		int nCursorStore = nCursor;
		CLineObject* plo = &tempa;
		for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
		{
			CEditElementVector& eev = *it;
			int nCount = (int)eev.size();
			if( -1 == nCursor)
			{
				tempb.insert( tempb.end(), eev.begin(), eev.end());
			}
			else if( nCursor > nCount)
			{
				tempa.insert( tempa.end(), eev.begin(), eev.end());
				nCursor -= nCount;
			}
			else if( nCursor == nCount)
			{
				nCursor = -1;
				tempa.insert( tempa.end(), eev.begin(), eev.end());
			}
			else
			{
				for( CEditElementVector::iterator itv = eev.begin(); itv != eev.end(); ++ itv)
				{
					CEditElementObject& ee = *itv;

					if( 0 == nCursor)
					{
						tempb.insert( tempb.end(), itv, eev.end());
						nCursor = -1;
						break;
					}
					else
					{
						tempa.push_back( ee);
						nCursor --;
					}
				} // for( CEditElementVector::iterator itv = eev.begin(); itv != eev.end(); ++ itv)
			}
		} // for( iterator it = begin(); it != end(); ++ it)
		m_ees.clear();
		m_ees.push_back( tempa);
		m_ees.push_back( tempb);
		ResetCursor( nCursorStore);
		assert( ( 0 == m_nCursorY && (int)tempa.size() == m_nCursorX) || ( 1 == m_nCursorY && 0 == m_nCursorX));
	}
	int GetElementWidth( const CEditElementObject& ee, int nDefaultFont, bool bPassword) const
	{
		//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
		CEditElementFormat* eef = _GetEditElementFormat( ee.m_nIndex);
		assert( eef);

		switch( eef->GetType())
		{
		case DTPT_DEFAULT_TEXT:
			if( bPassword)
				return m_ww.GetWCharWidth( L'*', nDefaultFont);
			else
				return m_ww.GetWCharWidth( ee.m_char, nDefaultFont);
		case DTPT_FORMAT_TEXT:
		case DTPT_COMPLEX:
			return m_ww.GetWCharWidth( ee.m_char, eef->GetTextFont());
		case DTPT_STATIC_IMAGE:
		case DTPT_ANIMATE_IMAGE:
		case DTPT_EMOTION_IMAGE:
		case DTPT_CUSTOM_PIECE:
			return eef->GetImageWidth();
		case DTPT_BUTTON:
			return DividableString::GetStringWidth( eef->GetTextFont(), eef->GetText(), m_ww);
		case DTPT_RETURN:
			return 0;
		default:
			break;
		}
		assert( false);
		return -1;
	}
	void CheckCursorPositionOneLine( CLineObject& lo, int& x, int nDefaultFont, bool bPassword)
	{
		if( lo.empty())
		{
			x = 0;
			return;
		}
		int nx = 0;
		m_nCursorX = 0;
		for( CLineObject::iterator it = lo.begin(); it != lo.end(); ++ it, m_nCursorX ++)
		{
			CEditElementObject& ee = *it;
			int nWidth = GetElementWidth( ee, nDefaultFont, bPassword);
			assert( -1 != nWidth);
			int nr = nx + nWidth;

			if( x >= nx && x < nr)
			{
				nWidth /= 2;
				if( x < (nx + nWidth))
					x = nx;
				else
				{
					x = nr;
					m_nCursorX ++;
				}
				return;
			}
			nx = nr;
		} // for( CTextAreaLine::const_iterator it = tal.begin(); it != tal.end(); ++ it)

		int nCount = (int)lo.size();
		if( nCount == m_nCursorX)
		{
			CEditElementObject& ee = lo.back();
			//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
			CEditElementFormat* eef = _GetEditElementFormat( ee.m_nIndex);
			assert( eef);

			if( DTPT_RETURN == eef->GetType())
				m_nCursorX = nCount - 1;
		}
		x = nx;

		INVALIDATE();
	}
	void CheckCursorCharPositionOneLine( CLineObject& lo, int col, int& x, int nDefaultFont, bool bPassword)
	{
		if( lo.empty())
		{
			x = 0;
			return;
		}
		int nx = 0;
		m_nCursorX = 0;
		for( CLineObject::iterator it = lo.begin(); it != lo.end(); ++ it, m_nCursorX ++)
		{
			if( m_nCursorX >= col)
			{
				x = nx;
				return;
			}

			CEditElementObject& ee = *it;
			int nWidth = GetElementWidth( ee, nDefaultFont, bPassword);
			assert( -1 != nWidth);
			nx += nWidth;
		} // for( CTextAreaLine::const_iterator it = tal.begin(); it != tal.end(); ++ it)

		int nCount = (int)lo.size();
		if( nCount == m_nCursorX)
		{
			CEditElementObject& ee = lo.back();
			//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
			CEditElementFormat* eef = _GetEditElementFormat( ee.m_nIndex);
			assert( eef);

			if( DTPT_RETURN == eef->GetType())
				m_nCursorX = nCount - 1;
		}
		x = nx;
	}
	void GetCursorPositionOneLine( const CLineObject& lo, int& x, int nDefaultFont, bool bPassword) const
	{
		x = 0;
		int cx = m_nCursorX;
		CLineObject::const_iterator it = lo.begin();
		while( cx)
		{
			const CEditElementObject& ee = *it;
			x += GetElementWidth( ee, nDefaultFont, bPassword);
			cx --;
			++ it;
		}
	}
	void SetOneLineSelectFromTo( int xs, int xe, CLineObject& lo, int nDefaultFont, bool bPassword)
	{
		if ( xs == xe) return;

		int						nx = 0;
		CLineObject::iterator	itStart = lo.end();
		CLineObject::iterator	itEnd = lo.end();

		for( CLineObject::iterator it = lo.begin(); it != lo.end(); ++ it)
		{
			CEditElementObject& ee = *it;
			int nWidth = GetElementWidth( ee, nDefaultFont, bPassword);
			assert( -1 != nWidth);
			int nr = nx + nWidth;

			if( xs >= nx && xs < nr)
			{
				int nMiddle = nx + nWidth / 2;
				itStart = it;
				if( xs > nMiddle)
					++ itStart;
			}
			if( xe >= nx && xe < nr)
			{
				int nMiddle = nx + nWidth / 2;
				itEnd = it;
				if( xe < nMiddle && itEnd != itStart)
					-- itEnd;
				break;
			}
			nx = nr;
		} // for( CLineObject::iterator it = lo.begin(); it != lo.end(); ++ it, m_nCursorX ++)

		if( itStart == lo.end())
			return;
		for( ; itStart != itEnd; ++ itStart)
		{
			CEditElementObject& ee = *itStart;
			ee.m_bSelected = true;
		}
		if( itEnd != lo.end())
		{
			CEditElementObject& ee = *itEnd;
			ee.m_bSelected = true;
		}
	}
	void SetOneLineSelectFrom( int x, CLineObject& lo, int nDefaultFont, bool bPassword)
	{
		int						nx = 0;
		CLineObject::iterator	itStart = lo.end();

		for( CLineObject::iterator it = lo.begin(); it != lo.end(); ++ it)
		{
			CEditElementObject& ee = *it;
			int nWidth = GetElementWidth( ee, nDefaultFont, bPassword);
			assert( -1 != nWidth);
			int nr = nx + nWidth;

			if( x >= nx && x < nr)
			{
				int nMiddle = nx + nWidth / 2;
				itStart = it;
				if( x > nMiddle)
					++ itStart;
				break;
			}
			nx = nr;
		} // for( CLineObject::iterator it = lo.begin(); it != lo.end(); ++ it, m_nCursorX ++)
		
		if( itStart == lo.end())
			return;
		for( ; itStart != lo.end(); ++ itStart)
		{
			CEditElementObject& ee = *itStart;
			ee.m_bSelected = true;
		}
	}
	void SetOneLineSelectAll( CLineObject& lo)
	{
		for( CLineObject::iterator it = lo.begin(); it != lo.end(); ++ it)
		{
			CEditElementObject& ee = *it;
			ee.m_bSelected = true;
		} // for( CLineObject::iterator it = lo.begin(); it != lo.end(); ++ it)
	}
	void SetOneLineSelectTo( int x, CLineObject& lo, int nDefaultFont, bool bPassword)
	{
		int						nx = 0;
		for( CLineObject::iterator it = lo.begin(); it != lo.end(); ++ it)
		{
			CEditElementObject& ee = *it;
			ee.m_bSelected = true;

			int nWidth = GetElementWidth( ee, nDefaultFont, bPassword);
			assert( -1 != nWidth);
			int nr = nx + nWidth;
			if( x >= nx && x < nr)
			{
				int nMiddle = nx + nWidth / 2;
				if( x < nMiddle)
					ee.m_bSelected = false;
				return;
			}
			nx = nr;
		} // for( CLineObject::iterator it = lo.begin(); it != lo.end(); ++ it, m_nCursorX ++)
	}
	void RemoveComplexComponent( int nIndex)
	{
		assert( 2 == m_ees.size());
		{
			CLineObject& lo = m_ees.front();
			while( !lo.empty()) 
			{
				CEditElementObject& ee = lo.back();
				if( nIndex != ee.m_nIndex)
					break;
				lo.erase( --lo.end());
			}
		}
		{
			CLineObject& lo = m_ees.back();
			while( !lo.empty()) 
			{
				CEditElementObject& ee = lo.front();
				if( nIndex != ee.m_nIndex)
					break;
				lo.erase( lo.begin());
			}
		}
	}
	void CheckCreateTextPiece( int& nIndex, std::wstring& str, CDecoderTextPieceList& tpl)
	{
		if( -1 == nIndex)
			return;

		//CEditElementFormat* eef = m_eefm.GetEditElementFormat( nIndex);
		CEditElementFormat* eef = _GetEditElementFormat( nIndex);
		assert( eef);
		DTP_TYPE	dtpt = eef->GetType();

		if( DTPT_DEFAULT_TEXT == dtpt)
		{
			tpl.Insert( CDecoderTextPiece( str.c_str()));
		}
		else if( DTPT_COMPLEX == dtpt)
		{
			//assert( 0 == wcscmp( eef->GetText(), str.c_str()));
			tpl.Insert( *eef);
		}
		else if( DTPT_FORMAT_TEXT == dtpt)
		{
			tpl.Insert( CDecoderTextPiece( str.c_str(), eef->GetTextFont(), eef->GetTextFormatID(), eef->GetTextColor(), eef->GetTextGradualColor(), 
				eef->GetTextSelectColor(), eef->GetTextSelectGradualColor(), eef->GetTextSelectBKColor()));
		}
		else
		{
			assert( false);
		}

		nIndex = -1;
		str.erase();
	};

	// TODO: 这只是一个临时的函数, 梦诛反馈, 这个地方eef有可能为空, 原因还需继续关注, 临时使用默认id重新取一遍指针
	inline CEditElementFormat* _GetEditElementFormat( int nId) const
	{
		CEditElementFormat* eef = m_eefm.GetEditElementFormat( nId);
		if ( !eef)
			eef = m_eefm.GetEditElementFormat( m_nIDDefault);
		return eef;
	}
public:
	CWCharWidth& GetWCharWidth() { return m_ww; }
	const CDecoderTextPiece& GetDecoderTextPieceByEditElement( const CEditElementObject& ee) const
	{
		//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
		CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
		assert( eef);
		return *eef;
	}
	struct FindSpace : std::binary_function<CEditElementObject, wchar_t, bool>
	{
		bool operator() (CEditElementObject& _1st, wchar_t _2nd) const
		{
			return (result_type)(_2nd == _1st.m_char);
		}
	};
	void RecalculateWordBreak( int nLineWidth, int nDefaultFont, bool bSingle, bool bPassword, bool bFullRecalc)
	{
		CEditElementVector	temp;

		int nCursor = GetCursorPos();
		if ( bFullRecalc)
		{
			for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
			{
				CEditElementVector& eev = *it;
				temp.insert( temp.end(), eev.begin(), eev.end());
			} // for( iterator it = begin(); it != end(); ++ it)
			m_ees.clear();
		}
		else
		{
			if ( !m_ees.empty())
			{
				CEditElementVector& eev = m_ees.back();
				temp.insert( temp.end(), eev.begin(), eev.end());
				m_ees.pop_back();
			}
		}

		CLineObject*			pLine = NULL;
		CLineObject				t_xLine;
		int						lw = 0;
		int						nLineDefaultHeight = m_ww.GetFontHeight( nDefaultFont);
		bool					bComplexStarted = false;

		bool					bHaveNotSpace = false;//是否已经含有非空格字符
		bool					bHaveOneWord = false;//是否已经有一个单词
		
		for( CEditElementVector::iterator it = temp.begin(); it != temp.end(); ++ it)
		{
			CEditElementObject& ee = *it;
			//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
			CEditElementFormat* eef = _GetEditElementFormat( ee.m_nIndex);
			assert( eef);

			int nWidth = -1;
			int nHeight = -1;
			switch( eef->GetType())
			{
			case DTPT_DEFAULT_TEXT:
				{
					nHeight = m_ww.GetFontHeight( nDefaultFont);
					if( bPassword)
						nWidth = m_ww.GetWCharWidth( L'*', nDefaultFont);
					else
						nWidth = m_ww.GetWCharWidth( ee.m_char, nDefaultFont);
					bComplexStarted = false;
				}
				break;
			case DTPT_FORMAT_TEXT:
				{
					int font = eef->GetTextFont();
					nHeight = m_ww.GetFontHeight( font);
					nWidth = m_ww.GetWCharWidth( ee.m_char, font);
					bComplexStarted = false;
				}
				break;
			case DTPT_COMPLEX:
				{
					if( !bComplexStarted)
					{
						bComplexStarted = true;
						int nAllWidth = DividableString::GetStringWidth( eef->GetTextFont(), std::wstring( eef->GetText(), eef->GetTextLength()), m_ww);
						if( !bSingle && ( ( lw + nAllWidth ) > nLineWidth ) )
						{
							pLine = NewLine( nLineDefaultHeight);
							lw = 0;
							bHaveOneWord = false;
							bHaveNotSpace = false;
						}
					}
					int font = eef->GetTextFont();
					nHeight = m_ww.GetFontHeight( font);
					nWidth = m_ww.GetWCharWidth( ee.m_char, font);
				}
				break;
			case DTPT_STATIC_IMAGE:
			case DTPT_ANIMATE_IMAGE:
			case DTPT_EMOTION_IMAGE:
			case DTPT_CUSTOM_PIECE:
				nWidth = eef->GetImageWidth();
				nHeight = eef->GetImageHeight(); // bugfix by llq 08.07.25
				bComplexStarted = false;
				break;
			case DTPT_BUTTON:
				{
					nWidth = eef->GetButtonImageWidth();
					nHeight = eef->GetButtonImageHeight(); //CTextDrawManager::GetInstance().GetFontHeight( eef->GetTextFont());
					if( lw || !pLine)
					{
						pLine = NewLine( nLineDefaultHeight);
						bHaveOneWord = false;
						bHaveNotSpace = false;
					}
					//pLine->push_back( ee);
					//continue;
				}
				break;;
			case DTPT_RETURN:
				nWidth = 0;
				nHeight = CTextDrawManager::GetInstance().GetFontHeight( nDefaultFont);
				bComplexStarted = false;
				break;
			default:
				assert( false);
				break;
			}
			assert( -1 != nWidth && -1 != nHeight);

			if(ee.m_char != L' ')
			{
				bHaveNotSpace = true;
			}
			if(true == bHaveNotSpace)
			{
				if(ee.m_char == L' ')
					bHaveOneWord = true;
			}

			if( NULL == pLine || ( !bSingle && ( lw + nWidth) > nLineWidth) )
			{
				if(NULL != pLine)
				{
					CEditElementVector::reverse_iterator itSpace = std::find_if(pLine->rbegin(), pLine->rend(), std::bind2nd(FindSpace(), L' '));
					if(true == bHaveOneWord && itSpace != pLine->rend())
					{
						pLine->erase(itSpace.base(), pLine->end());
						CEditElementVector::reverse_iterator itTemp = CEditElementVector::reverse_iterator(it);
						CEditElementVector::reverse_iterator itSpace1 = std::find_if(itTemp, temp.rend(), std::bind2nd(FindSpace(), L' '));
						it = itSpace1.base();
					//	ee = *it;
					}
				}
				lw = 0;
				bHaveOneWord = false;
				bHaveNotSpace = false;
			
				pLine = NewLine( nLineDefaultHeight);
			}
//			pLine->push_back( ee);
			pLine->push_back( *it);
			lw += nWidth;
			pLine->SetHeight( nHeight);

			if( !bSingle && DTPT_RETURN == eef->GetType())
			{
				lw = 0;
				pLine = NewLine( nLineDefaultHeight);
				bHaveOneWord = false;
				bHaveNotSpace = false;
			}
		} // for( CEditElementVector::iterator it = temp.begin(); it != temp.end(); ++ it)

		ResetCursor( nCursor);
	}
	void Recalculate( int nLineWidth, int nDefaultFont, bool bSingle, bool bPassword, bool bFullRecalc)
	{
		if(true == CTextAreaProperties::GetEnableWordBreak())
		{
			RecalculateWordBreak(nLineWidth, nDefaultFont, bSingle, bPassword, bFullRecalc);
			return;
		}
		CEditElementVector	temp;

		int nCursor = GetCursorPos();
		if ( bFullRecalc)
		{
			for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
			{
				CEditElementVector& eev = *it;
				temp.insert( temp.end(), eev.begin(), eev.end());
			} // for( iterator it = begin(); it != end(); ++ it)
			m_ees.clear();
		}
		else
		{
			if ( !m_ees.empty())
			{
				CEditElementVector& eev = m_ees.back();
				temp.insert( temp.end(), eev.begin(), eev.end());
				m_ees.pop_back();
			}
		}

		CLineObject*			pLine = NULL;
		int						lw = 0;
		int						nLineDefaultHeight = m_ww.GetFontHeight( nDefaultFont);
		bool					bComplexStarted = false;

		for( CEditElementVector::iterator it = temp.begin(); it != temp.end(); ++ it)
		{
			CEditElementObject& ee = *it;
			//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
			CEditElementFormat* eef = _GetEditElementFormat( ee.m_nIndex);
			assert( eef);

			int nWidth = -1;
			int nHeight = -1;
			switch( eef->GetType())
			{
			case DTPT_DEFAULT_TEXT:
				{
					nHeight = m_ww.GetFontHeight( nDefaultFont);
					if( bPassword)
						nWidth = m_ww.GetWCharWidth( L'*', nDefaultFont);
					else
						nWidth = m_ww.GetWCharWidth( ee.m_char, nDefaultFont);
					bComplexStarted = false;
				}
				break;
			case DTPT_FORMAT_TEXT:
				{
					int font = eef->GetTextFont();
					nHeight = m_ww.GetFontHeight( font);
					nWidth = m_ww.GetWCharWidth( ee.m_char, font);
					bComplexStarted = false;
				}
				break;
			case DTPT_COMPLEX:
				{
					if( !bComplexStarted)
					{
						bComplexStarted = true;
						int nAllWidth = DividableString::GetStringWidth( eef->GetTextFont(), std::wstring( eef->GetText(), eef->GetTextLength()), m_ww);
						if( !bSingle && ( ( lw + nAllWidth ) > nLineWidth ) )
						{
							pLine = NewLine( nLineDefaultHeight);
							lw = 0;
						}
					}
					int font = eef->GetTextFont();
					nHeight = m_ww.GetFontHeight( font);
					nWidth = m_ww.GetWCharWidth( ee.m_char, font);
				}
				break;
			case DTPT_STATIC_IMAGE:
			case DTPT_ANIMATE_IMAGE:
			case DTPT_EMOTION_IMAGE:
			case DTPT_CUSTOM_PIECE:
				nWidth = eef->GetImageWidth();
				nHeight = eef->GetImageHeight(); // bugfix by llq 08.07.25
				bComplexStarted = false;
				break;
			case DTPT_BUTTON:
				{
					nWidth = eef->GetButtonImageWidth();
					nHeight = eef->GetButtonImageHeight(); //CTextDrawManager::GetInstance().GetFontHeight( eef->GetTextFont());
					if( lw || !pLine)
						pLine = NewLine( nLineDefaultHeight);
					//pLine->push_back( ee);
					//continue;
				}
				break;;
			case DTPT_RETURN:
				nWidth = 0;
				nHeight = CTextDrawManager::GetInstance().GetFontHeight( nDefaultFont);
				bComplexStarted = false;
				break;
			default:
				assert( false);
				break;
			}
			assert( -1 != nWidth && -1 != nHeight);

			if( NULL == pLine || ( !bSingle && ( lw + nWidth) > nLineWidth) )
			{
				lw = 0;
				pLine = NewLine( nLineDefaultHeight);
			}
			pLine->push_back( ee);
			lw += nWidth;
			pLine->SetHeight( nHeight);

			if( !bSingle && DTPT_RETURN == eef->GetType())
			{
				lw = 0;
				pLine = NewLine( nLineDefaultHeight);
			}
		} // for( CEditElementVector::iterator it = temp.begin(); it != temp.end(); ++ it)

		ResetCursor( nCursor);
	}
	void AppendToEnd( const CDecoderTextPieceList& tpl, int backgroundEffectId = 0)
	{
		CLineObject*  pLine = NULL;
		if( m_ees.empty())
			pLine = NewLine();
		else
			pLine = &m_ees.back();

		int nCount = tpl.GetCount();
		for( int i = 0; i < nCount; i ++)
		{
			const CDecoderTextPiece& dtp = tpl.GetAt( i);
			InsertEditElement( dtp, *pLine, 0, backgroundEffectId);
		}
	}
	void AppendAtCursor( const CDecoderTextPieceList& tpl, int nLimit, int backgroundEffectId = 0)
	{
		if( !IsCanInsert())
			return;
		CLineObject*  pLine = NULL;

		SeparatorFromCursor();
		if( m_ees.empty())
			pLine = NewLine();
		else
			pLine = &m_ees.front();
		
		int nLeft = 0;
		for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
		{
			CLineObject& lo = *it;
			nLeft += (int)lo.size();
		}
		nLeft = nLimit - nLeft;
		if( nLeft <= 0)
			return;
		int nCount = tpl.GetCount();
		nCount = min( nCount, nLeft);
		for( int i = 0; i < nCount && nLimit > 0; i ++)
		{
			const CDecoderTextPiece& dtp = tpl.GetAt( i);
			InsertEditElement( dtp, *pLine, nLimit, backgroundEffectId);
		}
		m_nCursorX = (int)pLine->size();
		m_nCursorY = 0;
	}
	int GetCharCount() const
	{
		int count = 0;
		for ( CContainerObject::const_iterator rt = m_ees.begin(); rt != m_ees.end(); ++ rt)
		{
			count += rt->size();
		}
		return count;
	}
	void CancelAllSelect()
	{
		for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
		{
			CLineObject& lo = *it;
			for( CLineObject::iterator itl = lo.begin(); itl != lo.end(); ++ itl)
			{
				CEditElementObject& ee = *itl;
				ee.m_bSelected = false;
			} // for( CLineObject::iterator itl = lo.begin(); itl != lo.end(); ++ itl)
		} // for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
	}
	void CheckCursorPosition( int& x, int& y, int ls, int nDefaultFont, bool bPassword)
	{
		if( m_ees.empty())
		{
			assert( 0 == m_nCursorX && 0 == m_nCursorY);
			x = 0;
			y = 0;
			return;
		}

		int ny = 0;
		m_nCursorY = 0;
		for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it, m_nCursorY ++)
		{
			CLineObject&  lo = (*it);
			int nb = ny + lo.m_nHeight + ls;
			if( y >= ny && y < nb)
			{
				y = ny;
				CheckCursorPositionOneLine( lo, x, nDefaultFont, bPassword);
				return;
			}
			ny = nb;
		} // for( CContainerObject::iterator it = m_ees->begin(); it != m_ees->end(); ++ it)

		//y = (int)m_ees.size() - 1;
		//x = (int)m_ees.back().size();
		CLineObject&  lo = m_ees.back();
		y = ny - lo.m_nHeight - ls;
		x = lo.size() * CTextDrawManager::GetInstance().GetFontHeight( nDefaultFont);
		CheckCursorPositionOneLine( lo, x, nDefaultFont, bPassword);
	}
	// 根据字符位置定位, 可以直接定位到第row行第col个字符后
	void CheckCursorCharPosition( int row, int col, int& x, int& y, int ls, int nDefaultFont, bool bPassword)
	{
		if( m_ees.empty())
		{
			assert( 0 == m_nCursorX && 0 == m_nCursorY);
			x = 0;
			y = 0;
			return;
		}

		int ny = 0;
		m_nCursorY = 0;
		for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it, m_nCursorY ++)
		{
			CLineObject&  lo = (*it);
			int nb = ny + lo.m_nHeight + ls;
			if( m_nCursorY >= row && ny <= nb)
			{
				y = ny;
				CheckCursorCharPositionOneLine( lo, col, x, nDefaultFont, bPassword);
				return;
			}
			ny = nb;
		} // for( CContainerObject::iterator it = m_ees->begin(); it != m_ees->end(); ++ it)

		CLineObject&  lo = m_ees.back();
		y = ny - lo.m_nHeight - ls;
		x = lo.size() * CTextDrawManager::GetInstance().GetFontHeight( nDefaultFont);
		CheckCursorPositionOneLine( lo, x, nDefaultFont, bPassword);
	}
	// 获取当前光标的字符位置
	void GetCursorCharPosition( int& row, int& col) const
	{
		row = m_nCursorY;
		col = m_nCursorX;
	}
	void SetSelect( int xs, int ys, int xe, int ye, int ls, int nDefaultFont, bool bPassword)
	{
		if( m_ees.empty())
			return;

		CancelAllSelect();

		int ny = 0;
		CContainerObject::iterator itStart = m_ees.end();
		CContainerObject::iterator itEnd = m_ees.end();

		for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
		{
			CLineObject&  lo = (*it);
			int nb = ny + lo.m_nHeight + ls;
			if( ys >= ny && ys < nb)
				itStart = it;
			if( ye >= ny && ye < nb)
			{
				itEnd = it;
				break;
			}
			ny = nb;
		}
		if( itStart == m_ees.end())
			return;

		if( itStart == itEnd)
		{
			if( xs > xe)
				std::swap( xs, xe);
			SetOneLineSelectFromTo( xs, xe, *itStart, nDefaultFont, bPassword);
			return;
		}

		SetOneLineSelectFrom( xs, *itStart, nDefaultFont, bPassword);
		for( ++ itStart; itStart != itEnd; ++ itStart)
			SetOneLineSelectAll( *itStart);
		if( itEnd != m_ees.end())
			SetOneLineSelectTo( xe, *itEnd, nDefaultFont, bPassword);
	}
	bool IsHasSelect()
	{
		for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
		{
			CLineObject& lo = *it;
			for( CLineObject::iterator itl = lo.begin(); itl != lo.end(); ++ itl)
			{
				CEditElementObject& ee = *itl;
				if( ee.m_bSelected)
					return true;
			} // for( CLineObject::iterator itl = lo.begin(); itl != lo.end(); ++ itl)
		} // for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
		return false;
	}
	void RemoveSelect()
	{	
		CEditElementVector	temp;

		{
			for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
			{
				CEditElementVector& eev = *it;
				temp.insert( temp.end(), eev.begin(), eev.end());
			} // for( iterator it = begin(); it != end(); ++ it)
			m_ees.clear();

			for( CEditElementVector::iterator it = temp.begin(); it != temp.end(); )
			{
				CEditElementObject& ee = *it;
				if( !ee.m_bSelected)
				{
					++ it;
					continue;
				}
				//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
				CEditElementFormat* eef = _GetEditElementFormat( ee.m_nIndex);
				assert( eef);
				if( DTPT_COMPLEX != eef->GetType())
				{
					++ it;
					continue;
				}
				CEditElementVector::iterator itBack = it;
				while( itBack != temp.begin())
				{
					-- itBack;
					CEditElementObject& eepre = *itBack;
					if( eepre.m_nIndex != ee.m_nIndex)
						break;
					eepre.m_bSelected = true;
				}
				while( true) 
				{
					++ it;
					if( it == temp.end())
						break;
					CEditElementObject& eenext = *it;
					if( eenext.m_nIndex != ee.m_nIndex)
						break;
					eenext.m_bSelected = true;
				}
			}
		}

		CLineObject* lo = NewLine();
		m_nCursorY = 0;
		int nCount = 0;
		m_nCursorX = 0;
		
		CLineObject* plo = NewLine(); 

		for( CEditElementVector::iterator it = temp.begin(); it != temp.end(); ++ it)
		{
			CEditElementObject& ee = *it;
			if( ee.m_bSelected)
			{
				m_nCursorX = nCount;
//				m_eefm.ReleaeEditElementFormat( ee.m_nIndex);
			}
			else
			{
				nCount ++;
				plo->push_back( ee);
			}
		} // for( CEditElementVector::iterator it = temp.begin(); it != temp.end(); )
	}
	bool EraseAll()
	{
		bool b = !m_ees.empty();
		m_ees.clear();
		m_eefm.Clear();
		m_ww.Clear();
		InitDefaultEEFM();
		return b;
	}
	bool IsCanInsert()
	{
		int x = m_nCursorX;
		int y = m_nCursorY;

		if( 0 == x)
			return true;
		if( y >= (int)m_ees.size())
			return true;
		CContainerObject::iterator it = m_ees.begin();
		while( y)
		{
			y --;
			++ it;
		}
		assert( it != m_ees.end());
		CLineObject& lo = *it;
		if( x >= (int)lo.size())
			return true;
		CLineObject::iterator itl = lo.begin();
		while( x) 
		{
			x --;
			++ itl;
		}
		assert( itl != lo.begin());
		assert( itl != lo.end());
		CEditElementObject& ee = *itl;
		//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
		CEditElementFormat* eef = _GetEditElementFormat( ee.m_nIndex);
		assert( eef);
		if( DTPT_COMPLEX != eef->GetType())
			return true;
		-- itl;
		CEditElementObject& eepre = *itl;
		return ee.m_nIndex != eepre.m_nIndex;
	}
	void GetSelectString( std::wstring& str)
	{
		for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
		{
			CLineObject& lo = *it;
			for( CLineObject::iterator itl = lo.begin(); itl != lo.end(); ++ itl)
			{
				CEditElementObject& ee = *itl;
				if( !ee.m_bSelected)
					continue;
				//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
				CEditElementFormat* eef = _GetEditElementFormat( ee.m_nIndex);
				assert( eef);

				switch( eef->GetType())
				{
				case DTPT_DEFAULT_TEXT:
				case DTPT_FORMAT_TEXT:
				case DTPT_COMPLEX:
					str.append( 1, ee.m_char);
					break;
				default:
					break;
				}
			} // for( CLineObject::iterator itl = lo.begin(); itl != lo.end(); ++ itl)
		} // for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
	}
	void SetCursorHome()
	{
		m_nCursorX = 0;
	}
	void SetCursorEnd()
	{
		int y = m_nCursorY;

		if( y >= (int)m_ees.size())
			return;
		CContainerObject::iterator it = m_ees.begin();
		while( y)
		{
			y --;
			++ it;
		}
		assert( it != m_ees.end());
		CLineObject& lo = *it;

		CEditElementObject& ee = lo.back();
		//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
		CEditElementFormat* eef = _GetEditElementFormat( ee.m_nIndex);
		assert( eef);

		if( DTPT_RETURN == eef->GetType())
			m_nCursorX = (int)lo.size() - 1;
		else
			m_nCursorX = (int)lo.size();
	}
	void SetCursorLast()
	{
		ResetCursor( max( 0, GetCursorPos() - 1));
	}
	void SetCursorNext()
	{
		ResetCursor( GetCursorPos() + 1);
	}
	void GetCursorPosition(int& x, int& y, int ls, int nDefaultFont, bool bPassword) const
	{
		if( m_ees.empty())
		{
			assert( 0 == m_nCursorX && 0 == m_nCursorY);
			x = 0;
			y = 0;
			return;
		}

		y = 0;
		int cy = m_nCursorY;
		CContainerObject::const_iterator it = m_ees.begin();
		while( cy)
		{
			const CLineObject&  lo = (*it);
			y += ( lo.m_nHeight + ls );
			cy --;
			++ it;
		}
		if( it == m_ees.end())
			x = 0;
		else
			GetCursorPositionOneLine( *it, x, nDefaultFont, bPassword);
	}
	void SetCursorBackspace()
	{
		SeparatorFromCursor();
		if( m_ees.empty())
			return;
		CLineObject& lo = m_ees.front();
		if( lo.empty())
			return;
		int nIndex = -1; 
		CEditElementObject& ee = lo.back();
		//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
		CEditElementFormat* eef = _GetEditElementFormat( ee.m_nIndex);
		assert( eef);
		nIndex = ee.m_nIndex;

		lo.erase( --lo.end());
		if( DTPT_COMPLEX == eef->GetType())
			RemoveComplexComponent( nIndex);
		m_nCursorY = 0;
		m_nCursorX = (int)lo.size();
	}
	void SetCursorDelete()
	{
		SeparatorFromCursor();
		if( m_ees.empty())
			return;
		{
			CLineObject& lo = m_ees.back();
			if( lo.empty())
				return;

			int nIndex = -1; 
			CEditElementObject& ee = lo.front();
			//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
			CEditElementFormat* eef = _GetEditElementFormat( ee.m_nIndex);
			assert( eef);
			nIndex = ee.m_nIndex;
			lo.erase( lo.begin());
			if( DTPT_COMPLEX == eef->GetType())
				RemoveComplexComponent( nIndex);
		}
		{
			CLineObject& lo = m_ees.front();
			m_nCursorY = 0;
			m_nCursorX = (int)lo.size();
		}
	}
	void EncodeTextPieceList( CDecoderTextPieceList& tpl)
	{
		std::wstring		strTemp;
		int					nIndex = -1;

		for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
		{
			CLineObject& lo = *it;
			for( CLineObject::iterator itl = lo.begin(); itl != lo.end(); ++ itl)
			{
				CEditElementObject& ee = *itl;
				//CEditElementFormat* eef = m_eefm.GetEditElementFormat( ee.m_nIndex);
				CEditElementFormat* eef = _GetEditElementFormat( ee.m_nIndex);
				assert( eef);
	
				DTP_TYPE	dtpt = eef->GetType();
				switch( dtpt)
				{
				case DTPT_DEFAULT_TEXT:
				case DTPT_FORMAT_TEXT:
				case DTPT_COMPLEX:
					if( nIndex == ee.m_nIndex)
					{
						strTemp.append( 1, ee.m_char);
					}
					else
					{
						CheckCreateTextPiece( nIndex, strTemp, tpl);
						nIndex = ee.m_nIndex;
						strTemp.assign( 1, ee.m_char);
					}
					break;
				case DTPT_STATIC_IMAGE:
				case DTPT_ANIMATE_IMAGE:
				case DTPT_EMOTION_IMAGE:
				case DTPT_CUSTOM_PIECE:
					{
						if ( !strTemp.empty())
						{
							CheckCreateTextPiece( nIndex, strTemp, tpl);
							strTemp.clear();
						}
						tpl.Insert( *eef);
					}
					break;
				case DTPT_RETURN:
					{
						if ( !strTemp.empty())
						{
							CheckCreateTextPiece( nIndex, strTemp, tpl);
							strTemp.clear();
						}
						tpl.Insert( CDecoderTextPiece());
					}
					break;
				case DTPT_BUTTON:
					{
						if ( !strTemp.empty())
						{
							CheckCreateTextPiece( nIndex, strTemp, tpl);
							strTemp.clear();
						}
						tpl.Insert( CDecoderTextPiece( eef->GetText(), eef->GetTextFont(), eef->GetTextFormatID(), eef->GetTextColor(), eef->GetTextGradualColor(), eef->GetButtonMouseOnColor(), eef->GetButtonMouseOnGradualColor(), eef->GetImage(),
							eef->GetButtonMouseOnImage(), eef->GetButtonDownImage(), eef->GetButtonNotify(), eef->GetButtonImageWidth(), eef->GetButtonImageHeight(), eef->GetTipFormatText()));
					}
					break;
				default:
					assert( false);
					break;
				}
			} // for( CLineObject::iterator itl = lo.begin(); itl != lo.end(); ++ itl)
		} // for( CContainerObject::iterator it = m_ees.begin(); it != m_ees.end(); ++ it)
		CheckCreateTextPiece( nIndex, strTemp, tpl);
	}
};

} // namespace INLINECLASS {
} // namespace GXWINDOW { 

