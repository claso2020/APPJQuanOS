package ar.com.quan.quanos.Fragmentos;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import android.widget.TableLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ar.com.quan.quanos.Fabrica.Dialogos;
import ar.com.quan.quanos.Interfaces.ErrorResponseHandler;
import ar.com.quan.quanos.Interfaces.FragmentChangeTrigger;
import ar.com.quan.quanos.Interfaces.SuccessResponseHandler;
import ar.com.quan.quanos.R;
import ar.com.quan.quanos.TableDynamic;
import ar.com.quan.quanos.WebService;

public class fragmento_cargaTitulares extends Fragment  implements View.OnClickListener{
    private Dialog dialogo, dialogoNac, dialogoEstadoCivil, dialogoplanesPrestacionales
            , dialogoProvincias, dialogoDepartamentos;
    private Context contexto;
    FragmentChangeTrigger trigger;
    String idUsuario, token, idProvinciaSeleccionada;
    Button btnVolverAInicial;

    EditText fecnac, telefono, direccion, mail;
    Spinner sexos,nacionalidades, estadosCiviles, planesPrestacionales, provincias, departamentos;
    ArrayList<String> listaSexos= new ArrayList<String>(),listaNacionalidades= new ArrayList<String>()
            ,listaEstadosCiviles= new ArrayList<String>(),listaPlanesPrestacionales= new ArrayList<String>()
            ,listaProvincias= new ArrayList<String>(),listaDepartamentos= new ArrayList<String>();
    ArrayAdapter<String> adapterSexos,adapterNacionalidades, adapterEstadosCiviles
            ,adapterplanesPrestacionales,adapterProvincias,adapterDepartamentos;
    Map <String, String> mapSexos= new HashMap<>(), mapNacionalidades= new HashMap<>(),
            mapEstadosCiviles= new HashMap<>(), mapPlanesPrestacionales= new HashMap<>()
            , mapProvincias= new HashMap<>(), mapDepartamentos= new HashMap<>();
    ImageButton btnGuardaTelefono, btnGuardaDireccion, btnGuardaMail;
    TableLayout tablaTelefono, tablaDireccion, tablaMail;
    String [] encabezado = {"Teléfonos "}, encabezadoDireccion={"Dirección"}, encabezadoMail={"Mail"}  ;
    ArrayList<String[]> filasTelefono = new ArrayList<String[]>();
    ArrayList<String[]> filasDomicilio = new ArrayList<String[]>();
    ArrayList<String[]> filasMails = new ArrayList<String[]>();
    TableDynamic tablaDinamicaTelefono;//=new TableDynamic(tablaTelefono,getContext());
    TableDynamic tablaDinamicaDomicilio;// =new TableDynamic(tablaDireccion,getContext());
    TableDynamic tablaDinamicaMail;// =new TableDynamic(tablaMail,getContext());

    public fragmento_cargaTitulares() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmento_carga_titulares, container, false);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contexto=view.getContext();
        btnVolverAInicial =(Button) view.findViewById(R.id.btnVolverAInicial);
        btnVolverAInicial.setOnClickListener(this);
        btnGuardaTelefono= (ImageButton) view.findViewById(R.id.btnGuardaTelefono);
        btnGuardaTelefono.setOnClickListener(this);
        btnGuardaDireccion= (ImageButton) view.findViewById(R.id.btnGuardaDireccion);
        btnGuardaDireccion.setOnClickListener(this);
        btnGuardaMail= (ImageButton) view.findViewById(R.id.btnGuardaMail);
        btnGuardaMail.setOnClickListener(this);

        fecnac= (EditText) view.findViewById(R.id.fecNac);
        fecnac.setOnClickListener(this);

        sexos = (Spinner) view.findViewById(R.id.sexos);
        nacionalidades=(Spinner) view.findViewById(R.id.nacionalidades);
        estadosCiviles=(Spinner) view.findViewById(R.id.estadosCiviles);
        planesPrestacionales=(Spinner) view.findViewById(R.id.planesPrestacionales);
        provincias=(Spinner) view.findViewById(R.id.provincias);
        departamentos=(Spinner) view.findViewById(R.id.departamentos);
        provincias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
                if(i!=0)
                {
                    String sel = listaProvincias.get(i).toString();
                    idProvinciaSeleccionada= getKey(mapProvincias,sel);
                    llenaDepartamentos(idProvinciaSeleccionada);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(contexto,"No selection",Toast.LENGTH_LONG).show();
            }
        });

        telefono= (EditText) view.findViewById(R.id.telefono);
        tablaTelefono =view.findViewById(R.id.tablaTelefono);
        tablaDinamicaTelefono =new TableDynamic(tablaTelefono,contexto);

        direccion=(EditText) view.findViewById(R.id.direccion);
        tablaDireccion =view.findViewById(R.id.tablaDireccion);
        tablaDinamicaDomicilio =new TableDynamic(tablaDireccion,contexto);

        mail=(EditText) view.findViewById(R.id.mail);
        tablaMail =view.findViewById(R.id.tablaMail);
        tablaDinamicaMail =new TableDynamic(tablaMail,contexto);


        creaTabs ();
        llenaSexos();
        llenaNacionalidades();
        llenaEstadoCiviles();
        llenaPlanesPrestacionales();
        llenaProvincias();

        iniciaTablas();


    }
/*    private ArrayList<String[]> buscaDatosTablas()
    {
        return filas;
    }*/
    private void iniciaTablas(){

    //telefono=view.findViewById(R.id.telefono);

    tablaDinamicaTelefono.addHeader(encabezado);
    tablaDinamicaTelefono.addData(filasTelefono); //le paso filas vacia sino ir a buscaDatosTablas()
    tablaDinamicaTelefono.backgroundHeader(Color.parseColor("#819FF7"));
    tablaDinamicaTelefono.backgroundData(Color.parseColor("#95cbf5"), Color.parseColor("#68879e"));
    tablaDinamicaTelefono.lineColor(Color.BLACK);
    tablaDinamicaTelefono.textColorData(Color.WHITE);


    tablaDinamicaDomicilio.addHeader(encabezadoDireccion);
    tablaDinamicaDomicilio.addData(filasDomicilio);
    tablaDinamicaDomicilio.backgroundHeader(Color.parseColor("#819FF7"));
    tablaDinamicaDomicilio.backgroundData(Color.parseColor("#95cbf5"), Color.parseColor("#68879e"));
    tablaDinamicaDomicilio.lineColor(Color.BLACK);
    tablaDinamicaDomicilio.textColorData(Color.WHITE);
    tablaDinamicaDomicilio.textColorHeader(Color.BLUE);


    tablaDinamicaMail.addHeader(encabezadoMail);
    tablaDinamicaMail.addData(filasMails);
    tablaDinamicaMail.backgroundHeader(Color.parseColor("#819FF7"));
    tablaDinamicaMail.backgroundData(Color.parseColor("#95cbf5"), Color.parseColor("#68879e"));
    tablaDinamicaMail.lineColor(Color.BLACK);
    tablaDinamicaMail.textColorData(Color.WHITE);
    tablaDinamicaMail.textColorHeader(Color.BLUE);

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
        if(v.getId()==R.id.btnGuardaDireccion){
            agregaDireccion();
        }
        if(v.getId()==R.id.btnGuardaMail){
            agregaMail();
        }

    }
    private void agregaMail(){
        String[]itemMail = new String[]{mail.getText().toString()};
        if (mail.getText().toString().equals(""))
        {
            Toast.makeText(contexto, "Debe ingresar un mail", Toast.LENGTH_LONG).show();
        }
        else
        {
            tablaDinamicaMail.addItems(itemMail);
            mail.setText("");
        }
    }
    private void agregaDireccion(){
        String[]itemDirecion = new String[]{direccion.getText().toString()};
        if (direccion.getText().toString().equals(""))
        {
            Toast.makeText(contexto, "Debe ingresar una dirección", Toast.LENGTH_LONG).show();
        }
        else
        {
            tablaDinamicaDomicilio.addItems(itemDirecion);
            direccion.setText("");
        }

    }
    private void agregaTelefono(){
        String[]item = new String[]{telefono.getText().toString()};
        if (telefono.getText().toString().equals(""))
        {
            Toast.makeText(contexto, "Debe ingresar un número de teléfono", Toast.LENGTH_LONG).show();
        }
        else
        {
            tablaDinamicaTelefono.addItems(item);
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

        spec = tabs.newTabSpec("tag3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Direción");
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag4");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Mail");
        tabs.addTab(spec);
    }

    //LLenado de spinners
    public void llenaDepartamentos(String idProvinciaSeleccionada ){
        dialogoDepartamentos = Dialogos.dlgBuscando(getActivity(),"Recuperando Departamentos");
        WebService.leerDepartamentos(getActivity(), idProvinciaSeleccionada
                , new SuccessResponseHandler<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject resultado) {
                        dialogoProvincias.dismiss();
                        try {
                            JSONObject completo =new JSONObject(resultado.getString("resultado"));
                            String datos =completo.getString("departamentos");
                            JSONArray arrayCompleto = new JSONArray(datos);
                            listaDepartamentos.add(0, "Seleccione Departamento");
                            mapDepartamentos.put("00000000-0000-0000-0000-000000000000","Seleccione Departamento");
                            for (int i = 0; i < arrayCompleto.length(); i++) {
                                JSONObject arrayFila = arrayCompleto.getJSONObject(i);
                                String id = arrayFila.getString("idDepartamento");
                                String nombre = arrayFila.getString("nombreDepartamento");
                                listaDepartamentos.add(i+1,nombre);
                                mapDepartamentos.put(id,nombre);
                            }
                            adapterDepartamentos = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaDepartamentos);
                            adapterDepartamentos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            departamentos.setAdapter(adapterDepartamentos);
                            dialogoDepartamentos.dismiss();
                            //String a = getKey(mapSexos,"Femenino");

                        } catch (Exception errEx) {
                            dialogoDepartamentos = Dialogos.dlgError(getActivity(),errEx.getMessage());
                        }
                    }
                }, new ErrorResponseHandler() {
                    @Override
                    public void onError(String msg) {
                        dialogoDepartamentos.dismiss();
                        dialogoDepartamentos = Dialogos.dlgError(getActivity(),msg);
                    }
                });
    }
    public void llenaProvincias(){
        dialogoProvincias = Dialogos.dlgBuscando(getActivity(),"Recuperando Provincias");
        WebService.leerProvincias(getActivity()
                , new SuccessResponseHandler<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject resultado) {
                        dialogoProvincias.dismiss();
                        try {
                            JSONObject completo =new JSONObject(resultado.getString("resultado"));
                            String datos =completo.getString("provincias");
                            JSONArray arrayCompleto = new JSONArray(datos);
                            listaProvincias.add(0, "Seleccione Provincia");
                            mapProvincias.put("00000000-0000-0000-0000-000000000000","Seleccione Provincia");
                            for (int i = 0; i < arrayCompleto.length(); i++) {
                                JSONObject arrayFila = arrayCompleto.getJSONObject(i);
                                String id = arrayFila.getString("idProvincia");
                                String nombre = arrayFila.getString("nombreProvincia");
                                listaProvincias.add(i+1,nombre);
                                mapProvincias.put(id,nombre);
                            }
                            adapterProvincias = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaProvincias);
                            adapterProvincias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            provincias.setAdapter(adapterProvincias);
                            dialogoProvincias.dismiss();
                            //String a = getKey(mapSexos,"Femenino");

                        } catch (Exception errEx) {
                            dialogoProvincias = Dialogos.dlgError(getActivity(),errEx.getMessage());
                        }
                    }
                }, new ErrorResponseHandler() {
                    @Override
                    public void onError(String msg) {
                        dialogoProvincias.dismiss();
                        dialogoProvincias = Dialogos.dlgError(getActivity(),msg);
                    }
                });
    }
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