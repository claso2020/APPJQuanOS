package ar.com.quan.quanos;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import ar.com.quan.quanos.Fabrica.Dialogos;
import ar.com.quan.quanos.Interfaces.ErrorResponseHandler;
import ar.com.quan.quanos.Interfaces.SuccessResponseHandler;

public class MainActivity extends AppCompatActivity {
    private Dialog dialogo;
    Activity actActual = this;
    EditText usu, pass;
    Button btn1;
    String  idUsuario;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usu=(EditText)findViewById(R.id.usu);
        pass=(EditText)findViewById(R.id.pass);
        btn1=(Button)findViewById(R.id.btn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificar();
            }
        });
        verificarPermisos();
    }
    private void verificar(){
        dialogo = Dialogos.dlgBuscando(actActual,"Verificando identidad");
        /*WebService.verificarUsuario(actActual, usu.getText().toString(),pass.getText().toString()*/
        WebService.verificarUsuario(actActual, "demo","demo"
                , new SuccessResponseHandler<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject valores) {
                        dialogo.dismiss();
                        try {
                            SharedPreferences globales = getSharedPreferences("datosGlobalesApp",MODE_PRIVATE);
                            SharedPreferences.Editor editorGlobales = globales.edit();
                            editorGlobales.putString("idUsuario", valores.getString("idConexion").toString());
                            editorGlobales.putString("token", valores.getString("token").toString());
                            editorGlobales.commit();
                            Intent inent = new Intent(getBaseContext(), Principal.class);
                            startActivity(inent);
                        } catch (Exception errEx) {
                            dialogo = Dialogos.dlgError(actActual,errEx.getMessage());
                        }
                    }
                }, new ErrorResponseHandler() {
                    @Override
                    public void onError(String msg) {
                        dialogo.dismiss();
                        dialogo = Dialogos.dlgError(actActual,msg);
                    }
                });

    }
    private void verificarPermisos(){
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                pedirPermisos();
            }else {
                // esLaPrimeraVez();
            }
        } catch (Exception errorEx){
            /*dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dlg_modal_error);
            // set the custom dialog components - text, image and button
            TextView textMsg = (TextView) dialog.findViewById(R.id.lblMsg);
            textMsg.setText("Error en inicio:\n" + errorEx.getMessage());
            ImageView imgCerrar= (ImageView) dialog.findViewById(R.id.btnCerrar);
            imgCerrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finishAffinity();
                    dialog.dismiss();
                }
            });
            dialog.show();*/
        }
    }
    private void pedirPermisos(){
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!agregarPermiso(permissionsList, android.Manifest.permission.INTERNET))
            permissionsNeeded.add("\t-Acceso a internet\n");

        if (!agregarPermiso(permissionsList, android.Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("\t-GPS\n");
        if (!agregarPermiso(permissionsList, android.Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("\t-Estado de la red\n");
        if (!agregarPermiso(permissionsList, android.Manifest.permission.CAMERA))
            permissionsNeeded.add("\t-Cámara\n");
        if (!agregarPermiso(permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("\t-Leer del almacenamiento externo\n");
        if (!agregarPermiso(permissionsList, android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("\t-Escribir en el almacenamiento externo\n");
        if (!agregarPermiso(permissionsList, android.Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("\t-Estado del teléfono\n");
        if (!agregarPermiso(permissionsList, android.Manifest.permission.GET_ACCOUNTS))
            permissionsNeeded.add("\t-Utilizar cuentas de email\n");
        if (!agregarPermiso(permissionsList, android.Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("\t-Contactos\n");
        if (!agregarPermiso(permissionsList, android.Manifest.permission.RECORD_AUDIO))
            permissionsNeeded.add("\t-Audio\n");
        if (!agregarPermiso(permissionsList, android.Manifest.permission.MODIFY_AUDIO_SETTINGS))
            permissionsNeeded.add("\t-Modificar el audio\n");
        if (!agregarPermiso(permissionsList, android.Manifest.permission.WAKE_LOCK))
            permissionsNeeded.add("\t-Activar el dispositivo\n");


        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "Para poder utilizar esta APP, se necesitan los siguientes permisos:\n " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++) {
                    message = message + permissionsNeeded.get(i);
                }
                mostrarMensajePermisos(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(actActual, permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        },new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                return;
            }

            ActivityCompat.requestPermissions(this,permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
        //esLaPrimeraVez();
    }
    private boolean agregarPermiso(List<String> permissionsList, String permission) {
/*        if (ContextCompat.checkSelfPermission(this,permission) =="INTERNET")
        {
            permissionsList.add(permission);

        }*/
        if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsList.add(permission);
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,permission))
                return false;
        }
        return true;
    }
    private void mostrarMensajePermisos(String message, DialogInterface.OnClickListener okListener
            , DialogInterface.OnClickListener cancelListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Continuar", okListener)
                .setNegativeButton("Cancelar", cancelListener)
                .setCancelable(false)
                .create()
                .show();





    }
}