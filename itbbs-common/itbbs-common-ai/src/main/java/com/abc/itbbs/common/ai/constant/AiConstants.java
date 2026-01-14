package com.abc.itbbs.common.ai.constant;

public class AiConstants {

    /**
     * DeepSeek
     */
    public static final String DEEP_SEEK_ENDPOINT = "https://api.deepseek.com";
    public static final String DEEP_SEEK_REQUEST_PATH = "/chat/completions";
    public static final String DEEP_SEEK_MODEL = "deepseek-chat";


    /**
     * Ollama
     */
    public static final String OLLAMA_ENDPOINT = "http://127.0.0.1:11434";
    public static final String OLLAMA_REQUEST_PATH = "/api/chat";
    public static final String OLLAMA_MODEL = "deepseek-r1:8b";

    /**
     * 讯飞
     */
    public static final String XUN_FEI_ENDPOINT = "https://spark-api-open.xf-yun.com/v2";
    public static final String XUN_FEI_REQUEST_PATH = "/chat/completions";
    public static final String XUN_FEI_MODEL = "x1";


    /**
     * 文本嵌入模型 Ollama
     */
    public static final String EMBEDDING_OLLAMA_REQUEST_PATH = "/api/embeddings";
    public static final String EMBEDDING_OLLAMA_DEFAULT_MODEL = "nomic-embed-text";



}
