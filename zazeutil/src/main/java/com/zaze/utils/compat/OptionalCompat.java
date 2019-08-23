package com.zaze.utils.compat;

import android.os.Build;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Description :
 * 修改自 {@link java.util.Optional} 以便使用于 android N 之前的版本
 * 功能不够就 参考源码 继续添加
 *
 * @author : ZAZE
 * @version : 2019-08-24 - 15:05
 */
public final class OptionalCompat<T> {

    private static final OptionalCompat<?> EMPTY = new OptionalCompat<>();

    private final T value;

    private OptionalCompat() {
        this.value = null;
    }

    private OptionalCompat(T value) {
        this.value = requireNonNull(value);
    }

    public static <T> T requireNonNull(T obj) {
        if (obj == null)
            throw new NullPointerException();
        return obj;
    }

    // ------------------------------------------------------
    public static @NotNull
    <T> OptionalCompat<T> of(@NotNull T value) {
        return new OptionalCompat<>(value);
    }

    public static @NotNull
    <T> OptionalCompat<T> ofNullable(@Nullable T value) {
        if (value == null) {
            return empty();
        } else {
            return of(value);
        }
    }

    // ------------------------------------------------------

    public static @NotNull
    <T> OptionalCompat<T> empty() {
        @SuppressWarnings("unchecked")
        OptionalCompat<T> t = (OptionalCompat<T>) EMPTY;
        return t;
    }

    // ------------------------------------------------------

    /**
     * Return {@code true} if there is a value present, otherwise {@code false}.
     *
     * @return {@code true} if there is a value present, otherwise {@code false}
     */
    public boolean isPresent() {
        return value != null;
    }

    public @NotNull
    T get() {
        if (value == null) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }


    /**
     * Return the value if present, otherwise return {@code other}.
     *
     * @param other the value to be returned if there is no value present, may
     *              be null
     * @return the value, if present, otherwise {@code other}
     */
    public T orElse(T other) {
        return value != null ? value : other;
    }


    // ------------------------------------------------------

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OptionalCompat)) {
            return false;
        }

        OptionalCompat<?> other = (OptionalCompat<?>) obj;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.equals(value, other.value);
        } else {
            return (value == other.value) || (value != null && value.equals(other.value));
        }
    }


    @Override
    public int hashCode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hashCode(value);
        } else {
            return value != null ? value.hashCode() : 0;
        }
    }

    @Override
    public String toString() {
        return value != null
                ? String.format("Optional[%s]", value)
                : "Optional.empty";
    }
}
