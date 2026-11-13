
package sonria.ortodoncia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class pacienteDAO {

    // REGISTRAR PACIENTE
    public boolean insertar(Paciente paciente) {

        String sql = "INSERT INTO pacientes "
                + "(nombres, apellidos, documento, telefono, correo, "
                + "fecha_nacimiento, direccion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paciente.getNombres());
            ps.setString(2, paciente.getApellidos());
            ps.setString(3, paciente.getDocumento());
            ps.setString(4, paciente.getTelefono());
            ps.setString(5, paciente.getCorreo());

            if (paciente.getFechaNacimiento() != null) {
                ps.setDate(6, java.sql.Date.valueOf(
                        paciente.getFechaNacimiento()));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }

            ps.setString(7, paciente.getDireccion());

            int resultado = ps.executeUpdate();

            System.out.println("Registro realizado. Filas: " + resultado);

            return resultado > 0;

        } catch (SQLException e) {

            System.out.println("ERROR AL REGISTRAR PACIENTE:");
            e.printStackTrace();

            return false;
        }
    }

    // CONSULTAR PACIENTES
    public List<Paciente> consultar() {

        List<Paciente> pacientes = new ArrayList<>();

        String sql = "SELECT * FROM pacientes ORDER BY id_paciente ASC";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Paciente p = new Paciente();

                p.setIdPaciente(rs.getInt("id_paciente"));
                p.setNombres(rs.getString("nombres"));
                p.setApellidos(rs.getString("apellidos"));
                p.setDocumento(rs.getString("documento"));
                p.setTelefono(rs.getString("telefono"));
                p.setCorreo(rs.getString("correo"));

                java.sql.Date fecha = rs.getDate("fecha_nacimiento");

                if (fecha != null) {
                    p.setFechaNacimiento(fecha.toLocalDate());
                } else {
                    p.setFechaNacimiento(null);
                }

                p.setDireccion(rs.getString("direccion"));

                java.sql.Timestamp registro =
                        rs.getTimestamp("fecha_registro");

                if (registro != null) {
                    p.setFechaRegistro(registro.toLocalDateTime());
                }

                pacientes.add(p);
            }

        } catch (SQLException e) {

            System.out.println("ERROR AL CONSULTAR PACIENTES:");
            e.printStackTrace();
        }

        return pacientes;
    }

    // ACTUALIZAR PACIENTE
    public boolean actualizar(Paciente paciente) {

        String sql = "UPDATE pacientes SET "
                + "nombres=?, apellidos=?, documento=?, "
                + "telefono=?, correo=?, fecha_nacimiento=?, "
                + "direccion=? WHERE id_paciente=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paciente.getNombres());
            ps.setString(2, paciente.getApellidos());
            ps.setString(3, paciente.getDocumento());
            ps.setString(4, paciente.getTelefono());
            ps.setString(5, paciente.getCorreo());

            if (paciente.getFechaNacimiento() != null) {
                ps.setDate(6, java.sql.Date.valueOf(
                        paciente.getFechaNacimiento()));
            } else {
                ps.setNull(6, java.sql.Types.DATE);
            }

            ps.setString(7, paciente.getDireccion());
            ps.setInt(8, paciente.getIdPaciente());

            int resultado = ps.executeUpdate();

            System.out.println("Pacientes actualizados: " + resultado);

            return resultado > 0;

        } catch (SQLException e) {

            System.out.println("ERROR AL ACTUALIZAR PACIENTE:");
            e.printStackTrace();

            return false;
        }
    }

    // ELIMINAR PACIENTE
    public boolean eliminar(int idPaciente) {

        String sql = "DELETE FROM pacientes WHERE id_paciente=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPaciente);

            int resultado = ps.executeUpdate();

            System.out.println("Pacientes eliminados: " + resultado);

            return resultado > 0;

        } catch (SQLException e) {

            System.out.println("ERROR AL ELIMINAR PACIENTE:");
            e.printStackTrace();

            return false;
        }
    }
}
