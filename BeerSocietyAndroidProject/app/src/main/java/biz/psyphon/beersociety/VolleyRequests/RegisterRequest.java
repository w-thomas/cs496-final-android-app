package biz.psyphon.beersociety.VolleyRequests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Will on 11/27/2016 for BeerSociety.
 * pojo to hold the registration request data before sending to server
 */

public class RegisterRequest extends StringRequest{
    private static final String REGISTER_REQUEST_URL = "http://10.0.2.2:8080/user/register";
    private Map<String, String> params;

    public RegisterRequest(String name, String email, String password, String password2,
                           Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
        params.put("confirmPassword", password2);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
