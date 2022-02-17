package practica5.sebastiancardonahenao.iesseveroochoa.net.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import practica5.sebastiancardonahenao.iesseveroochoa.net.R;
import practica5.sebastiancardonahenao.iesseveroochoa.net.model.DiaDiario;
import practica5.sebastiancardonahenao.iesseveroochoa.net.viewmodels.DiarioViewModel;

public class MainActivity extends AppCompatActivity {

    private DiarioViewModel diarioViewModel;

    FloatingActionButton btnPrueba;
    SearchView svBusqueda;
    DiaDiario diario;
    Menu menu;
    MenuItem itOrdena;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //RECYCLER VIEW

        RecyclerView recyclerView = findViewById(R.id.rvDias);

        //Adaptamos
        final DiaListAdapter adapter = new DiaListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //View model

        diarioViewModel = new ViewModelProvider(this).get(DiarioViewModel.class);
        diarioViewModel.getAllDias().observe(this, diaDiarios -> {
            adapter.setDias(diaDiarios);
            Log.d("P5","tamaño: "+diaDiarios.size());
        });
        //
        adapter.setOnClickBorrarListener(this::borrarDia);
        adapter.setOnClickDiaDiarioListener(this::editar);
        //////

        Toolbar toolbar = findViewById(R.id.toolbar);
        svBusqueda = findViewById(R.id.svBusqueda);
        itOrdena = findViewById(R.id.itOrdenar);
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

    private boolean ordena() {
        final CharSequence[] items = { "Fecha", "Valoración", "Resumen"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Ordenar por:");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (items[item].toString()){
                    case "Fecha":
                        //TODO
                        break;
                    case "Valoración":
                        //TODO
                        break;
                    case "Resumen":
                        //TODO
                        break;
                }
                dialog.dismiss();

            }
        }).show();
        return true;
    }

    private void editar(DiaDiario diario1) {
        Intent i = new Intent(this, EdicionDiaActivityBien.class);
        i.putExtra("Datos",diario1);
        startActivityForResoultEdit.launch(i);
    }

    private void borrarDia(DiaDiario diario1) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("¿Está seguro que desea eliminar la página del diario?")
                .setTitle("AVISO")
                .setPositiveButton("ACEPTAR", (dialog1, id) -> {
                    diarioViewModel.delete(diario1);
                    dialog1.cancel();
                }).setNegativeButton("Cancelar",(dialog1, id) ->
                dialog1.cancel());
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
                        diarioViewModel.update(diaRecibido);
                        ///NO FUNCIONA, MIRAR
                    }
                }
            });

}