package json;

/**
 * Created by Adam on 11/22/2015.
 */
public class Rows
{
    private Elements[] elements;

    public Elements[] getElements ()
    {
        return elements;
    }

    public void setElements (Elements[] elements)
    {
        this.elements = elements;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [elements = "+elements+"]";
    }
}
