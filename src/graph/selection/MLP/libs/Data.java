package graph.selection.MLP.libs;

public class Data {
    double [] input;
    double [] target;
    public Data(double [] inputs, double [] target )
    {
        this.input = inputs;
        this.target = target;
    }

    public double [] getInput()
    {
        return this.input;
    }
    public double [] getTarget()
    {
        return this.target;
    }
}
