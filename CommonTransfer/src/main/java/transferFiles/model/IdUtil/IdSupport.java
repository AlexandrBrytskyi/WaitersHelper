package transferFiles.model.IdUtil;


import java.io.Serializable;

public class IdSupport implements Serializable {


    private int Id;

    public IdSupport() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public String toString() {
        return "Id=" + Id;
    }
}
