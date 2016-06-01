package me.nlt.bavm.teams;

import java.util.ArrayList;

public abstract class Manager<T extends Manageable>
{
    protected ArrayList<T> manageables;

    public Manager()
    {
        manageables = new ArrayList<>();
    }

    public abstract void loadManageables();

    public abstract void saveManageables();

    public abstract void generateManageables();

    public T getManageable(int ID)
    {
        for (T object : manageables)
        {
            if (object.getID() == ID)
            {
                return object;
            }
        }

        return null;
    }
}
