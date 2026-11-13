
package sonria.ortodoncia;

import java.sql.*;

public class SonriaOrtodoncia {

    public static void main(String[] args) {

        String usuario = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/ortodoncia-estefany";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conexion = DriverManager.getConnection(
                url, usuario, password
            );

            System.out.println("Conexion exitosa con MySQL");

            Statement statement = conexion.createStatement();
            

            ResultSet rs = statement.executeQuery(
                "SELECT * FROM pacientes"
            );

            boolean hayPacientes = false;

            while (rs.next()) {

                hayPacientes = true;

                System.out.println("-------------------------");
                System.out.println("ID: " + rs.getInt("id_paciente"));
                System.out.println("Nombres: " + rs.getString("nombres"));
                System.out.println("Apellidos: " + rs.getString("apellidos"));
                System.out.println("Documento: " + rs.getString("documento"));
                System.out.println("Telefono: " + rs.getString("telefono"));
                System.out.println("Correo: " + rs.getString("correo"));
                System.out.println("Fecha nacimiento: " + rs.getDate("fecha_nacimiento"));
                System.out.println("Direccion: " + rs.getString("direccion"));
            }

            if (!hayPacientes) {
                System.out.println("No hay pacientes registrados.");
            }

            rs.close();
            statement.close();
            conexion.close();

        } catch (ClassNotFoundException | SQLException ex) {

            System.out.println("Error: " + ex.getMessage());

        }
    }
}
