package ru.nsu.shapatin.context;

import java.util.*;

public class Context {
    public Deque<Double> stack = new ArrayDeque<>();
    public Map<String, Double> parameters = new HashMap<>();
}
