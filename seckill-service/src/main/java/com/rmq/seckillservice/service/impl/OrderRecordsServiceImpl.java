package com.rmq.seckillservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rmq.seckillservice.entity.OrderRecords;
import com.rmq.seckillservice.service.OrderRecordsService;
import com.rmq.seckillservice.mapper.OrderRecordsMapper;
import org.springframework.stereotype.Service;

/**
* @author yangnan
* @description 针对表【order_records】的数据库操作Service实现
* @createDate 2025-04-21 15:40:06
*/
@Service
public class OrderRecordsServiceImpl extends ServiceImpl<OrderRecordsMapper, OrderRecords>
    implements OrderRecordsService{

}




