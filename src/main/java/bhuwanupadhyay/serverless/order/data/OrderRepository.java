package bhuwanupadhyay.serverless.order.data;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderRepository {

    private final DynamoDBMapper mapper;

    public OrderRepository() {
        mapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard().build());
    }

    public List<Order> getOrders() {
        PaginatedScanList<Order> orders = mapper.scan(Order.class, new DynamoDBScanExpression());
        return new ArrayList<>(orders);
    }

    public Order save(Order order) {
        mapper.save(order);
        return mapper.load(order);
    }

    public Optional<Order> findByOrderId(String orderId) {
        return Optional.ofNullable(mapper.load(Order.class, orderId));
    }
}
