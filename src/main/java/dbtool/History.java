package dbtool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {
    private int id;
    private String lat;
    private String lnt;
    private String searchDttm;
}
