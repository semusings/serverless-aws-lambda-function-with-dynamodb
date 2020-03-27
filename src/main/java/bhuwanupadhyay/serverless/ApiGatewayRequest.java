package bhuwanupadhyay.serverless;

import lombok.Data;
import lombok.SneakyThrows;

import java.util.Map;

import static bhuwanupadhyay.serverless.ApiGatewayResponse.MAPPER;

@Data
public class ApiGatewayRequest {

    private String resource;
    private String path;
    private String httpMethod;
    private Map<String, String> headers;
    private Map<String, String> queryStringParameters;
    private Map<String, String> pathParameters;
    private Map<String, String> stageVariables;
    private Map<String, Object> requestContext;
    private String body;
    private boolean isBase64Encoded;

    @SneakyThrows
    <T> T toBody(Class<T> valueType) {
        return MAPPER.readValue(body, valueType);
    }
}
