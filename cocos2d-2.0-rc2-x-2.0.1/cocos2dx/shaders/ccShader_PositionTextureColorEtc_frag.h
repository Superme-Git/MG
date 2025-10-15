"                                            \n\
#ifdef GL_ES                                \n\
precision lowp float;                        \n\
#endif                                        \n\
                                            \n\
varying vec4 v_fragmentColor;                \n\
varying vec2 v_texCoord;                    \n\
uniform sampler2D u_texture;                \n\
uniform sampler2D u_texture_alpha;                \n\
                                            \n\
void main()                                    \n\
{                                            \n\
gl_FragColor = vec4(v_fragmentColor.rgb*texture2D(u_texture, v_texCoord).rgb,  texture2D(u_texture_alpha, v_texCoord).r * v_fragmentColor.a);\n\
//gl_FragColor = v_fragmentColor * texture2D(u_texture, v_texCoord); \n\
//gl_FragColor.r = texture2D(u_texture_alpha, v_texCoord).a;gl_FragColor.a = 1.0;\n\
}                                         \n\
";
