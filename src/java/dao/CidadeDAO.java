/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import modelo.Cidade;
import modelo.Estado;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 *
 * @author Murilo
 */
public class CidadeDAO
{
    private Connection conexaoLocal;
        
    public CidadeDAO()
    {
        conexaoLocal = LocalConnectionFactory.create_connection();
    }
    
    public void encerraConexaoLocal()
    {
        try {
            conexaoLocal.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    //Insere uma cidade
    public boolean insereCidade(Cidade cidade)
    {
        try {
            
            String sql = "INSERT INTO cidade(ibge_id,uf,name,capital,lon,lat,no_accents,alternative_names,microregion,mesoregion) VALUES (?,?,?,?,?,?,?,?,?,?)";
            
            PreparedStatement comando = conexaoLocal.prepareStatement(sql);
            
            comando.setString(1,cidade.getIbge_id());
            comando.setString(2,cidade.getUf() );
            comando.setString(3,cidade.getName() );
            comando.setString(4,cidade.getCapital() );
            comando.setString(5,cidade.getLon() );
            comando.setString(6,cidade.getLat() );
            comando.setString(7,cidade.getNo_accents());
            comando.setString(8,cidade.getAlternative_names() );
            comando.setString(9,cidade.getMicroregion() );
            comando.setString(10,cidade.getMesoregion() );
            
            comando.execute();
            
            comando.close();
            
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            return false;
        }
    }
    
    //Busca uma cidade com base no id do IBGE
    public Cidade buscarPorID(String id)
    {
        try {
            
            String sql = "SELECT * from cidade where ibge_id = " + id;
            
            PreparedStatement comando = conexaoLocal.prepareStatement(sql);
            
            ResultSet resultado = comando.executeQuery();
                      
            if (resultado.next())
            {
                Cidade cidade = new Cidade(resultado.getString("ibge_id"), resultado.getString("uf"), resultado.getString("name"), resultado.getString("capital"), resultado.getString("lon"), resultado.getString("lat"), resultado.getString("no_accents"), resultado.getString("alternative_names"), resultado.getString("microregion"), resultado.getString("mesoregion")); 
           
                comando.close();
                resultado.close();
                
                return cidade;
            }
            else
            {
                comando.close();
                resultado.close();
                
                return null;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            return null;
        }
    }
    
    //Retorna a lista com todas as cidades
    public List<Cidade> listar()
    {
        try {
            
            String sql = "SELECT * FROM cidade";
            
            PreparedStatement comando = conexaoLocal.prepareStatement(sql);
            
            ResultSet resultado = comando.executeQuery();
            
            List<Cidade> lista_cidades = new ArrayList<Cidade>();
            
            Cidade cidade;
            
            while (resultado.next())
            {
                cidade = new Cidade(resultado.getString("ibge_id"), resultado.getString("uf"), resultado.getString("name"), resultado.getString("capital"), resultado.getString("lon"), resultado.getString("lat"), resultado.getString("no_accents"), resultado.getString("alternative_names"), resultado.getString("microregion"), resultado.getString("mesoregion"));
                
                lista_cidades.add(cidade);
            }
            
            comando.close();
            resultado.close();
            
            return lista_cidades;
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            return null;
        }
    }
    
    //Retorna apenas as cidades que são capitais
    public List<Cidade> listarCapitais()
    {
        try {
            
            String sql = "SELECT * FROM cidade WHERE capital <> '' ORDER BY name";
            
            PreparedStatement comando = conexaoLocal.prepareStatement(sql);
            
            ResultSet resultado = comando.executeQuery();
            
            List<Cidade> lista_cidades = new ArrayList<Cidade>();
            
            Cidade cidade;
            
            while (resultado.next())
            {
                cidade = new Cidade(resultado.getString("ibge_id"), resultado.getString("uf"), resultado.getString("name"), resultado.getString("capital"), resultado.getString("lon"), resultado.getString("lat"), resultado.getString("no_accents"), resultado.getString("alternative_names"), resultado.getString("microregion"), resultado.getString("mesoregion"));
                
                lista_cidades.add(cidade);
            }
            
            comando.close();
            resultado.close();
            
            return lista_cidades;
            
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            return null;
        }
    }
    
    //Remove uma cidade com base no id do IBGE
    public boolean excluir(String id)
    {
        try {
            
            String sql = "DELETE FROM cidade WHERE ibge_id = " + id;
            
            PreparedStatement comando = conexaoLocal.prepareStatement(sql);
            
            comando.execute();
            
            comando.close();
            
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            return false;
        }
    }
    
    //Importa um arquivo excel para dentro do banco
    public boolean importar(String arquivo)
    {
        try {
            
            String sql = "LOAD DATA INFILE '"+arquivo+"' \n" +
                         "INTO TABLE cidade \n" +
                         "FIELDS TERMINATED BY ',' \n" +
                         "LINES TERMINATED BY '\\n' \n" +
                         "IGNORE 1 LINES \n" +
                         "(ibge_id,uf,name,capital,lon,lat,no_accents,alternative_names,microregion,mesoregion);";
            
            PreparedStatement comando = conexaoLocal.prepareStatement(sql);
            
            comando.execute();
            
            comando.close();
            
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            return false;
        }
    }
    
    //Retorna o estado com o maior número de cidades e também a quantidade de cidades
    public JsonObject buscarMaiorEstado()
    {
        try {
            
            String sql = "select uf, count(*) as quantidade\n" +
                         "from cidade\n" +
                         "group by uf\n" +
                         "order by quantidade desc\n" +
                         "limit 1;";
            
            PreparedStatement comando = conexaoLocal.prepareStatement(sql);
            
            ResultSet resultado = comando.executeQuery();
            
            JsonObject obj;
                        
            if ( resultado.next() )
            {
                obj = new JsonObject();

                obj.addProperty("uf", resultado.getString("uf"));
                obj.addProperty("quantidade", resultado.getInt("quantidade"));
                
                comando.close();
                resultado.close();
                
                return obj;
            }
            else
            {
                comando.close();
                resultado.close();
                
                return null;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            return null;
        }
    }
    
    //Retorna o estado com o menor número de cidades e também a quantidade de cidades
    public JsonObject buscarMenorEstado()
    {
        try {
            
            String sql = "select uf, count(*) as quantidade\n" +
                         "from cidade\n" +
                         "group by uf\n" +
                         "order by quantidade \n" +
                         "limit 1;";
            
            PreparedStatement comando = conexaoLocal.prepareStatement(sql);
            
            ResultSet resultado = comando.executeQuery();
            
            JsonObject obj;
            
            if ( resultado.next() )
            {                
                obj = new JsonObject();

                obj.addProperty("uf", resultado.getString("uf"));
                obj.addProperty("quantidade", resultado.getInt("quantidade"));
                
                comando.close();
                resultado.close();
                
                return obj;
            }
            else
            {
                comando.close();
                resultado.close();
                
                return null;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            return null;
        }
    }
    
    //Retorna a relação dos estados e a quantidade de cidades em cada um
    public List<JsonObject> listarEstados()
    {
        try {
            
            String sql = "select uf, count(*) as quantidade\n" +
                         "from cidade\n" +
                         "group by uf\n" +
                         "order by uf;";
            
            PreparedStatement comando = conexaoLocal.prepareStatement(sql);
            
            ResultSet resultado = comando.executeQuery();
                        
            JsonObject obj = new JsonObject();
            
            List<JsonObject> lista_estados = new ArrayList<JsonObject>();
            
            while (resultado.next())
            {                        
                obj = new JsonObject();

                obj.addProperty("uf", resultado.getString("uf"));
                obj.addProperty("quantidade", resultado.getInt("quantidade"));

                lista_estados.add(obj);
            }
            
            comando.close();
            resultado.close();
            
            return lista_estados;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            return null;
        }
    }
    
    //Retorna todas as cidades de um certo estado
    public List<String> listarCidadesDoEstado(String uf)
    {
        try {
            
            String sql = "select name from cidade where uf = ?";
            
            PreparedStatement comando = conexaoLocal.prepareStatement(sql);
            
            comando.setString(1, uf);
            
            ResultSet resultado = comando.executeQuery();
            
            List<String> cidades = new ArrayList<String>();
            
            while (resultado.next())
            {
                cidades.add(resultado.getString("name"));
            }
            
            comando.close();
            resultado.close();
            
            return cidades;  
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            return null;
        }
    }
    
    //Retorna todas as cidades de acordo com o valor de uma das colunas do excel
    public List<Cidade> buscarCidadesPorColuna(String coluna, String texto)
    {
        try {
            
            String sql = "select * from cidade where " + coluna + " = ?";
            
            PreparedStatement comando = conexaoLocal.prepareStatement(sql);
            
            comando.setString(1, texto);
            
            ResultSet resultado = comando.executeQuery();
            
            List<Cidade> lista_cidades = new ArrayList<Cidade>();
            
            Cidade cidade;
            
            while (resultado.next())
            {
                cidade = new Cidade(resultado.getString("ibge_id"), resultado.getString("uf"), resultado.getString("name"), resultado.getString("capital"), resultado.getString("lon"), resultado.getString("lat"), resultado.getString("no_accents"), resultado.getString("alternative_names"), resultado.getString("microregion"), resultado.getString("mesoregion")); 
                
                lista_cidades.add(cidade);
            }
            
            comando.close();
            resultado.close();
            
            return lista_cidades;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            return null;
        }
    }
    
    //Retorna a quantidade de cidades de acordo com o valor de uma das colunas do excel
    public int quantidadePorColuna(String coluna, String texto)
    {
        try {
            
            String sql = "select * from cidade where " + coluna + " = ?";
            
            PreparedStatement comando = conexaoLocal.prepareStatement(sql);
            
            comando.setString(1, texto);
            
            ResultSet resultado = comando.executeQuery();
                        
            int quantidade = 0;
            
            while (resultado.next())
            {
                quantidade++;
            }
            
            comando.close();
            resultado.close();
            
            return quantidade;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            
            return -1;
        }
    }
            
}
