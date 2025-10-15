/*
<applet code="vorocli.class" height=350 width=500>
<param name="takap" value="350">
<param name="habap" value="500">
<param name="Np" value="20">
</applet>
*/

import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Point;

public class vorocli extends Applet
{
  Point[] p = new Point[100];
  Color col1;
  Color col2;
  Color col3;
  int N;
  int taka;
  int haba;
  int k;
  int i;
  int j;
  int l;
  int n;
  double di2;
  double di;
  double cp2;
  double cpx;
  double ys;
  double t;
  double x0;
  double y0;
  double xx;
  double yy;
  double xa1 = 0.0D;
  double ya1 = 0.0D;
  double yy2;
  double di4;
  double di3;
  double cp3;
  double cpx3;
  double ys3;
  double t2;
  double ds;
  double us;
  double y20;
  double y21;
  double sa0;
  double sa1;
  double sa2;
  double sa3;
  double sa4;
  double sa5;
  int sa6;
  int br;
  int br2;
  int u;
  int k2;
  int xz;
  int xz2;
  int yz;
  int yz2;
  String NS;
  String habaS;
  String takaS;
  double Nd;
  double takad;
  double habad;
  double[] kx = new double[100];
  double[] ky = new double[100];
  double[] kz = new double[100];
  double[] x1 = new double[100];
  double[] y1 = new double[100];
  int[] x = new int[100];
  int[] y = new int[100];
  double[] s = new double[100];
  String[] sss = new String[100];

  public double dou(String paramString)
  {
    double d = Double.valueOf(paramString).doubleValue();
    return d;
  }

  public double rand() {
    double d = Math.random();
    return d; }

  public void init() {
    this.col1 = Color.blue;
    this.col2 = Color.yellow;
    this.col3 = Color.white;
    this.takaS = super.getParameter("takap");
    this.habaS = super.getParameter("habap");
    this.NS = super.getParameter("Np");
    this.habad = dou(this.habaS);
    this.takad = dou(this.takaS);
    this.Nd = dou(this.NS);
    this.haba = (int)this.habad;
    this.taka = (int)this.takad;
    this.N = (int)this.Nd;
    if (this.N == 20)
      this.N = (4 + (int)(46.0D * rand()));
  }

  public double jou(double paramDouble1, double paramDouble2)
  {
    double d = Math.pow(paramDouble1, paramDouble2);
    return d;
  }

  void heapv(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, double[] paramArrayOfDouble3, int paramInt)
  {
    int i3;
    int i2 = paramInt / 2;
    for (int i1 = i2; i1 >= 1; --i1) {
      i3 = i1;
      double d1 = paramArrayOfDouble1[(i3 - 1)]; double d2 = paramArrayOfDouble2[(i3 - 1)]; double d3 = paramArrayOfDouble3[(i3 - 1)];
      while (2 * i3 <= paramInt) {
        int i4 = 2 * i3;
        if ((i4 + 1 <= paramInt) && 
          (paramArrayOfDouble1[(i4 - 1)] < paramArrayOfDouble1[i4])) {
          ++i4;
        }

        if (paramArrayOfDouble1[(i4 - 1)] <= d1)
          break;

        paramArrayOfDouble1[(i3 - 1)] = paramArrayOfDouble1[(i4 - 1)]; paramArrayOfDouble2[(i3 - 1)] = paramArrayOfDouble2[(i4 - 1)]; paramArrayOfDouble3[(i3 - 1)] = paramArrayOfDouble3[(i4 - 1)];
        i3 = i4;
      }
      paramArrayOfDouble1[(i3 - 1)] = d1; paramArrayOfDouble2[(i3 - 1)] = d2; paramArrayOfDouble3[(i3 - 1)] = d3;
    }
    for (int i5 = paramInt - 1; i5 >= 1; --i5) {
      double d4 = paramArrayOfDouble1[i5]; double d5 = paramArrayOfDouble2[i5]; double d6 = paramArrayOfDouble3[i5];
      paramArrayOfDouble1[i5] = paramArrayOfDouble1[0]; paramArrayOfDouble2[i5] = paramArrayOfDouble2[0]; paramArrayOfDouble3[i5] = paramArrayOfDouble3[0];
      i3 = 1;
      while (2 * i3 <= i5) {
        i1 = 2 * i3;
        if ((i1 + 1 <= i5) && 
          (paramArrayOfDouble1[(i1 - 1)] <= paramArrayOfDouble1[i1])) {
          ++i1;
        }

        if (paramArrayOfDouble1[(i1 - 1)] <= d4)
          break;

        paramArrayOfDouble1[(i3 - 1)] = paramArrayOfDouble1[(i1 - 1)]; paramArrayOfDouble2[(i3 - 1)] = paramArrayOfDouble2[(i1 - 1)]; paramArrayOfDouble3[(i3 - 1)] = paramArrayOfDouble3[(i1 - 1)];
        i3 = i1;
      }
      paramArrayOfDouble1[(i3 - 1)] = d4; paramArrayOfDouble2[(i3 - 1)] = d5; paramArrayOfDouble3[(i3 - 1)] = d6; }
  }

  public void paint(Graphics paramGraphics) {
    paramGraphics.setColor(this.col1);
    paramGraphics.fillRect(1, 1, this.haba, this.taka);
    paramGraphics.setColor(this.col2);

    this.N = this.n;
    for (this.k = 0; this.k < this.N; this.k += 1) {
      this.x1[this.k] = (this.p[this.k].x + Math.cos(this.k * 1.1D) + this.k * 0.0001D);
      this.y1[this.k] = (this.p[this.k].y + Math.sin(this.k * 1.1D) + this.k * 0.001D);
      this.x[this.k] = (int)this.x1[this.k];
      this.y[this.k] = (int)this.y1[this.k];
      this.s[this.k] = jou(this.x1[this.k] * this.x1[this.k] + this.y1[this.k] * this.y1[this.k], 0.5D);
      paramGraphics.fillOval(this.x[this.k] - 2, this.y[this.k] - 2, 4, 4);
    }
    if (this.N > 1) {
      paramGraphics.setColor(this.col3);
      for (this.i = 1; this.i <= this.N - 1; this.i += 1)
        for (this.j = (this.i + 1); this.j <= this.N; this.j += 1) {
          this.br = 0;
          this.di2 = ((this.y1[(this.i - 1)] - this.y1[(this.j - 1)]) / (this.x1[(this.i - 1)] - this.x1[(this.j - 1)]));
          this.di = (-1.0D / this.di2);
          this.cp2 = ((this.y1[(this.i - 1)] + this.y1[(this.j - 1)]) / 2.0D);
          this.cpx = ((this.x1[(this.i - 1)] + this.x1[(this.j - 1)]) / 2.0D);
          this.ys = (this.cp2 - (this.cpx * this.di));
          this.t = (jou(this.x1[(this.i - 1)] - this.x1[(this.j - 1)], 2.0D) + jou(this.y1[(this.i - 1)] - this.y1[(this.j - 1)], 2.0D));
          if ((this.ys > 0.0D) && (this.ys < this.taka)) {
            this.x0 = 0.0D; this.y0 = this.ys;
          }
          else if (this.di > 0.0D) {
            this.x0 = (-this.ys / this.di); this.y0 = 0.0D;
          }
          else {
            this.x0 = ((this.taka - this.ys) / this.di);
            this.y0 = this.taka;
          }

          this.yy = (this.di * this.haba + this.ys);
          if ((this.yy > 0.0D) && (this.yy < this.taka)) {
            this.xa1 = this.haba; this.ya1 = this.yy;
          }
          else if (this.di > 0.0D) {
            this.xa1 = ((this.taka - this.ys) / this.di); this.ya1 = this.taka;
          }
          else {
            this.xa1 = (-this.ys / this.di);
            this.ya1 = 0.0D;
          }

          this.l = 1;
          this.kx[(this.l - 1)] = this.x0; this.ky[(this.l - 1)] = this.y0;
          this.sa2 = (this.x1[(this.j - 1)] - this.x1[(this.i - 1)]);
          this.sa4 = (this.y1[(this.j - 1)] - this.y1[(this.i - 1)]);
          for (this.k = 1; this.k <= this.N; this.k += 1)
            if ((this.k != this.i) && (this.k != this.j)) {
              this.di4 = ((this.y1[(this.i - 1)] - this.y1[(this.k - 1)]) / (this.x1[(this.i - 1)] - this.x1[(this.k - 1)]));
              this.di3 = (-1.0D / this.di4);
              this.cp3 = ((this.y1[(this.i - 1)] + this.y1[(this.k - 1)]) / 2.0D);
              this.cpx3 = ((this.x1[(this.i - 1)] + this.x1[(this.k - 1)]) / 2.0D);
              this.ys3 = (this.cp3 - (this.cpx3 * this.di3));
              this.t2 = (jou(this.x1[(this.i - 1)] - this.x1[(this.k - 1)], 2.0D) + jou(this.y1[(this.i - 1)] - this.y1[(this.k - 1)], 2.0D));
              this.y20 = (this.di3 * this.x0 + this.ys3);
              this.y21 = (this.di3 * this.xa1 + this.ys3);
              this.sa0 = (this.y0 - this.y20);
              this.sa1 = (this.ya1 - this.y21);
              this.sa3 = (this.x1[(this.k - 1)] - this.x1[(this.i - 1)]);
              this.sa5 = (this.y1[(this.k - 1)] - this.y1[(this.i - 1)]);
              if ((this.sa2 * this.sa3 > 0.0D) && (this.sa4 * this.sa5 > 0.0D)) {
                this.sa6 = 1;
              }
              else
                this.sa6 = 0;

              if ((this.sa0 * this.sa1 > 0.0D) && (this.t > this.t2) && (this.sa6 == 1)) {
                this.br = 1;
                break;
              }
              if ((((this.sa0 * this.sa1 < 0.0D) || (this.t < this.t2) || (this.sa6 != 0))) && ((
                (this.sa0 * this.sa1 < 0.0D) || (this.t > this.t2)))) {
                this.l += 1;
                this.kx[(this.l - 1)] = ((this.ys3 - this.ys) / (this.di - this.di3));
                this.ky[(this.l - 1)] = (this.di * this.kx[(this.l - 1)] + this.ys);
              }
            }


          if (this.br == 0) {
            this.l += 1;
            this.kx[(this.l - 1)] = this.xa1;
            this.ky[(this.l - 1)] = this.ya1;
            for (this.u = 1; this.u <= this.l; this.u += 1)
              this.kz[(this.u - 1)] = 0.0D;

            heapv(this.kx, this.ky, this.kz, this.l);
            for (this.k = 1; this.k <= this.l - 1; this.k += 1) {
              this.k2 = (this.k + 1);
              this.xx = ((this.kx[(this.k - 1)] + this.kx[(this.k2 - 1)]) / 2.0D);
              this.yy2 = (this.di * this.xx + this.ys);
              this.ds = (jou(this.xx - this.x1[(this.i - 1)], 2.0D) + jou(this.yy2 - this.y1[(this.i - 1)], 2.0D));
              this.br2 = 0;
              for (this.u = 1; this.u <= this.N; this.u += 1)
                if ((this.u != this.i) && (this.u != this.j)) {
                  this.us = (jou(this.xx - this.x1[(this.u - 1)], 2.0D) + jou(this.yy2 - this.y1[(this.u - 1)], 2.0D));
                  if (this.us < this.ds) {
                    this.br2 = 1;
                    break;
                  }
                }

              if (this.br2 == 0) {
                this.xz = (int)(this.kx[(this.k - 1)] + 0.5D);
                this.xz2 = (int)(this.kx[(this.k2 - 1)] + 0.5D);
                this.yz = (int)(this.ky[(this.k - 1)] + 0.5D);
                this.yz2 = (int)(this.ky[(this.k2 - 1)] + 0.5D);
                paramGraphics.drawLine(this.xz, this.yz, this.xz2, this.yz2);
                break;
              }
            }
          }
        }
    }

    paramGraphics.drawString("Number of Sites So Far = " + this.N, 15, 15); }

  public boolean mouseDown(Event paramEvent, int paramInt1, int paramInt2) {
    if (this.n < 50) {
      this.p[this.n] = new Point(paramInt1, paramInt2); this.n += 1;
    }
    else {
      this.n = 0;
    }
    repaint();
    return true;
  }
}