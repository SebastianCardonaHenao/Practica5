package practica5.sebastiancardonahenao.iesseveroochoa.net.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import practica5.sebastiancardonahenao.iesseveroochoa.net.model.DiaDiario;
import practica5.sebastiancardonahenao.iesseveroochoa.net.repository.DiarioRepositorio;

public class DiarioViewModel extends AndroidViewModel {

    private DiarioRepositorio diarioRepositorio;
    private LiveData<List<DiaDiario>> allDias;

    public DiarioViewModel(@NonNull Application application) {
        super(application);
        diarioRepositorio = DiarioRepositorio.getInstance(application);
        allDias = diarioRepositorio.getAllDiaDiario();
    }

    public LiveData<List<DiaDiario>> getAllDias(){
        return allDias;
    }

    //Inserción y borrado que se reflejará automáticamente gracias al observador creado en la
    //actividad
    public void insert(DiaDiario diario){
        diarioRepositorio.insert(diario);
    }
    public void delete(DiaDiario diario){
        diarioRepositorio.delete(diario);
    }

}
