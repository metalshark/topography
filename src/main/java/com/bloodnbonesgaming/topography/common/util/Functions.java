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
}