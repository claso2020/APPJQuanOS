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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ar.com.quan.quanos.Fabrica.Dialogos;
import ar.com.quan.quanos.Interfaces.ErrorResponseHandler;
import ar.com.quan.quanos.Interfaces.FragmentChangeTrigger;
import ar.com.quan.quanos.Interfaces.SuccessResponseHandler;
import ar.com.quan.quanos.Principal;
import ar.com.quan.quanos.R;
import ar.com.quan.quanos.TableDynamic;
import ar.com.quan.quanos.WebService;



public class fragmento_cargaTitulares extends Fragment  implements View.OnClickListener{
    //region Declaraciones
    private Dialog dialogo, dialogoNac, dialogoEstadoCivil, dialogoplanesPrestacionales
            , dialogoProvincias, dialogoDepartamentos, dialogoLocalidades, dialogoParentesco;
    private Context contexto;
    FragmentChangeTrigger trigger;
    String idUsuario, token, idProvinciaSeleccionada, idDepartamentoSeleccionado;

    EditText apellido, nombre, cuil, fecnac,claveFiscal,cantGrupoFamiliar, telefono, comentarioTelefono,
            direccion, comentarioDireccion, mail, comentarioMail, fecnacFam, apellidoFam, nombreFam, cuilFam
            ,razonSocial, cuit, fecIngreso, aporteOS, sac;

    Spinner sexos,nacionalidades, estadosCiviles, planesPrestacionales,
            provincias, departamentos, localidades, sexosFam, nacionalidadesFam, estadosCivilesFam
            ,parentescos;

    ArrayList<String> listaSexos= new ArrayList<String>(),listaNacionalidades= new ArrayList<String>()
            ,listaEstadosCiviles= new ArrayList<String>(),listaPlanesPrestacionales= new ArrayList<String>()
            ,listaProvincias= new ArrayList<String>(),listaDepartamentos= new ArrayList<String>()
            ,listaLocalidades= new ArrayList<String>(),listaSexosFam= new ArrayList<String>()
            ,listaNacionalidadesFam= new ArrayList<String>(),listaEstadosCivilesFam= new ArrayList<String>()
            ,listaparentesco= new ArrayList<String>();

    ArrayAdapter<String> adapterSexos,adapterNacionalidades, adapterEstadosCiviles
            ,adapterplanesPrestacionales,adapterProvincias,adapterDepartamentos
            ,adapterLocalidades, adapterSexosFam, adapterNacionalidadesFam
            ,adapterEstadosCivilesFam ,adapterParentesco;

    Map <String, String> mapSexos= new HashMap<>(), mapNacionalidades= new HashMap<>()
            ,mapEstadosCiviles= new HashMap<>(), mapPlanesPrestacionales= new HashMap<>()
            , mapProvincias= new HashMap<>(), mapDepartamentos= new HashMap<>()
            , mapLocalidades= new HashMap<>(), mapSexosFam= new HashMap<>()
            , mapNacionalidadesFam= new HashMap<>(),mapEstadosCivilesFam= new HashMap<>()
            ,mapParentesco= new HashMap<>();

    ImageButton btnVolverAInicial,btnGuardaTelefono, btnGuardaDireccion, btnGuardaMail,
            btnGuardaFamiliares, btnGuardaRelLab, btnGuardaTitular ;
    TableLayout tablaTelefono, tablaDireccion, tablaMail, tablaFamiliares, tablaRelLab;

    String [] encabezado = {"Teléfonos ","Comentario","Acción"}, encabezadoDireccion={"Dirección", "Localidad","Comentario"}
            , encabezadoMail={"Mail", "Comentario"}
            , encabezadoFamiliares={"Apellido", "nombre","Fecha Nac.","CUIL","Sexo","Nacionalidad","EstadoCivil", "Parentesco"}
            ,encabezadoRelLab={"Razón Social", "Cuit", "Fecha ingreso", "Aporte OS", "SAC"};

    ArrayList<String[]> filasTelefono = new ArrayList<String[]>();
    ArrayList<String[]> filasDomicilio = new ArrayList<String[]>();
    ArrayList<String[]> filasMails = new ArrayList<String[]>();
    ArrayList<String[]> filasFamiliares= new ArrayList<String[]>();
    ArrayList<String[]> filasRellab= new ArrayList<String[]>();

    TableDynamic tablaDinamicaTelefono;//=new TableDynamic(tablaTelefono,getContext());
    TableDynamic tablaDinamicaDomicilio;// =new TableDynamic(tablaDireccion,getContext());
    TableDynamic tablaDinamicaMail;// =new TableDynamic(tablaMail,getContext());
    TableDynamic tablaDinamicaFamiliares;// =new TableDynamic(tablaMail,getContext());
    TableDynamic tablaDinamicaRelLab;// =new TableDynamic(tablaMail,getContext());
    //endregion

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
        //region declaraciones
        btnVolverAInicial =(ImageButton) view.findViewById(R.id.btnVolverAInicial);
        btnVolverAInicial.setOnClickListener(this);
        btnGuardaTelefono= (ImageButton) view.findViewById(R.id.btnGuardaTelefono);
        btnGuardaTelefono.setOnClickListener(this);
        btnGuardaDireccion= (ImageButton) view.findViewById(R.id.btnGuardaDireccion);
        btnGuardaDireccion.setOnClickListener(this);
        btnGuardaMail= (ImageButton) view.findViewById(R.id.btnGuardaMail);
        btnGuardaMail.setOnClickListener(this);
        btnGuardaFamiliares=(ImageButton) view.findViewById(R.id.btnGuardaFamiliares);
        btnGuardaFamiliares.setOnClickListener(this);
        btnGuardaRelLab=(ImageButton) view.findViewById(R.id.btnGuardaRelLab);
        btnGuardaRelLab.setOnClickListener(this);
        btnGuardaTitular =(ImageButton) view.findViewById(R.id.btnGuardaTitular);
        btnGuardaTitular.setOnClickListener(this);

        apellido= (EditText)  view.findViewById(R.id.apellido);
        nombre=(EditText) view.findViewById(R.id.nombre);
        cuil=(EditText) view.findViewById(R.id.cuil);
        claveFiscal=(EditText) view.findViewById(R.id.claveFiscal);
        cantGrupoFamiliar=(EditText) view.findViewById(R.id.cantGrupoFamiliar);
        telefono= (EditText) view.findViewById(R.id.telefono);
        comentarioTelefono=(EditText)  view.findViewById(R.id.comentarioTelefono);
        direccion=(EditText) view.findViewById(R.id.direccion);
        comentarioDireccion=(EditText) view.findViewById(R.id.comentarioDireccion);
        mail=(EditText) view.findViewById(R.id.mail);
        comentarioMail=(EditText) view.findViewById(R.id.comentarioMail);
        apellidoFam=(EditText) view.findViewById(R.id.apellidoFam);
        nombreFam=(EditText) view.findViewById(R.id.nombreFam);
        cuilFam=(EditText) view.findViewById(R.id.cuilFam);
        fecnac= (EditText) view.findViewById(R.id.fecNac);
        fecnacFam= (EditText) view.findViewById(R.id.fecNacFam);
        fecIngreso= (EditText) view.findViewById(R.id.fecIngreso);
        razonSocial= (EditText) view.findViewById(R.id.razonSocial);
        cuit= (EditText) view.findViewById(R.id.cuit);
        aporteOS=(EditText) view.findViewById(R.id.aporteOS);
        sac=(EditText) view.findViewById(R.id.sac);

        fecnacFam.setOnClickListener(this);
        fecnac.setOnClickListener(this);
        fecIngreso.setOnClickListener(this);

        sexos = (Spinner) view.findViewById(R.id.sexos);
        nacionalidades=(Spinner) view.findViewById(R.id.nacionalidades);
        estadosCiviles=(Spinner) view.findViewById(R.id.estadosCiviles);
        planesPrestacionales=(Spinner) view.findViewById(R.id.planesPrestacionales);
        provincias=(Spinner) view.findViewById(R.id.provincias);
        departamentos=(Spinner) view.findViewById(R.id.departamentos);
        localidades=(Spinner) view.findViewById(R.id.localidades);
        sexosFam=(Spinner) view.findViewById(R.id.sexosFam);
        nacionalidadesFam=(Spinner) view.findViewById(R.id.nacionalidadesFam);
        estadosCivilesFam=(Spinner) view.findViewById(R.id.estadosCivilesFam);
        parentescos=(Spinner) view.findViewById(R.id.parentescos);

        tablaTelefono =view.findViewById(R.id.tablaTelefono);
        tablaDinamicaTelefono =new TableDynamic(tablaTelefono,contexto);
        tablaDireccion =view.findViewById(R.id.tablaDireccion);
        tablaDinamicaDomicilio =new TableDynamic(tablaDireccion,contexto);
        tablaMail =view.findViewById(R.id.tablaMail);
        tablaDinamicaMail =new TableDynamic(tablaMail,contexto);
        tablaFamiliares =view.findViewById(R.id.tablaFamiliares);
        tablaDinamicaFamiliares =new TableDynamic(tablaFamiliares,contexto);
        tablaRelLab =view.findViewById(R.id.tablaRelLab);
        tablaDinamicaRelLab =new TableDynamic(tablaRelLab,contexto);

        //endregion

        departamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View v, int i, long l) {
                if(i!=0)
                {
                    String sel = listaDepartamentos.get(i).toString();
                    idDepartamentoSeleccionado= getKey(mapDepartamentos,sel);
                    llenaLocalidades(idDepartamentoSeleccionado);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(contexto,"No selection",Toast.LENGTH_LONG).show();
            }
        });
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

        creaTabs ();
        //region Llena spinners
        llenaSexos();
        llenaNacionalidades();
        llenaEstadoCiviles();
        llenaPlanesPrestacionales();
        llenaProvincias();
        llenaParentescos();
        //endregion
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

        tablaDinamicaFamiliares.addHeader(encabezadoFamiliares);
        tablaDinamicaFamiliares.addData(filasFamiliares);
        tablaDinamicaFamiliares.backgroundHeader(Color.parseColor("#819FF7"));
        tablaDinamicaFamiliares.backgroundData(Color.parseColor("#95cbf5"), Color.parseColor("#68879e"));
        tablaDinamicaFamiliares.lineColor(Color.BLACK);
        tablaDinamicaFamiliares.textColorData(Color.WHITE);
        tablaDinamicaFamiliares.textColorHeader(Color.BLUE);

        tablaDinamicaRelLab.addHeader(encabezadoRelLab);
        tablaDinamicaRelLab.addData(filasRellab);
        tablaDinamicaRelLab.backgroundHeader(Color.parseColor("#819FF7"));
        tablaDinamicaRelLab.backgroundData(Color.parseColor("#95cbf5"), Color.parseColor("#68879e"));
        tablaDinamicaRelLab.lineColor(Color.BLACK);
        tablaDinamicaRelLab.textColorData(Color.WHITE);
        tablaDinamicaRelLab.textColorHeader(Color.BLUE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnVolverAInicial){
            trigger.fireChange("cargaTitulares_btnVolverAInicial");
        }
        if(v.getId()==R.id.fecNac){
            showDatePickerDialog(fecnac);
        }
        if(v.getId()==R.id.fecNacFam){
            showDatePickerDialog(fecnacFam);
        }
        if(v.getId()==R.id.fecIngreso){
            showDatePickerDialog(fecIngreso);
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
        if(v.getId()==R.id.btnGuardaFamiliares){
            agregaFamiliares();
        }
        if(v.getId()==R.id.btnGuardaRelLab){
            agregaRelacionLaboral();
        }
        if(v.getId()==R.id.btnGuardaTitular){
            agregaTitulares();
        }
    }

    private void agregaTitulares() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        String fechaCarga = dateFormat.format(date);
        String idsexoSeleccionado = getKey(mapSexos,sexos.getSelectedItem().toString());
        String idNacionalidadSeleccionado = getKey(mapNacionalidades,nacionalidades.getSelectedItem().toString());
        String idEstCivilSeleccionado = getKey(mapEstadosCiviles,estadosCiviles.getSelectedItem().toString());
        String idPlanPrestacionalSeleccionado = getKey(mapPlanesPrestacionales,planesPrestacionales.getSelectedItem().toString());

        //region datos personales

        String[] datos;
        datos= new String[]{apellido.getText().toString(), nombre.getText().toString(),
                fecnac.getText().toString(), cuil.getText().toString(), idsexoSeleccionado,
                idEstCivilSeleccionado, claveFiscal.getText().toString(),
                cantGrupoFamiliar.getText().toString(), idPlanPrestacionalSeleccionado };

        JSONArray dtContactosEmail = new JSONArray();
        try {

            for (int i = 0; i < filasMails.size(); i++) {
                JSONObject jGroup = new JSONObject();
                jGroup.put("idmail", "00000000-0000-0000-0000-000000000000");
                jGroup.put("fechaCarga", fechaCarga);
                jGroup.put("mail", filasMails.get(i)[0]);
                jGroup.put("comentario", filasMails.get(i)[1]);

                dtContactosEmail.put(jGroup);
            }
        // --Este no haría falta ----- jResult.put("dtContactosEmail", jArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //endregion

        //region Datos teléfono
        JSONArray dtContactosTelefonos = new JSONArray();
        try {

            for (int i = 0; i < filasTelefono.size(); i++) {
                JSONObject jGroupTel = new JSONObject();
                jGroupTel.put("idTelefono", "00000000-0000-0000-0000-000000000000");
                jGroupTel.put("fechaCarga", fechaCarga);
                jGroupTel.put("telefono", filasTelefono.get(i)[0]);
                jGroupTel.put("comentario",  filasTelefono.get(i)[1]);

                dtContactosTelefonos.put(jGroupTel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //endregion

        //region Domilicios
        JSONArray dtContactosDomicilio = new JSONArray();
        try {

            for (int i = 0; i < filasDomicilio.size(); i++) {
                JSONObject jGroupDom = new JSONObject();
                jGroupDom.put("idDomicilio", "00000000-0000-0000-0000-000000000000");
                jGroupDom.put("fechaCarga", fechaCarga);
                jGroupDom.put("domicilio", filasDomicilio.get(i)[0]);
                jGroupDom.put("comentario", filasDomicilio.get(i)[2]);
                jGroupDom.put("idLocalidad", getKey(mapLocalidades,filasDomicilio.get(i)[1]));

                dtContactosDomicilio.put(jGroupDom);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //endregion
        //region Grupo familiar
        JSONArray dtFamiliares = new JSONArray();
        try {
            for (int i = 0; i < filasFamiliares.size(); i++) {
                JSONObject jGroupFam = new JSONObject();
                jGroupFam.put("idFamiliar", "00000000-0000-0000-0000-000000000000");
                jGroupFam.put("apellido", filasFamiliares.get(i)[0]);
                jGroupFam.put("nombre",  filasFamiliares.get(i)[1]);
                jGroupFam.put("fecNac",  filasFamiliares.get(i)[2]);
                jGroupFam.put("cuil",  filasFamiliares.get(i)[3]);
                jGroupFam.put("idSexo", getKey(mapSexos,filasFamiliares.get(i)[4]));
                jGroupFam.put("idNacionalidad", getKey(mapNacionalidades,filasFamiliares.get(i)[5]));
                jGroupFam.put("idEstadoCivil", getKey(mapEstadosCiviles,filasFamiliares.get(i)[6]));
                jGroupFam.put("idParentesco", getKey(mapParentesco,filasFamiliares.get(i)[7]));
                jGroupFam.put("idEstadoAfiliacion", "00000000-0000-0000-0000-000000000000");
                jGroupFam.put("perAlta", "00000000-0000-0000-0000-000000000000");
                jGroupFam.put("perBaja", "00000000-0000-0000-0000-000000000000");

                dtFamiliares.put(jGroupFam);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //endregion

        //region Relación laboral
            JSONArray dtRelacionesLaborales = new JSONArray();
        try {

            for (int i = 0; i < filasRellab.size(); i++) {
                JSONObject jGroupRelLab = new JSONObject();
                jGroupRelLab.put("idEmpleador", "00000000-0000-0000-0000-000000000000");
                jGroupRelLab.put("idTipoRelacionLaboral", "00000000-0000-0000-0000-000000000000");
                jGroupRelLab.put("razonSocial",  filasRellab.get(i)[0]);
                jGroupRelLab.put("cuit",  filasRellab.get(i)[1]);
                jGroupRelLab.put("fecIngreso",  filasRellab.get(i)[2]);
                jGroupRelLab.put("aporteOs", filasRellab.get(i)[3]);
                jGroupRelLab.put("sac", filasRellab.get(i)[4]);

                dtRelacionesLaborales.put(jGroupRelLab);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //endregion

        WebService.modificarDatosTitulares(getActivity()
                ,"00000000-0000-0000-0000-000000000000"
                ,apellido.getText().toString()
                ,nombre.getText().toString()
                ,fecnac.getText().toString()
                ,cuil.getText().toString()
                ,idsexoSeleccionado
                ,idNacionalidadSeleccionado
                ,idEstCivilSeleccionado
                ,claveFiscal.getText().toString()
                ,cantGrupoFamiliar.getText().toString()
                ,idPlanPrestacionalSeleccionado
                ,"Comentario ingreso titular"
                ,dtContactosDomicilio.toString()
                ,dtContactosTelefonos.toString()
                ,dtContactosEmail.toString()
                ,dtFamiliares.toString()
                ,"REL" //"00000000-0000-0000-0000-000000000000"
                ,dtRelacionesLaborales.toString()
                ,"dtArchivosAdjuntos"
                ,"cuentaVerificacionCUILTitular"
                ,"noVerificarCuilTitular"
                ,"origenPeticion"
                ,""//"00000000-0000-0000-0000-000000000000"
                ,"perAlta"
                ,"perBaja"

                , new SuccessResponseHandler<JSONObject>()  {
                    @Override
                    public void onSuccess(JSONObject valores) {
                        dialogo.dismiss();
                        try {

                        } catch (Exception errEx) {
                            dialogo = Dialogos.dlgError(contexto,errEx.getMessage());
                        }
                    }
                }, new ErrorResponseHandler() {
                    @Override
                    public void onError(String msg) {
                        dialogo.dismiss();
                        dialogo = Dialogos.dlgError(contexto,msg);
                    }
                });

        String a ="";
    }
    //region agrega a tablas visibles
    private void agregaRelacionLaboral(){
        String[]itemRelLab = new String[]{razonSocial.getText().toString()
                ,cuit.getText().toString()
                ,fecIngreso.getText().toString()
                ,aporteOS.getText().toString()
                ,sac.getText().toString()
                };
        if (razonSocial.getText().toString().equals("") ||
                cuit.getText().toString().equals("") ||
                aporteOS.getText().toString().equals("") ||
                sac.getText().toString().equals("")
        )
        {
            Toast.makeText(contexto, "Debe ingresar todos los datos", Toast.LENGTH_LONG).show();
        }
        else
        {
            tablaDinamicaRelLab.addItems(itemRelLab);

        }

    }
    private void agregaMail(){
        String[]itemMail = new String[]{mail.getText().toString(), comentarioMail.getText().toString()};
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
    private void agregaFamiliares(){
        String[]itemFam = new String[]{apellidoFam.getText().toString()
                ,nombreFam.getText().toString()
                ,fecnacFam.getText().toString()
                ,cuilFam.getText().toString()
                ,sexosFam.getSelectedItem().toString()
                ,nacionalidadesFam.getSelectedItem().toString()
                ,estadosCivilesFam.getSelectedItem().toString()
                ,parentescos.getSelectedItem().toString()
        };
        if (apellidoFam.getText().toString().equals("") ||
                nombreFam.getText().toString().equals("") ||
                cuilFam.getText().toString().equals("") ||
                sexosFam.getSelectedItem().toString().equals("") ||
                nacionalidadesFam.getSelectedItem().toString().equals("") ||
                estadosCivilesFam.getSelectedItem().toString().equals("") ||
                parentescos.getSelectedItem().toString().equals("")
        )
        {
            Toast.makeText(contexto, "Debe ingresar todos los datos", Toast.LENGTH_LONG).show();
        }
        else
        {
            tablaDinamicaFamiliares.addItems(itemFam);
            direccion.setText("");
        }

    }
    private void agregaDireccion(){
        String[]itemDireccion = new String[]{direccion.getText().toString(), localidades.getSelectedItem().toString()
                , comentarioDireccion.getText().toString()};
        if (direccion.getText().toString().equals(""))
        {
            Toast.makeText(contexto, "Debe ingresar una dirección", Toast.LENGTH_LONG).show();
        }
        else
        {
            tablaDinamicaDomicilio.addItems(itemDireccion);
            direccion.setText("");
        }

    }
    private void agregaTelefono(){
        String[]item = new String[]{telefono.getText().toString(), comentarioTelefono.getText().toString(), "EliminarModificar"};
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
        spec.setIndicator("",getResources().getDrawable(R.drawable.datospersonalespeque));

        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Teléfonos");
        spec.setIndicator("",getResources().getDrawable(R.drawable.telefonopeque));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Direción");
        spec.setIndicator("",getResources().getDrawable(R.drawable.direpeque));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag4");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Mail");
        spec.setIndicator("",getResources().getDrawable(R.drawable.mailpeque));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag5");
        spec.setContent(R.id.tab5);
        spec.setIndicator("Familiares");
        spec.setIndicator("",getResources().getDrawable(R.drawable.fampeque));
        tabs.addTab(spec);

        spec = tabs.newTabSpec("tag6");
        spec.setContent(R.id.tab6);
        spec.setIndicator("Realción laboral");
        spec.setIndicator("",getResources().getDrawable(R.drawable.rellabpeque));
        tabs.addTab(spec);

    }
    //endregion
    //region LLenado de spinners
    public void llenaLocalidades(String idDepartamentoSeleccionado ){
        dialogoLocalidades = Dialogos.dlgBuscando(getActivity(),"Recuperando Localidades");
        WebService.leerLocalidades(getActivity(), idDepartamentoSeleccionado
                , new SuccessResponseHandler<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject resultado) {
                        dialogoLocalidades.dismiss();
                        try {
                            JSONObject completo =new JSONObject(resultado.getString("resultado"));
                            String datos =completo.getString("localidades");
                            JSONArray arrayCompleto = new JSONArray(datos);
                            listaLocalidades.add(0, "Seleccione Localidad");
                            mapLocalidades.put("00000000-0000-0000-0000-000000000000","Seleccione Localidad");
                            for (int i = 0; i < arrayCompleto.length(); i++) {
                                JSONObject arrayFila = arrayCompleto.getJSONObject(i);
                                String id = arrayFila.getString("idLocalidad");
                                String nombre = arrayFila.getString("nombreLocalidad");
                                listaLocalidades.add(i+1,nombre);
                                mapLocalidades.put(id,nombre);
                            }
                            adapterLocalidades = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaLocalidades);
                            adapterLocalidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            localidades.setAdapter(adapterLocalidades);
                            dialogoLocalidades.dismiss();
                            //String a = getKey(mapSexos,"Femenino");

                        } catch (Exception errEx) {
                            dialogoLocalidades = Dialogos.dlgError(getActivity(),errEx.getMessage());
                        }
                    }
                }, new ErrorResponseHandler() {
                    @Override
                    public void onError(String msg) {
                        dialogoLocalidades.dismiss();
                        dialogoLocalidades = Dialogos.dlgError(getActivity(),msg);
                    }
                });
    }
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
    public void llenaParentescos(){
        dialogoParentesco = Dialogos.dlgBuscando(getActivity(),"Recuperando sexos");
        WebService.leerParentescos(getActivity()
                , new SuccessResponseHandler<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject resultado) {
                        dialogoParentesco.dismiss();
                        try {
                            JSONObject completo =new JSONObject(resultado.getString("resultado"));
                            String datos =completo.getString("parentescos");
                            JSONArray arrayCompleto = new JSONArray(datos);
                            listaparentesco.add(0, "Seleccione Parentesco");
                            mapParentesco.put("00000000-0000-0000-0000-000000000000","Seleccione Parentesco");

                            for (int i = 0; i < arrayCompleto.length(); i++) {
                                JSONObject arrayFila = arrayCompleto.getJSONObject(i);
                                String id = arrayFila.getString("idParentesco");
                                String nombre = arrayFila.getString("nombreParentesco");

                                listaparentesco.add(i+1,nombre);
                                mapParentesco.put(id,nombre);
                            }
                            adapterParentesco = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaparentesco);
                            adapterParentesco.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            parentescos.setAdapter(adapterParentesco);

                            dialogoParentesco.dismiss();
                            //String a = getKey(mapSexos,"Femenino");

                        } catch (Exception errEx) {
                            dialogoParentesco = Dialogos.dlgError(getActivity(),errEx.getMessage());
                        }
                    }
                }, new ErrorResponseHandler() {
                    @Override
                    public void onError(String msg) {
                        dialogoParentesco.dismiss();
                        dialogoParentesco = Dialogos.dlgError(getActivity(),msg);
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
                            listaSexosFam.add(0, "Seleccione Sexo");
                            mapSexosFam.put("00000000-0000-0000-0000-000000000000","Seleccione Sexo");
                            for (int i = 0; i < arrayCompleto.length(); i++) {
                                JSONObject arrayFila = arrayCompleto.getJSONObject(i);
                                String id = arrayFila.getString("idSexo");
                                String nombre = arrayFila.getString("nombre");
                                listaSexos.add(i+1,nombre);
                                mapSexos.put(id,nombre);
                                listaSexosFam.add(i+1,nombre);
                                mapSexosFam.put(id,nombre);
                            }
                            adapterSexos = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaSexos);
                            adapterSexos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sexos.setAdapter(adapterSexos);

                            adapterSexosFam = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaSexosFam);
                            adapterSexosFam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sexosFam.setAdapter(adapterSexosFam);

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

                            listaNacionalidadesFam.add(0, "Seleccione nacionalidad");
                            mapNacionalidadesFam.put("00000000-0000-0000-0000-000000000000","Seleccione nacionalidad");

                            for (int i = 0; i < arrayCompleto.length(); i++) {
                                JSONObject arrayFila = arrayCompleto.getJSONObject(i);
                                String id = arrayFila.getString("idNacionalidad");
                                String nombre = arrayFila.getString("nombre");

                                listaNacionalidades.add(i+1,nombre);
                                mapNacionalidades.put(id,nombre);
                                listaNacionalidadesFam.add(i+1,nombre);
                                mapNacionalidadesFam.put(id,nombre);
                            }
                            adapterNacionalidades = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaNacionalidades);
                            adapterNacionalidades.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            nacionalidades.setAdapter(adapterNacionalidades);
                            adapterNacionalidadesFam = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaNacionalidadesFam);
                            adapterNacionalidadesFam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            nacionalidadesFam.setAdapter(adapterNacionalidadesFam);

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
                            listaEstadosCivilesFam.add(0, "Seleccione estado civil");
                            mapEstadosCivilesFam.put("00000000-0000-0000-0000-000000000000","Seleccione estado civil");

                            for (int i = 0; i < arrayCompleto.length(); i++) {
                                JSONObject arrayFila = arrayCompleto.getJSONObject(i);
                                String id = arrayFila.getString("idEstadoCivil");
                                String nombre = arrayFila.getString("nombre");

                                listaEstadosCiviles.add(i+1,nombre);
                                mapEstadosCiviles.put(id,nombre);

                                listaEstadosCivilesFam.add(i+1,nombre);
                                mapEstadosCivilesFam.put(id,nombre);
                            }
                            adapterEstadosCiviles = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaEstadosCiviles);
                            adapterEstadosCiviles.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            estadosCiviles.setAdapter(adapterEstadosCiviles);

                            adapterEstadosCivilesFam = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listaEstadosCivilesFam);
                            adapterEstadosCivilesFam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            estadosCivilesFam.setAdapter(adapterEstadosCivilesFam);

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
    //endregion

    public <K, V> K getKey(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
    //Selector de fechas
    private void showDatePickerDialog(EditText fechaDestino ) {
        DatePickerFragment newFragment =
                DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because January is zero
                        final String selectedDate = twoDigits(day) + "/" + twoDigits(month+1) + "/" + year;
                        fechaDestino.setText(selectedDate);
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