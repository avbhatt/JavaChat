import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimpleChatClient {
	PrintWriter pw;
	Socket sock;
	InputStreamReader isr;
	BufferedReader br;
	JTextArea area;
	String username;

	public static void main(String[] args) {
		SimpleChatClient cli = new SimpleChatClient();
		cli.start();
		//pw.close();
	}

	public void setUpNetworking() {
		try{
			sock = new Socket("127.0.0.1", 60038);
			//sock = new Socket("172.16.76.128", 60038);
			pw = new PrintWriter(sock.getOutputStream());
			isr = new InputStreamReader(sock.getInputStream());
			br = new BufferedReader(isr);
			System.out.println("established");

			//System.out.println(read.readLine());
			//pw.close();
		} catch (Exception ex) {}

	}

	public void start() {
		JFrame frame = new JFrame("SimpChat");
		JTextField field = new JTextField(20);
		/*JTextArea */area = new JTextArea(15,25);
		JPanel panel = new JPanel();
		JScrollPane scroll = new JScrollPane(area);

		username = (String)JOptionPane.showInputDialog(
				frame,
				"Enter Username:",
				"SimpChat",
				JOptionPane.PLAIN_MESSAGE);

		// Anonymous actionlistener to send text
		ActionListener listen = new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				try {
					pw.println(username + ": " + field.getText());
					pw.flush();
				} catch (Exception ex) {
				}
				field.setText("");
				field.requestFocus();
			}
		};



		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setEditable(false);

		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		field.addActionListener(listen);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.add(scroll);
		frame.getContentPane().add(BorderLayout.CENTER, panel);
		panel.add(field);
		JButton send = new JButton("Send");
		send.addActionListener(listen);
		panel.add(send);
		setUpNetworking();

		//Thread t = new Thread(new Incoming());
		Thread t = new Thread( /*new Runnable() {
			@Override
			public void run() {
				String message = null;
				try {
					while ((message = br.readLine()) != null)
						area.append(message + "\n");
				} catch (Exception ex) {ex.printStackTrace();}
			}
		});*/() -> {
			String message = null;
			try {
				while ((message = br.readLine()) != null)
					area.append(message + "\n");
			} catch (NullPointerException np) {
				area.setText("Server not online");
			}
			catch (Exception ex) {ex.printStackTrace();}
		});

		t.start();

		frame.setSize(350,310);
		frame.setVisible(true);


	}

	/*
	public class Incoming implements Runnable {
		@Override
		public void run() {
			try {
				String message = br.readLine();
				while (true) {
					if (message == null) break;
					area.append(message + "\n");
				}
			} catch (Exception ex) {ex.printStackTrace();}
		}
	}

	/*public class SendListener implements ActionListener {
		public void actionPerformed(ActionEvent a) {
			try {
				pw.write(field.getText() + "\n");
				pw.flush();
			} catch (Exception ex) {}
			field.setText("");
			field.requestFocus();
		}
	}*/
}