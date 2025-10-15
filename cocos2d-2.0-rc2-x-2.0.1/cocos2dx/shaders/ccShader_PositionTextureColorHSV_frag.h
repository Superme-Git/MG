"                                            \n\
#ifdef GL_ES                                \n\
precision mediump float;                        \n\
#endif                                        \n\
                                            \n\
varying vec4 v_fragmentColor;                \n\
varying vec4 v_PartParam0;                \n\
varying vec4 v_PartParam1;                \n\
varying vec4 v_PartParam2;                \n\
varying vec2 v_texCoord;                    \n\
uniform sampler2D u_texture;                \n\
uniform sampler2D u_texture_part;                \n\
                                            \n\
float GetPartIndex()                                    \n\
{                                            \n\
	vec4 texPart = texture2D(u_texture_part, v_texCoord);                                            \n\
	if (texPart.g > 0.5)                                            \n\
		return 1.5;                                            \n\
	else if (texPart.b > 0.5)                                            \n\
		return 2.5;                                            \n\
	else if (texPart.r > 0.5)                                            \n\
		return 0.5;                                            \n\
	else                                            \n\
		return 1.5;                                            \n\
}											\n\
vec2 GetPartHS(float PartIndex)                                    \n\
{                                            \n\
	if (PartIndex > 2.0)                                            \n\
		return vec2(v_PartParam0.ba);                                            \n\
	else if (PartIndex > 1.0)                                            \n\
		return vec2(v_PartParam0.rg);                                            \n\
	else                                            \n\
		return vec2(0.0, 0.0);                                            \n\
}											\n\
vec4 GetPartColor(float PartIndex)                                    \n\
{                                            \n\
	if (PartIndex > 2.0)                                            \n\
		return v_PartParam2;                                            \n\
	else if (PartIndex > 1.0)                                            \n\
		return v_PartParam1;                                            \n\
	else                                            \n\
		return vec4(2.0, 3.0, 4.0, 0.5);                                            \n\
}											\n\
float colorChange(vec3 Src, float fv)                                    \n\
{                                            \n\
	if (fv < 0.5)                                            \n\
		return 0.0;                                            \n\
	else if (fv < 1.5)                                            \n\
		return 1.0;                                            \n\
	else if (fv < 2.5)                                            \n\
		return Src.r;                                            \n\
	else if (fv < 3.5)                                            \n\
		return Src.g;                                            \n\
	else if (fv < 4.5)                                            \n\
		return Src.b;                                            \n\
	else if (fv < 5.5)                                            \n\
		return 1.0 - Src.r;                                            \n\
	else if (fv < 6.5)                                            \n\
		return 1.0 - Src.g;                                            \n\
	else if (fv < 7.5)                                            \n\
		return 1.0 - Src.b;                                            \n\
	else                                            \n\
		return 0.0;                                            \n\
}											\n\
vec4 GetRealColor(vec4 Src, vec4 Color)                                         \n\
{                                            \n\
	vec3 c = Src.rgb;                                         \n\
	if (Color.a > 1.0)                                         \n\
	{                                         \n\
		float mc = 0.0;                                         \n\
		if (Color.a > 3.0)                                            \n\
			mc = (Src.r + Src.g + Src.b) * 0.33333;                                            \n\
		else if (Color.a > 2.0)                                            \n\
			mc = max(Src.r, max(Src.g, Src.b));                                            \n\
		else                                           \n\
			mc = min(Src.r, min(Src.g, Src.b));                                            \n\
		mc = mc + Color.r;                                            \n\
		c = vec3(mc, mc, mc);                                            \n\
	}                                            \n\
	else                                            \n\
	{                                            \n\
		c = vec3(colorChange(Src.rgb,Color.r), colorChange(Src.rgb,Color.g), colorChange(Src.rgb,Color.b));          \n\
	}                                            \n\
	return vec4(c,Src.a);                                         \n\
}											\n\
vec4 RGB2HSV(vec4 rgb)										\n\
{									\n\
	vec4 hsv;																\n\
	float max1 = max(rgb.x, max(rgb.y, rgb.z));											\n\
	float min1 = min(rgb.x, min(rgb.y, rgb.z));											\n\
	min1 = max1 - min1;											\n\
	if (rgb.x == max1)															\n\
		hsv.x = (rgb.y - rgb.z) / min1;									\n\
	else if (rgb.y == max1)														\n\
		hsv.x = 2.0 + (rgb.z - rgb.x) / min1;							\n\
	else if (rgb.z == max1)													\n\
		hsv.x = 4.0 + (rgb.x - rgb.y) / min1;								\n\
	hsv.x = hsv.x * 60.0;												\n\
	if (hsv.x < 0.0)														\n\
		hsv.x = hsv.x + 360.0;											\n\
	hsv.z = max1;													\n\
	hsv.y = min1 / max1;									\n\
	hsv.w = rgb.a;														\n\
	return hsv;																\n\
}																			\n\
vec4 HSV2RGB(vec4 hsv, vec2 Change)											\n\
{											\n\
	hsv.x = hsv.x + Change.x;											\n\
	hsv.x = mod(hsv.x,360.0);											\n\
	hsv.y = hsv.y+hsv.y*Change.y;											\n\
	float R, G, B;											\n\
	if (hsv.y == 0.0)											\n\
		R = G = B = hsv.z;											\n\
	else											\n\
	{											\n\
		hsv.x = hsv.x * 0.016667;											\n\
		int i = int(hsv.x);											\n\
		float f = hsv.x - float(i);											\n\
		float zy = hsv.z * hsv.y;											\n\
		float zyf = zy*f;											\n\
		float a = hsv.z - zy;											\n\
		float b = hsv.z - zyf;											\n\
		float c = a + zyf;											\n\
		if(i == 0)											\n\
		{ R = hsv.z; G = c; B = a;}											\n\
		else if(i==1)														\n\
		{ R = b; G = hsv.z; B = a;}											\n\
		else if(i==2)										\n\
		{ R = a; G = hsv.z; B = c;}											\n\
		else if(i==3)									\n\
		{ R = a; G = b; B = hsv.z;}											\n\
		else if(i==4)											\n\
		{ R = c; G = a; B = hsv.z;}										\n\
		else											\n\
		{ R = hsv.z; G = a; B = b;}											\n\
	}											\n\
	return vec4(R,G,B,hsv.a);											\n\
}\n\
void main()                                    \n\
{                                            \n\
	vec4 texColor = texture2D(u_texture, v_texCoord);\n\
	float PartIndex = GetPartIndex();\n\
	gl_FragColor = texColor;\n\
	gl_FragColor.a = gl_FragColor.a * v_fragmentColor.a;\n\
	if (PartIndex > 1.0 && gl_FragColor.a > 0.0) \n\
	{\n\
		gl_FragColor = GetRealColor( gl_FragColor, GetPartColor(PartIndex));\n\
		vec2 hs = GetPartHS(PartIndex);\n\
		if(hs.x != 0.0 || hs.y != 0.0){ \n\
			gl_FragColor = RGB2HSV(gl_FragColor);\n\
			gl_FragColor = HSV2RGB(gl_FragColor,hs ); \n\
		}\n\
	}\n\
}                                         \n\
";