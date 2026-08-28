package com.example.rbc.engine;

import com.example.rbc.model.Rule;
import java.util.List;

/** Optional application-side abstraction for supplying rules from any source. */
@FunctionalInterface
public interface RuleProvider {
    List<Rule> getRules();
}
