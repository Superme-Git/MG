#ifdef GL_ES                                
precision lowp float;                        
#endif                                        

varying vec4 v_fragmentColor;                
varying vec2 v_texCoord;                    
uniform sampler2D CC_Texture0;                
uniform float u_f_verticle;                 
uniform float u_f_red;                      

void main()                                    
{                                           
vec4 vTexture = texture2D(CC_Texture0, v_texCoord);
gl_FragColor = v_fragmentColor * vTexture; 
if(v_texCoord.y < u_f_verticle)         
{                                       
    float adv = (vTexture.r + vTexture.g + vTexture.b)*0.333; 
    vTexture.r = vTexture.g = vTexture.b = adv; 
    gl_FragColor = v_fragmentColor * vTexture;            
    if(v_texCoord.y > u_f_red)              
    {                                       
    gl_FragColor = (gl_FragColor*0.5) + vec4(0.8, 0.08, 0.08, 1.0)*0.5;
    }                                       
}                                                       
}                                         
