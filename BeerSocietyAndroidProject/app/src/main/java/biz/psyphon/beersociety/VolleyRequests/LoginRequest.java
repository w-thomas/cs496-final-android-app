package biz.psyphon.beersociety.VolleyRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Will on 11/27/2016 for BeerSociety.
 * pojo for registration POST request via Volley
 */

public class LoginRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://10.0.2.2:8080/user/authenticate";
    private Map<String, String> params;

    public LoginRequest(String email, String password, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
