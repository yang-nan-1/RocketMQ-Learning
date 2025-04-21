package com.rmq.seckillservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rmq.seckillservice.entity.Goods;
import com.rmq.seckillservice.service.GoodsService;
import com.rmq.seckillservice.mapper.GoodsMapper;
import org.springframework.stereotype.Service;

/**
* @author yangnan
* @description 针对表【goods】的数据库操作Service实现
* @createDate 2025-04-21 15:40:06
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{

}




