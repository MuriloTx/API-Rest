/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.CidadeDAO;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import modelo.Cidade;
import modelo.Estado;

/**
 * REST Web Service
 *
 * @author Murilo
 */
@Path("WS")
public class CidadesWS {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CidadesWS
     */
    public CidadesWS() {
    }

    /**
     * Retrieves representation of an instance of ws.CidadesWS
     * @return an instance of java.lang.String
     */

    //Faz a importação da planilha para o banco de dados
    @GET
    @Produces("application/json")
    @Path("Importar")
    public String importar()
    {
        CidadeDAO dao = new CidadeDAO();
        
        boolean importou = dao.importar("lista_cidades.csv");
        
        Gson g = new Gson();
        
        if (importou)
        {
            return g.toJson(new String("Importação com sucesso"));
        }
        else
        {
            return g.toJson(new String("Importação falhou"));
        }
    }
    
    //Retorna apenas as cidades que são capitais ordenadas por nome
    @GET
    @Produces("application/json")
    @Path("Capitais")
    public String listaCapitais()
    {
        List<Cidade> lista_cidades;
        
        CidadeDAO dao = new CidadeDAO();
        
        lista_cidades = dao.listarCapitais();
               
        //Converter para Gson
        Gson g = new Gson();
        
        return g.toJson(lista_cidades);
    }
    
    //Retorna o nome do maior estado e a quantidade de cidades
    @GET
    @Produces("application/json")
    @Path("MaiorEstado")
    public String buscaMaiorEstado()
    {
        JsonObject resultado;
        
        CidadeDAO dao = new CidadeDAO();
        
        resultado = dao.buscarMaiorEstado();
               
        //Converter para Gson
        Gson g = new Gson();
        
        return g.toJson(resultado);
    }
    
    //Retorna o nome do menor estado e a quantidade de cidades
    @GET
    @Produces("application/json")
    @Path("MenorEstado")
    public String buscaMenorEstado()
    {
        JsonObject resultado;
        
        CidadeDAO dao = new CidadeDAO();
        
        resultado = dao.buscarMenorEstado();
               
        //Converter para Gson
        Gson g = new Gson();
        
        return g.toJson(resultado);
    }
    
    //Retorna a quantidade de cidades por estado
    @GET
    @Produces("application/json")
    @Path("Estados")
    public String listaEstados()
    {
        List<JsonObject> resultado;
        
        CidadeDAO dao = new CidadeDAO();
        
        resultado = dao.listarEstados();

        //Converter para Gson
        Gson g = new Gson();
        
        return g.toJson(resultado);
    }
     
    //Retona os dados da cidade com base no id do ibge
    @GET
    @Produces("application/json")
    @Path("Cidades/{id}")
    public String getCidade(@PathParam("id") String id)
    {
        Cidade cidade;
        
        CidadeDAO dao = new CidadeDAO();
        
        cidade = dao.buscarPorID(id);

        //Converter para Gson
        Gson g = new Gson();
        
        return g.toJson(cidade);
    }
    
    //Retorna os nomes das cidades de um estado
    @GET
    @Produces("application/json")
    @Path("CidadesDoEstado/{uf}")
    public String listaCidadesDoEstado(@PathParam("uf") String uf)
    {
        List<String> resultado;
        
        CidadeDAO dao = new CidadeDAO();
        
        resultado = dao.listarCidadesDoEstado(uf);

        //Converter para Gson
        Gson g = new Gson();
        
        return g.toJson(resultado);
    }
    
    //Retorna todas as cidades
    @GET
    @Produces("application/json")
    @Path("Cidades")
    public String listCidades()
    {
        List<Cidade> lista_cidades;
        
        CidadeDAO dao = new CidadeDAO();
        
        lista_cidades = dao.listar();
               
        //Converter para Gson
        Gson g = new Gson();
        
        return g.toJson(lista_cidades);
    }
    
    //Adiciona uma cidade
    @POST
    @Consumes("application/json")
    @Path("Cidades/adicionar")
    public String adicionaCidade(String conteudo)
    {  
        CidadeDAO dao = new CidadeDAO();
               
        Gson g = new Gson();
        
        Cidade cidade = (Cidade) g.fromJson(conteudo, Cidade.class );
        
        dao.insereCidade(cidade);
        
        return g.toJson(cidade);
    }
    
    //Deleta uma cidade
    @DELETE
    @Path("Cidades/excluir/{id}")
    public void excluir(@PathParam("id") String id)
    {
        CidadeDAO dao = new CidadeDAO();
        
        Gson g = new Gson();
        
        dao.excluir(id);
    }
    
    //Retorna as cidades com base no filtro de uma coluna
    @GET
    @Produces("application/json")
    @Path("{coluna}/{texto}")
    public String buscarPorColuna(@PathParam("coluna") String coluna, @PathParam("texto") String texto)
    {
        List<Cidade> resultado;
        
        CidadeDAO dao = new CidadeDAO();
        
        resultado = dao.buscarCidadesPorColuna(coluna, texto);

        //Converter para Gson
        Gson g = new Gson();
        
        return g.toJson(resultado);
    }
    
    //Retorna a quantidade de cidades com base no filtro de uma coluna
    @GET
    @Produces("application/json")
    @Path("Quantidade/{coluna}/{texto}")
    public String quantidadePorColuna(@PathParam("coluna") String coluna, @PathParam("texto") String texto)
    {
        int resultado;
        
        CidadeDAO dao = new CidadeDAO();
        
        resultado = dao.quantidadePorColuna(coluna, texto);

        //Converter para Gson
        Gson g = new Gson();
        
        return g.toJson(resultado);
    }
    
    //Retorna a quantidade total de cidades
    @GET
    @Produces("application/json")
    @Path("Quantidade")
    public String quantidadeTotal()
    {
        CidadeDAO dao = new CidadeDAO();
        
        List<Cidade> lista_cidades = dao.listar();
       
        //Converter para Gson
        Gson g = new Gson();
        
        return g.toJson(String.valueOf(lista_cidades.size()));
    }
            
    /**
     * PUT method for updating or creating an instance of CidadesWS
     * @param content representation for the resource
     */
    @PUT
    //@Consumes(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
