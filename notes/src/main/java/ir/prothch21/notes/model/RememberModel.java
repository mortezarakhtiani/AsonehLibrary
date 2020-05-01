package ir.prothch21.notes.model;

public class RememberModel {
//            JSON.append({'title':i.title,'location':i.location,'des':i.des,'code':i.code,'year':i.year,'month':i.month,'day':i.day,'hour':i.hour,'minute':i.minute})
    private String title,location,des,code,year,month,day,hour,minute;

    public RememberModel(String title, String location, String des, String code, String year, String month, String day, String hour, String minute) {
        this.title = title;
        this.location = location;
        this.des = des;
        this.code = code;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDes() {
        return des;
    }

    public String getCode() {
        return code;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }
}
