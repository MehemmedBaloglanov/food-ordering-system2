package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.tracking.TrackingOrderQuery;
import com.food.ordering.system.order.service.domain.dto.tracking.TrackingOrderResponse;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Service
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderTrackCommandHandler orderTrackCommandHandler;

    private final OrderCreatedCommandHandler orderCreatedCommandHandler;

    public OrderApplicationServiceImpl(OrderTrackCommandHandler orderTrackCommandHandler,
                                       OrderCreatedCommandHandler orderCreatedCommandHandler) {
        this.orderTrackCommandHandler = orderTrackCommandHandler;
        this.orderCreatedCommandHandler = orderCreatedCommandHandler;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreatedCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackingOrderResponse trackOrder(TrackingOrderQuery trackingOrderQuery) {
        return orderTrackCommandHandler.trackOrder(trackingOrderQuery);
    }
}
