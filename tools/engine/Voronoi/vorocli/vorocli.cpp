/*
<applet code="vorocli.class" height=350 width=500>
<param name="takap" value="350">
<param name="habap" value="500">
<param name="Np" value="20">
</applet>
*/

struct Point
{
	double x;
	double y;
};

typedef double  VectorD[];
typedef int	VectorN[];
typedef Point VectorPoint[];
typedef DWORD Color;

static double pow2( double value )
{
	return value * value;
}

static int uround( double value )
{
	return (int)(value+0.5d);
}

static void heapv(VectorD xArray,
				  VectorD yArray,
				  int size)
{
	for (int i = size / 2; i >= 1; --i)
	{
		double d1 = xArray[i - 1];
		double d2 = yArray[i - 1]; 
		
		int j = i;
		while (2 * j <= size)
		{
			int k = 2 * j;

			if ((k + 1 <= size) && (xArray[k - 1] < xArray[k]))
			{
				++k;
			}

			if (xArray[k - 1] <= d1)
				break;

			xArray[j - 1] = xArray[k - 1];
			yArray[j - 1] = yArray[k - 1];

			j = k;
		}

		xArray[j - 1] = d1;
		yArray[j - 1] = d2;
		
	}

	for (int i = size - 1; i >= 1; --i)
	{
		double d1 = xArray[i];
		double d2 = yArray[i]; 
		
		xArray[i] = xArray[0];
		yArray[i] = yArray[0]; 
		
		int j = 1;

		while (2 * j <= i)
		{
			int k = 2 * j;
			if ((k + 1 <= i) && (xArray[k - 1] <= xArray[k]))
			{
				++k;
			}

			if (xArray[k - 1] <= d1)
				break;

			xArray[j - 1] = xArray[k - 1];
			yArray[j - 1] = yArray[k - 1];
		
			j = k;
		}

		xArray[j - 1] = d1;
		yArray[j - 1] = d2;
		
	}
}

class vorocli
{
	VectorPoint m_p = new Point[100];
	Color m_col1;
	Color m_col2;
	Color m_col3;
	int m_n;
	int m_height;
	int m_width;

public:
	void init( int width, int height )
	{
		m_col1 = Color.blue;
		m_col2 = Color.yellow;
		m_col3 = Color.white;
		m_width = width;
		m_height = height;
		m_n = 0;
	}

	void paint(Graphics paramGraphics)
	{
		// paramGraphics.setColor(m_col1);
		// paramGraphics.fillRect(1, 1, m_width, m_height);
		// paramGraphics.setColor(m_col2);

		int N = m_n;

		VectorD _kx = new double[100];
		VectorD _ky = new double[100];
		VectorD _x1 = new double[100];
		VectorD _y1 = new double[100];
		
		for (int k = 0; k < N; k ++ )
		{
			_x1[k] = (m_p[k].x + cos(k * 1.1D) + k * 0.0001D);
			_y1[k] = (m_p[k].y + sin(k * 1.1D) + k * 0.001D);

			int _x = (int)_x1[k];
			int _y = (int)_y1[k];

			//paramGraphics.fillOval(_x - 2, _y - 2, 4, 4);
		}

		if (N > 1) 
		{
			//paramGraphics.setColor(m_col3);
			double x0;
			double y0;
			double xa1 = 0.0;
			double ya1 = 0.0;
			
			for (int i = 0; i < N - 1; i ++)
			{
				for (int j = i + 1; j <= N; j ++)
				{
					// 计算垂直平分线与可见区的交点坐标(x0,y0),(xa1,ya1)
					double di2 = (_y1[i] - _y1[j]) / (_x1[i] - _x1[j]);
					double di = -1.0 / di2;
					double cpy = (_y1[i] + _y1[j]) / 2.0;
					double cpx = (_x1[i] + _x1[j]) / 2.0;
					double ys = cpy - (cpx * di);

					if ((ys > 0.0) && (ys < m_height))
					{
						// 位于左边线上
						x0 = 0.0;
						y0 = ys;
					}
					else if (di > 0.0)
					{
						// 底边线
						x0 = -ys / di;
						y0 = 0.0;
					}
					else
					{
						// 位于上边线上
						x0 = (m_height - ys) / di;
						y0 = m_height;
					}

					double yy = di * m_width + ys;
					if ((yy > 0.0) && (yy < m_height))
					{
						// 右边线
						xa1 = m_width;
						ya1 = yy;
					}
					else if (di > 0.0) 
					{
						// 上边线交点
						xa1 = (m_height - ys) / di;
						ya1 = m_height;
					}
					else 
					{
						// 下边线交点
						xa1 = -ys / di;
						ya1 = 0.0;
					}
					
					_kx[0] = x0;
					_ky[0] = y0;
					int l = 1;
					double _sa2 = _x1[j] - _x1[i];
					double _sa4 = _y1[j] - _y1[i];

					double t = pow2(_sa2) + pow2(_sa4);

					bool _break = false;
					for (int k = 0; k < N; k ++ )
					{
						if ((k != i) && (k != j))
						{
							double di4 = (_y1[i] - _y1[k]) / (_x1[i] - _x1[k]); // p(i), p(k) 的斜率
							double di3 = -1.0 / di4;							// p(i), p(k) 的中垂线的斜率

							double cpy3 = (_y1[i] + _y1[k]) / 2.0;				// p(i), p(k) 的中点坐标(cpx3,cpy3)
							double cpx3 = (_x1[i] + _x1[k]) / 2.0;				// 	
							double ys3 = cpy3 - (cpx3 * di3);					// p(i), p(k) 的中垂线的与y轴的交点
							double y20 = di3 * x0 + ys3;						// p(i), p(k) 的中垂线与p(i),p(j)中垂线的交点边界(x0,y20),(xa1,y21)
							double y21 = di3 * xa1 + ys3;						//

							double _sa0 = y0 - y20;								// 
							double _sa1 = ya1 - y21;
							double _sa3 = _x1[k] - _x1[i];
							double _sa5 = _y1[k] - _y1[i];

							double t2 = pow2(_sa3) + pow2(_sa5);
							
							bool sa = false;
							if ((_sa2 * _sa3 > 0.0) && (_sa4 * _sa5 > 0.0))
							{
								sa = true;
							}

							if ((_sa0 * _sa1 > 0.0) && (t > t2) && sa)
							{
								_break = true;
								break;
							}

							if (((_sa0 * _sa1 < 0.0) || (t < t2) || sa) &&
								((_sa0 * _sa1 < 0.0) || (t > t2)) )
							{
								_kx[l] = (ys3 - ys) / (di - di3);
								_ky[l] = di * _kx[l] + ys;
								l ++;
							}
						}
					}

					if (!_break)
					{
						_kx[l] = xa1;
						_ky[l] = ya1;
						l ++;

						heapv(_kx, _ky, l);

						for (int k = 0; k < l-1; k ++ )
						{
							double _xx = (_kx[k] + _kx[k+1]) / 2.0;
							double _yy = di * _xx + ys;
							double _ds = pow2(_xx - _x1[i]) + pow2(_yy - _y1[i]);

							bool _breaked = false;
							for (int u = 0; u < N; u ++ )
							{
								if ((u != i) && (u != j)) 
								{
									double us = pow2(_xx - _x1[u]) + pow2(_yy - _y1[u]);
									if ( us < _ds )
									{
										_breaked = true;
										break;
									}
								}
							}

							if (!_breaked)
							{
								int x1 = uround(_kx[k]);
								int x2 = uround(_kx[k+1]);
								int y1 = uround(_ky[k]);
								int y2 = uround(_ky[k+1]);
								// paramGraphics.drawLine(x1, y1, x2, y2);
								break;
							}
						}
					}
				}
			}
		}

		//paramGraphics.drawString("Number of Sites So Far = " + N, 15, 15); 
	}

	bool mouseDown(int x, int y)
	{
		if (m_n < 50)
		{
			m_p[m_n] = new Point(x, y); 
			m_n += 1;
		}
		else 
		{
			m_n = 0;
		}

		repaint();

		return true;
	}

	void repaint()
	{

	}
}