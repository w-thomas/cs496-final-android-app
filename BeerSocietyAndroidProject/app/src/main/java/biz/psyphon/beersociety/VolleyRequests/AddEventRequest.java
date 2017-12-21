package biz.psyphon.beersociety.VolleyRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Will on 12/4/2016 for BeerSociety.
 * request pojo for adding an event from the general list into your own personal list.
 * Utilizing Volley library
 */

public class AddEventRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://10.0.2.2:8080/user/event";
    private Map<String, String> params;
    private Map<String, String> headers;

    public AddEventRequest(String eventId, String authToken, Response.Listener<String> listener){
        super(Method.PUT, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("events", eventId);

        headers = new HashMap<>();
        headers.put("x-access-token", authToken);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

    @Override
    public Map<String, String> getHeaders(){
        return headers;
    }
}
