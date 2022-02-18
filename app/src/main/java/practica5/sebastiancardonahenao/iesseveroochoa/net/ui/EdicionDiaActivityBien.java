package practica5.sebastiancardonahenao.iesseveroochoa.net.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

import practica5.sebastiancardonahenao.iesseveroochoa.net.R;
import practica5.sebastiancardonahenao.iesseveroochoa.net.model.DiaDiario;

public class EdicionDiaActivityBien extends AppCompatActivity {

    private static final int STATUS_CODE_SELECCION_IMAGEN = 300;
    Button btCalendario,btImagen;
    FloatingActionButton fab;

    Spinner spinnerValoraVida;

    TextView tvFecha;
    EditText tbResumen,tbInfoDia;
    boolean editando=false;

    Date fecha;
    private Uri uriFoto = null;
    DiaDiario esteDia;
    ImageView ivFotoDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion_dia_bien);

        btCalendario = findViewById(R.id.calendario);
        fab = findViewById(R.id.fbSave);
        tvFecha = findViewById(R.id.tvFecha);
        tbResumen = findViewById(R.id.tbResumen);
        tbInfoDia = findViewById(R.id.tbInfoDia);
        btImagen = findViewById(R.id.btImagen);
        ivFotoDia = findViewById(R.id.ivFoto);


        btImagen.setOnClickListener(e->muestraOpcionesImagen());
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
            editando=true;
            editarDia();
        }

    }

    private void elegirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Seleccione una imagen"), STATUS_CODE_SELECCION_IMAGEN);
    }

    private void muestraFoto(){
        Glide.with(this).load(uriFoto).into(ivFotoDia);//imageView
    }
    private void muestraOpcionesImagen() {
        final CharSequence[] option = {"Tomar foto","Elegir de la galería", getString(android.R.string.cancel)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(android.R.string.dialog_alert_title);
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // abrirCamara();//opcional
                        break;
                    case 1:
                        elegirGaleria();
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }



    private void guardarDiaPreferencias(Date fecha) {
        //buscamos el fichero de preferencias
        try {
            if (!editando){
            SharedPreferences sharedPref =
                    this.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
            //lo abrimos en modo edición
            SharedPreferences.Editor editor = sharedPref.edit();
            //guardamos la fecha del día como entero
            editor.putLong(getString(R.string.pref_key_ultimo_dia),
                    fecha.getTime());
            //finalizamos
            editor.commit();}
        }catch (Exception e){
            Log.d("Preferences Erro","Ha habido un error al guardar las preferencias");
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
            guardarDiaPreferencias(fecha);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case STATUS_CODE_SELECCION_IMAGEN:
                    uriFoto = data.getData();
                    muestraFoto();
                    break;
            }
        }
    }
}