package chatapplication;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class chatWindow extends JFrame implements ActionListener
{
	private JTextField sendingMessage;
	private JTextArea messageArea;
	 private JButton sendButton;
	 
	 String receiver;
    
	String loginUser;
	public chatWindow(String loginUser, String receiver) throws SQLException
	{
		
		super("Chat Window");
                System.out.println("chatapplication.chatWindow.<init>()");
		this.loginUser=loginUser;
		this.receiver=receiver;
		System.out.println(receiver);
		JPanel j=new JPanel();
		j.setBackground(Color.white);
		sendingMessage=new JTextField();		
		sendingMessage.addActionListener(this);
		sendingMessage.setBounds(0, 400, 300, 40);
		sendingMessage.setBackground(Color.YELLOW);
		messageArea=new JTextArea();
		messageArea.setBounds(0, 0, 600, 400);
		
		messageArea.setBackground(Color.white);
		JScrollPane scrollPane = new JScrollPane(messageArea); 
		scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(250, 250));
		sendButton=new JButton("Send");
		sendButton.addActionListener(this);
		sendButton.setBounds(400, 400, 100, 100);
		add(sendButton);
		add(messageArea);
		add(sendingMessage);
		add(j);
	    setSize(600, 600);
	    setVisible(true);
	    receiveMessages();
	}
	
	
	
	
	
	
	private void receiveMessages() throws SQLException {
            System.out.println("in receive message"+loginUser);
		sqlConnection s=new sqlConnection();
		Connection con=s.connectSqlDb();
		Thread t=new Thread()
		{
			public void run()
			{
				while(true)
				{
					
					 Statement stmt;
					try {
						stmt = con.createStatement();
                                                
						String query = "select * from"+" "+loginUser+";";
                                                
		                ResultSet rs = stmt.executeQuery(query);
		                while (rs.next()) {
                                    Securechat s=new Securechat();
                                    String query1 = "select * from users;";
                                    PreparedStatement st1 = con.prepareStatement(query1);
                                     ResultSet rs2 = st1.executeQuery(query1);
                                     
                                     while(rs2.next())
                                     {
                                         if(rs2.getObject("user_name").toString().equals(loginUser))
                                        {
                                            System.out.println(rs2.getObject("private_key").toString().trim());
                                            System.out.println(rs.getObject(3).toString());
                                       String decryptedMessage= s.decrypt(rs.getObject(3).toString(),rs2.getObject("private_key").toString().trim());
		                	messageArea.append(rs.getObject(2)+":"+decryptedMessage+"\n");
		                	String query2="DELETE FROM"+" "+ loginUser+" "+"WHERE"+"  ID = "+" "+rs.getObject(1)+";";
		                	PreparedStatement st2 = con.prepareStatement(query2);
		                	st2.executeUpdate();
                                        }
                                     }
		                }               
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception ex) {
                                        Logger.getLogger(chatWindow.class.getName()).log(Level.SEVERE, null, ex);
                                    }
		                
				}
			}
		};
		t.start();			
		
	}






	public static void main(String args[]) throws SQLException
	{
		new chatWindow(null,null);
		//c.setVisible(true);
	}






	@Override
	public void actionPerformed(ActionEvent e) {
           
		if(e.getActionCommand().trim().equals("Send"))
		{
                    
			if(sendingMessage.getText()!=null&&sendingMessage.getText()!=" ")
			{
                            System.out.println(sendingMessage.getText());
			   try {	
                            sqlConnection s=new sqlConnection();
				Connection con=s.connectSqlDb();
                                Securechat securechat=new Securechat();
                                String query1 = "select * from users ;";
                                Statement st = con.createStatement();
                                ResultSet rec = st.executeQuery(query1);
                               //ResultSet rs = stmt.executeQuery(command);
                              
          
            while (rec.next())
            {
                System.out.println(rec.getObject(1).toString());
        
                if(rec.getObject("user_name").toString().equals(receiver))
                {
                 String public_key;
                public_key = rec.getObject("public_key").toString();
                System.out.println("public key"+public_key);
                
                                String message=securechat.secureMessage(sendingMessage.getText(), public_key);
				messageArea.append("me:"+sendingMessage.getText()+"\n");
				String query="INSERT INTO"+" "+ receiver+" "+"(user,message)"+" "+
				"VALUES("+"\""+loginUser+"\",\""+message+"\");";
				System.out.println(query);
				PreparedStatement p;
				
					p = con.prepareStatement(query);
                           
                                p.executeUpdate();
                            
					con.close();
					sendingMessage.setText("");
                }
                      }                   
                        
				} catch (SQLException e1) {
                                    System.out.println(e1.getMessage());
					// TODO Auto-generated catch block
                                        } catch (IOException ex) {
                                Logger.getLogger(chatWindow.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvalidKeySpecException ex) {
                                Logger.getLogger(chatWindow.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                Logger.getLogger(chatWindow.class.getName()).log(Level.SEVERE, null, ex);
                            }
				
				
				
			}
		}
		
	}
}