package ar.com.quan.quanos.Fragmentos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.os.StrictMode;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jama.carouselview.enums.IndicatorAnimationType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import ar.com.quan.quanos.Fabrica.Dialogos;
import ar.com.quan.quanos.Fabrica.datosCompartidos;
import ar.com.quan.quanos.Interfaces.ErrorResponseHandler;
import ar.com.quan.quanos.Interfaces.FragmentChangeTrigger;
import ar.com.quan.quanos.Interfaces.SuccessResponseHandler;
import ar.com.quan.quanos.Principal;
import ar.com.quan.quanos.R;
import ar.com.quan.quanos.TableDynamic;
import ar.com.quan.quanos.WebService;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;

public class fragmento_muestraTitulares extends Fragment implements View.OnClickListener {
    FragmentChangeTrigger trigger;
    private Context contexto;
    TableDynamic tablaDinamicaTitulares;
    private Dialog dialogo;
    private datosCompartidos dtCompartidos;

    String [] encabezadoTitulares = {"Titular ","CUIL", "Modificar"};
    ArrayList<String[]> filasTitulares = new ArrayList<String[]>();
    TableLayout tablaMuestraTitulares;

    ImageButton btnVolverAInicial;
    public fragmento_muestraTitulares() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_muestra_titulares, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contexto=view.getContext();

        btnVolverAInicial =(ImageButton) view.findViewById(R.id.btnVolverAInicial);
        btnVolverAInicial.setOnClickListener(this);


        tablaMuestraTitulares =view.findViewById(R.id.tablaMuestraTitulares);
        tablaDinamicaTitulares =new TableDynamic(tablaMuestraTitulares,contexto,"tablaDinamicaTitulares"){
            @Override
            public void onClick(View v) {
                int clicked_id = v.getId();
                int puntero = clicked_id - 1;

                if (clicked_id > 10000) { //Botón modificar
                    String[] dataSeleccionada;
                    dataSeleccionada=data.get(puntero-10000); //Guardo los datos seleccionados

                    //dtCompartidos= ViewModelProvider.of(getActivity()).get(datosCompartidos.class);
                  //dtCompartidos= ViewModelProvider(getActivity()).get(datosCompartidos.class);
                    //dtCompartidos= ;
                    dtCompartidos= new ViewModelProvider(getActivity()).get(datosCompartidos.class);
                    dtCompartidos.setIdGeneral(dataSeleccionada[3]);
                    trigger.fireChange("inicial_btnCargaTitulares");


                    //dataSeleccionada[3];
                }

            }
        };
        tablaDinamicaTitulares.addHeader(encabezadoTitulares);
        tablaDinamicaTitulares.addData(filasTitulares); //le paso filas vacia sino ir a buscaDatosTablas()
        tablaDinamicaTitulares.backgroundHeader(Color.parseColor("#819FF7"));
        tablaDinamicaTitulares.backgroundData(Color.parseColor("#95cbf5"), Color.parseColor("#68879e"));
        tablaDinamicaTitulares.lineColor(Color.BLACK);
        tablaDinamicaTitulares.textColorData(Color.WHITE);
        llenaTitulares();
    }
    public void llenaTitulares(){
        dialogo = Dialogos.dlgBuscando(getActivity(),"Recuperando titulares");
        WebService.leerTitulares(getActivity()
                , new SuccessResponseHandler<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject resultado) {
                        dialogo.dismiss();
                        try {
                            JSONObject completo =new JSONObject(resultado.getString("resultado"));
                            String datos =completo.getString("titulares");
                            JSONArray arrayCompleto = new JSONArray(datos);
                            for (int i = 0; i < arrayCompleto.length(); i++) {
                                JSONObject arrayFila = arrayCompleto.getJSONObject(i);
                                String id = arrayFila.getString("idTitular");
                                String nombre = arrayFila.getString("apellido");
                                String cuil = arrayFila.getString("cuil");

                                String[]itemTitulares = new String[]{nombre, cuil, "Modificar", id};

                                //tablaDinamicaRelLab.addItems(itemRelLab);
                                tablaDinamicaTitulares.addItems(itemTitulares);
                            }

                            dialogo.dismiss();
                            //String a = getKey(mapSexos,"Femenino");

                        } catch (Exception errEx) {
                            dialogo = Dialogos.dlgError(getActivity(),errEx.getMessage());
                        }
                    }
                }, new ErrorResponseHandler() {
                    @Override
                    public void onError(String msg) {
                        dialogo.dismiss();
                        dialogo = Dialogos.dlgError(getActivity(),msg);
                    }
                });
    }
    public void ModificaTitulares(String[] datosSeleccionados){
        String[] datosS=datosSeleccionados;

    }
    public void setTrigger(FragmentChangeTrigger trigger) {
        this.trigger = trigger;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnVolverAInicial){
            trigger.fireChange("muestraTitulares_btnVolverAInicial");
        }
    }
}