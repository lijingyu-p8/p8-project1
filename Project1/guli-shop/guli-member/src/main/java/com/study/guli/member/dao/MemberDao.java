package com.study.guli.member.dao;

import com.study.guli.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author lijingyu
 * @email lijingyu@gmail.com
 * @date 2021-11-14 14:38:41
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
