

#version 330 core


float MyRandom(int x, int y)
{
 	return fract(sin(dot(vec2(x,y) ,vec2(12.9898,78.233))) * 43758.5453);
}

float Cosine_Interpolate(float a, float b, float x)
{
	float ft = x * 3.1415927;
	float f = (1 - cos(ft)) * .5;

	return  a*(1-f) + b*f;
}

float InterpolatedNoise(float x, float y, float z)
{
      int intX    = int(x);
      float fractX = x - intX;

      int intY    = int(y);
      float fractY = y - intY;
	  
	  int intZ    = int(z);
      float fractZ = z - intZ;
	  
	  float v1 = MyRandom(intX,     intY);
      float v2 = MyRandom(intX + 1, intY);
      float v3 = MyRandom(intX,     intY + 1);
      float v4 = MyRandom(intX + 1, intY + 1);

      float i1 = Cosine_Interpolate(v1 , v2 , fractX);
      float i2 = Cosine_Interpolate(v3 , v4 , fractX);

      return Cosine_Interpolate(i1 , i2 , fractY);
	  
}

float noise(vec3 v)
{
	float noise = 0;
	float persist = 0.25;
	int octave = 5;
	float frequency = 4;
	float amplitude = 0.6;
	
	for(int i = 0; i < octave; i++)
	{
		frequency *= 10;//pow(2, i);
		amplitude *= 0.4;//pow(persist, i);
		
		noise += InterpolatedNoise(v.x * frequency, v.y * frequency, v.z * frequency) * amplitude;
	}
	
	return noise;
}

uniform sampler2D bird;

in vec3 v_normal;
in vec4 rawpos;
in vec4 TexCoord;
out vec4 outColor;

    void main() {
	
		vec3 DarkGrey = vec3(0.0, 0.5, 0.5);
		//vec3 LightGrey = vec3(0.35, 0.164706, 0.14);
		vec3 LightGrey = vec3(0.0, 0.0, 0.0);
		vec3 seed = rawpos.xyz;
		vec3 noisevec = vec3(noise(seed), noise(seed + 3.33), noise(seed + 7.77)) * 3.0;
		
		vec3 lightDir,halfV;
		float NdotL,NdotHV;
		vec3 n = normalize(v_normal);
                	
		vec4 globalAmbient = gl_LightModel.ambient * gl_FrontMaterial.ambient;
		vec4 ambient = vec4(0,0,0,0);
		vec4 diffuse = vec4(0,0,0,0);
		vec4 specular = vec4(0,0,0,0);
		vec4 color = globalAmbient;
					
		for(int i = 0; i < 4; i++){
							//get light direction
			lightDir = normalize(vec3(gl_ModelViewMatrix * gl_LightSource[i].position));
							//compute N.L
			NdotL = max(dot(n,lightDir),0.0);
							//get half vector
			halfV = normalize(vec3(gl_ModelViewMatrix * vec4(gl_LightSource[i].halfVector.xyz, 0.0)));
			ambient += gl_FrontMaterial.ambient * gl_LightSource[i].ambient * gl_Color;
					
			if (NdotL > 0.0) {
				diffuse += gl_FrontMaterial.diffuse * gl_LightSource[i].diffuse * NdotL * gl_Color;
					//"			halfV = normalize(halfVector);
				NdotHV = max(dot(n,halfV),0.0);
				specular += gl_LightSource[i].specular * pow(NdotHV, 10);
			}
		}
		
		color = color + ambient + diffuse;
		//color = vec4(mix(LightGrey, DarkGrey, noisevec[2]), 1.0) + specular;
		color = texture2D(bird, TexCoord.st) *  noisevec[2] + specular;
		
		
        gl_FragColor = color;
    }