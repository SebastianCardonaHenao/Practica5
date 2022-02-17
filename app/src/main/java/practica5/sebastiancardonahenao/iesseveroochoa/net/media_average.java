package practica5.sebastiancardonahenao.iesseveroochoa.net;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class media_average extends AppCompatActivity {

    ImageView image;
    TextView texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_average);
        image = findViewById(R.id.avg);
        texto = findViewById(R.id.tvMediaAVG);
    }
}