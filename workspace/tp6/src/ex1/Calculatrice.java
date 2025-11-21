package ex1;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Calculatrice extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calculatrice frame = new Calculatrice();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Calculatrice() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(true);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNombre_1 = new JLabel("Nombre2");
		lblNombre_1.setBounds(62, 218, 56, 17);
		lblNombre_1.setBackground(new Color(0, 0, 0));
		contentPane.add(lblNombre_1);

		JLabel lblResultat = new JLabel("Resultat");
		lblResultat.setBounds(323, 161, 50, 17);
		lblResultat.setBackground(Color.WHITE);
		contentPane.add(lblResultat);

		JLabel lblNombre = new JLabel("Nombre1");
		lblNombre.setBounds(62, 119, 56, 17);
		lblNombre.setBackground(new Color(0, 0, 0));
		contentPane.add(lblNombre);

		JLabel lblCalculatrice = new JLabel("Calculatrice");
		lblCalculatrice.setBounds(189, 65, 79, 49);
		lblCalculatrice.setForeground(new Color(255, 0, 0));
		lblCalculatrice.setBackground(Color.WHITE);
		contentPane.add(lblCalculatrice);

		textField = new JTextField();
		textField.setBounds(36, 148, 114, 21);
		contentPane.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setBounds(36, 247, 114, 21);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		textField_2 = new JTextField();
		textField_2.setBounds(294, 190, 114, 21);
		contentPane.add(textField_2);
		textField_2.setColumns(10);

		JButton btnNewButton = new JButton("Calculer");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Double nbr1;
				Double nbr2;
				
				try {
					nbr1 = Double.parseDouble(textField.getText());
					nbr2 = Double.parseDouble(textField_1.getText());
					textField_2.setText("" + (nbr1 + nbr2));
				} catch (Exception ex) {
					ex.printStackTrace();
					ex.printStackTrace();
				    JOptionPane.showMessageDialog(null, 
				        "Veuillez saisir des nombres valides!", 
				        "Erreur", 
				        JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton.setBounds(177, 187, 105, 27);
		contentPane.add(btnNewButton);

	}
}
