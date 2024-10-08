package com.food.ordering.system.order.service.domain;


import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderCreateHelper {
    private final OrderDomainService orderDomainService;

    private final CustomerRepository customerRepository;

    private final OrderRepository orderRepository;

    private final RestaurantRepository restaurantRepository;

    private final OrderDataMapper orderDataMapper;

    public OrderCreateHelper(OrderDomainService orderDomainService,
                             CustomerRepository customerRepository,
                             OrderRepository orderRepository,
                             RestaurantRepository restaurantRepository,
                             OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitializeOrder(order, restaurant);
        saveOrder(order);
        log.info("Order is created with id {}", orderCreatedEvent.getOrder().getId().getValue());
        return orderCreatedEvent;
    }
    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findRestaurantInformation(restaurant);
        if (optionalRestaurant.isEmpty()){
            log.warn("Restaurant {} not found", createOrderCommand.getRestaurantId());
            throw new OrderDomainException("Restaurant " + createOrderCommand.getRestaurantId() + " not found");
        }
        return optionalRestaurant.get();
    }


    private void checkCustomer(@NotNull UUID customerId) {
        Optional<Customer> customer =  customerRepository.findCustomer(customerId);
        if (customer.isEmpty()) {
            log.error("Customer not found: {}", customerId);
            throw new OrderDomainException("Customer not found");
        }
    }

    private Order saveOrder(Order order) {
        Order save = orderRepository.save(order);
        if(order == null){
            log.error("Order could not be saved");
            throw new OrderDomainException("Could not save order!");
        }
        log.info("Order is saved with id: {}", order.getId().getValue());
        return save;
    }
}
