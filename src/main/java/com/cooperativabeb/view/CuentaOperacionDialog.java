package com.cooperativabeb.view;

import com.cooperativabeb.dao.CuentaAhorroDAO;
import com.cooperativabeb.dao.TransaccionDAO;
import com.cooperativabeb.model.CuentaAhorro;
import com.cooperativabeb.model.Transaccion;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CuentaOperacionDialog extends JDialog {

    private CuentaAhorroDAO cuentaDAO = new CuentaAhorroDAO();
    private TransaccionDAO transaccionDAO = new TransaccionDAO();
    private CuentaAhorro cuentaActual;

    private JLabel lblNroCuenta, lblSaldo, lblTipo, lblEstado;
    private JTextField txtMonto, txtDescripcion;
    private JTable tablaHistorial;
    private JButton btnDepositar, btnRetirar, btnActualizar;

    public CuentaOperacionDialog(Frame parent, CuentaAhorro cuenta) {
        super(parent, "Operaciones de Cuenta", true);
        this.cuentaActual = cuenta;
        initComponents();
        cargarHistorial();
    }

    private void initComponents() {
        setSize(620, 580);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panel info cuenta
        JPanel panelInfo = new JPanel(new GridLayout(2, 4, 10, 8));
        panelInfo.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            "Informacion de la cuenta",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(100, 149, 237)));

        lblNroCuenta = new JLabel(cuentaActual.getNroCuenta());
        lblNroCuenta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblNroCuenta.setForeground(new Color(100, 149, 237));

        lblSaldo = new JLabel(String.format("S/. %.2f", cuentaActual.getSaldo()));
        lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSaldo.setForeground(new Color(80, 200, 120));

        lblTipo = new JLabel(cuentaActual.getTipoCuenta());
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        lblEstado = new JLabel(cuentaActual.getEstado());
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblEstado.setForeground(cuentaActual.getEstado().equals("ACTIVA") ?
            new Color(80, 200, 120) : Color.RED);

        Font fLbl = new Font("Segoe UI", Font.PLAIN, 12);
        JLabel l1 = new JLabel("Nro. cuenta:"); l1.setFont(fLbl);
        JLabel l2 = new JLabel("Saldo actual:"); l2.setFont(fLbl);
        JLabel l3 = new JLabel("Tipo:"); l3.setFont(fLbl);
        JLabel l4 = new JLabel("Estado:"); l4.setFont(fLbl);

        panelInfo.add(l1); panelInfo.add(lblNroCuenta);
        panelInfo.add(l2); panelInfo.add(lblSaldo);
        panelInfo.add(l3); panelInfo.add(lblTipo);
        panelInfo.add(l4); panelInfo.add(lblEstado);

        // Panel operacion
        JPanel panelOp = new JPanel(new GridBagLayout());
        panelOp.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            "Realizar operacion",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(100, 149, 237)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMonto = new JTextField(15);
        txtMonto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMonto.setPreferredSize(new Dimension(0, 35));
        txtMonto.putClientProperty("JTextField.placeholderText", "Ingrese el monto");

        txtDescripcion = new JTextField(15);
        txtDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescripcion.setPreferredSize(new Dimension(0, 35));
        txtDescripcion.putClientProperty("JTextField.placeholderText", "Descripcion opcional");

        gbc.gridx=0; gbc.gridy=0; gbc.weightx=0.3;
        JLabel lMonto = new JLabel("Monto (S/.):");
        lMonto.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelOp.add(lMonto, gbc);
        gbc.gridx=1; gbc.weightx=0.7;
        panelOp.add(txtMonto, gbc);

        gbc.gridx=0; gbc.gridy=1; gbc.weightx=0.3;
        JLabel lDesc = new JLabel("Descripcion:");
        lDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelOp.add(lDesc, gbc);
        gbc.gridx=1; gbc.weightx=0.7;
        panelOp.add(txtDescripcion, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        btnDepositar = new JButton("DEPOSITAR");
        btnRetirar   = new JButton("RETIRAR");
        btnActualizar = new JButton("Actualizar historial");

        btnDepositar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDepositar.setPreferredSize(new Dimension(130, 38));
        btnDepositar.setBackground(new Color(80, 200, 120));
        btnDepositar.setForeground(Color.WHITE);
        btnDepositar.setFocusPainted(false);
        btnDepositar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRetirar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRetirar.setPreferredSize(new Dimension(130, 38));
        btnRetirar.setBackground(new Color(220, 80, 80));
        btnRetirar.setForeground(Color.WHITE);
        btnRetirar.setFocusPainted(false);
        btnRetirar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnActualizar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnActualizar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBotones.add(btnDepositar);
        panelBotones.add(btnRetirar);
        panelBotones.add(btnActualizar);

        gbc.gridx=0; gbc.gridy=2; gbc.gridwidth=2;
        panelOp.add(panelBotones, gbc);

        // Tabla historial
        String[] cols = {"ID", "Tipo", "Monto", "Saldo anterior", "Saldo posterior", "Fecha"};
        tablaHistorial = new JTable(new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        });
        tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaHistorial.setRowHeight(26);
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JPanel panelHistorial = new JPanel(new BorderLayout());
        panelHistorial.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(100, 149, 237), 1),
            "Historial de transacciones",
            TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(100, 149, 237)));
        panelHistorial.add(new JScrollPane(tablaHistorial));

        // Layout principal
        JPanel panelCentro = new JPanel(new GridLayout(2, 1, 0, 10));
        panelCentro.add(panelOp);
        panelCentro.add(panelHistorial);

        panelPrincipal.add(panelInfo, BorderLayout.NORTH);
        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        add(panelPrincipal);

        // Acciones
        btnDepositar.addActionListener(e -> realizarOperacion("DEPOSITO"));
        btnRetirar.addActionListener(e -> realizarOperacion("RETIRO"));
        btnActualizar.addActionListener(e -> cargarHistorial());
    }

    private void realizarOperacion(String tipo) {
        String montoStr = txtMonto.getText().trim();
        if (montoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el monto", "Error",
                JOptionPane.WARNING_MESSAGE);
            txtMonto.requestFocus();
            return;
        }

        double monto;
        try {
            monto = Double.parseDouble(montoStr);
            if (monto <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Monto invalido", "Error",
                JOptionPane.WARNING_MESSAGE);
            txtMonto.requestFocus();
            return;
        }

        String desc = txtDescripcion.getText().trim();
        if (desc.isEmpty()) desc = tipo.equals("DEPOSITO") ?
            "Deposito en efectivo" : "Retiro en efectivo";

        int confirm = JOptionPane.showConfirmDialog(this,
            String.format("Confirmar %s de S/. %.2f?", tipo, monto),
            "Confirmar operacion", JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        boolean ok;
        if (tipo.equals("DEPOSITO")) {
            ok = cuentaDAO.depositar(cuentaActual.getIdCuenta(), monto, desc);
        } else {
            ok = cuentaDAO.retirar(cuentaActual.getIdCuenta(), monto, desc);
        }

        if (ok) {
            JOptionPane.showMessageDialog(this,
                tipo + " realizado exitosamente", "Exito",
                JOptionPane.INFORMATION_MESSAGE);
            txtMonto.setText("");
            txtDescripcion.setText("");
            // Recargar cuenta actualizada
            CuentaAhorro actualizada = cuentaDAO.buscarPorNro(cuentaActual.getNroCuenta());
            if (actualizada != null) {
                cuentaActual = actualizada;
                lblSaldo.setText(String.format("S/. %.2f", cuentaActual.getSaldo()));
            }
            cargarHistorial();
        } else {
            JOptionPane.showMessageDialog(this,
                tipo.equals("RETIRO") ? "Saldo insuficiente o error en la operacion"
                    : "Error al realizar la operacion",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarHistorial() {
        DefaultTableModel model = (DefaultTableModel) tablaHistorial.getModel();
        model.setRowCount(0);
        List<Transaccion> lista = transaccionDAO.listarPorCuenta(cuentaActual.getIdCuenta());
        for (Transaccion t : lista) {
            model.addRow(new Object[]{
                t.getIdTransaccion(),
                t.getTipo(),
                String.format("S/. %.2f", t.getMonto()),
                String.format("S/. %.2f", t.getSaldoAnterior()),
                String.format("S/. %.2f", t.getSaldoPosterior()),
                t.getFechaTransaccion()
            });
        }
    }
}
