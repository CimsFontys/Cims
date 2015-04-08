/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Protocol;

/**
 *
 * @author Merijn
 */
public class InvalidTypeException extends Exception {

    /**
     * Creates a new instance of <code>InvalidTypeException</code> without
     * detail message.
     */
    public InvalidTypeException() {
    }

    /**
     * Constructs an instance of <code>InvalidTypeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidTypeException(String msg) {
        super(msg);
    }
}
