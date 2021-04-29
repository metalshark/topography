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
	public interface QuinFunction<T, U, V, W, S, R> {
		R apply(T var, U var2, V var3, W var4, S var5);
	}
	
	@FunctionalInterface
	public interface HexFunction<T, U, V, W, X, Y, R> {
		R apply(T var, U var2, V var3, W var4, X var5, Y var6);
	}
	
	@FunctionalInterface
	public interface HepFunction<T, U, V, W, X, Y, R, Z> {
		R apply(T var, U var2, V var3, W var4, X var5, Y var6, Z var7);
	}
	
	@FunctionalInterface
	public interface QuadConsumer<T, U, V, W> {
		void apply(T var, U var2, V var3, W var4);
	}
}