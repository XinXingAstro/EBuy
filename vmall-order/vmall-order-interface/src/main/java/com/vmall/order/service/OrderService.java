package com.vmall.order.service;

import com.vmall.common.pojo.VMallResult;
import com.vmall.order.pojo.OrderInfo;

public interface OrderService {
    VMallResult createOrder(OrderInfo orderInfo);
}
