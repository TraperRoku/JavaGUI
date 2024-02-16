import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog{
    private JTextField tfEmail;
    private JPasswordField tfPassword;
    private JButton OKButton;
    private JButton CancelButton;
    private JPanel loginPanel;
    private JButton clickMeButton;
    private boolean isAdmin = false;


    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450,475));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = tfEmail.getText();
                String password = String.valueOf(tfPassword.getPassword());

                User user = getAuthenticatedUser(email, password);

                if (user != null) {
                    if(isAdmin){
                        dispose();
                        AdminPanel adminForm = new AdminPanel(null, user);
                        adminForm.setVisible(true);


                    }else{
                    isAdmin = false;
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Welcome " + user.getName() + "!",
                            "Welcome", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    HelloPanel helloForm = new HelloPanel(null, null,user);
                    helloForm.setVisible(true);
                    }

                } else {
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or Password Invalid",
                            "Try again", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        clickMeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                RegisterPanel registerForm = new RegisterPanel(null);
                registerForm.setVisible(true);
            }
        });
        CancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });
        setVisible(true);
    }
    private User getAuthenticatedUser(String email, String password) {
        User user = null;

        try {
            Connection connection = DriverManager.getConnection(User.DB_URL, User.USERNAME, User.PASSWORD);
            String query = "SELECT * FROM user WHERE email = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                user.setAddress(resultSet.getString("address"));
                user.setPassword(resultSet.getString("password"));
            }
            if (user != null && user.getEmail().equals("FilipKazmierczak@wp.pl")) {
                isAdmin = true;
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        new LoginForm(null);
    }

}
