import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class PerpustakaanApp extends JFrame {

    JTabbedPane tabPane;

    JTextField txtNama, txtAlamat, txtHP;
    JTable tableAnggota;
    DefaultTableModel modelAnggota;

    JTextField txtJudul, txtPenulis, txtTahun;
    JTable tableBuku;
    DefaultTableModel modelBuku;

    Connection conn;

    public PerpustakaanApp() {

        setTitle("Aplikasi Perpustakaan");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        connectDatabase();

        tabPane = new JTabbedPane();

        tabPane.add("Data Anggota", panelAnggota());
        tabPane.add("Data Buku", panelBuku());

        add(tabPane);

        tampilAnggota();
        tampilBuku();

        setVisible(true);
    }

    private void connectDatabase() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/perpustakaan",
                    "root",
                    ""
            );

            System.out.println("Database Connected");

        } catch(Exception e) {

            JOptionPane.showMessageDialog(null,
                    "Database gagal connect\\n" + e.getMessage());
        }
    }

    private JPanel panelAnggota() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4,2,10,10));
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        txtNama = new JTextField();
        txtAlamat = new JTextField();
        txtHP = new JTextField();

        form.add(new JLabel("Nama"));
        form.add(txtNama);

        form.add(new JLabel("Alamat"));
        form.add(txtAlamat);

        form.add(new JLabel("No HP"));
        form.add(txtHP);

        JPanel btnPanel = new JPanel();

        JButton btnTambah = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnHapus = new JButton("Hapus");
        JButton btnReset = new JButton("Reset");

        btnPanel.add(btnTambah);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);
        btnPanel.add(btnReset);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(btnPanel, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);

        modelAnggota = new DefaultTableModel();

        modelAnggota.addColumn("ID");
        modelAnggota.addColumn("Nama");
        modelAnggota.addColumn("Alamat");
        modelAnggota.addColumn("No HP");

        tableAnggota = new JTable(modelAnggota);

        panel.add(new JScrollPane(tableAnggota), BorderLayout.CENTER);

        btnTambah.addActionListener(e -> tambahAnggota());
        btnUpdate.addActionListener(e -> updateAnggota());
        btnHapus.addActionListener(e -> hapusAnggota());
        btnReset.addActionListener(e -> resetAnggota());

        tableAnggota.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                int row = tableAnggota.getSelectedRow();

                txtNama.setText(modelAnggota.getValueAt(row,1).toString());
                txtAlamat.setText(modelAnggota.getValueAt(row,2).toString());
                txtHP.setText(modelAnggota.getValueAt(row,3).toString());
            }
        });

        return panel;
    }

    private JPanel panelBuku() {

        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(4,2,10,10));
        form.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        txtJudul = new JTextField();
        txtPenulis = new JTextField();
        txtTahun = new JTextField();

        form.add(new JLabel("Judul Buku"));
        form.add(txtJudul);

        form.add(new JLabel("Penulis"));
        form.add(txtPenulis);

        form.add(new JLabel("Tahun Terbit"));
        form.add(txtTahun);

        JPanel btnPanel = new JPanel();

        JButton btnTambah = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnHapus = new JButton("Hapus");
        JButton btnReset = new JButton("Reset");

        btnPanel.add(btnTambah);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnHapus);
        btnPanel.add(btnReset);

        JPanel top = new JPanel(new BorderLayout());
        top.add(form, BorderLayout.CENTER);
        top.add(btnPanel, BorderLayout.SOUTH);

        panel.add(top, BorderLayout.NORTH);

        modelBuku = new DefaultTableModel();

        modelBuku.addColumn("ID");
        modelBuku.addColumn("Judul");
        modelBuku.addColumn("Penulis");
        modelBuku.addColumn("Tahun");

        tableBuku = new JTable(modelBuku);

        panel.add(new JScrollPane(tableBuku), BorderLayout.CENTER);

        btnTambah.addActionListener(e -> tambahBuku());
        btnUpdate.addActionListener(e -> updateBuku());
        btnHapus.addActionListener(e -> hapusBuku());
        btnReset.addActionListener(e -> resetBuku());

        tableBuku.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

                int row = tableBuku.getSelectedRow();

                txtJudul.setText(modelBuku.getValueAt(row,1).toString());
                txtPenulis.setText(modelBuku.getValueAt(row,2).toString());
                txtTahun.setText(modelBuku.getValueAt(row,3).toString());
            }
        });

        return panel;
    }

    private void tampilAnggota() {

        modelAnggota.setRowCount(0);

        try {

            String sql = "SELECT * FROM anggota";

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {

                modelAnggota.addRow(new Object[]{

                        rs.getInt("id_anggota"),
                        rs.getString("nama"),
                        rs.getString("alamat"),
                        rs.getString("no_hp")
                });
            }

        } catch(Exception e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void tampilBuku() {

        modelBuku.setRowCount(0);

        try {

            String sql = "SELECT * FROM buku";

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(sql);

            while(rs.next()) {

                modelBuku.addRow(new Object[]{

                        rs.getInt("id_buku"),
                        rs.getString("judul"),
                        rs.getString("penulis"),
                        rs.getString("tahun_terbit")
                });
            }

        } catch(Exception e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void tambahAnggota() {

        try {

            String sql = "INSERT INTO anggota(nama,alamat,no_hp) VALUES(?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtNama.getText());
            ps.setString(2, txtAlamat.getText());
            ps.setString(3, txtHP.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    "Anggota berhasil ditambah");

            tampilAnggota();
            resetAnggota();

        } catch(Exception e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void updateAnggota() {

        try {

            int row = tableAnggota.getSelectedRow();

            int id = Integer.parseInt(
                    modelAnggota.getValueAt(row,0).toString());

            String sql = "UPDATE anggota SET nama=?, alamat=?, no_hp=? WHERE id_anggota=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtNama.getText());
            ps.setString(2, txtAlamat.getText());
            ps.setString(3, txtHP.getText());
            ps.setInt(4, id);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    "Data anggota berhasil diupdate");

            tampilAnggota();
            resetAnggota();

        } catch(Exception e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void hapusAnggota() {

        try {

            int row = tableAnggota.getSelectedRow();

            int id = Integer.parseInt(
                    modelAnggota.getValueAt(row,0).toString());

            String sql = "DELETE FROM anggota WHERE id_anggota=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    "Data anggota berhasil dihapus");

            tampilAnggota();
            resetAnggota();

        } catch(Exception e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void resetAnggota() {

        txtNama.setText("");
        txtAlamat.setText("");
        txtHP.setText("");
    }

    private void tambahBuku() {

        try {

            String sql = "INSERT INTO buku(judul,penulis,tahun_terbit) VALUES(?,?,?)";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtJudul.getText());
            ps.setString(2, txtPenulis.getText());
            ps.setString(3, txtTahun.getText());

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    "Buku berhasil ditambah");

            tampilBuku();
            resetBuku();

        } catch(Exception e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void updateBuku() {

        try {

            int row = tableBuku.getSelectedRow();

            int id = Integer.parseInt(
                    modelBuku.getValueAt(row,0).toString());

            String sql = "UPDATE buku SET judul=?, penulis=?, tahun_terbit=? WHERE id_buku=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, txtJudul.getText());
            ps.setString(2, txtPenulis.getText());
            ps.setString(3, txtTahun.getText());
            ps.setInt(4, id);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    "Buku berhasil diupdate");

            tampilBuku();
            resetBuku();

        } catch(Exception e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void hapusBuku() {

        try {

            int row = tableBuku.getSelectedRow();

            int id = Integer.parseInt(
                    modelBuku.getValueAt(row,0).toString());

            String sql = "DELETE FROM buku WHERE id_buku=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(null,
                    "Buku berhasil dihapus");

            tampilBuku();
            resetBuku();

        } catch(Exception e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void resetBuku() {

        txtJudul.setText("");
        txtPenulis.setText("");
        txtTahun.setText("");
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new PerpustakaanApp();
        });
    }
}