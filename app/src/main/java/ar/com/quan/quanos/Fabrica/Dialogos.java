package ar.com.quan.quanos.Fabrica;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ar.com.quan.quanos.R;

public class Dialogos {
    public static Dialog dlgBuscando(Context contexto, String mensaje){
        final Dialog dialog = new Dialog(contexto);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.dlg_modal_buscando);
        TextView textMsg = (TextView) dialog.findViewById(R.id.lblMsg);
        textMsg.setText(mensaje);
        ImageView imageView = dialog.findViewById(R.id.imgCargando);
        //*//*load from raw folder*//**/
        Glide.with(contexto).load(R.drawable.loading).into(imageView);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    public static Dialog dlgError(Context contexto, String mensaje){
        final Dialog dialog = new Dialog(contexto);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setContentView(R.layout.dlg_modal_error);
        TextView textMsgError = (TextView) dialog.findViewById(R.id.lblMsg);
        textMsgError.setText(mensaje);
        LinearLayout lnMsgTotal=(LinearLayout)dialog.findViewById(R.id.lnMsgTotal);
        lnMsgTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }
}
