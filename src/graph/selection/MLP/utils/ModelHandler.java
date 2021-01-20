package graph.selection.MLP.utils;

import graph.selection.MLP.libs.Matrix;

import java.io.*;

public class ModelHandler
{
    public static boolean export(String path, double[] items)
    {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(new File(path));
            oos = new ObjectOutputStream(fos);

            oos.writeObject(items);
            System.out.println("EXPORTED");
            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean export(String path, Matrix[] items)
    {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(new File(path));
            oos = new ObjectOutputStream(fos);

            oos.writeObject(items);

            System.out.println("EXPORTED");
            return true;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static double[] loadArray (String path) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(new File(path));
            ois = new ObjectInputStream(fis);

            return (double[])  ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            return null;
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Matrix[] loadMatrix (String path) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(new File(path));
            ois = new ObjectInputStream(fis);

            return (Matrix[])  ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

            return null;
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
