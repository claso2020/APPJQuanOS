package ar.com.quan.quanos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import ar.com.quan.quanos.Fragmentos.fragmento_cargaTitulares;
import ar.com.quan.quanos.Fragmentos.fragmento_inicial;
import ar.com.quan.quanos.Interfaces.FragmentChangeTrigger;

public class Principal extends AppCompatActivity implements FragmentChangeTrigger {
    FragmentManager fragmentManager;
    public static String pantActual="";
    fragmento_inicial pantInicial;
    fragmento_cargaTitulares pantCargaTitulares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        fragmentManager = getSupportFragmentManager();
        pantInicial = new fragmento_inicial();
        pantInicial.setTrigger(this);

        pantActual="inicio";
        fragmentManager.beginTransaction()
                .add(R.id.frg_pantallaActual, pantInicial)
                .commit();
    }

    @Override
    public void fireChange(String i) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(i){
            case "inicial_btnVolver":
                String a="";
                Intent inent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(inent);
                pantActual="MainActivity";
                break;
            case "inicial_btnCargaTitulares":
                pantCargaTitulares = new fragmento_cargaTitulares();
                pantCargaTitulares.setTrigger(this);
                pantActual="pantCargaTitulares";
                fragmentManager.beginTransaction()
                        .replace(R.id.frg_pantallaActual, pantCargaTitulares)
                        .commit();
                break;
            case "cargaTitulares_btnVolverAInicial":
                fragmentManager.beginTransaction()
                        .replace(R.id.frg_pantallaActual, pantInicial)
                        .commit();
                break;

            default:
                break;

        }
    }
    @Override
    public void fireChange(int i, Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch(i){
            case 2:
                String a="";
                break;
            case 3:
                String b="";
                break;
        }
    }
}