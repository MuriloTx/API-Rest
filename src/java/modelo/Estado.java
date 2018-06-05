/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Murilo
 */
public class Estado 
{
    private String uf;
    private int quantidade;
    
    public Estado(String uf, int quantidade)
    {
        this.uf = uf;
        this.quantidade = quantidade;
    }
    
    public Estado()
    {
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }   
}
