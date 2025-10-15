#pragma once

namespace GXWINDOW { 

namespace INLINECLASS { 

typedef std::vector<CComponent*>  CComponentList;
class CContainer : public CComponentList
{
public:
	CContainer()
	{
	}
	virtual ~CContainer()
	{
	}
public:
	virtual bool SetForeground( CComponent* pc)
	{
		if( empty())
			return false;
		if( back()->IsSameComponent( *pc))
			return true;
		if( Remove( pc))
		{
			push_back( pc);
			return true;
		}
		return false;
	}
	virtual bool SetBackground( CComponent* pc)
	{
		if( empty())
			return false;
		if( front()->IsSameComponent( *pc))
			return true;
		if( Remove( pc))
		{
			insert( begin(), pc);
			return true;
		}
		return false;
	}
	virtual void Insert( CComponent* pc)
	{
		iterator itfind = std::find( begin(), end(), pc);
		if( itfind == end())
			push_back( pc);
	}

	void OnMouseMoveLeave()
	{
		CComponentList curlist = *this;
		for( CComponentList::iterator it = curlist.begin(), ite = curlist.end(); it != ite; ++ it)
		{
			CComponent& com = **it;
			com.OnMouseLeave();
		}
	}
	static void OnComponentMouseMoveEnter( CComponent& com)
	{
		com.OnMouseEnter();
	}
	bool IsComponentExist( CComponent* pc)
	{
		for( iterator it = begin(), ite = end(); it != ite;  ++ it)
		{
			if( pc->IsSameComponent( **it))
				return true;
		}
		return false;
	}
	bool Remove( CComponent* pc)
	{
		for( iterator it = begin(); it != end(); ++ it)
		{
			if( pc->IsSameComponent( **it))
			{
				erase( it);
				return true;
			}
		}
		return false;
	}
	
	void Draw( CDraw& draw, COLOR clrBorder = 0)
	{
		for( iterator it = begin(); it != end(); ++ it)
		{
			CComponent& com = **it;
			com.Draw( draw);
			com.DoComponentAfterDrawNofity( draw);
			if ( GetColorAlpha( clrBorder))
				draw.Rect( com.GetAbsPosX(), com.GetAbsPosY(), com.GetAbsPosX() + com.GetWidth(), com.GetAbsPosY() + com.GetHeight(), clrBorder);
		}
	}
	void Draw( CDraw& draw, int cx, int cy, int cw, int ch, COLOR clrBorder = 0)
	{
		int cr = cx + cw;
		int cb = cy + ch;
		for( iterator it = begin(); it != end(); ++ it)
		{
			CComponent& com = **it;
			ClipComponentDraw( draw, cx, cy, cr, cb, com, clrBorder);
		}
	}

	CComponent* ComponentFromPoint( int x, int y) // x y base parent component
	{
		for( reverse_iterator it = rbegin(); it != rend(); ++ it)
		{
			CComponent& com = **it;
			if( !com.IsViewOnly() && com.IsPointIn( x, y))
				return &com;
		}
		return NULL;
	}
	void ComponentFromPoint( int x, int y, CComponentList& list) // x y base parent component
	{
		for( reverse_iterator it = rbegin(); it != rend(); ++ it)
		{
			CComponent& com = **it;
			if( !com.IsViewOnly() && com.IsPointIn( x, y))
				list.push_back( &com);
		}
	}

	template<typename F>
	bool DispatchMouseMessage( F f, CComponent* p, int x, int y, int key)
	{
		int nx = x - p->GetAbsPosX();
		int ny = y - p->GetAbsPosY();

		CComponentList		clist;

		ComponentFromPoint( nx, ny, clist);
		for( CComponentList::iterator it = clist.begin(); it != clist.end(); ++ it)
		{
			CComponent& com = **it;
			if( com.IsViewOnly())
				continue;
			if( (com.*f)( x, y, key))
				return true;
		}
		return false;
	}
private:
	void ClipComponentDraw( CDraw& draw, int l, int t, int r, int b, CComponent& com, COLOR clrBorder = 0)
	{
		int nl = com.GetPosX();
		int nt = com.GetPosY();
		int nr = com.GetPosR();
		int nb = com.GetPosB();

		int cx = 0;
		int cy = 0;
		int cw = 0;
		int ch = 0;

		if( nl < l) // left
		{
			if(  nt < t) // top left
			{
				if( nr < l || nb < t)
					return;

				//cx -= nl;
				//cy -= nt;
				cx = l - nl;
				cy = t - nt;
				if( nr < r)
					cw = nr - l;
				else // nr >= r
					cw = r - l;
				if( nb < b)
					ch = nb - t;
				else // nb >= b
					ch = b - t;
			} //if(  nt < t) // top left
			else if( nt < b) // left midlle
			{
				if( nr < l)
					return;
				cx = l - nl;
				cy = 0;
				if( nr < r)
					cw = nr - l;
				else // nr >= r
					cw = r - l;
				if( nb < b)
					ch = nb - nt;
				else // nb >= b
					ch = b - nt;
			} // else if( nt < r)
			else // nt >= b
			{
				return;
			}
		}
		else if( nl < r) // middle
		{
			if( nt < t) //// top middle
			{
				if( nb < t)
					return;
				cx = 0;
				cy = t - nt;
				if( nr < r)
					cw = nr - nl;
				else
					cw = r - nl;
				if( nb < b)
					ch = nb - t;
				else
					ch = b - t;
			}
			else if( nt < b)
			{
				cx = 0;
				cy = 0;
				if( nr < r)
					cw = nr - nl;
				else
					cw = r - nl;
				if( nb < b)
					ch = nb - nt;
				else
					ch = b - nt;
			}
			else // nt >= b
			{
				return;
			}
		}
		else// nl >= r, right
		{
			return;
		}

		nl = com.GetAbsPosX();
		nt = com.GetAbsPosY();

		com.Draw( draw, nl, nt, cx, cy, cw, ch);
		com.DoComponentAfterDrawNofity( draw, nl, nt, cx, cy, cw, ch);
		if ( GetColorAlpha( clrBorder))
			draw.Rect( nl + cx, nt + cy, nl + cx + cw, nt + cy + ch, clrBorder);
	}

};


class CDesktopContainer : public CContainer
{
	void InsertCareViewLevel( CComponent* pc)
	{
		CFrame* frame = dynamic_cast<CFrame*>( pc);
		assert( frame);
		if( NULL == frame)
			return;

		const int nCurrentViewLevel = frame->GetViewLevel();

		for( iterator it = begin(), ite = end(); it != ite; ++ it)
		{
			CFrame* f = dynamic_cast<CFrame*>( *it);
			assert( f);
			if( NULL == f)
				continue;
			if( nCurrentViewLevel < f->GetViewLevel())
			{
				insert( it, pc);
				return;
			}
		}
		push_back( pc);
	}
public:
	CDesktopContainer() {}
	virtual ~CDesktopContainer() {}
public:
	virtual bool SetForeground( CComponent* pc)
	{
		if( empty())
			return false;
		if( Remove( pc))
		{
			InsertCareViewLevel( pc);
			return true;
		}
		return false;
	}
	virtual bool SetBackground( CComponent* pc)
	{
		if( empty())
			return false;

		CFrame* frame = dynamic_cast<CFrame*>( pc);
		const int nCurrentViewLevel = frame->GetViewLevel();

		iterator itbegin = begin();
		iterator itend = end();
		for( ; itbegin != itend; ++ itbegin)
		{
			CFrame* f = dynamic_cast<CFrame*>( *itbegin);
			if( nCurrentViewLevel == f->GetViewLevel())
			{
				if( f->IsSameComponent( frame))
					return true;
				else
					break;
			}
		} // for( iterator it = begin(), ite = end(); it != ite; ++ it)

		for( iterator it = itbegin; it != itend; ++ it)
		{
			CFrame* f = dynamic_cast<CFrame*>( *it);
			if( nCurrentViewLevel != f->GetViewLevel())
				return false;
			if( f->IsSameComponent( frame))
			{
				erase( it);
				insert( itbegin, pc);
				return true;
			}
		}
		return false;
	}
	virtual void Insert( CComponent* pc)
	{
		iterator itfind = std::find( begin(), end(), pc);
		if( itfind == end())
			InsertCareViewLevel( pc);
	}
};

} // namespace INLINECLASS {

} // namespace GXWINDOW { 