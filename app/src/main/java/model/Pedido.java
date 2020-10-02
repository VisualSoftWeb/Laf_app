package model;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import Helper.ConfiguracaoFirebase;

public class Pedido {


    private String idUsuario;
    private String idEmpresa;
    private String idPedido;
    private String nomeCompleto;
    private String cpfCnpj;
    private String cidade;
    private String bairro;
    private String rua;
    private String numEst;
    private String telefone;
    private String status = "Pendente";
    private List <ItemPedido> itens;
    private String observacao;
    private int periodoEntrega;

    public Pedido() {

    }

    public int getPeriodoEntrega() {
        return periodoEntrega;
    }

    public void setPeriodoEntrega(int periodoEntrega) {
        this.periodoEntrega = periodoEntrega;
    }

    public Pedido(String idUsu, String idEmp) {


        setIdUsuario(idUsu);
        setIdEmpresa(idEmp);


        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_clientes")
                .child(idEmp)
                .child(idUsu);

        setIdPedido(pedidoRef.push().getKey());

    }

    public void salvar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_clientes")
                .child(getIdEmpresa())
                .child(getIdUsuario());

        pedidoRef.setValue(this);


    }

    public void remover(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_clientes")
                .child(getIdEmpresa())
                .child(getIdUsuario());

        pedidoRef.removeValue();


    }


    public void confirmar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_confirmados")
                .child(getIdEmpresa())
                .child(getIdUsuario())
                .child(getIdPedido());

        pedidoRef.setValue(this);


    }

    public void atualizarStatus(){

        HashMap<String, Object> status = new HashMap<>();
        status.put("status",getStatus());

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos_finalizados")
                .child(getIdEmpresa())
                .child(getIdUsuario())
                .child(getIdPedido());


  pedidoRef.updateChildren(status);



    }



    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumEst() {
        return numEst;
    }

    public void setNumEst(String numEst) {
        this.numEst = numEst;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }


}
