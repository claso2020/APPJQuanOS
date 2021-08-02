package ar.com.quan.quanos.Interfaces;

import android.os.Bundle;

public interface FragmentChangeTrigger {
    void fireChange(String nombreBtn);
    void fireChange(int i, Bundle bundle);
}
