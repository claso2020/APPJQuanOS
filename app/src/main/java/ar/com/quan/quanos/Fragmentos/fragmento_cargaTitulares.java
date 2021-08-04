package ar.com.quan.quanos.Fragmentos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import ar.com.quan.quanos.Fabrica.Dialogos;
import ar.com.quan.quanos.Interfaces.ErrorResponseHandler;
import ar.com.quan.quanos.Interfaces.FragmentChangeTrigger;
import ar.com.quan.quanos.Interfaces.SuccessResponseHandler;
import ar.com.quan.quanos.Principal;
import ar.com.quan.quanos.R;
import ar.com.quan.quanos.TableDynamic;
import ar.com.quan.quanos.WebService;

public class fragmento_cargaTitulares extends Fragment  implements View.OnClickListener{
    private Dialog dialogo, dialogoNac, dialogoEstadoCivil, dialogoplanesPrestacionales;
    private Context contexto;
    FragmentChangeTrigger trigger;
    String idUsuario, token;
    Button btnVolverAInicial;
    ImageButton btnGuardaTelefono;
    EditText fecnac, telefono;
    Spinner sexos,nacionalidades, estadosCiviles, planesPrestacionales;
    ArrayList<String> listaSexos= new ArrayList<String>(),listaNacionalidades= new ArrayList<String>()
            ,listaEstadosCiviles= new ArrayList<String>(),listaPlanesPrestacionales= new ArrayList<String>();
    ArrayAdapter<String> adapterSexos,adapterNacionalidades, adapterEstadosCiviles
            ,adapterplanesPrestacionales;
    Map <String, String> mapSexos= new HashMap<>(), mapNacionalidades= new HashMap<>(),
            mapEstadosCiviles= new HashMap<>(), mapPlanesPrestacionales= new HashMap<>();
    TableLayout tablaTelefono ;
    String [] encabezado = {"Teléfonos "} ;
    ArrayList<String[]> filas = new ArrayList<String[]>();
    TableDynamic tableDynamic =new TableDynamic(tablaTelefono,getContext());

    public fragmento_cargaTitulares() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmento_carga_titulares, container, false);

    }
    private ArrayList<String[]> buscaDatosTablas()
    {
        filas.add(new String[]{"uno"});
        filas.add(new String[]{"dos"});
        filas.add(new String[]{"tres"});
        filas.add(new String[]{"cuatro"});
        filas.add(new String[]{"5"});
        filas.add(new String[]{"6"});
        filas.add(new String[]{"7"});
        return filas;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contexto=view.getContext();
        btnVolverAInicial =(Button) view.findViewById(R.id.btnVolverAInicial);
        btnVolverAInicial.setOnClickListener(this);
        btnGuardaTelefono= (ImageButton) view.findViewById(R.id.btnGuardaTelefono);
        btnGuardaTelefono.setOnClickListener(this);

        fecnac= (EditText) view.findViewById(R.id.fecNac);
        fecnac.setOnClickListener(this);

        sexos = (Spinner) view.findViewById(R.id.sexos);
        nacionalidades=(Spinner) view.findViewById(R.id.nacionalidades);
        estadosCiviles=(Spinner) view.findViewById(R.id.estadosCiviles);
        planesPrestacionales=(Spinner) view.findViewById(R.id.planesPrestacionales);

        telefono= (EditText) view.findViewById(R.id.telefono);
        tablaTelefono =view.findViewById(R.id.tablaTelefono);
        tableDynamic =new TableDynamic(tablaTelefono,contexto);

        creaTabs ();
        llenaSexos();
        llenaNacionalidades();
        llenaEstadoCiviles();
        llenaPlanesPrestacionales();
        iniciaTablas();


    }
private void iniciaTablas(){

    //telefono=view.findViewById(R.id.telefono);

    tableDynamic.addHeader(encabezado);
    tableDynamic.addData(buscaDatosTablas());
    tableDynamic.backgroundHeader(Color.parseColor("#819FF7"));
    tableDynamic.backgroundData(Color.parseColor("#95cbf5"), Color.parseColor("#68879e"));
    tableDynamic.lineColor(Color.BLACK);
    tableDynamic.textColorData(Color.WHITE);
    tableDynamic.textColorHeader(Color.BLUE);

}
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnVolverAInicial){
            trigger.fireChange("cargaTitulares_btnVolverAInicial");
        }
        if(v.getId()==R.id.fecNac){
            showDatePickerDialog();
        }
        if(v.getId()==R.id.sexos){

        }
        if(v.getId()==R.id.btnGuardaTelefono){
            agregaTelefono();
        }
    }
    private void agregaTelefono(){
        String[]item = new String[]{telefono.getText().toString()};
        if (telefono.getText().toString().equals(""))
        {
            Toast.makeText(contexto, "Debe ingresart un número de teléfono", Toast.LENGTH_LONG).show();
        }
        else
        {
            tableDynamic.addItems(item);
            telefono.setText("");
        }

    }
    private void creaTabs (){
        TabHost tabs = (TabHost) getActivity().findViewById(R.id.tabHost);
        tabs.setup();
        TabHost.TabSpec spec = tabs.newTabSpec("tabs");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Datos personales");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Teléfonos");
        tabs.addTab(spec);
    }

    //LLenado de spinners
    public void llenaSexos(){
        dialogo = Dialogos.dlgBuscando(getActivity(),"Recuperando sexos");
        WebService.leerSexos(getActivity(),"true"
                , new SuccessResponseHandler<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject resultado) {
                        dialogo.dismiss();
                        try {
                            JSONObject completo =new JSONObject(resultado.getString("resultado"));
                            String datos =completo.getString("datos");
                            JSONArray arrayCompleto = new JSONArray(datos);
                            listaSexos.add(0, "Seleccione Sexo");
                            mapSexos.put("00000000-0000-0000-0000-000000000000","Seleccione Sexo");
                            for (int i = 0; i < arrayCompleto.length(); i++) {
                                JSONObject arrayFila = arrayCompleto.getJSONObject(i);
                                String id = arrayFila.getString("idSexo");
                                String nombre = arrayFila.getString("nombre");
                                listaSexos.add(i+1,nombre);
                                mapSexos.put(id,nombre);
                            }
                            adapterSexos = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaSexos);
                            adapterSexos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sexos.setAdapter(adapterSexos);
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
    public void llenaNacionalidades(){
        dialogoNac = Dialogos.dlgBuscando(getActivity(),"Recuperando nacionalidades");
        WebService.leerNacionalidades(getActivity(),"true"
                , new SuccessResponseHandler<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject resultado) {
                        dialogoNac.dismiss();
                        try {
                            JSONObject completo =new JSONObject(resultado.getString("resultado"));
                            String datos =completo.getString("datos");
                            JSONArray arrayCompleto = new JSONArray(datos);
                            listaNacionalidades.add(0, "Seleccione nacionalidad");
                            mapNacionalidades.put("00000000-0000-0000-0000-000000000000","Seleccione nacionalidad");
                            for (int i = 0; i < arrayCompleto.length(); i++) {
                                JSONObject arrayFila = arrayCompleto.getJSONObject(i);
                                String id = arrayFila.getString("idNacionalidad");
                                String nombre = arrayFila.getString("nombre");
                                listaNacionalidades.add(i+1,nombre);
                                mapNacionalidades.put(id,nombre);
                            }
                            adapterNacionalidades = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaNacionalidades);
                            adapterNacionalidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            nacionalidades.setAdapter(adapterNacionalidades);
                            dialogoNac.dismiss();
                            //String a = getKey(nacionalidades,"Argentina");

                        } catch (Exception errEx) {
                            dialogoNac = Dialogos.dlgError(getActivity(),errEx.getMessage());
                        }
                    }
                }, new ErrorResponseHandler() {
                    @Override
                    public void onError(String msg) {
                        dialogoNac.dismiss();
                        dialogoNac = Dialogos.dlgError(getActivity(),msg);
                    }
                });
    }
    public void llenaEstadoCiviles(){
        dialogoEstadoCivil = Dialogos.dlgBuscando(getActivity(),"Recuperando estados civiles");
        WebService.leerEstadosCiviles(getActivity(), "true"
                , new SuccessResponseHandler<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject resultado) {
                        dialogoNac.dismiss();
                        try {
                            JSONObject completo =new JSONObject(resultado.getString("resultado"));
                            String datos =completo.getString("datos");
                            JSONArray arrayCompleto = new JSONArray(datos);
                            listaEstadosCiviles.add(0, "Seleccione estado civil");
                            mapEstadosCiviles.put("00000000-0000-0000-0000-000000000000","Seleccione estado civil");
                            for (int i = 0; i < arrayCompleto.length(); i++) {
                                JSONObject arrayFila = arrayCompleto.getJSONObject(i);
                                String id = arrayFila.getString("idEstadoCivil");
                                String nombre = arrayFila.getString("nombre");
                                listaEstadosCiviles.add(i+1,nombre);
                                mapEstadosCiviles.put(id,nombre);
                            }
                            adapterEstadosCiviles = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaEstadosCiviles);
                            adapterEstadosCiviles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            estadosCiviles.setAdapter(adapterEstadosCiviles);
                            dialogoEstadoCivil.dismiss();
                            //String a = getKey(nacionalidades,"Argentina");

                        } catch (Exception errEx) {
                            dialogoEstadoCivil = Dialogos.dlgError(getActivity(),errEx.getMessage());
                        }
                    }
                }, new ErrorResponseHandler() {
                    @Override
                    public void onError(String msg) {
                        dialogoEstadoCivil.dismiss();
                        dialogoEstadoCivil = Dialogos.dlgError(getActivity(),msg);
                    }
                });
    }
    public void llenaPlanesPrestacionales(){
        dialogoplanesPrestacionales = Dialogos.dlgBuscando(getActivity(),"Recuperando planes prestacionales");
        WebService.leerPlanesPrestacionales(getActivity(),"true"
                , new SuccessResponseHandler<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject resultado) {
                        dialogoplanesPrestacionales.dismiss();
                        try {
                            JSONObject completo =new JSONObject(resultado.getString("resultado"));
                            String datos =completo.getString("datos");
                            JSONArray arrayCompleto = new JSONArray(datos);
                            listaPlanesPrestacionales.add(0, "Seleccione plan prestacional");
                            mapPlanesPrestacionales.put("00000000-0000-0000-0000-000000000000","Seleccione plan prestacional");
                            for (int i = 0; i < arrayCompleto.length(); i++) {
                                JSONObject arrayFila = arrayCompleto.getJSONObject(i);
                                String id = arrayFila.getString("idPlanPrestacional");
                                String nombre = arrayFila.getString("nombre");
                                listaPlanesPrestacionales.add(i+1,nombre);
                                mapPlanesPrestacionales.put(id,nombre);
                            }
                            adapterplanesPrestacionales = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1
                                    , listaPlanesPrestacionales);
                            adapterplanesPrestacionales.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            planesPrestacionales.setAdapter(adapterplanesPrestacionales);
                            dialogoplanesPrestacionales.dismiss();
                            //String a = getKey(nacionalidades,"Argentina");

                        } catch (Exception errEx) {
                            dialogoplanesPrestacionales = Dialogos.dlgError(getActivity(),errEx.getMessage());
                        }
                    }
                }, new ErrorResponseHandler() {
                    @Override
                    public void onError(String msg) {
                        dialogoplanesPrestacionales.dismiss();
                        dialogoplanesPrestacionales = Dialogos.dlgError(getActivity(),msg);
                    }
                });
    }
    //Fin llenado spinners

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
    //Selector de fechas
    private void showDatePickerDialog() {
        DatePickerFragment newFragment =
                DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                fecnac.setText(selectedDate);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }
    //Fin selector de fechas
    public void setTrigger(FragmentChangeTrigger trigger) {
        this.trigger = trigger;
    }
}