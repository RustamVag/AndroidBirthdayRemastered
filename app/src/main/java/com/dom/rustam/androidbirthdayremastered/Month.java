package com.dom.rustam.androidbirthdayremastered;

public class Month {

    private String name;

    private int number;

    private int count;

    public String getName()
    {
        return this.name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public int getNumber()
    {
        return this.number;
    }
    public void setNumber(int number)
    {
        this.number = number;

        switch (number) {
            case 1:  this.name = "Январь"; this.count = 31;
                break;
            case 2:  this.name = "Февраль"; this.count = 28;
                break;
            case 3:  this.name = "Март"; this.count = 31;
                break;
            case 4:  this.name = "Апрель"; this.count = 30;
                break;
            case 5:  this.name = "Май"; this.count = 31;
                break;
            case 6:  this.name = "Июнь"; this.count = 30;
                break;
            case 7: this.name = "Июль"; this.count = 31;
                break;
            case 8:  this.name = "Август"; this.count = 31;
                break;
            case 9:  this.name = "Сентябрь"; this.count = 30;
                break;
            case 10: this.name = "Октябрь"; this.count = 31;
                break;
            case 11: this.name = "Ноябрь"; this.count = 30;
                break;
            case 12: this.name = "Декабрь"; this.count = 31;
                break;
        }

    }

    public int getCount()
    {
        return this.count;
    }
    public void setCount(int count)
    {
        this.count = count;
    }

    public Month(int number)
    {
        setNumber(number);
    }

    public Month()
    {
        setNumber(1);
    }

}

