package practica5.sebastiancardonahenao.iesseveroochoa.net.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import io.reactivex.Single;

/**
 * a. void insert(DiaDiario)
 * b. void deleteByDiaDiario(DiaDiario)
 * c. void update(DiaDiario)
 * d. void deleteAll() borra todo el diario
 * e. LiveData<List<DiaDiario>> getAllDiario() nos devuelve todo el diario
 * f. LiveData<List<DiaDiario>> getDiarioOrderBy(String resumen) nos devuelve el
 * diario con aquellos días que contienen la palabra “resumen” en el campo
 * Resumen del diaDiario.
 * g. Single<Float> getValoracioTotal() nos devuelve la media (AVG) de la valoración
 * de todos los días en un objeto observable(rxJava). Ya hablaremos de rxJava
 */

@Dao
public interface DiaDiarioDao {
    // Nuevo dia borrando el anterior
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DiaDiario diario);

    // Borrar día concreto
    @Delete
    void deleteByDiaDiario(DiaDiario diario);

    //  Editar día
    @Update
    void update(DiaDiario diario);

    // NO USAR, BORRA TODO
    @Query("DELETE FROM "+DiaDiario.TABLE_NAME)
    void deleteAll();

    // Live Data todo el diario
    @Query("SELECT * FROM "+DiaDiario.TABLE_NAME)
    LiveData<List<DiaDiario>> getAllDiario();

    // Live Data Ordenada
    @Query("SELECT * FROM "+DiaDiario.TABLE_NAME+" ORDER BY :resumen")
    LiveData<List<DiaDiario>> getDiarioOrderBy(String resumen);

    // rxJava AVG
    @Query("SELECT AVG(*) FROM "+DiaDiario.TABLE_NAME)
    Single<Float> getValoracionTotal();
}