import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
public class ChangingPanel extends JDialog {
    private JButton exitting;
    private JTextField cPEmail;
    private JPasswordField cPPassword;
    private JButton OKButton;
    private JButton cancelButton;
    private JTextField cPPhone;
    private JTextField cPAddress;
    private JTextField cPName;
    private JPanel changingPanel;

    public ChangingPanel(JFrame parent, String name){
        super(parent);
        setTitle("Change");
        setContentPane(changingPanel);
        setMinimumSize(new Dimension(550,675));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            cPName.setText(name);
            try {
                Connection conn = DriverManager.getConnection(User.DB_URL, User.USERNAME, User.PASSWORD);
                String sql = "SELECT * FROM user WHERE name = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, name);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");
                    String address = resultSet.getString("address");
                    String password = resultSet.getString("password");

                    cPEmail.setText(email);
                    cPPhone.setText(phone);
                    cPAddress.setText(address);
                    cPPassword.setText(password);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        exitting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginForm loginForm = new LoginForm(null);
                loginForm.setVisible(true);
            }
        });
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nameToChange = cPName.getText();
                String email = cPEmail.getText();
                String phone = cPPhone.getText();
                String address = cPAddress.getText();
                String password = String.valueOf(cPPassword.getPassword());

                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(ChangingPanel.this,
                            "All fields are required",
                            "Try again", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        Connection conn = DriverManager.getConnection(User.DB_URL, User.USERNAME, User.PASSWORD);
                        String sql = "UPDATE user SET name = ?, email = ?, phone = ?, address = ?, password = ? WHERE name = ?";
                        PreparedStatement statement = conn.prepareStatement(sql);
                        statement.setString(1, nameToChange);
                        statement.setString(2, email);
                        statement.setString(3, phone);
                        statement.setString(4, address);
                        statement.setString(5, password);
                        statement.setString(6, name);

                        int rowsUpdated = statement.executeUpdate();
                        if (rowsUpdated > 0) {
                            JOptionPane.showMessageDialog(ChangingPanel.this,
                                    "User has been updated successfully!",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            LoginForm loginForm = new LoginForm(null);
                            loginForm.setVisible(true);
                        }
                        conn.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               dispose();
                LoginForm loginForm = new LoginForm(null);
                loginForm.setVisible(true);
            }
        });
    }


}
