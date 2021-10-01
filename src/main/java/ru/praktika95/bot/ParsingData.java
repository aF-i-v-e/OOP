package ru.praktika95.bot;

public class ParsingData {
    private int codeCategory;
    private DatePeriod datePeriod;

    public int getCodeCategory(){
        return codeCategory;
    }

    public DatePeriod getDatePeriod(){
        return datePeriod;
    }

    public void setCategory(int codeCategory){
        this.codeCategory = codeCategory;
    }

    public void setPeriod(DatePeriod datePeriod){
        this.datePeriod = datePeriod;
    }
}
