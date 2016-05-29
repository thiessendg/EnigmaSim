package enigma.test;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import enigma.morse.*;;


/*
 * EIN ANWENDUNGSBEISPIEL DER MORSE KLASSE FÜR DEN DANIEL 
 * WENN NICHT MEHR BENOETIGT BITTE WIEDER LOESCHEN
 * JUNIT TEST KOMMT NOCH
 */
@SuppressWarnings("serial")
public class MorseTest extends JFrame implements ActionListener{
	Morse morse;
	JButton start = new JButton("start");
	JButton stop = new JButton("stop");
	JTextArea area = new JTextArea(15,15);
	MorseSound s= null;
	String code ;
	
	public MorseTest (Morse m, String s){
		morse = m;
		code = s;
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(200,200);
		setTitle("Morse");
		
		Container cp = getContentPane();
		cp.setLayout(new FlowLayout());
		cp.add(start);
		cp.add(stop);
		cp.add(area);
		
		stop.addActionListener(this);
		start.addActionListener(this);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		//m.start();
		Morse m = new Morse("HALLO");
		System.out.println(m.getMorseCode());
		m.setPlainTxt("HALLOWIEGEHTSDENNSO");
		System.out.println(m.getMorseCode());
		new MorseTest(m, m.getMorseCode());
	}


	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==start){
			if (s == null){
				s = new MorseSound(code);
				s.start();
			}
		}else{
			if (s != null) s.done();
			s = null;
		}
	}
}


