package bhuwanupadhyay.serverless.order;

import bhuwanupadhyay.serverless.ApiGatewayResponse;
import bhuwanupadhyay.serverless.HttpEventHandler;
import bhuwanupadhyay.serverless.order.data.Order;
import bhuwanupadhyay.serverless.order.data.OrderRepository;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import java.util.Optional;

import static bhuwanupadhyay.serverless.ApiGatewayResponse.bad;
import static bhuwanupadhyay.serverless.ApiGatewayResponse.ok;

public class OrderProcessHandler extends HttpEventHandler<Order> {

    private final OrderRepository repository = new OrderRepository();

    @Override
    protected ApiGatewayResponse handle(Order request, LambdaLogger log) {
        Optional<Order> order = repository.findByOrderId(request.getOrderId());
        return order.
                map(o -> bad(String.format("Order already exist with id %s", o.getOrderId()))).
                orElse(ok(repository.save(request)));
    }

}
