package bhuwanupadhyay.serverless;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;


@Getter
public class ApiGatewayResponse {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    private final int statusCode;
    private final String body;
    private final Map<String, String> headers = new HashMap<>();
    private final boolean isBase64Encoded;

    private ApiGatewayResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
        this.setHeaders();
        this.isBase64Encoded = false;
    }

    public static ApiGatewayResponse bad(String message) {
        return build(HttpStatus.SC_BAD_REQUEST, message);
    }

    public static <T> ApiGatewayResponse ok(T body) {
        return build(HttpStatus.SC_OK, body);
    }

    static ApiGatewayResponse serverError(String message) {
        return build(HttpStatus.SC_INTERNAL_SERVER_ERROR, message);
    }

    @SneakyThrows
    private static <T> ApiGatewayResponse build(int statusCode, T body) {
        return new ApiGatewayResponse(statusCode, MAPPER.writeValueAsString(body));
    }

    private void setHeaders() {
        headers.put("X-Powered-By", "BhuwanUpadhyay");
        headers.put("Content-Type", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
    }
}
