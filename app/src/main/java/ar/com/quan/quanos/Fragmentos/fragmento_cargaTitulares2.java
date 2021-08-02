package ar.com.quan.quanos.Fragmentos;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ar.com.quan.quanos.Interfaces.FragmentChangeTrigger;
import ar.com.quan.quanos.R;


public class fragmento_cargaTitulares2 extends Fragment implements View.OnClickListener{
    FragmentChangeTrigger trigger;

    public fragmento_cargaTitulares2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragmento_carga_titulares2, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }
    @Override
    public void onClick(View v) {}
    public void setTrigger(FragmentChangeTrigger trigger) {
        this.trigger = trigger;
    }

}