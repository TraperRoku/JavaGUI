import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegisterPanel extends JDialog{
    private JPanel registerPanel;
    private JTextField rPEmail;
    private JPasswordField rPPassword;
    private JButton OKButton;
    private JButton CancelButton;
    private JTextField rPName;
    private JTextField rPPhone;
    private JTextField rPAddress;
    private JButton loggingButton;

    public RegisterPanel(JFrame parent){
        super(parent);
        setTitle("Register");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(550,675));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        loggingButton.addActionListener(new ActionListener() {
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
                String name = rPName.getText();
                String email = rPEmail.getText();
                String phone = rPPhone.getText();
                String address = rPAddress.getText();
                String password = String.valueOf(rPPassword.getPassword());

                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPhone(phone);
                user.setAddress(address);
                user.setPassword(password);

                if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getPhone().isEmpty() ||
                        user.getAddress().isEmpty() || user.getPassword().isEmpty()) {
                    JOptionPane.showMessageDialog(registerPanel,
                            "All fields are required",
                            "Try again", JOptionPane.ERROR_MESSAGE);
                } else {
                    try {
                        Connection connection = DriverManager.getConnection(User.DB_URL, User.USERNAME, User.PASSWORD);

                        if (isUnique(user.getEmail(), user.getPhone(), connection)) {

                            String query = "INSERT INTO user (name, email, phone, address, password) VALUES (?, ?, ?, ?, ?)";
                            PreparedStatement statement = connection.prepareStatement(query);
                            statement.setString(1, name);
                            statement.setString(2, email);
                            statement.setString(3, phone);
                            statement.setString(4, address);
                            statement.setString(5, password);
                            statement.executeUpdate();

                            JOptionPane.showMessageDialog(registerPanel,
                                    "User registered successfully",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                            LoginForm loginForm = new LoginForm(null);
                            loginForm.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(registerPanel,
                                    "Email or Phone already exists",
                                    "Try again", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                        }
                    }


        });
        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
    private boolean isUnique(String email, String phone, Connection connection) throws SQLException {
        String query = "SELECT * FROM user WHERE email = ? OR phone = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, email);
        statement.setString(2, phone);
        ResultSet resultSet = statement.executeQuery();

        boolean unique = !resultSet.next();

        resultSet.close();
        statement.close();

        return unique;
    }

}
