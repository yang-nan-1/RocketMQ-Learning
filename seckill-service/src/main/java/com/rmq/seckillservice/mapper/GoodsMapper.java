package com.rmq.seckillservice.mapper;

import com.rmq.seckillservice.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author yangnan
* @description 针对表【goods】的数据库操作Mapper
* @createDate 2025-04-21 15:40:06
* @Entity com.rmq.seckillservice.entity.Goods
*/
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    List<Goods> selectSeckillGoods();
}




