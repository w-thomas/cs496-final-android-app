package biz.psyphon.beersociety.VolleyRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Will on 12/3/2016 for BeerSociety.
 * Requests object for user's personal event data
 */

public class myEventRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://10.0.2.2:8080/user/mydata";
    private Map<String, String> headers;

    public myEventRequest(String authToken, Response.Listener<String> listener){
        super(Method.GET, REGISTER_REQUEST_URL, listener, null);

        headers = new HashMap<>();
        headers.put("x-access-token", authToken);
    }

    @Override
    public Map<String, String> getHeaders(){
        return headers;
    }
}
