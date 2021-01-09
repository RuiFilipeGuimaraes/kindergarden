package my.postal.codes.app.retriever.impl;

import com.google.gson.Gson;
import my.postal.codes.app.domain.external.PostalCodeSearchResponse;
import my.postal.codes.app.retriever.api.PostalCodeInfoRetriever;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@Component
public class HttpPostalCodeInfoRetriever implements PostalCodeInfoRetriever {

    private final String httpUrl;
    private final CloseableHttpClient httpClient;
    private final Gson serializer;

    public HttpPostalCodeInfoRetriever(@Value("${app.http.info.retriever.url}") final String httpUrl) {
        checkArgument(Strings.isNotBlank(httpUrl),
                String.format("httpUrl is null or blank. httpUrl:%s", httpUrl));
        this.httpUrl = httpUrl;
        this.httpClient = HttpClients.createDefault();
        this.serializer = new Gson();
    }

    @Override
    public Optional<PostalCodeSearchResponse> retrieveInfo(final String postalCode) {
        checkArgument(Strings.isNotBlank(postalCode),
                String.format("postalCode is null or blank. postalCode:%s", postalCode));

        Optional<PostalCodeSearchResponse> result = Optional.empty();

        HttpGet request = new HttpGet(httpUrl.concat("/").concat(postalCode));

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String resultJson = EntityUtils.toString(entity);
                result = Optional.of(serializer.fromJson(resultJson, PostalCodeSearchResponse.class));
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Copied from https://www.baeldung.com/java-http-request
     */
    private static class ParameterStringBuilder {
        public static String getParamsString(Map<String, String> params)
                throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            }

            String resultString = result.toString();
            return resultString.length() > 0
                    ? resultString.substring(0, resultString.length() - 1)
                    : resultString;
        }
    }
}
