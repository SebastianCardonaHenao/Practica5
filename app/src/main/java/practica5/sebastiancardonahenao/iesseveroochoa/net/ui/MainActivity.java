package practica5.sebastiancardonahenao.iesseveroochoa.net.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import practica5.sebastiancardonahenao.iesseveroochoa.net.R;
import practica5.sebastiancardonahenao.iesseveroochoa.net.model.DiaDiario;
import practica5.sebastiancardonahenao.iesseveroochoa.net.viewmodels.DiarioViewModel;

public class MainActivity extends AppCompatActivity {

    private DiarioViewModel diarioViewModel;

    FloatingActionButton btnPrueba;
    SearchView svBusqueda;
    DiaDiario diario;
    Toolbar toolbar;
    DiaListAdapter adapter;
    SharedPreferences sharedPref;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //RECYCLER VIEW

        recyclerView= findViewById(R.id.rvDias);

        //Adaptamos
        adapter = new DiaListAdapter(this);
        recyclerView.setAdapter(adapter);
        int orientation = getResources().getConfiguration().orientation;
        if(orientation== Configuration.ORIENTATION_PORTRAIT)//una fila
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            setTitle(sharedPref.getString("nombre",""));
        }

        //View model

        diarioViewModel = new ViewModelProvider(this).get(DiarioViewModel.class);
        diarioViewModel.getAllDias().observe(this, diaDiarios -> {
            adapter.setDias(diaDiarios);
            Log.d("P5","tama??o: "+diaDiarios.size());
        });
        //
        adapter.setOnClickBorrarListener(this::borrarDia);
        adapter.setOnClickDiaDiarioListener(this::editar);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
             diario = ((DiaListAdapter.DiaViewHolder)viewHolder).getDia();
             borrarDia(diario);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //////

        toolbar = findViewById(R.id.toolbar);
        svBusqueda = findViewById(R.id.svBusqueda);
        btnPrueba = findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        btnPrueba.setOnClickListener(e->nuevoDia());

        svBusqueda.setOnQueryTextListener(new
              SearchView.OnQueryTextListener() {
                  @Override
                  public boolean onQueryTextSubmit(String query) {
                      diarioViewModel.setCondicionBusqueda(query);
                      return true;
                  }

                  @Override
                  public boolean onQueryTextChange(String newText) {
                      if (newText.length()==0)
                          diarioViewModel.setCondicionBusqueda("");
                      return false;
                  }
              });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sex = sharedPref.getString("sexo","");
        if (Integer.parseInt(sex)==1)
            recyclerView.setBackgroundResource(R.color.chico);
        else
            recyclerView.setBackgroundResource(R.color.chica);
    }

    private void editar(DiaDiario diario1) {
        Intent i = new Intent(this, EdicionDiaActivityBien.class);
        i.putExtra("Datos",diario1);
        startActivityForResoultEdit.launch(i);
    }

    private void borrarDia(DiaDiario diario1) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("??Est?? seguro que desea eliminar la p??gina del diario?")
                .setTitle("AVISO")
                .setPositiveButton("ACEPTAR", (dialog1, id) -> {
                    diarioViewModel.delete(diario1);
                    dialog1.cancel();
                }).setNegativeButton("Cancelar",(dialog1, id) ->{
                    //final int posicion=viewHolder.getBindingAdapterPosition();
                    //adapter.notifyItemChanged(posicion);
                    dialog1.cancel();
                });

        dialog.create();
        dialog.show();
    }

    private void nuevoDia() {
        Intent i = new Intent(this, EdicionDiaActivityBien.class);
        i.putExtra("Datos",diario);
        startActivityForResoult.launch(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is
        this.
                getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.itOrdenar:
                ordena();
                break;
            case R.id.itAcerca:
                acercaDe();
                break;
            case R.id.itvaloraVida:
                mostrarAVG();
                break;
            case R.id.itLastDay:
                lastDay();
                break;
            case R.id.itConfig:
                Intent i = new Intent(this, PreferenciasActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void lastDay() {
        //recuperamos las preferencias
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file),
                Context.MODE_PRIVATE);
        //recuperamos la fecha del ??ltimos d??a editado
        long ultimoDia
                =sharedPref.getLong(getString(R.string.pref_key_ultimo_dia),0);
        //mostramos el resultado
        Toast.makeText(this,"El ??ltimo d??a editado "+DiaDiario.getFechaFormatoLocal(new Date(ultimoDia)),Toast.LENGTH_LONG).show();
    }

    private void ordena() {
        final CharSequence[] items = { "Fecha", "Valoraci??n", "Resumen"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Ordenar por:");
        builder.setItems(items, (dialog, item) -> {
            switch (items[item].toString()){
                case "Fecha":
                    //TODO
                    break;
                case "Valoraci??n":
                    //TODO
                    break;
                case "Resumen":
                    //TODO
                    break;
            }
            dialog.dismiss();

        }).show();
    }

    private void mostrarAVG() {
        diarioViewModel.getValoracionTotal()//obtenemos objeto reactivo de un solo uso 'Single' para que haga la consulta en un hilo
                .subscribeOn(Schedulers.io())//el observable(la consulta sql) se ejecuta en uno diferente
                .observeOn(AndroidSchedulers.mainThread())//indicamos que el observador es el hilo principal  de Android
                .subscribe(new SingleObserver<Float>() { //creamos el observador
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override//cuando termine  la consulta de la base de datos recibimos el valor
                    public void onSuccess(@NonNull Float media) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = MainActivity.this.getLayoutInflater();

                        View v = inflater.inflate(R.layout.activity_media_average, null);
                        ImageView image = v.findViewById(R.id.avg);
                        TextView texto = v.findViewById(R.id.tvMediaAVG);

                        if (media < 5)
                            image.setImageResource(R.drawable.ic_triste);
                        else if (media >= 5 && media < 8)
                            image.setImageResource(R.drawable.ic_basico);
                        else
                            image.setImageResource(R.drawable.ic_feliz);

                        texto.setText("La media de alegr??a es: "+media);

                        dialog.setView(v);


                        dialog.show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void acercaDe(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Est?? aplicaci??n ha sido dise??ada por:" +
                "\nSebasti??n Cardona Henao" +
                "\nEn PMDM 2??K")
                .setTitle("Acerca de...");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    ActivityResultLauncher<Intent> startActivityForResoult= registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //si el usuario pulsa OK en la Activity que hemos llamado
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //recuperamos los dados
                        Intent intent = result.getData();
                        assert intent != null;
                        DiaDiario diaRecibido = intent.getExtras().getParcelable("Datos");
                        diarioViewModel.insert(diaRecibido);
                    }
                }
            });

    ActivityResultLauncher<Intent> startActivityForResoultEdit = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        //Recuperamos datos
                        Intent intent = result.getData();
                        assert intent != null;
                        DiaDiario diaRecibido = intent.getExtras().getParcelable("Datos");
                        diarioViewModel.insert(diaRecibido);
                        ///NO FUNCIONA EL UPDATE, MIRAR
                    }
                }
            });


    /**
     * ImageView image = findViewById(R.id.avg);
     *                         TextView texto = findViewById(R.id.tvMediaAVG);
     *
     *                         if (media < 5)
     *                             image.setImageResource(R.drawable.ic_triste);
     *                         else if (media > 5 && media < 8)
     *                             image.setImageResource(R.drawable.ic_basico);
     *                         else
     *                             image.setImageResource(R.drawable.ic_feliz);
     *                         texto.setText("La media de alegr??a es: "+media);
     */

}