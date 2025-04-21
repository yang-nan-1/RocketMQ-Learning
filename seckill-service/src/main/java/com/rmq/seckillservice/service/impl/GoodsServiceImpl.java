package com.rmq.seckillservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rmq.seckillservice.entity.Goods;
import com.rmq.seckillservice.service.GoodsService;
import com.rmq.seckillservice.mapper.GoodsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
* @author yangnan
* @description 针对表【goods】的数据库操作Service实现
* @createDate 2025-04-21 15:40:06
*/
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods>
    implements GoodsService{


    /**
     * 扣减库存
     * 写订单表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void realSeckill(Integer userId, Integer goodsId) {
        boolean isUpdate = update()
                .setSql("stock = stock-1")
                .set("update_time",new Date())
                .eq("id", goodsId)
                .gt("stock", 0)
                .update();

        if (!isUpdate){
            throw new RuntimeException("商品"+goodsId+"库存不足");
        }
    }
}




