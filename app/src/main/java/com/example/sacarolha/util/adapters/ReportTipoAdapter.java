package com.example.sacarolha.util.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sacarolha.R;
import com.example.sacarolha.util.model.VendaPorTipoVinho;
import java.util.List;

public class ReportTipoAdapter extends RecyclerView.Adapter<ReportTipoAdapter.VinhoViewHolder>  {
    private List<VendaPorTipoVinho> vinhoList;

    public ReportTipoAdapter(List<VendaPorTipoVinho> vinhoList) {
        this.vinhoList = vinhoList;
    }

    @NonNull
    @Override
    public VinhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report_tipo, parent, false);
        return new VinhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VinhoViewHolder holder, int position) {
        VendaPorTipoVinho vinho = vinhoList.get(position);
        if (vinho != null) {
            holder.textTipoVinho.setText(vinho.getTipoVinho());
            holder.textQuantidadeVendida.setText("Quantidade Vendida: " + vinho.getQuantidadeVendida());
            holder.textValorTotal.setText("Valor Total: R$ " + String.format("%.2f", vinho.getValorTotal()));
        }
    }

    @Override
    public int getItemCount() {
        return vinhoList.size();
    }

    static class VinhoViewHolder extends RecyclerView.ViewHolder {
        TextView textTipoVinho;
        TextView textQuantidadeVendida;
        TextView textValorTotal;

        VinhoViewHolder(@NonNull View itemView) {
            super(itemView);
            textTipoVinho = itemView.findViewById(R.id.text_tipo_vinho);
            textQuantidadeVendida = itemView.findViewById(R.id.text_quantidade_vendida);
            textValorTotal = itemView.findViewById(R.id.text_valor_total);
        }
    }
}
