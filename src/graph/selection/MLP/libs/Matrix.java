package graph.selection.MLP.libs;

import java.io.Serializable;
import java.util.Random;

public class Matrix implements Serializable {
    public int n_rows;
    public int n_cols;
    public double [][] matrix;

    /**
     * Constructor with null matrix
     * @param num_rows the number of rows
     * @param num_cols the number of columns
     * @return a null matrix with given dimensions
     */
    public Matrix (int num_rows,int num_cols)
    {
        this.n_cols = num_cols;
        this.n_rows = num_rows;
        this.matrix = new double[num_rows][num_cols];
        for (int i = 0 ; i<num_rows; i++)
        {
            for (int k = 0; k< num_cols; k++)
            {
                this.matrix[i][k] = 0;
            }
        }
    }

    /**
     * Constructor from array
     * @param input the array resulting in a row
     * @return a single column matrix
     */
    public Matrix (double [] input)
    {
        this.matrix = clone(input);
        this.n_rows = input.length;
        this.n_cols = 1;
    }
    /**
     * Constructor from array
     * @param matrix the double dimensions array
     * @return a matrix with the given values
     */
    public Matrix (double [][] matrix)
    {
        this.n_rows = matrix.length;
        this.n_cols = matrix[0].length;
        this.matrix = clone(matrix);
    }

    /**
     * method to clone the matrix
     * @param m the double dimensions array to copy
     * @return the double dimensions array copied
     */
    public double [][] clone (double [][] m)
    {
        double [][] result = new double[m.length][m[0].length];
        for (int i = 0; i< m.length; i++)
        {
            System.arraycopy(m[i], 0, result[i], 0, m[0].length);
        }
        return result;
    }
    /**
     * method to clone the matrix
     * @param m the Matrix to copy
     * @return a new Matrix with the double dimensions array copied
     */
    public static Matrix clone (Matrix m)
    {
        double [][] result = new double[m.n_rows][m.n_cols];
        for (int i = 0; i< m.n_rows; i++)
        {
            for (int k = 0;k< m.n_cols; k++)
            {
                result [i][k] = m.getMatrix()[i][k];
            }
        }
        return new Matrix(result);
    }
    /**
     * method to clone the matrix
     * @param m the single dimension array to copy
     * @return the double dimensions array copied
     */
    public double [][] clone (double [] m)
    {
        double [][] result = new double[m.length][1];
        for (int i = 0; i< m.length; i++)
        {
            result [i][0] = m[i];
        }
        return result;
    }

    /**
     * LA matrix transpose
     * @param m the matrix to transpose
     * @return a new matrix equal but transposed
     */
    public static Matrix transpose (Matrix m)
    {
        double [][] result = new double[m.n_cols][m.n_rows];
        for ( int i = 0; i< m.n_cols; i++)
        {
            for (int k = 0; k< m.n_rows; k++)
            {
                result[i][k] = m.getMatrix()[k][i];
            }
        }
        return new Matrix(result);
    }

    /**
     * method to assign a new matrix to the instance field
     * @param m the double dimensions array to set
     */
    public void setMatrix (double [][] m)
    {
        this.n_rows = m.length;
        this.n_cols = m[0].length;
        this.matrix = clone(m);
    }
    /**
     * method to assign a new matrix to the instance field
     * @param m the matrix to set
     */
    public void setMatrix (Matrix m)
    {
        this.n_rows = m.n_rows;
        this.n_cols = m.n_cols;
        this.matrix = clone(m.getMatrix());
    }

    /**
     * accessor method
     * @return the matrix of this field
     */
    public double [][] getMatrix ()
    {
        return this.matrix;
    }

    /**
     * static method for the dot product
     * @param n the left sided matrix
     * @param m the right sided matrix
     * @return a new matrix resulting from the dot product
     */
    public static Matrix product(Matrix n, Matrix m)
    {
        if (n.n_rows != m.n_rows|| n.n_cols != m.n_cols)
        {
            System.out.println(n.n_rows + " -> "+ m.n_rows);
            System.out.println(n.n_cols + " -> "+ m.n_cols);
            System.out.println("error");
            return null;
        }
        double [][] res = new double[n.n_rows][m.n_cols];
        for (int i = 0; i<n.n_rows; i++)
        {
            for ( int j = 0; j< n.n_cols; j++)
            {
                res[i][j] = n.getMatrix()[i][j] * m.getMatrix()[i][j];
            }
        }
        return new Matrix(res);
    }
    /**
     * instance method for the dot product
     * @param m the right sided matrix
     * @return a new matrix resulting from the dot product
     */
    public Matrix multiply(Matrix m)
    {
        if (m.n_rows == 1 && m.n_cols == 1)
        {
            return this.multiply(m.getMatrix()[0][0]);
        }
        if (this.n_rows != m.n_rows|| this.n_cols != m.n_cols)
        {
            System.out.println(this.n_rows + " -> "+ m.n_rows);
            System.out.println(this.n_cols + " -> "+ m.n_cols);
            System.out.println("error");
            return null;
        }
        double [][] res = new double[this.n_rows][m.n_cols];
        for (int i = 0; i<this.n_rows; i++)
        {
            for ( int j = 0; j< this.n_cols; j++)
            {
                res[i][j] = this.getMatrix()[i][j] * m.getMatrix()[i][j];
            }
        }
        return new Matrix(res);
    }

    /**
     * static method for the matrix multiplication
     * @param n the left side matrix
     * @param m the right side matrix
     * @return the product from the two in a 2-dim array
     */
    public static double [][] multiplyM (Matrix n, Matrix m)
    {
        if (n.n_cols != m.n_rows)
        {
            System.out.println("error");
            return null;
        }
        double [][] result = new double[n.n_rows][m.n_cols];
        for (int i = 0; i<n.n_rows; i++)
        {
            for ( int j = 0; j< m.n_cols; j++)
            {
                double sum = 0;
                for (int k = 0; k< n.n_cols; k++)
                {
                    sum += n.getMatrix()[i][k] * m.getMatrix()[k][j];
                }
                result [i][j] = sum;
            }
        }
        return result;
    }
    /**
     * static method for the matrix multiplication
     * @param n the left side matrix
     * @param m the right side matrix
     * @return the product from the two in a matrix
     */
    public static Matrix multiply (Matrix n, Matrix m)
    {
        if (n.n_cols != m.n_rows)
        {
            System.out.println("error");
            return null;
        }
        double [][] res = new double[n.n_rows][m.n_cols];
        for (int i = 0; i<n.n_rows; i++)
        {
            for ( int j = 0; j< m.n_cols; j++)
            {
                double sum = 0;
                for (int k = 0; k< n.n_cols; k++)
                {
                    sum += n.getMatrix()[i][k] * m.getMatrix()[k][j];
                }
                res [i][j] = sum;
            }
        }
        return new Matrix(res);
    }

    /**
     * mathod for the activation function
     * @param x the value to activate
     * @return the value activated
     */
    public static double activation (double x)
    {
        return 1/(1 + Math.exp(-x));
    }

    public static double tanHx(double x)
    {
        return Math.tanh(x);
    }

    public static Matrix tanh(Matrix m)
    {
        double [][] result = new double[m.n_rows][m.n_cols];
        for ( int i = 0; i< m.n_rows; i++)
        {
            for ( int k = 0; k< m.n_cols; k++)
            {
                result [i][k] = tanHx(m.getMatrix()[i][k]);
            }
        }
        return new Matrix(result);
    }

    /**
     * method to calculate the derivative of the matrix
     * @param m the given matrix
     * @return the new matrix with the derivative of the old one
     */
    public static Matrix dfunc (Matrix m)
    {
        double [][] result = new double[m.n_rows][m.n_cols];
        for ( int i = 0; i< m.n_rows; i++)
        {
            for ( int k = 0; k< m.n_cols; k++)
            {
                result [i][k] = m.getMatrix()[i][k] * (1 - m.getMatrix()[i][k]);
            }
        }
        return new Matrix(result);
    }

    /**
     * static method that performs the activation on a matrix
     * @param m the given matrix
     * @return a new matrix after each value has been activated
     */
    public static Matrix map(Matrix m)
    {
        double [][] result = new double[m.n_rows][m.n_cols];
        for (int i = 0; i<m.n_rows; i++)
        {
            for (int k = 0; k< m.n_cols; k++)
            {
                result[i][k] = activation(m.getMatrix()[i][k]);
            }
        }
        return new Matrix(result);
    }
    /**
     * instance method that performs the activation on a matrix
     */
    public void map()
    {
        double [][] res = new double[this.n_rows][this.n_cols];
        for (int i = 0; i<this.n_rows; i++)
        {
            for (int k = 0; k< this.n_cols; k++)
            {
                res[i][k] = activation(this.getMatrix()[i][k]);
            }
        }
        this.setMatrix(res);
    }


    /**
     * static method that performs a randomization of a matrix
     * @param m the old matrix
     * @return a new Matrix randomized from -1 to 1
     */
    public static Matrix randomize(Matrix m)
    {
        double [][] res = new double [m.n_rows][m.n_cols];
        for (int i = 0; i< m.n_rows; i++)
        {
            for ( int k = 0; k< m.n_cols; k++)
            {
                res[i][k] =new Random().nextDouble();
                if(new Random().nextDouble()<0.5)
                {
                    res[i][k] *=-1;
                }
            }
        }
        return new Matrix(res);
    }

    /**
     * instance method to print the matrix
     */
    public void printMatrix ()
    {
        //System.out.println(this.n_rows+" -> "+ this.n_cols);
        for (int i = 0; i< this.n_rows; i++)
        {
            for ( int k = 0; k< this.n_cols; k++)
            {
                System.out.print(this.matrix[i][k]+ "   ");
            }
            System.out.println();
        }
    }

    /**
     * instance method to print the matrix
     */
    public String getPrintMatrix ()
    {
        String matrix = "";
        for (int i = 0; i< this.n_rows; i++)
        {
            for ( int k = 0; k< this.n_cols; k++)
            {
                matrix += (this.matrix[i][k]+ "   ");
            }
            matrix += "\n";
        }
        return matrix;
    }

    /**
     * static method to multiply a matrix by a single value
     * @param m the given matrix
     * @param value the value to add
     * @return a new Matrix after the evaluation
     */
    public static Matrix scaleMatrix (Matrix m, double value)
    {
        double [][] res = new double [m.n_rows][m.n_cols];
        for (int i = 0; i< m.n_rows; i++)
        {
            for ( int k = 0; k< m.n_cols; k++)
            {
                res [i][k] = m.matrix[i][k] * value;
            }
        }
        return new Matrix(res);
    }
    /**
     * instance method to multiply a matrix by a single value
     * @param value the value to add
     * @return a new Matrix after the evaluation
     */
    public Matrix multiply ( double value)
    {
        double [][] res = new double [this.n_rows][this.n_cols];
        for (int i = 0; i< this.n_rows; i++)
        {
            for ( int k = 0; k< this.n_cols; k++)
            {
                res [i][k] = this.getMatrix()[i][k] * value;
            }
        }
        return new Matrix(res);
    }

    /**
     * static method to perform a matrix subtraction
     * @param n the left side matrix
     * @param m the right side matrix
     * @return a new matrix after the subtraction
     */
    public static Matrix subtract (Matrix n , Matrix m)
    {
        if (n.n_rows!= m.n_rows||n.n_cols!= m.n_cols)
        {
            System.out.println("error");
            return null;
        }
        double [][] result =  new double[n.n_rows][n.n_cols];
        for (int i = 0; i< n.n_rows; i++)
        {
            for ( int k = 0; k< n.n_cols; k++)
            {
                result[i][k] = n.getMatrix()[i][k] - m.getMatrix()[i][k];
            }
        }
        return new Matrix(result);
    }


    //--------------------------------------------------------//

    //for later usage methods

    //-------------------------------------------------------//

    /**
     * method to subtract a value from a matrix
     * @param value the value to subtract
     */
    public void subtract (double value){
        for (int i = 0; i< this.n_rows; i++)
        {
            for ( int k = 0; k< this.n_cols; k++)
            {
                this.matrix[i][k] -= value;
            }
        }
    }

    /**
     * static method to perform a matrix subtraction
     * @param n the left side matrix
     * @param m the right side matrix
     * @return a 2-dim array after the subtraction
     */
    public static double [][] subtract (double [][] n , double [][] m)
    {
        if (n.length!= m.length||n[0].length!= m[0].length)
        {
            System.out.println("error");
            return null;
        }
        double [][] result = new double[n.length][n[0].length];
        for (int i = 0; i< n.length; i++)
        {
            for ( int k = 0; k< n[0].length; k++)
            {
                result [i][k] = n[i][k] - m[i][k];
            }
        }
        return result;
    }
    /**
     * method to subtract a matrix from a matrix
     * @param m the matrix to subtract
     */
    public void subtract (Matrix m)
    {

        if (this.n_rows!= m.n_rows||this.n_cols!= m.n_cols)
        {
            System.out.println("error");
            return ;
        }
        for (int i = 0; i< this.n_rows; i++)
        {
            for ( int k = 0; k< this.n_cols; k++)
            {
                this.matrix[i][k] -= m.matrix[i][k];
            }
        }
    }

    /**
     * static method to perform a matrix addition
     * @param n the left side matrix
     * @param m the right side matrix
     * @return a 2-dim array after the addition
     */
    public static double [][] add (double [][] m, double [][] n)
    {

        if (m.length!=n.length&& m[0].length!= n[0].length)
        {
            System.out.println("error");
            return null;
        }
        for (int i = 0; i< m.length; i++)
        {
            for ( int k = 0; k< m[0].length; k++)
            {
                m[i][k] += n[i][k];
            }
        }
        return m;
    }

    /**
     * static method to perform a matrix addition with a single value
     * @param value the value to add
     * @return a 2-dim array after the addition
     */
    public double [][] add (double value)
    {

        double [][] res  = new double [this.n_rows][this.n_cols];

        for (int i = 0; i< this.n_rows; i++)
        {
            for ( int k = 0; k< this.n_cols; k++)
            {

                res[i][k] = this.getMatrix()[i][k] + value;
            }
        }
        return res;
    }
    /**
     * LA matrix transpose
     * a new matrix equal is setted but transposed
     */
    public void transpose()
    {
        Matrix result = new Matrix(this.n_cols,this.n_rows);
        for ( int i = 0; i< this.n_cols; i++)
        {
            for (int k = 0; k< this.n_rows; k++)
            {
                result.matrix[i][k] = this.getMatrix()[k][i];
            }
        }
        setMatrix(result.getMatrix());
    }

    /**
     * static method that combines @method dfunc and map
     * @param m the matrix that comes activated and derivated
     */

    public static Matrix ddfunc(Matrix m)
    {
        double [][] res = new double[m.n_rows][m.n_cols];
        for ( int i = 0; i< m.n_rows; i++)
        {
            for ( int k = 0; k< m.n_cols; k++)
            {
                res [i][k] = (activation(m.getMatrix()[i][k])*(1-activation(m.getMatrix()[i][k])));
            }
        }
        return new Matrix(res);
    }

    /**
     * instance method to perform a matrix addition
     * @param m the matrix to add
     */
    public void add (Matrix m)
    {

        if (this.n_rows!= m.n_rows||this.n_cols!= m.n_cols)
        {
            System.out.println("error");
            return;
        }

        double [][] res = new double[n_rows][n_cols];
        if (m.n_rows== 1 && m.n_cols ==1 )
        {
            res = this.add(m.getMatrix()[0][0]);
            this.setMatrix(res);
            return;
        }
        else
            for (int i = 0; i< this.n_rows; i++)
            {
                for ( int k = 0; k< this.n_cols; k++)
                {
                    res [i][k]= this.getMatrix()[i][k] + m.getMatrix()[i][k];
                }
            }
        this.setMatrix(res);
    }

    /**
     * static method to perform a matrix addition
     * @param n the left side matrix
     * @param m the right side matrix
     * @return a new matrix after the addition
     */
    public static Matrix add (Matrix n , Matrix m)
    {
        if (n.n_rows!= m.n_rows||n.n_cols!= m.n_cols)
        {
            System.out.println("error");
            return null;
        }
        double [][] res =  new double[n.n_rows][n.n_cols];
        for (int i = 0; i< n.n_rows; i++)
        {
            for ( int k = 0; k< n.n_cols; k++)
            {
                res[i][k] = n.getMatrix()[i][k] + m.getMatrix()[i][k];
            }
        }
        return new Matrix(res);
    }
}
