package practica5.sebastiancardonahenao.iesseveroochoa.net.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import io.reactivex.Single;
import practica5.sebastiancardonahenao.iesseveroochoa.net.model.DiaDiario;
import practica5.sebastiancardonahenao.iesseveroochoa.net.repository.DiarioRepositorio;

public class DiarioViewModel extends AndroidViewModel {

    private final DiarioRepositorio diarioRepositorio;

    private final MutableLiveData<String> condicionBusqueda;

    private LiveData<List<DiaDiario>> allDias;

    public DiarioViewModel(@NonNull Application application) {
        super(application);
        diarioRepositorio = DiarioRepositorio.getInstance(application);
        allDias = diarioRepositorio.getAllDiaDiario();

        condicionBusqueda = new MutableLiveData<>();
        condicionBusqueda.setValue("");

        allDias = Transformations.switchMap(condicionBusqueda,
                diarioRepositorio::getDiaOrderByResumen);
    }

    public LiveData<List<DiaDiario>> getAllDias(){
        return allDias;
    }

    public void setCondicionBusqueda(String resu){
        condicionBusqueda.setValue(resu);
    }

    //Inserción y borrado que se reflejará automáticamente gracias al observador creado en la
    //actividad
    public void insert(DiaDiario diario){
        diarioRepositorio.insert(diario);
    }
    public void delete(DiaDiario diario){
        diarioRepositorio.delete(diario);
    }
    public void update(DiaDiario diario) {diarioRepositorio.update(diario);}
    public Single<Float> getValoracionTotal(){return diarioRepositorio.getValoracionTotal();}

}
