package practica5.sebastiancardonahenao.iesseveroochoa.net.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {DiaDiarioDao.class}, version = 1)
//Transforma las fechas en enteros
@TypeConverters({TransformaFechaSQLite.class})
public abstract class DiaDiarioDatabase extends RoomDatabase {
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    //para el manejo de base de datos con room necesitamos realzar las tareas CRUD en hilos,
    //las consultas Select que devuelve LiveData, Room crea los hilos automáticamente, pero para las
    //insercciones, acutalizaciones y borrado, tenemos que crear el hilo nosotros
    //Utilizaremos ExecutorService para el control de los hilos.
    // bd
    private static volatile DiaDiarioDatabase INSTANCE;
    //crearemos una tarea en segundo plano que nos permite cargar los datos de ejemplo la primera
    //vez que se abre la base de datos
    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // new PopulateDbAsync(INSTANCE).execute();
            //creamos algunos contactos en un hilo
            databaseWriteExecutor.execute(() -> {
                //obtenemos la base de datos
                DiaDiarioDao diaDiarioDao = INSTANCE.diaDiarioDao();
                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");
                DiaDiario diario = null;
                //creamos unos contactos
                try {
                    diario = new DiaDiario(formatoDelTexto.parse("12-2-2022"), 8, "PDF 2", "Terminamos el primer PDF y empezamos con el segundo");
                    diaDiarioDao.insert(diario);
                    diario = new DiaDiario(formatoDelTexto.parse("11-2-2022"), 6, "HIBERNATE", "Me he hecho una práctica de 2 semanas en un día, me duele la vida");
                    diaDiarioDao.insert(diario);
                    diario = new DiaDiario(formatoDelTexto.parse("10-2-2022"), 3, "Pop-Ops", "Casi la lio, me he tenido que ir a casita a hacer del vientre, con los problemas del estómago se me olvida que no puedo comer chocolate");
                    diaDiarioDao.insert(diario);
                    diario = new DiaDiario(formatoDelTexto.parse("9-2-2022"), 1, "PRUEBA", "Este es una prueba, todos mis días en verdad son de 10");
                    diaDiarioDao.insert(diario);
                    diario = new DiaDiario(formatoDelTexto.parse("12-3-2020"), 5, "Un día un" +
                            "poco aburrido, solo he visto Netflix", "Lorem ipsum dolor sit amet," +
                            "consectetur adipiscing elit. Nam rutrum lectus vestibulum, consectetur urna"
                            + "vel, rutrum tortor. Phasellus at leo nibh. Pellentesque lacinia blandit dui"
                            + "eu aliquam. Cras et suscipit nibh. Cras vehicula lobortis ante, vel hendrerit"
                            + "diam convallis at. Nullam egestas vel dui sed tincidunt. In placerat ac"
                            + "mauris eu faucibus. Nullam eu pretium justo. Suspendisse in leo nisi. Nulla"
                            + "hendrerit erat a finibus egestas. Nulla et libero eu purus euismod"
                            + "maximus.");
                    diaDiarioDao.insert(diario);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            //si queremos realizar alguna tarea cuando se abre
        }
    };

    public static DiaDiarioDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DiaDiario.class) {
                if (INSTANCE == null) {
                    // Auí crea la base de datos
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DiaDiarioDatabase.class, "Día diario") //Ponemos nombre
                            .addCallback(sRoomDatabaseCallback) // Funcion al crear o abrir
                            .build();
                }
            }

        }
        return INSTANCE;
    }

    // Permite acceso a los métodos CRUD
    public abstract DiaDiarioDao diaDiarioDao();

}
