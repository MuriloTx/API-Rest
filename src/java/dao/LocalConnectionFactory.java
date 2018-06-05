/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Murilo
 */
public class LocalConnectionFactory 
{
    
    public static Connection create_connection()
    {
        String stringDeConexao = "jdbc:mysql://localhost/cidades?useTimezone=true&serverTimezone=UTC&useSSL=false";
        String usuario = "root";
        String senha = "1qazxsw2";
        
        Connection conexao = null;
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexao = DriverManager.getConnection(stringDeConexao, usuario, senha);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        return conexao;
    }
}
