package lafpan.laf_app.com.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import Helper.ConfiguracaoFirebase;
import Helper.UsuarioFirebase;
import lafpan.laf_app.com.splashactivity.R;
import model.Empresa;

public class ConfiguracoesAdminActivity extends AppCompatActivity {


    private EditText editEmpresaNome, editCategoria, editPrazoEntrega,editEndereco,editContato;
    private ImageView imagePerfilEmpresa;

    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private DatabaseReference firebaseRef;
    private String idProduto;
    private String idUsuarioLogado;
    private String urlImagemSelecionada = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_admin);

        //configurações iniciais
        inicializarComponentes();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        idUsuarioLogado = UsuarioFirebase.getIdUsuario();


        //configuração tollbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagePerfilEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI

                        );
                if (i.resolveActivity(getPackageManager()) != null){

startActivityForResult(i,SELECAO_GALERIA);
                }
            }
        });


        recuperarDadosEmpresa();

    }

    private void recuperarDadosEmpresa(){

            DatabaseReference empresaref = firebaseRef.child("empresa").child(idUsuarioLogado);



            empresaref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        Empresa empresa = dataSnapshot.getValue(Empresa.class);

                        editEmpresaNome.setText(empresa.getNome());
                        editCategoria.setText(empresa.getCategoria());
                        editPrazoEntrega.setText(empresa.getTempo());
                        editEndereco.setText(empresa.getEndereco());
                        editContato.setText(empresa.getContato());

                       urlImagemSelecionada = empresa.getUrlImagem();

                        if( urlImagemSelecionada != "" && ! urlImagemSelecionada.isEmpty() ) {


                            Picasso.get()
                                    .load(urlImagemSelecionada)
                                    .error(R.drawable.logo)
                                    .placeholder(R.drawable.logo)
                                    .into(imagePerfilEmpresa);


                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    public  void validarDadosEmpresa(View view){

        String nome = editEmpresaNome.getText().toString();
        String categoria = editCategoria.getText().toString();
        String prazo = editPrazoEntrega.getText().toString();
        String endereco = editEndereco.getText().toString();
        String contato = editContato.getText().toString();

        if (!nome.isEmpty()){
            if (!categoria.isEmpty()){
                if (!prazo.isEmpty()){
                    if (!endereco.isEmpty()){
                        if (!contato.isEmpty()){


                            Empresa empresa = new Empresa();

                            empresa.setIdUsuario(idUsuarioLogado);
                            empresa.setNome(nome);
                            empresa.setCategoria(categoria);
                            empresa.setTempo(prazo);
                            empresa.setEndereco(endereco);
                            empresa.setContato(contato);
                            empresa.setUrlImagem(urlImagemSelecionada);
                            empresa.salvar();
                            finish();


                        }else {
                            exibirMensagem("Digite um nome para Empresa");

                        }


                    }else {
                        exibirMensagem("Digite o endereço ");

                    }

                }else {
                    exibirMensagem("Digite um prazo de entrega");

                }

            }else {
                exibirMensagem("Digite a categoria");

            }

        }else {
                 exibirMensagem("Digite um nome para Empresa");

        }


    }

    private void exibirMensagem(String texto){

        Toast.makeText(this, texto,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            Bitmap imagem = null;

            try {

                switch (requestCode){

                    case SELECAO_GALERIA:
                        Uri localImagem = data.getData();
                        imagem = MediaStore.Images
                    .Media
                    .getBitmap(

                            getContentResolver(),localImagem


                    );
                        break;
                }

                if (imagem != null){

                    imagePerfilEmpresa.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG,80,baos);

                    byte[] dadosImagem = baos.toByteArray();

                final StorageReference imagemRef = storageReference.child("Imagem").child("Empresa")
                        .child(idUsuarioLogado + "Jpeg");





                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfiguracoesAdminActivity.this,
                                    "Erro ao fazer o upload da imagem", Toast.LENGTH_SHORT)
                                    .show();


                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@androidx.annotation.NonNull Task<Uri> task) {

                                    Uri url = task.getResult();

                                    urlImagemSelecionada = String.valueOf(url);



                                }
                            });

                            Toast.makeText(ConfiguracoesAdminActivity.this, "Sucesso ao fazer upload da imagem",
                                    Toast.LENGTH_SHORT).show();

                                }
                            });


                        }
                    } catch (Exception e){

                e.printStackTrace();
            }
        }
    }



    private void inicializarComponentes(){

                editEmpresaNome = findViewById(R.id.editEmpresaNome);
                editCategoria = findViewById(R.id.editCategoria);
                editPrazoEntrega = findViewById(R.id.editPrazoEntrega);
                editEndereco = findViewById(R.id.editEndereco);
                 editContato = findViewById(R.id.editContato);
                 imagePerfilEmpresa = findViewById(R.id.imagePerfilEmpresa);


    }
}

