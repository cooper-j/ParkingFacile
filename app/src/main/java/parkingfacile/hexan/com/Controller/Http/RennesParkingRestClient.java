package parkingfacile.hexan.com.Controller.Http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by james_000 on 9/20/2015.
 */
public abstract class RennesParkingRestClient {
    private static final String BASE_URL = "http://data.citedia.com/r1";

    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("From", "james.cooper@epitech.eu");
        client.addHeader("User-Agent", "AccesParking");
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    /**
     * @param relativeUrl
     * @return the AbsoluteUrl
     */
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
