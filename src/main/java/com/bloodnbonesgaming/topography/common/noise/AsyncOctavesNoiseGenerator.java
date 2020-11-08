package com.bloodnbonesgaming.topography.common.noise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import com.bloodnbonesgaming.topography.Topography;
import com.bloodnbonesgaming.topography.common.config.ConfigurationManager;

import net.minecraft.util.SharedSeedRandom;
import net.minecraft.world.gen.ImprovedNoiseGenerator;
import net.minecraft.world.gen.OctavesNoiseGenerator;

public class AsyncOctavesNoiseGenerator extends OctavesNoiseGenerator {

	public AsyncOctavesNoiseGenerator(SharedSeedRandom p_i232142_1_, IntStream p_i232142_2_) {
		super(p_i232142_1_, p_i232142_2_);
	}

	@Override
	public double getValue(double x, double y, double z, double p_215462_7_, double p_215462_9_, boolean p_215462_11_) {
		double d0 = 0.0D;
		double d1 = this.field_227461_c_;
		double d2 = this.field_227460_b_;

		final List<Callable<Double>> callables = new ArrayList<Callable<Double>>();

		for (ImprovedNoiseGenerator improvednoisegenerator : this.octaves) {
			if (improvednoisegenerator != null) {
				//d0 += improvednoisegenerator.func_215456_a(maintainPrecision(x * d1), p_215462_11_ ? -improvednoisegenerator.yCoord : maintainPrecision(y * d1), maintainPrecision(z * d1), p_215462_7_ * d1, p_215462_9_ * d1) * d2;
				callables.add(new CallableNoiseGenerator(improvednoisegenerator, maintainPrecision(x * d1), p_215462_11_ ? -improvednoisegenerator.yCoord : maintainPrecision(y * d1), maintainPrecision(z * d1), p_215462_7_ * d1, p_215462_9_ * d1, d2));
			}

			d1 /= 2.0D;
			d2 *= 2.0D;
		}

		try {
			List<Future<Double>> futures = ConfigurationManager.getExecutor().invokeAll(callables);
			
			for (Future<Double> future : futures) {
				d0 += future.get();
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return d0;
	}

	private static class CallableNoiseGenerator implements Callable<Double> {
		
		private final ImprovedNoiseGenerator generator;
		private final double x;
		private final double y;
		private final double z;
		private final double a;
		private final double b;
		private final double c;

		private CallableNoiseGenerator(ImprovedNoiseGenerator generator, double x, double y, double z, double a, double b, double c) {
			this.generator = generator;
			this.x = x;
			this.y = y;
			this.z = z;
			this.a = a;
			this.b = b;
			this.c = c;
		}

		@Override
		public Double call() throws Exception {
			return generator.func_215456_a(x, y, z, a, b) * c;
		}

	}
}
