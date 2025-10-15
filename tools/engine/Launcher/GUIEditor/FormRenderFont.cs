using System;
using System.Drawing;
using System.Windows.Forms;

namespace GUIEditor2
{
	public partial class CFormRenderFont : UserControl
	{
		private readonly CFormMainFont m_form;
		private IRender m_render;

		public IRender Render
		{
			get { return m_render; }
		}

		public CFormRenderFont(CFormMainFont form)
		{
			InitializeComponent();

			m_form = form;
			m_render = Program.GetMainForm().Panel.Render;
		}

		protected override void OnLoad(EventArgs e)
		{
			SetStyle(ControlStyles.Opaque, true);
		}

		protected override void OnPaint(PaintEventArgs e)
		{
			if(m_render != null)
			{
				if(m_render.DrawBegin(this, m_form.BkColor, 1, 1))
				{
					string text = m_form.PreviewText;
					if(text.Length > 0)
					{
						CFontProfile.CFontFormatParam fontformat = CFontProfile.FindFormat(m_form.PreviewFontFormat);
						if(fontformat != null)
						{
							IFont font = CFontProfile.GetFont(fontformat.fontid);
							if(font != null)
							{
								Rectangle rect = new Rectangle(0, 0, ClientSize.Width, ClientSize.Height / 5);
								for(int i = 0; i < 5; ++i)
								{
									font.SetColor(fontformat.state[i].color);
									font.SetAlign(ETextAlign.H_LEFT | ETextAlign.V_TOP | ETextAlign.SINGLELINE);
									m_render.DrawTextEx(rect, text, font, fontformat.GetEffect(i), fontformat.state[i].effcolor);
									rect.Y += ClientSize.Height / 5;
								}
							}
						}
					}
					m_render.DrawEnd();
				}
			}
		}
	}
}
