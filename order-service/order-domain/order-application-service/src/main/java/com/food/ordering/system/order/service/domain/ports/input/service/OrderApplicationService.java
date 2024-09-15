package com.food.ordering.system.order.service.domain.ports.input.service;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.tracking.TrackingOrderQuery;
import com.food.ordering.system.order.service.domain.dto.tracking.TrackingOrderResponse;

import javax.validation.Valid;

public interface OrderApplicationService {
    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

    TrackingOrderResponse trackOrder(@Valid  TrackingOrderQuery trackingOrderQuery);
}


