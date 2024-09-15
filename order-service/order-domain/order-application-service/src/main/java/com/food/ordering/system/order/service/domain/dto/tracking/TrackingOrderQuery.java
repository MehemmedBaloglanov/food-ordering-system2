package com.food.ordering.system.order.service.domain.dto.tracking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class TrackingOrderQuery {
    private final UUID trackingOrderId;
}
