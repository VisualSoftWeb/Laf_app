package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import lafpan.laf_app.com.splashactivity.R;
import model.ItemPedido;
import model.Pedido;


public class AdapterPedido extends RecyclerView.Adapter<AdapterPedido.MyViewHolder> {

    private List<Pedido> pedidos;

    public AdapterPedido(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pedidos, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        Pedido pedido = pedidos.get(i);
        holder.nome.setText( pedido.getNomeCompleto() );
        holder.cpf_cnpj.setText( pedido.getCpfCnpj() );
        holder.cidade.setText( pedido.getCidade() );
        holder.bairro.setText( pedido.getBairro() );
        holder.rua.setText( pedido.getRua() );
        holder.num.setText( pedido.getNumEst() );
        holder.telefone.setText( pedido.getTelefone() );
        holder.observacao.setText( "Obs: "+ pedido.getObservacao() );

        List<ItemPedido> itens = new ArrayList<>();
        itens = pedido.getItens();
        String descricaoItens = "";

        int numeroItem = 1;
        for( ItemPedido itemPedido : itens ){

            int qtde = itemPedido.getQuantidade();

            String nome = itemPedido.getNomeProduto();
            descricaoItens += numeroItem + " - " + nome + " / (" + qtde + ") \n";
            numeroItem++;
        }

        holder.itens.setText(descricaoItens);

        int periodoEntrega = pedido.getPeriodoEntrega();
        String periodo = periodoEntrega == 0 ? "Dinheiro" : "Máquina cartão" ;
        holder.periodoEntrega.setText( "Periodo: " + periodo );

    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView cpf_cnpj;
        TextView cidade;
        TextView bairro;
        TextView rua;
        TextView num;
        TextView telefone;
        TextView periodoEntrega;
        TextView observacao;
        TextView itens;

        public MyViewHolder(View itemView) {
            super(itemView);

            nome        = itemView.findViewById(R.id.textPedidoNome);
            cpf_cnpj   = itemView.findViewById(R.id.textPedidoCpfCnpj);
            cidade = itemView.findViewById(R.id.textPedidoCidade);
            bairro = itemView.findViewById(R.id.textPedidoBairro);
            rua = itemView.findViewById(R.id.textPedidoRua);
            num = itemView.findViewById(R.id.textPedidoNum);
            telefone = itemView.findViewById(R.id.textPedidoTelefone);
            periodoEntrega  = itemView.findViewById(R.id.textPedidoPeriodoEntrega);
            observacao  = itemView.findViewById(R.id.textPedidoObs);
            itens       = itemView.findViewById(R.id.textPedidoItens);
        }
    }

}
