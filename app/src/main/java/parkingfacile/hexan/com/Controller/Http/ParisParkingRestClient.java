package parkingfacile.hexan.com.Controller.Http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by james_000 on 9/20/2015.
 */
public abstract class ParisParkingRestClient {
    private static final String BASE_URL = "http://opendata.paris.fr/api/records/1.0/search";

    private static AsyncHttpClient client = new AsyncHttpClient();

    /**
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
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
