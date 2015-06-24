/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Mitchell
 */
public class Salt 
{
    private static Salt salt;
    
    private Salt()
    {}
    
    public static Salt getInstance()
    {
        if(salt == null)
        {
            salt = new Salt();
        }
        return salt;
    }
    
    public byte[] encryptObject(Object obj)
    {
        byte[] encrypted = null;
        try
        {
            String key1 = "1357975312468642";
            byte[] key2 = key1.getBytes();
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeySpec secret = new SecretKeySpec(key2, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);
            encrypted = cipher.doFinal(this.serialize(obj));
        }
        catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IOException | IllegalBlockSizeException | BadPaddingException ex)
        {}
        return encrypted;
    }
    
    public Object decryptObject(byte[] encrypted) throws IOException, ClassNotFoundException
    {
        Object obj = null;
        try
        {
            byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            String key1 = "1357975312468642";
            byte[] key2 = key1.getBytes();
            SecretKeySpec secret = new SecretKeySpec(key2, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, ivspec);
            byte[] decrypted = cipher.doFinal(encrypted);
            obj = this.deserialize(decrypted);
        }
        catch(NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex)
        {}
        return obj;
    }
    
    private byte[] serialize(Object obj) throws IOException 
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    
    private Object deserialize(byte[] data) throws IOException, ClassNotFoundException 
    {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
    
}
