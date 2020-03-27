package bhuwanupadhyay.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.lang.reflect.ParameterizedType;

public abstract class HttpEventHandler<T> implements RequestHandler<ApiGatewayRequest, ApiGatewayResponse> {

    @Override
    public ApiGatewayResponse handleRequest(ApiGatewayRequest input, Context context) {
        try {
            return this.handle(toRequestIfNoVoidType(input), context.getLogger());
        } catch (Exception e) {
            return ApiGatewayResponse.serverError(ExceptionUtils.getStackTrace(e));
        }
    }

    protected abstract ApiGatewayResponse handle(T request, LambdaLogger log);

    private T toRequestIfNoVoidType(ApiGatewayRequest input) {
        final Class<T> bodyType = getBodyType();
        return !bodyType.equals(Void.class) ? input.toBody(bodyType) : null;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getBodyType() {
        try {
            String typeName = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
            return (Class<T>) Class.forName(typeName);
        } catch (Exception e) {
            throw new RuntimeException("Class is not parametrized with generic type!!! Please use extends <> ", e);
        }
    }

}