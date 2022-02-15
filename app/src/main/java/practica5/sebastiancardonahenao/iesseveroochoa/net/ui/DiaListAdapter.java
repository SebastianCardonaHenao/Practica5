package practica5.sebastiancardonahenao.iesseveroochoa.net.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import practica5.sebastiancardonahenao.iesseveroochoa.net.R;
import practica5.sebastiancardonahenao.iesseveroochoa.net.model.DiaDiario;

public class DiaListAdapter extends RecyclerView.Adapter<DiaListAdapter.DiaViewHolder> {

    private final LayoutInflater layoutInflater;
    private List<DiaDiario> dias;

    private OnItemClickBorrarListener listenerBorrar;
    private OnItemClickDiaDiarioListener listenerClickDiaDiario;
    private OnIntemEditarListener listenerEditar;


    public DiaListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.item_dia, parent, false);
        return new DiaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaViewHolder holder, int position) {
        if (dias != null){
            final DiaDiario diario = dias.get(position);
            int valor = diario.getValoracionResumida();
            holder.resumen.setText(diario.getResumen());
            holder.fecha.setText(diario.getFechaFormatoLocal());
            switch (valor){
                case 1:
                    holder.imagen.setImageResource(R.drawable.ic_triste);
                    break;
                case 2:
                    holder.imagen.setImageResource(R.drawable.ic_basico);
                    break;
                case 3:
                    holder.imagen.setImageResource(R.drawable.ic_feliz);
                    break;
            }
        }else {
            holder.resumen.setText("Sín información");
            holder.fecha.setText("Vacio");
            holder.imagen.setImageResource(R.drawable.ic_basico);
        }
    }

    @Override
    public int getItemCount() {
        if(dias != null)
            return dias.size();
        else return 0;
    }

    void setDias(List<DiaDiario> dias){
        this.dias = dias;
        notifyDataSetChanged();
    }

    public class DiaViewHolder extends RecyclerView.ViewHolder {

        private TextView resumen;
        private TextView fecha;
        private ImageView imagen;


        public DiaViewHolder(@NonNull View itemView) {
            super(itemView);
            resumen = itemView.findViewById(R.id.tvResumen);
            fecha = itemView.findViewById(R.id.tvFechita);
            imagen = itemView.findViewById(R.id.icono);
            Button borrar = itemView.findViewById(R.id.btBorrar);

            //listener
            borrar.setOnClickListener(view -> {
                if (listenerBorrar!=null)
                    listenerBorrar.onItemClickBorrar(dias.get(DiaViewHolder.this.getAbsoluteAdapterPosition()));
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listenerClickDiaDiario != null)
                        listenerClickDiaDiario.onItemClickDiaDiario(dias.get(DiaViewHolder.this.getAbsoluteAdapterPosition()));
                }
            });
        }

    }
    public interface OnItemClickBorrarListener {
        void onItemClickBorrar(DiaDiario diario);
    }
    public interface OnItemClickDiaDiarioListener{
        void onItemClickDiaDiario(DiaDiario diario);
    }
    public interface OnIntemEditarListener{
        void onItemEditar(DiaDiario diario);
    }

    /**
     * permiten crear el listener de acción
     */

    public void setOnClickBorrarListener(OnItemClickBorrarListener listener) {
        this.listenerBorrar = listener;
    }
    public void setOnClickDiaDiarioListener(OnItemClickDiaDiarioListener listener) {
        this.listenerClickDiaDiario = listener;
    }
    public void setOnClickEditarLister(OnIntemEditarListener listener){
        this.listenerEditar = listener;
    }
}
