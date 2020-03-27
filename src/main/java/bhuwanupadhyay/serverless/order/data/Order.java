package bhuwanupadhyay.serverless.order.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;

@Data
@DynamoDBTable(tableName = "Order")
public class Order {

    @DynamoDBHashKey
    private String orderId;
    private String description;
    private String customer;
}
