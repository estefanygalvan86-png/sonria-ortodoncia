
package sonria.ortodoncia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class formulariopaciente extends JFrame {

    private JTextField txtId;
    private JTextField txtNombres;
    private JTextField txtApellidos;
    private JTextField txtDocumento;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtFechaNacimiento;
    private JTextField txtDireccion;

    private JTable tabla;
    private DefaultTableModel modelo;

    private final pacienteDAO dao = new pacienteDAO();

    public formulariopaciente() {

        setTitle("SONRIA ORTODONCIA - Gestion de Pacientes");
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        construirInterfaz();
        consultarPacientes(false);
    }

    // CONSTRUIR INTERFAZ
    private void construirInterfaz() {

        JPanel principal = new JPanel(new BorderLayout(15, 15));
        principal.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );
        principal.setBackground(new Color(240, 248, 248));

        JLabel titulo = new JLabel(
                "SONRIA ORTODONCIA",
                SwingConstants.CENTER
        );
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(new Color(0, 102, 102));

        JLabel subtitulo = new JLabel(
                "SISTEMA DE GESTION DE PACIENTES",
                SwingConstants.CENTER
        );
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 15));

        JPanel encabezado = new JPanel(new GridLayout(2, 1));
        encabezado.setBackground(new Color(240, 248, 248));
        encabezado.add(titulo);
        encabezado.add(subtitulo);

        principal.add(encabezado, BorderLayout.NORTH);

        // FORMULARIO
        JPanel formulario = new JPanel(new GridLayout(4, 4, 10, 12));
        formulario.setBorder(
                BorderFactory.createTitledBorder("Datos del paciente")
        );
        formulario.setBackground(Color.WHITE);

        txtId = new JTextField();
        txtId.setEditable(false);

        txtNombres = new JTextField();
        txtApellidos = new JTextField();
        txtDocumento = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtFechaNacimiento = new JTextField();
        txtDireccion = new JTextField();

        agregarCampo(formulario, "ID:", txtId);
        agregarCampo(formulario, "Nombres:", txtNombres);
        agregarCampo(formulario, "Apellidos:", txtApellidos);
        agregarCampo(formulario, "Documento:", txtDocumento);
        agregarCampo(formulario, "Telefono:", txtTelefono);
        agregarCampo(formulario, "Correo:", txtCorreo);
        agregarCampo(formulario, "Fecha nacimiento:", txtFechaNacimiento);
        agregarCampo(formulario, "Direccion:", txtDireccion);

        // BOTONES
        JButton btnRegistrar = new JButton("REGISTRAR");
        JButton btnConsultar = new JButton("CONSULTAR");
        JButton btnActualizar = new JButton("ACTUALIZAR");
        JButton btnEliminar = new JButton("ELIMINAR");
        JButton btnLimpiar = new JButton("LIMPIAR");

        btnRegistrar.setBackground(new Color(0, 128, 100));
        btnConsultar.setBackground(new Color(50, 120, 180));
        btnActualizar.setBackground(new Color(220, 160, 40));
        btnEliminar.setBackground(new Color(190, 60, 60));
        btnLimpiar.setBackground(new Color(100, 100, 100));

        JButton[] botones = {
            btnRegistrar, btnConsultar, btnActualizar,
            btnEliminar, btnLimpiar
        };

        for (JButton boton : botones) {
            boton.setForeground(Color.WHITE);
            boton.setFont(new Font("Arial", Font.BOLD, 12));
            boton.setFocusPainted(false);
            boton.setEnabled(true);
            boton.setOpaque(true);
        }

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(Color.WHITE);

        for (JButton boton : botones) {
            panelBotones.add(boton);
        }

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.setBackground(Color.WHITE);
        panelSuperior.add(formulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // TABLA
        modelo = new DefaultTableModel(
                new Object[]{
                    "ID", "Nombres", "Apellidos",
                    "Documento", "Telefono", "Correo",
                    "Fecha nacimiento", "Direccion"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        tabla.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 12)
        );

        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.setBorder(
                BorderFactory.createTitledBorder("Pacientes registrados")
        );

        JPanel centro = new JPanel(new BorderLayout(10, 10));
        centro.setBackground(new Color(240, 248, 248));
        centro.add(panelSuperior, BorderLayout.NORTH);
        centro.add(desplazamiento, BorderLayout.CENTER);

        principal.add(centro, BorderLayout.CENTER);
        setContentPane(principal);

        // ACCIONES DE LOS BOTONES
        btnRegistrar.addActionListener(e -> registrarPaciente());

        btnConsultar.addActionListener(e -> consultarPacientes(true));

        btnActualizar.addActionListener(e -> actualizarPaciente());

        btnEliminar.addActionListener(e -> eliminarPaciente());

        btnLimpiar.addActionListener(e -> limpiarCampos());

        // SELECCIONAR PACIENTE DE LA TABLA
        tabla.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()
                    && tabla.getSelectedRow() != -1) {

                cargarPacienteSeleccionado();
            }
        });
    }

    // AGREGAR CAMPOS
    private void agregarCampo(
            JPanel panel, String nombre, JTextField campo) {

        panel.add(new JLabel(nombre));
        panel.add(campo);
    }

    // OBTENER DATOS
    private Paciente obtenerDatos() {

        String nombres = txtNombres.getText().trim();
        String apellidos = txtApellidos.getText().trim();
        String documento = txtDocumento.getText().trim();

        if (nombres.isEmpty()
                || apellidos.isEmpty()
                || documento.isEmpty()) {

            throw new IllegalArgumentException(
                    "Los nombres, apellidos y documento son obligatorios."
            );
        }

        String fechaTexto = txtFechaNacimiento.getText().trim();
        LocalDate fechaNacimiento = null;

        if (!fechaTexto.isEmpty()) {

            try {
                fechaNacimiento = LocalDate.parse(fechaTexto);
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "La fecha debe tener el formato AAAA-MM-DD."
                );
            }
        }

        return new Paciente(
                nombres,
                apellidos,
                documento,
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                fechaNacimiento,
                txtDireccion.getText().trim()
        );
    }

    // REGISTRAR PACIENTE
    private void registrarPaciente() {

        try {

            Paciente paciente = obtenerDatos();

            int confirmar = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea registrar este paciente?",
                    "Confirmar registro",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmar != JOptionPane.YES_OPTION) {
                return;
            }

            if (dao.insertar(paciente)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Paciente registrado correctamente."
                );

                limpiarCampos();
                consultarPacientes(false);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo registrar el paciente. "
                        + "Revise la consola de NetBeans.",
                        "Error de registro",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Datos incorrectos",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // CONSULTAR PACIENTES
    private void consultarPacientes(boolean mostrarMensaje) {

        try {

            List<Paciente> pacientes = dao.consultar();

            modelo.setRowCount(0);

            for (Paciente p : pacientes) {

                modelo.addRow(new Object[]{
                    p.getIdPaciente(),
                    p.getNombres(),
                    p.getApellidos(),
                    p.getDocumento(),
                    p.getTelefono(),
                    p.getCorreo(),
                    p.getFechaNacimiento() == null
                            ? ""
                            : p.getFechaNacimiento(),
                    p.getDireccion()
                });
            }

            if (mostrarMensaje) {

                JOptionPane.showMessageDialog(
                        this,
                        "Consulta realizada correctamente.\n"
                        + "Total de pacientes encontrados: "
                        + pacientes.size(),
                        "Consulta de pacientes",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Error al consultar pacientes:\n"
                    + e.getMessage(),
                    "Error de consulta",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // ACTUALIZAR PACIENTE
    private void actualizarPaciente() {

        try {

            if (txtId.getText().isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Seleccione un paciente de la tabla."
                );
                return;
            }

            Paciente paciente = obtenerDatos();

            paciente.setIdPaciente(
                    Integer.parseInt(txtId.getText())
            );

            int confirmar = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea actualizar los datos del paciente?",
                    "Confirmar actualizacion",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmar != JOptionPane.YES_OPTION) {
                return;
            }

            if (dao.actualizar(paciente)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Paciente actualizado correctamente."
                );

                limpiarCampos();
                consultarPacientes(false);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No se pudo actualizar el paciente. "
                        + "Revise la consola de NetBeans."
                );
            }

        } catch (IllegalArgumentException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage()
            );
        }
    }

    // ELIMINAR PACIENTE
    private void eliminarPaciente() {

        if (txtId.getText().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un paciente de la tabla."
            );
            return;
        }

        int id = Integer.parseInt(txtId.getText());

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                "¿Esta seguro de eliminar el paciente con ID " + id + "?",
                "Confirmar eliminacion",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.eliminar(id)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Paciente eliminado correctamente."
            );

            limpiarCampos();
            consultarPacientes(false);

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el paciente. "
                    + "Revise la consola de NetBeans."
            );
        }
    }

    // CARGAR PACIENTE SELECCIONADO
    private void cargarPacienteSeleccionado() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            return;
        }

        txtId.setText(modelo.getValueAt(fila, 0).toString());
        txtNombres.setText(modelo.getValueAt(fila, 1).toString());
        txtApellidos.setText(modelo.getValueAt(fila, 2).toString());
        txtDocumento.setText(modelo.getValueAt(fila, 3).toString());

        txtTelefono.setText(valorTexto(modelo.getValueAt(fila, 4)));
        txtCorreo.setText(valorTexto(modelo.getValueAt(fila, 5)));
        txtFechaNacimiento.setText(valorTexto(modelo.getValueAt(fila, 6)));
        txtDireccion.setText(valorTexto(modelo.getValueAt(fila, 7)));
    }

    private String valorTexto(Object valor) {
        return valor == null ? "" : valor.toString();
    }

    // LIMPIAR FORMULARIO
    private void limpiarCampos() {

        txtId.setText("");
        txtNombres.setText("");
        txtApellidos.setText("");
        txtDocumento.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtFechaNacimiento.setText("");
        txtDireccion.setText("");

        tabla.clearSelection();
    }

    // INICIAR APLICACION
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            formulariopaciente formulario =
                    new formulariopaciente();

            formulario.setVisible(true);
        });
    }
}
