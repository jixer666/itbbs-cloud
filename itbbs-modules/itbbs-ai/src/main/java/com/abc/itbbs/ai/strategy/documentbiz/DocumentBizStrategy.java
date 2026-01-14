package com.abc.itbbs.ai.strategy.documentbiz;

import java.util.List;

public interface DocumentBizStrategy<T> {

    List<T> run(Object obj);

}
