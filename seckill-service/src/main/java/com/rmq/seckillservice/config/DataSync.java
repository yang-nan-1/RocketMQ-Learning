package com.rmq.seckillservice.config;

import com.rmq.seckillservice.entity.Goods;
import com.rmq.seckillservice.mapper.GoodsMapper;
import com.rmq.seckillservice.service.impl.GoodsServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Component
public class DataSync {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private GoodsMapper goodsMapper;
//    @Scheduled(cron = 0 0 10 0 0 ?)
//    public void initData(){
//
//    }


    //这里为了测试把定时任务改成了每次启动后执行
    @PostConstruct
    public void initData(){
        List<Goods> goodsList=goodsMapper.selectList(null);
        if(CollectionUtils.isEmpty(goodsList))
            return;

        goodsList.forEach(goods -> {
            stringRedisTemplate.opsForValue().set("goodsId:"+goods.getId(),goods.getStocks().toString());
        });
    }
}
