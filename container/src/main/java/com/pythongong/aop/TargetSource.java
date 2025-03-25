package com.pythongong.aop;

import com.pythongong.stereotype.Nullable;

public record TargetSource(Object target) {
    /**
     * Return the type of targets returned by this {@link TargetSource}.
     * <p>
     * Can return <code>null</code>, although certain usages of a
     * <code>TargetSource</code> might just work with a predetermined
     * target class.
     * 
     * @return the type of targets returned by this {@link TargetSource}
     */
    @Nullable
    public Class<?>[] getTargetClass() {
        return this.target.getClass().getInterfaces();
    }

}