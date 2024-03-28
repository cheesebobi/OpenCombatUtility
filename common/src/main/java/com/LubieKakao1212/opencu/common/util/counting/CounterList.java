package com.LubieKakao1212.opencu.common.util.counting;

import java.util.ArrayList;
import java.util.List;

public class CounterList<T extends ICounter> {

    private List<T> elements;

    public CounterList() {
        elements = new ArrayList<>();
    }

    public void add(T element) {
        elements.add(element);
    }

    public void tick() {
        elements.removeIf(T::decrement);
    }

    public int size() {
        return elements.size();
    }

}