/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cobalt.itidez.derputils.messageutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author iTidez
 */
public class SLAPI
{
	public static void save(Object obj, File file) throws Exception
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file, true));
            oos.writeObject(obj);
            oos.flush();
            oos.close();
        }
        public static Object load(File file) throws Exception
        {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            Object result = ois.readObject();
            ois.close();
            return result;
        }
}
