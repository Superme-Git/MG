"                                            \n\
#ifdef GL_ES                                \n\
precision lowp float;                        \n\
#endif                                        \n\
\n\
varying vec4 v_fragmentColor;                \n\
varying vec2 v_texCoord;                    \n\
uniform sampler2D u_texture;                \n\
uniform float u_f_verticle;                 \n\
uniform float u_f_red;                      \n\
\n\
void main()                                    \n\
{                                           \n\
vec4 vTexture = texture2D(u_texture, v_texCoord);\n\
gl_FragColor = v_fragmentColor * vTexture; \n\
if(v_texCoord.y < u_f_verticle)         \n\
{                                       \n\
    float adv = (vTexture.r + vTexture.g + vTexture.b)*0.333; \n\
    vTexture.r = vTexture.g = vTexture.b = adv; \n\
    gl_FragColor = v_fragmentColor * vTexture;            \n\
    if(v_texCoord.y > u_f_red)              \n\
    {                                       \n\
    gl_FragColor = (gl_FragColor*0.5) + vec4(0.8, 0.08, 0.08, 1.0)*0.5;\n\
    }                                       \n\
}                                                       \n\
}                                         \n\
";
