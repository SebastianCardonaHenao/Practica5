package practica5.sebastiancardonahenao.iesseveroochoa.net.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.Single;
import practica5.sebastiancardonahenao.iesseveroochoa.net.model.DiaDiario;
import practica5.sebastiancardonahenao.iesseveroochoa.net.model.DiaDiarioDao;
import practica5.sebastiancardonahenao.iesseveroochoa.net.model.DiaDiarioDatabase;

public class DiarioRepositorio {

    //implementamos Singleton
    private static volatile DiarioRepositorio INSTANCE;

    //Objetos
    private final DiaDiarioDao diaDiarioDao;
    private LiveData<List<DiaDiario>> allDiaDiario;

    //singleton
    public static DiarioRepositorio getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (DiarioRepositorio.class) {
                if (INSTANCE == null) {
                    INSTANCE=new DiarioRepositorio(application);
                }
            }
        }
        return INSTANCE;
    }

    private DiarioRepositorio(Application application){
        //creamos la base de datos
        DiaDiarioDatabase db=DiaDiarioDatabase.getDatabase(application);
        //Recuperamos el DAO necesario para el CRUD de la base de datos
        diaDiarioDao =db.diaDiarioDao();
        //Recuperamos la lista como un LiveData
        allDiaDiario = diaDiarioDao.getAllDiario();
    }

    //Todo el diario
    public LiveData<List<DiaDiario>> getAllDiaDiario(){
        return allDiaDiario;
    }

    //valoracion rxJava
    public Single<Float> getValoracionTotal(){
        return diaDiarioDao.getValoracionTotal();
    }

    //lista ordenado por columnas diferentes
    public LiveData<List<DiaDiario>> getDiaOrderByResumen(String resumen){
        allDiaDiario = diaDiarioDao.getDiarioOrderBy(resumen);
        return allDiaDiario;
    }

    /*
    Insertar: nos obliga a crear tarea en segundo plano
     */
    public void insert(DiaDiario diario){
        //administramos el hilo con el Executor
        DiaDiarioDatabase.databaseWriteExecutor.execute(()-> diaDiarioDao.insert(diario));
    }

    /*
    Borrar: nos obliga a crear tarea en segundo plano
     */
    public void delete(DiaDiario diario){
        //administramos el hilo con el Executor
        DiaDiarioDatabase.databaseWriteExecutor.execute(()-> diaDiarioDao.deleteByDiaDiario(diario));
    }

    //Editar el día
    public void update(DiaDiario diario){
        //administramos el hilo con el Executor
        DiaDiarioDatabase.databaseWriteExecutor.execute(()-> diaDiarioDao.update(diario));
    }

    // BORRAMOS TODO
    public void deleteAll(){
        //administramos el hilo con el Executor
        DiaDiarioDatabase.databaseWriteExecutor.execute(diaDiarioDao::deleteAll);
    }
}
