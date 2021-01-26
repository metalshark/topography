package com.bloodnbonesgaming.topography.common.util;

public final class Functions {
	
	@FunctionalInterface
	public interface VarArgFunction<T, R> {
		R apply(T... args);
	}
	
	@FunctionalInterface
	public interface VarArgBiFunction<T, U, R> {
		R apply(T var, U... args);
	}
	
	@FunctionalInterface
	public interface VarArgTriFunction<T, U, V, R> {
		R apply(T var, U bar2, V... args);
	}
	
	@FunctionalInterface
	public interface TriFunction<T, U, V, R> {
		R apply(T var, U var2, V var3);
	}
	
	@FunctionalInterface
	public interface QuadFunction<T, U, V, W, R> {
		R apply(T var, U var2, V var3, W var4);
	}
	
	@FunctionalInterface
	public interface QuadConsumer<T, U, V, W> {
		void apply(T var, U var2, V var3, W var4);
	}
}