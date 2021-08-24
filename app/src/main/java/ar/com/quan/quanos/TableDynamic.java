package ar.com.quan.quanos;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TableDynamic implements View.OnClickListener {
    private TableLayout tableLayout;
    private Context context;
    private String[] header;
    private ArrayList<String[]> data;
    private TableRow tableRow;
    private TextView txtCell;
    private Button btnAccion;
    private ImageButton btnAccionI,btnAccionII;
    private int indexC;
    private int indexR;
    private boolean multiColor = false;
    int firtColor;
    int secondColor;
    int textColor;
    private TableRow filaEliminar;

    public TableDynamic(TableLayout tableLayout, Context context) {
        this.tableLayout = tableLayout;
        this.context = context;
    }

    public void addHeader(String[] header) {
        this.header = header;
        createHeader();
    }

    public void addData(ArrayList<String[]> data) {
        this.data = data;
        createDataTable();
    }

    private void newRow() {
        tableRow = new TableRow(context);
    }

    private void newCell() {
        txtCell = new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(17);
    }

    private void createHeader() {
        indexC = 0;
        newRow();
        while (indexC < header.length) {
            newCell();
            txtCell.setText(header[indexC++]);
            tableRow.addView(txtCell, newTableRowParams());
        }
        tableLayout.addView(tableRow);
    }

    private void createDataTable() {
        String info;
        for (indexR = 1; indexR <= data.size(); indexR++) {
            newRow();
            for (indexC = 0; indexC < header.length; indexC++) {
                String[] columns = data.get(indexR-1 );
                info = (indexC < columns.length) ? columns[indexC] : "";

               if (info.equals("Eliminar")) {
                   btnAccionII = new ImageButton(context);
                   btnAccionII.setImageResource(R.drawable.borragrilla);
                    tableRow.addView(btnAccionII, newTableRowParams());
                   btnAccionII.setId(indexR);
                   btnAccionII.setOnClickListener(TableDynamic.this); // set TableRow onClickListner

                } else if (info.equals("Modificar")) {
/*                   btnAccionI = new ImageButton(context);
                    btnAccionI.setImageResource(R.drawable.borragrilla);
                    tableRow.addView(btnAccionI, newTableRowParams());
                    btnAccionI.setId(indexR);
                    btnAccionI.setOnClickListener(TableDynamic.this); // set TableRow onClickListner*/
                    btnAccionI = new ImageButton(context);
                    btnAccionI.setImageResource(R.drawable.modificagrilla);
                    tableRow.addView(btnAccionI, newTableRowParams());
                    btnAccionI.setId(indexR + 10000);
                    btnAccionI.setOnClickListener(TableDynamic.this); // set TableRow onClickListner

                } else {
                    newCell();
                    txtCell.setText(info);
                    tableRow.addView(txtCell, newTableRowParams());

                }
            }
            tableLayout.addView(tableRow);

        }
    }

    public void addItems(String[] item) {
        String info;
        data.add(item);
        indexC = 0;
        newRow();
        while (indexC < header.length) {
            if (item[indexC].equals("Eliminar")) {
                btnAccionII = new ImageButton(context);
                btnAccionII.setImageResource(R.drawable.borragrilla);
                tableRow.addView(btnAccionII, newTableRowParams());
                btnAccionII.setId(data.size());
                btnAccionII.setOnClickListener(TableDynamic.this); // set TableRow onClickListner
                indexC++;
            } else if (item[indexC].equals("Modificar")) {
                /*btnAccionI = new ImageButton(context);
                btnAccionI.setImageResource(R.drawable.borragrilla);
                tableRow.addView(btnAccionI, newTableRowParams());
                btnAccionI.setId(data.size());
                btnAccionI.setOnClickListener(TableDynamic.this); // set TableRow onClickListner*/
                btnAccionI = new ImageButton(context);
                btnAccionI.setImageResource(R.drawable.modificagrilla);
                tableRow.addView(btnAccionI, newTableRowParams());
                btnAccionI.setId(data.size() + 10000);
                btnAccionI.setOnClickListener(TableDynamic.this); // set TableRow onClickListner
                indexC++;
            } else {
                newCell();
                info = (indexC < item.length) ? item[indexC++] : "";
                txtCell.setText(info);
                tableRow.addView(txtCell, newTableRowParams());
            }
        }
        tableLayout.addView(tableRow, data.size());//Se quito el -1 despues de size para corregir
        reColoring();
    }

    public void backgroundHeader(int color) {
        indexC = 0;
        newRow();
        while (indexC < header.length) {
            txtCell = getCell(0, indexC++);
            txtCell.setBackgroundColor(color);
        }
    }

    public void backgroundData(int firtColor, int secondColor) {
        for (indexR = 1; indexR <= data.size(); indexR++) {
            multiColor = !multiColor;
            for (indexC = 0; indexC < header.length; indexC++) {
                if (header[indexC].equals("Eliminar")||header[indexC].equals("Modificar")) {

                } else {
                    txtCell = getCell(indexR, indexC);
                    txtCell.setBackgroundColor((multiColor) ? firtColor : secondColor);
                }
            }
        }
        this.firtColor = firtColor;
        this.secondColor = secondColor;
    }

    public void lineColor(int color) {
        indexR = 0;
        while (indexR < data.size()) {
            getRow(indexR++).setBackgroundColor(color);
        }
    }

    public void textColorData(int color) {
        for (indexR = 1; indexR <= data.size(); indexR++) {
            for (indexC = 0; indexC < header.length; indexC++) {
                if (header[indexC].equals("Eliminar")||header[indexC].equals("Modificar")) {

                } else {
                    getCell(indexR, indexC).setTextColor(color);
                }
            }
        }
        this.textColor = color;
    }

    public void textColorHeader(int color) {
        indexC = 0;
        while (indexC < header.length) {
            getCell(0, indexC++).setTextColor(color);
        }
    }

    public void reColoring() {
        indexC = 0;
        multiColor = !multiColor;
        while (indexC < header.length) {

            if (header[indexC].equals("Eliminar"))
            {
                btnAccionII.setBackgroundColor((multiColor) ? firtColor : secondColor);


            }
            else if (header[indexC].equals("Modificar"))
            {
                btnAccionI.setBackgroundColor((multiColor) ? firtColor : secondColor);


            }else {
                txtCell = getCell(data.size(), indexC);//Se quito el -1 despues de size
                txtCell.setBackgroundColor((multiColor) ? firtColor : secondColor);
                txtCell.setTextColor(textColor);
            }
            indexC++;
        }
    }

    public void reColoringAll() {
        indexR = 1;
        while (indexR < data.size()) {
            indexC = 0;
            //multiColor = !multiColor;
            while (indexC < header.length) {
                if (header[indexC].equals("Eliminar"))
                {
                    //btnAccionII.setBackgroundColor((multiColor) ? firtColor : secondColor);


                }
                else if (header[indexC].equals("Modificar"))
                {
                   // btnAccionI.setBackgroundColor((multiColor) ? firtColor : secondColor);


                } else {
                    txtCell = getCell(indexR, indexC);//Se quito el -1 despues de size
                    //txtCell.setBackgroundColor((multiColor) ? firtColor : secondColor);
                    txtCell.setTextColor(textColor);
                }
                indexC++;
            }
            indexR++;
        }
        this.backgroundData(firtColor,secondColor);
    }

    private TableRow getRow(int index) {
        return (TableRow) tableLayout.getChildAt(index);
    }

    private TextView getCell(int rowIndex, int columIndex) {
        tableRow = getRow(rowIndex);
        return (TextView) tableRow.getChildAt(columIndex);
    }

    private TableRow.LayoutParams newTableRowParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1, 1, 1, 1);
        params.weight = 1;
        return params;
    }

    @Override
    public void onClick(View v) {
        int clicked_id = v.getId();
        int puntero = clicked_id - 1;

        String a = String.valueOf(clicked_id);
        if (clicked_id < 10000) {
            data.remove(puntero);
            tableLayout.removeAllViews();
            this.addHeader(header);
            this.addData(data);
            this.backgroundHeader(Color.parseColor("#819FF7"));
            this.textColorData(Color.WHITE);
            this.textColorHeader(Color.BLUE);
            this.reColoringAll();
            //Toast.makeText(context, "boton apretado "+ clicked_id +" puntero "+puntero, Toast.LENGTH_LONG).show();

        }

    }


}
