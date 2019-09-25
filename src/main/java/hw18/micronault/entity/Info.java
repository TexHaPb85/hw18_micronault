package hw18.micronault.entity;


public class Info {
    private Long id;
    private String textInfo;
    private double value;

    public Info(Long id, String textInfo, double value) {
        this.id = id;
        this.textInfo = textInfo;
        this.value = value;
    }

    public Info() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTextInfo() {
        return textInfo;
    }

    public void setTextInfo(String textInfo) {
        this.textInfo = textInfo;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return  "id=" + id +
                ", textInfo=" + textInfo +
                ", value=" + value +
                '}';
    }
}
