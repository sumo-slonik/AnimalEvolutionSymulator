package Symulation;

public class SelectedAnimalObserver
{
    private int newLivingChildren;
    private int newLivingDescendants;
    private int allNewChildren;
    private int allNewDescendants;
    public SelectedAnimalObserver()
    {
        this.newLivingChildren = 0;
        this.newLivingDescendants =0;
        this.allNewChildren = 0;
        this.allNewDescendants = 0;
    }
    public void addChild()

    {
        this.allNewChildren++;
        this.newLivingChildren++;
    }
    public void addDescendants()
    {
        this.allNewDescendants++;
        this.newLivingDescendants++;
    }
    public void killChild()
    {
        this.newLivingChildren--;
    }
    public void killDescendants()
    {
        this.newLivingDescendants--;
    }
    public int getNumberOfLivingChildren()
    {
        return this.newLivingChildren;
    }
    public int getNumberOfLivingDescendants()
    {
        return this.newLivingDescendants;
    }
    public int getAllNewChildren()
    {
        return this.allNewChildren;
    }
    public int getAllNewDescendants()
    {
        return allNewDescendants;
    }
    public void reset()
    {
        this.newLivingChildren = 0;
        this.newLivingDescendants =0;
        this.allNewChildren = 0;
        this.allNewDescendants = 0;
    }


}
