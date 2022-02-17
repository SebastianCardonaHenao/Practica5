package practica5.sebastiancardonahenao.iesseveroochoa.net.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

import practica5.sebastiancardonahenao.iesseveroochoa.net.R;
import practica5.sebastiancardonahenao.iesseveroochoa.net.model.DiaDiario;

public class EdicionDiaActivityBien extends AppCompatActivity {

    Button btCalendario;
    FloatingActionButton fab;

    Spinner spinnerValoraVida;

    TextView tvFecha;
    EditText tbResumen,tbInfoDia;

    Date fecha;

    DiaDiario esteDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_dia_bien);

        btCalendario = findViewById(R.id.calendario);
        fab = findViewById(R.id.fbSave);
        tvFecha = findViewById(R.id.tvFecha);
        tbResumen = findViewById(R.id.tbResumen);
        tbInfoDia = findViewById(R.id.tbInfoDia);


        btCalendario.setOnClickListener(e-> fecha(tvFecha));
        fab.setOnClickListener(e-> guardar());

        spinnerValoraVida = findViewById(R.id.spValoracion);

        ArrayAdapter<CharSequence>adaptadorValoracion = ArrayAdapter.createFromResource(this,
                R.array.spinner_vida, android.R.layout.simple_spinner_item);
        spinnerValoraVida.setAdapter(adaptadorValoracion);

        spinnerValoraVida.setSelection(5);

        Bundle bundle = getIntent().getExtras();
        esteDia = bundle.getParcelable("Datos");
        if (esteDia!=null){
            this.setTitle("Editar Día "+esteDia.getId());
            editarDia();
        }

    }

    private void editarDia() {
        spinnerValoraVida.setSelection(esteDia.getValoracionDia());
        tbResumen.setText(esteDia.getResumen());
        tbInfoDia.setText(esteDia.getContenido());
        fecha = esteDia.getFecha();
        tvFecha.setText(esteDia.getFechaFormatoLocal());
    }


    private void guardar() {
        int valoracion = spinnerValoraVida.getSelectedItemPosition();
        String resumen = String.valueOf(tbResumen.getText());
        String contenido = String.valueOf(tbInfoDia.getText());

        if (resumen.equals("")) {
            dialogo("El campo resumen no puede estar vacio,\n" +
                    "por favor, rellenelo.");
            setResult(RESULT_CANCELED);
        } else if (contenido.equals("")) {
            dialogo("El contenido del día no puede estar vacio,\n" +
                    "por favor, rellenelo.");
            setResult(RESULT_CANCELED);
        } else if (fecha == null ){
            dialogo("La fecha es obligatoria,\n" +
                    "por favor, seleccionela.");
            setResult(RESULT_CANCELED);
        }
        else {
            esteDia = new DiaDiario(fecha,valoracion,resumen,contenido);
            Intent iBack = getIntent();
            iBack.putExtra("Datos", esteDia);
            setResult(RESULT_OK,iBack);
            finish();
        }
    }

    public void fecha(TextView tvFecha){
        Calendar nuevoCalendario = Calendar.getInstance();

        DatePickerDialog dialogo = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            tvFecha.setText(dayOfMonth+"/"+ monthOfYear+"/"+year);
            fecha=calendar.getTime();

        }, nuevoCalendario.get(Calendar.YEAR),
                nuevoCalendario.get(Calendar.MONTH),
                nuevoCalendario.get(Calendar.DAY_OF_MONTH));//esto último es el dia a mostrar
        dialogo.show();
    }

    public void dialogo(String mensajeDialogo){
        AlertDialog.Builder builder = new AlertDialog.Builder(EdicionDiaActivityBien.this);
        builder.setMessage(mensajeDialogo)
                .setTitle("Aviso");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}