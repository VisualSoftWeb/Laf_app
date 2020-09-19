package Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lafpan.laf_app.com.splashactivity.R;
import model.Produto;


public class AdapterProduto extends RecyclerView.Adapter<AdapterProduto.MyViewHolder>{

        private List<Produto> produtos;
        private Context context;




    public AdapterProduto(List<Produto> produtos, Context context) {
            this.produtos = produtos;
            this.context = context;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_produto, parent, false);
            return new MyViewHolder(itemLista);
        }

        @SuppressLint("ResourceType")
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
            Produto produto = produtos.get(i);
            
            holder.nomeProduto.setText(String.valueOf(produto.getNomeProduto()));
            holder.fermentacao.setText(String.valueOf(produto.getFermentacao()) );
            holder.descricao.setText(produto.getDescricao());

            Picasso.get().load(produto.getUrlImagem()).fit().into(holder.imagemProduto);
        }

        @Override
        public int getItemCount() {
            return produtos.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            CircleImageView imagemProduto;
            TextView nomeProduto;
            TextView descricao;
            TextView fermentacao;

            public MyViewHolder(View itemView) {
                super(itemView);

                imagemProduto = itemView.findViewById(R.id.imagemProduto);
                nomeProduto = itemView.findViewById(R.id.textNomeProduto);
                fermentacao = itemView.findViewById(R.id.textFermentacao);
                descricao = itemView.findViewById(R.id.textDescricao);
            }
        }
    }