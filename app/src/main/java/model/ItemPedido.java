package model;

public class ItemPedido {

   private String idProduto;
    private String nomeProduto;
    private int quantidade;
    private String fermentacao;


    public ItemPedido() {
    }

    public String getFermentacao() {
        return fermentacao;
    }

    public void setFermentacao(String fermentacao) {
        this.fermentacao = fermentacao;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
