package com.abc.itbbs.blog.mapper;

import com.abc.itbbs.blog.domain.dto.SensitiveWordDTO;
import com.abc.itbbs.blog.domain.entity.SensitiveWord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 敏感词Mapper接口
 *
 * @author LiJunXi
 * @date 2025-12-31
 */
@Mapper
public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {
    List<SensitiveWord> selectSensitiveWordList(SensitiveWordDTO sensitiveWordDTO);
}
