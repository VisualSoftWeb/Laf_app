package lafpan.laf_app.com.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterPedido;
import Helper.ConfiguracaoFirebase;
import Helper.UsuarioFirebase;
import dmax.dialog.SpotsDialog;
import lafpan.laf_app.com.splashactivity.R;
import model.Pedido;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos;
    private AdapterPedido adapterPedido;
    private List<Pedido> pedidos = new ArrayList<>();
    private AlertDialog dialog;
    private DatabaseReference firebaseRef;
    private String idEmpresa;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        // configurações

        inicializarComponentes();

        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idEmpresa = UsuarioFirebase.getIdUsuario();


            //Configuração da Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Pedidos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //configura RecycleView
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setHasFixedSize(true);
        adapterPedido = new AdapterPedido(pedidos);
        recyclerPedidos.setAdapter(adapterPedido);

        recuperarPedidos();

    }

    private  void recuperarPedidos(){

        dialog = new SpotsDialog.Builder().setContext(this)
                .setMessage("Carregando dados")
                .setCancelable(false)
                .build();
        dialog.show();

        Log.i("idEmpresa ========> ", idEmpresa);

        DatabaseReference pedidoRef = firebaseRef

                .child("pedidos_confirmados")
                .child(idEmpresa);

        //Query pedidoPesquisa = pedidoRef.orderByChild( "status")
                //Seu firebase está "Confirmado" e você está tentando encontrar um valor "confirmado"
                //.equalTo("Confirmado");



        pedidoRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                pedidos.clear();
                if(dataSnapshot.getValue() != null){


                    for (DataSnapshot ds: dataSnapshot.getChildren()){

                        //Vamos tentar com um outro for
                        for(DataSnapshot ds2 : ds.getChildren()){
                            Pedido pedido = ds2.getValue(Pedido.class);
                            pedidos.add(pedido);
                        }
                    }

                    adapterPedido.notifyDataSetChanged();

                }

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void inicializarComponentes(){

        recyclerPedidos= findViewById(R.id.recyclerPedidos);
    }
}