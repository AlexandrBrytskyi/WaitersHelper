package brytskyi.waitershelperclient.app.activities.work;


import android.widget.TextView;
import transferFiles.model.denomination.Denomination;

public class DenomTimeWrapper {

    private Denomination denomination;
    private String time;
    private TextView view;

    public DenomTimeWrapper(Denomination denomination, String time) {
        this.denomination = denomination;
        this.time = time;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public TextView getView() {
        return view;
    }

    public void setView(TextView view) {
        this.view = view;
    }

    public void setDenomination(Denomination denomination) {
        this.denomination = denomination;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
