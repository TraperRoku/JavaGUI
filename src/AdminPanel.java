import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminPanel extends JDialog {
    private JPanel AdminPanel;
    private JButton changeItButton;
    private JButton signOut;

    public AdminPanel(JFrame parent, User user) {
        super(parent);
        setTitle("Admin");
        setContentPane(AdminPanel);
        setMinimumSize(new Dimension(450,475));
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
        changeItButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
               DestroyPanel destroyForm = new DestroyPanel(null);
                destroyForm.setVisible(true);
            }
        });
    }
}
