package urlConnectionClasses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonAutoDetect
@JsonIgnoreProperties({"Cur_ID","Cur_Abbreviation"})
public class Rate {
    @JsonFormat(pattern = "yyy-MM-dd")
    public Date Date;
    public double Cur_Scale;
    public String Cur_Name;
    public double Cur_OfficialRate;
    Rate(){}

    @Override
    public String toString() {
        return "Rate{" +
                "Date='" + Date + '\'' +
                ", Cur_Scale=" + Cur_Scale +
                ", Cur_Name='" + Cur_Name + '\'' +
                ", Cur_OfficialRate=" + Cur_OfficialRate +
                '}';
    }
}
