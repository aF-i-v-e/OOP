package ru.praktika95.bot;

public class ParsingData {
    private String codeCategory;
    private DatePeriod datePeriod;

    public String getCodeCategory(){
        return codeCategory;
    }

    public DatePeriod getDatePeriod(){
        return datePeriod;
    }

    public void setCategory(String codeCategory){
        this.codeCategory = codeCategory;
    }

    public void setPeriod(DatePeriod datePeriod){
        this.datePeriod = datePeriod;
    }
}
