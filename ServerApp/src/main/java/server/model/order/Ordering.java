package server.model.order;

import server.model.denomination.Denomination;
import server.model.fund.Fund;
import server.model.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
public class Ordering implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    @Column(nullable = false)
    private LocalDateTime dateOrderCreated;

    @Column(nullable = true)
    private LocalDateTime dateClientsCome;

    @Column(nullable = true)
    private int amountOfPeople;

    @Column(nullable = true)
    private String description;

    @ManyToOne()
    @JoinColumn(name = "user_taken_id", referencedColumnName = "id")
    private User whoTakenOrder;


    @ManyToOne()
    @JoinColumn(name = "user_serving_id", referencedColumnName = "id")
    private User whoServesOrder;


    @OneToOne(targetEntity = Fund.class, mappedBy = "order", cascade = {CascadeType.REMOVE})
    private Fund fund;


    @Column
    @Enumerated
    private OrderType type;


    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Denomination> denominations;

    public Ordering() {
    }


    public int getAmountOfPeople() {
        return amountOfPeople;
    }

    public void setAmountOfPeople(int amountOfPeople) {
        this.amountOfPeople = amountOfPeople;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateOrderCreated() {
        return dateOrderCreated;
    }

    public void setDateOrderCreated(LocalDateTime dateOrderCreated) {
        this.dateOrderCreated = dateOrderCreated;
    }

    public LocalDateTime getDateClientsCome() {
        return dateClientsCome;
    }

    public void setDateClientsCome(LocalDateTime dateClientsCome) {
        this.dateClientsCome = dateClientsCome;
    }

    public User getWhoTakenOrder() {
        return whoTakenOrder;
    }

    public void setWhoTakenOrder(User whoTakenOrder) {
        this.whoTakenOrder = whoTakenOrder;
    }

    public User getWhoServesOrder() {
        return whoServesOrder;
    }

    public void setWhoServesOrder(User whoServesOrder) {
        this.whoServesOrder = whoServesOrder;
    }

    public Fund getFund() {
        return fund;
    }

    public void setFund(Fund fund) {
        this.fund = fund;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public List<Denomination> getDenominations() {
        return denominations;
    }

    public void setDenominations(List<Denomination> denominations) {
        this.denominations = denominations;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Ordering{" +
                "id=" + id +
                ", dateOrderCreated=" + dateOrderCreated +
                ", dateClientsCome=" + dateClientsCome +
                ", amountOfPeople=" + amountOfPeople +
                ", description='" + description + '\'' +
                ", whoTakenOrder=" + whoTakenOrder +
                ", whoServesOrder=" + whoServesOrder +
                ", fund=" + fund +
                ", type=" + type +
                ", denominations=" + denominations +
                '}';
    }
}
