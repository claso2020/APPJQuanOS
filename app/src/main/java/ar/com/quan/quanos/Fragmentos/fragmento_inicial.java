package ar.com.quan.quanos.Fragmentos;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ar.com.quan.quanos.Interfaces.FragmentChangeTrigger;
import ar.com.quan.quanos.R;

public class fragmento_inicial extends Fragment implements View.OnClickListener{
    FragmentChangeTrigger trigger;
    Button btnVolver;
    Button btnCargaTitulares;
    Button btnMostrarTitulares;

    public fragmento_inicial() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmento_inicial, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnVolver = (Button) view.findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(this);
        btnCargaTitulares =(Button) view.findViewById(R.id.btnCargaTitulares);
        btnCargaTitulares.setOnClickListener(this);
        btnMostrarTitulares=(Button) view.findViewById(R.id.btnMostrarTitulares);
        btnMostrarTitulares.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnVolver){
            trigger.fireChange("inicial_btnVolver");
        }
        if(v.getId()==R.id.btnCargaTitulares){
            trigger.fireChange("inicial_btnCargaTitulares");
        }
        if(v.getId()==R.id.btnMostrarTitulares){
            trigger.fireChange("inicial_btnMostrarTitulares");
        }
    }

    public void setTrigger(FragmentChangeTrigger trigger) {
        this.trigger = trigger;
    }
}