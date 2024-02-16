import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DestroyPanel extends JDialog {
    private JPanel destroyPanel;
    private JButton signOut;
    private JButton cancelButton;
    private JTextField dPName;
    private JButton editButton;
    private JButton deleteButton;

    public DestroyPanel(JFrame parent) {
        super(parent);
        setTitle("Admin");
        setContentPane(destroyPanel);
        setMinimumSize(new Dimension(450, 475));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    signOut.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            LoginForm loginForm = new LoginForm(null);
            loginForm.setVisible(true);
        }
    });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = dPName.getText();
                try {
                    if(isUserExist(dPName.getText())){
                        dispose();
                        ChangingPanel changingForm = new ChangingPanel(null, name);
                        changingForm.setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(DestroyPanel.this,
                                "User " + name + " does not exist",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = dPName.getText();
                try {
                    if (isUserExist(name)) {
                        deleteUser(name);
                        JOptionPane.showMessageDialog(DestroyPanel.this,
                                "User " + name + " has been deleted successfully",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        AdminPanel adminPanel = new AdminPanel(null,null);
                        adminPanel.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(DestroyPanel.this,
                                "User " + name + " does not exist",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                AdminPanel adminForm = new AdminPanel(null,null);
                adminForm.setVisible(true);
            }
        });
    }
    private boolean isUserExist(String name) throws SQLException {
        Connection connection = DriverManager.getConnection(User.DB_URL, User.USERNAME, User.PASSWORD);
        String query = "SELECT * FROM user WHERE name = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    public void deleteUser(String name) throws SQLException {
        Connection connection = DriverManager.getConnection(User.DB_URL, User.USERNAME, User.PASSWORD);
        String query = "DELETE FROM user WHERE name =?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,name);
        statement.executeUpdate();
    }

}

