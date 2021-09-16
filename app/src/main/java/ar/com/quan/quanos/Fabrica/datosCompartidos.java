package ar.com.quan.quanos.Fabrica;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class datosCompartidos extends ViewModel {
    private MutableLiveData<String> idGeneral=new MutableLiveData<>();
    private MutableLiveData<String> txtGeneral=new MutableLiveData<>();

    public void setIdGeneral(String input){
        idGeneral.setValue(input);
    }

    public LiveData<String> getIdGeneral(){
        return idGeneral;
    }

    public void setTxtGeneral(String input){
        txtGeneral.setValue(input);
    }

    public LiveData<String> getTxtGeneral(){
        return txtGeneral;
    }
}
