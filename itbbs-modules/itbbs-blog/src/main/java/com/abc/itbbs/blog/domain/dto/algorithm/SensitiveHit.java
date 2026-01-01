package com.abc.itbbs.blog.domain.dto.algorithm;

import com.abc.itbbs.blog.domain.entity.SensitiveWord;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SensitiveHit {

    /** 命中开始位置 */
    private int start;

    /** 命中结束位置 */
    private int end;

    /** 命中的敏感词实体 */
    private SensitiveWord sensitiveWord;

    public SensitiveHit(int start, int end, SensitiveWord sensitiveWord) {
        this.start = start;
        this.end = end;
        this.sensitiveWord = sensitiveWord;
    }

}
