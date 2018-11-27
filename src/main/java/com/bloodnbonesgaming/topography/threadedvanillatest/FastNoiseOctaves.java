package com.bloodnbonesgaming.topography.threadedvanillatest;

import com.bloodnbonesgaming.topography.util.noise.FastNoise;

public class FastNoiseOctaves {
	
	final FastNoise noise;
	final int octaves;
	
	public FastNoiseOctaves(final FastNoise noise, final int octaves)
	{
		this.noise = noise;
		this.octaves = octaves;
	}
	
	public float GetNoise(float x, float y, float z, double persistence) {
		return this.GetNoise(x, y, z, persistence, true);
	}
	
	public float GetNoise(float x, float y, float z, double persistence, boolean normalize) {
		if (normalize)
    	{
			float total = 0;
            float frequency = 1;
            float amplitude = 1;
            float maxValue = 0;  // Used for normalizing result to 0.0 - 1.0
            for(int i=0;i<octaves;i++) {
                total += ((this.noise.GetNoise(x * frequency, y * frequency, z * frequency) + 1) / 2) * amplitude;
                
                maxValue += amplitude;
                
                amplitude *= persistence;
                frequency *= 2;
            }
            
            return total/maxValue;
    	}
    	else
    	{
    		float total = 0;
    		float frequency = 1;
    		float amplitude = 1;
            for(int i=0;i<octaves;i++) {
                total += ((this.noise.GetNoise(x * frequency, y * frequency, z * frequency) + 1) / 2) * amplitude;
                
                amplitude *= persistence;
                frequency *= 2;
            }
            
            return total;
    	}
	}
}
