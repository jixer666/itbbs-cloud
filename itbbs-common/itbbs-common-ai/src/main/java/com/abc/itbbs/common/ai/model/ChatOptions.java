package com.abc.itbbs.common.ai.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ChatOptions {

    /**
     * 指定使用的大模型名称。
     * 例如："gpt-4", "qwen-max", "claude-3-sonnet" 等。
     * 如果未设置，将使用客户端默认模型。
     */
    private String model;

    /**
     * 随机种子（Seed），用于控制生成结果的可重复性。
     * 当 seed 相同时，相同输入将产生相同输出（前提是其他参数也一致）。
     * 注意：并非所有模型都支持 seed 参数。
     */
    private String seed;

    /**
     * 温度（Temperature）控制输出的随机性。
     * <ul>
     *   <li>值越低（如 0.1~0.3）：输出更确定、稳定、可重复，适合事实性任务（如 RAG、结构化输出）</li>
     *   <li>值越高（如 0.7~1.0）：输出更多样、有创意，但可能不稳定或偏离事实</li>
     * </ul>
     * 推荐值：
     * <ul>
     *   <li>文档处理、路由、工具调用：0.1 ~ 0.3</li>
     *   <li>问答、摘要：0.2 ~ 0.5</li>
     *   <li>创意写作：0.7 ~ 1.0</li>
     * </ul>
     * 默认值：0.5f
     */
    private Float temperature = 0.5f;

    /**
     * Top-p（也称 nucleus sampling）控制生成时考虑的概率质量。
     * 模型从累积概率不超过 p 的最小词集中采样。
     * - 值为 1.0 表示考虑所有词（等同于无 top-p 限制）
     * - 值为 0.9 表示只考虑累积概率达 90% 的词
     * 注意：temperature 和 top_p 不应同时调整，通常只用其一。
     */
    private Float topP;

    /**
     * Top-k 控制生成时考虑的最高概率词的数量。
     * 模型仅从 top-k 个最可能的词中采样。
     * - 值为 50 表示只考虑概率最高的 50 个词
     * - 值越小，输出越确定；值越大，输出越多样
     * 注意：与 top_p 类似，通常不与 temperature 同时使用。
     */
    private Integer topK;

    /**
     * 生成内容的最大 token 数量（不包括输入 prompt）。
     * 用于限制响应长度，防止生成过长内容。
     * 注意：不同模型有不同上限，超过将被截断或报错。
     */
    private Integer maxTokens;

    /**
     * 停止序列（Stop Sequences），当生成内容包含这些字符串时立即停止。
     * 例如：设置为 ["\n", "。"] 可在句末或换行时停止。
     * 适用于需要精确控制输出长度的场景。
     */
    private List<String> stop;

    /**
     * 是否启用“思考模式”（Thinking Mode）。
     * 适用于支持该特性的模型（如 Qwen3），开启后模型会显式输出推理过程。
     * 默认为 null（由模型决定）。
     */
    private Boolean thinkingEnabled;

    /**
     * 额外的模型参数，用于传递模型特有或未明确暴露的配置。
     * 例如：{"response_format": "json", "presence_penalty": 0.5}
     * 使用 addExtra() 方法可方便地添加单个参数。
     */
    private Map<String, Object> extra;


    protected Boolean retryEnabled; // 默认开启错误重试
    protected Integer retryCount;
    protected Integer retryInitialDelayMs;


    /**
     * 是否为流式请求。
     * 这个不允许用户设置，由 Framework 自动设置（用户设置也可能被修改）。
     * 用户调用 chat 或者 chatStream 方法时，会自动设置这个字段。
     */
    private boolean streaming;


}
