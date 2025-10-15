using System;
using System.Drawing;
using System.Windows.Forms;
using System.Xml;

namespace GUIEditor2
{
	public class CComponentProgressBar : CComponent
	{
		private readonly CFormMain m_mainform;
		private readonly CFormProgressBar m_form;
    
        private IBrush m_brush2;
        private Color m_fontColor;
        
		public CFormProgressBar Form
		{
			get { return m_form; }
		}

        public IBrush Brush1
        {
            get { return Brush; }
            protected set { Brush = value; }
        }

        public IBrush Brush2
        {
            get { return m_brush2; }
            protected set { m_brush2 = value; }
        }

        public Color FontColor
        {
            get { return m_fontColor; }
            set { m_fontColor = value; }
        }

		public override void Dispose()
		{
			m_form.Dispose();
		}

		public CComponentProgressBar(CComponentDesktop desktop, CFormMain mainform, string name) : base(desktop)
		{
			Name = name;
			m_mainform = mainform;
			m_form = new CFormProgressBar(m_mainform, this);
			m_form.TopLevel = false;
			m_form.N = Name;            
            m_brush2 = Render.CreateBrush();
            m_form.FontColor = Brush1.GetColor();
		}

		public override CComponent Clone()
		{
			CComponentProgressBar one = new CComponentProgressBar(Desktop, m_mainform, Name);
			one.Parent = Parent;
            Rectangle rect = Rect;
            rect.Offset(5, 10);
			one.Rect = rect;
            one.Lock = Lock;
			
            one.Brush1 = Brush1.Clone();
            one.Brush2 = Brush2.Clone();

			one.Form.N = m_form.N;
			one.Form.C = m_form.C;
			one.OnSize();
			one.Form.ImageBlockName = m_form.ImageBlockName;
            one.Form.BackgroundImageName = m_form.BackgroundImageName;
            one.Form.FontColor = FontColor;

			one.Form.IsViewOnly = m_form.IsViewOnly;
			one.Form.TipInfo = m_form.TipInfo;
            one.Form.ShowPercent = m_form.ShowPercent;
			return one;
		}

		public override Form GetDialog()
		{
			return m_form;
		}

		public override void OnMove()
		{
			Rectangle rect = Rect;
			m_form.X = rect.X;
			m_form.Y = rect.Y;
		}

		public override void OnSize()
		{
			Rectangle rect = Rect;
			m_form.X = rect.X;
			m_form.Y = rect.Y;
			m_form.W = rect.Width;
			m_form.H = rect.Height;
		}

        public void GetImage2Param(out Rectangle rect, out int w0, out int w1, out int h0, out int h1, out EExpandMode mode)
        {
            rect = m_brush2.GetImageRect();
            m_brush2.GetImageBlock(out w0, out w1, out h0, out h1);
            mode = m_brush2.GetImageExpandMode();
        }

        public void SetImage2Param(Rectangle rect, int w0, int w1, int h0, int h1, EExpandMode mode)
        {
            m_brush2.SetImageBlock(rect, w0, w1, h0, h1, mode);
            if (mode == (EExpandMode.H_TILE | EExpandMode.V_TILE))
                Rect = new Rectangle(Rect.X, Rect.Y, rect.Width, rect.Height);
        }

		public override void SaveXML(XmlDocument doc, XmlElement elem)
		{
			XmlElement sub = doc.CreateElement("ProgressBar");
			base.SaveXML(doc, sub);
            sub.SetAttribute("c", CComponent.Color2String(m_fontColor));

            if( m_form.ShowPercent )
                sub.SetAttribute("percent", "true" );
            if( m_form.IsViewOnly )
                sub.SetAttribute("viewonly", "true" );

			sub.SetAttribute("tipinfo", m_form.TipInfo);

            SaveImage(doc, sub, m_brush2, "Image2");
           
			elem.AppendChild(sub);
		}

        public override void LoadXML(XmlElement elem)
        {
            base.LoadXML(elem);
            LoadImage(elem, m_brush2, "Image2");
            m_form.N = Name;
            m_form.C = Class;
            m_form.ImageBlockName = Brush.GetImageFileName();
            m_form.BackgroundImageName = Brush2.GetImageFileName();
            m_form.FontColor = GetAttributeColor(elem, "c");
            m_form.IsViewOnly = GetAttributeBool(elem, "viewonly");
            m_form.TipInfo = GetAttributeString(elem, "tipinfo");
            m_form.ShowPercent = GetAttributeBool(elem, "percent");
        }

        // 进度条上显示的百分比数字。
        // 仅仅用于编辑器测试效果。
        private int _PercentValue = 35;

        public override void OnKeyDown(KeyEventArgs e)
        {
            // 用户按 -，= 键或A,S键可以调整进度条的显示进度，以观察效果。
            // shift键控制调整的步长(缺省10，shift键按下时，进度为1).
            int step = 10;
            if (e.Shift)
            {
                step = 1;
            }

            if ( e.KeyCode == Keys.Oemplus )
            {
                _PercentValue += step;
                if (_PercentValue > 100)
                    _PercentValue = 100;
            }
            else if (e.KeyCode == Keys.OemMinus)
            {
                if (_PercentValue < step)
                    _PercentValue = 0;
                else
                    _PercentValue -= step;
            }
            else if (e.KeyCode == Keys.A)
            {
                _PercentValue += step;
                if (_PercentValue > 100)
                    _PercentValue = 100;
            }
            else if (e.KeyCode == Keys.S)
            {
                if (_PercentValue < step)
                    _PercentValue = 0;
                else
                    _PercentValue -= step;
            }

            return;
        }
        
        public override void OnDraw()
        {
            if (Render != null && !Hide)
            {  
                // 利用背景刷画背景图
                if (Brush2 != null )
                {
                    Render.DrawRect(AbsRect, Brush2);
                }

                // 利用前景刷画进度块
                if (Brush1 != null)
                {
                    Rectangle rect = AbsRect;
                    {
                        rect.Width = rect.Width * _PercentValue / 100;
                        Render.DrawRect(rect, Brush1);
                    }
                }

                // 如果控件被选中了，画选中框
                if (Select && AbsRect.Width > 0 && AbsRect.Height > 0)
                     Render.DrawBox(AbsRect, Color.FromArgb(192, 255, 0, 255));
            }

            // 根据是否显示百分比选项，在进度条上显示百分比信息
            if (m_form.ShowPercent)
            {
                IFont font = Render.CreateFont();
                if (font != null)
                {
                    font.SetFont("SimSun", 12);
                    font.SetColor(FontColor);
                    font.SetAlign(ETextAlign.H_CENTER | ETextAlign.V_CENTER);
                    string prog = string.Format("{0}%", _PercentValue);
                    Render.DrawText(AbsRect, prog, font);
                }
            }
        }

	}
}
