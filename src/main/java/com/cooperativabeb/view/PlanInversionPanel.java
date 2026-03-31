package com.cooperativabeb.view;

import com.cooperativabeb.dao.ClienteDAO;
import com.cooperativabeb.dao.PlanInversionDAO;
import com.cooperativabeb.dao.ProductoFinancieroDAO;
import com.cooperativabeb.model.Cliente;
import com.cooperativabeb.model.PlanInversion;
import com.cooperativabeb.model.ProductoFinanciero;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PlanInversionPanel extends JPanel {

    private PlanInversionDAO planDAO = new PlanInversionDAO();
    private ProductoFinancieroDAO productoDAO = new ProductoFinancieroDAO();
    private ClienteDAO clienteDAO = new ClienteDAO();

    private JTable tablaPlanes;
    private JComboBox<Cliente> cmbCliente;
    private JComboBox<ProductoFinanciero> cmbProducto;
    private JTextField txtMonto;
    private JLabel lblTasa, lblPlazo, lblGanancia, lblMontoFinal;




    public void refrescar() {
        cargarDatos();
    }



    public PlanInversionPanel() {
        initComponents();
        cargarDatos();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel tabla planes
        String[] cols = {"ID", "Cliente ID", "Producto", "Monto invertido",
                         "Tasa %", "Plazo", "Vencimiento", "Ganancia est.", "Estado"};
        tablaPlanes = new JTable(new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        tablaPlanes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaPlanes.setRowHeight(28);
        tablaPlanes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaPlanes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Panel nuevo plan
        JPanel panelNuevo = new JPanel(new GridBagLayout());
        panelNuevo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            "Contratar nuevo plan de inversion",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(100, 149, 237)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbCliente  = new JComboBox<>();
        cmbProducto = new JComboBox<>();
        txtMonto    = new JTextField(15);
        txtMonto.setPreferredSize(new Dimension(0, 32));
        txtMonto.putClientProperty("JTextField.placeholderText", "Monto a invertir");

        lblTasa      = new JLabel("Tasa: —");
        lblPlazo     = new JLabel("Plazo: —");
        lblGanancia  = new JLabel("Ganancia estimada: —");
        lblMontoFinal = new JLabel("Monto final: —");

        lblGanancia.setForeground(new Color(80, 200, 120));
        lblMontoFinal.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMontoFinal.setForeground(new Color(100, 149, 237));

        Font fLbl = new Font("Segoe UI", Font.PLAIN, 13);

        Object[][] filas = {
            {"Cliente:", cmbCliente},
            {"Producto:", cmbProducto},
            {"Monto (S/.):", txtMonto},
        };

        for (int i = 0; i < filas.length; i++) {
            gbc.gridx=0; gbc.gridy=i; gbc.weightx=0.3;
            JLabel l = new JLabel(filas[i][0].toString()); l.setFont(fLbl);
            panelNuevo.add(l, gbc);
            gbc.gridx=1; gbc.weightx=0.7;
            panelNuevo.add((Component) filas[i][1], gbc);
        }

        // Info del producto seleccionado
        JPanel panelInfo = new JPanel(new GridLayout(2, 2, 10, 4));
        panelInfo.setBorder(new EmptyBorder(5, 0, 5, 0));
        panelInfo.add(lblTasa);
        panelInfo.add(lblPlazo);
        panelInfo.add(lblGanancia);
        panelInfo.add(lblMontoFinal);

        gbc.gridx=0; gbc.gridy=3; gbc.gridwidth=2;
        panelNuevo.add(panelInfo, gbc);

        JButton btnSimular = new JButton("Simular");
        JButton btnContratar = new JButton("Contratar plan");
        btnContratar.setBackground(new Color(100, 149, 237));
        btnContratar.setForeground(Color.WHITE);
        btnContratar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnContratar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimular.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel panelBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelBtns.add(btnSimular);
        panelBtns.add(btnContratar);

        gbc.gridx=0; gbc.gridy=4; gbc.gridwidth=2;
        panelNuevo.add(panelBtns, gbc);

        // Panel inferior con tabla y botones
        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            "Planes de inversion activos",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(100, 149, 237)));

        JPanel panelTabBtns = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JButton btnRefrescar = new JButton("Refrescar");
        JButton btnCancelar  = new JButton("Cancelar plan");
        btnCancelar.setForeground(Color.RED);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefrescar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelTabBtns.add(btnRefrescar);
        panelTabBtns.add(btnCancelar);

        panelTabla.add(new JScrollPane(tablaPlanes), BorderLayout.CENTER);
        panelTabla.add(panelTabBtns, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelNuevo, panelTabla);
        split.setDividerLocation(240);
        split.setResizeWeight(0.4);

        add(split, BorderLayout.CENTER);

        // Acciones
        cmbProducto.addActionListener(e -> actualizarInfoProducto());
        txtMonto.addActionListener(e -> simular());
        btnSimular.addActionListener(e -> simular());

        btnContratar.addActionListener(e -> {
            if (cmbCliente.getSelectedItem() == null ||
                cmbProducto.getSelectedItem() == null ||
                txtMonto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }
            double monto;
            try {
                monto = Double.parseDouble(txtMonto.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Monto invalido"); return;
            }
            Cliente cliente = (Cliente) cmbCliente.getSelectedItem();
            ProductoFinanciero prod = (ProductoFinanciero) cmbProducto.getSelectedItem();

            if (monto < prod.getMontoMinimo()) {
                JOptionPane.showMessageDialog(this,
                    "Monto minimo para este producto: S/. " + prod.getMontoMinimo());
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Contratar %s por S/. %.2f para %s?",
                    prod.getNombre(), monto, cliente.getNombreCompleto()),
                "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (planDAO.contratar(cliente.getIdCliente(),
                        prod.getIdProducto(), 1, monto)) {
                    JOptionPane.showMessageDialog(this, "Plan contratado exitosamente");
                    txtMonto.setText("");
                    cargarTablaPlanes();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al contratar el plan");
                }
            }
        });

        btnRefrescar.addActionListener(e -> cargarTablaPlanes());

        btnCancelar.addActionListener(e -> {
            int fila = tablaPlanes.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Seleccione un plan"); return; }
            int idPlan = (int) tablaPlanes.getValueAt(fila, 0);
            int ok = JOptionPane.showConfirmDialog(this,
                "Cancelar el plan ID " + idPlan + "?",
                "Confirmar cancelacion", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            if (ok == JOptionPane.YES_OPTION) {
                if (planDAO.cancelar(idPlan)) {
                    JOptionPane.showMessageDialog(this, "Plan cancelado");
                    cargarTablaPlanes();
                }
            }
        });
    }

    private void actualizarInfoProducto() {
        ProductoFinanciero p = (ProductoFinanciero) cmbProducto.getSelectedItem();
        if (p != null) {
            lblTasa.setText(String.format("Tasa: %.2f%%", p.getTasaBase()));
            lblPlazo.setText("Plazo: " + p.getPlazoMeses() + " meses");
            simular();
        }
    }

    private void simular() {
        ProductoFinanciero p = (ProductoFinanciero) cmbProducto.getSelectedItem();
        if (p == null || txtMonto.getText().trim().isEmpty()) return;
        try {
            double monto = Double.parseDouble(txtMonto.getText().trim());
            double ganancia = p.calcularGanancia(monto);
            double total = monto + ganancia;
            lblGanancia.setText(String.format("Ganancia: S/. %.2f", ganancia));
            lblMontoFinal.setText(String.format("Monto final: S/. %.2f", total));
        } catch (NumberFormatException ignored) {}
    }

    private void cargarDatos() {
        // Cargar clientes
        cmbCliente.removeAllItems();
        for (Cliente c : clienteDAO.listarTodos())
            if (c.getEstado().equals("ACTIVO")) cmbCliente.addItem(c);

        // Cargar productos de inversion
        cmbProducto.removeAllItems();
        for (ProductoFinanciero p : productoDAO.listarPorTipo("INVERSION"))
            cmbProducto.addItem(p);

        cargarTablaPlanes();
        actualizarInfoProducto();
    }

    private void cargarTablaPlanes() {
        DefaultTableModel model = (DefaultTableModel) tablaPlanes.getModel();
        model.setRowCount(0);
        for (PlanInversion p : planDAO.listarTodos()) {
            model.addRow(new Object[]{
                p.getIdPlan(),
                p.getIdCliente(),
                p.getIdProducto() + " - CDT",
                String.format("S/. %.2f", p.getMontoInvertido()),
                p.getTasaPactada() + "%",
                p.getPlazoMeses() + " meses",
                p.getFechaVencimiento(),
                String.format("S/. %.2f", p.calcularGanancia()),
                p.getEstado()
            });
        }
    }
}
