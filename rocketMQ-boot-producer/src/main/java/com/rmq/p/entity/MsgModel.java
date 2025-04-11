package com.rmq.p.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgModel {
    private String orderSn; //订单号
    private Integer userId;  //用户id
    private String desc; // （描述信息）下单 短信 物流
}
