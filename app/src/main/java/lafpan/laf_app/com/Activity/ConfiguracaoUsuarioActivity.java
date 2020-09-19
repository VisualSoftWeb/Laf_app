package lafpan.laf_app.com.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import Helper.ConfiguracaoFirebase;
import Helper.UsuarioFirebase;
import dmax.dialog.SpotsDialog;
import lafpan.laf_app.com.splashactivity.R;
import model.Usuario;

public class ConfiguracaoUsuarioActivity extends AppCompatActivity {

    private EditText editNomeCompleto , editCpfCnpj,editCidade,
            editBairro,editRua,editN,editContatoTelefonico;

    private String idUsuario;
    private AlertDialog dialog;

    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao_usuario);

        //Configuração iniciais
        inicializarComponentes();
        idUsuario = UsuarioFirebase.getIdUsuario();
        firebaseRef = ConfiguracaoFirebase.getFirebase();

        //configuração tollbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações usuário");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recupera dados do Cliente
        recuperarDadosUsuario();



        // criando mascara para CPF/CNPJ e telefone
        SimpleMaskFormatter smfTelefone = new SimpleMaskFormatter("(NN)N.NNNN-NNNN");
        MaskTextWatcher mtwTelefone = new MaskTextWatcher(editContatoTelefonico,smfTelefone);

        editContatoTelefonico.addTextChangedListener(mtwTelefone);

        SimpleMaskFormatter smfCPF = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtwCPF = new MaskTextWatcher(editCpfCnpj,smfCPF);

        editCpfCnpj.addTextChangedListener(mtwCPF);



    }

    private void recuperarDadosUsuario(){


        //loading de dados
        dialog = new SpotsDialog.Builder().setContext(this)
                .setMessage("Carregando dados")
                .setCancelable(false)
                .build();
        dialog.show();

        // recuperar as informações do cliente

        DatabaseReference usuarioRef = firebaseRef
                .child("clientes").child(idUsuario);

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot.getValue() != null){

                     Usuario usuario = dataSnapshot.getValue(Usuario.class);
                     editNomeCompleto.setText(usuario.getNomeCompleto());
                     editCpfCnpj.setText(usuario.getCpfCnpj());
                     editCidade.setText(usuario.getCidade());
                     editBairro.setText(usuario.getBairro());
                     editRua.setText(usuario.getRua());
                     editN.setText(usuario.getNumEst());
                     editContatoTelefonico.setText(usuario.getTelefone());



                 }
                 carregarDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public  void validarDadosUsuario(View view){
 //valida se os campos foram preenchidos do cliente
        String nomeCompleto = editNomeCompleto.getText().toString();
        String cpfCnpj = editCpfCnpj.getText().toString();
        String cidade = editCidade.getText().toString();
        String bairro = editBairro.getText().toString();
        String rua = editRua.getText().toString();
        String numEst = editN.getText().toString();
        String telefone = editContatoTelefonico.getText().toString();

        if (!nomeCompleto.isEmpty()){
            if (!cpfCnpj.isEmpty()){
                if (!cidade.isEmpty()){
                    if (!bairro.isEmpty()){
                        if (!rua.isEmpty()){
                            if (!numEst.isEmpty()){
                                if (!telefone.isEmpty()){


                                    Usuario usuario = new Usuario();

                                    usuario.setIdUsuario(idUsuario);
                                    usuario.setNomeCompleto(nomeCompleto);
                                    usuario.setCpfCnpj(cpfCnpj);
                                    usuario.setCidade(cidade);
                                    usuario.setBairro(bairro);
                                    usuario.setRua(rua);
                                    usuario.setNumEst(numEst);
                                    usuario.setTelefone(telefone);
                                    usuario.salvar();

                                    exibirMensagem("Dados cadastrados com sucesso!");

                                    finish();


                                }else {
                                    exibirMensagem("Digite o numero telefone");

                                }


                            }else {
                                exibirMensagem("Número do estabelecimento");

                            }

                        }else {
                            exibirMensagem("Digite o nome da rua");

                        }

                    }else {
                        exibirMensagem("Digite o Bairro");

                    }

                }else {
                    exibirMensagem("Digite a cidade");

                }

            }else {
                exibirMensagem("Digite o CPF ou CNPJ");

            }

        }else {
            exibirMensagem("Digite o nome completo");

        }

    }
    private void exibirMensagem(String texto){

        Toast.makeText(this, texto,Toast.LENGTH_SHORT).show();
    }

    private void carregarDados(){
        dialog.dismiss();

    }

    private void inicializarComponentes(){

        editNomeCompleto = findViewById(R.id.editNomeCompleto);
        editCpfCnpj = findViewById(R.id.editCpfCnpj);
        editCidade = findViewById(R.id.editCidade);
        editBairro = findViewById(R.id.editBairro);
        editRua = findViewById(R.id.editRua);
        editN = findViewById(R.id.editN);
        editContatoTelefonico=findViewById(R.id.editContatoTelefonico);

    }



}