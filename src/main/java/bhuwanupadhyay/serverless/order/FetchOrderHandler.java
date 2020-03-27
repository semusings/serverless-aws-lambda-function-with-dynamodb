package bhuwanupadhyay.serverless.order;

import bhuwanupadhyay.serverless.ApiGatewayResponse;
import bhuwanupadhyay.serverless.HttpEventHandler;
import bhuwanupadhyay.serverless.order.data.OrderRepository;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

public class FetchOrderHandler extends HttpEventHandler<Void> {

    private final OrderRepository repository = new OrderRepository();

    @Override
    protected ApiGatewayResponse handle(Void request, LambdaLogger log) {
        log.log("Fetching orders....");
        return ApiGatewayResponse.ok(repository.getOrders());
    }
}
