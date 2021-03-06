package org.kevoree.sample.producerconsumer.callback;

import org.kevoree.annotation.*;
import org.kevoree.api.Callback;
import org.kevoree.api.CallbackResult;
import org.kevoree.api.Port;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

/**
 * Created with IntelliJ IDEA.
 * User: gregory.nain
 * Date: 28/11/2013
 * Time: 17:43
 */

@ComponentType
public class FrameComponent {

    private JFrame mainFrame;
    private JTextField textField;
    private JButton send;

    @Output
    private Port textEntered;


    @Start
    public void start() {

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    mainFrame = new JFrame("Enter your text:");
                    mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    mainFrame.getContentPane().setLayout(new FlowLayout());

                    textField = new JTextField(20);
                    send = new JButton("Send");

                    send.addActionListener(new ActionListener() { //Listen to the send button.
                        public void actionPerformed(ActionEvent e) { //on click
                            textEntered.send(textField.getText(), new Callback() {
                                public void onSuccess(CallbackResult callbackResult) {
                                    String response = (Boolean.valueOf(callbackResult.getPayload()) ? "Answered Yes." : "Answered No.");
                                    JOptionPane.showMessageDialog(mainFrame, response, "Message Read!", JOptionPane.INFORMATION_MESSAGE);
                                }

                                public void onError(Throwable throwable) {

                                }
                            });
                        }
                    });

                    mainFrame.getContentPane().add(textField);
                    mainFrame.getContentPane().add(send);

                    mainFrame.pack();
                    mainFrame.setVisible(true);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Stop
    public void stop() {
        try {
            if(mainFrame != null) {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        mainFrame.dispose();
                        mainFrame = null;
                    }
                });
            }
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
