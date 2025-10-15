#ifdef GL_ES                                    
precision lowp float;                           
#endif                                          

varying vec4 v_fragmentColor;                   
varying vec2 v_texCoord;                        
uniform sampler2D CC_Texture0;                    
uniform sampler2D u_texture_alpha;                
uniform     int  u_tss_color_op;                        
//uniform   int  u_tss_alpha_op;  //add later if needed 

void main()                                     
{                                               
vec4 texClr = vec4(texture2D(CC_Texture0, v_texCoord).rgb, texture2D(u_texture_alpha, v_texCoord).a);                             
if(u_tss_color_op == 0)//XPTOP_MODULATE                                     
{gl_FragColor = v_fragmentColor * texClr;                                   
}else if(u_tss_color_op == 1)//XPTOP_SELECTARG1(texture)                    
{gl_FragColor = vec4(texClr.rgb, texClr.a*v_fragmentColor.a);               
}else if(u_tss_color_op == 2)//XPTOP_SELECTARG2(diffuse)                    
{gl_FragColor = vec4(v_fragmentColor.rgb, texClr.a*v_fragmentColor.a);      
}else if(u_tss_color_op == 3)//XPTOP_ADD                                    
{gl_FragColor = vec4(v_fragmentColor.rgb + texClr.rgb, texClr.a*v_fragmentColor.a);           
}else if(u_tss_color_op == 4)//XPTOP_SUBTRACT                               
{gl_FragColor = vec4(texClr.rgb - v_fragmentColor.rgb, texClr.a*v_fragmentColor.a);                                   
}else if(u_tss_color_op == 5)//XPTOP_ADDSMOOTH                              
{gl_FragColor = vec4(v_fragmentColor.rgb + texClr.rgb, texClr.a*v_fragmentColor.a);                        
//}else if(u_tss_color_op == 6)//XPTOP_ADDSIGNED                              
//{gl_FragColor = v_fragmentColor*texClr*2.0;                                 
}else if(u_tss_color_op == 7 || u_tss_color_op == 6)//XPTOP_MODULATE2                              
{gl_FragColor = vec4((v_fragmentColor.rgb * texClr.rgb)*2.0, texClr.a*v_fragmentColor.a);       
}                                                                           
}    