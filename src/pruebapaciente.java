
package sonria.ortodoncia;

import java.util.List;

public class pruebapaciente {

    public static void main(String[] args) {

        pacienteDAO dao = new pacienteDAO();

        // Consultar pacientes registrados
        List<Paciente> pacientes = dao.consultar();

        System.out.println("LISTA DE PACIENTES");

        for (Paciente p : pacientes) {
            System.out.println(
                "ID: " + p.getIdPaciente()
                + " | Nombre: " + p.getNombres()
                + " " + p.getApellidos()
                + " | Documento: " + p.getDocumento()
            );
        }
    }
}
