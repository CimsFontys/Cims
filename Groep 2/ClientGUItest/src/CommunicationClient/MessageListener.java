/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package CommunicationClient;

import Protocol.Message;

/**
 *
 * @author Michael
 */
public interface MessageListener {
    public void proces(Message message);
    
}
