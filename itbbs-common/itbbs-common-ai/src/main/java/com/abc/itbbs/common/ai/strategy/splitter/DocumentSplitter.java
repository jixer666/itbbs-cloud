package com.abc.itbbs.common.ai.strategy.splitter;

import com.abc.itbbs.common.ai.model.DocumentChunk;

import java.util.List;

public interface DocumentSplitter {

    List<DocumentChunk> split(String text);

}
