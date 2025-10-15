#ifndef __Nuclear_astarImplement_H__
#define __Nuclear_astarImplement_H__

#include <stdlib.h>
#include <string.h>

#pragma push_macro("new")
#undef new // for some DEBUG mode

inline static unsigned int FastIntSqrt(unsigned int x)
{
	static const unsigned char T[] = {
		0,  16,  23,	28,  32,  36,  39,	43,  46,  48,  51,	53,  56,  58,  60,	62,
		64,  66,  68,	70,  72,  74,  75,	77,  79,  80,  82,	83,  85,  86,  88,	89,
		91,  92,  94,	95,  96,  98,  99, 100, 101, 103, 104, 105, 106, 108, 109, 110,
		111, 112, 113, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127,
		128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143,
		143, 144, 145, 146, 147, 148, 149, 150, 150, 151, 152, 153, 154, 155, 155, 156,
		157, 158, 159, 159, 160, 161, 162, 163, 163, 164, 165, 166, 167, 167, 168, 169,
		170, 170, 171, 172, 173, 173, 174, 175, 176, 176, 177, 178, 178, 179, 180, 181,
		181, 182, 183, 183, 184, 185, 186, 186, 187, 188, 188, 189, 190, 190, 191, 192,
		192, 193, 194, 194, 195, 196, 196, 197, 198, 198, 199, 199, 200, 201, 201, 202,
		203, 203, 204, 205, 205, 206, 206, 207, 208, 208, 209, 210, 210, 211, 211, 212,
		213, 213, 214, 214, 215, 216, 216, 217, 217, 218, 219, 219, 220, 220, 221, 221,
		222, 223, 223, 224, 224, 225, 225, 226, 227, 227, 228, 228, 229, 229, 230, 230,
		231, 232, 232, 233, 233, 234, 234, 235, 235, 236, 237, 237, 238, 238, 239, 239,
		240, 240, 241, 241, 242, 242, 243, 243, 244, 245, 245, 246, 246, 247, 247, 248,
		248, 249, 249, 250, 250, 251, 251, 252, 252, 253, 253, 254, 254, 255, 255, 255};
		unsigned int r;
		if		(x < 0x10000)
			if		(x <  0x400- 3) r = T[(x+ 3)>>2] >> 3;
			else if(x < 0x4000-28) r = T[(x+28)>>6] >> 1;
			else				    r = T[ x    >>8];
		else
			if	 (x <  0x1000000)
				if (x <   0x100000) { r = T[x>>12]; r = ((x/r)>>3) + (r<<1); }
				else				  { r = T[x>>16]; r = ((x/r)>>5) + (r<<3); }
			else
				if (x < 0x10000000)
					if(x <  0x4000000) { r = T[x>>18]; r = ((x/r)>>6) + (r<<4); }
					else				  { r = T[x>>20]; r = ((x/r)>>7) + (r<<5); }
				else
					if(x < 0x40000000) { r = T[x>>22]; r = ((x/r)>>8) + (r<<6); }
					else			      { r = T[x>>24]; r = ((x/r)>>9) + (r<<7); }
					return r - (r*r > x);
}
inline static int FastIntSqrt(int x) { return static_cast<int>(FastIntSqrt(static_cast<unsigned int>(x))); }

namespace astar
{

	struct ANode
	{
		// ANode����Ϣ���������Ը��������ļ���õ���Ϊ���ٶȣ��������෽ʽ��
		short x;   // ��ǰ�ڵ����ꡣ
		short y;

		ANode* parent; // ��СȨֵ·���ĸ��ڵ㡣�ڲ��ҹ����п��ܻᷢ���ı䡣
		int gcost;     // ��ʼ�㵽��ǰ�ڵ��·��Ȩֵ��
		int fcost;     // �㷨Ȩֵ��_fcost = gcost + hcost��
		int whichList; // ��¼�ڵ�״̬��δ�á��򿪡��رա�����0��ʾδ�ã��򿪹رյı����������䡣
		int openpos;   // ��open���е�λ�á�

	public:
		inline ANode()      { reset(); }
		inline void reset() { whichList = 0; } // �����������������������á�����x,y��AScanNodesֻ��ʼ��һ�Ρ�

		inline void open(ANode * parent, int cost, ANode * target)
		{
			this->parent = parent;
			this->gcost  = parent->gcost + cost;
			this->fcost = this->gcost + 5 * (FastIntSqrt((target->x - this->x)*(target->x - this->x) + (target->y - this->y)*(target->y - this->y)));
		}

		inline bool adjust(ANode * parent, int cost)
		{
			int newgcost = parent->gcost + cost;
			if (newgcost < this->gcost)
			{
				this->fcost = newgcost + this->fcost - this->gcost;
				this->gcost = newgcost;
				this->parent = parent;
				return true;
			}
			return false;
		}
	};

	class AScanNodes
	{
	public:
		enum { eMaxWidth  = 1024 * 5 / 16, eMaxHeight = 768 * 5 / 16 };
		inline bool isOutofRange(const AMap & amap) { return amap.width() > eMaxWidth || amap.height() > eMaxHeight; }
		inline AScanNodes()
		{
			onOpenList  = 1; onClosedList = 2;
			for (short x = 0; x < eMaxWidth; ++x)
				for (short y = 0; y < eMaxHeight; ++y)
				{
					nodes[x][y].x = x;
					nodes[x][y].y = y;
				}
		}

		inline ANode * getNode(int x, int y) { return &nodes[x][y]; }

		inline void initialize()
		{
			if (onClosedList == 0) // ��־�Ѿ�����������Χ��ѭ�����������³�ʼ��һ�νڵ�ı�־λ��
			{
				for (int x = 0; x < eMaxWidth; ++x)
					for (int y = 0; y < eMaxHeight; ++y)
						nodes[x][y].reset();
				onOpenList   = 1;
				onClosedList = 2;
			}
			else
			{
				onOpenList   += 2;
				onClosedList += 2;
			}
		}

		inline void open(ANode * node, ANode * parent, int cost, ANode * target)
		{
			node->open(parent, cost, target);
			node->whichList = onOpenList;
		}
		inline void close(ANode * node)    const { node->whichList = onClosedList; }
		inline bool isClosed(ANode * node) const { return onClosedList == node->whichList; }
		inline bool isOpen(ANode * node)   const { return onOpenList == node->whichList; }

	private:
		int onClosedList;  // ��¼���ڵ��Ƿ��ǹرյı�־�����������ʼ���ڵ㡣
		int onOpenList;
		ANode nodes[eMaxWidth][eMaxHeight];
	};

	class AOpenList
	{
		ANode * openList[AScanNodes::eMaxWidth * AScanNodes::eMaxHeight]; // ���б�����С�Ѵ洢�� ��С fcost �ڶ���
		int size;

		inline void setPos(ANode* node, int i)
		{
			node->openpos = i;
			openList[i] = node;
		}
	public:
		inline void initialize() { size = 0; }
		inline void push(ANode * node)
		{
			node->openpos = size++;
			adjust(node);
		}
		inline bool empty() const      { return size == 0; }
		inline ANode * pop()
		{
			ANode* const t = openList[0];
			ANode* const e = openList[--size];
			const int fcost = e->fcost;
			int i = 0, j = 2;
			for(; j <= size; j = (i + 1) * 2)
			{
				ANode* const p = openList[j - 1];
				if(p->fcost < fcost)
				{
					if(j == size)           { setPos(p, i); setPos(e, j - 1); return t; }
					ANode* const q = openList[j];
					if(p->fcost < q->fcost) { setPos(p, i); i = j - 1; }
					else                    { setPos(q, i); i = j; }
				}
				else
				{
					if(j == size) break;
					ANode* const q = openList[j];
					if(q->fcost < fcost)    { setPos(q, i); i = j; }
					else break;
				}
			}
			setPos(e, i);
			return t;
		}
		inline void adjust(ANode * node)
		{
			int i, j;
			for(i = node->openpos; i > 0; i = j)
			{
				j = (i - 1) / 2;
				ANode* const p = openList[j];
				if(p->fcost <= node->fcost)
					break;
				setPos(p, i);
			}
			setPos(node, i);
		}
	};

	#define ACORNER(cx, cy, cost, cx0, cy0, cx1, cy1)            \
	{                                                            \
		int x = parent->x + cx;                                  \
		int y = parent->y + cy;                                  \
		if (amap.walkable(x, y)                                  \
			&& amap.walkable(parent->x + cx0, parent->y + cy0)   \
			&& amap.walkable(parent->x + cx1, parent->y + cy1))  \
		{                                                        \
			ANode * child = scanNodes.getNode(x, y);             \
			if (!scanNodes.isClosed(child))                      \
			{                                                    \
				if (!scanNodes.isOpen(child))                    \
				{                                                \
					scanNodes.open(child, parent, cost, target); \
					openList.push(child);                        \
				}                                                \
				else                                             \
				{                                                \
					if (child->adjust(parent, cost))             \
						openList.adjust(child);                  \
				}                                                \
			}                                                    \
		}                                                        \
	}

	#define ACROSS(cx, cy, cost)                                 \
	{                                                            \
		int x = parent->x + cx;                                  \
		int y = parent->y + cy;                                  \
		if (amap.walkable(x, y))                                 \
		{                                                        \
			ANode * child = scanNodes.getNode(x, y);             \
			if (!scanNodes.isClosed(child))                      \
			{                                                    \
				if (!scanNodes.isOpen(child))                    \
				{                                                \
					scanNodes.open(child, parent, cost, target); \
					openList.push(child);                        \
				}                                                \
				else                                             \
				{                                                \
					if (child->adjust(parent, cost))             \
						openList.adjust(child);                  \
				}                                                \
			}                                                    \
		}                                                        \
	}

	template < typename ScanNodes, typename OpenList >
	class AStar
	{
		ScanNodes scanNodes;
		OpenList  openList;

	public:
		// path: ���ز���·����˳��Ϊ (��ʼ��, Ŀ���]��[ע��] ��������ʼ�㡣
		inline bool find(const AMap & amap, int startX, int startY, int targetX, int targetY, APath & path /* out */)
		{
			if (scanNodes.isOutofRange(amap)) return false; // ��ͼ��С��������
			if (!amap.walkable(targetX, targetY)) return false; // Ŀ��㲻�ɴ�,��ʼ���������ϰ�
			if (startX == targetX && startY == targetY) return false; // ��ʼ��==Ŀ��㣬������㷨���ܴ������������

			// Reset some variables that need to be cleared
			scanNodes.initialize();
			openList.initialize();

			// ׼����ʼ���Ŀ��㡣����ʼ����뵽���б�
			ANode * target = scanNodes.getNode(targetX, targetY);
			ANode * start = scanNodes.getNode(startX, startY);
			start->gcost = 0; // ������ʼ���Ȩֵ����Ϊ��ʼparent�Ĳ�����fcost û��ʹ�ã�����ʼ���ˡ�
			openList.push(start);

			// ��ʼ���ң�������б�Ϊ�ա�����ʧ�ܡ�
			while (!openList.empty())
			{
				ANode * parent = openList.pop();
				scanNodes.close(parent);

				// ugly �������10%���ܡ�ѭ����д��Ҳ�ȽϷ����ܶ��жϡ�������ע�����档
				ACORNER(-1, -1, 7, 0, -1, -1, 0);  ACROSS(0, -1, 5); ACORNER(+1, -1, 7, 0, -1, +1, 0);
				ACROSS (-1,  0, 5);                                  ACROSS (+1,  0, 5);
				ACORNER(-1, +1, 7, -1, 0,  0, +1); ACROSS(0, +1, 5); ACORNER(+1, +1, 7, +1, 0, 0, +1);

				// ��Ŀ�������б����ҳɹ���
				if (scanNodes.isOpen(target))
				{
					path.clear();
					for (ANode * p = target; p != start; p = p->parent)
						path.insert(path.begin(), Grid(p->x, p->y));
					return true;
				}
			}
			return false;
		}
	};

	template <typename AMap, int GridSize>
	class AFinder : public Finder<AMap, GridSize>
	{
	public:
		// path: ���ز���·����˳��Ϊ (��ʼ��, Ŀ���]��[ע��] ��������ʼ�㡣
		bool find(int startX, int startY, int targetX, int targetY, APath & path /* out */)
		{
			return star->find(this->amap, startX, startY, targetX, targetY, path);
		}

		AFinder()  { star = new AStar<AScanNodes, AOpenList>; }
		~AFinder() { delete star; }
	private:
		AStar<AScanNodes, AOpenList> * star;  // �㷨ʵ�֣���������Ԥ�����ڴ档���ڶ����棬AScanNodes���ܴܺ�
	};

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// �̶������ڴ��. ����������,ֻ��������ʱȫ���ͷ�.
	// ʹ��ʱֻҪ�������༴��,new/deleteʱ���Զ��ڳ��з���/�ͷ�,�ݲ�֧��new[]/delete[]
	template<class T, int BLOCK_NODE_SIZE = 1000>
	class CMemPoolNode
	{
		class CMemPool
		{
			struct SNode
			{
				char    m_data[sizeof(T)];
				SNode*  m_next;
			};
			struct SBlock
			{
				SNode           m_nodes[BLOCK_NODE_SIZE];
				SBlock* const   m_next;
				SBlock(SBlock* next) : m_next(next)
				{
					for(int i = 0; i < BLOCK_NODE_SIZE-1; ++i)
						m_nodes[i].m_next = m_nodes+i+1;
					m_nodes[BLOCK_NODE_SIZE-1].m_next = 0;
				}
			private:
				SBlock& operator=(const SBlock&);
			};
			SBlock* m_block_head;
			SNode*  m_node_head;
		public:
			CMemPool() : m_block_head(0), m_node_head(0) {}
			~CMemPool() { FreeAll(); }
			void* Alloc()
			{
				if(!m_node_head)
				{
					m_block_head = new SBlock(m_block_head);
					m_node_head = m_block_head->m_nodes;
				}
				SNode* const node = m_node_head;
				m_node_head = node->m_next;
				return node;
			}
			void Free(void* data)
			{
				SNode* const node = static_cast<SNode*>(data);
				node->m_next = m_node_head;
				m_node_head = node;
			}
			void FreeAll()
			{
				while(m_block_head)
				{
					SBlock* const next = m_block_head->m_next;
					delete m_block_head;
					m_block_head = next;
				}
				m_node_head = 0;
			}
		};
		static CMemPool ms_mempool;
	public:
		void* operator new(size_t) { return ms_mempool.Alloc(); }
		void operator delete(void* ptr) { ms_mempool.Free(ptr); }
		static void FreeAll() { ms_mempool.FreeAll(); }
	};
	template<class T, int BLOCK_NODE_SIZE>
	typename CMemPoolNode<T, BLOCK_NODE_SIZE>::CMemPool CMemPoolNode<T, BLOCK_NODE_SIZE>::ms_mempool;

	class BScanNodes
	{
		enum { eBlockSize = 16, eMaxSize = 1024 * 30 / 16 };
		struct BNode : public ANode, public CMemPoolNode<BNode>
		{
			BNode** next;
		};
		struct BBlock : public CMemPoolNode<BBlock>
		{
			BNode* nodes[eBlockSize][eBlockSize];
			BBlock** next;
		};
		BNode** node_head;
		BBlock** block_head;
		BBlock* blocks[eMaxSize/eBlockSize][eMaxSize/eBlockSize];

	public:
		inline bool isOutofRange(const AMap & amap) { return amap.width() > eMaxSize || amap.height() > eMaxSize; }
		inline BScanNodes() : node_head(0), block_head(0) { memset(blocks, 0, sizeof(blocks)); }
		inline ~BScanNodes() { initialize(); }

		inline ANode * getNode(int x, int y)
		{
			BBlock*& block = blocks[x/eBlockSize][y/eBlockSize];
			if(!block)
			{
				block = new BBlock;
				memset(block->nodes, 0, sizeof(block->nodes));
				block->next = block_head;
				block_head = &block;
			}
			BNode*& node = block->nodes[x%eBlockSize][y%eBlockSize];
			if(!node)
			{
				node = new BNode;
				node->x = static_cast<short>(x);
				node->y = static_cast<short>(y);
				node->next = node_head;
				node_head = &node;
			}
			return node;
		}

		inline void initialize()
		{
			while(node_head)
			{
				BNode** const next = (*node_head)->next;
				delete *node_head;
				node_head = next;
			}
			while(block_head)
			{
				BBlock** const next = (*block_head)->next;
				delete *block_head;
				*block_head = 0;
				block_head = next;
			}
		}

		inline void open(ANode * node, ANode * parent, int cost, ANode * target)
		{
			node->open(parent, cost, target);
			node->whichList = 1;
		}
		inline void close(ANode * node)    { node->whichList = 2; }
		inline bool isClosed(ANode * node) { return node->whichList == 2; }
		inline bool isOpen(ANode * node)   { return node->whichList == 1; }
	};

	class BOpenList
	{
		std::vector<ANode *> openList; // ���б�����С�Ѵ洢�� ��С fcost �ڶ���

		inline void setPos(ANode* node, int i)
		{
			node->openpos = i;
			openList[i] = node;
		}
	public:
		inline void initialize() { openList.clear(); openList.reserve(0x10000); }
		inline void push(ANode * node)
		{
			node->openpos = static_cast<int>(openList.size());
			openList.push_back(node);
			adjust(node);
		}
		inline bool empty() const      { return openList.empty(); }
		inline ANode * pop()
		{
			int size = static_cast<int>(openList.size());
			ANode* const t = openList[0];
			ANode* const e = openList[--size];
			openList.pop_back();
			if(size > 0)
			{
				const int fcost = e->fcost;
				int i = 0, j = 2;
				for(; j <= size; j = (i + 1) * 2)
				{
					ANode* const p = openList[j - 1];
					if(p->fcost < fcost)
					{
						if(j == size)           { setPos(p, i); setPos(e, j - 1); return t; }
						ANode* const q = openList[j];
						if(p->fcost < q->fcost) { setPos(p, i); i = j - 1; }
						else                    { setPos(q, i); i = j; }
					}
					else
					{
						if(j == size) break;
						ANode* const q = openList[j];
						if(q->fcost < fcost)    { setPos(q, i); i = j; }
						else break;
					}
				}
				setPos(e, i);
			}
			return t;
		}
		inline void adjust(ANode * node)
		{
			int i, j;
			for(i = node->openpos; i > 0; i = j)
			{
				j = (i - 1) / 2;
				ANode* const p = openList[j];
				if(p->fcost <= node->fcost)
					break;
				setPos(p, i);
			}
			setPos(node, i);
		}
	};

	template <typename AMap, int GridSize>
	class BFinder : public Finder<AMap, GridSize>
	{
	public:
		// path: ���ز���·����˳��Ϊ (��ʼ��, Ŀ���]��[ע��] ��������ʼ�㡣
		bool find(int startX, int startY, int targetX, int targetY, APath & path /* out */)
		{
			return star->find(this->amap, startX, startY, targetX, targetY, path);
		}

		BFinder()  { star = new AStar<BScanNodes, BOpenList>; }
		~BFinder() { delete star; }
	private:
		AStar<BScanNodes, BOpenList> * star;  // �㷨ʵ�֣���������Ԥ�����ڴ档���ڶ����棬BScanNodes���ܴܺ�
	};

	//////////////////////////////////////////////////////////////////////////////////////
	// ֱ��Ѱ·

	// �Ż���Bresenham�㷨,������Դ�˵�,ֻ֧��32767���ڵ�����,�ٴ��������Ը���64λint
	template <typename __Plot>
	inline eFindResult Bresenham(int srcx, int srcy, int dstx, int dsty, __Plot plot)
	{
		if (srcx == dstx && srcy == dsty)
			return astar::eFindInrange0;

		bool ylonger = false;
		int lenmin = dsty - srcy;
		int lenmax = dstx - srcx;
		if(abs(lenmin) > abs(lenmax))
		{
			std::swap(lenmin, lenmax);
			ylonger = true;
		}
		int delta = (lenmax==0 ? 0 : (lenmin<<16)/lenmax);
		delta += (int)(delta < 0);  // ���ӽ�ԭBresenham�㷨������

		if(ylonger)
		{
			if(lenmax > 0)
			{
				lenmax += srcy;
				for(int i = 0x7fff+(srcx<<16)+delta; ++srcy <= lenmax; i += delta)
					if(!plot(i >> 16, srcy)) return astar::eFindFail;
				return astar::eFindSuccess;
			}
			lenmax += srcy;
			for(int i = 0x8000+(srcx<<16)-delta; --srcy >= lenmax; i -= delta)
				if(!plot(i >> 16, srcy)) return astar::eFindFail;
			return astar::eFindSuccess;
		}

		if(lenmax > 0)
		{
			lenmax += srcx;
			for(int j = 0x7fff+(srcy<<16)+delta; ++srcx <= lenmax; j += delta)
				if(!plot(srcx, j >> 16)) return astar::eFindFail;
			return astar::eFindSuccess;
		}
		lenmax += srcx;
		for(int j = 0x8000+(srcy<<16)-delta; --srcx >= lenmax; j -= delta)
			if(!plot(srcx, j >> 16)) return astar::eFindFail;
		return astar::eFindSuccess;
	}

	template <typename AMap>
	struct Plot
	{
		const AMap & amap;
		explicit Plot(const AMap & _amap) : amap(_amap) { }
		bool operator()(int x, int y) const { return amap.walkable(x, y); }
	private:
		Plot operator=(const Plot&);    // suppress VC2005 warning: "assignment operator could not be generated"
	};

	template <typename AMap, int GridSize>
	eFindResult Finder<AMap, GridSize>::line(int x1, int y1, int x2, int y2) const
	{
		return Bresenham(x1, y1, x2, y2, Plot<AMap>(this->amap));
	}

	template <typename Finder, typename AMap>
	struct PlotPixel
	{
		const Finder * finder;
		const AMap & amap;
		explicit PlotPixel(const Finder * f, const AMap & m) : finder(f), amap(m) { }
		bool operator()(int x, int y) { Grid grid = finder->world2grid(x, y); return amap.walkable(grid.x, grid.y); }
	private:
		PlotPixel operator=(const PlotPixel&);  // suppress VC2005 warning: "assignment operator could not be generated"
	};

	template <typename AMap, int GridSize>
	eFindResult Finder<AMap, GridSize>::linePixel(int x1, int y1, int x2, int y2) const
	{
		return Bresenham(x1, y1, x2, y2, PlotPixel<mytype, AMap>(this, this->amap));
	}

	template <typename AMap, int GridSize>
	void Finder<AMap, GridSize>::smoothness(const Point & start, APath & src, int limit, Path & dst) const
	{
		if (limit <= 0 || limit > (int)src.size())
			limit = (int)src.size(); // no limit.

		Point from = start;
		astar::APath::iterator first = src.begin();
		Point to = this->grid2world(first->x, first->y);    // ��һĿ��㲻�����ԡ�
		for (++first, --limit; limit > 0; --limit, ++first) // �ӵڶ�Ŀ��㿪ʼ�����ڵĵ����ǲ����ԡ�
		{
			Point test = this->grid2world(first->x, first->y);
			if (from.finder_equals(to) // �����ظ���·���㡣��׳
				|| astar::eFindFail != this->linePixel(from, test))
			{
				to = test;
				continue;
			}
			to.direction = astar::Direction(from, to);
			dst.push_back(to);
			from = to;
			to = test;
		}

		if (false == from.finder_equals(to)) // �����ظ���·���㡣��׳
		{
			to.direction = astar::Direction(from, to);
			dst.push_back(to);
		}
		src.erase(src.begin(), first);
	}

	template <typename AMap, int GridSize>
	int Finder<AMap, GridSize>::scantrailGrid(const Point & location, const astar::Path & trail, float & length /*out*/) const
	{
		if (trail.size() < 2) return -1;
		if (false == location.trail_equals(trail.front())) return -2; // �ϸ�ƴ�ӣ�����Ҳ����һ��

		length = 0.0f;
		astar::Path::const_iterator first = trail.begin();
		Grid from = this->world2grid(*first);
		for (astar::Path::const_iterator second = ++trail.begin(), last = trail.end(); second != last; ++second)
		{
			Grid to = this->world2grid(*second);
			length  += first->distance(*second);
			first   = second;

			// ��ͬһ�������ڡ�line ��ҪĿ��Ϊ��Ѱ·������������������ʧ�ܣ�����˵����Ѱ·����֤ʱ������
			// �����Point�Ѿ�ת���ɸ������꣬������֤·���Ƿ��ǿɴ�ġ�
			if (from == to) continue;

			if (astar::eFindFail == this->line(from, to))
				return -3;
			from = to;
		}
		return 0;
	}

	template <typename AMap, int GridSize>
	int Finder<AMap, GridSize>::scantrailPixel(const Point & location, const astar::Path & trail, float & length /*out*/) const
	{
		if (trail.size() < 2) return -1;
		if (false == location.trail_equals(trail.front())) return -2; // �ϸ�ƴ�ӣ�����Ҳ����һ��

		length = 0.0f;
		astar::Path::const_iterator first = trail.begin();
		const Point* from = &*first;
		for (astar::Path::const_iterator second = ++trail.begin(), last = trail.end(); second != last; ++second)
		{
			const Point* to = &*second;
			length  += first->distance(*second);
			first   = second;

			if (astar::eFindFail == this->linePixel(*from, *to))
				return -3;
			from = to;
		}
		return 0;
	}

} // namespace astar

#pragma pop_macro("new")

#endif