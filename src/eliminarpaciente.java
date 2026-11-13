
package sonria.ortodoncia;

import java.util.Scanner;

public class eliminarpaciente {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        pacienteDAO dao = new pacienteDAO();

        System.out.println("SONRIA ORTODONCIA");
        System.out.println("ELIMINAR PACIENTE");

        System.out.print("Ingrese el ID del paciente: ");
        int id = teclado.nextInt();

        System.out.print("¿Esta seguro de eliminarlo? (1=SI / 2=NO): ");
        int opcion = teclado.nextInt();

        if (opcion == 1) {

            if (dao.eliminar(id)) {
                System.out.println("Paciente eliminado correctamente.");
            } else {
                System.out.println("No se encontro el paciente.");
            }

        } else {
            System.out.println("Operacion cancelada.");
        }

        teclado.close();
    }
}
