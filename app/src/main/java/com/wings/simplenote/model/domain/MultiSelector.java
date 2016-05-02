package com.wings.simplenote.model.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wing on 2016/5/2.
 */
public class MultiSelector {
    private Set<Long> mSet;
    private boolean isSelectable;

    public MultiSelector() {
        this.isSelectable = false;
        this.mSet = new HashSet<>();
    }

    public void add(Long item) {
        mSet.add(item);
    }

    public void remove(Long item) {
        mSet.remove(item);
    }

    public boolean contains(Long item) {
        return mSet.contains(item);
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
    }

    public void clear() {
        mSet.clear();
    }

    public boolean isEmpty() {
        return mSet.isEmpty();
    }

}

