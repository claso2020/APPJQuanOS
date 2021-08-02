package ar.com.quan.quanos;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import ar.com.quan.quanos.Interfaces.ErrorResponseHandler;
import ar.com.quan.quanos.Interfaces.SuccessResponseHandler;

public class WebService {
    public static void verificarUsuario(final Context contexto, final String usu , final String pass
            , final SuccessResponseHandler successResponseHandler, final ErrorResponseHandler errorHandler ) {

        RequestQueue queue = Volley.newRequestQueue(contexto);
        String url =contexto.getResources().getString(R.string.srvLocProduccion);
        url=url+"login/Authenticate?usuario="+usu+"&password="+pass;
        StringRequest req = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject valores = new JSONObject(response);
                            String error = valores.getString("error");
                            if (error.substring(0, 2).equals("00")) {
                                successResponseHandler.onSuccess(valores);
                            } else if (error.substring(0, 2).equals("10")) {
                                successResponseHandler.onSuccess(valores);
                            } else if (error.substring(0, 2).equals("01")) {
                                String msgAviso = valores.getString("msg_error");
                                errorHandler.onError(msgAviso);
                            } else {
                                errorHandler.onError("Error al autenticar usuario:\n\tCódigo de retorno inválido");
                            }
                        } catch (Exception errEx) {
                            errorHandler.onError("Error desconocido al autenticar usuario:\n\t" + errEx.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorHandler.onError(contexto.getResources().getString(R.string.msgErrorConexion));
                        } else if (error.getClass().equals(ServerError.class)) {
                            errorHandler.onError(contexto.getResources().getString(R.string.msgErrorServidor));
                        }else if (error.getClass().equals(NoConnectionError.class)) {
                            errorHandler.onError(contexto.getResources().getString(R.string.msgErrorSinConexion));
                        } else {
                            errorHandler.onError("Error desconocido al autenticar usuario:\n" + error);
                        }
                        return;
                    }
                }) {

            /*protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                return params;
            }*/
        };
        // Add the request to the RequestQueue.
        req.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(40), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);
    }

    public static void leerSexos(final Context contexto, String idConexion , final String soloHabilitados
            , final SuccessResponseHandler successResponseHandler, final ErrorResponseHandler errorHandler ) {

        SharedPreferences  globales = contexto.getSharedPreferences("datosGlobalesApp",contexto.MODE_PRIVATE);
        SharedPreferences.Editor editorGlobales = globales.edit();
        String token =globales.getString("token", null);
        idConexion =globales.getString("idUsuario", null);

        String url =contexto.getResources().getString(R.string.srvLocProduccion);
        url=url+"sistema/leerSexos?idConexion="+idConexion+"&soloHabilitados=true";
                //"40D50A31-6DC9-443F-BA5C-705DC5E96403&soloHabilitados=true";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respuesta = new JSONObject(response);
                    JSONObject resultado =new JSONObject(respuesta.getString("resultado"));
                    JSONObject msgError =new JSONObject(resultado.getString("msgError"));

                    if (msgError.getString("valorDevuelto").equals("00")) {
                        successResponseHandler.onSuccess(respuesta);
                    } else if (msgError.getString("valorDevuelto").equals("10")) {
                        successResponseHandler.onSuccess(respuesta);
                    } else if (msgError.getString("valorDevuelto").equals("01")) {
                        String msgAviso = respuesta.getString("msg_error");
                        errorHandler.onError(msgAviso);
                    } else {
                        errorHandler.onError("Error al autenticar usuario:\n\tCódigo de retorno inválido");
                    }
                } catch (Exception errEx) {
                    errorHandler.onError("Error desconocido al autenticar usuario:\n\t" + errEx.getMessage());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
                errorHandler.onError("Error: " + error);
            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Authorization", token );//"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6ImRlbW8iLCJuYmYiOjE2Mjc5MTM4MzEsImV4cCI6MTYyNzk1NzAzMSwiaWF0IjoxNjI3OTEzODMxLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjQ5NDcyIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo0OTQ3MiJ9.ty1MuRsIWcQ8GpXddwQpmsaSmIVsKjBIQdcNLsD_8TI");
                //params.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6ImRlbW8iLCJuYmYiOjE2Mjc2MDYyMTksImV4cCI6MTYyNzY0OTQxOSwiaWF0IjoxNjI3NjA2MjE5LCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjQ5NDcyIiwiYXVkIjoiaHR0cDovL2xvY2FsaG9zdDo0OTQ3MiJ9.3EjDWuguN7oeLvSAMLkoiAAZfVCZ8_aOUILCYLriSM0");
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idConexion", "E793B2BF-7745-442C-B482-A71D5566B54A");
                params.put("soloHabilitados", "true");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(contexto);
        queue.add(request);

    }




}
