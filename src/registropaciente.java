
package sonria.ortodoncia;

import java.time.LocalDate;
import java.util.Scanner;

public class registropaciente {

    public static void main(String[] args) {

        Scanner teclado = new Scanner(System.in);
        pacienteDAO dao = new pacienteDAO();

        System.out.println("================================");
        System.out.println("   SONRIA ORTODONCIA");
        System.out.println("     REGISTRO DE PACIENTES");
        System.out.println("================================");

        System.out.print("Nombres: ");
        String nombres = teclado.nextLine();

        System.out.print("Apellidos: ");
        String apellidos = teclado.nextLine();

        System.out.print("Documento: ");
        String documento = teclado.nextLine();

        System.out.print("Telefono: ");
        String telefono = teclado.nextLine();

        System.out.print("Correo: ");
        String correo = teclado.nextLine();

        System.out.print("Fecha de nacimiento (AAAA-MM-DD): ");
        LocalDate fechaNacimiento =
                LocalDate.parse(teclado.nextLine());

        System.out.print("Direccion: ");
        String direccion = teclado.nextLine();

        Paciente paciente = new Paciente(
                nombres,
                apellidos,
                documento,
                telefono,
                correo,
                fechaNacimiento,
                direccion
        );

        if (dao.insertar(paciente)) {
            System.out.println("\nPaciente registrado correctamente.");
        } else {
            System.out.println("\nNo fue posible registrar el paciente.");
        }

        teclado.close();
    }
}
