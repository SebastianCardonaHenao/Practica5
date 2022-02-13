package practica5.sebastiancardonahenao.iesseveroochoa.net.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import practica5.sebastiancardonahenao.iesseveroochoa.net.R;
import practica5.sebastiancardonahenao.iesseveroochoa.net.model.DiaDiario;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btnPrueba;
    DiaDiario diario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnPrueba = findViewById(R.id.fab);
        btnPrueba.setOnClickListener(e->nuevoDia());


    }

    private void nuevoDia() {
        Intent i = new Intent(MainActivity.this, EdicionDiaActivityBien.class);
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
                        DiaDiario diaRecibido = intent.getExtras().getParcelable("Datos");
                        //diaDiarioViewModel.anyadeDia(diaRecibido);
                    }
                }
            });

}