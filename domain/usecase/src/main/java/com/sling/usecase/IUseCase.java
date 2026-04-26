package com.sling.usecase;

@FunctionalInterface
public interface IUseCase<T, R> {
    R execute(T command);
}
